package board;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EightQueens {
	public static final int NUMBER_OF_QUEENS = 8;

	public static final String EVEN_ROW;
	public static final String ODD_ROW;

	static {
		StringBuffer evenRow = new StringBuffer("# ");
		while (evenRow.length() < (NUMBER_OF_QUEENS / 2))
			evenRow.append(evenRow);
		evenRow.append(evenRow.substring(0, NUMBER_OF_QUEENS - evenRow.length()));
		EVEN_ROW = evenRow.toString();

		StringBuffer oddRow = new StringBuffer(" #");
		while (oddRow.length() < (NUMBER_OF_QUEENS / 2))
			oddRow.append(oddRow);
		oddRow.append(oddRow.substring(0, NUMBER_OF_QUEENS - oddRow.length()));
		ODD_ROW = oddRow.toString();
	}

	private Random random;
	private int[] columns = new int[NUMBER_OF_QUEENS];

	public EightQueens() {
		random = new Random();
		populate_queens();
	}

	public EightQueens(long seed) {
		random = new Random(seed);
		populate_queens();
	}

	public EightQueens(int ... initialColumns) {
		random = new Random();
		for (int column = 0; column < NUMBER_OF_QUEENS; ++column)
			columns[column] = initialColumns[column];
	}

	public EightQueens(EightQueens orig) {
		random = new Random();
		for (int column = 0; column < NUMBER_OF_QUEENS; ++column)
			columns[column] = orig.columns[column];		
	}
	
	private void populate_queens() {
		for (int column = 0; column < NUMBER_OF_QUEENS; ++column)
			columns[column] = random.nextInt(NUMBER_OF_QUEENS);
	}

	private int[] queensPerColumn() {
		int[] queensInColumn = new int[NUMBER_OF_QUEENS];
		for (int column = 0; column < NUMBER_OF_QUEENS; ++column)
			queensInColumn[column] = 0;

		for (int row = 0; row < NUMBER_OF_QUEENS; ++row)
			queensInColumn[columns[row]]++;

		return queensInColumn;
	}

	private int[] queensPerMajorDiagonal() {
		final int numberOfDiagonals = 2 * NUMBER_OF_QUEENS - 1;
		int[] queensInDiagonal = new int[numberOfDiagonals];
		for (int diagonal = 0; diagonal < numberOfDiagonals; ++diagonal)
			queensInDiagonal[diagonal] = 0;

		for (int row = 0; row < NUMBER_OF_QUEENS; ++row) {
			int diagonal = row - columns[row] + (NUMBER_OF_QUEENS - 1);
			queensInDiagonal[diagonal]++;
		}
		return queensInDiagonal;
	}

	private int[] queensPerMinorDiagonal() {
		final int numberOfDiagonals = 2 * NUMBER_OF_QUEENS - 1;
		int[] queensInDiagonal = new int[numberOfDiagonals];
		for (int diagonal = 0; diagonal < numberOfDiagonals; ++diagonal)
			queensInDiagonal[diagonal] = 0;

		for (int row = 0; row < NUMBER_OF_QUEENS; ++row) {
			int diagonal = row + columns[row];
			queensInDiagonal[diagonal]++;
		}
		return queensInDiagonal;
	}
	
	public void move(int row, int column) {
		columns[row] = column;
	}
	
	public List<EightQueens> neighbors() {
		List<EightQueens> result = new ArrayList<EightQueens>(NUMBER_OF_QUEENS * (NUMBER_OF_QUEENS - 1));
		for (int row = 0; row < NUMBER_OF_QUEENS; ++row) {
			for (int column = 0; column < NUMBER_OF_QUEENS; ++column) {
				if (column != columns[row]) {
					EightQueens neighbor = new EightQueens(this);
					neighbor.move(row,  column);
					result.add(neighbor);
				}
			}
		}
		return result;
	}
	
	public int score() {
		int attackingPairs = 0;
		int [] columns = queensPerColumn();
		for (int i = 0; i < columns.length; ++i) 
			attackingPairs += (columns[i] * (columns[i] - 1)) / 2;

		int [] majors = queensPerMajorDiagonal();
		for (int i = 0; i < majors.length; ++i) 
			attackingPairs += (majors[i] * (majors[i] - 1)) / 2;
		
		int [] minors = queensPerMinorDiagonal();
		for (int i = 0; i < minors.length; ++i) 
			attackingPairs += (minors[i] * (minors[i] - 1)) / 2;
		return attackingPairs;
	}
	
	@Override
	public String toString() {
		StringBuffer board = new StringBuffer();
		for (int row = 0; row < NUMBER_OF_QUEENS; ++row)
			if (row % 2 == 0)
				board.append(EVEN_ROW.substring(0, columns[row]) + "Q"
						+ EVEN_ROW.substring(columns[row] + 1) + "\n");
			else
				board.append(ODD_ROW.substring(0, columns[row]) + "Q"
						+ ODD_ROW.substring(columns[row] + 1) + "\n");
		return board.toString();
	}

}
