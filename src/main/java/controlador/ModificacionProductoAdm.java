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

import modelo.Producto;

/**
 * Servlet implementation class ModificacionProductoFinal
 */
@WebServlet("/ModificacionProductoAdm")

public class ModificacionProductoAdm extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private DataSource pool;

	public ModificacionProductoAdm() {
		super();
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String ruta = "";
		String sql = "";
		Connection con = null;
		PreparedStatement prestmt = null;
		String error;
		Producto producto = new Producto();
		String idProducto = request.getParameter("idProducto");
		String nombreProducto = request.getParameter("nombreProducto");
		String precioProducto = request.getParameter("precio");
		String cantidadProducto = request.getParameter("cantidad");

		String[] partes = precioProducto.split("\\.");

		if (nombreProducto.equals("")) {

			request.setAttribute("errMessage", "No puedes dejar el nombre del producto vacio");
			RequestDispatcher rd = request.getRequestDispatcher("ModificacionProductoAdm.jsp");
			rd.forward(request, response);

		} else if (precioProducto.equals("")) {

			request.setAttribute("errMessage", "No puedes dejar el precio vacio");
			RequestDispatcher rd = request.getRequestDispatcher("ModificacionProductoAdm.jsp");
			rd.forward(request, response);

		} else if (cantidadProducto.equals("")) {

			request.setAttribute("errMessage", "No puedes dejar la cantidad del producto vacia");
			RequestDispatcher rd = request.getRequestDispatcher("ModificacionProductoAdm.jsp");
			rd.forward(request, response);

		} else if (nombreProducto.length() >= 20) {
			request.setAttribute("errMessage", "El nombre del producto no puede tener más de veinte letras");
			RequestDispatcher rd = request.getRequestDispatcher("ModificacionProductoAdm.jsp");
			rd.forward(request, response);

		} else if (precioProducto.length() >= 5) {
			request.setAttribute("errMessage", "El precio no puede tener más de 5 dígitos");
			RequestDispatcher rd = request.getRequestDispatcher("ModificacionProductoAdm.jsp");
			rd.forward(request, response);

		} else if (cantidadProducto.length() >= 5) {

			request.setAttribute("errMessage", "La cantidad no puede tener más de 5 dígitos");
			RequestDispatcher rd = request.getRequestDispatcher("ModificacionProductoAdm.jsp");
			rd.forward(request, response);

		} else if (partes.length > 1 && partes[1].length() > 2) {
			request.setAttribute("errMessage", "El precio no puede tener más de dos decimales");
			RequestDispatcher rd = request.getRequestDispatcher("ModificacionProductoAdm.jsp");
			rd.forward(request, response);

		} else if (producto.esNumero(precioProducto) == false) {
			request.setAttribute("errMessage", "Tienes que ingresar números y no letras o carácteres especiales");
			RequestDispatcher rd = request.getRequestDispatcher("ModificacionProductoAdm.jsp");
			rd.forward(request, response);

		} else if (producto.esNumero(cantidadProducto) == false) {
			request.setAttribute("errMessage", "Tienes que ingresar números y no letras o carácteres especiales");
			RequestDispatcher rd = request.getRequestDispatcher("ModificacionProductoAdm.jsp");
			rd.forward(request, response);

		} else if (producto.esNumero(nombreProducto) == true) {
			request.setAttribute("errMessage", "No puedes ingresar un número en el nombre");
			RequestDispatcher rd = request.getRequestDispatcher("ModificacionProductoAdm.jsp");
			rd.forward(request, response);

		} else if (producto.validacion(nombreProducto) == true) {
			request.setAttribute("errMessage", "No puedes ingresar un número en el nombre del producto");
			RequestDispatcher rd = request.getRequestDispatcher("ModificacionProductoAdm.jsp");
			rd.forward(request, response);

		} else {

			try {

				int idproductoInt = Integer.parseInt(idProducto);

				double precioDouble = Double.parseDouble(precioProducto);

				int cantidadPedidoInt = Integer.parseInt(cantidadProducto);

				con = pool.getConnection();

				sql = "UPDATE productos SET nombreProducto=?, precioProducto=?, cantidadProducto=? WHERE idProducto=?";
				prestmt = con.prepareStatement(sql);
				prestmt.setString(1, nombreProducto);
				prestmt.setDouble(2, precioDouble);
				prestmt.setInt(3, cantidadPedidoInt);
				prestmt.setInt(4, idproductoInt);

				int sentencia = prestmt.executeUpdate();

				RequestDispatcher rd = request.getRequestDispatcher("ConsultaProductoAdm.jsp");
				rd.forward(request, response);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
