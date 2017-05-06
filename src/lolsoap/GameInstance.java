/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolsoap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class handles the individual games and their state. This consists of a
 * gameState object per player that contains a list of champions.
 * 
 * @author Mads Hornbeck
 */
public class GameInstance {

	private UUID id;
	private boolean gameStarted = false;
	private long startTime;
	private int numberOfChampions = 20;
	private Player winner = null;
	private ArrayList<Champion> championArray;

	private HashSet<Player> players;
	private HashMap<Player, GameState> playerGameStates = new HashMap<>();

	// TODO maybe add a player as the creator of the game. Where only they can start the game.
	public GameInstance() {
		this.players = new HashSet<Player>();
		
		// This creates a UUID which is a semi-random identifier of 128 bits;
		// read more here :
		// https://en.wikipedia.org/wiki/Universally_unique_identifier.
		id = UUID.randomUUID();
		championArray = makeChampionArray();
	}
	
	public void setNumberOfChampions(int numberOfChampions) {
		if(!gameStarted){
			playerGameStates.forEach((p, g) -> {
				g.setNumberOfChampions(numberOfChampions);
			});
		}
	}

	public UUID getGameId() {
		return id;
	}

	public void startGame() {
		if (!gameStarted) {
			gameStarted = true;
			startTime = System.currentTimeMillis();
		}
	}

	public void addPlayer(Player p) {
		this.players.add(p);
		p.joinGame(id);
		playerGameStates.put(p, new GameState(championArray, numberOfChampions));
	}

	public boolean guessChamp(Player p, String guess) {
		GameState gameState = playerGameStates.get(p);
		boolean correct = gameState.guessChampion(guess);
		checkIfDone(gameState, p);
		return correct;
	}

	public boolean playerIsDone(Player p) {
		GameState gameState = playerGameStates.get(p);
		return gameState.gameDone();
	}
	
	public boolean gameIsDone() {
		return winner != null;
	}
	
	public boolean isGameStarted() {
		return gameStarted;
	}

	public boolean hasWon(Player p) {
		return p.equals(winner);
	}

	public String getWinner() {
		if (winner != null) {
			return winner.getBrugernavn();
		}
		return null;
	}

	public String[] getUsernames() {
		HashSet<String> userNames = new HashSet<>();
		for (Player play : players) {
			userNames.add(play.getBrugernavn());
		}
		return userNames.toArray(new String[userNames.size()]);
	}

	public void skip(Player p) {
		GameState gameState = playerGameStates.get(p);
		checkIfDone(gameState, p);
		gameState.skip();
	}

	public Champion getCurrentChampion(Player p) {
		GameState gameState = playerGameStates.get(p);
		return gameState.getCurrentChampion();
	}

	public long getTimeTaken(Player p) {
		GameState gameState = playerGameStates.get(p);
		return gameState.getEndTime() - startTime;
	}
	
	private void checkIfDone(GameState gameState, Player p) {
		if(gameState.gameDone()) {
			p.endGame();
			setWinner();
		}
	}
	
	private void setWinner() {
		boolean allDone = true;
		int maxScoreSofar = 0;
		Player prospectiveWinner = null;
		for (Player p : players) {
			GameState gameState = playerGameStates.get(p);
			allDone &= gameState.gameDone();
			if (allDone && gameState.getScore() >= maxScoreSofar){
				int thisScore = gameState.getScore();
				// finds the winner if they both have same score.
				if (maxScoreSofar == thisScore && prospectiveWinner != null) {
					GameState proWinGame = playerGameStates.get(prospectiveWinner);
					if (proWinGame.getEndTime() > gameState.getEndTime()) {
						prospectiveWinner = p;
					}
				}
				// finds the winner in all other cases.
				else {
					maxScoreSofar = thisScore;
					prospectiveWinner = p;	
				}
				
			}
		}
		if (allDone) {
			if (winner == null) {
				winner = prospectiveWinner;
			}
		}
	}
	
	public String getSerializedGame() {
		if (winner != null) {
			GameState gameState = playerGameStates.get(winner);
			String info =
					id.toString() + " " 
				+ winner.getBrugernavn() + " "
				// TODO add champion count
				+ gameState.getScore() + " "
				+ gameState.getEndTime() + "\n";
			return info;
		}
		return "";
	}

	/*
	 * Queries the lol api and creates a ArrayList of the champions from JSON.
	 * TODO still needs to be in random order.
	 * 
	 * @author Tobias
	 */

	private ArrayList<Champion> makeChampionArray() {
		String lolVersion = null;
		String url = null;
		JSONArray jsonArray = null;
		Client client = ClientBuilder.newClient();
		Response res = client
				.target("https://global.api.riotgames.com/api/lol/static-data/EUW"
						+ "/v1.2/champion?champData=info&api_key=RGAPI-0120396b-3f2b-4cb3-bbc9-4f27240f6d45")
				.request(MediaType.APPLICATION_JSON).get();

		String svar = res.readEntity(String.class);

		try {

			JSONObject json = new JSONObject(svar);
			// JSONArray k = json.getJSONArray("data");

			JSONObject songs = json.getJSONObject("data");
			Iterator x = songs.keys();
			jsonArray = new JSONArray();

			while (x.hasNext()) {
				String key = (String) x.next();
				jsonArray.put(songs.get(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Response version = client
				.target("https://global.api.riotgames.com/api/lol/static-data/EUW"
						+ "/v1.2/versions?api_key=RGAPI-0120396b-3f2b-4cb3-bbc9-4f27240f6d45")
				.request(MediaType.APPLICATION_JSON).get();

		String versionNr = version.readEntity(String.class);

		try {
			JSONArray versionArray = new JSONArray(versionNr);
			lolVersion = versionArray.get(0).toString();
			url = "http://ddragon.leagueoflegends.com/cdn/" + lolVersion + "/img/champion/";

		} catch (Exception e) {
			e.printStackTrace();

		}

		ArrayList<Champion> newArray = new ArrayList<>();

		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				newArray.add(new Champion(jsonArray.getJSONObject(i), url));
			} catch (JSONException ex) {
				Logger.getLogger(GameInstance.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
                
                Collections.shuffle(newArray);
		return newArray;
	}
}
