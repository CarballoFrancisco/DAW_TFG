package controlador;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Servlet implementation class EliminarPedido
 */
@WebServlet("/EliminarPedidoAdm")
public class EliminarPedidoAdm extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataSource pool;  
   
	
	  public void init(ServletConfig config) throws ServletException {
	        try {
	        	super.init(config);
	            InitialContext ctx = new InitialContext();
	            pool = (DataSource) ctx.lookup("java:comp/env/jdbc/customers");
	            if (pool == null) {
	                throw new ServletException("DataSource desconocida 'customers'");
	            }
	        } catch (NamingException ex) {
	            ex.printStackTrace();
	            throw new ServletException(ex);
	        }
	    }
	
    public EliminarPedidoAdm() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)  
		// TODO Auto-generated method stub
		
		
	     throws ServletException, IOException {
	         String pedidoIdParam = request.getParameter("pedidoId");

	         if (pedidoIdParam == null || pedidoIdParam.isEmpty()) {
	             // Manejar el caso en el que el ID del pedido es nulo o vacío
	             response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID del pedido no válido");
	             return;
	         }

	         try (Connection con = pool.getConnection();
	                 PreparedStatement prestmt = con.prepareStatement("DELETE FROM pedidos WHERE idpedido=?")) {

	             prestmt.setInt(1, Integer.parseInt(pedidoIdParam));
	             prestmt.executeUpdate();
	             response.sendRedirect("ConsultaPedidoAdm.jsp"); // Redirigir después de la eliminación

	         } catch (SQLException e) {
	             e.printStackTrace();
	             response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error en la base de datos");
	             response.sendRedirect("ConsultaPedidoAdm.jsp");
	         } catch (NumberFormatException e) {
	             e.printStackTrace();
	             response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID del pedido no válido");
	             response.sendRedirect("ConsultaPedidoAdm.jsp");
	             
	         }
    }	
	
}


