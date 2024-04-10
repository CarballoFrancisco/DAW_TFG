package controlador;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import modelo.Pedido;
import modelo.Pedido;
import java.sql.Date;
/**
 * Servlet implementation class ModificacionPedidoFinal
 */
@WebServlet("/ModificacionPedidoAdm")
public class ModificacionPedidoAdm extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataSource pool;  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModificacionPedidoAdm() {
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		 String ruta = "";
	        String sql = "";
	        Connection con = null;
	        PreparedStatement prestmt = null;
	        String error;
	        Pedido pedido = new Pedido();

		 String idPedido = request.getParameter("idPedido");
		 String precioPedido = request.getParameter("precio");
         String idUsuarioFK1 = request.getParameter("idUsuario");
         String fechaPedido1 = request.getParameter("fecha");
         String[] partes = precioPedido.split("\\.");
         
		if(idPedido.equals("")) {
			request.setAttribute("errMessage", "No puedes dejar el id del pedido vacio");
             request.getRequestDispatcher("ModificacionPedidoAdm.jsp").forward(request, response);
             
		     }else if(precioPedido.equals("")){
		    	 request.setAttribute("errMessage", "No puedes dejar el precio vacio");
	             request.getRequestDispatcher("ModificacionPedidoAdm.jsp").forward(request, response);
		    	
		     }else if(pedido.validarFecha(fechaPedido1)==true){
		    	 request.setAttribute("errMessage", "Error en la fecha. El formato debe ser: dd/MM/yyyy");
	             request.getRequestDispatcher("ModificacionPedidoAdm.jsp").forward(request, response); 
		     }else if(idUsuarioFK1.equals("")) {
		    	 request.setAttribute("errMessage", "No puedes dejar el id del usuario vacio");
	             request.getRequestDispatcher("ModificacionPedidoAdm.jsp").forward(request, response);
		     }else if(pedido.buscarUsuario(idUsuarioFK1)==false) {
		    	 
		    	 request.setAttribute("errMessage", "El usuario no existe");
	             request.getRequestDispatcher("ModificacionPedidoAdm.jsp").forward(request, response);
	             
		     }else  if (partes.length > 1 && partes[1].length() > 2) {
		    	        request.setAttribute("errMessage", "El precio no puede tener más de dos decimales");
		    	        request.getRequestDispatcher("ModificacionPedidoAdm.jsp").forward(request, response);
		     }else  if (precioPedido.length() >=6) {
		    	        request.setAttribute("errMessage", "El precio no puede tener más de 5 dígitos");
		    	        request.getRequestDispatcher("ModificacionPedidoAdm.jsp").forward(request, response);
		      }else if (pedido.esNumero(precioPedido)==false) {
	    	    	request.setAttribute("errMessage", "Tienes que ingresar números y no letras o carácteres especiales");
	    	        RequestDispatcher rd = request.getRequestDispatcher("ModificacionPedidoAdm.jsp");
					rd.forward(request, response);
	    	    }else {
		     
			int idPedidoEntero = Integer.parseInt(idPedido);
			String fechaPedido = pedido.convertirFechaEuropeaAAmericana(fechaPedido1);
			int idUsuarioEntero = Integer.parseInt(idUsuarioFK1);
			Double pedidoPrecioEntero =  Double.parseDouble(precioPedido);
			
			 try {
				con = pool.getConnection();
				 sql = "UPDATE pedidos SET idUsuarioFK1=?, fechaPedido=?, pedidoPrecio=? WHERE idpedido=?";
	             prestmt = con.prepareStatement(sql);
	             prestmt.setInt(1,idUsuarioEntero);
	             prestmt.setString(2,fechaPedido);
	             prestmt.setDouble(3,pedidoPrecioEntero);
	             prestmt.setInt(4,idPedidoEntero);

	             int sentencia = prestmt.executeUpdate();

	             RequestDispatcher rd = request.getRequestDispatcher("ConsultaPedidoAdm.jsp");
	             rd.forward(request, response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
