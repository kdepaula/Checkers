import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class Square extends JComponent {
    //this is a white or blue empty square
    
    public static final int SQUARE_THICKNESS = 80;
    private int x; //position on board
    private int y; //position on board
    private int row; //row on board starting with 0 as top left
    private int col; // col on board starting with 0 as top left
    private Color color;
    
    public Square(int row, int col, Color c) {
        this.row = row;
        this.col = col;
        color = c;
        x = col * SQUARE_THICKNESS;
        y = row * SQUARE_THICKNESS;
    }
    
    public Square(int row, int col) {
        this.row = row;
        this.col = col;
        color = Color.BLUE;
        x = col * SQUARE_THICKNESS;
        y = row * SQUARE_THICKNESS;
    }
    
    @Override
    public int getX() {
        return x;
    }
    
    @Override
    public int getY() {
        return y;
    }
    
    public void setCol(int col) {
        this.x = col * SQUARE_THICKNESS;
        this.col = col;
    }
    
    public void setRow(int row) {
        this.y = row * SQUARE_THICKNESS;
        this.row = row;
    }
    
    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }
    
    public Color getColor() {
        return color;
    }
    
    
    public boolean isKing() {
        return false;
    }
    
    public void setColor(Color c) {
        color = c;
    }
    
    public boolean isOccupied() {
        return false;
    }
    
    /**
     * squares can't jump so this returns an empty array always
     * @param board
     * @param currPlayer
     * @return
     */
    public ArrayList<Square> jumpPossibilities(Square[][] board, boolean currPlayer) {
        ArrayList<Square> arr = new ArrayList<Square>();
        return arr;
    }
    
    /**
     * you can never start to move a piece on a square
     * @param currPlayer
     * @return
     */
    public boolean isValidStartSpot(boolean currPlayer) {
        return false;
    }
    
    /**
     * if its a blue square you can land on it
     * you can't land on the white squares
     * @return
     */
    public boolean isValidEndSpot() {
        if (this.color.equals(Color.BLUE)) {
            return true;
        }
        return false;
    }
    
    public boolean isCorrectSquareNum(Square endSquare) {
        return false;
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillRect(x, y, SQUARE_THICKNESS, SQUARE_THICKNESS);
    }
    
    public int getThickness() {
        return SQUARE_THICKNESS;
    }
    
    @Override
    public String toString() {
        if (getColor().equals(Color.BLUE)) {
            return "1";
        } else {
            return "0";
        }
    }
    
    /**
     * this is never a valid starting square
     * @param startRow
     * @param startCol
     * @param endRow
     * @param endCol
     * @param endSquare
     * @param currPlayer
     * @return
     */
    public boolean isValidMoveNoJump(int startRow, int startCol, int endRow, int endCol, 
            Square endSquare, boolean currPlayer) {
        return false;
    }
    
}