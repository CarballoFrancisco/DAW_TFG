package controlador;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet("/EliminarUsuarioAdm")
public class EliminarUsuarioAdm extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DataSource pool;

    public EliminarUsuarioAdm() {
        super();
    }

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

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    {
    	
    	String userIdParam = request.getParameter("userId");
        String sql = "DELETE FROM users WHERE idusers=?";
        Connection con = null;
        PreparedStatement prestmt = null;

        try {
         
            if (userIdParam == null || userIdParam.isEmpty()) {
            	
            }

            con = pool.getConnection();
            prestmt = con.prepareStatement(sql);
            prestmt.setInt(1, Integer.parseInt(userIdParam));
            prestmt.executeUpdate();

        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        } 
    }
}

