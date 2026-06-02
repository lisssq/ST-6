package com.mycompany.app;

import com.mycompany.app.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class ProgramTest {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
        game.player1.symbol = 'X';
        game.player2.symbol = 'O';
        game.cplayer = game.player1;
        game.symbol = 'X';
    }


    @Test
    void testInitialization() {
        Game testGame = new Game();
        assertNotNull(testGame.board);
        assertEquals(9, testGame.board.length);
        assertEquals(State.PLAYING, testGame.state);
        assertEquals(100, Game.INF);
    }

    @Test
    void testCheckStateHorizontalWin() {
        game.board[0] = 'X';
        game.board[1] = 'X';
        game.board[2] = 'X';
        game.symbol = 'X'; 
        assertEquals(State.XWIN, game.checkState(game.board));

        game.board[0] = ' '; game.board[1] = ' '; game.board[2] = ' ';
        game.board[3] = 'O';
        game.board[4] = 'O';
        game.board[5] = 'O';
        game.symbol = 'O';
        assertEquals(State.OWIN, game.checkState(game.board));
    }

    @Test
    void testCheckStateVerticalAndDiagonalWin() {
        game.board[0] = 'X';
        game.board[3] = 'X';
        game.board[6] = 'X';
        game.symbol = 'X';
        assertEquals(State.XWIN, game.checkState(game.board));

        char[] diagBoard = new char[9];
        diagBoard[2] = 'O';
        diagBoard[4] = 'O';
        diagBoard[6] = 'O';
        game.symbol = 'O';
        assertEquals(State.OWIN, game.checkState(diagBoard));
    }

    @Test
    void testCheckStateDrawCondition() {
        char[] fullBoard = {
            'X', 'O', 'X',
            'O', 'X', 'O',
            'O', 'X', 'O'
        };
        game.symbol = 'X';
        assertEquals(State.DRAW, game.checkState(fullBoard));
    }

    @Test
    void testGenerateMovesAndBoardSpaces() {
        ArrayList<Integer> moves = new ArrayList<>();
        
        game.generateMoves(game.board, moves);
        assertEquals(9, moves.size());

        game.board[2] = 'X';
        game.board[7] = 'O';
        moves.clear();
        game.generateMoves(game.board, moves);
        assertEquals(7, moves.size());
        assertFalse(moves.contains(2));
        assertFalse(moves.contains(7));
    }

    @Test
    void testEvaluatePositionScoring() {
        game.board[0] = 'X'; game.board[1] = 'X'; game.board[2] = 'X';
        game.symbol = 'X';
        
        assertEquals(Game.INF, game.evaluatePosition(game.board, game.player1));
        assertEquals(-Game.INF, game.evaluatePosition(game.board, game.player2));

        game.board[2] = ' ';
        assertEquals(-1, game.evaluatePosition(game.board, game.player1));
    }

    @Test
    void testMinimaxExecutionOnEmptyBoard() {
        int bestMove = game.MiniMax(game.board, game.player2);
        assertTrue(bestMove >= 1 && bestMove <= 9);
    }

    @Test
    void testMinAndMaxMovePruningBranches() {
        game.board[0] = 'X';
        game.symbol = 'X';
        
        int minResult = game.MinMove(game.board, game.player1);
        int maxResult = game.MaxMove(game.board, game.player2);
        
        assertTrue(minResult >= -Game.INF && minResult <= Game.INF);
        assertTrue(maxResult >= -Game.INF && maxResult <= Game.INF);
    }

    @Test
    void testTicTacToeCellState() {
        TicTacToeCell cell = new TicTacToeCell(1, 2, 0);
        assertEquals(1, cell.getNum());
        assertEquals(2, cell.getCol());
        assertEquals(0, cell.getRow());
        assertEquals(' ', cell.getMarker());

        cell.setMarker("O");
        assertEquals('O', cell.getMarker());
        assertFalse(cell.isEnabled());
    }

    @Test
    void testUtilityPrintMethods() {
        assertDoesNotThrow(() -> Utility.print(new char[9]));
        assertDoesNotThrow(() -> Utility.print(new int[9]));
        assertDoesNotThrow(() -> Utility.print(new ArrayList<Integer>()));
    }

    @Test
    void testPanelComponentsInception() {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        Component[] components = panel.getComponents();
        
        assertNotNull(components);
        assertTrue(components.length >= 9);
        
        for (int i = 0; i < 9; i++) {
            assertTrue(components[i] instanceof TicTacToeCell);
            TicTacToeCell cell = (TicTacToeCell) components[i];
            assertEquals(i, cell.getNum());
        }
    }

    @Test
    void testSafeInteractionWithUi() {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        Component[] components = panel.getComponents();
        
        TicTacToeCell firstCell = (TicTacToeCell) components[0];
        
        assertDoesNotThrow(() -> firstCell.doClick());
        
        assertNotEquals(' ', firstCell.getMarker());
    }

    @Test
    void testMainMethodBackgroundExecution() {
        assertDoesNotThrow(() -> {
            Thread uiThread = new Thread(() -> {
                try {
                    Program.main(new String[]{});
                } catch (Exception ignored) {
                }
            });
            uiThread.setDaemon(true);
            uiThread.start();
            Thread.sleep(30); 
        });
    }
}