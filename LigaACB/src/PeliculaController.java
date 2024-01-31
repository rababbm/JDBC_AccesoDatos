import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

/**
 * Controlador para manejar las operaciones relacionadas con las películas en la base de datos.
 */
public class PeliculaController {
    private Connection connection;
    private Scanner scanner;
    /**
     * Constructor de la clase PeliculaController.
     * @param connection La conexión a la base de datos.
     * @param scanner El objeto Scanner para leer la entrada del usuario.
     */
    public PeliculaController(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }
    /**
     * Elimina todas las tablas de la base de datos.
     * @throws SQLException Si ocurre un error al ejecutar la consulta SQL.
     */
    public void dropTablas() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS reparto");
            statement.executeUpdate("DROP TABLE IF EXISTS director");
            statement.executeUpdate("DROP TABLE IF EXISTS pelicula");

            System.out.println("Todas las tablas han sido eliminadas con éxito.");
        } catch (SQLException e) {
            System.out.println("Error al eliminar las tablas: " + e.getMessage());
            throw e;
        }
    }
    /**
     * Puebla la base de datos con datos de un archivo CSV de películas.
     */
    public void poblarMasivamente() {
        try (CSVReader reader = new CSVReader(new FileReader("LigaACB/resources/peliculas.csv"))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length >= 4) {
                    String titulo = nextLine[0];
                    int año = Integer.parseInt(nextLine[1]);
                    String duracion = nextLine[2];
                    String enlace = nextLine[3];

                    insertarPelicula(titulo, año, duracion, enlace);
                } else {
                    System.out.println("La fila no tiene la cantidad de columnas esperadas.");
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    private void insertarPelicula(String titulo, int año, String duracion, String enlace) {
        try {
            String sql = "INSERT INTO pelicula(titulo, año, duracion, enlace) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pst.setString(1, titulo);
                pst.setInt(2, año);
                pst.setString(3, duracion);
                pst.setString(4, enlace);

                pst.executeUpdate();

                ResultSet generatedKeys = pst.getGeneratedKeys();
                if (generatedKeys.next()) {
                    System.out.println("Película añadida con éxito. ID: " + generatedKeys.getInt(1));
                } else {
                    System.out.println("No se pudo obtener el ID de la película.");
                }
            }
        } catch (NumberFormatException | SQLException e) {
            System.out.println("Error: El valor de 'año' no es un número entero válido.");
            e.printStackTrace();
        }
    }

    public void crearTablaPelicula() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS Pelicula (" +
                    "id SERIAL PRIMARY KEY," +
                    "titulo VARCHAR(255)," +
                    "año INTEGER," +
                    "duracion VARCHAR(10)," +
                    "enlace VARCHAR(7000)" +
                    ")";

            try (PreparedStatement pst = connection.prepareStatement(sql)) {
                pst.executeUpdate();
                System.out.println("Tabla Pelicula creada exitosamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al intentar crear la tabla Pelicula: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * Busca películas por palabra clave en el título.

     */
    public void buscarPeliculasPorPalabraEnTitulo() {
        while (true) {
            System.out.println("Introduce la palabra a buscar en el título de las películas (o 'exit' para salir):");
            String palabra = scanner.nextLine();

            if ("exit".equalsIgnoreCase(palabra)) {
                break;
            }

            try {
                buscarPeliculasPorPalabraEnTitulo(palabra);
            } catch (SQLException e) {
                System.out.println("Error al buscar películas por palabra en el título: " + e.getMessage());
            }
        }
    }

    private void buscarPeliculasPorPalabraEnTitulo(String palabra) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM pelicula WHERE LOWER(titulo) LIKE '%" + palabra.toLowerCase() + "%'";
            ResultSet resultSet = statement.executeQuery(query);

            boolean encontrado = false;
            while (resultSet.next()) {
                encontrado = true;
                int id = resultSet.getInt("id");
                String titulo = resultSet.getString("titulo");
                int año = resultSet.getInt("año");
                String duracion = resultSet.getString("duracion");
                String enlace = resultSet.getString("enlace");

                System.out.println("ID: " + id + ", Título: " + titulo + ", Año: " + año + ", Duración: " + duracion + ", Enlace: " + enlace);
            }

            if (!encontrado) {
                System.out.println("No se encontraron películas con la palabra '" + palabra + "' en el título.");
            }
        }
    }
    /**
     * Busca películas por año
     */
    public void buscarPeliculasPorAnio() {
        while (true) {
            System.out.println("Introduce el año a partir del cual deseas buscar películas (o 'exit' para salir):");
            String input = scanner.nextLine();

            if ("exit".equalsIgnoreCase(input)) {
                break;
            }
            try {
                int año = Integer.parseInt(input);
                buscarPeliculasPorAnio(año);

            } catch (NumberFormatException e) {
                System.out.println("Error: Introduce un año válido.");
            } catch (SQLException e) {
                System.out.println("Error al buscar películas por año: " + e.getMessage());
            }
        }
    }


    private void buscarPeliculasPorAnio(int año) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM pelicula WHERE año >= " + año;
            ResultSet resultSet = statement.executeQuery(query);

            boolean encontrado = false;
            while (resultSet.next()) {
                encontrado = true;
                int id = resultSet.getInt("id");
                String titulo = resultSet.getString("titulo");
                int añoPelicula = resultSet.getInt("año");
                String duracion = resultSet.getString("duracion");
                String enlace = resultSet.getString("enlace");

                System.out.println("ID: " + id + ", Título: " + titulo + ", Año: " + añoPelicula + ", Duración: " + duracion + ", Enlace: " + enlace);
            }

            if (!encontrado) {
                System.out.println("No se encontraron películas a partir del año " + año);
            } else {
                System.out.println("Búsqueda completada.");
            }
        }
    }
    /**
     * Busca películas por id

     */
    public void buscarPeliculaPorId() {
        while (true) {
            System.out.println("Introduce el ID de la película que deseas buscar (o 'exit' para salir):");
            String input = scanner.nextLine();

            if ("exit".equalsIgnoreCase(input)) {
                break;
            }

            try {
                int id = Integer.parseInt(input);

                buscarPeliculaPorId(id);

            } catch (NumberFormatException e) {
                System.out.println("Error: Introduce un ID válido.");
            } catch (SQLException e) {
                System.out.println("Error al buscar película por ID: " + e.getMessage());
            }
        }
    }

    private void buscarPeliculaPorId(int id) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM pelicula WHERE id = " + id;
            ResultSet resultSet = statement.executeQuery(query);

            boolean encontrado = false;
            while (resultSet.next()) {
                encontrado = true;
                String titulo = resultSet.getString("titulo");
                int año = resultSet.getInt("año");
                String duracion = resultSet.getString("duracion");
                String enlace = resultSet.getString("enlace");

                System.out.println("ID: " + id + ", Título: " + titulo + ", Año: " + año + ", Duración: " + duracion + ", Enlace: " + enlace);
            }

            if (!encontrado) {
                System.out.println("No se encontró ninguna película con el ID " + id);
            } else {
                System.out.println("Búsqueda completada.");
            }
        }
    }
    /**
     * Modificar pelicula
     */
    public void modificarAñoPelicula() throws SQLException {
        System.out.println("Introduce el ID de la película que deseas modificar:");

        // Obtener el ID de la película del usuario
        int idPelicula = obtenerIdPeliculaDesdeUsuario();

        // Verificar si el ID es válido antes de intentar la modificación
        if (idPelicula <= 0) {
            System.out.println("ID de película no válido. Inténtalo de nuevo.");
            return;
        }

        // Realizar la modificación del año
        modificarAñoPelicula(idPelicula);
    }

    private int obtenerIdPeliculaDesdeUsuario() {
        System.out.print("ID de película: ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1; // Valor no válido
        }
    }
    private void actualizarAñoPelicula(int id, int nuevoAño) throws SQLException {
        String query = "UPDATE pelicula SET año = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, nuevoAño);
            pst.setInt(2, id);
            pst.executeUpdate();
        }
    }

    private int obtenerAñoPelicula(int id) throws SQLException {
        String query = "SELECT año FROM pelicula WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("año");
            } else {
                // Devolver un valor por defecto o lanzar una excepción, según tus requisitos
                return -1; // Por ejemplo, devolver -1 si no se encuentra la película con el ID dado
            }
        }
    }

    private void modificarAñoPelicula(int id) throws SQLException {
        System.out.println("Introduce el nuevo año. Presiona Enter para mantener el valor actual.");

        try {
            // Obtener el año actual de la película
            int añoActual = obtenerAñoPelicula(id);

            // Ejemplo de cómo puedes pedir el nuevo año al usuario
            System.out.print("Nuevo año (" + añoActual + "): ");
            String nuevoAñoStr = scanner.nextLine();
            int nuevoAño = nuevoAñoStr.isEmpty() ? añoActual : Integer.parseInt(nuevoAñoStr);

            // Llamar al método para actualizar el año de la película en la base de datos
            actualizarAñoPelicula(id, nuevoAño);

            System.out.println("Año de la película actualizado con éxito.");
        } catch (NumberFormatException e) {
            System.out.println("Error: Introduce un año válido.");
        }
    }
    /**
     * Eliminar pelicula por id
     */
    public void eliminarPeliculaPorId() {
        while (true) {
            System.out.println("Introduce el ID de la película que deseas eliminar (o 'exit' para salir):");
            String input = scanner.nextLine();

            if ("exit".equalsIgnoreCase(input)) {
                break;
            }

            try {
                int id = Integer.parseInt(input);

                // Verificar si el ID es válido antes de intentar la eliminación
                if (id <= 0) {
                    System.out.println("ID de película no válido. Inténtalo de nuevo.");
                    continue;
                }

                // Realizar la eliminación de la película
                eliminarPeliculaPorId(id);

            } catch (NumberFormatException e) {
                System.out.println("Error: Introduce un ID válido.");
            } catch (SQLException e) {
                System.out.println("Error al eliminar película por ID: " + e.getMessage());
            }
        }
    }

    private void eliminarPeliculaPorId(int id) throws SQLException {
        // Eliminar registros de la tabla reparto que hacen referencia a la película
        String deleteRepartoQuery = "DELETE FROM reparto WHERE pelicula_id = ?";
        try (PreparedStatement deleteRepartoPst = connection.prepareStatement(deleteRepartoQuery)) {
            deleteRepartoPst.setInt(1, id);
            deleteRepartoPst.executeUpdate();
        }

        // Eliminar la película
        String deletePeliculaQuery = "DELETE FROM pelicula WHERE id = ?";
        try (PreparedStatement deletePeliculaPst = connection.prepareStatement(deletePeliculaQuery)) {
            deletePeliculaPst.setInt(1, id);
            int filasEliminadas = deletePeliculaPst.executeUpdate();

            if (filasEliminadas > 0) {
                System.out.println("Película eliminada con éxito.");
            } else {
                System.out.println("No se encontró ninguna película con el ID " + id);
            }
        }
    }



}
