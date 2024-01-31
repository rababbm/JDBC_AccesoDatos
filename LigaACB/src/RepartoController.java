import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.text.ParseException;
import java.util.Arrays;

/**
 * Controlador para manejar la información de reparto en la base de datos.
 */
public class RepartoController {
    private Connection connection;
    /**
     * Constructor de la clase RepartoController.
     * @param connection La conexión a la base de datos.
     */
    public RepartoController(Connection connection) {
        this.connection = connection;
    }

    /**
     * Puebla masivamente la tabla de reparto con datos de un archivo CSV.
     */
    public void poblarMasivamenteReparto() {
        try (CSVReader reader = new CSVReader(new FileReader("LigaACB/resources/reparto.csv"))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length >= 4) {
                    String peliculaId = nextLine[0];
                    String actor1 = nextLine[1];
                    String actor2 = nextLine[2];
                    String actor3 = nextLine[3];

                    System.out.println("Línea del archivo CSV: " + Arrays.toString(nextLine));

                    // Insertar en la base de datos
                    insertarReparto(peliculaId, actor1, actor2, actor3);
                } else {
                    System.out.println("La fila no tiene la cantidad de columnas esperadas para reparto. Datos: " + Arrays.toString(nextLine));
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }
    /**
     * Inserta una fila en la tabla de reparto.
     * @param peliculaId ID de la película.
     * @param actor1 Nombre del primer actor principal.
     * @param actor2 Nombre del segundo actor principal.
     * @param actor3 Nombre del tercer actor principal.
     */
    private void insertarReparto(String peliculaId, String actor1, String actor2, String actor3) {
        try {
            String sql = "INSERT INTO reparto(pelicula_id, actor_principal_1, actor_principal_2, actor_principal_3) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pst = connection.prepareStatement(sql)) {
                pst.setInt(1, Integer.parseInt(peliculaId));
                pst.setString(2, actor1);
                pst.setString(3, actor2);
                pst.setString(4, actor3);

                pst.executeUpdate();

                System.out.println("Reparto añadido con éxito. Película ID: " + peliculaId);
            }
        } catch (NumberFormatException | SQLException e) {
            // Imprimir la excepción específica
            System.out.println("Error al insertar en reparto: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * Crea la tabla de reparto en la base de datos si no existe.
     */
    public void crearTablaReparto() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS Reparto (" +
                    "pelicula_id INTEGER REFERENCES Pelicula(id)," +
                    "actor_principal_1 VARCHAR(255)," +
                    "actor_principal_2 VARCHAR(255)," +
                    "actor_principal_3 VARCHAR(255)," +
                    "PRIMARY KEY (pelicula_id)" +
                    ")";

            try (PreparedStatement pst = connection.prepareStatement(sql)) {
                pst.executeUpdate();
                System.out.println("Tabla Reparto creada exitosamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al intentar crear la tabla Reparto: " + e.getMessage());
            e.printStackTrace();
        }
    }


}






    /*
    //AÑADIR A MANO EL REPARTO- sino hace falta eliminarlo
    public void addReparto() throws SQLException, NumberFormatException, IOException, ParseException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Inserta ID de la película:  ");
        String peliculaId = br.readLine();
        System.out.println("Inserta actor principal 1: ");
        String actor1 = br.readLine();
        System.out.println("Inserta actor principal 2: ");
        String actor2 = br.readLine();
        System.out.println("Inserta actor principal 3: ");
        String actor3 = br.readLine();


        String sql = "INSERT INTO reparto(pelicula_id, actor_principal_1, actor_principal_2, actor_principal_3) VALUES (?,?,?,?)";

        PreparedStatement pst = connection.prepareStatement(sql);

        pst.setString(1, peliculaId);
        pst.setString(2, actor1);
        pst.setString(3, actor2);
        pst.setString(4, actor3);

        pst.executeUpdate();
        int rowCount = pst.executeUpdate();
        //añadido para validar
        if (rowCount > 0) {
            System.out.println("Reparto añadido con éxito.");
        } else {
            System.out.println("No se pudo añadir el reparto.");
        }
    }
    public void showRepartos() throws SQLException, IOException {

        Statement st = connection.createStatement();
        ResultSet rs;

        rs = st.executeQuery("SELECT * FROM reparto");
        while (rs.next()) {
            System.out.println("Pelicula ID: " + rs.getInt("pelicula_id") + " " +
                    "Actor Principal 1: " + rs.getString("actor_principal_1") + " " +
                    "Actor Principal 2: " + rs.getString("actor_principal_2") + " " +
                    "Actor Principal 3: " + rs.getString("actor_principal_3"));
        }

        rs.close();
        st.close();
    }


}
*/