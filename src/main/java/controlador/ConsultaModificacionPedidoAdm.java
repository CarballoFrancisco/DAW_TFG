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
import modelo.Pedido;
import modelo.Usuario;

/**
 * Servlet implementation class ConsultaModificacionPedidosServlet
 */
@WebServlet("/ConsultaModificacionPedidoAdm")
public class ConsultaModificacionPedidoAdm extends HttpServlet {
	private static final long serialVersionUID = 1L;
	    private DataSource pool;     
   
    public ConsultaModificacionPedidoAdm() {
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String idPedidoString = request.getParameter("idPedido");

	    // Verificar si idPedidoString es un número entero válido
	    if (idPedidoString != null && idPedidoString.matches("\\d+")) {
	        int idPedidoInt = Integer.parseInt(idPedidoString);

	        try {
	            // Obtener una conexión desde el pool
	            try (Connection con = pool.getConnection()) {
	                String consultaSQL = "SELECT pedidos.*, users.username AS nombre_usuario FROM pedidos INNER JOIN users ON pedidos.idUsuarioFK1 = users.idusers WHERE pedidos.idpedido = ?";
	                // Preparar y ejecutar la consulta
	                try (PreparedStatement pstmt = con.prepareStatement(consultaSQL)) {
	                    pstmt.setInt(1, idPedidoInt);

	                    // Obtener el resultado de la consulta
	                    try (ResultSet resultadoConsulta = pstmt.executeQuery()) {
	                        if (resultadoConsulta.next()) {
	                            int idPedido = resultadoConsulta.getInt("idpedido");
	                            int idUsuarioFK1 = resultadoConsulta.getInt("idUsuarioFK1");
	                            String fechaPedido = resultadoConsulta.getString("fechaPedido");
	                            double pedidoPrecio = resultadoConsulta.getDouble("pedidoPrecio");
	                            String nombre_usuario = resultadoConsulta.getString("nombre_usuario");	                           
	                            Pedido pedido = new Pedido(idPedido, idUsuarioFK1, fechaPedido, pedidoPrecio,nombre_usuario, pool);
	                            HttpSession session = request.getSession(true);
	                            session.setAttribute("Pedido", pedido);
	                            request.getRequestDispatcher("ModificacionPedidoAdm.jsp").forward(request, response);
	                        } else {
	                            request.setAttribute("errorMessage", "No se encontró ningún pedido con el ID especificado.");
	                            request.getRequestDispatcher("bbbbb.jsp").forward(request, response);
	                        }
	                    }
	                }
	            }
	        } catch (SQLException e) {
	            // Manejo de la excepción SQL
	            e.printStackTrace();
	            response.sendRedirect("aaaa.jsp");
	        }
	    } else {
	        // Si idPedidoString no es un número entero válido, redirigir a una página de error
	        response.sendRedirect("paginaDeError.jsp");
	    }
	}

}
