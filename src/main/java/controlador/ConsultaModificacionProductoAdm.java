package controlador;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import modelo.Producto;
import modelo.Pedido;

@WebServlet("/ConsultaModificacionProductoAdm")
public class ConsultaModificacionProductoAdm extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DataSource pool;

    public ConsultaModificacionProductoAdm() {
        super();
    }

    public void init() throws ServletException {
        try {
            InitialContext ctx = new InitialContext();
            pool = (DataSource) ctx.lookup("java:comp/env/jdbc/customers");
            if (pool == null) {
                throw new ServletException("DataSource desconocida 'customers'");
            }
        } catch (NamingException ex) {
            throw new ServletException(ex);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idPedidoString = request.getParameter("idPedido");

        try {
            int idPedidoInt = Integer.parseInt(idPedidoString);

            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet resultadoConsulta = null;

            try {
                con = pool.getConnection();

                String consultaSQL = "SELECT * FROM productos WHERE idProducto = ?";
                pstmt = con.prepareStatement(consultaSQL);
                pstmt.setInt(1, idPedidoInt);

                resultadoConsulta = pstmt.executeQuery();

                if (resultadoConsulta.next()) {
                    int idPedido = resultadoConsulta.getInt("idProducto");
                    String nombreProducto = resultadoConsulta.getString("nombreProducto");
                    double precioProducto = resultadoConsulta.getDouble("precioProducto");
                    int cantidadProducto = resultadoConsulta.getInt("cantidadProducto");
                    InputStream foto = resultadoConsulta.getBinaryStream("imagenProducto");
                    // Crear un objeto Pedidos con los datos recuperados
                    Producto productos = new Producto(idPedido, nombreProducto, precioProducto, cantidadProducto,foto, pool,0,0);

                    // Almacenar el objeto Pedidos en la sesión
                    HttpSession session = request.getSession(true);
                    session.setAttribute("Productos", productos);

                    // Redireccionar a la página de modificación de pedido
                    request.getRequestDispatcher("ModificacionProductoAdm.jsp").forward(request, response);
                } else {
                    // Manejar el caso donde no se encontró ningún pedido con el ID dado
                    // Por ejemplo, redireccionar a una página de error
                }
            } catch (SQLException e) {
                // Manejar las excepciones de SQL
                e.printStackTrace();
            } finally {
                // Cerrar recursos
                if (resultadoConsulta != null) {
                    try {
                        resultadoConsulta.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (pstmt != null) {
                    try {
                        pstmt.close();
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
        } catch (NumberFormatException e) {
            // Manejar el caso donde el ID de pedido no es un número válido
            // Por ejemplo, redireccionar a una página de error
            e.printStackTrace();
        }
    }
}
