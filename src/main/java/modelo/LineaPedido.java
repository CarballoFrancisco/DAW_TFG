package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class LineaPedido {
    
    private int idLineaPedido;    
    private int idPedidoFK2;
    private int idProductoFK3;
    private String nombre_producto;
    private int lineaPedidoCantidadProducto;
    private double lineaPedidoPrecio;
    private String nombre;
    private DataSource pool;

    public LineaPedido() {
        super();
    }

    public LineaPedido(int idLineaPedido, int idPedidoFK2, int idProductoFK3, String nombre_producto,
            int lineaPedidoCantidadProducto, double lineaPedidoPrecio, String nombre, DataSource pool) {
        super();
        this.idLineaPedido = idLineaPedido;
        this.idPedidoFK2 = idPedidoFK2;
        this.idProductoFK3 = idProductoFK3;
        this.nombre_producto = nombre_producto;
        this.lineaPedidoCantidadProducto = lineaPedidoCantidadProducto;
        this.lineaPedidoPrecio = lineaPedidoPrecio;
        this.nombre = nombre;
        this.pool = pool;
    }

    public int getIdLineaPedido() {
        return idLineaPedido;
    }

    public void setIdLineaPedido(int idLineaPedido) {
        this.idLineaPedido = idLineaPedido;
    }

    public int getIdPedidoFK2() {
        return idPedidoFK2;
    }

    public void setIdPedidoFK2(int idPedidoFK2) {
        this.idPedidoFK2 = idPedidoFK2;
    }

    public int getIdProductoFK3() {
        return idProductoFK3;
    }

    public void setIdProductoFK3(int idProductoFK3) {
        this.idProductoFK3 = idProductoFK3;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public int getLineaPedidoCantidadProducto() {
        return lineaPedidoCantidadProducto;
    }

    public void setLineaPedidoCantidadProducto(int lineaPedidoCantidadProducto) {
        this.lineaPedidoCantidadProducto = lineaPedidoCantidadProducto;
    }

    public double getLineaPedidoPrecio() {
        return lineaPedidoPrecio;
    }

    public void setLineaPedidoPrecio(double lineaPedidoPrecio) {
        this.lineaPedidoPrecio = lineaPedidoPrecio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNombreUsuario() {
        return nombre; 
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public DataSource getPool() {
        return pool;
    }

    public void setPool(DataSource pool) {
        this.pool = pool;
    }

    public ArrayList<LineaPedido> generarTablaLineaPedido() {
        ArrayList<LineaPedido> arrayDinamico = new ArrayList<>();
        Connection conexion = null;

        try {
            InitialContext ctx = new InitialContext();
            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/customers");

            // Obtener la conexión del pool
            conexion = pool.getConnection();

            //String consultaSQL = "SELECT lineapedido.idLineaPedido, lineapedido.idPedidoFK2,lineapedido.idProductoFK3, productos.nombreProducto as nombre_producto, lineapedido.lineaPedidoCantidadProducto, lineapedido.lineaPedidoPrecio FROM lineapedido JOIN productos ON lineapedido.idProductoFK3 = productos.idProducto;";
            String consultaSQL = "SELECT lineapedido.idLineaPedido, lineapedido.idPedidoFK2, lineapedido.idProductoFK3, productos.nombreProducto AS nombre_producto, lineapedido.lineaPedidoCantidadProducto, lineapedido.lineaPedidoPrecio, (SELECT username FROM users WHERE idUsers = (SELECT idUsuarioFK1 FROM pedidos WHERE idPedido = lineapedido.idPedidoFK2)) AS username FROM lineapedido JOIN productos ON lineapedido.idProductoFK3 = productos.idProducto;";

            try (Statement stmt = conexion.createStatement();
                    ResultSet resultadoConsulta = stmt.executeQuery(consultaSQL)) {

                while (resultadoConsulta.next()) {
                    int idLineaPedido= resultadoConsulta.getInt("idLineaPedido");
                    int idPedidoFK2 = resultadoConsulta.getInt("idPedidoFK2");
                    int idProductoFK3 = resultadoConsulta.getInt("idProductoFK3");
                    String nombre_producto = resultadoConsulta.getString("nombre_producto");
                    int lineaPedidoCantidadProducto= resultadoConsulta.getInt("lineaPedidoCantidadProducto");
                    double lineaPedidoPrecio = resultadoConsulta.getDouble("lineaPedidoPrecio");
                    String nombre = resultadoConsulta.getString("username");
                    LineaPedido lineaPedido = new LineaPedido(idLineaPedido,idPedidoFK2,idProductoFK3,nombre_producto,lineaPedidoCantidadProducto,lineaPedidoPrecio,nombre,pool);
                    
                    arrayDinamico.add(lineaPedido);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (NamingException | SQLException e) {
            e.printStackTrace();
        } finally {
            // Cerrar la conexión en el bloque finally
            try {
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return arrayDinamico;
    }

    public boolean verificarExistenciaProducto(int idProducto) throws SQLException, NamingException {
        Connection con = null;

        try {
            InitialContext ctx = new InitialContext();
            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/customers");

            // Obtener la conexión del pool
            con = pool.getConnection();

            String consultaSQL = "SELECT 1 FROM productos WHERE idProducto = ?";

            try (PreparedStatement pstmt = con.prepareStatement(consultaSQL)) {
                pstmt.setInt(1, idProducto); // Usar el parámetro nombreProducto

                try (ResultSet resultadoConsulta = pstmt.executeQuery()) {
                    return resultadoConsulta.next();
                        
                }
            }
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    public boolean verificarExistenciaPedido(int idPedido) throws SQLException, NamingException {
        Connection con = null;
        
        try {
            InitialContext ctx = new InitialContext();
            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/customers");

            // Obtener la conexión del pool
            con = pool.getConnection();

            String consultaSQL = "SELECT 1 FROM pedidos WHERE idpedido = ?";

            try (PreparedStatement pstmt = con.prepareStatement(consultaSQL)) {
                pstmt.setInt(1, idPedido); // Usar el parámetro nombreProducto

                try (ResultSet resultadoConsulta = pstmt.executeQuery()) {
                    return resultadoConsulta.next();
                        
                }
            }
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    public boolean esNumero(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    } 

    public static boolean validacion(String cadena) {

        char[] caracteresEspeciales = { 'a','b','c', 'd','e','f','g','h','i','j','k','l','m','n','ñ','o','p','q','r','s','t','u','w','x','y','z'};

        boolean condicion = false;

        for (int i = 0; i < (cadena.length()); i++) {

            for (int a = 0; a < caracteresEspeciales.length; a++) {

                if (cadena.charAt(i) == caracteresEspeciales[a]) {

                    condicion = true;

                    break;

                } else {

                    condicion = false;

                }
            }

        }

        return condicion;

    }
}

