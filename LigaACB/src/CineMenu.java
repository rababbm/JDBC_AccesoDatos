import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * Clase que representa el menú de la aplicación del cine.
 */

public class CineMenu {
	private int option;
	/**
	 * Constructor de la clase CineMenu.
	 */
	public CineMenu() {
		super();
	}
	/**
	 * Método que muestra el menú principal y devuelve la opción seleccionada por el usuario.
	 * @return La opción seleccionada por el usuario.
	 */
	public int mainMenu() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		do {
			System.out.println(" \nMENU PRINCIPAL \n");//SI select concreto
			System.out.println("0. Buscar peliculas por id"); //SI select x año
			System.out.println("1. Buscar peliculas a partir de un año"); //SI select x año
			System.out.println("2. Buscar palabra en titulo de peliculas"); //SI select x nombre
			System.out.println("3. Crear tabla Pelicula"); //SI
			System.out.println("4. Crear tabla Reparto");//SI
			System.out.println("5. Crear tabla Director");//SI
			System.out.println("6. Borrar todas las tablas de la base de datos y su información"); //SI
			System.out.println("7. Poblar masivamente las tablas peliculas ");//SI
			System.out.println("8. Poblar masivamente las tablas reparto");//SI
			System.out.println("9. Poblar masivamente las tablas directores");//SI
			System.out.println("10. Modificar el año de una pelicula");//SI modificar regitro
			System.out.println("11. Eliminar el registro de una pelicula por el ID:");//SI eliminar registro
			System.out.println("12. Sortir");
			System.out.println("Esculli opció: ");
			try {
				option = Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				System.out.println("valor no vàlid");
				e.printStackTrace();

			}

		} while (option != 0 && option != 1 && option != 2 && option != 3 && option != 4 && option != 5 && option != 6 && option != 7
				&& option != 8 && option != 9 && option != 10 && option != 11 && option != 12);

		return option;
	}
}
