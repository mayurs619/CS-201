import java.util.*;

public class PercolationDFS extends PercolationDefault {

    PercolationDFS(int size) {
        super(size);
    }

    @Override
    public void search(int row, int col) {
        int fillWith = FULL;
        int lookFor = OPEN;
        int[] rowDelta = { -1, 1, 0, 0 };
        int[] colDelta = { 0, 0, -1, 1 };

        if (myGrid[row][col] != lookFor)
            return; // not part of blob

        Stack<int[]> stack = new Stack<>();
        myGrid[row][col] = fillWith;
        stack.push(new int[] { row, col });
        while (stack.size() != 0) {
            int[] coords = stack.pop();
            for (int k = 0; k < rowDelta.length; k++) {
                row = coords[0] + rowDelta[k];
                col = coords[1] + colDelta[k];
                if (inBounds(row, col) && myGrid[row][col] == lookFor) {
                    stack.push(new int[] { row, col });
                    myGrid[row][col] = fillWith;
                }
            }
        }
    }

}