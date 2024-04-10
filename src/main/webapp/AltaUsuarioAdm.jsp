<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page session="true"%>
<%@ page import="modelo.Usuario"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="controlador.Login"%>
<%
// Verificar si el usuario est� autenticado
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
<div class="pb-5">
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
					href="ConsultaUsuarioAdm.jsp"><font color="red">Volver</font></a></li>
			</ul>
		</div>
	</nav>
</div>
</head>
<body>
	<div>
		<div class="pt-5 pb-5">
			<div align="center">
				<form name="form"
					action="<%=request.getContextPath()%>/AltaUsuarioAdm" method="post"
					style="width: 23rem;">
					<h3 class="fw-normal mb-3 pb-3"
						style="letter-spacing: 1px; text-align: center;">
						<font color="sky blue">Alta Usuario</font>
					</h3>
					<h4>
					<span style="color: red"><%=(request.getAttribute("errMessage") == null) ? "" : request.getAttribute("errMessage")%></span>
				    </h4><br>
					<div class="form-outline mb-4">
						<label class="form-label" for="form2Example18"><font
							color="black">Nombre de usuario</font></label> <input type="text"
							id="username" class="form-control form-control-lg"
							name="username" />
					</div>
					<div class="form-outline mb-4">
						<label class="form-label" for="form2Example28"><font
							color="black">Password</font></label> <input type="password"
							id="password" class="form-control form-control-lg"
							name="password" />
					</div>
					<div class="form-outline mb-4">
						<label class="form-label" for="form2Example38"><font
							color="black">Confirmar Password</font></label> <input type="password"
							id="confirmPassword" class="form-control form-control-lg"
							name="confirmPassword" />
					</div>
					<div class="form-outline mb-4">
						<label class="form-label" for="form2Example28"><font
							color="black">Email</font></label> <input type="text" name="email"
							id="email" class="form-control form-control-lg" />
					</div>
					<div class="form-outline mb-4">
						<label class="form-label" for="form2Example28"><font
							color="black">Nombre y apellidos</font></label> <input type="text" name="nombreYApellidos"
							id="nombreYApellidos" class="form-control form-control-lg" />
					</div>
					<div class="form-outline mb-4">
						<label class="form-label" for="form2Example28"><font
							color="black">Direcci�n</font></label> <input type="text" name="direccion"
							id="direccion" class="form-control form-control-lg" />
					</div>
					<div class="pt-5">
						<div class="pt-1 mb-4 pb-3">
							<button type="submit" name="btnEnviar" value="enviar"
								class="btn btn-success" style="width: 84px;">Add</button>
						</div>
					</div>

				</form>				
			</div>
		</div>
	</div>
	<div></div>
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