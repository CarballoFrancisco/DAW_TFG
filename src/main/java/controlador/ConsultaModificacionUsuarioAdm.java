package controlador;

import modelo.Usuario;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

@WebServlet("/ConsultaModificacionUsuarioAdm")
public class ConsultaModificacionUsuarioAdm extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DataSource pool;

    public ConsultaModificacionUsuarioAdm() {
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
            throw new ServletException(ex);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idUsuario = request.getParameter("userId");

        try {
            
            int idUsuarioInt = Integer.parseInt(idUsuario);

            try (Connection con = pool.getConnection()) {
                String consultaSQL = "SELECT * FROM users WHERE idusers = ?";
                
                // Preparar y ejecutar la consulta
                try (PreparedStatement pstmt = con.prepareStatement(consultaSQL)) {
                    pstmt.setInt(1, idUsuarioInt);

                    // Obtener el resultado de la consulta
                    try (ResultSet resultadoConsulta = pstmt.executeQuery()) {
                        if (resultadoConsulta.next()) { // Verificar si hay resultados
                            int id = resultadoConsulta.getInt("idusers");
                            String userName = resultadoConsulta.getString("userName");
                            String password = resultadoConsulta.getString("password");
                            String role = resultadoConsulta.getString("role");
                            String email = resultadoConsulta.getString("email");
                            String nombreYApellidos = resultadoConsulta.getString("nombreYApellidos");
        	                String direccion = resultadoConsulta.getString("direccion");
        	                Usuario usuario = new Usuario(userName, password, role, email, pool, id,nombreYApellidos, direccion);
                          
                            // Almacenar el usuario en la sesión
                            HttpSession session = request.getSession(true);
                            session.setAttribute("usuario", usuario);

                           
                            // Redirigir a la página de destino
                            request.getRequestDispatcher("ModificacionUsuarioAdm.jsp").forward(request, response);
                        } else {
                            // No se encontraron resultados
                            // Puedes redirigir a una página de error o manejarlo de otra manera
                            response.sendRedirect("paginaDeError.jsp");
                        }
                    }
                }
            }
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            // Puedes redirigir a una página de error o manejarlo de otra manera
            response.sendRedirect("paginaDeError.jsp");
        }
    }
}