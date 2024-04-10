package controlador;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import modelo.Producto;

@WebServlet("/AltaProductoAdm")
public class AltaProductoAdm extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Producto producto = new Producto();
	private DataSource pool;

	public void init() throws ServletException {
		try {
			InitialContext ctx = new InitialContext();
			pool = (DataSource) ctx.lookup("java:comp/env/jdbc/customers");
			if (pool == null) {
				throw new ServletException("DataSource desconocida 'customers'");
			}
		} catch (NamingException ex) {
			throw new ServletException("Error al buscar el DataSource", ex);
		}
	}

	public AltaProductoAdm() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
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
		String ruta = "ConsultaProductoAdm.jsp";
		String sql = "";
		Connection con = null;
		PreparedStatement prestmt = null;

		String nombreProducto = request.getParameter("nombreProducto");

		String precioDelProducto = request.getParameter("precio");

		String CantidadProducto = request.getParameter("cantidadProducto");

		String[] partes = precioDelProducto.split("\\.");

		if (nombreProducto.equals("")) {

			request.setAttribute("errMessage", "No puedes dejar el nombre del producto vacio");
			RequestDispatcher rd = request.getRequestDispatcher("AltaProductoAdm.jsp");
			rd.forward(request, response);

		} else if (precioDelProducto.equals("")) {

			request.setAttribute("errMessage", "No puedes dejar el precio vacio");
			RequestDispatcher rd = request.getRequestDispatcher("AltaProductoAdm.jsp");
			rd.forward(request, response);

		} else if (CantidadProducto.equals("")) {

			request.setAttribute("errMessage", "No puedes dejar la cantidad del producto vacia");
			RequestDispatcher rd = request.getRequestDispatcher("AltaProductoAdm.jsp");
			rd.forward(request, response);

		} else if (nombreProducto.length() >= 15) {
			
			request.setAttribute("errMessage", "El nombre del producto no puede tener más de 15 letras");
			RequestDispatcher rd = request.getRequestDispatcher("AltaProductoAdm.jsp");
			rd.forward(request, response);

		} else if (precioDelProducto.length() >= 5) {
			request.setAttribute("errMessage", "El precio no puede tener más de 5 dígitos");
			RequestDispatcher rd = request.getRequestDispatcher("AltaProductoAdm.jsp");
			rd.forward(request, response);

		} else if (CantidadProducto.length() >= 5) {

			request.setAttribute("errMessage", "La cantidad no puede tener más de 5 digitos");
			RequestDispatcher rd = request.getRequestDispatcher("AltaProductoAdm.jsp");
			rd.forward(request, response);

		} else if (partes.length > 1 && partes[1].length() > 2) {
			
			request.setAttribute("errMessage", "El precio no puede tener más de dos decimales");
			RequestDispatcher rd = request.getRequestDispatcher("AltaProductoAdm.jsp");
			rd.forward(request, response);

		} else if (producto.esNumero(precioDelProducto) == false) {
			
			request.setAttribute("errMessage", "Tienes que ingresar números y no letras o carácteres especiales");
			RequestDispatcher rd = request.getRequestDispatcher("AltaProductoAdm.jsp");
			rd.forward(request, response);

		} else if (producto.esNumero(CantidadProducto) == false) {
			
			request.setAttribute("errMessage", "Tienes que ingresar números y no letras o carácteres especiales");
			RequestDispatcher rd = request.getRequestDispatcher("AltaProductoAdm.jsp");
			rd.forward(request, response);

		} else if (producto.esNumero(nombreProducto) == true) {
			
			request.setAttribute("errMessage", "No puedes ingresar un numero en el nombre del producto");
			RequestDispatcher rd = request.getRequestDispatcher("AltaProductoAdm.jsp");
			rd.forward(request, response);

		} else if (producto.validacion(nombreProducto) == true) {
			
			request.setAttribute("errMessage", "No puedes ingresar un numero en el nombre del producto");
			RequestDispatcher rd = request.getRequestDispatcher("AltaProductoAdm.jsp");
			rd.forward(request, response);

		}
		try {

			if (producto.verificarExistenciaProducto(nombreProducto) == true) {
				request.setAttribute("errMessage", "El producto ya existe");
				RequestDispatcher rd = request.getRequestDispatcher("AltaProductoAdm.jsp");
				rd.forward(request, response);

			} else

				try {

					try {

						double pedidoDelProductoDouble = Double.parseDouble(precioDelProducto);

						int cantidadProductoInt = Integer.parseInt(CantidadProducto);
						con = pool.getConnection();

						sql = "INSERT INTO productos(nombreProducto, precioProducto, cantidadProducto) VALUES(?,?,?)";
						prestmt = con.prepareStatement(sql);
						prestmt.setString(1, nombreProducto);
						prestmt.setDouble(2, pedidoDelProductoDouble);
						prestmt.setDouble(3, cantidadProductoInt);
						int sentencia = prestmt.executeUpdate();
						response.sendRedirect(ruta);
						//RequestDispatcher rd = request.getRequestDispatcher(ruta);
						//rd.forward(request, response);

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
