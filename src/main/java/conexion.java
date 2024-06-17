import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.*;
public class conexion {
    public static void main(String[] args) throws SQLException{
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/DBProyect", "usuario", "contraseña");
        /*
        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM servicio")) {
            while (rs.next()) {
                String nombre = rs.getString("nombre_servicio");
                String costo = rs.getString("costo");
                System.out.println("Servicio: " + nombre + " \n" + "Precio:" + costo);
            }
        }*/
    }
}
