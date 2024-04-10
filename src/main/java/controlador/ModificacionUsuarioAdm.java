package controlador;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import modelo.Usuario;

@WebServlet("/ModificacionUsuarioAdm")
public class ModificacionUsuarioAdm extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DataSource pool;

    public ModificacionUsuarioAdm() {
        super();
    }

    public void init(ServletConfig config) throws ServletException {
        try {
            InitialContext ctx = new InitialContext();
            pool = (DataSource) ctx.lookup("java:comp/env/jdbc/customers");
            if (pool == null) {
                throw new ServletException("DataSource desconocida 'customers'");
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String ruta = "";
        String sql = "";
        Connection con = null;
        PreparedStatement prestmt = null;
        String error;
        Usuario usuario = new Usuario();

            if (request.getParameter("btnEnviar").equals("enviar")) {

    			String nombre = (request.getParameter("username"));
    			
    			String email = (request.getParameter("email"));
    			
    			String contrasena = (request.getParameter("password"));
    			
    			String confirmarContrasena =(request.getParameter("confirmPassword"));
    			
    			String nombreYApellidos = (request.getParameter("nombreYApellidos"));
    			
                String direccion = (request.getParameter("direccion"));
                
                
                
    			int id = Integer.parseInt(request.getParameter("userId"));

    			if (nombre.equals("")) {
    				request.setAttribute("errMessage", error = "No puedes dejar el nombre vacio");
    				RequestDispatcher rd = request.getRequestDispatcher("ModificacionUsuarioAdm.jsp");
    				rd.forward(request, response);

    			}else if (nombreYApellidos.equals("")) {
    				request.setAttribute("errMessage", error = "No puedes dejar el campo del nombre y apellidos vacios");
    				RequestDispatcher rd = request.getRequestDispatcher("ModificacionUsuarioAdm.jsp");
    				rd.forward(request, response);
    				
    			}else if (direccion .equals("")) {
    				request.setAttribute("errMessage", error = "No puedes dejar la dirección vacía");
    				RequestDispatcher rd = request.getRequestDispatcher("ModificacionUsuarioAdm.jsp");
    				rd.forward(request, response);
    				
    			}else if (nombreYApellidos.length()>25) {
    				request.setAttribute("errMessage", error = "El campo nombre y apellidos no puede tener más de 25 letras");
    				RequestDispatcher rd = request.getRequestDispatcher("ModificacionUsuarioAdm.jsp");
    				rd.forward(request, response);
    				
    			}else if (direccion.length()>25) {
    				request.setAttribute("errMessage", error = "La dirección no puede tener más de 25 letras");
    				RequestDispatcher rd = request.getRequestDispatcher("ModificacionUsuarioAdm.jsp");
    				rd.forward(request, response);
    				
    			}else if( Usuario.validacion(nombreYApellidos)==true) {
    				
    				request.setAttribute("errMessage", error = "El nombre y los apellidos no pueden contener carácteres especiales");
    				RequestDispatcher rd = request.getRequestDispatcher("ModificacionUsuarioAdm.jsp");
    				rd.forward(request, response);
    				
    			}else if( Usuario.validacion(direccion)==true) {
    				
    				request.setAttribute("errMessage", error = "La dirección no puede contener carácteres especiales");
    				RequestDispatcher rd = request.getRequestDispatcher("ModificacionUsuarioAdm.jsp");
    				rd.forward(request, response);
    				
    			} else if (nombre.length() < 4) {
    				request.setAttribute("errMessage", error = "El campo del nombre tiene que tener más de cuantro letras");
    				RequestDispatcher rd = request.getRequestDispatcher("ModificacionUsuarioAdm.jsp");
    				rd.forward(request, response);

    			} else if (nombre.length() > 12) {
    				request.setAttribute("errMessage", error = "El campo del nombre no puede tener más de 12 letras");
    				RequestDispatcher rd = request.getRequestDispatcher("ModificacionUsuarioAdm.jsp");
    				rd.forward(request, response);

    			}else if( Usuario.validacion(nombre)==true) {
    				
    				request.setAttribute("errMessage", error = "El nombre de usuario no puede contener carácteres especiales");
    				RequestDispatcher rd = request.getRequestDispatcher("ModificacionUsuarioAdm.jsp");
    				rd.forward(request, response);
    				
    			}else if(Usuario.contieneEspacios(nombre)==true){
    				
    				request.setAttribute("errMessage", error = "El nombre de usuario no puede contener espacios");
    				RequestDispatcher rd = request.getRequestDispatcher("ModificacionUsuarioAdm.jsp");
    				rd.forward(request, response);
    				
    			}else if(Usuario.contieneComillas(nombre)==true){
    				
    				request.setAttribute("errMessage", error = "El nombre de usuario no puede contener carácteres especiales");
    				RequestDispatcher rd = request.getRequestDispatcher("ModificacionUsuarioAdm.jsp");
    				rd.forward(request, response);
    					
    			}else if(request.getParameter("email")==" "){
    				
    				request.setAttribute("errMessage", error = "El campo email no puede contener espacios");
    				RequestDispatcher rd = request.getRequestDispatcher("ModificacionUsuarioAdm.jsp");
    				rd.forward(request, response);
    				
    				
    			}else if(request.getParameter("email")=="") {
    				
    		
    				request.setAttribute("errMessage", error = "El correo no puede estar vacio");
    				RequestDispatcher rd = request.getRequestDispatcher("ModificacionUsuarioAdm.jsp");
    				rd.forward(request, response);
    					
    				
    			}else if(Usuario.validarCuentaGmail(email)==1) {
    				
    				request.setAttribute("errMessage", error = "El correo debe terminar en: @gmail.com");
    				RequestDispatcher rd = request.getRequestDispatcher("ModificacionUsuarioAdm.jsp");
    				rd.forward(request, response);
    					
    				
    			}else if(Usuario.validarCuentaGmail(email)==3) {
    	
    				request.setAttribute("errMessage", error = "El nombre de usuario debe tener una longitud mínima de 11 carácteres");
    				RequestDispatcher rd = request.getRequestDispatcher("ModificacionUsuarioAdm.jsp");
    				rd.forward(request, response);
    					
    				
    			}else if(Usuario.validarCuentaGmail(email)==2) {
    		
    				request.setAttribute("errMessage", error = "El nombre de usuario debe de tener una longitud máxima de 26 carácteres");
    				RequestDispatcher rd = request.getRequestDispatcher("ModificacionUsuarioAdm.jsp");
    				rd.forward(request, response);
    						
    			}else if(Usuario.validarCuentaGmailCaracteresEspeciales(email)==true) {

    				request.setAttribute("errMessage", error = "El nombre de usuario no admite carácteres especiales");
    				RequestDispatcher rd = request.getRequestDispatcher("ModificacionUsuarioAdm.jsp");
    				rd.forward(request, response);
    				
    				
    			}else if(Usuario.contieneComillas(email)==true){
    				
    				request.setAttribute("errMessage", error = "El nombre de usuario no admite carácteres especiales");
    				RequestDispatcher rd = request.getRequestDispatcher("ModificacionUsuarioAdm.jsp");
    				rd.forward(request, response);
    					
    			}else if (contrasena.equals("")) {
    				request.setAttribute("errMessage", error = "La contraseña no puede estar vacia");
    				RequestDispatcher rd = request.getRequestDispatcher("ModificacionUsuarioAdm.jsp");
    				rd.forward(request, response);

    			}else if (contrasena.equals(" ")) {
    				request.setAttribute("errMessage", error = "La contraseña no puede contener espacios");
    				RequestDispatcher rd = request.getRequestDispatcher("ModificacionUsuarioAdm.jsp");
    				rd.forward(request, response);

    			}else if (contrasena.length() < 4) {
    				request.setAttribute("errMessage", error = "El campo del nombre tiene que tener más de cuantro letras");
    				RequestDispatcher rd = request.getRequestDispatcher("ModificacionUsuarioAdm.jsp");
    				rd.forward(request, response);

    			}else if (contrasena.length() > 8) {
    				request.setAttribute("errMessage", error = "El campo de la contraseña debe contener más de 8 carácteres");
    				RequestDispatcher rd = request.getRequestDispatcher("ModificacionUsuarioAdm.jsp");
    				rd.forward(request, response);

    			}else if(Usuario.validacionContrasenaUsuario(contrasena)==true){
    				
    				request.setAttribute("errMessage", error = "No se admiten carácteres especiales en la contraseña");
    				RequestDispatcher rd = request.getRequestDispatcher("ModificacionUsuarioAdm.jsp");
    				rd.forward(request, response);
    					
    			}else if(Usuario.contieneComillaContrasena(contrasena)==true){

    				request.setAttribute("errMessage", error = "No se admiten carácteres especiales en la contraseña");
    				RequestDispatcher rd = request.getRequestDispatcher("ModificacionUsuarioAdm.jsp");
    				rd.forward(request, response);
    					
    			}else if (!contrasena.equals(confirmarContrasena)) {
    			   
    			    request.setAttribute("errMessage", error = "Las contraseñas no coinciden");
    			    RequestDispatcher rd = request.getRequestDispatcher("ModificacionUsuarioAdm.jsp");
    			    rd.forward(request, response);
    			}else {

            try {
                con = pool.getConnection();
                sql = "UPDATE users SET password=?, username=?, email=?, nombreYApellidos=?,direccion=? WHERE idUsers=?";
                prestmt = con.prepareStatement(sql);
                prestmt.setString(1, Usuario.md5encryp(contrasena));
                prestmt.setString(2, nombre);
                prestmt.setString(3, email);
                prestmt.setString(4, nombreYApellidos);
                prestmt.setString(4, nombreYApellidos);
                prestmt.setString(5, direccion);
                prestmt.setInt(6, id);

                int sentencia = prestmt.executeUpdate();

                RequestDispatcher rd = request.getRequestDispatcher("ConsultaUsuarioAdm.jsp");
                rd.forward(request, response);

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (prestmt != null) {
                        prestmt.close();
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
    }
}





