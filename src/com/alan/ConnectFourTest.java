package com.alan;

import com.theoryinpractice.testng.inspection.AssertEqualsBetweenInconvertibleTypesTestNGInspection;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
        final Character BLANK = ' ';
        final Character PLAYER_CODE_1 = 'A';
        final Character PLAYER_CODE_2 = 'B';

        //
    }
}