<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page session="true"%>
<%@ page import="modelo.Usuario"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="controlador.Login"%>
<%
// Verificar si el usuario está autenticado
HttpSession misession = request.getSession();  
String usuarioAutenticado = (String)misession.getAttribute("Adm");
if(usuarioAutenticado!= null)
{
if((usuarioAutenticado.equals("Adm"))){
%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/css/bootstrap.min.css">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link href="css/css.css" rel="stylesheet" />
<div class="pb-1">
	<br>
	<br>
	<nav
		class="navbar navbar-expand-lg navbar-light bg-light fixed-top pb-2 pt-3">
		<div class="ms-5 ">
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
		<div class="collapse navbar-collapse justify-content-end mx-5 "
			id="navbarNav">
			<ul class="navbar-nav">
				<li class="nav-item active"><a class="nav-link" href="logout"><font
						color="black">Logout</font></a></li>
				<li class="nav-item active"><a class="nav-link"
					href="ConsultaLineaPedidoAdm.jsp"><font color="red">Volver</font></a>
				</li>
			</ul>
		</div>
	</nav>
</div>
</head>
<body>
	<div class="pb-5">
		<br>
		<br>
		<div class="pt-5 pb-5">
			<div align="center">	
				<form name="form"
					action="<%=request.getContextPath()%>/AltaLineaPedidoAdm"
					method="post" style="width: 23rem;">
					<h3 class="fw-normal mb-3 pb-2"
						style="letter-spacing: 1px; text-align: center;">
						<font color="sky blue">Alta linea Pedido</font>
					</h3>
					<br>
				<h4>
					<span style="color: red"><%=(request.getAttribute("errMessage") == null) ? "" : request.getAttribute("errMessage")%></span>
				</h4><br>	
					<div class="form-outline mb-4">
						<label class="form-label" for="idUsuario"><font
							color="black">ID del pedidoFK</font></label> <input type="number"
							id="idLineaPedido" class="form-control form-control-lg"
							name="idPedidoFK" />
					</div>
					<div class="form-outline mb-4">
						<label class="form-label" for="fechaPedido"><font
							color="black">ID del roductoFK</font></label> <input type="text"
							id="idLineaPedido" class="form-control form-control-lg"
							name="idLineaPedido" />
					</div>
					<div class="form-outline mb-4">
						<label class="form-label" for="pedidoPrecio"><font
							color="black">Cantidad de la linea de pedido</font></label> <input
							type="text" id="cantidadLineaPedido"
							class="form-control form-control-lg" name="cantidadLineaPedido" />
					</div>
					<div class="form-outline mb-4">
						<label class="form-label" for="pedidoPrecio"><font
							color="black">Precio de la linea de pedido</font></label> <input
							type="text" id="LineaPedidoPrecio"
							class="form-control form-control-lg" name="LineaPedidoPrecio" />
					</div>
					<div class="pt-5">
						<div class="pt-1 mb-4 pb-3">
							<button type="submit" name="btnEnviar" value="enviar"
								class="btn btn-success" style="width: 84px;">Add</button>
						</div>
					</div>
				</form>
				<br>				
			</div>
		</div>
	</div>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
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
