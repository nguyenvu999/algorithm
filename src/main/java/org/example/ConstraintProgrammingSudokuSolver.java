package org.example;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConstraintProgrammingSudokuSolver {

    private static final int SIZE = 9;
    private final int[][] input;

    public ConstraintProgrammingSudokuSolver(int[][] input) {
        this.input = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(input[i], 0, this.input[i], 0, SIZE);
        }
    }

    public boolean solveWithChoco() {
        Model model = new Model("Sudoku Solver");
        IntVar[][] vars = model.intVarMatrix("board", SIZE, SIZE, 1, 9);

        // Row and column constraints
        for (int i = 0; i < SIZE; i++) {
            model.allDifferent(vars[i]).post();
            IntVar[] column = new IntVar[SIZE];
            for (int j = 0; j < SIZE; j++) {
                column[j] = vars[j][i];
            }
            model.allDifferent(column).post();
        }

        // Subgrid constraints (3x3 boxes)
        for (int boxRow = 0; boxRow < SIZE; boxRow += 3) {
            for (int boxCol = 0; boxCol < SIZE; boxCol += 3) {
                IntVar[] block = new IntVar[SIZE];
                int index = 0;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        block[index++] = vars[boxRow + i][boxCol + j];
                    }
                }
                model.allDifferent(block).post();
            }
        }

        // Initial values
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (input[i][j] != 0) {
                    model.arithm(vars[i][j], "=", input[i][j]).post();
                }
            }
        }

        // Solve and track steps
        Solution solution = model.getSolver().findSolution();
        if (solution != null) {
            int[][] solved = new int[SIZE][SIZE];
            List<String> steps = new ArrayList<>();

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    int value = solution.getIntVal(vars[i][j]);
                    solved[i][j] = value;
                    if (input[i][j] == 0) {
                        steps.add("Step: (" + i + "," + j + ") = " + value + " (Placed by solver)");
                    }
                }
            }

            writeSolutionToFile(solved, "choco_constraint_solution.txt");
            writeStepsToFile(steps, "choco_constraint_steps.txt");
            return true;
        } else {
            System.err.println("No solution found.");
            return false;
        }
    }

    private void writeSolutionToFile(int[][] board, String fileName) {
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
        if (steps == null || steps.isEmpty()) {
            System.err.println("No steps to write.");
            return;
        }
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
