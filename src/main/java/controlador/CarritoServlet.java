package controlador;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.SQLException;
import modelo.Producto;

@WebServlet("/CarritoServlet")
public class CarritoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DataSource pool;

    public void init() throws ServletException {
        try {
            InitialContext ctx = new InitialContext();
            pool = (DataSource) ctx.lookup("java:comp/env/jdbc/customers");
            if (pool == null) {
                throw new ServletException("DataSource 'customers' no encontrado");
            }
        } catch (NamingException ex) {
            throw new ServletException(ex);
        }
    }

    public CarritoServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(true);

        Producto producto = new Producto();

        @SuppressWarnings("unchecked")
        ArrayList<Producto> carrito = (ArrayList<Producto>) (sesion.getAttribute("carrito"));

        ArrayList<Producto> todojuntoProducto = new ArrayList<Producto>();

        String idBoton = request.getParameter("idProducto");
        int idBotonInt = Integer.parseInt(idBoton);
        int idProducto = 0;
        String nombreProducto = null;
        double precioProducto = 0;

        int cantidadProducto = 0;
        double precioTotalProductos = 0;
        InputStream imagenProducto = null;
        double precioIndividial = 0.0;

        int cantidadDelPedido = producto.getCantidadPedidaDeProductoEnCarritoSolicitada();

        try {
            todojuntoProducto = (ArrayList<Producto>) producto.listarProductos(idBotonInt);
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Producto producto1 : todojuntoProducto) {

            idProducto = producto1.getIdProducto();
            nombreProducto = producto1.getNombreProducto();
            precioProducto = producto1.getPrecioProducto();
            cantidadProducto = producto1.getCantidadProducto();
            cantidadDelPedido = producto1.getCantidadPedidaDeProductoEnCarritoSolicitada();
            imagenProducto = producto1.getFoto();
            precioIndividial = producto1.getPrecioProducto();

        }

        if (carrito == null) {
        	
            if (cantidadProducto > 0) {
            	
            	 inicializarCarrito(sesion, producto, idProducto, nombreProducto, precioProducto, cantidadProducto,
                         cantidadDelPedido, imagenProducto, idBotonInt, precioIndividial);
            	
                precioTotalProductos = precioProducto;

                sesion.setAttribute("precioTotal", precioTotalProductos);

                RequestDispatcher rd = request.getRequestDispatcher("CarroDeCompralUsuario.jsp");
                rd.forward(request, response);

            } else {
            	String mensaje = "El producto: "+nombreProducto+" está agotado";
            	request.setAttribute("mensaje", mensaje);
                RequestDispatcher rd = request.getRequestDispatcher("CarroDeCompralUsuario.jsp");
                rd.forward(request, response);

            }

        } else {

            boolean productoYaEnCarrito = false;

            for (Producto productoDentroDelcarrito : carrito) {

                if (productoDentroDelcarrito.getIdProducto() == idProducto) {
                    cantidadDelPedido = productoDentroDelcarrito.getCantidadPedidaDeProductoEnCarritoSolicitada() + 1;

                    if (cantidadProducto >= cantidadDelPedido) {
                        productoDentroDelcarrito.aumentarCantidadPedida();
                        productoDentroDelcarrito.aumentarPrecio(precioProducto);
                        sesion.setAttribute("carrito", carrito);
                        Double precioTotal = (Double) sesion.getAttribute("precioTotal");
                        precioTotal = precioTotal + precioProducto;
                        double numeroRedondeado = Math.round(precioTotal * 1000.0) / 1000.0;
                        sesion.setAttribute("precioTotal", numeroRedondeado);
                        productoYaEnCarrito = true;
                        RequestDispatcher rd = request.getRequestDispatcher("CarroDeCompralUsuario.jsp");
                        rd.forward(request, response);
                        break; // Salir del bucle una vez que se agrega el producto al carrito

                    } else {
                    	String mensaje = "El producto: "+nombreProducto+" está agotado";
                    	request.setAttribute("mensaje", mensaje);
                        RequestDispatcher rd = request.getRequestDispatcher("CarroDeCompralUsuario.jsp");
                        rd.forward(request, response);
                        return; // Salir del método si no hay suficiente cantidad
                    }
                }
            }

            if (!productoYaEnCarrito) { // Solo si la cantidad es mayor que cero

                if (cantidadProducto > 0) {
                    carrito.add(new Producto(idProducto, nombreProducto, precioProducto, cantidadProducto,
                            imagenProducto, pool, 1, precioIndividial));
                    sesion.setAttribute("carrito", carrito);
                    Double precioTotal = (Double) sesion.getAttribute("precioTotal");
                    precioTotal = precioTotal + precioProducto;
                    double numeroRedondeado = Math.round(precioTotal * 1000.0) / 1000.0;
                    sesion.setAttribute("precioTotal", numeroRedondeado);
                    RequestDispatcher rd = request.getRequestDispatcher("CarroDeCompralUsuario.jsp");
                    rd.forward(request, response);
                } else {
                	String mensaje = "El producto: "+nombreProducto+" está agotado";
                	request.setAttribute("mensaje", mensaje);               
                    RequestDispatcher rd = request.getRequestDispatcher("CarroDeCompralUsuario.jsp");
                    rd.forward(request, response);
                }
            }
        }
    }

    private void inicializarCarrito(HttpSession sesion, Producto producto, int idProducto, String nombreProducto,
            double precioProducto, int cantidadProducto, int cantidadDelPedido, InputStream imagenProducto,
            int idBotonInt, double precioIndividial) {

        ArrayList<Producto> carrito = new ArrayList<Producto>();  	
            int cantidad = 1;
            producto.setCantidadPedidaDeProductoEnCarritoSolicitada(cantidad);

            carrito.add(new Producto(idProducto, nombreProducto, precioProducto, cantidadProducto, imagenProducto,
                    pool, cantidad, precioIndividial));
            sesion.setAttribute("carrito", carrito);

       
    }

}
