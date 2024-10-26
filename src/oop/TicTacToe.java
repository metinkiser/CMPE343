package oop; // Paket adını unutma

import java.util.Scanner;

public class TicTacToe {

    // Constants for players
    private static final char PLAYER_X = 'X';
    private static final char PLAYER_O = 'O';
    private static final char EMPTY = ' ';

    // Board Structure
    private char[][] board;
    private char currentPlayer;

    // Constructor to initialize the board
    public TicTacToe() {
        board = new char[3][3];
        initializeBoard();
        currentPlayer = PLAYER_X; // X starts first
    }

    // Empty Spaces
    private void initializeBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board[row][col] = EMPTY;
            }
        }
    }

    // Display the current board
    public void printBoard() {
        System.out.println("Current board:");
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                System.out.print(board[row][col]);
                if (col < 2) {
                    System.out.print(" | ");
                }
            }
            System.out.println();
            if (row < 2) {
                System.out.println("---------");
            }
        }
    }

    public boolean makeMove(int row, int col) {
        if (isValidMove(row, col)) {
            board[row][col] = currentPlayer;
            return true;
        }
        return false;
    }

    // Checking the validity
    private boolean isValidMove(int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == EMPTY;
    }

    // Checking if any player wins
    public boolean checkWin() {
        return (checkRows() || checkColumns() || checkDiagonals());
    }

    // Checking if there is a row able to win
    private boolean checkRows() {
        for (int row = 0; row < 3; row++) {
            if (board[row][0] == currentPlayer && board[row][1] == currentPlayer && board[row][2] == currentPlayer) {
                return true;
            }
        }
        return false;
    }

    // Checking if there is a column able to win
    private boolean checkColumns() {
        for (int col = 0; col < 3; col++) {
            if (board[0][col] == currentPlayer && board[1][col] == currentPlayer && board[2][col] == currentPlayer) {
                return true;
            }
        }
        return false;
    }

    // Checking if there is a diagonal is able to win
    private boolean checkDiagonals() {
        return (board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) ||
               (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer);
    }

    // Checking if it is a tie 
    public boolean checkTie() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == EMPTY) {
                    return false;  // Still moves available
                }
            }
        }
        return true;  // No more moves, it's a tie
    }

    // Switch to the next player
    public void switchPlayer() {
        currentPlayer = (currentPlayer == PLAYER_X) ? PLAYER_O : PLAYER_X;
    }

    // Get the current player
    public char getCurrentPlayer() {
        return currentPlayer;
    }

    // Start game method to be called from Main
    public static void startGame(Scanner scanner) {
        TicTacToe game = new TicTacToe();

        // Game loop function
        while (true) {
            game.printBoard();
            System.out.println("Player " + game.getCurrentPlayer() + ", enter your move (row and column): ");
            int row = scanner.nextInt();
            int col = scanner.nextInt();

            if (game.makeMove(row, col)) {
                if (game.checkWin()) {
                    game.printBoard();
                    System.out.println("Player " + game.getCurrentPlayer() + " wins!");
                    break;
                } else if (game.checkTie()) {
                    game.printBoard();
                    System.out.println("It's a tie!");
                    break;
                }
                game.switchPlayer();
            } else {
                System.out.println("Invalid move, please try again.");
            }
        }
    }
}
