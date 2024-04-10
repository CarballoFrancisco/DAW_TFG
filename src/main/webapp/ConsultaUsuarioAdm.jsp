<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="modelo.Usuario"%>
<%@ page import="modelo.Pedido"%>
<%@ page import="controlador.EliminarPedidoAdm" %>
<%
// Verificar si el usuario está autenticado
HttpSession misession = request.getSession();
Usuario usuarioAutenticado = (Usuario) misession.getAttribute("Usuario");
String usuarioAutenticado2 = (String)misession.getAttribute("Adm");
if(usuarioAutenticado!= null)
{
if((usuarioAutenticado2.equals("Adm"))){    
%>  
<!DOCTYPE html>
<html lang="es">
<head>
<script src="//cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/alertify.min.js"></script>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/css/bootstrap.min.css">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" rel="stylesheet">
   <link href="css/css.css" rel="stylesheet"/>
  <link rel="stylesheet"
    href="//cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/css/alertify.rtl.min.css" />
<!-- Default theme -->
<link rel="stylesheet"
    href="//cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/css/themes/default.rtl.min.css" />
<!-- Semantic UI theme -->
<link rel="stylesheet"
    href="//cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/css/themes/semantic.rtl.min.css" />
<!-- Bootstrap theme -->
<link rel="stylesheet"
    href="//cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/css/themes/bootstrap.rtl.min.css" />
  <style>
    .table th {
      background-color: #28a745; 
      color: #ffffff; 
      text-align: center;
    }
    .table td {
      text-align: center;
    }
  </style>
</head>
<body>
<div class="pb-5"><br><br>
  <nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top pb-2 pt-3">
    <div class="ms-5">
      <a class="navbar-brand" href="#">
        <img src="img/icono.png" width="60" height="60" class="d-inline-block align-top" alt="Icono">
      </a>
    </div>
    <button class="navbar-toggler navbar-toggler-dark ms-2 mx-4" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation" >
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse justify-content-end mx-5 " id="navbarNav">
      <ul class="navbar-nav">
      <li class="nav-item active">
            <a class="nav-link" href="logout"><font color="black">Logout</font></a>
            </li>
        <li class="nav-item active">
          <a class="nav-link" href="PaginaPrincipalDeAdm.jsp"><font color="red"> Volver</font></a>
        </li>
      </ul>
    </div>
  </nav>
</div>
<div class="row mx-auto">
<div class="pb-3 pt-3 text-center ">
<h1>Gestion de usuarios</h1>
</div>
<div class="container-fluid mt-5 table-container justify-content-center table-responsive pb-5" style="padding: 0 40px;">

    <table class="table table-striped table-hover ">
      <thead>
        <tr>
          <th>ID</th>
          <th>Usuario</th>
          <th>Contraseña</th>         
          <th>Email</th>
          <th>Nombre y apellidos</th>
          <th>Dirección</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <% 
          try {
            ArrayList<Usuario> listaUsuarios = usuarioAutenticado.generarTablaUsuarios();
            for (Usuario user : listaUsuarios) {                   
        %>
          <tr>
            <td><%= user.getId() %></td>
            <td><%= user.getUserName() %></td>
            <td><%= user.getPassword() %></td>
            <td><%= user.getEmail() %></td>
            <td><%= user.getNombreYApellidos()%></td>
            <td><%= user.getDireccion()%></td>
            <td>
            <div class="pb-1">
         <form action="ConsultaModificacionUsuarioAdm" method="post">
             <input type="hidden" name="userId" value="<%= user.getId() %>">
             <div>
            <button type="submit" class="btn btn-primary" style="width: 80px;">Update</button>
            </div>
         </form>
              </div>
              <div>
              <%-- Verificar si la condición está cumplida y desactivar el botón Delete en consecuencia --%>
              <% if (user.isCondicionCumplida()== true) { %>
                  <button onclick="confirmarBorrado('<%= user.getId() %>', '<%= user.getUserName() %>')" class="btn btn-danger" style="width: 80px;">Delete</button>
              <% } else { %>
                  <button class="btn btn-danger" style="width: 80px;" disabled>Delete</button>
              <% } %>
              </div>
              
            </td>
          </tr>
        <% 
            } 
          } catch (Exception e) { 
        %>
          <tr>
            <td colspan="6">Error al generar la tabla de usuarios: <%= e.getMessage() %></td>
          </tr>
        <% 
          } 
        %>
      </tbody>
    </table><br>
    <div class="pt-4" style="text-align: center;">
      <a href="AltaUsuarioAdm.jsp" class="btn btn-success" style="width: 137px;">Add a new user</a>
   </div>
  </div>
</div>
<script>
function confirmarBorrado(idUsuario, nombreUsuario) {

    if (confirm('¿Estás seguro que quieres borrar al usuario con el nombre ' + nombreUsuario + ' y con el id = ' + idUsuario + '?.')) {

     let url = 'EliminarUsuarioAdm?userId=' + idUsuario;

      let options = {

        method: 'POST',

        //Especifica el tipo de contenido que se está enviando o recibiendo en una solicitud HTTP.

        headers: {

          'Content-Type': 'application/json' 
        }

      };

      fetch(url, options)

        .then(response => {

          if (response.ok) {

              alertify.success('Usuario borrado con éxito');

              setTimeout(function(){window.location.reload();}, 3000);

          } else {

              alertify.error('Error en la solicitud');

          }

        })

        .catch(error => {

          console.error('Error en la solicitud:', error);

          alertify.error('Error en la solicitud');

        });

    }

  }
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
<%
    }
else{
         response.sendRedirect("Login.jsp");
    }
}else{
     response.sendRedirect("Login.jsp");
}
%>

