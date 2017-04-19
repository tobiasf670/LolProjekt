package lolsoap;

import java.util.ArrayList;

/**
*
* @author Mads Hornbeck
*/
public class GameState {

	private int index = 0;
	private ArrayList<Champion> champions;
	private long endTime;
	private int skips = 0;
	private long skipPenalty = 0; // Milliseconds
	
	public GameState(ArrayList<Champion> champions) {
		this.champions = champions;
	}
	
	public boolean guessChampion(String guess) {
		boolean correct = champions.get(index).guessName(guess);
		if (correct) {
			index++;
			if (index == champions.size()) {
				endTime = System.currentTimeMillis();
			};
		}
		return correct;
	}
	
	public Champion getCurrentChampion() {
		return champions.get(index);
	}

	public boolean doneGuessing () {
		return index == champions.size();
	}
	
	public long getEndTime(){
		return endTime + skips * skipPenalty;
	}
	
	public void skip() {
		index++;
		skips++;
	}
}
