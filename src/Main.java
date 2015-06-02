import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by catman on 5/27/15.
 */
public class Main {
    public static Scanner input;
    public static void main(String[] args) {
        input = new Scanner(System.in);
        System.out.println("Enter Dimension : ");
        int size = input.nextInt();
        // Simple Backtracking

        Solver solver = new Solver(size);
        solver.Solve();

        //

    }
}

class Solver {

    ArrayList<int[]> allSolutions;
    boolean baseCall;
    int size;
    int [][] board;

    public Solver(int N) {
        allSolutions = new ArrayList<int[]>();
        baseCall = true;
        size = N;
        board = new int[N][N];

    }

    public void Solve() {
        while (true) {
            System.out.println("1. Simple Backtracking\n2. FC & MRV\n3. Exit");
            int in = Main.input.nextInt();
            if (in == 3) break;
            else if (in == 1) {
                allSolutions = new ArrayList<int[]>();
                baseCall = true;
                System.out.println("Simple Backtracking");
                int [] queenPosition = new int[size];
                PlaceNQueens(queenPosition, size, 0);
                printAll(size);
            }
            else if (in == 2) {
                System.out.println("With Forward Checking & MRV");
                baseCall = true;
                allSolutions = new ArrayList<int[]>();
                int [] queenPosition = new int[size];
                for (int i=0;i<size;i++) queenPosition[i] = -1;
                PlaceNQUeensFCMRV(queenPosition, size);
                printAll(size);

            }
        }



    }

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

    public boolean PlaceNQUeensFCMRV(int [] queenPosition, int N) {
        int queensLeft = 0;
        for (int i=0;i<size;i++) {
            if (queenPosition[i] == -1) queensLeft++;
        }
        //System.out.println("Left " + queensLeft);
        if (queensLeft == 0) {
            if (baseCall) {
                print(queenPosition, N);
                baseCall = false;
            }
            int [] temp = new int[N];
            for (int k=0;k<N;k++) {
                temp[k] = queenPosition[k];
            }
            allSolutions.add(temp);
            return true;
        }
        int i = SelectRowMRV(queenPosition);

        ArrayList<Integer> validCells = ValidCells(i);
        //for (Integer j:validCells) System.out.print(j + " ");
        //System.out.println();
        for (Integer j: validCells) {
            //System.out.println("Row " + i + " Col " + j);
            queenPosition[i] = j;
            int [][] backup = new int[size][size];
            for (int k=0;k<size;k++) {
                for (int l=0;l<size;l++) {
                    backup[k][l] = board[k][l];
                }
            }
            boolean flag = ForwardCheck(queenPosition, i, j);
            //System.out.println(flag);
            //System.out.println("After");
            //printBoard();
            //for (int k=0;k<size;k++) System.out.print(queenPosition[k] + " ");
            //System.out.println();
            if (!flag) {
                //System.out.println("Backtracking");

                queenPosition[i] = -1;
                for (int k=0;k<size;k++) {
                    for (int l=0;l<size;l++) {
                        board[k][l] = backup[k][l];

                    }
                }
                //printBoard();

                continue;
            }

            boolean succ = PlaceNQUeensFCMRV(queenPosition, N);
             {
                //System.out.println("Failed Return");

                queenPosition[i] = -1;
                for (int k=0;k<size;k++) {
                    for (int l=0;l<size;l++) {
                        board[k][l] = backup[k][l];

                    }
                }
                //printBoard();
            }
        }

        return false;

    }

    public int SelectRowMRV(int [] queenPosition) {
        int [] xcount = new int[size];
        for (int i=0;i<size;i++) {
            if (queenPosition[i] >= 0) continue;
            int rowxcount = 0;
            for (int j=0;j<size;j++) {
                if (board[i][j] == -1) rowxcount++;
            }
            xcount[i] = rowxcount;
        }
        int maxval = -1;
        int maxpos = -1;
        for (int i=0;i<size;i++) {
            if (xcount[i] > maxval) {
                maxval = xcount[i];
                maxpos = i;
            }
        }
        return maxpos;

    }

    public ArrayList<Integer> ValidCells(int row) {
        ArrayList<Integer> validcells = new ArrayList<Integer>();
        for (int i=0;i<size;i++) {
            if (board[row][i] == 0) validcells.add(i);
        }
        return validcells;
    }

    public boolean ForwardCheck(int [] queenPosition, int row, int col) {

        for (int i=0;i<size;i++) {
            if (queenPosition[i] >= 0) continue;
            board[i][col] = -1;
            if (col+row-i <size && col+row-i >= 0) board[i][col+row-i] = -1;
            if (col-(row-i) >= 0 && col-(row-i) < size) board[i][col-(row-i)] = -1;
        }
        //System.out.println(xcount);
        if (!CheckRows()) {
            //queenPosition[row] = -1;
            return false;
        }
        return true;
    }

    // returns false if a row has all X's, true otherwise
    public boolean CheckRows() {
        for (int i=0;i<size;i++) {
            int rowxcount = 0;
            for (int j=0;j<size;j++) {
                if (board[i][j] == -1) rowxcount++;
            }
            if (rowxcount == size) return false;
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
        allSolutions = null;
    }

    public void printBoard() {
        for (int i=0;i<size;i++) {
            for (int j=0;j<size;j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }



    public String toString() {
        return "Backtrack";
    }

}
