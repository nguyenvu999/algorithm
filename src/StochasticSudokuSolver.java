import java.io.*;
import java.util.*;

public class StochasticSudokuSolver {

    private static final int SIZE = 9;
    private int[][] board;
    private boolean[][] fixed;
    private int stepCounter = 0;
    private static final int MAX_STEPS = 360000;

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

    public boolean solve() {
        List<String> steps = new ArrayList<>();
        fillRandomInBlocks(); // Fill the board with random numbers
        int conflicts = computeConflicts();
        Random rand = new Random();

        // Loop until 0 conflicts or maximum steps reached
        while (conflicts > 0 && stepCounter < MAX_STEPS) {
            int blockRow = rand.nextInt(3);
            int blockCol = rand.nextInt(3);

            // Get swappable cells
            List<int[]> swappables = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    int r = blockRow * 3 + i;
                    int c = blockCol * 3 + j;
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

            // Swap cells
            swap(cell1, cell2);
            int newConflicts = computeConflicts();

            if (newConflicts <= conflicts || rand.nextDouble() < 0.01) {
                conflicts = newConflicts;
                steps.add("Step: Swapped (" + cell1[0] + "," + cell1[1] + ") with (" + cell2[0] + "," + cell2[1] + ")");
            } else {
                swap(cell1, cell2); // Undo swap
            }

            stepCounter++;
        }

        // Print the result and write steps to file
        if (conflicts == 0) {
            System.out.println("Stochastic Solver Success");
            writeSolutionToFile("stochastic_solution.txt"); // Write final solution to file
            writeStepsToFile(steps, "stochastic_steps.txt"); // Write steps to file
            printBoard();
            return true;
        } else {
            System.out.println("Stopped with remaining conflicts: " + conflicts);
            return false;
        }
    }

    private void swap(int[] a, int[] b) {
        int temp = board[a[0]][a[1]];
        board[a[0]][a[1]] = board[b[0]][b[1]];
        board[b[0]][b[1]] = temp;
    }

    private void fillRandomInBlocks() {
        Random rand = new Random();

        for (int blockRow = 0; blockRow < 3; blockRow++) {
            for (int blockCol = 0; blockCol < 3; blockCol++) {
                List<Integer> nums = new ArrayList<>();
                boolean[] used = new boolean[SIZE + 1];

                // Collect used numbers in the block
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        int row = blockRow * 3 + i;
                        int col = blockCol * 3 + j;
                        if (fixed[row][col]) {
                            used[board[row][col]] = true;
                        }
                    }
                }

                // Fill remaining numbers
                for (int n = 1; n <= SIZE; n++) {
                    if (!used[n]) nums.add(n);
                }
                Collections.shuffle(nums);

                // Assign to non-fixed cells
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        int row = blockRow * 3 + i;
                        int col = blockCol * 3 + j;
                        if (!fixed[row][col]) {
                            board[row][col] = nums.remove(0);
                        }
                    }
                }
            }
        }
    }

    private int computeConflicts() {
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

    private void printBoard() {
        System.out.println("Sudoku Solution:");
        for (int i = 0; i < SIZE; i++) {
            if (i % 3 == 0 && i != 0) {
                System.out.println("------+-------+------");
            }
            for (int j = 0; j < SIZE; j++) {
                if (j % 3 == 0 && j != 0) {
                    System.out.print("| ");
                }
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private void writeSolutionToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    writer.write(board[i][j] + " ");
                    if (j == 2 || j == 5) writer.write("| ");
                }
                writer.newLine();
                if (i == 2 || i == 5) {
                    writer.write("------+-------+------");
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing solution to file: " + e.getMessage());
        }
    }

    private void writeStepsToFile(List<String> steps, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String step : steps) {
                writer.write(step);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing steps to file: " + e.getMessage());
        }
    }
}
