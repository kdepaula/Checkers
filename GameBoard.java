import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.*;

/**
 * This class instantiates a TicTacToe object, which is the model for the game.
 * As the user clicks the game board, the model is updated.  Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 * 
 * This game adheres to a Model-View-Controller design framework.  This
 * framework is very effective for turn-based games.  We STRONGLY 
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:  
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with 
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {
    
    private Square[][] board;
    private CheckerGame game; // model for the game
    private JLabel status; // current status text
    public static final int BOARD_DIM = 8;
    
    LinkedList<int[]> moves = new LinkedList<int[]>();
    
    // Game constants
    public static final int BOARD_WIDTH = Square.SQUARE_THICKNESS * BOARD_DIM;
    public static final int BOARD_HEIGHT = Square.SQUARE_THICKNESS * BOARD_DIM;
    
    /**
     * the end row of the last square that made a jump
     */
    private int lastRowJump;
    
    /**
     * the end column of the last square that made a jump
     */
    private int lastColJump;
    
    //you can ignore these I was using them to test things
    // I didn't remove them in case I wanted to test more things
    private boolean justJumpedBlack = false;
    private boolean justJumpedRed = false;
    private boolean justJumped = true;
    
    
    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);
        
        game = new CheckerGame(); // initializes model for the game
        status = statusInit; // initializes the status JLabel
        
        game.reset();
        
        /*
         * Listens for mouseclicks.  Updates the model, then updates the game board
         * based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            int startRow;
            int startCol;
            int endRow;
            int endCol;
            Square startSquare;
            
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                if (getBoardRow(p) != -1 && getBoardCol(p) != -1) {
                    startRow = getBoardRow(p);
                    startCol = getBoardCol(p);
                    startSquare = board[startRow][startCol];
                
                    if (startSquare instanceof Checker) {
                        if (!jumpExists().isEmpty()) {
                            ArrayList<Square> arr = jumpExists();
                            if (!arr.contains(startSquare)) {
                                startSquare = null;
                            }
                        }  
                    } else {
                        startSquare = null;
                    }
                } else {
                    startSquare = null;
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();
                endRow = getBoardRow(p);
                endCol = getBoardCol(p);
                if (startSquare != null && getBoardRow(p) != -1 && getBoardCol(p) != -1) {
                    Square endSquare = board[endRow][endCol];
                    if (!jumpExists().isEmpty() && 
                            startSquare.jumpPossibilities(board, 
                                    game.getCurrentPlayer()).contains(endSquare)) {
                            makeJump(startRow, startCol, endRow, endCol);
                            if (startSquare.getColor().equals(Color.BLACK)) {
                                justJumpedBlack = true;
                            } else {
                                justJumpedRed = true;
                            }
                            if (jumpExists().isEmpty() || !jumpExists().contains(startSquare)) {
                                game.changePlayer();
                            }
                    } else if (jumpExists().isEmpty() && 
                           startSquare.isValidMoveNoJump(startRow, startCol, endRow, endCol, 
                                   endSquare, game.getCurrentPlayer())) {
                        updateBoard(startRow, startCol, endRow, endCol);
                        replaceWithKing(endRow, endCol, game.getCurrentPlayer());
                        recordMove(startRow, startCol, endRow, endCol, 
                                game.getCurrentPlayer(), false);
                        game.changePlayer(); 
                        justJumpedBlack = false;
                        justJumpedRed = false;
                    }
                
                    updateStatus();
                    repaint(); 
                }
            }
        });
    }
    
    /**
     * 
     * calls the methods appropriate to make a jump
     * @param startRow
     * @param startCol
     * @param endRow
     * @param endCol
     */
    public void makeJump(int startRow, int startCol, int endRow, int endCol) {
        updateBoard(startRow, startCol, endRow, endCol);
        boolean king = 
                deleteJumpedSquare(startRow, startCol, endRow, endCol, game.getCurrentPlayer());
        replaceWithKing(endRow, endCol, game.getCurrentPlayer());
        recordMove(startRow, startCol, endRow, endCol, game.getCurrentPlayer(), king);
        setRowJump(endRow);
        setColJump(endCol);
    }
    
    /**
     * returns the end row of where the last jump occurred
     * @return
     */
    public int lastRowJump() {
        return lastRowJump;
    }
    
    /**
     * returns the end col of where the last jump occurred
     * @return
     */
    public int lastColJump() {
        return lastColJump;
    }
    
    /**
     * records the last row where a jump occurred
     * this is the ending row of the jump
     * @param row
     */
    private void setRowJump(int row) {
        lastRowJump = row;
    }
    
    /**
     * records the last column where a jump occurred
     * this is the ending column of the jump
     * @param col
     */
    private void setColJump(int col) {
        lastColJump = col;
    }
    
    /**
     * replaces a checker with a king checker if it gets to the end of the board
     * @param endRow
     * @param endCol
     * @param currPlayer
     */
    private void replaceWithKing(int endRow, int endCol, boolean currPlayer) {
        if (currPlayer && endRow == 7) {
            board[endRow][endCol] = new KingChecker(endRow, endCol, currPlayer);
        } else if (!currPlayer && endRow == 0) {
            board[endRow][endCol] = new KingChecker(endRow, endCol, currPlayer);
        }
    }
    
    /**
     * this is used to undo a king awarded to a player 
     * and replace is with a normal checker if a move is undone
     * @param endRow
     * @param endCol
     * @param currPlayer
     */
    private void undoKing(int endRow, int endCol, boolean currPlayer) {
        if (currPlayer && endRow == 7) {
            board[endRow][endCol] = new Checker(endRow, endCol, currPlayer);
        } else if (!currPlayer && endRow == 0) {
            board[endRow][endCol] = new Checker(endRow, endCol, currPlayer);
        }
    }
    
    /**
     * this swaps the squares on the board when a move is made
     * @param startRow
     * @param startCol
     * @param endRow
     * @param endCol
     */
    private void updateBoard(int startRow, int startCol, int endRow, int endCol) {
        Square endSquare = board[endRow][endCol];
        Square startSquare = board[startRow][startCol];
        board[startRow][startCol] = endSquare;
        endSquare.setRow(startRow);
        endSquare.setCol(startCol);
        board[endRow][endCol] = startSquare;
        startSquare.setRow(endRow);
        startSquare.setCol(endCol);
    }
    
    /**
     * this records a move by adding an array of information to the moves collection
     * each array represents a move
     * @param startRow
     * @param startCol
     * @param endRow
     * @param endCol
     * @param currPlayer
     * @param isKing
     */
    public void recordMove(int startRow, int startCol, int endRow, int endCol, boolean currPlayer, 
            boolean isKing) {
        int[] arr = new int[6];
        arr[0] = startRow;
        arr[1] = startCol;
        arr[2] = endRow;
        arr[3] = endCol;
        if (currPlayer) {
            arr[4] = 1;
        } else {
            arr[4] = 2;
        }
        if (isKing) {
            arr[5] = 1; 
        } else {
            arr[5] = 0;
        }
        moves.addFirst(arr);
    }
    
    /**
     * this deletes a square that is jumped
     * @param startRow
     * @param startCol
     * @param endRow
     * @param endCol
     * @param currPlayer
     * @return
     */
    private boolean deleteJumpedSquare(int startRow, int startCol, int endRow, 
            int endCol, boolean currPlayer) {
        int deletedRow = (startRow + endRow) / 2;
        int deletedCol = (startCol + endCol) / 2;
        Square deletedSquare = board[deletedRow][deletedCol];
        if (Math.abs(endRow - startRow) > 1) {
            board[deletedRow][deletedCol] = new Square(deletedRow, deletedCol);
            if (currPlayer) {
                game.decRedPieces();
            } else {
                game.decBlackPieces();
            }
        }
        return deletedSquare instanceof KingChecker;
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        game.reset();
        board = new Square[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col ++) {
                if ((row + col) % 2 == 0) {
                    board[row][col] = new Square(row, col, Color.WHITE);
                } else {
                    if (row < 3) {
                        board[row][col] = new Checker(row, col, true);  
                    } else if (row == 3 || row == 4) {
                        board[row][col] = new Square(row, col, Color.BLUE);  
                    } else {
                        board[row][col] = new Checker(row, col, false); 
                    }
                }
            }
        }
        moves.clear();
        status.setText("Player 1's Turn (Black)");
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }
    
    /**
     * saves the game by writing to a file
     */
    public void saveGame() {
        FileWriter writer = null;
        try {
            writer = new FileWriter("save.checkers");
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter buff = new BufferedWriter(writer);
        try {
            buff.write(movesToString());
            buff.newLine();
            for (String str: getBoardString()) {
                buff.write(str);
                buff.newLine();
            }
            buff.write("" + game.getCurrentPlayer());
            buff.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * opens the saved game by reading from save.checkers
     */
    public void openSavedGame() {
        moves.clear();
        FileReader reader = null;
        try {
            reader = new FileReader("save.checkers");
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader buff = new BufferedReader(reader);
        try {
            String firstLine = buff.readLine();
            String[] arr = firstLine.split(";");
            for (String str: arr) {
                String[] stringMove = str.split(" ");
                int[] numMove = new int[6];
                for (int i = 0; i < 6; i++) {
                    numMove[i] = Integer.parseInt(stringMove[i]);
                }
                moves.addLast(numMove);
            }
            
            int numBlackPieces = 0;
            int numRedPieces = 0;
            
            // 0 is empty white square
            //1 is empty blue square
            //2 is regular black checker
            //3 is regular red checker
            //4 is black king checker
            //5 is red king checker
            for (int row = 0; row < 8; row++) {
                String rowNum = buff.readLine();
                for (int col = 0; col < 8; col++) {
                    String[] rowStrings = rowNum.split(" ");
                    if (rowStrings[col].equals("0")) {
                        board[row][col] = new Square(row, col, Color.WHITE);
                    } else if (rowStrings[col].equals("1")) {
                        board[row][col] = new Square(row, col, Color.BLUE);
                    } else if (rowStrings[col].equals("2")) {
                        board[row][col] = new Checker(row, col, true);
                        numBlackPieces++;
                    } else if (rowStrings[col].equals("3")) {
                        board[row][col] = new Checker(row, col, false);
                        numRedPieces++;
                    } else if (rowStrings[col].equals("4")) {
                        numBlackPieces++;
                        board[row][col] = new KingChecker(row, col, true);
                    } else if (rowStrings[col].equals("5")) {
                        numRedPieces++;
                        board[row][col] = new KingChecker(row, col, false);
                    }
                }
            }
            
            game.setBlackPieces(numBlackPieces);
            game.setRedPieces(numRedPieces);
            game.setPlayer(Boolean.parseBoolean(buff.readLine()));
            updateStatus();
            
            repaint();
            requestFocusInWindow();
            buff.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * undoes a move
     */
    public void undoMove() {
        if (moves.isEmpty()) {
            game.reset(); 
        } else {
            int[] arr = moves.getFirst();
            int startRow = arr[0];
            int startCol = arr[1];
            int endRow = arr[2];
            int endCol = arr[3];
            boolean playerAtTime;
            if (arr[4] == 1) {
                playerAtTime = true;
            } else {
                playerAtTime = false;
            }
            undoKing(endRow, endCol, playerAtTime);
            updateBoard(startRow, startCol, endRow, endCol);
            if (Math.abs(endRow - startRow) > 1) {
                int deletedRow = (startRow + endRow) / 2;
                int deletedCol = (startCol + endCol) / 2;
                if (arr[5] == 0) {
                    board[deletedRow][deletedCol] = 
                            new Checker(deletedRow, deletedCol, !playerAtTime);
                } else {
                    board[deletedRow][deletedCol] = 
                            new KingChecker(deletedRow, deletedCol, !playerAtTime);
                }
                if (!playerAtTime) {
                    game.incRedPieces();
                } else {
                    game.incBlackPieces();
                }
            }
            game.setPlayer(playerAtTime);
            repaint();
            updateStatus();
            moves.removeFirst();
        }
    }
    
    /**
     * returns empty array list if no jump exists
     * @return array with possible startsquares for jumps for the current player
     */
    public ArrayList<Square> jumpExists() {
        ArrayList<Square> arr = new ArrayList<Square>();
        for (Square[] squares : board) {
            for (Square sq : squares) {
                if (!sq.jumpPossibilities(board, game.getCurrentPlayer()).isEmpty()) {
                    arr.add(sq);
                }
            }
        }
        return arr;
    }
    
    /**
     * 
     * @param p
     * @return the row given a point
     * return -1 if out of bounds
     */
    public int getBoardRow(Point p) {
        if (p.y > BOARD_HEIGHT || p.y < 0) {
            return -1;
        }
        int row = 8;
        int pos = BOARD_HEIGHT;
        while (p.y < pos) {
            row -= 1;
            pos -= Square.SQUARE_THICKNESS;
        }
        return row;
    }
    
    /**
     * 
     * @param p
     * @return the column given a point
     * returns -1 if its out of bounds
     */
    public int getBoardCol(Point p) {
        if (p.x > BOARD_WIDTH || p.x < 0) {
            return -1;
        }
        int col = 8;
        int pos = BOARD_WIDTH;
        while (p.x < pos) {
            col -= 1;
            pos -= Square.SQUARE_THICKNESS;
        }
        return col;
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        if (game.getCurrentPlayer()) {
            status.setText("Player 1's Turn (Black)");
        } else {
            status.setText("Player 2's Turn (Red)");
        }
        
        int winner = game.checkWinner();
        if (winner == 1) {
            status.setText("Player 2 (Red) wins!!!");
        } else if (winner == 2) {
            status.setText("Player 1 (Black) wins!!!");
        }
    }
    

    /**
     * Draws the game board.
     * 
     * This employs dynamic dispatch when drawing objects
     * because a checkers looks different from a square
     * which looks different than a king checker
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        for (Square[] squareArr : board) {
            for (Square sq : squareArr) {
                sq.paintComponent(g);
            }
        }
    }
    
    
    /**
     * 
     * @return a CLONE of the board game to keep everything encapsulated
     */
    public Square[][] getBoard() {
        return board.clone();
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
    
    /**
     * 
     * @return a string containing the moves of the board separated by semicolons
     */
    public String movesToString() {
        String movesStr = "";
        for (int[] arr : moves) {
            for (int i : arr) {
                movesStr += "" + i + " ";
            }
            movesStr = movesStr.trim();
            movesStr += ";";
        }
        if (movesStr != "") {
            movesStr = movesStr.substring(0, movesStr.length() - 1);  
        }
        return movesStr;
    }
    
    /**
     * gets the board as a string array
     * 0 is empty white square
     * 1 is empty blue square
     * 2 is regular black checker
     * 3 is regular red checker
     * 4 is black king checker
     * 5 is red king checker
     * @return string array of board state. each row is a row of the board
     */
    public String[] getBoardString() {
        String[] strings = new String[8];
        int i = 0;

        for (Square[] arr : board) {
            String str = "";
            for (Square sq: arr) {
                if (sq.getColor().equals(Color.WHITE)) {
                    str += "" + 0 + " ";
                } else if (!sq.isOccupied()) {
                    str += "" + 1 + " ";
                } else if (!sq.isKing() && sq.getColor().equals(Color.BLACK)) {
                    str += "" + 2 + " ";
                } else if (!sq.isKing() && sq.getColor().equals(Color.RED)) {
                    str += "" + 3 + " ";
                } else if (sq.getColor().equals(Color.BLACK)) {
                    str += "" + 4 + " ";
                } else {
                    str += "" + 5 + " ";
                }
            }
            strings[i] = str.trim();
            i++;
        }
        return strings;
    }
    
    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public void printGameState() {
        for (Square[] arr: board) {
            for (Square sq: arr) {
                System.out.print(sq.toString() + " ");
            }
            System.out.print("\n");
        }
    }
}
