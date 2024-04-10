package controlador;

import java.awt.event.ActionListener;
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
 * Servlet implementation class AltaServlet
 */
@WebServlet("/AltaRegistroNuevoUsuario")
public class AltaRegistroNuevoUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataSource pool;

	public AltaRegistroNuevoUsuario() {
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String ruta = "";
		String sql = "";
		Connection con = null;
		// cualquier sentencia que sea INSERT , UPDATE , DELETE se usa preparedstatement
		// en vez de statement
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
				String mensaje = "No puedes dejar el nombre vacio";
				request.setAttribute("mensaje", mensaje);				
				RequestDispatcher rd = request.getRequestDispatcher("Registro.jsp");
				rd.forward(request, response);

			}else if (nombreYApellidos.equals("")) {
				String mensaje = "No puedes dejar el campo del nombre y apellidos vacios";
				request.setAttribute("mensaje", mensaje);
				RequestDispatcher rd = request.getRequestDispatcher("Registro.jsp");
				rd.forward(request, response);
			}else if (direccion .equals("")) {
				String mensaje = "No puedes dejar la dirección vacía";
				request.setAttribute("mensaje", mensaje);
				RequestDispatcher rd = request.getRequestDispatcher("Registro.jsp");
				rd.forward(request, response);
				
			}else if (nombreYApellidos.length()>25) {
				String mensaje = "El campo nombre y apellidos no puede tener más de 25 letras";
				request.setAttribute("mensaje", mensaje);
				RequestDispatcher rd = request.getRequestDispatcher("Registro.jsp");
				rd.forward(request, response);	
				
			}else if (direccion.length()>25) {			
				String mensaje = "La dirección no puede tener más de 25 letras";
				request.setAttribute("mensaje", mensaje);
				RequestDispatcher rd = request.getRequestDispatcher("Registro.jsp");
				rd.forward(request, response);	
			}else if( Usuario.validacion(nombreYApellidos)==true) {
				String mensaje = "El nombre y los apellidos no pueden contener carácteres especiales";
				request.setAttribute("mensaje", mensaje);
				RequestDispatcher rd = request.getRequestDispatcher("Registro.jsp");
				rd.forward(request, response);	
			}else if( Usuario.validacion(direccion)==true) {
				String mensaje = "La dirección no puede contener carácteres especiales";
				request.setAttribute("mensaje", mensaje);
				RequestDispatcher rd = request.getRequestDispatcher("Registro.jsp");
				rd.forward(request, response);
			}else if (nombre.length() < 4) {
				String mensaje = "El campo del nombre tiene que tener más de cuantro letras";
				request.setAttribute("mensaje", mensaje);
				RequestDispatcher rd = request.getRequestDispatcher("Registro.jsp");
				rd.forward(request, response);

			} else if (nombre.length() > 12) {
				String mensaje = "El campo del nombre no puede tener más de 12 letras";
				request.setAttribute("mensaje", mensaje);
				RequestDispatcher rd = request.getRequestDispatcher("Registro.jsp");
				rd.forward(request, response);

			}else if( Usuario.validacion(nombre)==true) {
				String mensaje = "El nombre de usuario no puede contener carácteres especiales";
				request.setAttribute("mensaje", mensaje);				
				RequestDispatcher rd = request.getRequestDispatcher("Registro.jsp");
				rd.forward(request, response);
				
			}else if(Usuario.contieneEspacios(nombre)==true){
				String mensaje = "El nombre de usuario no puede contener espacios";
				request.setAttribute("mensaje", mensaje);
				RequestDispatcher rd = request.getRequestDispatcher("Registro.jsp");
				rd.forward(request, response);
				
			}else if(Usuario.contieneComillas(nombre)==true){
				String mensaje = "El nombre de usuario no puede contener carácteres especiales";
				request.setAttribute("mensaje", mensaje);
				RequestDispatcher rd = request.getRequestDispatcher("Registro.jsp");
				rd.forward(request, response);
					
			}else if(request.getParameter("email")==" "){
				String mensaje = "El campo email no puede contener espacios";
				request.setAttribute("mensaje", mensaje);
				RequestDispatcher rd = request.getRequestDispatcher("Registro.jsp");
				rd.forward(request, response);
			}else if(request.getParameter("email")=="") {
				String mensaje = "El correo no puede estar vacio";
				request.setAttribute("mensaje", mensaje);
				RequestDispatcher rd = request.getRequestDispatcher("Registro.jsp");
				rd.forward(request, response);
			}else if(Usuario.validarCuentaGmail(email)==1) {
				String mensaje = "El correo debe terminar en: @gmail.com";
				request.setAttribute("mensaje", mensaje);
				RequestDispatcher rd = request.getRequestDispatcher("Registro.jsp");
				rd.forward(request, response);
			}else if(Usuario.validarCuentaGmail(email)==3) {
				String mensaje = "El nombre de usuario debe tener una longitud mínima de 11 carácteres";
				request.setAttribute("mensaje", mensaje);
				RequestDispatcher rd = request.getRequestDispatcher("Registro.jsp");
				rd.forward(request, response);	
			}else if(Usuario.validarCuentaGmail(email)==2) {
				String mensaje = "El nombre de usuario debe de tener una longitud máxima de 26 carácteres";
				request.setAttribute("mensaje", mensaje);
				RequestDispatcher rd = request.getRequestDispatcher("Registro.jsp");
				rd.forward(request, response);		
			}else if(Usuario.validarCuentaGmailCaracteresEspeciales(email)==true) {
				String mensaje = "El nombre de usuario no admite carácteres especiales";
				request.setAttribute("mensaje", mensaje);
				RequestDispatcher rd = request.getRequestDispatcher("Registro.jsp");
				rd.forward(request, response);
			}else if(Usuario.contieneComillas(email)==true){
				String mensaje = "El nombre de usuario no admite carácteres especiales";
				request.setAttribute("mensaje", mensaje);
				RequestDispatcher rd = request.getRequestDispatcher("Registro.jsp");
				rd.forward(request, response);	
			}else if (contrasena.equals("")) {
				String mensaje = "La contraseña no puede estar vacía";
				request.setAttribute("mensaje", mensaje);
				RequestDispatcher rd = request.getRequestDispatcher("Registro.jsp");
				rd.forward(request, response);
			}else if (contrasena.equals(" ")) {
				String mensaje = "La contraseña no puede contener espacios";
				request.setAttribute("mensaje", mensaje);
				RequestDispatcher rd = request.getRequestDispatcher("Registro.jsp");
				rd.forward(request, response);
			}else if (contrasena.length() < 4) {
				String mensaje = "El campo del nombre tiene que tener más de cuatro letras";
				request.setAttribute("mensaje", mensaje);
				RequestDispatcher rd = request.getRequestDispatcher("Registro.jsp");
				rd.forward(request, response);
			}else if (contrasena.length() > 8) {
				String mensaje = "El campo de la contraseña debe contener más de 8 caracteres";
				request.setAttribute("mensaje", mensaje);
				RequestDispatcher rd = request.getRequestDispatcher("Registro.jsp");
				rd.forward(request, response);
			}else if(Usuario.validacionContrasenaUsuario(contrasena)==true){
				String mensaje = "No se admiten carácteres especiales en la contraseña";
				request.setAttribute("mensaje", mensaje);
				RequestDispatcher rd = request.getRequestDispatcher("Registro.jsp");
				rd.forward(request, response);
					
			}else if(Usuario.contieneComillaContrasena(contrasena)==true){
				String mensaje = "No se admiten carácteres especiales en la contraseña";
				request.setAttribute("mensaje", mensaje);
				RequestDispatcher rd = request.getRequestDispatcher("Registro.jsp");
				rd.forward(request, response);
			}else if (!contrasena.equals(confirmarContrasena)) {
				String mensaje = "Las contraseñas no coinciden";
				request.setAttribute("mensaje", mensaje);
			    RequestDispatcher rd = request.getRequestDispatcher("Registro.jsp");
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
						    	String mensaje = "El usuario ya existe";
								request.setAttribute("mensaje", mensaje);   	
							RequestDispatcher rd = request.getRequestDispatcher("Registro.jsp");
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

								RequestDispatcher rd = request.getRequestDispatcher(ruta);
								rd.forward(request, response);

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
