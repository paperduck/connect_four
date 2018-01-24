package com.paperduck;

import com.theoryinpractice.testng.inspection.AssertEqualsBetweenInconvertibleTypesTestNGInspection;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
//import java.lang.reflect.*;

public class ConnectFourTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void TestConnectFourBoardInsertPiece() throws Exception {
        ConnectFour cf = new ConnectFour();
        ConnectFour.Board board = cf.new Board(6,7);
        final Character BLANK = ' ';
        final Character PLAYER_CODE_1 = 'A';
        final Character PLAYER_CODE_2 = 'B';

        // Verify board was initialized correctly
        for (int i = 0; i < board.numRows(); i++) {
            for (int j = 0; j < board.numColumns(); j++) {
                Assert.assertEquals(board.grid[i][j], BLANK);
            }
        }

        // Fill bottom row
        for (int column = 0; column < board.numColumns(); column ++) {
            board.insertPiece(column,PLAYER_CODE_1);
        }
        // Check bottom row
        for (int column = 0; column < board.numColumns(); column ++) {
            Assert.assertEquals(board.grid[board.numRows() - 1][column], PLAYER_CODE_1);
        }

        // Fill left column
        for (int row = board.numRows() - 2; row >= 0; row --) {
            board.insertPiece(0, PLAYER_CODE_1);
        }
        // Check left column
        for (int row = board.numRows() - 2; row >= 0; row --) {
            Assert.assertEquals(board.grid[row][0], PLAYER_CODE_1);
        }

        // Fill rest of board
        for (int row = board.numRows() - 2; row >= 0; row --) {
            for (int column = 1; column < board.numColumns(); column++) {
                board.insertPiece(column, PLAYER_CODE_2);
            }
        }
        // Check entire board
        for (int row = 0; row < board.numRows(); row ++) {
            for (int column = 0; column < board.numColumns(); column ++ ) {
                if (column == 0 || row == board.numRows() - 1) {
                    Assert.assertEquals(board.grid[row][column], PLAYER_CODE_1);
                } else {
                    Assert.assertEquals(board.grid[row][column], PLAYER_CODE_2);
                }
            }
        }
    }

    @Test
    public void TestConnectFourBoardWon() throws Exception {
        ConnectFour cf = new ConnectFour();
        ConnectFour.Board board = cf.new Board(6,7);
        //final Character BLANK = ' ';
        final Character PLAYER_CODE_1 = 'A';
        final Character PLAYER_CODE_2 = 'B';
        ArrayList<Integer> position = new ArrayList<Integer>();
        position.add(0, 0);
        position.add(1, 0);

        // Case: no pieces inserted
        for (int row = 0; row < board.numRows(); row ++) {
            for (int column = 0; column < board.numColumns(); column++) {
                // Check every spot in board
                position.set(0, row);
                position.set(1, column);
                Assert.assertEquals(board.won(position), false);
            }
        }

        // Case: no winners
        // Initialize board to anti-line pattern
        // This pattern repeats with sideways 'L' shapes, so it's possible to make it algorithmically.
        board.grid = new Character[][]{
                {'O','*','*','*','O','*','*',},
                {'O','O','O','*','O','O','O',},
                {'*','O','*','*','*','O','*',},
                {'*','O','O','O','*','O','O',},
                {'*','*','O','*','*','*','O',},
                {'O','*','O','O','O','*','O',},
        };
        // Verify no spot has (4) in a row
        for (int row = 0; row < board.numRows(); row ++) {
            for (int column = 0; column < board.numColumns(); column++) {
                // Check every spot in board
                position.set(0, row);
                position.set(1, column);
                Assert.assertEquals(board.won(position), false);
            }
        }

        // Case: winner - horizontal
        board.grid = new Character[][]{
                {'B','B','B','B','A','B','B',},
                {'A','A','A','B','A','A','A',},
                {'B','A','B','B','B','A','B',},
                {'B','A','A','A','B','A','A',},
                {'B','B','A','B','B','B','A',},
                {'A','B','A','A','A','B','A',},
        };
        // from left end
        position.set(0, 0);
        position.set(1, 0);
        Assert.assertEquals(board.won(position), true);
        // from middle
        position.set(0, 0);
        position.set(1, 1);
        Assert.assertEquals(board.won(position), true);
        // from right end
        position.set(0, 0);
        position.set(1, 3);
        Assert.assertEquals(board.won(position), true);

        // Case: winner - diagonal (\)
        board.grid = new Character[][]{
                {'O','*','*','*','*','*','*',},
                {'*','O','*','*','*','*','*',},
                {'*','*','O','*','*','*','*',},
                {'*','*','*','O','*','*','*',},
                {'*','*','*','*','O','*','*',},
                {'*','*','*','*','*','O','*',},
        };
        // from left end
        position.set(0, 0);
        position.set(1, 0);
        Assert.assertEquals(board.won(position), true);
        // from middle
        position.set(0, 3);
        position.set(1, 3);
        Assert.assertEquals(board.won(position), true);
        // from right end
        position.set(0, 5);
        position.set(1, 5);
        Assert.assertEquals(board.won(position), true);

        // Case: winner - diagonal (/)
        board.grid = new Character[][]{
                {'*','*','*','*','*','*','*',},
                {'*','*','*','*','*','O','*',},
                {'*','*','*','*','O','*','*',},
                {'*','*','*','O','*','*','*',},
                {'*','*','O','*','*','*','*',},
                {'*','*','*','*','*','*','*',},
        };
        // from left end
        position.set(0, 4);
        position.set(1, 2);
        Assert.assertEquals(board.won(position), true);
        // from middle
        position.set(0, 3);
        position.set(1, 3);
        Assert.assertEquals(board.won(position), true);
        // from right end
        position.set(0, 1);
        position.set(1, 5);
        Assert.assertEquals(board.won(position), true);
    }
}