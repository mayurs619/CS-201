import java.util.*;


public class PercolationUF implements IPercolate{

    private IUnionFind myFinder;
    private boolean[][] myGrid;
    private final int VTOP;
    private final int VBOTTOM;
    private int myOpenCount;

    public PercolationUF(IUnionFind finder, int size){
        finder.initialize(size * size + 2);
        myFinder =  finder;
        VTOP = size * size;
        VBOTTOM = size * size + 1;
        myGrid = new boolean[size][size];
        myOpenCount = 0;
    }

    @Override
    public void open(int row, int col) {
        if (! inBounds(row,col)) {
			throw new IndexOutOfBoundsException(
					String.format("(%d,%d) not in bounds", row,col));
		}
        if(myGrid[row][col] == true) return;
         
        myGrid[row][col] = true;
        myOpenCount++;

        if(row == 0){
            myFinder.union(row*myGrid.length + col, VTOP);
        }
        if(row == myGrid[0].length-1){
            myFinder.union(row*myGrid.length + col, VBOTTOM);
        }
        if(inBounds(row-1, col) && myGrid[row-1][col] == true){
            myFinder.union(row*myGrid.length + col, (row-1)*myGrid.length + col);
        }
         if(inBounds(row + 1, col) && myGrid[row+1][col] == true){
            myFinder.union(row*myGrid.length + col, (row+1)*myGrid.length + col);
        }

         if(inBounds(row, col - 1) && myGrid[row][col-1] == true){
            myFinder.union(row*myGrid.length + col, row*myGrid.length +(col-1));
        }
         if(inBounds(row, col + 1) && myGrid[row][col+1] == true){
            myFinder.union(row*myGrid.length + col, row*myGrid.length + col+1);
        }
    }

    @Override
    public boolean isOpen(int row, int col) {
        if (! inBounds(row,col)) {
			throw new IndexOutOfBoundsException(
					String.format("(%d,%d) not in bounds", row,col));
		}
         return myGrid[row][col];
    }

    @Override
    public boolean isFull(int row, int col) {
        if (! inBounds(row,col)) {
			throw new IndexOutOfBoundsException(
					String.format("(%d,%d) not in bounds", row,col));
		}
        return myFinder.connected(row*myGrid.length + col, VTOP);

    }

    @Override
    public boolean percolates() {
        return myFinder.connected(VTOP, VBOTTOM);
    }

    @Override
    public int numberOfOpenSites() {
        return myOpenCount;
    }

    private boolean inBounds(int row, int col) {
		if (row < 0 || row >= myGrid.length) return false;
		if (col < 0 || col >= myGrid[0].length) return false;
		return true;
	}
}