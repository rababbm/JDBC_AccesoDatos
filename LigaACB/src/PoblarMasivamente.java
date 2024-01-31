import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 * Clase para poblar masivamente la base de datos con datos de archivos CSV.
 */
public class PoblarMasivamente {

    private static final String CSV_PELICULAS = "LigaACB/resources/peliculas.csv";

    private static final String CSV_REPARTO = "LigaACB/resources/reparto.csv";
    private static final String CSV_DIRECTORES = "LigaACB/resources/directores.csv";

    /**
     * Puebla masivamente la base de datos con datos de películas, reparto y directores.
     * @param connection La conexión a la base de datos.
     */
    public static void poblarMasivamente(Connection connection) {
        try {
            poblarPeliculas(connection);
            poblarReparto(connection);
            poblarDirectores(connection);
        } catch (IOException | CsvException | SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Puebla masivamente la tabla de películas con datos de un archivo CSV.
     * @param connection La conexión a la base de datos.
     * @throws IOException Si ocurre un error de entrada/salida al leer el archivo CSV.
     * @throws CsvException Si ocurre un error al analizar el archivo CSV.
     * @throws SQLException Si ocurre un error al ejecutar la consulta SQL.
     */
    private static void poblarPeliculas(Connection connection) throws IOException, CsvException, SQLException {
        try (CSVReader reader = new CSVReader(new FileReader(CSV_PELICULAS))) {
            String[] nextRecord;
            String insertQuery = "INSERT INTO pelicula(titulo, año, duracion, enlace) VALUES (?,?,?,?)";
            try (PreparedStatement pst = connection.prepareStatement(insertQuery)) {
                while ((nextRecord = reader.readNext()) != null) {
                    pst.setString(1, nextRecord[0]); // título
                    pst.setInt(2, Integer.parseInt(nextRecord[1])); // año
                    pst.setString(3, nextRecord[2]); // duración
                    pst.setString(4, nextRecord[3]); // enlace
                    pst.executeUpdate();
                }
            }
        }
    }
    /**
     * Puebla masivamente la tabla de reparto con datos de un archivo CSV.
     * @param connection La conexión a la base de datos.
     * @throws IOException Si ocurre un error de entrada/salida al leer el archivo CSV.
     * @throws CsvException Si ocurre un error al analizar el archivo CSV.
     * @throws SQLException Si ocurre un error al ejecutar la consulta SQL.
     */
    private static void poblarReparto(Connection connection) throws IOException, CsvException, SQLException {
        try (CSVReader reader = new CSVReader(new FileReader(CSV_REPARTO))) {
            String[] nextRecord;
            String insertQuery = "INSERT INTO reparto(pelicula_id, actor_principal_1, actor_principal_2, actor_principal_3) VALUES (?,?,?,?)";
            try (PreparedStatement pst = connection.prepareStatement(insertQuery)) {
                while ((nextRecord = reader.readNext()) != null) {
                    pst.setInt(1, Integer.parseInt(nextRecord[0])); // película_id
                    pst.setString(2, nextRecord[1]); // actor_principal_1
                    pst.setString(3, nextRecord[2]); // actor_principal_2
                    pst.setString(4, nextRecord[3]); // actor_principal_3
                    pst.executeUpdate();
                }
            }
        }
    }
    /**
     * Puebla masivamente la tabla de directores con datos de un archivo CSV.
     * @param connection La conexión a la base de datos.
     * @throws IOException Si ocurre un error de entrada/salida al leer el archivo CSV.
     * @throws CsvException Si ocurre un error al analizar el archivo CSV.
     * @throws SQLException Si ocurre un error al ejecutar la consulta SQL.
     */
    private static void poblarDirectores(Connection connection) throws IOException, CsvException, SQLException {
        try (CSVReader reader = new CSVReader(new FileReader(CSV_DIRECTORES))) {
            String[] nextRecord;
            String insertQuery = "INSERT INTO director(nombre) VALUES (?)";
            try (PreparedStatement pst = connection.prepareStatement(insertQuery)) {
                while ((nextRecord = reader.readNext()) != null) {
                    pst.setString(1, nextRecord[0]); // nombre del director
                    pst.executeUpdate();
                }
            }
        }
    }
}
