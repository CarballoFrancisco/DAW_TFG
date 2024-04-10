<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true"%>
<%@ page import="modelo.Usuario"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="controlador.Login"%>
<%
HttpSession misession = request.getSession();
String usuarioAutenticado = (String) misession.getAttribute("User");
System.out.println(usuarioAutenticado + "eeeee");
    
if(usuarioAutenticado != null && usuarioAutenticado.equals("User")) {
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Datos del usuario</title>
</head>
<body>
<%

Usuario usuario = (Usuario) misession.getAttribute("Usuario2");
PrintWriter pw = response.getWriter();
pw.println("<h3>Datos del usuario:</h3>");
pw.println("<p>Nombre del usuario: " + usuario.getUserName() + "</p>");
pw.println("<p>Contraseña del usuario: " + usuario.getPassword() + "</p><br>");

// Botón para volver
pw.println("<form action=\"CarroDeCompralUsuario.jsp\" method=\"get\">");
pw.println("<button type=\"submit\">Volver</button>");
pw.println("</form>");
pw.close();
%>
</body>
</html>
<%
} else {
    response.sendRedirect("Login.jsp");
}
%>
