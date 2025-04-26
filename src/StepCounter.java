import java.io.*;
import java.util.*;

public class StepCounter {

    // Method to count the number of steps in a given file
    public static int countStepsInFile(String filePath) {
        int steps = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Each line represents a step
                steps++;
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + filePath + ": " + e.getMessage());
        }

        return steps;
    }
}
