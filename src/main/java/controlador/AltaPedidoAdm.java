package controlador;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import modelo.Pedido;
@WebServlet("/AltaPedido")
public class AltaPedidoAdm extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DataSource pool;
Pedido pedido = new Pedido();
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String ruta = "ConsultaPedidoAdm.jsp";
        String sql = "";
        Connection con = null;
        PreparedStatement prestmt = null;
        String PrecioPedidoString = request.getParameter("pedidoPrecio");
        String[] partes = PrecioPedidoString.split("\\.");
        if (request.getParameter("btnEnviar").equals("enviar")) {
            String idPedidoString = request.getParameter("idUsuario");
            
            String fechaPedidoString = request.getParameter("fechaPedido");

            if (idPedidoString.equals("")) {
                request.setAttribute("errMessage", "No puedes dejar el id del usuario vacío");
                RequestDispatcher rd = request.getRequestDispatcher("AltaPedidoAdm.jsp");
				rd.forward(request, response);
            } else if (PrecioPedidoString.equals("")) {
                request.setAttribute("errMessage", "No puedes dejar el precio vacío");
                RequestDispatcher rd = request.getRequestDispatcher("AltaPedidoAdm.jsp");
				rd.forward(request, response);
            } else if (fechaPedidoString.equals("")) {
                request.setAttribute("errMessage", "No puedes dejar la fecha vacía");
                RequestDispatcher rd = request.getRequestDispatcher("AltaPedidoAdm.jsp");
				rd.forward(request, response);
            }else if(pedido.buscarUsuario(idPedidoString)==false){
            	request.setAttribute("errMessage", "El usuario no existe");
            	RequestDispatcher rd = request.getRequestDispatcher("AltaPedidoAdm.jsp");
				rd.forward(request, response);
            }else  if (partes.length > 1 && partes[1].length() > 2) {
    	        request.setAttribute("errMessage", "El precio no puede tener más de dos decimales");
    	        RequestDispatcher rd = request.getRequestDispatcher("AltaPedidoAdm.jsp");
				rd.forward(request, response);
    	    }else  if (PrecioPedidoString.length() >=6) {
    	        request.setAttribute("errMessage", "El precio no puede tener más de 5 dígitos");
    	        RequestDispatcher rd = request.getRequestDispatcher("AltaPedidoAdm.jsp");
				rd.forward(request, response);
    	    }else if (pedido.esNumero(PrecioPedidoString)==false) {
    	    	request.setAttribute("errMessage", "Tienes que ingresar números y no letras o carácteres especiales");
    	        RequestDispatcher rd = request.getRequestDispatcher("AltaPedidoAdm.jsp");
				rd.forward(request, response);

    	    }else {
                try {
                    con = pool.getConnection();
                    sql = "INSERT INTO pedidos(idUsuarioFK1, fechaPedido, pedidoPrecio) VALUES(?,?,?)";
                    prestmt = con.prepareStatement(sql);
                    int idPedidoInt = Integer.parseInt(idPedidoString);
                    double pedidoPrecio = Double.parseDouble(PrecioPedidoString);
                    prestmt.setInt(1, idPedidoInt);
                    prestmt.setString(2, fechaPedidoString);
                    prestmt.setDouble(3, pedidoPrecio);
                    int sentencia = prestmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    request.setAttribute("errMessage", "Error al acceder a la base de datos");
                } finally {
                    try {
                        if (prestmt != null) {
                            prestmt.close();
                        }
                        if (con != null) {
                            con.close();
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

       // RequestDispatcher rd = request.getRequestDispatcher(ruta);
        //rd.forward(request, response);
        
        response.sendRedirect(ruta);
        
    }
}




