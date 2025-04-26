import java.io.*;
import java.util.*;

public class BestSudokuSolver {

    public static void main(String[] args) {
        // File paths for the step files of each solving method
        String[] files = {
                "C:\\Users\\Admin\\IdeaProjects\\sudoku\\relation_residual_steps.txt",
                "C:\\Users\\Admin\\IdeaProjects\\sudoku\\backtracking_steps.txt",
                "C:\\Users\\Admin\\IdeaProjects\\sudoku\\constraint_programming_steps.txt",
                "C:\\Users\\Admin\\IdeaProjects\\sudoku\\stochastic_steps.txt"
        };

        // Create a map to store the number of steps for each method
        Map<String, Integer> stepsCount = new HashMap<>();

        // Using StepCounter to count steps for each method
        for (String file : files) {
            int stepCount = StepCounter.countStepsInFile(file);
            stepsCount.put(file, stepCount);
        }

        // Find the method with the fewest steps
        String bestMethod = getBestMethod(stepsCount);

        // Write the best solution information to a file
        writeBestSolutionToFile(bestMethod);
    }

    // Method to get the best method with the fewest steps
    private static String getBestMethod(Map<String, Integer> stepsCount) {
        String bestMethod = null;
        int minSteps = Integer.MAX_VALUE;

        // Find the method with the fewest steps
        for (Map.Entry<String, Integer> entry : stepsCount.entrySet()) {
            if (entry.getValue() < minSteps) {
                minSteps = entry.getValue();
                bestMethod = entry.getKey();
            }
        }

        return bestMethod;
    }

    // Method to write the best solution to a file
    private static void writeBestSolutionToFile(String bestMethod) {
        // Extract the method name from the file path
        String bestMethodName = bestMethod.substring(bestMethod.lastIndexOf("\\") + 1, bestMethod.lastIndexOf("_steps"));
        String message = "The best solution for this Sudoku is: " + bestMethodName;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Admin\\IdeaProjects\\sudoku\\best_solution.txt"))) {
            // Write the best method name to a file
            writer.write(message);
            System.out.println(message);
        } catch (IOException e) {
            System.err.println("Error writing the best solution to file: " + e.getMessage());
        }
    }
}
