import java.io.*;
import java.util.*;

public class RelationResidualSudokuSolver {

    private static final int SIZE = 9;
    private int[][] board;
    private Map<String, Set<Integer>> residuals;

    public RelationResidualSudokuSolver(int[][] input) {
        board = new int[SIZE][SIZE];
        residuals = new HashMap<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = input[i][j];
            }
        }
        initializeResiduals();
    }

    private void initializeResiduals() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    Set<Integer> candidates = getCandidates(row, col);
                    residuals.put(row + "," + col, candidates);
                }
            }
        }
    }

    public boolean solve() {
        List<String> steps = new ArrayList<>();
        if (residualSolve(steps)) {
            writeSolutionToFile("E:\\group soduku\\untitled\\src\\relation_residual_solution.txt");
            writeStepsToFile(steps, "E:\\group soduku\\untitled\\src\\relation_residual_steps.txt");
            return true;
        } else {
            return false;
        }
    }

    private boolean residualSolve(List<String> steps) {
        String nextCell = getCellWithFewestCandidates();
        if (nextCell == null) return true; // no empty cell left

        String[] parts = nextCell.split(",");
        int row = Integer.parseInt(parts[0]);
        int col = Integer.parseInt(parts[1]);
        Set<Integer> candidates = new HashSet<>(residuals.get(nextCell));

        for (int num : candidates) {
            if (isValid(row, col, num)) {
                board[row][col] = num;
                steps.add("Step: (" + row + "," + col + ") = " + num + " (Placed)");

                // Backup current residuals
                Map<String, Set<Integer>> backup = cloneResiduals();
                updateResiduals(row, col, num);

                if (residualSolve(steps)) {
                    return true;
                }

                board[row][col] = 0;
                steps.add("Step: (" + row + "," + col + ") = " + num + " (Backtracked)");
                residuals = backup;
            }
        }
        return false;
    }

    private String getCellWithFewestCandidates() {
        String minKey = null;
        int minSize = Integer.MAX_VALUE;
        for (Map.Entry<String, Set<Integer>> entry : residuals.entrySet()) {
            int size = entry.getValue().size();
            if (size < minSize && board[Integer.parseInt(entry.getKey().split(",")[0])][Integer.parseInt(entry.getKey().split(",")[1])] == 0) {
                minSize = size;
                minKey = entry.getKey();
            }
        }
        return minKey;
    }

    private Map<String, Set<Integer>> cloneResiduals() {
        Map<String, Set<Integer>> copy = new HashMap<>();
        for (Map.Entry<String, Set<Integer>> entry : residuals.entrySet()) {
            copy.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
        return copy;
    }

    private Set<Integer> getCandidates(int row, int col) {
        Set<Integer> candidates = new HashSet<>();
        for (int i = 1; i <= 9; i++) candidates.add(i);

        for (int i = 0; i < SIZE; i++) {
            candidates.remove(board[row][i]);
            candidates.remove(board[i][col]);
        }
        int startRow = row - row % 3, startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                candidates.remove(board[startRow + i][startCol + j]);
            }
        }
        return candidates;
    }

    private void updateResiduals(int row, int col, int value) {
        board[row][col] = value;
        residuals.remove(row + "," + col);
        for (int i = 0; i < SIZE; i++) {
            removeCandidate(row, i, value);
            removeCandidate(i, col, value);
        }
        int startRow = row - row % 3, startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                removeCandidate(startRow + i, startCol + j, value);
            }
        }
    }

    private void removeCandidate(int row, int col, int value) {
        String key = row + "," + col;
        if (residuals.containsKey(key)) {
            residuals.get(key).remove(value);
        }
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
}
