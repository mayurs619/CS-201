import java.util.Arrays;

/**
 * Simulate percolation thresholds for a grid-base system using
 * depth-first-search, aka 'flood-fill' techniques for determining if the top of
 * a grid is connected to the bottom of a grid.
 * <P>
 * Modified from the COS 226 Princeton code for use at Duke. The modifications
 * consist of supporting the <code>IPercolate</code> interface, renaming methods
 * and fields to be more consistent with Java/Duke standards and rewriting code
 * to reflect the DFS/flood-fill techniques used in discussion at Duke.
 * <P>
 * This class is designed to be extended, e.g.,
 * by DFS/BFS using stack or queue, respectively
 * 
 * <P>
 * @author Kevin Wayne, wayne@cs.princeton.edu
 * @author Owen Astrachan, ola@cs.duke.edu
 * @author Jeff Forbes, forbes@cs.duke.edu
 */

public class PercolationDefault implements IPercolate{
    
	protected int[][] myGrid;    // grid to simulate
	protected int myOpenCount;   // # cells open

	/**
	 * Initialize a grid so that all cells are blocked.
	 * 
	 * @param n
	 *            is the size of the simulated (square) grid
	 */
	public PercolationDefault(int n) {
		myGrid = new int[n][n];
		myOpenCount = 0;
		for (int[] row : myGrid) {
			Arrays.fill(row, BLOCKED);
		}
	}

	/**
	 * Open the cell at [row,col], no action
	 * if the cell is other than BLOCKED, meaning
	 * there is action if BLOCKED, and state
	 * will change to OPEN, and then perhaps FULL
	 * if it should be, checked by calling
	 * updateOnOpen
	 * 
	 * Subclasses can likely simply implement
	 * search, called from updateOnOpen
	 */
	@Override
	public void open(int row, int col) {
		
		if (! inBounds(row,col)) {
			throw new IndexOutOfBoundsException(
					String.format("(%d,%d) not in bounds", row,col));
		}
		if (myGrid[row][col] != BLOCKED) {
				return;
		}
		myOpenCount += 1;
		myGrid[row][col] = OPEN;
		updateOnOpen(row,col);
	}

	/**
	 * returns true if and only if cell at [row,col]
	 * has state == OPEN
	 */
	public boolean isOpen(int row, int col) {
		
		if (! inBounds(row,col)) {
			throw new IndexOutOfBoundsException(
					String.format("(%d,%d) not in bounds", row,col));
		}
		return myGrid[row][col] != BLOCKED;
	}

	/**
	 * returns true if and only if cell at [row,col]
	 * has state == FULL
	 */
	public boolean isFull(int row, int col) {
		
		if (! inBounds(row,col)) {
			throw new IndexOutOfBoundsException(
					String.format("(%d,%d) not in bounds", row,col));
		}
		
		return myGrid[row][col] == FULL;
	}

	/**
	 * Update cells that might be FULL rather
	 * than OPEN, the cell at [row,col] should
	 * have been just defined as OPEN rather
	 * than BLOCKED, now check if it should be FULL
	 * that is, updated when Open
	 * @param row
	 * @param col
	 */
	protected void updateOnOpen(int row, int col) {
		if (row == 0) {
			// it's in the top row, search from there
			search(row,col);		
			return;
		}
		int[] rd = {-1,0,0,1};
		int[] cd = {0,-1,1,0};
		for(int k=0; k < rd.length; k++) {
			int nr = row + rd[k];
			int nc = col + cd[k];
			
			if (inBounds(nr,nc) && isFull(nr,nc)) {
				search(row,col);
				return;
			}
		}
	}

	/**
	 * Returns true if and only if system percolates
	 */
	public boolean percolates() {
		// check bottom row for fullness
		for (int col = 0; col < myGrid[myGrid.length - 1].length; col++) {
			if (isFull(myGrid.length - 1, col)) {
				return true;
            }
        }
		return false;
	}

	/**
	 * Protected helper method to mark all cells that are open and reachable from
	 * (row,col). To be overridden in subclasses, e.g.,
	 * DFS and BFS
	 * 
	 * @param row
	 *            is the row coordinate of the cell being checked/marked
	 * @param col
	 *            is the col coordinate of the cell being checked/marked
	 */
	protected void search(int row, int col) {
		// out of bounds?
		if (! inBounds(row,col)) return;
		
		// full or NOT open, don't process
		if (isFull(row, col) || !isOpen(row, col)){
			return;
		}
		myGrid[row][col] = FULL;
		search(row - 1, col);
		search(row, col - 1);
		search(row, col + 1);
		search(row + 1, col);
	}

	public int numberOfOpenSites() {
		return myOpenCount;
	}
	
	/**
	 * Determine if (row,col) is valid for given grid
	 * @param row specifies row
	 * @param col specifies column
	 * @return true if (row,col) on grid, false otherwise
	 */
	protected boolean inBounds(int row, int col) {
		if (row < 0 || row >= myGrid.length) return false;
		if (col < 0 || col >= myGrid[0].length) return false;
		return true;
	}
}
