import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by catman on 5/27/15.
 */
public class Main {

    public static void main(String[] args) {
        int [][] board;
        int [] queenPosition;
        Scanner input = new Scanner(System.in);
        int size = input.nextInt();
        board = new int[size][size];
        queenPosition = new int[size];

        // Simple Backtracking

        SimpleBackTrack simple = new SimpleBackTrack();
        simple.PlaceNQueens(queenPosition, size, 0);
        simple.printAll(size);

        //

    }
}

class SimpleBackTrack {

    ArrayList<int[]> allSolutions = new ArrayList<>();
    boolean baseCall = true;

    public boolean PlaceNQueens(int [] queenPosition, int N, int i) {
        if (i==N) {

            if (baseCall) {
                print(queenPosition, N);
                baseCall = false;
            }
            //print(queenPosition, N);
            int [] temp = new int[N];
            for (int k=0;k<N;k++) {
                temp[k] = queenPosition[k];
            }
            allSolutions.add(temp);
            return true;
        }
        for (int j=0;j<N;j++) {
            queenPosition[i] = j;
            boolean flag = CheckConstraints(queenPosition,i);
            if (!flag) continue;
            boolean succ = PlaceNQueens(queenPosition, N, i+1);

        }
        return false;

    }

    public boolean CheckConstraints(int [] queenPosition, int r) {
        for (int i=0;i<r;i++) {
            if (queenPosition[i] == queenPosition[r] || queenPosition[r]-(r-i) == queenPosition[i]
                    || queenPosition[r]+(r-i) == queenPosition[i])
                return false;

        }
        return true;
    }

    public void print(int [] queenPosition, int N) {
        for (int j=0;j<N;j++) System.out.print(queenPosition[j]+1  + " ");
        System.out.println();
    }

    public void printAll(int N) {
        int s = allSolutions.size();
        System.out.println(s);
        for (int i=0;i<s;i++) {
            int [] t = allSolutions.get(i);
            print(t, N);
        }
    }

    public String toString() {
        return "Backtrack";
    }

}
