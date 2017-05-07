package lolsoap;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import brugerautorisation.data.Bruger;
import brugerautorisation.transport.rmi.Brugeradmin;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;
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
			GameInstance game = new GameInstance(player);
			UUID id = game.getGameId();
			game.addPlayer(player);
			games.put(id, game);
			return id;
		}
		return null;
	}
	
	@Override
	public UUID[] findGames() {
		Set<UUID> keys = new HashSet<UUID>(games.keySet());
		// this filters out non-joinable games.
		keys.removeIf(k -> {
			GameInstance game = games.get(k);
			return game.isGameStarted();
		});
		return keys.toArray(new UUID[0]);
	}
	
	@Override
	public UUID[] findAllGames() {
		Set<UUID> keys = games.keySet();
		return keys.toArray(new UUID[0]);
	}
	
	@Override
	public String[] getUsernames(UUID gameId) {
		GameInstance game = games.get(gameId);
		if (game != null) {
			String[] players = game.getUsernames();
			return players;			
		}
		return null;
	}

	@Override
	public void joinGame(UUID gameId, String username) {
		Player player = players.get(username);
		GameInstance game = games.get(gameId);
		if (player != null && game != null){
			game.addPlayer(player);	
		}
	}
	
	@Override
	public UUID[] getPlayersGames(String username) {
		Player player = players.get(username);
		if (player != null) {
			Set<UUID> gamesSet = player.getGames();
			return gamesSet.toArray(new UUID[0]);
		}
		return null;
	}
	
	@Override
	public int getScore(UUID gameId, String username) {
		Player player = players.get(username);
		GameInstance game = games.get(gameId);
		if (player != null && game != null && game.gameIsDone()){
			return game.getScore(player);
		}
		return 0;
	}
	
	@Override 
	public long getTimeTaken(UUID gameId, String username){
		Player player = players.get(username);
		GameInstance game = games.get(gameId);
		if (player != null && game != null && game.gameIsDone()){
			return game.getTimeTaken(player);
		}
		return 0;
	}

	@Override
	public void startGame(String username) {
		Player player = players.get(username);
		if (player != null) {
			GameInstance game = getGame(player);
			if (game != null) {
				game.startGame(player);	
			}
		}
	}
	
	@Override 
	public void setNumberOfChamps(UUID gameId, int number) {
		GameInstance game = games.get(gameId);
		if (game != null) {
			game.setNumberOfChampions(number);
		}
	}
	
	@Override
	public boolean isGameStarted(String username) {
		Player player = players.get(username);
		GameInstance game = getGame(player);
		if (game != null) {
			return game.isGameStarted();
		}
		return false;
	}
	
	@Override
	public boolean playerDoneGuessing(UUID gameId, String username) {
		Player player = players.get(username);
		GameInstance game = games.get(gameId);
		if (player != null && game != null) {
			return game.playerIsDone(player);
		}
		return false;
	}
	
	@Override
	public boolean isGameDone(UUID gameId) {
		GameInstance game = games.get(gameId);
		if (game != null) {
			return game.gameIsDone();	
		}
		return false;
	}

	@Override
	public boolean didIWin(UUID gameId, String username) {
		Player player = players.get(username);
		GameInstance game = games.get(gameId);
		if (player != null && game != null) {
			return game.hasWon(player);	
		}
		return false;
	}
	
	@Override
	public String getWinner(UUID gameId) {
		GameInstance game = games.get(gameId);
		if (game != null) {
			return game.getWinner();	
		}
		return "";
	}

	@Override
	public String getChampionImgUrl(String username) {
		Player player = players.get(username);
		GameInstance game = getGame(player);
		if (game != null) {
			if (game.isGameStarted()) {
				Champion champion = game.getCurrentChampion(player);
				return  champion.getUrl();	
			}
			return "Game not started";
		}
		return "Error";
	}
	
	@Override
	public String getChampionTitle(String username) {
		Player player = players.get(username);
		GameInstance game = getGame(player);
		if (game != null) {
			if (game.isGameStarted()) {
				Champion champion = game.getCurrentChampion(player);
				return  champion.getTitle();
			}
			return "Game not started";
		}
		// TODO real error message.
		return "Error";
	}
	

	@Override
	public boolean guessChampion(String username, String guess) {
		Player player = players.get(username);
		GameInstance game = getGame(player);
		if (game != null && game.isGameStarted()) {
			return game.guessChamp(player, guess);
		}
		return false;
	}

	@Override
	public void skipChampion(String username) {
		Player player = players.get(username);
		GameInstance game = getGame(player);
		if (game != null && game.isGameStarted()) {
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
