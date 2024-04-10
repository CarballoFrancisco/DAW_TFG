
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="modelo.Usuario"%>
<%@ page import="modelo.Producto"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%
HttpSession misession = request.getSession();
Usuario usuarioAutenticado = (Usuario) misession.getAttribute("Usuario");
String usuarioAutenticado2 = (String) misession.getAttribute("Adm");
if (usuarioAutenticado != null) {
	if ((usuarioAutenticado2.equals("Adm"))) {
%>
<!DOCTYPE html>
<html lang="es">
<head>
<script
	src="//cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/alertify.min.js"></script>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/css/bootstrap.min.css">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link href="css/css.css" rel="stylesheet" />
<link rel="stylesheet"
	href="//cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/css/alertify.rtl.min.css" />
<link rel="stylesheet"
	href="//cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/css/themes/default.rtl.min.css" />
<link rel="stylesheet"
	href="//cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/css/themes/semantic.rtl.min.css" />
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


.action-btn {
	width: 90px;
}
</style>
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
			<li class="nav-item active"><a class="nav-link"
				href="inicio.jsp"><font color="black">Logout</font></a></li>
			<li class="nav-item active"><a class="nav-link"
				href="PaginaPrincipalDeAdm.jsp"><font color="red">Volver</font></a></li>
		</ul>
	</div>
</nav>
</head>
<body>
	<div class="container pb-3 pt-5">
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
						href="PaginaPrincipalDeAdm.jsp"><font color="red">Volver</font></a>
					</li>
				</ul>
			</div>
		</nav>
	</div>
	<div class="row mx-auto">
		<div class="pb-3 pt-5 pt-3 text-center ">
			<h1>Gestión de Productos</h1>
		</div>
		<div
			class="container mt-5 table-container justify-content-center table-responsive pb-5"
			style="width: 70%">
			<table class="table table-striped table-hover ">
				<thead>
					<tr>
						<th style="text-align: center;">ID del Producto</th>
						<th style="text-align: center;">Nombre del producto</th>
						<th style="text-align: center;">precioProducto</th>
						<th style="text-align: center;">cantidad del producto</th>
						<th style="text-align: center;">Acciones</th>
					</tr>
				</thead>
				<tbody>
					<%
					try {
						Producto producto = new Producto();
						ArrayList<Producto> listaProductos = producto.generarTablaProductos();
						for (Producto producto1 : listaProductos) {
					%>
					<tr>
						<td style="text-align: center;"><%=producto1.getIdProducto()%></td>
						<td style="text-align: center;"><%=producto1.getNombreProducto()%></td>
						<td style="text-align: center;"><%=producto1.getPrecioProducto()%></td>
						<td style="text-align: center;"><%=producto1.getCantidadProducto()%></td>
						<td>
							<div class="pb-1">
								<form action="ConsultaModificacionProductoAdm" method="post">
									<input type="hidden" name="idPedido" id="idPedido"
										value="<%=producto1.getIdProducto()%>">
									<div style="text-align: center;">
										<button type="submit" class="btn btn-primary"
											style="width: 80px;">Update</button>
									</div>
								</form>
							</div>
							<div>
								<%
								if (producto1.verificarExistenciaProductosEnLineaPedidos() == false) {
								%>
								<div style="text-align: center;">
									<button
										onclick="confirmarBorradoProducto('<%=producto1.getIdProducto()%>')"
										class="btn btn-danger" style="width: 80px;">Delete</button>
								</div>
								<%
								} else {
								%>
								<div style="text-align: center;">
									<button class="btn btn-danger" style="width: 80px;" disabled>Delete</button>
								</div>
								<%
								}
								%>
							</div>
						</td>
					</tr>
					<%
					}
					} catch (Exception e) {
					%>
					<tr>
						<td colspan="6">Error al generar la tabla de usuarios: <%=e.getMessage()%></td>
					</tr>
					<%
					}
					%>
				</tbody>
			</table>
			<div class="pt-4" style="text-align: center;">
				<a href="AltaProductoAdm.jsp" class="btn btn-success"
					style="width: 137px;">Add producto</a>
			</div>
		</div>
	</div>
	<script>
        function confirmarBorradoProducto(idProducto) {
            if (confirm('¿Estás seguro que quieres eliminar el producto con ID ' + idProducto + '?')) {
                let url = 'EliminarProductoAdm?productoId=' + idProducto.trim();
                let options = {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                };
                fetch(url, options)
                    .then(response => {
                        if (response.ok) {
                            alertify.success('Producto eliminado con éxito');
                            setTimeout(function () { window.location.reload(); }, 3000);
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
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
<%
} else {
response.sendRedirect("Login.jsp");
}
} else {
response.sendRedirect("Login.jsp");
}
%>