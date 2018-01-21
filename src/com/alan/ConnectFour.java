package com.alan;

import java.io.InputStream;
import java.util.Scanner;
import java.util.ArrayList;

public class ConnectFour {

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
                "Enter q to quit.\n" +
                "Enter commands without any whitespace.\n" +
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


    protected class Board {
        private Integer rows, columns;
        private Character[][] grid;
        private final Character EMPTY_SPOT = ' ';
        private final int WINNING_LENGTH = 4;

        public Board(Integer rows, Integer columns){
            this.rows = rows;
            this.columns = columns;
            this.grid = new Character[rows][columns];
            // Initialize just to be sure
            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    grid[row][column] = ' ';
                }
            }
        }

        public Integer numRows(){ return this.rows; }

        public Integer numColumns(){ return this.columns; }

        public void print() {
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
         * @param column - 0-based
         * @return row, column (0-based) of end spot; null if none possible
         */
        public ArrayList<Integer> insertPiece(Integer column, Character playerColor) {
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
            // insert into last row
            this.grid[this.numRows() - 1][column] = playerColor;
            // return insertion spot
            ArrayList<Integer> restingPlace = new ArrayList<Integer>(2);
            restingPlace.add(0, this.numRows() - 1);
            restingPlace.add(1, column);
            return restingPlace;
        }

        /**
         *
         * @param pieceLocation
         * @return true if pieceLocation is part of a winning streak, false otherwise.
         */
        public boolean won(ArrayList<Integer> pieceLocation) {
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

    protected class Player {
        private Integer id;
        private String colorName;
        private Character colorCode;

        public Player(final Integer id, final String colorName, final Character colorCode) {
            this.id = id;
            this.colorName = colorName;
            this.colorCode = colorCode;
        }

        public Integer getId() { return this.id; }
        public String getColorName() { return this.colorName; }
        public Character getColorCode() { return this.colorCode; }
    }
}

