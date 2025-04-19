public class SudokuSolver {


    public static boolean isValid(int[][] board, int row, int col, int num) {

        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num) {
                return false;
            }
        }


        for (int i = 0; i < 9; i++) {
            if (board[i][col] == num) {
                return false;
            }
        }


        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (board[i][j] == num) {
                    return false;
                }
            }
        }

        return true;
    }


    public static boolean solveSudoku(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            if (solveSudoku(board)) {
                                return true;
                            }
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    // In bảng Sudoku
    public static void printBoard(int[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int[][] board = {
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

        if (solveSudoku(board)) {
            printBoard(board);
        } else {
            System.out.println("Không thể giải Sudoku!");
        }
    }
}
