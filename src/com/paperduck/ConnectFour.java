package com.paperduck;

import java.io.InputStream;
import java.util.Scanner;
import java.util.ArrayList;

public class ConnectFour {

    /**
     * Main gameplay loop.
     */
    protected void play() {
        final String COMMAND_QUIT = "q";
        final Integer NUM_PLAYERS = 2;
        ArrayList<Player> players = new ArrayList<Player>(NUM_PLAYERS);
        players.add(new Player(1, "RED", 'R'));
        players.add(new Player(2, "GREEN", 'G'));
        Integer currentPlayerIndex = 0;
        Scanner scanner = new Scanner( System.in );
        String inputLine = "";
        String prompt;
        Board board = new Board(6, 7);
        Integer inputColumn = -1;
        ArrayList<Integer> insertResult = null;

        // Greet user
        prompt = "*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*\n" +
                "Welcome to Connect Four.\n" +
                "Enter commands without any whitespace.\n" +
                "Enter q to quit.\n" +
                "(Press the ENTER key to start)\n" +
                "*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*";
        System.out.println(prompt);
        inputLine = scanner.nextLine();

        // Start game
        while(! inputLine.equals(COMMAND_QUIT)) {
            // prompt
            board.print();
            prompt = String.format(
                    "Player %s [%s] - choose column (%d - %d): ",
                    players.get(currentPlayerIndex).getId(),
                    players.get(currentPlayerIndex).getColorName(),
                    1,
                    board.numColumns()
            );
            System.out.println(prompt);
            inputLine = scanner.nextLine();

            try {
                inputColumn = Integer.valueOf(inputLine);
            } catch(Exception e) {
                if (! inputLine.equals(COMMAND_QUIT)) {
                    System.out.println("Invalid column number. Enter a number without whitespace.");
                }
                continue;
            }
            if (inputColumn < 1 || inputColumn > board.numColumns()) {
                System.out.println("Invalid column number. Exceeds size of board.");
                continue;
            } else {
                // user input valid column number
                inputColumn -= 1; // make 0-based
                insertResult = board.insertPiece(inputColumn, players.get(currentPlayerIndex).getColorCode());
                if (insertResult == null) {
                    // Unable to insert. Column may be full.
                    System.out.println("Unable to insert. Is the column full?");
                } else {
                    // successful insert.
                    // Check for winner
                    if (board.won(insertResult)) {
                        board.print();
                        System.out.println(String.format(
                                "Player %s [%s] wins!",
                                players.get(currentPlayerIndex).getId(),
                                players.get(currentPlayerIndex).getColorName()
                        ));
                        break;
                    }
                    // Move to next player.
                    currentPlayerIndex ++;
                    if (currentPlayerIndex >= players.size()) {
                        currentPlayerIndex = 0;
                    }
                }
            }
        }
        System.out.println("Goodbye.");
    }


    /**
     * Class to represent a game board.
     */
    protected class Board {
        private Integer rows, columns;
        protected Character[][] grid;
        private final Character EMPTY_SPOT = ' ';
        private final int WINNING_LENGTH = 4;

        /**
         * Constructor
         * @param rows      - number of rows in board (height)
         * @param columns   - number of columns in board (width)
         */
        protected Board(Integer rows, Integer columns){
            this.rows = rows;
            this.columns = columns;
            this.grid = new Character[rows][columns];
            // Initialize just to be sure
            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    grid[row][column] = EMPTY_SPOT;
                }
            }
        }

        // Getters
        public Integer numRows(){ return this.rows; }
        public Integer numColumns(){ return this.columns; }

        /**
         * Display, in the command prompt window,
         * the current status of the game board.
         */
        private void print() {
            String line = "";
            for (int row = 0; row < this.rows; row ++) {
                line = "|";
                for (int column = 0; column < this.columns; column ++) {
                    line += grid[row][column].toString() + "|";
                }
                System.out.println(line);
            }
            System.out.println("");
        }

        /**
         * Insert a game piece into the board.
         * This starts at the top row of the given column, and iterates
         * downward, simulating gravity. When the bottom of the board or another
         * game piece is reached, the piece is inserted there, unless the column
         * is full.
         * @param column - (0-based) column to insert piece into.
         * @param playerColor - color of piece being inserted
         * @return (row, column) (0-based) of end spot; null if none possible.
         */
        protected ArrayList<Integer> insertPiece(Integer column, Character playerColor) {
            for (int row = 0; row < this.rows; row++) {
                if (this.grid[row][column] != EMPTY_SPOT) {
                    // this spot is not available; try spot above
                    if (row - 1 >= 0){
                        // insert
                        this.grid[row - 1][column] = playerColor;
                        // return insertion spot
                        ArrayList<Integer> restingPlace = new ArrayList<Integer>(2);
                        restingPlace.add(0, row - 1);
                        restingPlace.add(1, column);
                        return restingPlace;
                    } else {
                        // No possible spots
                        return null;
                    }
                }
            }
            // Bottom reached; insert into last row.
            this.grid[this.numRows() - 1][column] = playerColor;
            // Return insertion spot.
            ArrayList<Integer> restingPlace = new ArrayList<Integer>(2);
            restingPlace.add(0, this.numRows() - 1);
            restingPlace.add(1, column);
            return restingPlace;
        }

        /**
         * Check if the piece at the given coordinates is part of a line of
         * consecutive pieces.
         * @param pieceLocation - (row, column) coordinates of piece to check
         * @return true if pieceLocation is part of a winning streak, false otherwise.
         */
        protected boolean won(ArrayList<Integer> pieceLocation) {
            // edge case: Called with empty spot in board
            if (grid[pieceLocation.get(0)][pieceLocation.get(1)] == EMPTY_SPOT) {
                return false;
            }

            int curRow = pieceLocation.get(0);
            int curColumn = pieceLocation.get(1);
            Character pieceColor = grid[curRow][curColumn];
            int numConsecutive = 1;

            // Check horizontally (left)
            curColumn =  pieceLocation.get(1) - 1;
            while (curColumn >= 0 && grid[curRow][curColumn] == pieceColor) {
                numConsecutive ++;
                curColumn --;
                if (numConsecutive >= WINNING_LENGTH) { return true; }
            }
            // Check horizontally (right)
            curColumn = pieceLocation.get(1) + 1;
            while (curColumn < this.numColumns() && grid[curRow][curColumn] == pieceColor) {
                numConsecutive ++;
                curColumn ++;
                if (numConsecutive >= WINNING_LENGTH) { return true; }
            }

            numConsecutive = 1;
            // Check diagonally (upper-left)
            curRow = pieceLocation.get(0) - 1;
            curColumn =  pieceLocation.get(1) - 1;
            while (curRow >= 0 && curColumn >= 0 && grid[curRow][curColumn] == pieceColor) {
                numConsecutive ++;
                curRow --;
                curColumn --;
                if (numConsecutive >= WINNING_LENGTH) { return true; }
            }
            // Check diagonally (lower-right)
            curRow = pieceLocation.get(0) + 1;
            curColumn = pieceLocation.get(1) + 1;
            while (curRow < this.numRows() && curColumn < this.numColumns() && grid[curRow][curColumn] == pieceColor) {
                numConsecutive ++;
                curRow ++;
                curColumn ++;
                if (numConsecutive >= WINNING_LENGTH) { return true; }
            }

            numConsecutive = 1;
            // Check diagonally (upper-right)
            curRow = pieceLocation.get(0) - 1;
            curColumn =  pieceLocation.get(1) + 1;
            while (curRow >= 0 && curColumn < this.numColumns() && grid[curRow][curColumn] == pieceColor) {
                numConsecutive ++;
                curRow --;
                curColumn ++;
                if (numConsecutive >= WINNING_LENGTH) { return true; }
            }
            // Check diagonally (lower-left)
            curRow = pieceLocation.get(0) + 1;
            curColumn = pieceLocation.get(1) - 1;
            while (curRow < this.numRows() && curColumn >= 0 && grid[curRow][curColumn] == pieceColor) {
                numConsecutive ++;
                curRow ++;
                curColumn --;
                if (numConsecutive >= WINNING_LENGTH) { return true; }
            }

            return false;
        }
    }

    /**
     * Class to represent one player.
     * The ID and color name are used for user interface output.
     * The color code is used to display the piece in the board.
     */
    private class Player {
        private Integer id;
        private String colorName;
        private Character colorCode;

        private Player(final Integer id, final String colorName, final Character colorCode) {
            this.id = id;
            this.colorName = colorName;
            this.colorCode = colorCode;
        }

        private Integer getId() { return this.id; }
        private String getColorName() { return this.colorName; }
        private Character getColorCode() { return this.colorCode; }
    }
}

