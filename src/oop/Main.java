package cmpe343_oop;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {
	
	/**
	 * Main method to start the program and display the main menu.
	 *
	 * @param args command-line arguments
	 */
    public static void main(String[] args) {
    	
        // ASCII 
        System.out.println("█     █  █▀▀  █     ▄▀▀▀   ▄▀▀▀▄   █▀▄ ▄▀█  █▀▀  █");
        System.out.println("█  ▄  █  █■■  █    ■      ■     ■  █  ▀  █  █■■  █");
        System.out.println("█▄▀ ▀▄█  █▄▄  █▄▄   ▀▄▄▄   ▀▄▄▄▀   █     █  █▄▄  ▄ ");
        System.out.println("                                                   ");
        System.out.println("                                                   ");
        System.out.println("▀▀█▀▀  █▀▀   ▄▀▀▄   █▀▄ ▄▀█    █▀▄ ▄▀█  █▀▀  █▀▄ ▄▀█  █▀▀▀▄  █▀▀  █▀▀▀▄   ▄▀▀▀  ■");
        System.out.println("  █    █■■  █■■■■█  █  ▀  █    █  ▀  █  █■■  █  ▀  █  █■■■   █■■  █■■█     ▀▄    ");
        System.out.println("  █    █▄▄  █    █  █     █    █     █  █▄▄  █     █  █▄▄▄▀  █▄▄  █  ▀▄   ▄▄▄▀  ■");
        System.out.println("                                                                                 ");
        System.out.println("█▀▀▀▄   ▄▀▀▄   █▀▀▀▄  ▀   ▄▀▀▄   █▀▄  █  █    █  █▀▀▀▄      ▄▀▀▄   ▀▄ ▄▀   ▄▀▀▀▀  █ ▀ ▀ █  █▀▄  █    ▄▀▀ █   ▀▀▄");
        System.out.println("█■■█   █■■■■█  █■■■   █  █■■■■█  █  ▀▄█  █    █  █■■█      █■■■■█    █    ■   ■■  █     █  █  ▀▄█    ■   █     ■");
        System.out.println("█  ▀▄  █    █  █▄▄▄▀  █  █    █  █    █  █▄▄▄▄█  █   ▀▄    █    █    █     ▀▄▄▄▀   ▀▄▄▄▀   █    █    ▀▄▄ █▄▄ ▄▄▀");
        System.out.println("                                                                                                                ");
        System.out.println(" ▄▀▀▄   █▄■▀  █  █  █▀▄ ▄▀█  █▀▀  █▀▀▄    █▀▄  █   ▄▀▀▄   ▀▀▀▀█▀   ▄▀▀▄   █▀▀▀▄   ▄▀▀▀▄   ▀▄   ▄▀");
        System.out.println("█■■■■█  █■▄   █■■█  █  ▀  █  █■■  █  █    █  ▀▄█  █■■■■█    ▄▀    █■■■■█  █■■█   ■     ■   ▀▄ ▄▀ ");
        System.out.println("█    █  █  █  █  █  █     █  █▄▄  █▄▄▀    █    █  █    █  ▄█▄▄▄▄  █    █  █  ▀▄   ▀▄▄▄▀      █   ");
        System.out.println("                                                                                                 ");
        System.out.println("█▀▄ ▄▀█  █▀▀  █  █  █▀▄ ▄▀█  █▀▀  ▀▀█▀▀    █▀▀▄   ▄▀▀▀▄   █▀▀▀▄  █    █  █▄■▀    █▀▀▄  ▀  █▀▄  █   ▄▀▀▀  ▀▀█▀▀  █ ▀ ▀ █  █▀▀▀▄  █▄■▀");
        System.out.println("█  ▀  █  █■■  █■■█  █  ▀  █  █■■    █      █  █  ■     ■  █■■█   █    █  █■▄     █  █  █  █  ▀▄█  ■        █    █     █  █■■█   █■▄");
        System.out.println("█     █  █▄▄  █  █  █     █  █▄▄    █      █▄▄▀   ▀▄▄▄▀   █  ▀▄  █▄▄▄▄█  █  █    █▄▄▀  █  █    █   ▀▄▄▄    █     ▀▄▄▄▀   █  ▀▄  █  █");
        System.out.println("                                                                                                     ▀                              ");
        System.out.println("█▀▄ ▄▀█  █▀▀  ▀▀█▀▀  ▀  █▀▄  █     ▄▀▀▀   ▄▀▀▄   █▀▄  █    █▄■▀  ▀  ▄▀▀▀  █▀▀  █▀▀▀▄");
        System.out.println("█  ▀  █  █■■    █    █  █  ▀▄█    ■      █■■■■█  █  ▀▄█    █■▄   █   ▀▄   █■■  █■■█ ");
        System.out.println("█     █  █▄▄    █    █  █    █     ▀▄▄▄  █    █  █    █    █  █  █  ▄▄▄▀  █▄▄  █  ▀▄");
        System.out.println("                                                                                                                                     ▄ ▄                ");
        System.out.println("█▀▄ ▄▀█   █     █  █  █   ▄▀▀▄   █▀▄ ▄▀█  █▀▄ ▄▀█  █▀▀  ▀▀█▀▀    █▀▀  █▀▄ ▄▀█  ▀  █▀▀▀▄     ▄▀▀▀   █ ▀ ▀ █  █▀▄  █  █▀▀▄   ▄▀▀▀▄    ▄▀▀▀▀   █▀▀▄  █    █");
        System.out.println("█  ▀  █   █     █  █■■█  █■■■■█  █  ▀  █  █  ▀  █  █■■    █      █■■  █  ▀  █  █  █■■█     ■   ■■  █     █  █  ▀▄█  █  █  ■     ■  ■   ■■   █  █  █    █");
        System.out.println("█     █    ▀▄▄▄▀   █  █  █    █  █     █  █     █  █▄▄    █      █▄▄  █     █  █  █  ▀▄     ▀▄▄▄▀   ▀▄▄▄▀   █    █  █▄▄▀   ▀▄▄▄▀    ▀▄▄▄▀   █▄▄▀  █▄▄▄▄█");
        System.out.println("                                                                                                                                                        ");

       
            Scanner input = new Scanner(System.in);
            boolean running = true;

            while (running) {
                clearConsole(); 
                System.out.println("\nMain Menu");
                System.out.println("[A] Statistical Information");
                System.out.println("[B] Matrix Operations");
                System.out.println("[C] Text Encryption/Decryption");
                System.out.println("[D] Tic-Tac-Toe");
                System.out.println("[E] Exit");
                System.out.print("Make your choice (A/B/C/D/E): ");
                String choice = input.nextLine();

                switch (choice.toUpperCase()) {
                    case "A":
                        clearConsole();
                        displayStatistics(input); // Call statistical information
                        break;
                    case "B":
                        clearConsole();
                        runMatrixOperations(input); // Call matrix operations
                        break;
                    case "C":
                        clearConsole();
                        runEncryptionDecryption(input); // Call encryption/decryption functionality
                        break;
                    case "D":
                        clearConsole();
                        runTicTacToe(input); // Call Tic-Tac-Toe functionality
                        break;
                    case "E":
                        System.out.println("Exiting the program...");
                        input.close(); // Close the scanner
                        System.exit(0); // Exit the program
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }

    /**
     * Clears the console screen.
     */
        private static void clearConsole() {
            try {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            } catch (Exception e) {
                System.out.println("An error occurred while clearing the console: " + e.getMessage());
            }
        }
  //Statistical Information
        
        
        /**
         * Displays statistical information (mean, median, etc.) for an array of numbers.
         *
         * @param input the Scanner object for user input
         */
      public static void displayStatistics(Scanner input) {
            clearConsole();
            System.out.println("Statistical Information");

            while (true) {
                try {
                    System.out.print("Enter the array size: ");
                    int size = input.nextInt();
                    double[] numbers = new double[size];

                    System.out.println("Enter the array elements:");
                    for (int i = 0; i < size; i++) {
                        numbers[i] = input.nextDouble();
                    }

                    double mean = calculateMean(numbers);
                    double median = calculateMedian(numbers);

                    boolean hasZeroOrNegative = false;
                    boolean hasNegative = false;
                    for (double num : numbers) {
                        if (num <= 0) {
                            hasZeroOrNegative = true;
                        }
                        if (num < 0) {
                            hasNegative = true;
                        }
                        if (hasZeroOrNegative) {
                            break;
                        }
                    }

                    if (hasZeroOrNegative) {
                        System.out.println("Harmonic Mean cannot be calculated for arrays containing zero or negative values.");
                    } else {
                        double harmonicMean = calculateHarmonicMean(numbers, size);
                        System.out.printf("Harmonic Mean: %.2f%n", harmonicMean);
                    }

                    if (hasZeroOrNegative || hasNegative) {
                        System.out.println("Geometric Mean cannot be calculated for arrays containing zero or negative values.");
                    } else {
                        double geometricMean = calculateGeometricMean(numbers);
                        System.out.printf("Geometric Mean: %.2f%n", geometricMean);
                    }

                    // displaying results
                    System.out.printf("Arithmetic Mean: %.2f%n", mean);
                    System.out.printf("Median: %.2f%n", median);
                    break; // Exit the loop when operations are completed

                } catch (Exception e) {
                    System.out.println("Invalid input. Please try again."); // Incorrect input warning
                    input.nextLine(); // Clear the bad input
                }
            }

            System.out.println("Press Enter to return to Main Menu...");
            input.nextLine(); // Wait for the user to press Enter
        }


      /**
       * Calculates the arithmetic mean of an array of numbers.
       *
       * @param numbers an array of double values
       * @return the arithmetic mean of the numbers
       */
    public static double calculateMean(double[] numbers) {
        double sum = 0;
        for (double num : numbers) {
            sum += num;
        }
        return sum / numbers.length;
    }

    

/**
 * Calculates the median of an array of numbers.
 *
 * @param numbers an array of double values
 * @return the median of the numbers
 */
    public static double calculateMedian(double[] numbers) {
        java.util.Arrays.sort(numbers);
        int middle = numbers.length / 2;
        if (numbers.length % 2 == 0) {
            return (numbers[middle - 1] + numbers[middle]) / 2.0;
        } else {
            return numbers[middle];
        }
    }

    /**
     * Calculates the geometric mean of an array of numbers.
     *
     * @param numbers an array of double values
     * @return the geometric mean of the numbers
     */
    public static double calculateGeometricMean(double[] numbers) {
        double product = 1;
        for (double num : numbers) {
            product *= num;
        }
        return Math.pow(product, 1.0 / numbers.length);
    }

    
    /**
     * Calculates the harmonic mean of an array of numbers using recursion.
     *
     * @param numbers an array of double values
     * @param size the number of elements in the array
     * @return the harmonic mean of the numbers
     */
    public static double calculateHarmonicMean(double[] numbers, int size) {
        double sumOfInverses = harmonicMeanRecursive(numbers, size, 0);
        return size / sumOfInverses;
    }

    private static double harmonicMeanRecursive(double[] numbers, int size, int index) {
        if (index >= size) {
            return 0;
        }
        return 1.0 / numbers[index] + harmonicMeanRecursive(numbers, size, index + 1);
        
    }
    

    
    //MatrixOperations
 
    /**
     * Runs the matrix operations menu and allows the user to select different matrix functions.
     *
     * @param input the Scanner object for user input
     */
    
    public static void runMatrixOperations(Scanner input) {
        int choice;
        double[][] matrix1 = null;
        double[][] matrix2 = null;
        double[][] result = null;

        do {
            displayMenu(); 
            choice = getValidInteger(input); 

            switch (choice) {
                case 1:
                    System.out.println("\nMATRIX MULTIPLICATION\n");
                    matrix1 = inputMatrix(input);
                    matrix2 = inputMatrix(input);
                    result = multiplyMatrices(matrix1, matrix2);
                    if (result != null) printMatrix(result);
                    break;

                case 2:
                    System.out.println("\nELEMENT-WISE MULTIPLICATION\n");
                    matrix1 = inputMatrix(input);
                    matrix2 = inputMatrix(input);
                    result = elementWiseMultiply(matrix1, matrix2);
                    if (result != null) printMatrix(result);
                    break;

                case 3:
                    System.out.println("\nTRANSPOSE OF THE MATRIX\n");
                    matrix1 = inputMatrix(input);
                    result = transposeMatrix(matrix1);
                    printMatrix(result);
                    break;

                case 4:
                    System.out.println("\nINVERSE OF THE MATRIX\n");
                    matrix1 = inputSquareMatrix(input); 
                    result = findInverse(matrix1);
                    if (result != null) printMatrix(result);
                    break;

                case 5:
                    System.out.println("Returning to main menu...");
                    return; 
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }

        } while (true); 
    }

    
    /**
     * Takes user input to fill a square matrix.
     *
     * @param input the Scanner object for user input
     * @return a square matrix filled with user input
     */
    public static double[][] inputSquareMatrix(Scanner input) {
        int size;

        System.out.print("Enter the size of the square matrix (rows = columns): ");
        size = getValidInteger(input); 

        double[][] matrix = new double[size][size];

        System.out.println("Enter elements of the matrix:");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print("Matrix[" + i + "][" + j + "]: ");
                matrix[i][j] = getValidDouble(input); 
            }
        }
        return matrix;
    }

    
    /**
     * Prompts the user for a valid integer input.
     *
     * @param input the Scanner object for user input
     * @return a valid integer input
     */
    public static int getValidInteger(Scanner input) {
        while (true) {
            try {
                return input.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter a valid integer.");
                input.next(); // Hatalı girişi temizle
            }
        }
    }

    /**
     * Takes user input to fill a matrix.
     *
     * @param input the Scanner object for user input
     * @return a matrix filled with user input
     */
    public static double[][] inputMatrix(Scanner input) {
        int rows, columns;

        System.out.print("Enter the number of rows: ");
        rows = getValidInteger(input);

        System.out.print("Enter the number of columns: ");
        columns = getValidInteger(input);

        double[][] matrix = new double[rows][columns];

        System.out.println("Enter elements of the matrix:");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print("Matrix[" + i + "][" + j + "]: ");
                matrix[i][j] = getValidDouble(input); // Geçerli double giriş
            }
        }
        return matrix;
    }

    
    
    /**
     * Prompts the user for a valid double input.
     *
     * @param input the Scanner object for user input
     * @return a valid double input
     */
    public static double getValidDouble(Scanner input) {
        while (true) {
            try {
                return input.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter a valid number.");
                input.next(); // Hatalı girişi temizle
            }
        }
    }


    
    /**
     * Multiplies two matrices and returns the result.
     *
     * @param matrix1 the first matrix
     * @param matrix2 the second matrix
     * @return the result of matrix multiplication
     */
    public static double[][] multiplyMatrices(double[][] matrix1, double[][] matrix2) {
        int numRows1 = matrix1.length;
        int numCols1 = matrix1[0].length;
        int numRows2 = matrix2.length;
        int numCols2 = matrix2[0].length;

        if (numCols1 != numRows2) {
            System.out.println("Matrix dimensions are not compatible for multiplication.");
            return null;
        }

        double[][] result = new double[numRows1][numCols2];

        for (int i = 0; i < numRows1; i++) {
            for (int j = 0; j < numCols2; j++) {
                double sum = 0;
                for (int k = 0; k < numCols1; k++) {
                    sum += matrix1[i][k] * matrix2[k][j];
                }
                result[i][j] = sum;
            }
        }
        return result;
    }

    
    /**
     * Multiplies two matrices element-wise and returns the result.
     *
     * @param matrix1 the first matrix
     * @param matrix2 the second matrix
     * @return the result of element-wise matrix multiplication
     */
    public static double[][] elementWiseMultiply(double[][] matrix1, double[][] matrix2) {
        int numRows = matrix1.length;
        int numCols = matrix1[0].length;

        if (matrix1.length != matrix2.length || matrix1[0].length != matrix2[0].length) {
            System.out.println("Matrix dimensions do not match for element-wise multiplication.");
            return null;
        }

        double[][] result = new double[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                result[i][j] = matrix1[i][j] * matrix2[i][j];
            }
        }

        return result;
    }

 
    
    /**
     * Computes the transpose of a matrix.
     *
     * @param matrix1 the matrix to transpose
     * @return the transpose of the matrix
     */
    public static double[][] transposeMatrix(double[][] matrix1) {
        int numRows = matrix1.length;
        int numCols = matrix1[0].length;
        double[][] result = new double[numCols][numRows];

        for (int i = 0; i < numCols; i++) {
            for (int j = 0; j < numRows; j++) {
                result[i][j] = matrix1[j][i];
            }
        }
        return result;
    }

    // Bir matrisin tersini bulan metot
    
    /**
     * Finds the inverse of a square matrix.
     *
     * @param matrix the matrix to find the inverse of
     * @return the inverse of the matrix, or null if the matrix is singular
     */
    public static double[][] findInverse(double[][] matrix) {
        int n = matrix.length;
        double[][] augmentedMatrix = new double[n][2 * n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                augmentedMatrix[i][j] = matrix[i][j];
                augmentedMatrix[i][j + n] = (i == j) ? 1.0 : 0.0;
            }
        }

        for (int i = 0; i < n; i++) {
            double pivot = augmentedMatrix[i][i];
            if (pivot == 0) {
                System.out.print("The matrix is singular, and its inverse does not exist.\n");
                return null;
            }
            for (int j = 0; j < 2 * n; j++) {
                augmentedMatrix[i][j] /= pivot;
            }
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = augmentedMatrix[k][i];
                    for (int j = 0; j < 2 * n; j++) {
                        augmentedMatrix[k][j] -= factor * augmentedMatrix[i][j];
                    }
                }
            }
        }

        double[][] inverse = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverse[i][j] = augmentedMatrix[i][j + n];
            }
        }

        return inverse;
    }
    /**
     * Prints a matrix to the console.
     *
     * @param matrix the matrix to print
     */
    public static void printMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            System.out.print("[");
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("%.2f", matrix[i][j]);
                if (j < matrix[i].length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.print("]\n");
        }
    }

    /**
     * Displays the matrix operations menu.
     */
    public static void displayMenu() {
        System.out.println("\nMatrix Operations Menu:");
        System.out.println("1. Matrix Multiplication");
        System.out.println("2. Element-wise Multiplication");
        System.out.println("3. Transpose of the Matrix");
        System.out.println("4. Inverse of the Matrix");
        System.out.println("5. Return to Main Menu");
        System.out.print("Please enter the number of the operation you want to perform (1-5): ");
    }

 //EncryptionDecryption
    
    /**
     * Runs the encryption and decryption functionality using Caesar cipher.
     *
     * @param input the Scanner object for user input
     */
    public static void runEncryptionDecryption(Scanner input) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // show the menu
            System.out.println("\nText Encryption/Decryption");
            System.out.println("1.Encryption");
            System.out.println("2.Decryption");
            System.out.println("3.Return to the Main Menu");
            System.out.print("Choose: ");

            int choice;
            try {
                choice = scanner.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Please enter a valid number");
                scanner.nextLine(); // clean the buffer
                continue;
            }
            scanner.nextLine(); // It is going to prevent to push the enter button for multiple times
            
            // control for return to the main menu
            if (choice == 3) {
                System.out.println("Returning to main menu...");
                break;
            }

            // take the shifting value
            System.out.print("Please enter shifting value (between -26 to 26): ");
            int shift;
            try {
                shift = scanner.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                scanner.nextLine(); // Clean the buffer
                continue;
            }
            scanner.nextLine(); // It is going to prevent to push the enter button for multiple times

            // check the validity of shifting value
            if (shift < -26 || shift > 26) {
                System.out.println("Unvalid shifting value. Please enter a value between -26 to 26.");
                continue;
            }

            // take the text
            System.out.print("Enter the text: ");
            String text = scanner.nextLine();

            // create a string for result
            String result = "";
            // make encryption or decryption
            for (int i = 0; i < text.length(); i++) {
                char character = text.charAt(i);

                if (Character.isLetter(character)) {
                    char mainLetter = Character.isUpperCase(character) ? 'A' : 'a';
                    int diff = character - mainLetter;
                    int newDiff;

                    if (choice == 1) { // encryption
                        newDiff = (diff + shift + 26) % 26;
                    } else { // decryption
                        newDiff = (diff - shift + 26) % 26;
                    }

                    char newChar = (char) (mainLetter + newDiff);
                    result = result + newChar;
                } else {
                    result = result + character;
                }
            }

            // show the result
            if (choice == 1) {
                System.out.println("Encrypted text: " + result);
            } else {
                System.out.println("Decrypted text: " + result);
            }
            
        }
        

        scanner.close();
    }
    
    
    
    //Tic-Tac-Toe
    /**
     * Runs the Tic-Tac-Toe game.
     *
     * @param input the Scanner object for user input
     */
    public static void runTicTacToe(Scanner input) {
        char[][] board = {
            {'1', '2', '3'},
            {'4', '5', '6'},
            {'7', '8', '9'}
        };
        char currentPlayer = 'X';
        boolean gameWon = false;

        while (!gameWon && !isBoardFull(board)) {
            printBoard(board);
            System.out.print("Player " + currentPlayer + ", enter your move (1-9): ");
            int move = input.nextInt();

            // Convert the user entered action into a table
            int row = (move - 1) / 3;
            int col = (move - 1) % 3;

            // Invalid movement control
            if (move < 1 || move > 9 || (board[row][col] == 'X' || board[row][col] == 'O')) {
                System.out.println("This move is not valid. Try again.");
                continue;
            }

            // Add the move to the table
            board[row][col] = currentPlayer;
            gameWon = checkWin(board, currentPlayer);

            if (gameWon) {
                printBoard(board);
                System.out.println("Player " + currentPlayer + " wins!");
            } else {
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            }
        }

        if (!gameWon) {
            printBoard(board);
            System.out.println("It's a draw!");
        }
    }

    /**
     * Prints the Tic-Tac-Toe board to the console.
     *
     * @param board the Tic-Tac-Toe board
     */
    private static void printBoard(char[][] board) {
        System.out.println("Current board:");
        for (int i = 0; i < 3; i++) {
            System.out.print("|");
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + "|");
            }
            System.out.println();
        }
    }
    
    /**
     * Checks if the board is full in the Tic-Tac-Toe game.
     *
     * @param board the Tic-Tac-Toe board
     * @return true if the board is full, false otherwise
     */

    private static boolean checkWin(char[][] board, char player) {
        // Check rows, columns, and diagonals for a win
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == player && board[i][1] == player && board[i][2] == player) || // Check rows
                (board[0][i] == player && board[1][i] == player && board[2][i] == player)) { // Check columns
                return true;
            }
        }
        return (board[0][0] == player && board[1][1] == player && board[2][2] == player) || // Check diagonal
               (board[0][2] == player && board[1][1] == player && board[2][0] == player); // Check reverse diagonal
    }

    /**
     * Checks if the board is full in the Tic-Tac-Toe game.
     *
     * @param board the Tic-Tac-Toe board
     * @return true if the board is full, false otherwise
     */
    private static boolean isBoardFull(char[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] != 'X' && board[i][j] != 'O') {
                    return false; // Found an empty space
                }
            }
        }
        return true; // No empty spaces
    }

    }
