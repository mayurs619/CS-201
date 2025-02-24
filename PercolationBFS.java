import java.util.*;

public class PercolationBFS extends PercolationDefault {

    public PercolationBFS(int n) {
        super(n);
    }

    @Override
    public void search(int row, int col) {

        int fillWith = FULL;
        int lookFor = OPEN;
        int[] rowDelta = {-1,1,0,0};
        int[] colDelta = {0,0,-1,1};

        Queue<int[]> qp = new LinkedList<>();
        myGrid[row][col] = fillWith;  // mark pixel
        qp.add(new int[]{row,col});
        while (qp.size() != 0){
            int[] p = qp.remove();
            for(int k=0; k < rowDelta.length; k++){
                row = p[0] + rowDelta[k];
                col = p[1] + colDelta[k];
                if (inBounds(row,col) && myGrid[row][col] == lookFor){
                    qp.add(new int[]{row,col});
                    myGrid[row][col] = fillWith;
                    
                }
            }
        }

        /*if (!inBounds(row, col))
            return;

        // full or NOT open, don't process
        if (isFull(row, col) || !isOpen(row, col))
            return;

        Queue<int[]> qp = new LinkedList<>();
        myGrid[row][col] = FULL;
        qp.add(new int[]{row,col});
        
        while (qp.size() != 0){
            int[] p = qp.remove();
            for(int k=0; k < myGrid[row].length; k++){
                row = p[0] + myGrid[row][k];
                col = p[1] + myGrid[k][col];
                if (inBounds(row,col) && myGrid[row][col] == OPEN){
                    qp.add(new int[]{row,col});
                    myGrid[row][col] = FULL;
                }
            }
        }*/

    }

}