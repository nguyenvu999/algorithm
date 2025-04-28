package org.example;

import java.io.*;
import java.util.*;

public class RelationResidualSudokuSolver {

    private static final int SIZE = 9;
    private int[][] board;

    public RelationResidualSudokuSolver(int[][] input) {
        board = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = input[i][j];
            }
        }
    }

    public boolean solve() {
        List<String> steps = new ArrayList<>();
        if (residualSolve(steps)) {
            writeSolutionToFile("relation_residual_solution.txt");
            writeStepsToFile(steps, "relation_residual_steps.txt");
            return true;
        } else {
            return false;
        }
    }

    private boolean residualSolve(List<String> steps) {
        int row = -1, col = -1;
        boolean emptyFound = false;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == 0) {
                    row = i;
                    col = j;
                    emptyFound = true;
                    break;
                }
            }
            if (emptyFound) break;
        }

        if (!emptyFound) return true;

        // Prioritize placing values that reduce the residual domain of other cells
        for (int num = 1; num <= 9; num++) {
            if (isValid(row, col, num)) {
                board[row][col] = num;
                steps.add("Step: (" + row + "," + col + ") = " + num + " (Placed)");

                // Update residuals
                updateResiduals(row, col);
                if (residualSolve(steps)) {
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

    private void updateResiduals(int row, int col) {
        // Implement logic to update the residuals (possible values) for affected cells
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
