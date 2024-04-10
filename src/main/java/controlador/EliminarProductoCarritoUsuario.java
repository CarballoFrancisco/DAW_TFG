package controlador;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;
import modelo.Producto;

@WebServlet("/EliminarProductoCarritoUsuario")
public class EliminarProductoCarritoUsuario extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public EliminarProductoCarritoUsuario() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idProducto = request.getParameter("idProducto");
        
        String borrarTodo = request.getParameter("borrarTodo");

        if(borrarTodo != null && borrarTodo.equals("borrar")){

        	 // Eliminar todos los elementos del carrito
            HttpSession session = request.getSession();
            session.removeAttribute("carrito");
            session.removeAttribute("precioTotal");	
            RequestDispatcher rd = request.getRequestDispatcher("CarroDeCompraConfirmacion.jsp");
            rd.forward(request, response);
            return;
          
        }else if (idProducto != null && !idProducto.isEmpty()) {
            try {
                int idProductoInt = Integer.parseInt(idProducto);
                HttpSession sesion = request.getSession(true);
                ArrayList<Producto> carritoEnSesion = (ArrayList<Producto>) sesion.getAttribute("carrito");
                Double precioTotal = (Double) sesion.getAttribute("precioTotal");
                
                if (carritoEnSesion != null && !carritoEnSesion.isEmpty()) {
                    Producto productoEliminado = null;
                    for (Producto producto : carritoEnSesion) {
                        if (idProductoInt == producto.getIdProducto()) {
                            int cantidad = producto.getCantidadPedidaDeProductoEnCarritoSolicitada();
                            double precio = producto.getPrecioProducto();
                            double precioIndividual = producto.getPrecioIndividial();
                            double operacion = precioIndividual - precio;
                            
                            if (cantidad > 1) {
                                producto.setCantidadPedidaDeProductoEnCarritoSolicitada(cantidad - 1);
                                producto.setPrecioIndividial(Math.round(operacion * 1000.0) / 1000.0);
                                precioTotal = precioTotal - precio; // Restar solo el precio del producto eliminado
                                sesion.setAttribute("precioTotal", (Math.round(precioTotal * 1000.0) / 1000.0));
                            } else {
                                productoEliminado = producto;
                                precioTotal = precioTotal - precio; // Restar solo el precio del producto eliminado
                                sesion.setAttribute("precioTotal", (Math.round(precioTotal * 1000.0) / 1000.0));
                                
                                if(precioTotal==0.0) {
                                	 HttpSession session = request.getSession();
                                     session.removeAttribute("carrito");
                                     session.removeAttribute("precioTotal");	
                                     RequestDispatcher rd = request.getRequestDispatcher("CarroDeCompraConfirmacion.jsp");
                                     rd.forward(request, response);
                                     return;
                                }
  
                            }
                            break;
                        }
                    }
                    if (productoEliminado != null) {
                        carritoEnSesion.remove(productoEliminado);
                    }
                    // Actualizar la sesión con el carrito modificado
                    sesion.setAttribute("carrito", carritoEnSesion);
                }
                
                // Redirigir de nuevo a la página del carrito
                RequestDispatcher rd = request.getRequestDispatcher("CarroDeCompraConfirmacion.jsp");
                rd.forward(request, response);
                return;
                
            } catch (NumberFormatException e) {
                // Manejar el caso en el que el ID del producto no sea un número válido
                e.printStackTrace(); // o muestra un mensaje de error al usuario
            }
        }
        response.sendRedirect("alguna_pagina_de_error.jsp");
    }
}

