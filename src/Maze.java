
public class Maze {
	class Cell {
		public boolean deadend = false, noRight = false, noLeft = false, noDown = false, noUp = false, start = false,
		target = false;
		public int horizontalJump = 0;
		public int verticalJump = 0;
		public int wait = 0;
		public int shortest = -1;// not yet discovered
		public Cell nextStep;
		public int row, col;
		
		Cell(int row, int col) {
			this.row = row;
			this.col = col;
		}
		
		Cell(int row, int col, int wait) {
			this.row = row;
			this.col = col;
			this.wait = wait;
		}
		
		public String toString() {
			if (wait == 0)
			return "(" + row + ", " + col + ")";
			return new Cell(row, col, wait - 1).toString() + "->(" + row + ", " + col + ")";
		}
	}
	
	private int rows, columns;
	private Cell[][] map;
	
	public int getRows() {
		return rows;
	}
	
	public int getColumns() {
		return columns;
	}
	
	public Maze(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		map = new Cell[rows][columns];
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++)
				map[r][c] = new Cell(r, c);
	}
	
	private Cell getStart() {
		for (int r = 0; r < rows; r++)
		for (int c = 0; c < columns; c++)
		if (map[r][c].start)
		return map[r][c];
		return null;
	}
	
	private Cell getTarget() {
		for (int r = 0; r < rows; r++)
		for (int c = 0; c < columns; c++)
		if (map[r][c].target)
		return map[r][c];
		return null;
	}
	
	public void solve() {
		// Step 1: find the start; it should be the top-left square if you don't do the
		// extra credit.
		Cell start = getStart(), target = getTarget();
		// Step 2: call the recursive helper function
		solveHelper(start, target);
	}
	
	private void solveHelper(Cell source, Cell target) {
		
		int sourceRow = source.row, targetRow = target.row;
		int sourceCol = source.col, targetCol = target.col;
		int rowsMinus1 = rows - 1, columnMinus1 = columns - 1;

		if (sourceRow == targetRow && sourceCol == targetCol)// base case
		return;
		if (sourceRow < rowsMinus1 && map[sourceRow + 1][sourceCol].shortest == -1)// down cell is not discovered yet!
		solveHelper(map[sourceRow + 1][sourceCol], target);// call it recursively for bottom neighbor
		if (sourceCol < columnMinus1 && map[sourceRow][sourceCol + 1].shortest == -1)// right cell is not discovered
		// yet!
		solveHelper(map[sourceRow][sourceCol + 1], target);// call it recursively for right neighbor
		if (sourceRow == rowsMinus1) {
			if(source.deadend) { // check if row 5, columns 0-3 contains DE
				source.nextStep = null;
			} else if(source.noDown) { // check if row 5, columns 0-3 contain XD
				if(source.horizontalJump > 0) {
					source.shortest = map[sourceRow][sourceCol + source.horizontalJump].shortest + source.wait + 1;
					source.nextStep = map[sourceRow][sourceCol + source.horizontalJump];
				} else {
					source.shortest = map[sourceRow][sourceCol + 1].shortest + source.wait + 1;
					source.nextStep = map[sourceRow][sourceCol + 1];
				}
			} else { // assign row 5, columns 0-4 the next piece with time depending on jump and wait time
				if(source.horizontalJump > 0) {
					source.shortest = map[sourceRow + source.verticalJump][sourceCol + source.horizontalJump].shortest + source.wait + 1;
					source.nextStep = map[sourceRow + source.verticalJump][sourceCol + source.horizontalJump];
				} else {
					source.shortest = map[sourceRow + source.verticalJump][sourceCol + 1].shortest + source.wait + 1;
					source.nextStep = map[sourceRow + source.verticalJump][sourceCol + 1];
				}
			}
		} else if (sourceCol == columnMinus1) {
			if(source.deadend) { // check if rows 0-5, column 4 contains DE
				source.nextStep = null;
			} else {
				if(source.noRight) { // check if rows 0-5, column 4 contains XR
					if(source.verticalJump > 0) {
						source.shortest = map[sourceRow + source.verticalJump][sourceCol].shortest + source.wait + 1;
						source.nextStep = map[sourceRow + source.verticalJump][sourceCol];
					} else {
						source.shortest = map[sourceRow + 1][sourceCol].shortest + source.wait + 1;
						source.nextStep = map[sourceRow + 1][sourceCol];
					}
				} else { // assign row 0-5, columns 4 the next piece with time depending on jump and wait time
					if(source.verticalJump > 0) {
						source.shortest = map[sourceRow + source.verticalJump][sourceCol + source.horizontalJump].shortest + source.wait + 1;
						source.nextStep = map[sourceRow + source.verticalJump][sourceCol + source.horizontalJump];
					} else {
						source.shortest = map[sourceRow + 1][sourceCol + source.horizontalJump].shortest + source.wait + 1;
						source.nextStep = map[sourceRow + 1][sourceCol + source.horizontalJump];
					}
				}
			}
		} else { // assign diagonal, right or bottom piece with appropriate jump and wait time for rows 0-4, columns 0-3
			Cell rightNeighbor = map[sourceRow][sourceCol + (source.horizontalJump > 0 ? source.horizontalJump : 1)],
			bottomNeighbor = map[sourceRow + (source.verticalJump > 0 ? source.verticalJump : 1)][sourceCol],
			diagonalNeighbor = (source.verticalJump > 0 && source.horizontalJump > 0) ? map[sourceRow + source.verticalJump][sourceCol + source.horizontalJump] : null;

			if(source.noRight) { // XR located
				rightNeighbor = null;
				if(diagonalNeighbor != null) { // diagonal ladder present
					source.shortest = Math.min(bottomNeighbor.shortest + source.wait, diagonalNeighbor.shortest + source.wait) + 1;
					if(source.shortest == bottomNeighbor.shortest + source.wait + 1) { // assign bottom neighbour piece
						source.nextStep = bottomNeighbor;
					} else { // assign diagonal jump piece 
						source.nextStep = diagonalNeighbor.nextStep; 
					}
				} else { // no diagonal ladder present, bottom piece if only possibility
					source.shortest = bottomNeighbor.shortest + source.wait + 1;
					source.nextStep = bottomNeighbor.nextStep;
				}
			} else if(source.noDown) { // XD located
				bottomNeighbor = null;
				if(diagonalNeighbor != null) { // diagonal ladder present
					source.shortest = Math.min(rightNeighbor.shortest + source.wait, diagonalNeighbor.shortest + source.wait) + 1;
					if(source.shortest == rightNeighbor.shortest + source.wait + 1) { // assign right neighbour piece
						source.nextStep = rightNeighbor;
					} else { // assign diagonal jump piece
						source.nextStep = diagonalNeighbor.nextStep;
					}
				} else { // no diagonal ladder present, right piece only possibility
					source.shortest = rightNeighbor.shortest + source.wait + 1;
					source.nextStep = rightNeighbor.nextStep;
				}
			} else { // No XR or XD located
				source.shortest = Math.min(bottomNeighbor.shortest + source.wait, rightNeighbor.shortest + source.wait) + 1;
				if(diagonalNeighbor != null) { // diagonal ladder located
					source.shortest = Math.min(source.shortest, diagonalNeighbor.shortest + source.wait + 1);
					if(source.shortest == bottomNeighbor.shortest + source.wait + 1) { // assign bottom piece
						source.nextStep = bottomNeighbor;
					} else if(source.shortest == rightNeighbor.shortest + source.wait + 1) { // assign right piece
						source.nextStep = rightNeighbor;
					} else { // assign diagonal piece
						source.nextStep = diagonalNeighbor;
					}
				} else { // no diagonal ladder located
					if (source.shortest == bottomNeighbor.shortest + source.wait + 1) { // assign bottom piece
						source.nextStep = bottomNeighbor;
					} else { // assign right piece
						source.nextStep = rightNeighbor;
					}
				}
			}
		}
	}
	
	public void addTarget(int rowIndex, int columnIndex) {
		map[rowIndex][columnIndex].target = true;
		map[rowIndex][columnIndex].shortest = 0;
		
	}
	
	public void addStart(int rowIndex, int columnIndex) {
		map[rowIndex][columnIndex].start = true;
	}
	
	public void addDeadEnd(int rowIndex, int columnIndex) {
		map[rowIndex][columnIndex].deadend = true;
	}
	
	public void addNoDown(int rowIndex, int columnIndex) {
		map[rowIndex][columnIndex].noDown = true;
	}
	
	public void addNoUp(int rowIndex, int columnIndex) {
		map[rowIndex][columnIndex].noUp = true;
	}
	
	public void addNoLeft(int rowIndex, int columnIndex) {
		map[rowIndex][columnIndex].noLeft = true;
	}
	
	public void addVerticalJump(int rowIndex, int columnIndex, int length) {
		map[rowIndex][columnIndex].verticalJump = length;
	}
	
	public void addNoRight(int rowIndex, int columnIndex) {
		map[rowIndex][columnIndex].noRight = true;
	}
	
	public void addHorizontalJump(int rowIndex, int columnIndex, int length) {
		map[rowIndex][columnIndex].horizontalJump = length;
	}
	
	public void addDelay(int rowIndex, int columnIndex, int length) {
		map[rowIndex][columnIndex].wait = length;
	}
	
	public void printSolution() {
		Cell source = getStart();
		System.out.println("Shortest path of length " + source.shortest + ": ");
		Cell current = source;
		while (current != null) {
			System.out.print(current + (current.nextStep == null ? "" : "->"));
			current = current.nextStep;
		}
		
	}
}
