package modelo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class Pedido {

    private int idpedido;
    private int idUsuarioFK1;
    private String fechaPedido;
    private double pedidoPrecio;
    private String nombre_usuario;
    private DataSource pool;

    public Pedido() {
		super();
	}

	public Pedido(int idpedido, int idUsuarioFK1, String fechaPedido, double pedidoPrecio, String nombre_usuario,
			DataSource pool) {
		super();
		this.idpedido = idpedido;
		this.idUsuarioFK1 = idUsuarioFK1;
		this.fechaPedido = fechaPedido;
		this.pedidoPrecio = pedidoPrecio;
		this.nombre_usuario = nombre_usuario;
		this.pool = pool;
		
	}

	public int getIdpedido() {
		return idpedido;
	}



	public void setIdpedido(int idpedido) {
		this.idpedido = idpedido;
	}



	public int getIdUsuarioFK1() {
		return idUsuarioFK1;
	}



	public void setIdUsuarioFK1(int idUsuarioFK1) {
		this.idUsuarioFK1 = idUsuarioFK1;
	}



	public String getFechaPedido() {
		return fechaPedido;
	}



	public void setFechaPedido(String fechaPedido) {
		this.fechaPedido = fechaPedido;
	}



	public double getPedidoPrecio() {
		return pedidoPrecio;
	}



	public void setPedidoPrecio(double pedidoPrecio) {
		this.pedidoPrecio = pedidoPrecio;
	}



	public String getNombre_usuario() {
		return nombre_usuario;
	}



	public void setNombre_usuario(String nombre_usuario) {
		this.nombre_usuario = nombre_usuario;
	}



	public DataSource getPool() {
		return pool;
	}



	public void setPool(DataSource pool) {
		this.pool = pool;
	}


	public ArrayList<Pedido> generarTablaPedidos() {
        ArrayList<Pedido> arrayDinamico = new ArrayList<>();
        Connection conexion = null;

        try {
            InitialContext ctx = new InitialContext();
            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/customers");

            // Obtener la conexión del pool
            conexion = pool.getConnection();

            String consultaSQL = "SELECT pedidos.idpedido, pedidos.idUsuarioFK1, users.username as nombre_usuario, pedidos.fechaPedido, pedidos.pedidoPrecio FROM pedidos JOIN users ON pedidos.idUsuarioFK1 = users.idusers;";

            try (Statement stmt = conexion.createStatement();
                    ResultSet resultadoConsulta = stmt.executeQuery(consultaSQL)) {

                while (resultadoConsulta.next()) {
                    int idpedido = resultadoConsulta.getInt("idpedido");
                    int idUsuarioFK1 = resultadoConsulta.getInt("idUsuarioFK1");
                    String nombre_usuario = resultadoConsulta.getString("nombre_usuario");
                    String fechaPedido = resultadoConsulta.getString("fechaPedido");
                    double pedidoPrecio = resultadoConsulta.getDouble("pedidoPrecio");
                    Pedido pedido = new Pedido(idpedido, idUsuarioFK1, fechaPedido, pedidoPrecio, nombre_usuario, pool);
                    arrayDinamico.add(pedido);
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

    public boolean verificarExistenciaEnPedidos() throws SQLException, NamingException {
        Connection con = null;

        try {
            InitialContext ctx = new InitialContext();
            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/customers");

            // Obtener la conexión del pool
            con = pool.getConnection();

            String consultaSQL = "SELECT 1 FROM lineapedido WHERE idPedidoFK2 = ?";

            try (PreparedStatement pstmt = con.prepareStatement(consultaSQL)) {
                pstmt.setInt(1, this.idpedido);

                try (ResultSet resultadoConsulta = pstmt.executeQuery()) {
                    return resultadoConsulta.next();
                }
            }
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }
    
    public boolean validarFecha(String fecha) {
        try {
            DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(fecha, formatoFecha);
          
            return false;
        } catch (DateTimeParseException e) {
            return true;
        }
       
    }

    public boolean buscarUsuario(String idUsuario) {
        Connection conexion = null;

        try {
            InitialContext ctx = new InitialContext();
            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/customers");

            // Obtener la conexión del pool
            conexion = pool.getConnection();

            String consultaSQL = "SELECT * FROM users WHERE role = 'User'";

            try (Statement stmt = conexion.createStatement();
                 ResultSet resultadoConsulta = stmt.executeQuery(consultaSQL)) {

                while (resultadoConsulta.next()) {
                    int id = resultadoConsulta.getInt("idusers");
                    // Se asume que el id es único, por lo que se puede usar para la comparación directa.

                    if (Integer.parseInt(idUsuario) == id) {
                        return true;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (NamingException | SQLException e) {
            e.printStackTrace();
        } finally {
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

        public static boolean verificarLongitudNumero(int numero) {
            // Convertir el número a cadena y verificar la longitud
            String numeroComoCadena = Integer.toString(numero);
                    
           if( numeroComoCadena.length() > 4){
        	          	   
        	   return true;
           
        }else {
        	
        	return false;
        }
    
        }  
    
        public String convertirFechaAmericanaAEuropea(String fechaAmericana) { // Cambiado de Date a String
            try {
                SimpleDateFormat formatoAmericano = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat formatoEuropeo = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date fecha = formatoAmericano.parse(fechaAmericana);
                String fechaFormateada = formatoEuropeo.format(fecha);
                return fechaFormateada;
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }     
        
        }
        
        public String convertirFechaEuropeaAAmericana(String fechaEuropea) { // Cambiado de Date a String
            try {
                SimpleDateFormat formatoAmericano = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat formatoEuropeo = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date fecha = formatoEuropeo.parse(fechaEuropea);
                String fechaFormateada = formatoAmericano.format(fecha);
                return fechaFormateada;
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
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
            
        public int idUltimoPedido() throws SQLException, NamingException {
            int ultimoPedidoId = 0;
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;

            try {
                InitialContext ctx = new InitialContext();
                DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/customers");

                // Obtener la conexión del pool
                con = pool.getConnection();

                String consultaSQL = "SELECT MAX(idpedido) AS ultimo_pedido FROM pedidos";
                pstmt = con.prepareStatement(consultaSQL);

                rs = pstmt.executeQuery();

                if (rs.next()) {
                    ultimoPedidoId = rs.getInt("ultimo_pedido");
                }
            } finally {
                // Cerrar recursos
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (con != null) {
                    con.close();
                }
            }
            return ultimoPedidoId;
        }                   
        
}
