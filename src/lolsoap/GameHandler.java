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
	
	@Override
	public synchronized UUID createNewGame(Player p) {
		GameInstance game = new GameInstance();
		UUID id = game.getGameId();
                System.out.println(id);
		game.addPlayer(p);
		games.put(id, game);
		return id;
	}
	
	@Override
	public Set<UUID> findGames() {
		Set<UUID> keys = games.keySet();
		return keys;
	}
	
	@Override
	public Set<Player> getPlayers(UUID gameId) {
		GameInstance game = games.get(gameId);
		Set<Player> players = game.getPlayers();
		return players;
	}

	@Override
	public void joinGame(UUID gameId, Player p) {
		GameInstance game = games.get(gameId);
		game.addPlayer(p);
	}

	@Override
	public void startGame(UUID gameId) {
		GameInstance game = games.get(gameId);
		game.startGame();
	}
	
	@Override
	public boolean isGameDone(UUID gameId) {
		GameInstance game = games.get(gameId);
		return game.isDone();
	}

	@Override
	public boolean didIWin(UUID gameId, Player p) {
		GameInstance game = games.get(gameId);
		return game.hasWon(p);
	}

	@Override
	public Champion getCurrentChampion (UUID gameId, Player p) {
		GameInstance game = games.get(gameId);
		return  game.getCurrentChampion(p);
	}
	

	@Override
	public boolean guessChampion(UUID gameId, Player p, String guess) {
		GameInstance game = games.get(gameId);
		return game.guessChamp(p, guess);
	}

	@Override
	public void skipChampion(UUID gameId, Player p) {
		GameInstance game = games.get(gameId);
		game.skip(p);
	}

	@Override
	public Player hentBruger(String user, String pass) throws Exception {
        Brugeradmin ba = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
        Bruger b = ba.hentBruger(user, pass);
        Player player = new Player(b);
        return player;
	}
        
        
        @Override
	public Bruger hentBruger1(String user, String pass) throws Exception {
        Brugeradmin ba = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
        Bruger b = ba.hentBruger(user, pass);
        return b;
	}
	

}
