import java.io.*;
import java.util.*;

public class ConstraintProgrammingSudokuSolver {

    private static final int SIZE = 9;
    private int[][] board;

    public ConstraintProgrammingSudokuSolver(int[][] input) {
        board = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = input[i][j];
            }
        }
    }

    public boolean solveWithBacktracking() {
        List<String> steps = new ArrayList<>();
        if (backtrackSolve(steps)) {
            writeSolutionToFile("constraint_programming_solution.txt");
            writeStepsToFile(steps, "constraint_programming_steps.txt");
            return true;
        } else {
            return false;
        }
    }

    private boolean backtrackSolve(List<String> steps) {
        int row = -1, col = -1;
        boolean emptyFound = false;

        // Apply Minimum Remaining Values (MRV) heuristic
        List<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == 0) {
                    emptyCells.add(new int[]{i, j});
                }
            }
        }

        if (emptyCells.isEmpty()) return true;

        // Use MRV - choose the cell with the fewest possible values
        emptyCells.sort(Comparator.comparingInt(cell -> countValidValues(cell[0], cell[1])));
        int[] selectedCell = emptyCells.get(0);
        row = selectedCell[0];
        col = selectedCell[1];

        for (int num = 1; num <= 9; num++) {
            if (isValid(row, col, num)) {
                board[row][col] = num;
                steps.add("Step: (" + row + "," + col + ") = " + num + " (Placed)");

                // Apply forward checking - propagate constraints
                if (backtrackSolve(steps)) {
                    return true;
                }

                board[row][col] = 0;
                steps.add("Step: (" + row + "," + col + ") = " + num + " (Backtracked)");
            }
        }
        return false;
    }

    private boolean isValid(int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) {
                return false;
            }
        }
        int startRow = row - row % 3, startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i + startRow][j + startCol] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    private int countValidValues(int row, int col) {
        int count = 0;
        for (int num = 1; num <= 9; num++) {
            if (isValid(row, col, num)) {
                count++;
            }
        }
        return count;
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
