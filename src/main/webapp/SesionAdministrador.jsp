<%@ page language="java" contentType="text/html; charset= UTF-8" pageEncoding="ISO-8859-1"%>
<%@ page session="true"%>
<%@ page import="modelo.Usuario"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="controlador.Login"%>
  <%
    // Verificar si el usuario está autenticado
   HttpSession misession = request.getSession();
Usuario usuarioAutenticado = (Usuario) misession.getAttribute("Usuario");
String usuarioAutenticado2 = (String)misession.getAttribute("Adm");
    
    if(usuarioAutenticado!= null)
    {
    if((usuarioAutenticado2.equals("Adm"))) {
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
</head>
<body>
<%
Usuario usuario = (Usuario) misession.getAttribute("Usuario");
PrintWriter pw = response.getWriter();
pw.println("<h3>Datos del usuario:</h3>");
pw.println("<html><body>"+"Nombre del usuario: " + usuario.getUserName()+"</body></html><br>");
pw.println("<html><body>"+"Rol del usuario: " + usuario.getRole() +"</body></html><br>");
pw.println("<html><body>"+"Contraseña del usuario: " + usuario.getPassword() +"</body></html><br><br>");
pw.println("<form action=\"PaginaPrincipalDeAdm.jsp\" method=\"get\">");
pw.println("<button type=\"submit\">Volver</button>");
pw.println("</form>");
pw.close();
%>
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
