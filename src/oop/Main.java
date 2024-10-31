package cmpe343_oop;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
    	
        // ASCII sanatı çıktısı
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

        private static void clearConsole() {
            try {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            } catch (Exception e) {
                System.out.println("An error occurred while clearing the console: " + e.getMessage());
            }
        }
  //Statistical Information
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

    public static double calculateMean(double[] numbers) {
        double sum = 0;
        for (double num : numbers) {
            sum += num;
        }
        return sum / numbers.length;
    }

    public static double calculateMedian(double[] numbers) {
        java.util.Arrays.sort(numbers);
        int middle = numbers.length / 2;
        if (numbers.length % 2 == 0) {
            return (numbers[middle - 1] + numbers[middle]) / 2.0;
        } else {
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
        return size / sumOfInverses;
    }

    private static double harmonicMeanRecursive(double[] numbers, int size, int index) {
        if (index >= size) {
            return 0;
        }
        return 1.0 / numbers[index] + harmonicMeanRecursive(numbers, size, index + 1);
        
    }
    

    
    //MatrixOperations
    public static void runMatrixOperations(Scanner input) {
        int choice;
        int maxRowsAndColumns = 5;
        double[][] matrix1 = null;
        double[][] matrix2 = null;
        double[][] result = null;

        do {
            displayMenu(); // Show matrix operations menu
            choice = input.nextInt(); // Get selection from user
            input.nextLine(); // To clear the blank line

            switch (choice) {
                case 1:
                    System.out.println("\nMATRIX MULTIPLICATION\n");
                    matrix1 = inputMatrix(input, maxRowsAndColumns);
                    matrix2 = inputMatrix(input, maxRowsAndColumns);
                    result = multiplyMatrices(matrix1, matrix2);
                    printMatrix(result);
                    break;

                case 2:
                    System.out.println("\nELEMENT-WISE MULTIPLICATION\n");
                    matrix1 = inputMatrix(input, maxRowsAndColumns);
                    matrix2 = inputMatrix(input, maxRowsAndColumns);
                    result = elementWiseMultiply(matrix1, matrix2);
                    printMatrix(result);
                    break;

                case 3:
                    System.out.println("\nTRANSPOSE OF THE MATRIX\n");
                    matrix1 = inputMatrix(input, maxRowsAndColumns);
                    result = transposeMatrix(matrix1);
                    printMatrix(result);
                    break;

                case 4:
                    System.out.println("\nINVERSE OF THE MATRIX\n");
                    matrix1 = inputMatrix(input, maxRowsAndColumns);
                    result = findInverse(matrix1);
                    if (result != null) {
                        printMatrix(result);
                    }
                    break;

                case 5:
                    System.out.println("Returning to main menu...");
                    return; // Return to main menu
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }

           // After the process is completed, the user is given the option to return to the main menu or continue
            System.out.print("\nDo you want to return to the main menu? (yes/no): ");
            String decision = input.nextLine().trim().toLowerCase();
            if (decision.equals("yes")) {
                System.out.println("Returning to the main menu...");
            } else {
                System.out.println("Continuing with matrix operations...");
            }
        } while (true);
    }
    
    

    private static void displayMenu() {
        System.out.println("\n--- Matrix Operations Menu ---");
        System.out.println("1. Matrix Multiplication");
        System.out.println("2. Element-wise Multiplication");
        System.out.println("3. Transpose of the Matrix");
        System.out.println("4. Inverse of the Matrix");
        System.out.println("5. Return to Main Menu");
        System.out.print("Choose an option (1-5): ");
    }

    private static double[][] inputMatrix(Scanner input, int maxRowsAndColumns) {
        System.out.print("Enter the number of rows (1-" + maxRowsAndColumns + "): ");
        int rows = input.nextInt();
        System.out.print("Enter the number of columns (1-" + maxRowsAndColumns + "): ");
        int columns = input.nextInt();

        double[][] matrix = new double[rows][columns];
        System.out.println("Enter the elements of the matrix:");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print("Element [" + i + "][" + j + "]: ");
                matrix[i][j] = input.nextDouble();
            }
        }
        return matrix;
    }

    private static double[][] multiplyMatrices(double[][] matrix1, double[][] matrix2) {
        int rows1 = matrix1.length;
        int cols1 = matrix1[0].length;
        int rows2 = matrix2.length;
        int cols2 = matrix2[0].length;

        if (cols1 != rows2) {
            System.out.println("Matrix multiplication is not possible.");
            return null;
        }

        double[][] result = new double[rows1][cols2];
        for (int i = 0; i < rows1; i++) {
            for (int j = 0; j < cols2; j++) {
                for (int k = 0; k < cols1; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        return result;
    }

    private static double[][] elementWiseMultiply(double[][] matrix1, double[][] matrix2) {
        int rows = Math.min(matrix1.length, matrix2.length);
        int cols = Math.min(matrix1[0].length, matrix2[0].length);
        double[][] result = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = matrix1[i][j] * matrix2[i][j];
            }
        }
        return result;
    }

    private static double[][] transposeMatrix(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        double[][] result = new double[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[j][i] = matrix[i][j];
            }
        }
        return result;
    }

    private static double[][] findInverse(double[][] matrix) {
        int n = matrix.length;
        double[][] augmentedMatrix = new double[n][2 * n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                augmentedMatrix[i][j] = matrix[i][j];
                augmentedMatrix[i][j + n] = (i == j) ? 1 : 0;
            }
        }

        // Applied Gauss-Jordan Elimination
        for (int i = 0; i < n; i++) {
            double pivot = augmentedMatrix[i][i];
            if (pivot == 0) {
                System.out.println("Matrix is singular and cannot be inverted.");
                return null;
            }

            for (int j = 0; j < 2 * n; j++) {
                augmentedMatrix[i][j] /= pivot; // Pivot'u 1 yap
            }

            for (int j = 0; j < n; j++) {
                if (i != j) {
                    double factor = augmentedMatrix[j][i];
                    for (int k = 0; k < 2 * n; k++) {
                        augmentedMatrix[j][k] -= factor * augmentedMatrix[i][k];
                    }
                }
            }
        }

        // Take the opposite in the second half
        double[][] inverseMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverseMatrix[i][j] = augmentedMatrix[i][j + n];
            }
        }
        return inverseMatrix;
    }

    private static void printMatrix(double[][] matrix) {
        if (matrix == null) {
            System.out.println("No matrix to print.");
            return;
        }
        System.out.println("Matrix:");
        for (double[] row : matrix) {
            for (double element : row) {
                System.out.printf("%.2f ", element);
            }
            System.out.println();
        }

    }
    
 //EncryptionDecryption
    public static void runEncryptionDecryption(Scanner input) {
        

        while (true) {
            // show the menu
            System.out.println("\nText Encryption/Decryption");
            System.out.println("1.Encryption");
            System.out.println("2.Decryption");
            System.out.println("3.Return to the Main Menu");
            System.out.print("Choose: ");

            int choice;
            try {
                choice = input.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Please enter a valid number");
                input.nextLine(); // clean the buffer
                continue;
            }
            input.nextLine(); // It is going to prevent to push the enter button for multiple times
            
            // control for return to the main menu
            if (choice == 3) {
                System.out.println("Returning to main menu...");
                break;
            }

            // take the shifting value
            System.out.print("Please enter shifting value (between -26 to 26): ");
            int shift;
            try {
                shift = input.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                input.nextLine(); // Clean the buffer
                continue;
            }
            input.nextLine(); // It is going to prevent to push the enter button for multiple times

            // check the validity of shifting value
            if (shift < -26 || shift > 26) {
                System.out.println("Unvalid shifting value. Please enter a value between -26 to 26.");
                continue;
            }

            // take the text
            System.out.print("Enter the text: ");
            String text = input.nextLine();

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
        

        
    }
    
    
    
    //Tic-Tac-Toe
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
