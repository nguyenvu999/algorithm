package org.example;

import java.io.*;
import java.util.*;
public class BacktrackingSudokuSolver {

    private static final int SIZE = 9;
    private int[][] board;

    public BacktrackingSudokuSolver(int[][] input) {
        board = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = input[i][j];
            }
        }
    }

    public boolean solve(List<String> steps) {
        if (backtrackSolve(steps)) {
            writeSolutionToFile("backtracking_solution.txt");  // Writing final board
            writeStepsToFile(steps, "backtracking_steps.txt");  // Writing steps
            return true;
        } else {
            return false;
        }
    }

    // Modified backtracking with randomization and minimum remaining values (MRV)
    private boolean backtrackSolve(List<String> steps) {
        int row = -1, col = -1;
        boolean emptyFound = false;

        // Apply Minimum Remaining Values (MRV) heuristic - search for the cell with the fewest possibilities
        List<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == 0) {
                    emptyCells.add(new int[]{i, j});
                }
            }
        }

        // If no empty cells, we've solved the puzzle
        if (emptyCells.isEmpty()) return true;

        // Randomize cell order (simulating a non-deterministic approach)
        Collections.shuffle(emptyCells);
        int[] selectedCell = emptyCells.get(0);
        row = selectedCell[0];
        col = selectedCell[1];

        // Try numbers in a randomized order
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Collections.shuffle(numbers);

        for (int num : numbers) {
            if (isValid(row, col, num)) {
                board[row][col] = num;
                steps.add("Step: (" + row + "," + col + ") = " + num + " (Placed)");
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

    // Main method for testing the Backtracking solver
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

        BacktrackingSudokuSolver solver = new BacktrackingSudokuSolver(puzzle);
        List<String> steps = new ArrayList<>();
        if (solver.solve(steps)) {
            System.out.println("Sudoku solved successfully!");
        } else {
            System.out.println("No solution found.");
        }
    }
}
