package main;

import java.util.List;

import board.EightQueens;

public class Queens {
	int iteration = 0;
	int tries = 0;
	
	public EightQueens tryBest() {
		EightQueens current = new EightQueens();
		int currentScore = current.score();

		while (true) {
			List<EightQueens> neighbors = current.neighbors();
			EightQueens best = neighbors.get(0);
			for (EightQueens board : neighbors) {
				if (board.score() < best.score()) {
					best = board;
				}
			}
			
			if (best.score() < currentScore) {
				current = best;
				currentScore = current.score();
				++iteration;
			} else 
				break;
		}
		return current;
	}
	
	public EightQueens findAsGoodAs(int score) {
		EightQueens current = tryBest();
		tries = 1;
		while (current.score() > score) {
			current = tryBest();
			tries++;
		}
		return current;
	}
	
	public static void main(String[] args) {
		Queens q = new Queens();
		EightQueens current = q.findAsGoodAs(0);
		
		System.out.println("Local Max " + current.score() + " after " + q.iteration +", " + q.tries);
		System.out.println(current);
	}

}
