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
		if (index < champions.size() && champions.get(index).guessName(guess)) {
			increaseIndex();
			return true;
		}
		return false;
	}
	
	public Champion getCurrentChampion() {
		// TODO maybe add index out of bounds check here.
		return champions.get(index);
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
		
		if (index < championCount - 1){
			index++;	
		}
		// Also checks if the game is done.
		if (!gameDone && index >= championCount) {
			endTime = System.currentTimeMillis();
			gameDone = true;
		}
	}
}
