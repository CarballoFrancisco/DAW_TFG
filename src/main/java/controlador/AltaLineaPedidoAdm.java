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
import modelo.LineaPedido;

/**
 * Servlet implementation class AltaLineaPedido
 */
@WebServlet("/AltaLineaPedidoAdm")
public class AltaLineaPedidoAdm extends HttpServlet {
	private static final long serialVersionUID = 1L;
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

	public AltaLineaPedidoAdm() {
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

		String sql = "";
		Connection con = null;
		PreparedStatement prestmt = null;
		String idPedidoFk = request.getParameter("idPedidoFK");
		String idProductoFk = request.getParameter("idLineaPedido");
		String cantidadLineaPedido = request.getParameter("cantidadLineaPedido");
		String LineaPedidoPrecio = request.getParameter("LineaPedidoPrecio");
		String[] partes = LineaPedidoPrecio.split("\\.");
		LineaPedido lineaPedido = new LineaPedido();

		if (idPedidoFk.equals("")) {
			request.setAttribute("errMessage", "No puedes dejar el id del pedido vacio");
			RequestDispatcher rd = request.getRequestDispatcher("AltaLineaPedidoAdm.jsp");
			rd.forward(request, response);

		} else if (idProductoFk.equals("")) {
			request.setAttribute("errMessage", "No puedes dejar el id del producto vacio");
			RequestDispatcher rd = request.getRequestDispatcher("AltaLineaPedidoAdm.jsp");
			rd.forward(request, response);

		} else if (cantidadLineaPedido.equals("")) {
			request.setAttribute("errMessage", "No puedes dejar la cantidad vacia");
			RequestDispatcher rd = request.getRequestDispatcher("AltaLineaPedidoAdm.jsp");
			rd.forward(request, response);

		} else if (LineaPedidoPrecio.equals("")) {
			request.setAttribute("errMessage", "No puedes dejar el precio vacio");
			RequestDispatcher rd = request.getRequestDispatcher("AltaLineaPedidoAdm.jsp");
			rd.forward(request, response);

		} else if (cantidadLineaPedido.length()>=4) {
			request.setAttribute("errMessage", "La cantidad no puede tener más de 3 dígitos");
			RequestDispatcher rd = request.getRequestDispatcher("AltaLineaPedidoAdm.jsp");
			rd.forward(request, response);

		}else if (lineaPedido.esNumero(LineaPedidoPrecio) == false) {
			request.setAttribute("errMessage", "No es un número");
			RequestDispatcher rd = request.getRequestDispatcher("AltaLineaPedidoAdm.jsp");
			rd.forward(request, response);

		}else if (lineaPedido.esNumero(cantidadLineaPedido) == false) {
			request.setAttribute("errMessage", "No es un número");
			RequestDispatcher rd = request.getRequestDispatcher("AltaLineaPedidoAdm.jsp");
			rd.forward(request, response);

		}else  if (partes.length > 1 && partes[1].length() > 2) {
	        request.setAttribute("errMessage", "El precio no puede tener más de dos decimales");
	        RequestDispatcher rd = request.getRequestDispatcher("AltaLineaPedidoAdm.jsp");
			rd.forward(request, response);
	        
	    }else {

			int idPedidoFkInt = Integer.parseInt(idPedidoFk);
			int idProductoFkInt = Integer.parseInt(idProductoFk);
			int cantidadLineaPedidoInt = Integer.parseInt(cantidadLineaPedido);
			double LineaPedidoPrecioDouble = Double.parseDouble(LineaPedidoPrecio);

			try {

				if (lineaPedido.verificarExistenciaProducto(idProductoFkInt) == false) {
					request.setAttribute("errMessage", "El producto no existe");
					RequestDispatcher rd = request.getRequestDispatcher("AltaLineaPedidoAdm.jsp");
					rd.forward(request, response);

				}else if (lineaPedido.verificarExistenciaPedido(idPedidoFkInt) == false) {
					request.setAttribute("errMessage", "El pedido no existe");
					RequestDispatcher rd = request.getRequestDispatcher("AltaLineaPedidoAdm.jsp");
					rd.forward(request, response);

				}else if (lineaPedido.esNumero(LineaPedidoPrecio) == false) {
					request.setAttribute("errMessage", "No es un número");
					RequestDispatcher rd = request.getRequestDispatcher("AltaLineaPedidoAdm.jsp");
					rd.forward(request, response);

				}else if (lineaPedido.validacion(LineaPedidoPrecio) == true) {
					request.setAttribute("errMessage", "No es un número");
					RequestDispatcher rd = request.getRequestDispatcher("AltaLineaPedidoAdm.jsp");
					rd.forward(request, response);

				}else if (lineaPedido.validacion(cantidadLineaPedido) == true) {
					request.setAttribute("errMessage", "No es un número");
					RequestDispatcher rd = request.getRequestDispatcher("AltaLineaPedidoAdm.jsp");
					rd.forward(request, response);

				} else {

					con = pool.getConnection();
					sql = "INSERT INTO lineaPedido(idPedidoFK2,idProductoFK3,lineaPedidoCantidadProducto,lineaPedidoPrecio) VALUES(?,?,?,?)";
					prestmt = con.prepareStatement(sql);
					prestmt.setInt(1, idPedidoFkInt);
					prestmt.setInt(2, idProductoFkInt);
					prestmt.setInt(3, cantidadLineaPedidoInt);
					prestmt.setDouble(4, LineaPedidoPrecioDouble);
					int sentencia = prestmt.executeUpdate();
					
					//RequestDispatcher rd = request.getRequestDispatcher("ConsultaLineaPedidoAdm.jsp");
					//rd.forward(request, response);
					
					response.sendRedirect("ConsultaLineaPedidoAdm.jsp");
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}

	}

}
