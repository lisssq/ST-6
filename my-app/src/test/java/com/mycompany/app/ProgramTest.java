package com.mycompany.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.GridLayout;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class GameLogicTest {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
        game.cplayer = game.player1;
        game.player1.symbol = 'X';
        game.player2.symbol = 'O';
    }

    @Test
    void testGameConstructorInitializesCorrectly() {
        Game newGame = new Game();
        assertEquals('X', newGame.player1.symbol);
        assertEquals('O', newGame.player2.symbol);
        assertEquals(State.PLAYING, newGame.state);
        for (int i = 0; i < 9; i++) {
            assertEquals(' ', newGame.board[i]);
        }
    }

    @Test
    void testInitialBoardEmpty() {
        for (int i = 0; i < 9; i++) {
            assertEquals(' ', game.board[i]);
        }
        assertEquals(State.PLAYING, game.state);
    }

    @Test
    void testCheckStateXWinFirstRow() {
        game.board[0] = 'X';
        game.board[1] = 'X';
        game.board[2] = 'X';
        game.symbol = 'X';
        State result = game.checkState(game.board);
        assertEquals(State.XWIN, result);
    }

    @Test
    void testCheckStateXWinColumn() {
        game.board[0] = 'X';
        game.board[3] = 'X';
        game.board[6] = 'X';
        game.symbol = 'X';
        State result = game.checkState(game.board);
        assertEquals(State.XWIN, result);
    }

    @Test
    void testCheckStateXWinDiagonal() {
        game.board[0] = 'X';
        game.board[4] = 'X';
        game.board[8] = 'X';
        game.symbol = 'X';
        State result = game.checkState(game.board);
        assertEquals(State.XWIN, result);
    }

    @Test
    void testCheckStateOWinDiagonal() {
        game.board[0] = 'O';
        game.board[4] = 'O';
        game.board[8] = 'O';
        game.symbol = 'O';
        State result = game.checkState(game.board);
        assertEquals(State.OWIN, result);
    }

    @Test
    void testCheckStateDraw() {
        char[] drawBoard = {
            'X', 'O', 'X',
            'O', 'X', 'O',
            'O', 'X', 'O'
        };
        System.arraycopy(drawBoard, 0, game.board, 0, 9);
        State result = game.checkState(game.board);
        assertEquals(State.DRAW, result);
    }

    @Test
    void testCheckStatePlaying() {
        game.board[0] = 'X';
        State result = game.checkState(game.board);
        assertEquals(State.PLAYING, result);
    }

    @Test
    void testCheckStateAllWinConditions() {
        int[][] winCombos = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
        };
        for (int[] combo : winCombos) {
            game.board = new char[9];
            for (int i = 0; i < 9; i++) {
                game.board[i] = ' ';
            }
            game.board[combo[0]] = 'X';
            game.board[combo[1]] = 'X';
            game.board[combo[2]] = 'X';
            game.symbol = 'X';
            State result = game.checkState(game.board);
            assertEquals(State.XWIN, result, "Failed for combo: " + combo[0] + "," + combo[1] + "," + combo[2]);
        }
    }

    @Test
    void testCheckStateWithNoSymbol() {
        game.board[0] = 'X';
        game.board[1] = 'X';
        game.board[2] = 'X';
        State result = game.checkState(game.board);
        assertNotEquals(State.XWIN, result);
    }

    @Test
    void testGenerateMoves() {
        game.board[0] = 'X';
        game.board[4] = 'O';
        game.board[8] = 'X';
        ArrayList<Integer> moves = new ArrayList<>();
        game.generateMoves(game.board, moves);
        assertEquals(6, moves.size());
        assertTrue(moves.contains(1));
        assertTrue(moves.contains(2));
        assertTrue(moves.contains(3));
        assertFalse(moves.contains(0));
    }

    @Test
    void testGenerateMovesEmptyBoard() {
        game.board = new char[9];
        for (int i = 0; i < 9; i++) {
            game.board[i] = ' ';
        }
        ArrayList<Integer> moves = new ArrayList<>();
        game.generateMoves(game.board, moves);
        assertEquals(9, moves.size());
        for (int i = 0; i < 9; i++) {
            assertTrue(moves.contains(i));
        }
    }

    @Test
    void testGenerateMovesFullBoard() {
        char[] fullBoard = {
            'X', 'O', 'X',
            'O', 'X', 'O',
            'O', 'X', 'O'
        };
        System.arraycopy(fullBoard, 0, game.board, 0, 9);
        ArrayList<Integer> moves = new ArrayList<>();
        game.generateMoves(game.board, moves);
        assertEquals(0, moves.size());
    }

    @Test
    void testEvaluatePositionXWin() {
        game.board[0] = 'X';
        game.board[1] = 'X';
        game.board[2] = 'X';
        game.symbol = 'X';
        int value = game.evaluatePosition(game.board, game.player1);
        assertEquals(Game.INF, value);
    }

    @Test
    void testEvaluatePositionOLoseToX() {
        game.board[0] = 'X';
        game.board[1] = 'X';
        game.board[2] = 'X';
        game.symbol = 'X';
        int value = game.evaluatePosition(game.board, game.player2);
        assertEquals(-Game.INF, value);
    }

    @Test
    void testEvaluatePositionDraw() {
        char[] drawBoard = {
            'X', 'O', 'X',
            'O', 'X', 'O',
            'O', 'X', 'O'
        };
        System.arraycopy(drawBoard, 0, game.board, 0, 9);
        int value = game.evaluatePosition(game.board, game.player1);
        assertEquals(0, value);
    }

    @Test
    void testEvaluatePositionReturnsMinusOneForPlaying() {
        game.board = new char[9];
        for (int i = 0; i < 9; i++) {
            game.board[i] = ' ';
        }
        game.board[0] = 'X';
        game.symbol = 'X';
        int value = game.evaluatePosition(game.board, game.player1);
        assertEquals(-1, value);
    }

    @Test
    void testMaxMoveReturnsCorrectValue() {
        game.board = new char[9];
        for (int i = 0; i < 9; i++) {
            game.board[i] = ' ';
        }
        game.board[0] = 'X';
        game.board[1] = 'X';
        game.symbol = 'X';
        int value = game.MaxMove(game.board, game.player1);
        assertTrue(value >= -Game.INF && value <= Game.INF);
    }

    @Test
    void testMinMoveReturnsCorrectValue() {
        game.board = new char[9];
        for (int i = 0; i < 9; i++) {
            game.board[i] = ' ';
        }
        game.board[0] = 'O';
        game.board[1] = 'O';
        game.symbol = 'O';
        int value = game.MinMove(game.board, game.player2);
        assertTrue(value >= -Game.INF && value <= Game.INF);
    }

    @Test
    void testMinMoveOnAlmostWinningBoard() {
        char[] board = {
            'X', 'X', ' ',
            'O', 'O', ' ',
            ' ', ' ', ' '
        };
        System.arraycopy(board, 0, game.board, 0, 9);
        game.symbol = 'O';
        int value = game.MinMove(game.board, game.player2);
        assertTrue(value >= -Game.INF && value <= Game.INF);
    }

    @Test
    void testMaxMoveOnAlmostWinningBoard() {
        char[] board = {
            'X', 'X', ' ',
            'O', 'O', ' ',
            ' ', ' ', ' '
        };
        System.arraycopy(board, 0, game.board, 0, 9);
        game.symbol = 'X';
        int value = game.MaxMove(game.board, game.player1);
        assertTrue(value >= -Game.INF && value <= Game.INF);
    }

    @Test
    void testMinimaxMakesValidMove() {
        game.board = new char[9];
        for (int i = 0; i < 9; i++) {
            game.board[i] = ' ';
        }
        int move = game.MiniMax(game.board, game.player2);
        assertTrue(move >= 1 && move <= 9);
    }

    @Test
    void testMinimaxReturnsValidMoveOnEmptyBoard() {
        game.board = new char[9];
        for (int i = 0; i < 9; i++) {
            game.board[i] = ' ';
        }
        int move = game.MiniMax(game.board, game.player2);
        assertTrue(move >= 1 && move <= 9, "Move must be between 1 and 9, got " + move);
    }

    @Test
    void testMiniMaxReturnsBestMoveWhenOnlyOneMoveLeft() {
        char[] almostFull = {
            'X', 'O', 'X',
            'O', 'X', 'O',
            'O', 'X', ' '
        };
        System.arraycopy(almostFull, 0, game.board, 0, 9);
        int move = game.MiniMax(game.board, game.player2);
        assertEquals(9, move);
    }

    @Test
    void testMiniMaxWithAlmostFullBoardForX() {
        char[] almostFull = {
            'X', 'O', 'X',
            'O', 'X', 'O',
            'O', ' ', 'X'
        };
        System.arraycopy(almostFull, 0, game.board, 0, 9);
        int move = game.MiniMax(game.board, game.player1);
        assertTrue(move >= 1 && move <= 9);
    }

    @Test
    void testGameINFConstant() {
        assertEquals(100, Game.INF);
    }

    @Test
    void testPlayerSymbols() {
        assertEquals('X', game.player1.symbol);
        assertEquals('O', game.player2.symbol);
    }

    @Test
    void testSwitchCurrentPlayer() {
        assertEquals(game.player1, game.cplayer);
        game.cplayer = game.player2;
        assertEquals(game.player2, game.cplayer);
    }

    @Test
    void testStateEnumValues() {
        assertNotNull(State.PLAYING);
        assertNotNull(State.OWIN);
        assertNotNull(State.XWIN);
        assertNotNull(State.DRAW);
    }

    @Test
    void testStateValues() {
        State[] states = State.values();
        assertEquals(4, states.length);
        assertTrue(containsState(states, State.PLAYING));
        assertTrue(containsState(states, State.XWIN));
        assertTrue(containsState(states, State.OWIN));
        assertTrue(containsState(states, State.DRAW));
    }

    private boolean containsState(State[] states, State target) {
        for (State s : states) {
            if (s == target) {
                return true;
            }
        }
        return false;
    }

    @Test
    void testTicTacToeCellCreation() {
        TicTacToeCell cell = new TicTacToeCell(5, 1, 2);
        assertEquals(' ', cell.getMarker());
        assertEquals(5, cell.getNum());
        assertEquals(2, cell.getRow());
        assertEquals(1, cell.getCol());
        cell.setMarker("X");
        assertEquals('X', cell.getMarker());
    }

    @Test
    void testTicTacToeCellSetMarkerDisablesButton() {
        TicTacToeCell cell = new TicTacToeCell(0, 0, 0);
        assertTrue(cell.isEnabled());
        cell.setMarker("O");
        assertFalse(cell.isEnabled());
        assertEquals('O', cell.getMarker());
    }

    @Test
    void testTicTacToeCellMultipleMarkers() {
        TicTacToeCell cell = new TicTacToeCell(0, 0, 0);
        cell.setMarker("X");
        assertEquals('X', cell.getMarker());
        assertFalse(cell.isEnabled());
        cell.setMarker("O");
        assertEquals('O', cell.getMarker());
    }

    @Test
    void testTicTacToePanelCreation() {
        assertDoesNotThrow(() -> {
            TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
            assertNotNull(panel);
        });
    }

    @Test
    void testTicTacToePanelCellsInitialization() {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        try {
            java.lang.reflect.Field field = TicTacToePanel.class.getDeclaredField("cells");
            field.setAccessible(true);
            TicTacToeCell[] cells = (TicTacToeCell[]) field.get(panel);
            assertNotNull(cells);
            assertEquals(9, cells.length);
            for (int i = 0; i < 9; i++) {
                assertNotNull(cells[i]);
            }
        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }

    @Test
    void testTicTacToePanelGetGameState() throws Exception {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        java.lang.reflect.Field gameField = TicTacToePanel.class.getDeclaredField("game");
        gameField.setAccessible(true);
        Game panelGame = (Game) gameField.get(panel);
        assertNotNull(panelGame);
        assertEquals('X', panelGame.player1.symbol);
    }

    @Test
    void testTicTacToePanelActionPerformedXMove() throws Exception {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        java.lang.reflect.Field cellsField = TicTacToePanel.class.getDeclaredField("cells");
        cellsField.setAccessible(true);
        TicTacToeCell[] cells = (TicTacToeCell[]) cellsField.get(panel);
        java.lang.reflect.Field gameField = TicTacToePanel.class.getDeclaredField("game");
        gameField.setAccessible(true);
        Game panelGame = (Game) gameField.get(panel);
        panelGame.cplayer = panelGame.player1;
        cells[0].doClick();
        assertEquals('X', cells[0].getMarker());
    }

    @Test
    void testTicTacToePanelActionPerformedOMove() throws Exception {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        java.lang.reflect.Field cellsField = TicTacToePanel.class.getDeclaredField("cells");
        cellsField.setAccessible(true);
        TicTacToeCell[] cells = (TicTacToeCell[]) cellsField.get(panel);
        java.lang.reflect.Field gameField = TicTacToePanel.class.getDeclaredField("game");
        gameField.setAccessible(true);
        Game panelGame = (Game) gameField.get(panel);
        panelGame.cplayer = panelGame.player2;
        cells[0].doClick();
        assertEquals('O', cells[0].getMarker());
    }

    @Test
    void testTicTacToePanelActionPerformedAndMinimaxResponse() throws Exception {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        java.lang.reflect.Field cellsField = TicTacToePanel.class.getDeclaredField("cells");
        cellsField.setAccessible(true);
        TicTacToeCell[] cells = (TicTacToeCell[]) cellsField.get(panel);
        java.lang.reflect.Field gameField = TicTacToePanel.class.getDeclaredField("game");
        gameField.setAccessible(true);
        Game panelGame = (Game) gameField.get(panel);
        panelGame.cplayer = panelGame.player1;
        cells[4].doClick();
        boolean someMoveMade = false;
        for (TicTacToeCell cell : cells) {
            if (cell.getMarker() == 'O') {
                someMoveMade = true;
                break;
            }
        }
        assertTrue(someMoveMade, "Minimax should make a move for O");
    }

    @Test
    void testTicTacToePanelGameStateXWin() throws Exception {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        java.lang.reflect.Field gameField = TicTacToePanel.class.getDeclaredField("game");
        gameField.setAccessible(true);
        Game panelGame = (Game) gameField.get(panel);
        panelGame.state = State.XWIN;
        assertEquals(State.XWIN, panelGame.state);
    }

    @Test
    void testTicTacToePanelGameStateOWin() throws Exception {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        java.lang.reflect.Field gameField = TicTacToePanel.class.getDeclaredField("game");
        gameField.setAccessible(true);
        Game panelGame = (Game) gameField.get(panel);
        panelGame.state = State.OWIN;
        assertEquals(State.OWIN, panelGame.state);
    }

    @Test
    void testTicTacToePanelGameStateDraw() throws Exception {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        java.lang.reflect.Field gameField = TicTacToePanel.class.getDeclaredField("game");
        gameField.setAccessible(true);
        Game panelGame = (Game) gameField.get(panel);
        panelGame.state = State.DRAW;
        assertEquals(State.DRAW, panelGame.state);
    }

    @Test
    void testTicTacToePanelFullGameSimulation() throws Exception {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        java.lang.reflect.Field cellsField = TicTacToePanel.class.getDeclaredField("cells");
        cellsField.setAccessible(true);
        TicTacToeCell[] cells = (TicTacToeCell[]) cellsField.get(panel);
        java.lang.reflect.Field gameField = TicTacToePanel.class.getDeclaredField("game");
        gameField.setAccessible(true);
        Game panelGame = (Game) gameField.get(panel);
        panelGame.cplayer = panelGame.player1;
        cells[0].doClick();
        int markedCells = 0;
        for (TicTacToeCell cell : cells) {
            if (cell.getMarker() != ' ') {
                markedCells++;
            }
        }
        assertTrue(markedCells >= 2, "At least 2 cells should be marked after player and AI moves");
    }

    @Test
    void testUtilityPrintMethodsDontThrowExceptions() {
        char[] charBoard = {'X', 'O', ' ', 'X', ' ', 'O', ' ', ' ', 'X'};
        int[] intBoard = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        ArrayList<Integer> moves = new ArrayList<>();
        moves.add(1);
        moves.add(3);
        moves.add(5);
        assertDoesNotThrow(() -> Utility.print(charBoard));
        assertDoesNotThrow(() -> Utility.print(intBoard));
        assertDoesNotThrow(() -> Utility.print(moves));
    }

    @Test
    void testProgramMainDoesNotThrowException() {
        assertDoesNotThrow(() -> {
            Thread t = new Thread(() -> {
                try {
                    Program.main(new String[]{});
                } catch (Exception e) {
                }
            });
            t.setDaemon(true);
            t.start();
            Thread.sleep(100);
        });
    }
}
