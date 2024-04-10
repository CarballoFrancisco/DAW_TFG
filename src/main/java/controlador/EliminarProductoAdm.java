package controlador;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class EliminarProducto
 */
@WebServlet("/EliminarProductoAdm")
public class EliminarProductoAdm extends HttpServlet {
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
	            throw new ServletException(ex);
	        }
	    }
	
    public EliminarProductoAdm() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String pedidoIdParam = request.getParameter("productoId");	
		
		Connection con;
		try {
			
			con = pool.getConnection();
			 PreparedStatement prestmt = con.prepareStatement("DELETE FROM productos WHERE idProducto=?");
			  prestmt.setInt(1, Integer.parseInt(pedidoIdParam));
	          prestmt.executeUpdate();
	          
	          
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		 
	}

}
