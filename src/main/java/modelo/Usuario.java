package modelo;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.security.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Usuario {

	private String userName;

	private String password;

	private String role;

	private String email;
	
	private DataSource pool;

	private int id;
	
	private String nombreYApellidos;
	
	private String direccion;

	public Usuario() {
		super();
	}


	public Usuario(String userName, String password, String role, String email, DataSource pool, int id, String nombreYApellidos, String direccion) {
		super();
		this.userName = userName;
		this.password = password;
		this.role = role;
		this.email = email;
		this.pool = pool;
		this.id = id;
		this.nombreYApellidos = nombreYApellidos;
		this.direccion = direccion;
	}



	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getRole() {
		return role;
	}



	public void setRole(String role) {
		this.role = role;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public DataSource getPool() {
		return pool;
	}



	public void setPool(DataSource pool) {
		this.pool = pool;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getNombreYApellidos() {
		return nombreYApellidos;
	}



	public void setNombreYApellidos(String nombreYApellidos) {
		this.nombreYApellidos = nombreYApellidos;
	}



	public String getDireccion() {
		return direccion;
	}



	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}



	public static String md5encryp(String input) {

		String output = "";

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");

			byte[] digest = md.digest(input.getBytes());

			StringBuilder sb = new StringBuilder();

			for (byte b : digest) {
				sb.append(String.format("%02x", b));
			}

			output = sb.toString();

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return output;

	}

	public static boolean validacion(String usuarioContrasena) {

		char[] caracteresEspeciales = { 'ª', 'º', '!', '|', '"', '@', '·', '#', '$', '%', '&', '/', '(', ')', '=', '?',
				'¿', '¡', '/', '*', '-', '^', '`', '[', 'Ç', '}', '{', '¨', '´', '{', '+', ',', ';', '.', '-', '_', '<',
				'>' };

		boolean condicion = false;

		for (int i = 0; i < (usuarioContrasena.length()); i++) {

			for (int a = 0; a < caracteresEspeciales.length; a++) {

				if (usuarioContrasena.charAt(i) == caracteresEspeciales[a]) {

					condicion = true;

					break;

				} else {

					condicion = false;

				}
			}

		}

		return condicion;

	}
	
	public static boolean contieneEspacios(String nombreFormulario) {

		boolean condicion = false;

		if (nombreFormulario.contains(" ")) {

			condicion = true;
		}

		return condicion;

	}

	public static boolean contieneComillas(String nombreFormulario) {

		boolean condicion = false;

		if (nombreFormulario.contains("'")) {

			condicion = true;
		}

		return condicion;

	}

	public static int validarCuentaGmail(String correoElectronico) {

		String correoGmail = "@gmail.com";

		int condicion = 0;

		if (correoElectronico.length() >= 11& correoElectronico.length() <=18) {
			

			if (!correoElectronico.substring(correoElectronico.length() - 10).equals(correoGmail)) {

				
				condicion = 1;
			}

		} else if (correoElectronico.length()>=26) {

			condicion = 2;

		}else if (correoElectronico.length()<11) {

			condicion = 3;
		}
		return condicion;
		

	}public static boolean validarCuentaGmailCaracteresEspeciales(String correoElectronico) {
		char[] caracteresEspeciales = { 'ª', 'º', '!', '|', '"', '@', '·', '#', '$', '%', '&', '/', '(', ')', '=', '?',
				'¿', '¡', '/', '*', '-', '^', '`', '[', 'Ç', '}', '{', '¨', '´', '{', '+', ',', ';', '.', '-', '_', '<',
				'>' };

		boolean condicion = false;
		
		
		 for (int i = correoElectronico.length() - 11; i >= 0; i--) {
			 
	            char caracter = correoElectronico.charAt(i);
	            
	            
	            for (int a = 0; a < caracteresEspeciales.length; a++) {

					if (caracter== caracteresEspeciales[a]) {

						condicion = true;
						
					return condicion;
						
					
					} else {


						condicion = false;
					}
				} 

	        }
		return condicion;

	}	
	
	
	public static boolean contieneComillasEmail(String correogmail) {

		boolean condicion = false;

		if (correogmail.contains("'")) {

			condicion = true;
		}

		return condicion;

	}
	
	public static boolean contieneComillaContrasena(String contrasena) {

		boolean condicion = false;

		if (contrasena.contains("'")) {

			condicion = true;
		}

		return condicion;

	}
	
	public static boolean validacionContrasenaUsuario(String contrasena) {

		char[] caracteresEspeciales = { 'ª', 'º', '!', '|', '"', '@', '·', '#', '$', '%', '&', '/', '(', ')', '=', '?',
				'¿', '¡', '/', '*', '-', '^', '`', '[', 'Ç', '}', '{', '¨', '´', '{', '+', ',', ';', '.', '-', '_', '<',
				'>' };

		boolean condicion = false;

		for (int i = 0; i < (contrasena.length()); i++) {

			for (int a = 0; a < caracteresEspeciales.length; a++) {

				if (contrasena.charAt(i) == caracteresEspeciales[a]) {

					condicion = true;

					return condicion;

				} else {

					condicion = false;

				}
			}

		}

		return condicion;
	}
	
	public ArrayList<Usuario> generarTablaUsuarios() {
	    ArrayList<Usuario> arrayDinamico = new ArrayList<>();
	    Connection conexion = null;

	    try {
	        InitialContext ctx = new InitialContext();
	        DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/customers");

	        conexion = pool.getConnection();

	        String consultaSQL = "SELECT * FROM users WHERE role = 'User'";
	        
	        try (Statement stmt = conexion.createStatement();
	             ResultSet resultadoConsulta = stmt.executeQuery(consultaSQL)) {

	            while (resultadoConsulta.next()) {
	            	int id = resultadoConsulta.getInt("idusers");
	                String userName = resultadoConsulta.getString("userName");
	                String password = resultadoConsulta.getString("password");
	                String role = resultadoConsulta.getString("role");
	                String email = resultadoConsulta.getString("email");
	                String nombreYApellidos = resultadoConsulta.getString("nombreYApellidos");
	                String direccion = resultadoConsulta.getString("direccion");
	                
	                
	                
	                Usuario usuario = new Usuario( userName, password, role, email, pool,id, nombreYApellidos,direccion);
	                arrayDinamico.add(usuario);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	    } catch (NamingException | SQLException e) {
	        e.printStackTrace();
	    } finally {
	        // Cerrar la conexión en el bloque finally
	        try {
	            if (conexion != null) {
	                conexion.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return arrayDinamico;
	}

	public ArrayList<Usuario> cogerUsuario(int idUsuario) throws NamingException {
        ArrayList<Usuario> arrayDinamico = new ArrayList<>();
        Connection con = null;

        try {
            InitialContext ctx = new InitialContext();
            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/customers");

            // Obtener la conexión del pool
            con = pool.getConnection();

            String consultaSQL = "SELECT * FROM users WHERE idusers = ?";

            // Utilizar try-with-resources para asegurar que se cierren los recursos
            try (PreparedStatement pstmt = con.prepareStatement(consultaSQL)) {
                pstmt.setInt(1, idUsuario);

                // Ejecutar la consulta y obtener el conjunto de resultados
                try (ResultSet resultadoConsulta = pstmt.executeQuery()) {
                    while (resultadoConsulta.next()) {
                        int id = resultadoConsulta.getInt("idusers");
                        String userName = resultadoConsulta.getString("userName");
                        String password = resultadoConsulta.getString("password");
                        String role = resultadoConsulta.getString("role");
                        String email = resultadoConsulta.getString("email");
                        String nombreYApellidos = resultadoConsulta.getString("nombreYApellidos");
    	                String direccion = resultadoConsulta.getString("direccion");
    	                
                        Usuario usuario = new Usuario(userName, password, role, email, pool, id,nombreYApellidos, direccion);
                        arrayDinamico.add(usuario);
 
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return arrayDinamico;
    }
	
	public boolean isCondicionCumplida() throws SQLException, NamingException {

	        		if(this.role.equals("User") && !verificarExistenciaEnPedidos()==true) {
	        			
	        			return true;
	        		}	  
	        		else {
	        			
	        			return false;
	        		}
	}

	private boolean verificarExistenciaEnPedidos() throws SQLException, NamingException {
	    Connection con = null;

	    try {
	        InitialContext ctx = new InitialContext();
	        DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/customers");

	        // Obtener la conexión del pool
	        con = pool.getConnection();

	        String consultaSQL = "SELECT 1 FROM pedidos WHERE idUsuarioFK1 = ?";
	        
	        // Utilizamos PreparedStatement para manejar consultas parametrizadas
	        try (PreparedStatement pstmt = con.prepareStatement(consultaSQL)) {
	            // Establecemos el valor del parámetro idUsuarioFK1
	            pstmt.setInt(1, this.id);

	            // Ejecutamos la consulta y obtenemos el conjunto de resultados
	            try (ResultSet resultadoConsulta = pstmt.executeQuery()) {
	                return resultadoConsulta.next(); // Devuelve true si existe en pedidos, false si no
	            }
	        }
	    } finally {
	        // Cerrar la conexión en el bloque finally
	        if (con != null) {
	            con.close();
	        }
	    }
	}

	public class FormatoFechaEuropeo {

	    public static String obtenerFechaEnFormatoEuropeo() {
	        // Obtener la fecha actual
	        Date fechaActual = new Date();

	        // Crear un objeto SimpleDateFormat con el formato europeo
	        SimpleDateFormat formatoEuropeo = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

	        // Formatear la fecha y devolverla como una cadena
	        return formatoEuropeo.format(fechaActual);
	    }
	}
		
	public boolean esNumero(String str) {
	    try {
	        Double.parseDouble(str);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}
		
	    // Función para obtener la fecha actual
	public static String obtenerFechaActual() {
        // Crea un objeto de fecha actual
        Date fechaActual = new Date();

        // Crear un objeto SimpleDateFormat para formatear la fecha
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Formatear la fecha en el formato deseado
        String fechaFormateada = dateFormat.format(fechaActual);

        // Devolver la fecha formateada
        return fechaFormateada;
    }

}

