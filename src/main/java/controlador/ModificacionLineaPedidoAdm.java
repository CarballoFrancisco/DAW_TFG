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

import modelo.LineaPedido;

/**
 * Servlet implementation class ModificacionLineaPedidoFinal
 */
@WebServlet("/ModificacionLineaPedidoAdm")
public class ModificacionLineaPedidoAdm extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataSource pool;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ModificacionLineaPedidoAdm() {
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
        LineaPedido LineaPedido = new LineaPedido();
        String idLineaPedido = request.getParameter("idLineaPedido");
        String idPedidoFK = request.getParameter("idPedidoFK");
        String idProductoFK = request.getParameter("idProductoFK");
        String lineaPedidoCantidadProducto = request.getParameter("lineaPedidoCantidadProducto");
        String lineaPedidoPrecio = request.getParameter("lineaPedidoPrecio");
        String[] partes = lineaPedidoPrecio.split("\\.");
        LineaPedido lineaPedido = new LineaPedido();
        
        if(idPedidoFK.equals("")) {
        	
        	request.setAttribute("errMessage", "No puedes dejar el id del pedido vacio");
			RequestDispatcher rd = request.getRequestDispatcher("ModificacionLineaPedidoAdm.jsp");
			rd.forward(request, response);
			
        }else if(idProductoFK.equals("")){
        	
        	request.setAttribute("errMessage", "No puedes dejar el id del producto vacio");
			RequestDispatcher rd = request.getRequestDispatcher("ModificacionLineaPedidoAdm.jsp");
			rd.forward(request, response);
        	
        }else if(lineaPedidoCantidadProducto.equals("")){
        	
        	request.setAttribute("errMessage", "No puedes dejar la cantidad del producto vacia");
			RequestDispatcher rd = request.getRequestDispatcher("ModificacionLineaPedidoAdm.jsp");
			rd.forward(request, response);
        	
        }else if(lineaPedidoPrecio.equals("")){
        	
        	request.setAttribute("errMessage", "No puedes dejar el precio vacio");
			RequestDispatcher rd = request.getRequestDispatcher("ModificacionLineaPedidoAdm.jsp");
			rd.forward(request, response);
        	
        }else if (lineaPedido.esNumero(lineaPedidoCantidadProducto) == false) {
			request.setAttribute("errMessage", "No es un número");
			RequestDispatcher rd = request.getRequestDispatcher("ModificacionLineaPedidoAdm.jsp");
			rd.forward(request, response);

		}else if (lineaPedido.esNumero(lineaPedidoPrecio) == false) {
			request.setAttribute("errMessage", "No es un número");
			RequestDispatcher rd = request.getRequestDispatcher("ModificacionLineaPedidoAdm.jsp");
			rd.forward(request, response);

		}else  if (partes.length > 1 && partes[1].length() > 2) {
	        request.setAttribute("errMessage", "El precio no puede tener más de dos decimales");
	        RequestDispatcher rd = request.getRequestDispatcher("ModificacionLineaPedidoAdm.jsp");
			rd.forward(request, response);
	        
	    }else  if (lineaPedidoPrecio.length()>=5) {
	        request.setAttribute("errMessage", "El precio no puede tener más de 5 dígitos");
	        RequestDispatcher rd = request.getRequestDispatcher("ModificacionLineaPedidoAdm.jsp");
			rd.forward(request, response);
	        
	    }else  if (lineaPedidoCantidadProducto.length()>=6) {
	        request.setAttribute("errMessage", "La cantidad no puede tener más de 6 dígitos");
	        RequestDispatcher rd = request.getRequestDispatcher("ModificacionLineaPedidoAdm.jsp");
			rd.forward(request, response);
	        
	    }else {
	    	
        int idLineaPedidoInt = Integer.parseInt(idLineaPedido);
        int idPedidoFKInt = Integer.parseInt(idPedidoFK);
        int idProductoFKInt = Integer.parseInt(idProductoFK);
        int lineaPedidoCantidadProductoInt = Integer.parseInt(lineaPedidoCantidadProducto);
        double lineaPedidoPrecioDouble = Double.parseDouble(lineaPedidoPrecio);
        
        try {
			if(lineaPedido.verificarExistenciaPedido(idPedidoFKInt)==false) {
				
				request.setAttribute("errMessage", "El pedido no existe");
				RequestDispatcher rd = request.getRequestDispatcher("ModificacionLineaPedidoAdm.jsp");
				rd.forward(request, response);	
				
			}else if(lineaPedido.verificarExistenciaProducto(idProductoFKInt)==false) {
				
				request.setAttribute("errMessage", "El producto no existe");
				RequestDispatcher rd = request.getRequestDispatcher("ModificacionLineaPedidoAdm.jsp");
				rd.forward(request, response);
				
			}else {
				
			try {
			    con = pool.getConnection();
			    sql = "UPDATE lineapedido SET idPedidoFK2=?, idProductoFK3=?,lineaPedidoCantidadProducto=?,lineaPedidoPrecio=? WHERE idLineaPedido=?";
			    prestmt = con.prepareStatement(sql);
			    prestmt.setInt(1, idPedidoFKInt);
			    prestmt.setInt(2, idProductoFKInt);
			    prestmt.setInt(3, lineaPedidoCantidadProductoInt);
			    prestmt.setDouble(4, lineaPedidoPrecioDouble);
			    prestmt.setInt(5, idLineaPedidoInt);

			    int sentencia = prestmt.executeUpdate();

			    RequestDispatcher rd = request.getRequestDispatcher("ConsultaLineaPedidoAdm.jsp");
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
			    } catch (SQLException ex) {
			        ex.printStackTrace();
			    }
			}
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

}

