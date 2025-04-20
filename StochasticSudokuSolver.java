import java.util.*;

public class StochasticSudokuSolver {

    private static final int SIZE = 9; // Size of the Sudoku board (9x9)
    private static final int BLOCK = 3; // Size of each block (3x3)
    private static final int MAX_STEPS = 360000; // Maximum number of steps

    private int[][] board;
    private boolean[][] fixed;
    private int stepCounter = 0;

    // Constructor to initialize the board
    public StochasticSudokuSolver(int[][] input) {
        board = new int[SIZE][SIZE];
        fixed = new boolean[SIZE][SIZE];

        // Copy input and mark fixed cells
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = input[i][j];
                fixed[i][j] = input[i][j] != 0;
            }
        }
    }

    // Method to fill random numbers in each block
    public void fillRandomInBlocks() {
        Random rand = new Random();

        for (int blockRow = 0; blockRow < BLOCK; blockRow++) {
            for (int blockCol = 0; blockCol < BLOCK; blockCol++) {
                List<Integer> nums = new ArrayList<>();
                boolean[] used = new boolean[SIZE + 1];

                // Collect used numbers
                for (int i = 0; i < BLOCK; i++) {
                    for (int j = 0; j < BLOCK; j++) {
                        int row = blockRow * BLOCK + i;
                        int col = blockCol * BLOCK + j;
                        if (fixed[row][col]) {
                            used[board[row][col]] = true;
                        }
                    }
                }

                // Fill the remaining numbers
                for (int n = 1; n <= SIZE; n++) {
                    if (!used[n]) nums.add(n);
                }
                Collections.shuffle(nums);

                // Assign to non-fixed cells
                for (int i = 0; i < BLOCK; i++) {
                    for (int j = 0; j < BLOCK; j++) {
                        int row = blockRow * BLOCK + i;
                        int col = blockCol * BLOCK + j;
                        if (!fixed[row][col]) {
                            board[row][col] = nums.remove(0);
                        }
                    }
                }
            }
        }
    }

    // Method to compute the number of conflicts in the current board
    public int computeConflicts() {
        int conflicts = 0;

        // Check for conflicts in rows and columns
        for (int i = 0; i < SIZE; i++) {
            Set<Integer> rowSet = new HashSet<>();
            Set<Integer> colSet = new HashSet<>();
            for (int j = 0; j < SIZE; j++) {
                rowSet.add(board[i][j]);
                colSet.add(board[j][i]);
            }
            conflicts += (SIZE - rowSet.size()) + (SIZE - colSet.size());
        }

        return conflicts;
    }

    // Main solving method using stochastic approach
    public void solve() {
        fillRandomInBlocks(); // Fill the board with random numbers
        int conflicts = computeConflicts();
        Random rand = new Random();

        // Loop until 0 conflicts or maximum steps reached
        while (conflicts > 0 && stepCounter < MAX_STEPS) {
            int blockRow = rand.nextInt(3);
            int blockCol = rand.nextInt(3);

            // Get swappable cells
            List<int[]> swappables = new ArrayList<>();
            for (int i = 0; i < BLOCK; i++) {
                for (int j = 0; j < BLOCK; j++) {
                    int r = blockRow * BLOCK + i;
                    int c = blockCol * BLOCK + j;
                    if (!fixed[r][c]) {
                        swappables.add(new int[]{r, c});
                    }
                }
            }

            if (swappables.size() < 2) {
                stepCounter++;
                continue;
            }

            int[] cell1 = swappables.get(rand.nextInt(swappables.size()));
            int[] cell2 = swappables.get(rand.nextInt(swappables.size()));
            while (Arrays.equals(cell1, cell2)) {
                cell2 = swappables.get(rand.nextInt(swappables.size()));
            }

            // Swap
            swap(cell1, cell2);
            int newConflicts = computeConflicts();

            if (newConflicts <= conflicts || rand.nextDouble() < 0.01) {
                conflicts = newConflicts;
            } else {
                swap(cell1, cell2); // undo swap
            }

            stepCounter++;
        }

        // Print the result
        System.out.println("Total steps: " + stepCounter);
        if (conflicts == 0) {
            System.out.println("Sudoku solved!");
        } else {
            System.out.println("Stopped with remaining conflicts: " + conflicts);
        }

        printBoard();
    }

    // Helper method to swap two cells
    private void swap(int[] a, int[] b) {
        int temp = board[a[0]][a[1]];
        board[a[0]][a[1]] = board[b[0]][b[1]];
        board[b[0]][b[1]] = temp;
    }

    // Print the sudoku board
    public void printBoard() {
        for (int i = 0; i < SIZE; i++) {
            if (i % 3 == 0 && i != 0) System.out.println("------+-------+------");
            for (int j = 0; j < SIZE; j++) {
                if (j % 3 == 0 && j != 0) System.out.print("| ");
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Main method to run
    public static void main(String[] args) {
        int[][] puzzle = {
                {4, 0, 0, 1, 7, 5, 0, 0, 2},
                {2, 0, 0, 3, 6, 9, 7, 0, 0},
                {0, 0, 0, 0, 0, 8, 0, 9, 0},
                {5, 3, 1, 0, 0, 7, 9, 0, 4},
                {8, 2, 0, 0, 9, 4, 6, 0, 0},
                {0, 0, 0, 0, 0, 3, 0, 0, 0},
                {0, 6, 0, 0, 0, 0, 3, 0, 0},
                {0, 0, 0, 0, 3, 0, 0, 0, 0},
                {0, 0, 4, 0, 0, 0, 0, 6, 1}
        };

        StochasticSudokuSolver solver = new StochasticSudokuSolver(puzzle);
        solver.solve();
    }
}
