import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Checker extends Square {
    
    //this is a checker square
    //the background of the checker square is black
    //the checker piece color is black if it does not occupy the square
    
    
    private Color checkerColor;
    public static final int RADIUS = 70;
    
    //true if player1 and false if player2
    //player1 is black checkers
    private boolean player;
    
    public Checker(int row, int col, boolean player) {
        super(row, col, Color.BLUE);
        if (player) {
            checkerColor = Color.BLACK;
        } else {
            checkerColor = Color.RED;
        }
        this.player = player;
    }
    
    public void setCheckerColor(Color c) {
        checkerColor = c;
    }
    
    @Override
    public boolean isOccupied() {
        return true;
    }

    public boolean isKing() {
        return false;
    }
    
    public Color getCheckerColor() {
        return checkerColor;
    }
    
   /**
    * this actually returns the CHECKER COLOR for checker objects
    * I know this could be a field but I found dynamic dispatch useful here
    */
    @Override
    public Color getColor() {
        return checkerColor;
    }
    
    public boolean getPlayer() {
        return player;
    }
    
    /**
     * this is the starting square
     * determines if a move is valid given the endsquare and other parameters
     */
    @Override
    public boolean isValidMoveNoJump(int startRow, int startCol, int endRow, 
            int endCol, Square endSquare, boolean currPlayer) {
        if (endSquare.isValidEndSpot() && this.isValidStartSpot(currPlayer)) {
            if ((getPlayer() && startRow + 1 == endRow 
                    && (startCol + 1 == endCol || startCol - 1 == endCol))
                 || (!getPlayer() && startRow - 1 == endRow 
                 && (startCol + 1 == endCol || startCol - 1 == endCol))) {
                return true;
            }
        }
        return false;
    }
    
    /*
     * returns true if the square belongs to the
     * players whose turn it is
     */
    @Override
    public boolean isValidStartSpot(boolean currPlayer) {
        if (currPlayer == getPlayer()) {
            return true;
        }
        return false;
    }
    
    @Override
    public boolean isValidEndSpot() {
        return false;
    }
    

    /**
     * given this as the start square, what are the jump possibilities?
     * if no jump possibilities, it will return an empty array
     * this array will return the ENDING squares that can be landed on
     * 
     */
    @Override
    public ArrayList<Square> jumpPossibilities(Square[][] board, boolean currPlayer) {
        ArrayList<Square> arr = new ArrayList<Square>();
        if (getPlayer() == currPlayer) {
            Square jumpSq1 = null;
            Square jumpSq2 = null;
            
            if (getRow() >= 0 && getRow() < 6 && getCol() >= 0 && getCol() < 6 && getPlayer()) {
                Square sq1 = board[(getRow() + 1)] [(getCol() + 1)];
                if (sq1 instanceof Checker) {
                    if (((Checker) sq1).getPlayer() == !getPlayer()) {
                        jumpSq1 = board[(getRow() + 2)] [(getCol() + 2)];
                    }
                }
            } 
            if (getRow() >= 0 && getRow() < 6 && getCol() > 1 && getCol() < 8 && getPlayer()) {
                Square sq2 = board[(getRow() + 1)] [(getCol() - 1)];
                if (sq2 instanceof Checker) {
                    if (((Checker) sq2).getPlayer() == !getPlayer()) {
                        jumpSq2 = board[(getRow() + 2)] [(getCol() - 2)];
                    }
                }
            } 
            if (getRow() > 1 && getRow() < 8 && getCol() >= 0 && getCol() < 6 && !getPlayer()) {
                Square sq1 = board[(getRow() - 1)] [(getCol() + 1)];
                if (sq1 instanceof Checker) {
                    if (((Checker) sq1).getPlayer() == !getPlayer()) {
                        jumpSq1 = board[(getRow() - 2)] [(getCol() + 2)];
                    }
                }
            } 
            if (getRow() > 1 && getRow() < 8 && getCol() > 1 && getCol() < 8 && !getPlayer()) {
                Square sq2 = board[(getRow() - 1)] [(getCol() - 1)];
                if (sq2 instanceof Checker) {
                    if (((Checker) sq2).getPlayer() == !getPlayer()) {
                        jumpSq2 = board[(getRow() - 2)] [(getCol() - 2)];
                    }
                }
            }
           
            if (jumpSq1 != null && !jumpSq1.isOccupied()) {
                arr.add(jumpSq1);
            }
           
            if (jumpSq2 != null && !jumpSq2.isOccupied()) {
                arr.add(jumpSq2);
            }
           
        }
        return arr;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(super.getColor());
        g.fillRect(super.getX(), super.getY(), super.getThickness(), super.getThickness());
        g.setColor(checkerColor);
        g.fillOval(super.getX() + 5, super.getY() + 5, RADIUS, RADIUS);
    }
    
    @Override
    public String toString() {
        if (getColor().equals(Color.RED)) {
            return "3";
        } else {
            return "2";
        }
    }
    
}
