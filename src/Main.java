public class Main {
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

        RMIT_Sudoku_Solver solver = new RMIT_Sudoku_Solver();

        long startTime = System.nanoTime();  // Start timing
        int[][] solved = solver.solve(puzzle);
        long endTime = System.nanoTime();    // End timing

        long duration = endTime - startTime;
        double milliseconds = duration / 1_000_000.0;

        System.out.println("Solved Sudoku:");
        for (int[] row : solved) {
            for (int num : row) {
                System.out.print(num + " ");
            }
            System.out.println();
        }

        System.out.printf("Time taken: %.3f ms%n", milliseconds);
    }
}

