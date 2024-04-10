package controlador;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import modelo.Usuario;

@WebServlet("/Login")
public class Login extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private DataSource pool;

    public Login() {
        super();
    }

    public void init(ServletConfig config) throws ServletException {
        try {
            InitialContext ctx = new InitialContext();
            pool = (DataSource) ctx.lookup("java:comp/env/jdbc/customers");
            if (pool == null) {
                throw new ServletException("DataSource desconocida 'customers'");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuario = null;
        String idUsuarioString = request.getParameter("idusers");
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        String passwordMD5 = Usuario.md5encryp(password);

        Connection con = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            con = pool.getConnection();
            statement = con.createStatement();
            resultSet = statement.executeQuery("select idusers, username, password, role from users;");

            while (resultSet.next()) {
            	int idUsuario = resultSet.getInt("idusers");
                String userNameDB = resultSet.getString("username");
                String passwordDB = resultSet.getString("password");
                String roleDB = resultSet.getString("role");

                if (userName.equals(userNameDB) && passwordMD5.equals(passwordDB)) {
                    usuario = new Usuario();
                    usuario.setUserName(userNameDB);
                    usuario.setId(idUsuario);
                    usuario.setPassword(passwordDB);
                    usuario.setRole(roleDB);

                    if (roleDB.equals("Adm")) {
                        HttpSession session = request.getSession(false);
                        if (session != null) {
                            session.invalidate();
                        }
                        session = request.getSession(true);
                        session.setAttribute("Usuario", usuario);
                        session.setAttribute("Adm", roleDB);
                        request.getRequestDispatcher("PaginaPrincipalDeAdm.jsp").forward(request, response);
                        return;
                        
                    } else if (roleDB.equals("User")) {
                    	
                        HttpSession session = request.getSession(false);
                        if (session != null) {
                            session.invalidate();
                        }
                        session = request.getSession(true);
                        session.setAttribute("Usuario2", usuario);
                        session.setAttribute("User",roleDB);
                        request.getRequestDispatcher("PaginaPrincipalUsuario.jsp").forward(request, response);
                        return;
                    }
                }
            }
            // Si llegamos aquí, las credenciales no coinciden con ningún usuario en la base de datos
            String mensaje = "Error en el usuario o contraseña";
			request.setAttribute("mensaje", mensaje);				
			RequestDispatcher rd = request.getRequestDispatcher("registro.jsp");	
            request.getRequestDispatcher("Login.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
