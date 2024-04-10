<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Producto" %>
<%@ page import="modelo.Usuario" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="java.util.Base64" %>
<%
    // Verificar si el usuario está autenticado
    HttpSession misession = request.getSession();
    Usuario usuarioAutenticado = (Usuario) misession.getAttribute("Usuario2");
    if(usuarioAutenticado != null && usuarioAutenticado.getRole().equals("User")) {
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/css/bootstrap.min.css">
    <link href="css/css.css" rel="stylesheet" />
    <style>
        .table th {
            background-color: #28a745;
            color: #ffffff;
        }

        .action-btn {
            width: 90px;
        }
        
        .verde-fondo {
            background-color: #a3d6a6;
        }
        
        /* Estilos de la alerta */
        .alert-container {
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            z-index: 10000;
        }

        .alert-box {
            padding: 30px;
            background-color: #007bff; /* Color de fondo azul */
            color: white;
            border-radius: 10px;
            border: 1px solid #ccc;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.2);
            position: relative;
            max-width: 80%;
            text-align: center;
        }

        .close-btn {
            position: absolute;
            top: 10px;
            right: 10px;
            color: white;
            font-size: 30px;
            font-weight: bold;
            cursor: pointer;
        }

        .alert-message {
            font-size: 18px;
            line-height: 1.5;
        }
    </style>
</head>
<body style="background: linear-gradient(to bottom, #add8e6, #ffffff);">
    <nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top pb-1 pt-3">
        <div class="ms-5 ">
            <a class="navbar-brand" href="#">
                <img src="img/icono.png" width="60" height="60" class="d-inline-block align-top" alt="Icono">
            </a>
        </div>
        <button class="navbar-toggler navbar-toggler-dark ms-2 mx-4" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse justify-content-end mx-5 " id="navbarNav">
            <ul class="navbar-nav">
                <li class="nav-item active">
                    <a class="nav-link" href="logout">
                        <font color="black">Logout</font>
                    </a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link" href="PaginaPrincipalUsuario.jsp">
                        <font color="red">Volver</font>
                    </a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link" href="CarroDeCompraConfirmacion.jsp">
                        <img src="img/carrito.png" alt="Carro de compras" style="vertical-align: middle; width: 30px;">
                        <font color="blue">Mi carro de compra</font>
                    </a>
                </li>                
                <li class="nav-item active">
                    <a class="nav-link" href="SesionUsuario.jsp"><font color="black">Sesion</font></a>
                </li> 
            </ul>
        </div>
    </nav><br><br><br>
    <div class="pt-5 pb-3"><h3 style="font-family: 'Indie Flower', cursive; font-size: 28px; font-weight: bold; color: #000; text-align: center; margin-bottom: 20px;">Lista de nuestros platos</h3></div>
    <div class="container mt-2 pt-3 pb-3">
        <div class="row">
            <% 
            Producto producto = new Producto();
            List<Producto> listaProductos = producto.listar(); // Obtener la lista de productos desde tu lógica de negocio
            for (Producto producto1 : listaProductos) {    
            %> 
            <div class="col-md-4 col-sm-6 col-12 pb-4"> 
                <div class="card h-100">
  <div class="card-body text-center verde-fondo">
                            <label style="color: white;"><%=producto1.getNombreProducto()%></label>
                        </div>
                        <div class="card-body text-center">
                            <i><%=producto1.getPrecioProducto()%>€</i>
                            <div>
                                <img src="data:image/jpeg;base64, <%= new String(Base64.getEncoder().encode(producto1.listarImg(producto1.getIdProducto()))) %>" class="img-fluid" alt="Imagen flexible con tamaño máximo" style="max-height: 200px;">
                            </div>
                        </div>
                        <div class="card-footer text-center">
                            <form action="CarritoServlet" method="post">
                                <input type="hidden" name="idProducto" value="<%=producto1.getIdProducto()%>">
                                <button type="submit" class="btn btn-outline-info" style="width: 160px;">Agregar al Carrito</button>
                            </form>
                        </div>
                    </div>
                </div>
                <% } %>
            </div>
        </div>
    </div>
    <footer class="footer pt-5">
        <div class="container">
            <div class="row align-items-center pt-5">
                <!-- Contenido del pie de página omitido para mayor claridad -->
            </div>
        </div>
    </footer>
    <div class="alert-container">
        <% if (request.getAttribute("mensaje") != null) { %>
            <div class="alert-box">
                <span class="close-btn" onclick="cerrarAlerta();">&times;</span>
                <div class="alert-message">
                    <%= request.getAttribute("mensaje") %>
                </div>
            </div>
        <% } %>
    </div>
    <script>
        // Función para cerrar la alerta
        function cerrarAlerta() {
            var alertContainer = document.querySelector('.alert-container');
            alertContainer.style.display = 'none';
        }

        // Mostrar la alerta
        document.addEventListener("DOMContentLoaded", function() {
            var alertBox = document.querySelector('.alert-box');
            if (alertBox) {
                alertBox.style.display = 'block';
                setTimeout(cerrarAlerta, 5000); // Cerrar la alerta después de 5 segundos
            }
        });
    </script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>      
</body>
</html>
<%
    } else {
        response.sendRedirect("Login.jsp");
    }
%>                   

