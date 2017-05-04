package lolsoap;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import brugerautorisation.data.Bruger;
import brugerautorisation.transport.rmi.Brugeradmin;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Formatter;
import java.util.Scanner;
import javax.jws.WebService;

/*
 * This class holds all the instances of different games and lets players create or join a game.
 *  
 * @author Mads Hornbeck
 */
@WebService(endpointInterface = "lolsoap.LolSOAPI")
public class GameHandler implements LolSOAPI {

	HashMap<UUID, GameInstance> games = new HashMap<UUID, GameInstance>();
	HashMap<String, Player> players = new HashMap<String, Player>();
	
	@Override
	public synchronized UUID createNewGame(String username) {
		// TODO add error handling
		Player player = players.get(username);
		if (player != null) {
			GameInstance game = new GameInstance();
			UUID id = game.getGameId();
			game.addPlayer(player);
			games.put(id, game);
			return id;
		}
		return null;
	}
	
	@Override
	public UUID[] findGames() {
		Set<UUID> keys = games.keySet();
//                SaveHighScore();
		return keys.toArray(new UUID[0]);
	}
	
	@Override
	public String[] getUsernames(UUID gameId) {
		GameInstance game = games.get(gameId);
		String[] players = game.getUsernames();
		return players;
	}

	@Override
	public void joinGame(UUID gameId, String username) {
		Player player = players.get(username);
		GameInstance game = games.get(gameId);
		game.addPlayer(player);
	}
	
	@Override
	public UUID[] getPlayersGames(String username) {
		Player player = players.get(username);
		Set<UUID> gamesSet = player.getGames();
		return gamesSet.toArray(new UUID[0]);
	}

	@Override
	public void startGame(String username) {
		Player player = players.get(username);
		GameInstance game = getGame(player);
		game.startGame();
	}
	
	@Override 
	public void setNumberOfChamps(UUID gameId, int number) {
		GameInstance game = games.get(gameId);
		game.setNumberOfChampions(number);
	}
	
	@Override
	public boolean isGameStarted(String username) {
		Player player = players.get(username);
		GameInstance game = getGame(player);
		return game.isGameStarted();
	}
	
	@Override
	public boolean playerDoneGuessing(UUID gameId, String username) {
		Player player = players.get(username);
		GameInstance game = games.get(gameId);
		return game.playerIsDone(player);
	}
	
	@Override
	public boolean isGameDone(UUID gameId) {
		GameInstance game = games.get(gameId);
		return game.gameIsDone();
	}

	@Override
	public boolean didIWin(UUID gameId, String username) {
		Player player = players.get(username);
		GameInstance game = games.get(gameId);
		return game.hasWon(player);
	}
	
	@Override
	public String getWinner(UUID gameId) {
		GameInstance game = games.get(gameId);
		return game.getWinner();
	}

	@Override
	public String getChampionImgUrl(String username) {
		Player player = players.get(username);
		GameInstance game = getGame(player);
		if (player != null && game != null) {
			Champion champion = game.getCurrentChampion(player);
			return  champion.getUrl();
		}
		return "Error";
	}
	
	@Override
	public String getChampionTitle(String username) {
		Player player = players.get(username);
		GameInstance game = getGame(player);
		if (player != null && game != null) {
			Champion champion = game.getCurrentChampion(player);
			return  champion.getTitle();
		}
		// TODO real error message.
		return "Error";
	}
	

	@Override
	public boolean guessChampion(String username, String guess) {
		Player player = players.get(username);
		GameInstance game = getGame(player);
		if (player != null && game != null) {
			return game.guessChamp(player, guess);
		}
		return false;
	}

	@Override
	public void skipChampion(String username) {
		Player player = players.get(username);
		GameInstance game = getGame(player);
		if (player != null && game != null) {
			game.skip(player);			
		}
	}

	@Override
	public String hentBruger(String user, String pass) {
        Brugeradmin ba;
		try {
			ba = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
			Bruger b = ba.hentBruger(user, pass);
	        Player player = players.get(b.brugernavn);
	        if (player == null) {
	        	player = new Player(b);
	        	players.put(player.getBrugernavn(), player);
	        }
	        return player.getBrugernavn();
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Connection Error to javabog.dk";
	}
	
	private GameInstance getGame(Player player){
		GameInstance game = null;
		if (player != null) {
			UUID gameId = player.getCurrentGame();
			if (gameId != null) {
				game = games.get(gameId);
			}
		}
		return game;
	}
            
        public void SaveHighScore() {

        String Scores[] = {"one" , "Two" , "Trhee"};
        String writeableString = "";
            
            Formatter x;
            
            try {
                
                x = new Formatter("Scores.txt");
                FileWriter fw = new FileWriter("C:\\Users\\kristofer\\Documents\\LolProjekt\\HighScore/Score.txt");
                BufferedWriter bw = new BufferedWriter(fw);
                
                for(String item : Scores) {
                    writeableString += item + ",";
                }
               
                //Nederstående linje sletter det sidste komma så det er lettere at parse.
                writeableString = writeableString.substring(0 , writeableString.length() - 1);

                bw.write(writeableString);
                
                System.out.println("Programmet udskriver score og skriver til fil via array");
                
                bw.close();
            } catch (IOException e) {
                System.out.println("df");
            }
            
        }
        
        public void GetHighScore() {
            
            // ikke helt done endnu
            
            try {
                
                Scanner x = new Scanner(new File("Score"));
                
            } catch (IOException e) {
                System.out.println("df");
            }
            
            
        }
        
        
}
