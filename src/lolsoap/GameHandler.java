package lolsoap;

import java.rmi.Naming;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import brugerautorisation.data.Bruger;
import brugerautorisation.transport.rmi.Brugeradmin;
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
		GameInstance game = new GameInstance();
		UUID id = game.getGameId();
		game.addPlayer(player);
		games.put(id, game);
		return id;
	}
	
	@Override
	public UUID[] findGames() {
		Set<UUID> keys = games.keySet();
		return keys.toArray(new UUID[0]);
	}
	
	@Override
	public Set<String> getUsernames(UUID gameId) {
		GameInstance game = games.get(gameId);
		Set<String> players = game.getUsernames();
		return players;
	}

	@Override
	public void joinGame(UUID gameId, String username) {
		Player player = players.get(username);
		GameInstance game = games.get(gameId);
		game.addPlayer(player);
	}

	@Override
	public void startGame(String username) {
		Player player = players.get(username);
		UUID gameId = player.getCurrentGame();
		GameInstance game = games.get(gameId);
		game.startGame();
	}
	
	@Override
	public boolean isGameDone(String username) {
		Player player = players.get(username);
		UUID gameId = player.getCurrentGame();
		GameInstance game = games.get(gameId);
		return game.isDone();
	}

	@Override
	public boolean didIWin(UUID gameId, String username) {
		Player player = players.get(username);
		GameInstance game = games.get(gameId);
		return game.hasWon(player);
	}

	@Override
	public String getChampionImgUrl(String username) {
		Player player = players.get(username);
		UUID gameId = player.getCurrentGame();
		GameInstance game = games.get(gameId);
		Champion champion = game.getCurrentChampion(player);
		return  champion.getUrl();
	}
	
	@Override
	public String getChampionTitle(String username) {
		Player player = players.get(username);
		UUID gameId = player.getCurrentGame();
		GameInstance game = games.get(gameId);
		Champion champion = game.getCurrentChampion(player);
		return  champion.getTitle();
	}
	

	@Override
	public boolean guessChampion(String username, String guess) {
		Player player = players.get(username);
		UUID gameId = player.getCurrentGame();
		GameInstance game = games.get(gameId);
		return game.guessChamp(player, guess);
	}

	@Override
	public void skipChampion(String username) {
		Player player = players.get(username);
		UUID gameId = player.getCurrentGame();
		GameInstance game = games.get(gameId);
		game.skip(player);
	}

	@Override
	public String hentBruger(String user, String pass) throws Exception {
        Brugeradmin ba = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
        Bruger b = ba.hentBruger(user, pass);
        Player player = players.get(b.brugernavn);
        if (player == null) {
        	player = new Player(b);
        	players.put(player.getBrugernavn(), player);
        }
        return player.getBrugernavn();
	}
}
