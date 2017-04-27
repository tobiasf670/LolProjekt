package lolsoap;

import java.util.ArrayList;

/**
*
* @author Mads Hornbeck
*/
public class GameState {

	private int index = 0;
	private ArrayList<Champion> champions;
	private boolean gameDone = false;
	private long endTime;
	private int skips = 0;
	private long skipPenalty = 0; // Milliseconds
	
	public GameState(ArrayList<Champion> champions) {
		this.champions = champions;
	}
	
	public boolean guessChampion(String guess) {
		if (index < champions.size() && getCurrentChampion().guessName(guess)) {
			increaseIndex();
			return true;
		}
		return false;
	}
	
	public Champion getCurrentChampion() {
		int championCount = champions.size();
		if (index < championCount) {
			return champions.get(index);
		}
		return champions.get(championCount);
	}

	public boolean gameDone() {
		return gameDone;
	}
	
	public long getEndTime(){
		return endTime + skips * skipPenalty;
	}
	
	public void skip() {
		increaseIndex();
		skips++;
	}
	
	private void increaseIndex(){
		int championCount = champions.size();
		
		if (index < championCount){
			index++;	
		}
		// Also checks if the game is done.
		if (!gameDone && index >= championCount) {
			endTime = System.currentTimeMillis();
			gameDone = true;
		}
	}
}
