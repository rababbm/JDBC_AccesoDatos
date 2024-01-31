import java.beans.Statement;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Scanner;
/**
 * Clase principal que contiene el método main para ejecutar la aplicación del cine.
 */
public class CineMain {
    /**
     * Método principal que ejecuta la aplicación del cine.
     * @param args Argumentos de la línea de comandos (no utilizados en esta aplicación).
     * @throws IOException Si ocurre un error de entrada/salida.

     * @throws ParseException Si ocurre un error al analizar la entrada del usuario.
     */
    public static void main(String[] args) throws IOException, SQLException, ParseException {
        CineMenu menu = new CineMenu();
        Scanner scanner = new Scanner(System.in);
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection c = connectionFactory.connect();

        DirectorController directorController = new DirectorController(c);
        RepartoController repartoController = new RepartoController(c);
        PeliculaController peliculaController = new PeliculaController(c,scanner);

        // Mostrar el menú principal y obtener la opción seleccionada por el usuario.
        int option = menu.mainMenu();
        while (option >= 0 && option < 12) {
            switch (option) {
                case 0:
                    peliculaController.buscarPeliculaPorId();
                    break;
                case 1:
                    peliculaController.buscarPeliculasPorAnio();
                    break;

                case 2:
                    peliculaController.buscarPeliculasPorPalabraEnTitulo();
                    break;

                case 3:
                    peliculaController.crearTablaPelicula();
                    break;

                case 4:
                    repartoController.crearTablaReparto();
                    break;

                case 5:
                    directorController.crearTablaDirector();
                    break;

                case 6:
                    peliculaController.dropTablas();
                    break;

                case 7:
                    peliculaController.poblarMasivamente();
                    break;

                case 8:
                    repartoController.poblarMasivamenteReparto();
                    break;

                case 9:
                    directorController.poblarMasivamenteDirectores();
                    break;
                case 10:
                    peliculaController.modificarAñoPelicula();
                    break;
                case 11:
                    peliculaController.eliminarPeliculaPorId();
                    break;
                case 12:
                    System.exit(0);
                    break;

                default:
                    System.out.println("Introdueixi una de les opcions anteriors");
                    break;

            }
            //volver a mostrar el menú
            option = menu.mainMenu();
        }


    }

}
