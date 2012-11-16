public class SudokuSolver {
	
	public static int[][] board = new int[9][9];
	
	public static void main(String[] args){
		// initialize the board
		for (int row=0; row<9; row++){
			for (int col=0; col<9; col++){
				board[row][col] = 0;
			}
		}
		
		// start from the very first cell to solve the sudoku
		solve(0,0);
	}
	
	public static void solve(int row, int col){
		// when row > 8, the puzzle should have been solved so we print the board and exit
		if (row > 8){
			printBoard();
			System.exit(0);
		}
		// if the cell is already filled, continue with next cell
		if (board[row][col] != 0){
			if (col < 8)
				// next cell is at the same row
				solve(row, col+1);
			else
				// next cell is at the next row
				solve(row+1, 0);
		}
		// if the cell is not filled, try to fill it
		else{
			// Loop through all nine numbers and try to find a valid one
			for (int num=1; num<=9; num++){
				if (validRow(row,num)&&validCol(col,num)&&validSquare(row,col,num)){
					board[row][col] = num;
					// try to solve next cell
					if (col < 8)
						// next cell is at the same row
						solve(row, col+1);
					else
						// next cell is at the next row
						solve(row+1, 0);
				}
			}
			// No valid number find for this cell
			// clean up this cell
			board[row][col] = 0;
			
			// as you return here, the previous caller "solve(row, col-1) or solve(row-1, 8)" will try to figure out what to do
			// this is backtracking
		}
	}
	
	// Check whether it is valid to put num consider row rule
	public static boolean validRow(int row, int num){
		for (int col=0; col<9; col++){
			if (board[row][col] == num) return false;
		}
		return true;
	}
	
	// Check whether it is valid to put num consider col rule
	public static boolean validCol(int col, int num){
		for (int row=0; row<9; row++){
			if (board[row][col] == num) return false;
		}
		return true;
	}
	
	// Check whether it is valid to put num consider square rule
	public static boolean validSquare(int row, int col, int num){
		int r = row/3*3, c = col/3*3;
		for (int i=r;i<r+3;i++){
			for (int j=c;j<c+3;j++){
				if (board[i][j] == num) return false;
			}
		}
		return true;
	}
	
	// Print out the board
	public static void printBoard(){
		for (int row=0; row<9; row++){
			for (int col=0; col<9; col++){
				System.out.print(board[row][col] + "\t");
			}
			System.out.println();
		}
	}
}