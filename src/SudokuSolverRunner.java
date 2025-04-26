import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SudokuSolverRunner {

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

        // Create solvers for each algorithm
        BacktrackingSudokuSolver backtrackingSolver = new BacktrackingSudokuSolver(puzzle);
        ConstraintProgrammingSudokuSolver constraintSolver = new ConstraintProgrammingSudokuSolver(puzzle);
        RelationResidualSudokuSolver relationSolver = new RelationResidualSudokuSolver(puzzle);
        StochasticSudokuSolver stochasticSolver = new StochasticSudokuSolver(puzzle);

        // Create a thread pool to run all solvers concurrently
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // Submit tasks for each solver with success message and output to file
        executorService.submit(() -> {
            List<String> stepsBacktracking = new ArrayList<>();
            if (backtrackingSolver.solve(stepsBacktracking)) {
                System.out.println("Backtracking Solver Success");
            }
        });

        executorService.submit(() -> {
            if (constraintSolver.solveWithBacktracking()) {
                System.out.println("Constraint Programming Solver Success");
            }
        });

        executorService.submit(() -> {
            if (relationSolver.solve()) {
                System.out.println("Relation Residual Solver Success");
            }
        });

        executorService.submit(() -> {
            if (stochasticSolver.solve()) {
                System.out.println("Stochastic Solver Success");
            }
        });

        // Shutdown the executor after tasks are submitted
        executorService.shutdown();
    }
}
