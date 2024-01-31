/*
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

public class ACBMain {

	public static void main(String[] args) throws IOException, SQLException, ParseException {
		CineMenu menu = new CineMenu();
		
		ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
		Connection c = connectionFactory.connect();

		TeamController teamController = new TeamController(c);
		PlayerController playerController = new PlayerController(c);

		int option = menu.mainMenu();
		while (option > 0 && option < 12) {
			switch (option) {
			case 1:
				teamController.showTeams();
				break;

			case 2:
				teamController.showTeamPlayers();
				break;

			case 3:
				teamController.addTeam();
				break;

			case 4:
				playerController.addPlayer();
				break;

			case 5:
				break;

			case 6:
				break;

			case 7:
				playerController.signing();
				break;

			case 8:
				playerController.rescission();
				break;

			case 9:
				break;

			case 10:
				System.exit(0);
				break;

			default:
				System.out.println("Introdueixi una de les opcions anteriors");
				break;

			}
			option = menu.mainMenu();
		}

	}

}
*/