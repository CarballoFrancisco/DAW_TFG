<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="modelo.Usuario"%>    
<%
   HttpSession misession = request.getSession();
Usuario usuarioAutenticado = (Usuario) misession.getAttribute("Usuario");
String usuarioAutenticado2 = (String)misession.getAttribute("Adm");
    if(usuarioAutenticado!= null)
    {
    if((usuarioAutenticado2.equals("Adm"))) {
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/css/bootstrap.min.css">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="css/css.css" rel="stylesheet" />
  <style>
    .navbar-toggler-icon {
      font-size: 1,5rem; /* Ajusta el tamaño del icono del botón hamburguesa */
    }
    .navbar-nav {
      margin-top: 6px; /* Ajusta la separación entre la barra de navegación y los enlaces */
    }
  </style>
  <title>Barra de Navegación</title>
</head>
<body style="padding: 0; margin: 0;">
  <div class="container-fluid">
    <div class="pb-5">
      <nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top">
        <div class="ms-4">
          <a class="navbar-brand" href="#">
             <img src="img/icono.png" width="60" height="60" class="d-inline-block align-top" alt="Icono">
          </a>
        </div>
        <button class="navbar-toggler navbar-toggler-dark ms-2 mx-4" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse justify-content-end mx-4 " id="navbarNav">
          <ul class="navbar-nav">
          <li class="nav-item active">
            <a class="nav-link" href="logout"><font color="black">Logout</font></a>
            </li>
            <li class="nav-item active">
              <a class="nav-link" href="SesionAdministrador.jsp"><font color="black">Sesion</font></a>
            </li>
          </ul>
        </div>
      </nav>
    </div>
<div>
<div class="pb-4 pt-5 text-center"><br><br><br>
<h1>Gestión Administrador</h1>
</div>
</div>
    <div class="row col-12 mx-0 pt-5 pb-3">
        <div class="col-lg-6 pb-5">
        <div class="pb-2 pt-2 text-center">
        <h3><font color="red">Usuarios</font></h3>
        </div>
          <a href="ConsultaUsuarioAdm.jsp">
          <img id="imagen2" src="img/user.png" class="d-block w-100" />
          </a>
        </div>
        <div class="col-lg-6">
         <div class="pb-2 pt-2 text-center">
        <h3><font color="red">Pedidos</font></h3>
        </div>
          <a href="ConsultaPedidoAdm.jsp">
            <img id="imagen2" src="img/pedidos.png" class="d-block w-100" />
          </a>
        </div> 
    </div>
    <div class="row col-12 mx-0 pt-5">
      <div class="col-lg-6 pb-5">
       <div class="pb-2 pt-2 text-center">
        <h3><font color="red">Productos</font></h3>
        </div>
        <a href="ConsultaProductoAdm.jsp">
          <img id="imagen2" src="img/hamburguesa.png" class="d-block w-100" />
        </a>
      </div>
      <div class="col-lg-6 pt-2">
       <div class="pb-2 pt-2 text-center">
        <h3><font color="red">Lineas de pedidos</font></h3>
        </div>
        <a href="ConsultaLineaPedidoAdm.jsp">
          <img id="imagen2" src="img/lineaPedidos.png" class="d-block w-100" />
          </a>
      </div> 
  </div>
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

