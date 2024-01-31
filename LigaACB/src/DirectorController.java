import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
/**
 * Controlador para manejar las operaciones relacionadas con los directores en la base de datos.
 */

public class DirectorController {
    private Connection connection;

    /**
     * Constructor de la clase DirectorController.
     * @param connection La conexión a la base de datos.
     */
    public DirectorController(Connection connection) {
        this.connection = connection;
    }

    /**
     * Puebla la base de datos con datos de un archivo CSV de directores.
     */
    public void poblarMasivamenteDirectores() {
        try (CSVReader reader = new CSVReader(new FileReader("LigaACB/resources/directores.csv"))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length >= 1) { // Verificar que hay al menos 1 columna en la fila
                    String nombre = nextLine[0].replace("\"", ""); // Eliminar las comillas dobles

                    // Insertar en la base de datos
                    boolean exito = insertarDirector(nombre);
                    if (exito) {
                        System.out.println("Director añadido con éxito: " + nombre);
                    } else {
                        System.out.println("No se pudo añadir el director: " + nombre);
                    }
                } else {
                    System.out.println("La fila no tiene la cantidad de columnas esperadas para directores.");
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserta un nuevo director en la base de datos.
     * @param nombre El nombre del director.
     * @return true si la inserción fue exitosa, false de lo contrario.
     */
    private boolean insertarDirector(String nombre) {
        try {
            String sql = "INSERT INTO director(nombre) VALUES (?)";
            try (PreparedStatement pst = connection.prepareStatement(sql)) {
                pst.setString(1, nombre);
                pst.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Crea la tabla "Director" en la base de datos si no existe.
     */
    public void crearTablaDirector() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS Director (" +
                    "id SERIAL PRIMARY KEY," +
                    "nombre VARCHAR(255)" +
                    ")";

            try (PreparedStatement pst = connection.prepareStatement(sql)) {
                pst.executeUpdate();
                System.out.println("Tabla Director creada exitosamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al intentar crear la tabla Director: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
