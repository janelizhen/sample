public class EightQueen {
	
	public static int[][] board = new int[8][8];
	
	public static void main(String[] args) {
		// initialize the board
		for (int i=0; i<board.length; i++){
			for (int j=0; j<board[i].length; j++){
				// 0 means empty, 1 means with queen
				board[i][j] = 0;
			}
		}
		
		// start from first row
		solve(0);
	}
	
	// place the queen on a particular row
	private static void solve(int row){
		// solved
		if (row >= board.length) {
			printBoard();
			System.exit(0);
		}
		else if (rowSolved(row)){
			solve(row+1);
		}
		else {
			for (int j=0; j<board[row].length; j++){
				if (checkCol(j) && checkDiag(row, j)) {
					board[row][j] = 1;
					System.out.println("solved row: " + row);
					// solve next row
					solve(row+1);
					// if next row cannot be solved, clear this row
					clearRow(row);
				}
			}
		}
		System.out.println("backtrack row: " + (row-1));
	}
	
	private static void clearRow(int row) {
		for (int j=0; j<board[row].length; j++) {
			board[row][j] = 0;
		}
	}
	
	private static boolean rowSolved(int row) {
		for (int j=0; j<board[row].length; j++) {
			if (board[row][j] == 1) 
				return true;
		}
		return false;
	}
	
	private static boolean checkCol(int col){
		for (int i=0; i<board.length; i++) {
			if (board[i][col] == 1)
				return false;
		}
		return true;
	}
	
	private static boolean checkDiag(int row, int col){
		for (int i=row,j=col; i>0&&j>0; i--,j--) {
			if (board[i][j] == 1)
				return false;
		}
		for (int i=row,j=col; i<board.length&&j<board[i].length; i++,j++){
			if (board[i][j] == 1)
				return false;
		}
		for (int i=row,j=col; i>0&&j<board[i].length; i--,j++) {
			if (board[i][j] == 1)
				return false;
		}
		for (int i=row,j=col; i<board.length&&j>0; i++,j--) {
			if (board[i][j] == 1)
				return false;
		}
		return true;
	}
	
	// Print out the board
	public static void printBoard(){
		for (int row=0; row<board.length; row++){
			for (int col=0; col<board[row].length; col++){
				System.out.print(board[row][col] + "\t");
			}
			System.out.println();
		}
	}

}
