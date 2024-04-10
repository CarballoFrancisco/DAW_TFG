<%@ page import="java.util.ArrayList"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="modelo.Producto"%>
<%@ page import="modelo.Usuario"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.Base64"%>
<%
    HttpSession misession = request.getSession();
    Usuario usuarioAutenticado = (Usuario) misession.getAttribute("Usuario2");
    if(usuarioAutenticado != null && usuarioAutenticado.getRole().equals("User")) {
%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport"
    content="width=device-width, initial-scale=1, shrink-to-fit=no">
<title>Productos en el Carrito</title>
<link rel="stylesheet"
    href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/css/bootstrap.min.css">
<link rel="stylesheet"
    href="//cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/css/alertify.min.css">
<link rel="stylesheet"
    href="//cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/css/themes/default.min.css">
<link rel="stylesheet" href="css/css.css">
<!-- SweetAlert -->
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@10"></script>
<style>
body {
    font-family: Arial, sans-serif;
    background-color: linear-gradient(120deg, #fbc2eb, #a6c1ee);
    /* Degradado de rosa pastel */
    background-image: linear-gradient(120deg, #a1c4fd 0%, #c2e9fb 100%);
    /* Degradado de fondo */
    padding-top: 80px;
    /* Ajusta el espacio sobre el contenido para no ocultar por la barra de navegación */
}
.table th {
    background-color: #007bff;
    color: #ffffff;
}
.action-btn {
    width: 90px;
}
/* Estilo para la barra de navegación */
.navbar-toggler-icon {
    font-size: 1.5rem;
    /* Ajusta el tamaño del icono del botón hamburguesa */
}
.navbar-nav {
    margin-top: 6px;
    /* Ajusta la separación entre la barra de navegación y los enlaces */
}
table {
    width: 80%;
    margin: 0 auto;
    border-collapse: collapse;
    margin-bottom: 20px;
    background-color: #ffffff;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
}
th, td {
    padding: 12px;
    text-align: left;
    border-bottom: 1px solid #ddd;
}
th {
    background-color: #f2f2f2;
}
tfoot td {
    background-color: #f2f2f2;
    font-weight: bold;
}
.empty-message {
    text-align: center;
    font-style: italic;
    color: #888;
}
.btn-borrar {
    width: 150px;
    /* Ancho del botón "Borrar Todo" */
}
.btn-pedido {
    width: 180px;
    /* Ancho del botón "Realizar mi pedido" */
}
</style>
</head>
<body>
    <div class="pb-5">
        <nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top">
            <div class="ms-4">
                <a class="navbar-brand" href="#"> <img src="img/icono.png"
                    width="60" height="60" class="d-inline-block align-top" alt="Icono">
                </a>
            </div>
            <button class="navbar-toggler navbar-toggler-dark ms-2 mx-4"
                type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false"
                aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse justify-content-end mx-4 "
                id="navbarNav">
                <ul class="navbar-nav">
                 <li class="nav-item active"><a class="nav-link" href="logout"><font color="black">Logout</font></a></li>
                 <li class="nav-item active"><a class="nav-link" href="CarroDeCompralUsuario.jsp"><font color="red">Volver</font></a></li>
                </ul>
            </div>
        </nav>
    </div>
    <div class="pt-5 pb-5 text-center">
        <h1>Productos en el Carrito</h1>
    </div>
    <div class="align: center">
        <table>
            <thead>
                <tr>
                    <th style="background-color: #007bff; color: #ffffff;">Nombre
                        Producto</th>
                    <th style="background-color: #007bff; color: #ffffff;">Cantidad
                        solicitada</th>
                    <th style="background-color: #007bff; color: #ffffff;">Precio
                        Producto</th>
                    <th style="background-color: #007bff; color: #ffffff;">Imagen</th>
                    <th style="background-color: #007bff; color: #ffffff;">Acción</th>
                </tr>
            </thead>
            <tbody>
                <%
            int confirmacion= 1;   
            double totalCarrito = 0.0; // Valor predeterminado en caso de que precioTotal sea null
            Double precioTotal = (Double) session.getAttribute("precioTotal");
            
            if (precioTotal != null) {
                totalCarrito = precioTotal.doubleValue();
            }            
            ArrayList<Producto> carritoEnSesion = (ArrayList<Producto>) session.getAttribute("carrito");       
            if (carritoEnSesion != null) {
                for (Producto producto : carritoEnSesion) {
            %>
                <tr>
                    <td><%= producto.getNombreProducto() %></td>
                    <td><%= producto.getCantidadPedidaDeProductoEnCarritoSolicitada()%></td>
                    <td><%=producto.getPrecioIndividial()%></td>
                    <td>
                        <%
                        // Codificar la imagen a Base64 solo si aún no se ha codificado
                        String base64Image = producto.getCachedBase64Image();
                        if (base64Image == null) {
                            base64Image = producto.codificarBase64(producto.getFoto());
                            producto.setCachedBase64Image(base64Image);
                        }
                        %>
                        <img src="data:image/jpeg;base64, <%= base64Image %>"
                        class="img-fluid" alt="Imagen flexible con tamaño máximo"
                        style="max-height: 50px;">
                    </td>
                    <td>
                        <form action="EliminarProductoCarritoUsuario" method="post">
                            <input type="hidden" name="idProducto" id="idProducto"
                                value="<%= producto.getIdProducto()%>">
                            <div>
                                <button type="submit" class="btn btn btn-danger"
                                    style="width: 80px;">Borrar</button>
                            </div>
                        </form>
                    </td>
                </tr>
                <%
                }
            }
            else {
            %>
                <tr>
                    <td colspan="3" class="empty-message">No hay productos en el
                        carrito</td>
                </tr>
                <%
            }          
            %>
            </tbody>
            <tfoot>
                <tr>
                    <td colspan="4"
                        style="text-align: right; background-color: #f2f2f2; font-weight: bold;">Total del carrito:</td>
                    <td colspan="1"
                        style="background-color: #f2f2f2; font-weight: bold;"><%=totalCarrito%></td>
                </tr>
            </tfoot>
        </table>
        <div class="row pb-5">
            <div class="col-6 text-center mt-3">
                <form id="formBorrarTodo1" action="EliminarProductoCarritoUsuario" method="post">
                    <input type="hidden" name="borrarTodo" value="borrar">
                    <% if (carritoEnSesion != null && !carritoEnSesion.isEmpty()) { %>
                    <button type="submit" class="btn btn-danger btn-borrar">Borrar Todo</button>
             <% } %>       
                </form>
            </div>
            <div class="col-6 text-center mt-3">
                <% if (carritoEnSesion != null && !carritoEnSesion.isEmpty()) { %>
                    <button onclick="realizarPedido('')" class="btn btn-success" style="width: 110 px; value="<%=confirmacion%>"">Realizar Pedido</button>
                <% } %>
            </div>
        </div>
    </div>
    <script>
    function realizarPedido(confirmacion) {
        // Construye la URL con el parámetro confirmacion
        let url = 'CarroDeComprasComprarUsuario?confirmacion=' + confirmacion.trim();
        
        Swal.fire({
            title: '¿Estás seguro que quieres realizar el pedido?',
            icon: 'question',
            showCancelButton: true,
            confirmButtonText: 'Sí, realizar pedido',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                let options = {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                };
                // Realiza la solicitud fetch utilizando la URL construida
                fetch(url, options)
                    .then(response => {
                        if (response.ok) { 
                            Swal.fire({
                                title: '¡Pedido realizado!',
                                text: 'El pedido se ha realizado con éxito.',
                                icon: 'success',
                                timer: 3000,
                                timerProgressBar: true,
                                onClose: () => {
                                    window.location.reload();
                                }
                            });
                        } else {
                            Swal.fire('Error', 'Hubo un error en la solicitud.', 'error');
                        }
                    })
                    .catch(error => {
                        console.error('Error en la solicitud:', error);
                        Swal.fire('Error', 'Hubo un error en la solicitud.', 'error');
                    });
            }
        });
    }  
    </script>
    <script
        src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>   
</body>
</html>
<%
    } else {
        response.sendRedirect("Login.jsp");
    }
%>

