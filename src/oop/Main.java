package oop; // Don't forget to add the package name

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean running = true;

        while (running) {
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
                    StatisticalInformation.displayStatistics(input); // Call statistical information
                    break;
                case "B":
                    MatrixOperations.runMatrixOperations(input); // Call matrix operations
                    break;
                case "C":
                    EncryptionDecryption.runEncryptionDecryption(input); // Call encryption/decryption operations
                    break;
                case "D":
                	 TicTacToe.startGame(input); // Call Tic-Tac-Toe game
                    break;
                case "E":
                    System.out.println("Exiting the program...");
                    input.close(); // Close the scanner
                    System.exit(0); // Programı tamamen kapatır
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
