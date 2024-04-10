package controlador;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import modelo.LineaPedido;

/**
 * Servlet implementation class ConsultaModificacionLineaPedido
 */
@WebServlet("/ConsultaModificacionLineaPedidoAdm")
public class ConsultaModificacionLineaPedidoAdm extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataSource pool;       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ConsultaModificacionLineaPedidoAdm() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void init(ServletConfig config) throws ServletException {
        try {
            super.init(config);
            InitialContext ctx = new InitialContext();
            pool = (DataSource) ctx.lookup("java:comp/env/jdbc/customers");
            if (pool == null) {
                throw new ServletException("DataSource desconocida 'customers'");
            }
        } catch (NamingException ex) {
            throw new ServletException(ex);
        }
    }
    
    
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		String idLineaPedidoString = request.getParameter("idLineaPedido");
		
	        int idLineaPedidoInt = Integer.parseInt(idLineaPedidoString);

        try (Connection con = pool.getConnection()) {
            String consultaSQL = "SELECT lineapedido.idLineaPedido, lineapedido.idPedidoFK2,lineapedido.idProductoFK3, productos.nombreProducto as nombre_producto, lineapedido.lineaPedidoCantidadProducto, lineapedido.lineaPedidoPrecio FROM lineapedido JOIN productos ON lineapedido.idProductoFK3 = productos.idProducto where idLineaPedido=?";
            // Preparar y ejecutar la consulta
            try (PreparedStatement pstmt = con.prepareStatement(consultaSQL)) {
                pstmt.setInt(1,idLineaPedidoInt);

                // Obtener el resultado de la consulta
                try (ResultSet resultadoConsulta = pstmt.executeQuery()) {
                    if (resultadoConsulta.next()) {
                        int idLineaPedido = resultadoConsulta.getInt("idLineaPedido");
                        int idPedidoFK = resultadoConsulta.getInt("idPedidoFK2");
                        int idProductoFK = resultadoConsulta.getInt("idProductoFK3");
                        String nombre_producto = resultadoConsulta.getString("nombre_producto");
                        int lineaPedidoCantidadProducto = resultadoConsulta.getInt("lineaPedidoCantidadProducto");
                       double  lineaPedidoPrecio = resultadoConsulta.getDouble("lineaPedidoPrecio");
                        
                        
               LineaPedido  LineaPedido = new LineaPedido (idLineaPedido,idPedidoFK,idProductoFK,nombre_producto,lineaPedidoCantidadProducto,lineaPedidoPrecio,"",pool);

                        HttpSession session = request.getSession(true);
                        session.setAttribute("LineaPedido",LineaPedido );
                        request.getRequestDispatcher("ModificacionLineaPedidoAdm.jsp").forward(request, response);
                    } else {
                        request.setAttribute("errorMessage", "No se encontró ningún pedido con el ID especificado.");
                        request.getRequestDispatcher("ModificacionLineaPedidoAdm.jsp").forward(request, response);
                    }
                }
            } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}

}
