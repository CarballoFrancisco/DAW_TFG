package controlador;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import modelo.Pedido;
import modelo.Producto;
import modelo.Usuario;

@WebServlet("/CarroDeComprasComprarUsuario")
public class CarroDeComprasComprarUsuario extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private DataSource pool;

    public void init() throws ServletException {
        try {
            InitialContext ctx = new InitialContext();
            pool = (DataSource) ctx.lookup("java:comp/env/jdbc/customers");
            if (pool == null) {
                throw new ServletException("DataSource desconocida 'customers'");
            }
        } catch (NamingException ex) {
            throw new ServletException("Error al buscar el DataSource", ex);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection con = null;
        PreparedStatement prestmt = null;
        HttpSession session = request.getSession();
        Pedido pedido = new Pedido();
        Usuario usuario = (Usuario) session.getAttribute("Usuario2");
        ArrayList<Producto> carritoEnSesion = (ArrayList<Producto>) session.getAttribute("carrito");
        double precioTotal = (double) session.getAttribute("precioTotal");
        int idUsuario = usuario.getId();

        String fecha = obtenerFechaActual();
        
        try {
            con = pool.getConnection();
            String sql = "INSERT INTO `customers`.`pedidos` (`idUsuarioFK1`, `fechaPedido`, `pedidoPrecio`) VALUES(?,?,?)";
            prestmt = con.prepareStatement(sql);
            prestmt.setInt(1, idUsuario);
            prestmt.setString(2, fecha);
            prestmt.setDouble(3, precioTotal);
            prestmt.executeUpdate();
        } catch (SQLException e) {
            throw new ServletException("Error al realizar la inserción en la base de datos", e);
        } finally {
            cerrarConexion(con, prestmt);
        }
        
        try {
            int idUltimoPedido = pedido.idUltimoPedido();
            con = pool.getConnection();
            
            for (Producto producto : carritoEnSesion) {
                String sqlLineaPedido = "INSERT INTO `customers`.`lineapedido` (`idPedidoFK2`, `idProductoFK3`,`lineaPedidoCantidadProducto`, `lineaPedidoPrecio`) VALUES(?,?,?,?)";
                prestmt = con.prepareStatement(sqlLineaPedido);
                prestmt.setInt(1, idUltimoPedido);
                prestmt.setInt(2, producto.getIdProducto()); // Cambiar por el ID del producto
                prestmt.setInt(3, producto.getCantidadPedidaDeProductoEnCarritoSolicitada()); // Cambiar por la cantidad del producto
                prestmt.setDouble(4, producto.getPrecioIndividial()); // Cambiar por el precio del producto
                prestmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ServletException("Error al realizar la inserción en la tabla lineapedido", e);
        } catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
        try {
            con = pool.getConnection();
            
            for (Producto producto : carritoEnSesion) {
                int cantidadRestante = producto.getCantidadProducto() - producto.getCantidadPedidaDeProductoEnCarritoSolicitada();
                int idProducto = producto.getIdProducto();
                String sqlLineaPedido = "UPDATE `customers`.`productos` SET `cantidadProducto` = ? WHERE `idProducto` = ?";
                prestmt = con.prepareStatement(sqlLineaPedido);
                prestmt.setInt(1, cantidadRestante);
                prestmt.setInt(2, idProducto);
                prestmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ServletException("Error al realizar la actualización en la tabla productos", e);
        } finally {
            cerrarConexion(con, prestmt);
            limpiarSesion(session);
        }
    }

    private String obtenerFechaActual() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }

    private void cerrarConexion(Connection con, PreparedStatement prestmt) {
        if (prestmt != null) {
            try {
                prestmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void limpiarSesion(HttpSession session) {
        session.removeAttribute("carrito");
        session.removeAttribute("precioTotal");
    }
}


