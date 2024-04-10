package modelo;

import java.io.BufferedInputStream;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;

import com.mysql.cj.jdbc.Blob;

public class Producto {

	private int idProducto;

	private String nombreProducto;

	private double precioProducto;

	private int cantidadProducto;

	private InputStream foto;

	private DataSource pool;

	private int CantidadPedidaDeProductoEnCarritoSolicitada = 0;

	private double precioIndividial;

	double precio2 = 0;

	public Producto() {
		super();
	}

	public Producto(int idProducto, String nombreProducto, double precioProducto, int cantidadProducto, InputStream foto, DataSource pool, int cantidadPedidaDeProductoEnCarritoSolicitada, double precioIndividial) {
		super();
		this.idProducto = idProducto;
		this.nombreProducto = nombreProducto;
		this.precioProducto = precioProducto;
		this.cantidadProducto = cantidadProducto;
		this.foto = foto;
		this.pool = pool;
		CantidadPedidaDeProductoEnCarritoSolicitada = cantidadPedidaDeProductoEnCarritoSolicitada;
		this.precioIndividial = precioIndividial;
	}

	public int getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(int idProducto) {
		this.idProducto = idProducto;
	}

	public String getNombreProducto() {
		return nombreProducto;
	}

	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}

	public double getPrecioProducto() {
		return precioProducto;
	}

	public void setPrecioProducto(double precioProducto) {
		this.precioProducto = precioProducto;
	}

	public int getCantidadProducto() {
		return cantidadProducto;
	}

	public void setCantidadProducto(int cantidadProducto) {
		this.cantidadProducto = cantidadProducto;
	}

	public InputStream getFoto() {
		return foto;
	}

	public void setFoto(InputStream foto) {
		this.foto = foto;
	}

	public DataSource getPool() {
		return pool;
	}

	public void setPool(DataSource pool) {
		this.pool = pool;
	}

	public int getCantidadPedidaDeProductoEnCarritoSolicitada() {
		return CantidadPedidaDeProductoEnCarritoSolicitada;
	}

	public void setCantidadPedidaDeProductoEnCarritoSolicitada(int cantidadPedidaDeProductoEnCarritoSolicitada) {
		CantidadPedidaDeProductoEnCarritoSolicitada = cantidadPedidaDeProductoEnCarritoSolicitada;
	}

	public double getPrecioIndividial() {
		return precioIndividial;
	}

	public void setPrecioIndividial(double precioIndividial) {
		this.precioIndividial = precioIndividial;
	}

	public ArrayList<Producto> generarTablaProductos() {

		ArrayList<Producto> arrayDinamico = new ArrayList<>();
		Connection conexion = null;

		try {
			InitialContext ctx = new InitialContext();
			DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/customers");

			// Obtener la conexión del pool
			conexion = pool.getConnection();

			String consultaSQL = "SELECT * FROM productos;";

			try (Statement stmt = conexion.createStatement();
					ResultSet resultadoConsulta = stmt.executeQuery(consultaSQL)) {

				while (resultadoConsulta.next()) {
					int idProducto = resultadoConsulta.getInt("idProducto");
					String nombreProducto = resultadoConsulta.getString("nombreProducto");
					double precioProducto = resultadoConsulta.getDouble("precioProducto");
					int cantidadProducto = resultadoConsulta.getInt("cantidadProducto");
					InputStream foto = resultadoConsulta.getBinaryStream("imagenProducto");

					Producto producto = new Producto(idProducto, nombreProducto, precioProducto, cantidadProducto, foto, pool, 0, 0);
					arrayDinamico.add(producto);
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

	public boolean verificarExistenciaProducto(String nombreProducto) throws SQLException, NamingException {
		Connection con = null;

		try {
			InitialContext ctx = new InitialContext();
			DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/customers");

			// Obtener la conexión del pool
			con = pool.getConnection();

			String consultaSQL = "SELECT 1 FROM productos WHERE nombreProducto = ?";

			try (PreparedStatement pstmt = con.prepareStatement(consultaSQL)) {
				pstmt.setString(1, nombreProducto); // Usar el parámetro nombreProducto

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

	public boolean verificarExistenciaProductosEnLineaPedidos() throws SQLException, NamingException {
		Connection con = null;

		try {
			InitialContext ctx = new InitialContext();
			DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/customers");

			// Obtener la conexión del pool
			con = pool.getConnection();

			String consultaSQL = "SELECT 1 FROM lineapedido WHERE idProductoFK3 = ?";

			try (PreparedStatement pstmt = con.prepareStatement(consultaSQL)) {
				pstmt.setInt(1, this.idProducto);

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

	public boolean esNumero(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean validacion(String cadena) {

		char[] caracteresEspeciales = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		boolean condicion = false;

		for (int i = 0; i < (cadena.length()); i++) {

			for (int a = 0; a < caracteresEspeciales.length; a++) {

				if (cadena.charAt(i) == caracteresEspeciales[a]) {

					condicion = true;

					break;

				} else {

					condicion = false;

				}
			}

		}

		return condicion;

	}

	public List listar() {

		ArrayList<Producto> arrayDinamico = new ArrayList<>();
		Connection conexion = null;

		try {
			InitialContext ctx = new InitialContext();
			DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/customers");

			// Obtener la conexión del pool
			conexion = pool.getConnection();

			String consultaSQL = "SELECT * FROM productos;";

			try (Statement stmt = conexion.createStatement();
					ResultSet resultadoConsulta = stmt.executeQuery(consultaSQL)) {

				while (resultadoConsulta.next()) {

					Producto p = new Producto();

					int idProducto = resultadoConsulta.getInt("idProducto");
					String nombreProducto = resultadoConsulta.getString("nombreProducto");
					double precioProducto = resultadoConsulta.getDouble("precioProducto");
					int cantidadProducto = resultadoConsulta.getInt("cantidadProducto");
					InputStream foto = resultadoConsulta.getBinaryStream("imagenProducto");
					double precioIndi = precioIndividial;
					Producto producto = new Producto(idProducto, nombreProducto, precioProducto, cantidadProducto, foto,
							pool, 0, precioIndi);
					arrayDinamico.add(producto);
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

	public byte[] listarImg(int id) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		byte[] imagenBytes = null;

		try {
			InitialContext ctx = new InitialContext();
			DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/customers");
			con = pool.getConnection();

			String sql = "SELECT imagenProducto FROM productos WHERE idProducto = ?";
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();

			if (rs.next()) {
				Blob imagenBlob = (Blob) rs.getBlob("imagenProducto");
				if (imagenBlob != null) {
					imagenBytes = imagenBlob.getBytes(1, (int) imagenBlob.length());
				}
			}
		} catch (Exception e) {
			// Manejo de excepciones
			e.printStackTrace();
		} finally {
			// Cerrar conexiones y liberar recursos
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return imagenBytes;
	}

	public static List<Producto> listarProductos(int idProducto) throws NamingException, SQLException {
		List<Producto> listaCarrito = new ArrayList<>();
		Connection conexion = null;
		PreparedStatement prestmt = null;
		ResultSet rs = null;

		try {
			InitialContext ctx = new InitialContext();
			DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/customers");
			conexion = pool.getConnection();

			String sql = "SELECT * FROM productos where idProducto=?";
			prestmt = conexion.prepareStatement(sql);
			prestmt.setInt(1, idProducto);

			rs = prestmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("idProducto");
				String nombre = rs.getString("nombreProducto");
				double precio = rs.getDouble("precioProducto");
				int cantidad = rs.getInt("cantidadProducto");
				InputStream imagen = rs.getBinaryStream("imagenProducto");
				Producto producto = new Producto(id, nombre, precio, cantidad, imagen, pool, 0, 0);
				listaCarrito.add(producto);
			}
		} finally {
			if (rs != null)
				rs.close();
			if (prestmt != null)
				prestmt.close();
			if (conexion != null)
				conexion.close();
		}

		return listaCarrito;
	}

	public void aumentarCantidadPedida() {
		CantidadPedidaDeProductoEnCarritoSolicitada++;
	}

//Declaración de una variable global para mantener el precio acumulado

	public void aumentarPrecio(double precioProducto) {

		if (precio2 == 0) { // Si el precio2 es inicialmente 0
			precio2 = precioProducto * 2; // Incrementa al doble del precioProducto
		} else {
			precio2 += precioProducto; // Suma el precioProducto al precio2 actual
		}

		double numeroRedondeado = Math.round(precio2 * 1000.0) / 1000.0;

		precioIndividial = numeroRedondeado;
	}

	private byte[] imagen;

	private String cachedBase64Image;

// Otros atributos, constructor, getters y setters

	public byte[] getImagen() {
		// Supongamos que aquí obtienes los datos binarios de la imagen
		return imagen;
	}

	public String getImagenBase64() {
		// Obtener los datos binarios de la imagen
		byte[] imageData = getImagen();

		// Convertir los datos binarios a formato Base64
		String base64Image = null;
		if (imageData != null && imageData.length > 0) {
			base64Image = java.util.Base64.getEncoder().encodeToString(imageData);
		}

		return base64Image;
	}

	public static String codificarBase64(InputStream inputStream) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
			byte[] bytes = outputStream.toByteArray();
			return Base64.getEncoder().encodeToString(bytes);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getCachedBase64Image() {
		return cachedBase64Image;
	}

	public void setCachedBase64Image(String cachedBase64Image) {
		this.cachedBase64Image = cachedBase64Image;
	}
	
	
public int cantidadBaseDeDatos() {
	// TODO Auto-generated method stub
	return cantidadProducto;
}
	

}
