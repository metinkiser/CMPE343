package oop;

import java.util.Scanner;
public class StatisticalInformation {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in); 
        boolean running = true;

        while (running) {
            System.out.println("Main Menu");
            System.out.println("[A] Statistical Information");
            System.out.println("[E] Exit");
            System.out.print("Make your choice (A/E): ");
            String choice = input.nextLine();

            switch (choice.toUpperCase()) {
                case "A":
                    displayStatistics(input);
                    break;
                case "E":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        input.close();
    }

    public static void displayStatistics(Scanner input) {
        clearConsole(); // Clear the console
        System.out.println("Statistical Information"); 

        try {
            System.out.print("Enter the array size: ");
            int size = input.nextInt();
            double[] numbers = new double[size]; 

            System.out.println("Enter the array elements:");
            for (int i = 0; i < size; i++) { // Loop through based on the array size
                numbers[i] = input.nextDouble();
            }

            // Statistical calculations
            double mean = calculateMean(numbers);
            double median = calculateMedian(numbers);
            double geometricMean = calculateGeometricMean(numbers);
            double harmonicMean = calculateHarmonicMean(numbers, size);

            // Print the results
            System.out.printf("Arithmetic Mean: %.2f%n", mean);
            System.out.printf("Median: %.2f%n", median);
            System.out.printf("Geometric Mean: %.2f%n", geometricMean);
            System.out.printf("Harmonic Mean: %.2f%n", harmonicMean);

        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
            input.next(); // Clear the invalid input
        } finally {
            input.nextLine(); // Clear the remaining new line for the next input
        }
    }

    private static void clearConsole() {
        // Code to clear the console
        try {
            System.out.print("\033[H\033[2J"); // Clear the console
            System.out.flush(); // Refresh the console
            Thread.sleep(50); // Pause briefly to ensure the console is fully cleared
        } catch (Exception e) {
            System.out.println("An error occurred while clearing the console: " + e.getMessage());
        }
    }

    public static double calculateMean(double[] numbers) {
        double sum = 0;
        for (double num : numbers) {
            sum += num;
        }
        return sum / numbers.length;
    }

    public static double calculateMedian(double[] numbers) {
        java.util.Arrays.sort(numbers); // Sort the array
        int middle = numbers.length / 2;
        if (numbers.length % 2 == 0) { // If the number of elements is even
            return (numbers[middle - 1] + numbers[middle]) / 2.0;
        } else { // If odd
            return numbers[middle];
        }
    }

    public static double calculateGeometricMean(double[] numbers) {
        double product = 1;
        for (double num : numbers) {
            product *= num;
        }
        return Math.pow(product, 1.0 / numbers.length);
    }

    public static double calculateHarmonicMean(double[] numbers, int size) {
        double sumOfInverses = harmonicMeanRecursive(numbers, size, 0);
        return size / sumOfInverses; // Calculate harmonic mean
    }

    private static double harmonicMeanRecursive(double[] numbers, int size, int index) {
        if (index >= size) {
            return 0;
        }
        return 1.0 / numbers[index] + harmonicMeanRecursive(numbers, size, index + 1);
    }
}
