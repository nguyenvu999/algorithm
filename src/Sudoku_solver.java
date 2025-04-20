package org.example;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

public class Sudoku_Solver {

    public int[][] solve(int[][] puzzle) {
        Model model = new Model("RMIT Sudoku Solver");

        IntVar[][] vars = new IntVar[9][9];

        // Initialize variables with domains based on puzzle values
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (puzzle[i][j] == 0) {
                    vars[i][j] = model.intVar("cell_" + i + "_" + j, 1, 9);
                } else {
                    vars[i][j] = model.intVar("cell_" + i + "_" + j, puzzle[i][j]);
                }
            }
        }

        // Add row and column constraints
        for (int i = 0; i < 9; i++) {
            IntVar[] row = new IntVar[9];
            IntVar[] col = new IntVar[9];
            for (int j = 0; j < 9; j++) {
                row[j] = vars[i][j];
                col[j] = vars[j][i];
            }
            model.allDifferent(row).post();
            model.allDifferent(col).post();
        }

        // Add 3x3 block constraints
        for (int row = 0; row < 9; row += 3) {
            for (int col = 0; col < 9; col += 3) {
                IntVar[] block = new IntVar[9];
                int idx = 0;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        block[idx++] = vars[row + i][col + j];
                    }
                }
                model.allDifferent(block).post();
            }
        }

        // Solve the puzzle
        Solver solver = model.getSolver();

        if (solver.solve()) {
            int[][] solution = new int[9][9];
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    solution[i][j] = vars[i][j].getValue();
                }
            }
            return solution;
        } else {
            throw new IllegalArgumentException("No solution found for the given puzzle.");
        }
    }
}
