package controlador;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

/**
 * Servlet implementation class AltaAdmUsuario
 */
@WebServlet("/AltaUsuarioAdm")
public class AltaUsuarioAdm extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataSource pool;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AltaUsuarioAdm() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void init(ServletConfig config) throws ServletException {

		try {
			// Crea un contexto para poder luego buscar el recurso DataSource
			InitialContext ctx = new InitialContext();
			// Busca el recurso DataSource en el contexto
			pool = (DataSource) ctx.lookup("java:comp/env/jdbc/customers");
			if (pool == null) {
				throw new ServletException("DataSource desconocida'customers'");
			}
		} catch (NamingException ex) {
		}
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String ruta = "AltaAdm.jsp";
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
            		  
			if (nombre.equals("")) {
				request.setAttribute("errMessage", error = "No puedes dejar el nombre vacio");
				RequestDispatcher rd = request.getRequestDispatcher("AltaUsuarioAdm.jsp");
				rd.forward(request, response);

			} else if (nombreYApellidos.equals("")) {
				request.setAttribute("errMessage", error = "No puedes dejar el campo del nombre y apellidos vacios");
				RequestDispatcher rd = request.getRequestDispatcher("AltaUsuarioAdm.jsp");
				rd.forward(request, response);
				
			}else if (direccion .equals("")) {
				request.setAttribute("errMessage", error = "No puedes dejar la dirección vacía");
				RequestDispatcher rd = request.getRequestDispatcher("AltaUsuarioAdm.jsp");
				rd.forward(request, response);
				
			}else if (nombreYApellidos.length()>25) {
				request.setAttribute("errMessage", error = "El campo nombre y apellidos no puede tener más de 25 letras");
				RequestDispatcher rd = request.getRequestDispatcher("AltaUsuarioAdm.jsp");
				rd.forward(request, response);
				
			}else if (direccion.length()>25) {
				request.setAttribute("errMessage", error = "La dirección no puede tener más de 25 letras");
				RequestDispatcher rd = request.getRequestDispatcher("AltaUsuarioAdm.jsp");
				rd.forward(request, response);
				
			}else if( Usuario.validacion(nombreYApellidos)==true) {
				
				request.setAttribute("errMessage", error = "El nombre y los apellidos no pueden contener carácteres especiales");
				RequestDispatcher rd = request.getRequestDispatcher("AltaUsuarioAdm.jsp");
				rd.forward(request, response);
				
			}else if( Usuario.validacion(direccion)==true) {
				
				request.setAttribute("errMessage", error = "La dirección no puede contener carácteres especiales");
				RequestDispatcher rd = request.getRequestDispatcher("AltaUsuarioAdm.jsp");
				rd.forward(request, response);
				
			}else if (nombre.length() < 4) {
				request.setAttribute("errMessage", error = "El campo del nombre tiene que tener más de cuatro letras");
				RequestDispatcher rd = request.getRequestDispatcher("AltaUsuarioAdm.jsp");
				rd.forward(request, response);

			} else if (nombre.length() > 12) {
				request.setAttribute("errMessage", error = "El campo del nombre no puede tener más de 12 letras");
				RequestDispatcher rd = request.getRequestDispatcher("AltaUsuarioAdm.jsp");
				rd.forward(request, response);

			}else if( Usuario.validacion(nombre)==true) {
				
				request.setAttribute("errMessage", error = "El nombre de usuario no puede contener carácteres especiales");
				RequestDispatcher rd = request.getRequestDispatcher("AltaUsuarioAdm.jsp");
				rd.forward(request, response);
				
			}else if(Usuario.contieneEspacios(nombre)==true){
				
				request.setAttribute("errMessage", error = "El nombre de usuario no puede contener espacios");
				RequestDispatcher rd = request.getRequestDispatcher("AltaUsuarioAdm.jsp");
				rd.forward(request, response);
				
			}else if(Usuario.contieneComillas(nombre)==true){
				
				request.setAttribute("errMessage", error = "El nombre de usuario no puede contener carácteres especiales");
				RequestDispatcher rd = request.getRequestDispatcher("AltaUsuarioAdm.jsp");
				rd.forward(request, response);
					
			}else if(request.getParameter("email")==" "){
				request.setAttribute("errMessage", error = "El campo email no puede contener espacios");
				RequestDispatcher rd = request.getRequestDispatcher("AltaUsuarioAdm.jsp");
				rd.forward(request, response);
				
				
			}else if(email.equals("")) {
				
				request.setAttribute("errMessage", error = "El correo no puede estar vacio");
				RequestDispatcher rd = request.getRequestDispatcher("AltaUsuarioAdm.jsp");
				rd.forward(request, response);
					
				
			}else if(Usuario.validarCuentaGmail(email)==1) {
				
				request.setAttribute("errMessage", error = "El correo debe terminar en: @gmail.com");
				RequestDispatcher rd = request.getRequestDispatcher("AltaUsuarioAdm.jsp");
				rd.forward(request, response);
					
				
			}else if(Usuario.validarCuentaGmail(email)==3) {
	
				request.setAttribute("errMessage", error = "El correo debe tener una longitud mínima de 11 carácteres");
				RequestDispatcher rd = request.getRequestDispatcher("AltaUsuarioAdm.jsp");
				rd.forward(request, response);
					
				
			}else if(Usuario.validarCuentaGmail(email)==2) {
		
				request.setAttribute("errMessage", error = "El nombre de usuario debe de tener una longitud máxima de 26 carácteres");
				RequestDispatcher rd = request.getRequestDispatcher("AltaUsuarioAdm.jsp");
				rd.forward(request, response);
						
			}else if(Usuario.validarCuentaGmailCaracteresEspeciales(email)==true) {

				request.setAttribute("errMessage", error = "El nombre de usuario no admite carácteres especiales");
				RequestDispatcher rd = request.getRequestDispatcher("AltaUsuarioAdm.jsp");
				rd.forward(request, response);
				
			}else if(Usuario.contieneComillas(email)==true){
				
				request.setAttribute("errMessage", error = "El nombre de usuario no admite carácteres especiales");
				RequestDispatcher rd = request.getRequestDispatcher("AltaUsuarioAdm.jsp");
				rd.forward(request, response);
					
			}else if (contrasena.equals("")) {
				request.setAttribute("errMessage", error = "La contraseña no puede estar vacia");
				RequestDispatcher rd = request.getRequestDispatcher("AltaUsuarioAdm.jsp");
				rd.forward(request, response);

			}else if (contrasena.equals(" ")) {
				request.setAttribute("errMessage", error = "La contraseña no puede contener espacios");
				RequestDispatcher rd = request.getRequestDispatcher("AltaUsuarioAdm.jsp");
				rd.forward(request, response);

			}else if (contrasena.length() < 4) {
				request.setAttribute("errMessage", error = "El campo del nombre tiene que tener más de cuatro letras");
				RequestDispatcher rd = request.getRequestDispatcher("AltaUsuarioAdm.jsp");
				rd.forward(request, response);

			}else if (contrasena.length() > 8) {
				request.setAttribute("errMessage", error = "El campo de la contraseña debe contener más de 8 carácteres");
				RequestDispatcher rd = request.getRequestDispatcher("AltaUsuarioAdm.jsp");
				rd.forward(request, response);

			}else if(Usuario.validacionContrasenaUsuario(contrasena)==true){
				
				request.setAttribute("errMessage", error = "No se admiten carácteres especiales en la contraseña");
				RequestDispatcher rd = request.getRequestDispatcher("AltaUsuarioAdm.jsp");
				rd.forward(request, response);
					
			}else if(Usuario.contieneComillaContrasena(contrasena)==true){

				request.setAttribute("errMessage", error = "No se admiten carácteres especiales en la contraseña");
				RequestDispatcher rd = request.getRequestDispatcher("AltaUsuarioAdm.jsp");
				rd.forward(request, response);
					
			}else if (!contrasena.equals(confirmarContrasena)) {
			   
			    request.setAttribute("errMessage", error = "Las contraseñas no coinciden");
			    RequestDispatcher rd = request.getRequestDispatcher("AltaUsuarioAdm.jsp");
			    rd.forward(request, response);
			}else if (usuario.esNumero(nombre)==true) {
				
				request.setAttribute("errMessage", "No puedes ingresar un número");
		        RequestDispatcher rd = request.getRequestDispatcher("AltaUsuarioAdm.jsp");
				rd.forward(request, response);
				
				
			}else {

				try {
					con = pool.getConnection();

					Statement statement = con.createStatement();

					ResultSet resultSet = statement.executeQuery("select username from users;");
					
					do {
					
					while (resultSet.next())

					{
						    String userNameDB = resultSet.getString("username");

						    if(nombre.equals(userNameDB)) {
						    	
							request.setAttribute("errMessage", error = "El usuario ya existe");
							RequestDispatcher rd = request.getRequestDispatcher("AltaAdm.jsp");
							rd.forward(request, response);	
							
						    }
					}
					
					}while	(resultSet.next());	
							
							
							try {
								con = pool.getConnection();

								// En sql metemos la sentencia

								sql = "INSERT INTO users(username, password, role,email,nombreYApellidos,direccion) VALUES(?,?,?,?,?,?)";

								prestmt = con.prepareStatement(sql);
								
								prestmt.setString(1, (request.getParameter("username")));
								prestmt.setString(2, Usuario.md5encryp(request.getParameter("password")));

								prestmt.setString(3, "User");

								prestmt.setString(4, request.getParameter("email"));
								
								prestmt.setString(5, request.getParameter("nombreYApellidos"));
								
								prestmt.setString(6, request.getParameter("direccion"));

								int sentencia = prestmt.executeUpdate();

								//RequestDispatcher rd = request.getRequestDispatcher("ConsultaUsuarioAdm.jsp");
								//rd.forward(request, response);
								
								response.sendRedirect("ConsultaUsuarioAdm.jsp");

							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}			

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}
	}


