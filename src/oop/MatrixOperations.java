package oop;

import java.util.Scanner;

public class MatrixOperations {

    // Ana menüde çağrılacak metot
    public static void runMatrixOperations(Scanner input) {
        int choice;
        int maxRowsAndColumns = 5;
        double[][] matrix1 = null;
        double[][] matrix2 = null;
        double[][] result = null;

        do {
            displayMenu(); // Matris işlemleri menüsünü göster
            choice = input.nextInt(); // Kullanıcıdan seçim al
            input.nextLine(); // Boş satırı temizlemek için

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
                    return; // Ana menüye dönüş
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }

            // İşlem tamamlandıktan sonra kullanıcıya ana menüye dönme veya devam etme seçeneği sunuluyor
            System.out.print("\nDo you want to return to the main menu? (yes/no): ");
            String decision = input.nextLine().trim().toLowerCase();
            if (decision.equals("yes")) {
                System.out.println("Returning to main menu...");
                return; // Ana menüye dön
            } else {
                System.out.println("Continuing with matrix operations...");
            }

        } while (true); // Sürekli döngü, kullanıcı çıkana kadar çalışır
    }

    // Matris girişini kullanıcıdan almak için kullanılan metot
    public static double[][] inputMatrix(Scanner input, int maxRowsAndColumns) {
        int rows, columns;

        // Satır sayısını al
        do {
            System.out.print("Enter the number of rows (2-" + maxRowsAndColumns + "): ");
            rows = input.nextInt();
        } while (rows < 2 || rows > maxRowsAndColumns);

        // Sütun sayısını al
        do {
            System.out.print("Enter the number of columns (2-" + maxRowsAndColumns + "): ");
            columns = input.nextInt();
        } while (columns < 2 || columns > maxRowsAndColumns);

        double[][] matrix = new double[rows][columns];

        // Matris elemanlarını al
        System.out.println("Enter elements of the matrix:");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print("Matrix[" + i + "][" + j + "]: ");
                matrix[i][j] = input.nextDouble();
            }
        }

        return matrix;
    }

    // İki matrisi çarpan metot
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

    // İki matrisi eleman bazında çarpan metot
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

    // Bir matrisin transpozunu alan metot
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
    public static double[][] findInverse(double[][] matrix) {
        int n = matrix.length;
        double[][] augmentedMatrix = new double[n][2 * n];

        // Eğer kare matris değilse
        if (matrix[0].length != n) {
            System.out.println("Matrix's dimensions are not equal.");
            return null;
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                augmentedMatrix[i][j] = matrix[i][j];
                augmentedMatrix[i][j + n] = (i == j) ? 1.0 : 0.0;
            }
        }

        // Satır işlemleriyle tersi bul
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

    // Matrisi ekrana yazdırmak için metot
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
    
    public static void displayMenu() {
        System.out.println("\nMatrix Operations Menu:");
        System.out.println("1. Matrix Multiplication");
        System.out.println("2. Element-wise Multiplication");
        System.out.println("3. Transpose of the Matrix");
        System.out.println("4. Inverse of the Matrix");
        System.out.println("5. Return to Main Menu");
        System.out.print("Please enter the number of the operation you want to perform (1-5): ");
    }
}
