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
	private int championCount = 20;
	private int skips = 0;
	private long skipPenalty = 0; // Milliseconds
	private int score = 0;
	
	public GameState(ArrayList<Champion> champions, int numberOfChampions) {
		this.champions = champions;
		this.championCount = numberOfChampions;
	}
	
	public void setNumberOfChampions(int i) {
		if (champions.size() > i) {
			championCount = i;
		}
	}
	
	public boolean guessChampion(String guess) {
		boolean correct = getCurrentChampion().guessName(guess);
		if (index < champions.size() && correct) {
			score++;
		}
		increaseIndex();
		return correct;
	}
	
	public Champion getCurrentChampion() {
		if (index < championCount) {
			return champions.get(index);
		}
		return champions.get(championCount);
	}

	public boolean gameDone() {
		return gameDone;
	}
	
	public long getEndTime(){
		if (gameDone) {
			return endTime + skips * skipPenalty;
		}
		return 0;
	}
	
	public int getScore() {
		return score;
	}
	
	public void skip() {
		increaseIndex();
		skips++;
	}
	
	private void increaseIndex(){
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
