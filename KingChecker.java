import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class KingChecker extends Checker {

    public KingChecker(int row, int col, boolean player) {
        super(row, col, player);
    }
    
    @Override
    public boolean isKing() {
        return true;
    }
    
    /**
     * the king checker can jump along diagonals forwards AND backwards
     * this is why dynamic dispatch is necessary
     * this is an ESSENTIAL METHOD
     * I use this in the game board class on all the squares on the board in jumpExits
     */
    @Override
    public ArrayList<Square> jumpPossibilities(Square[][] board, boolean currPlayer) {
        ArrayList<Square> arr = new ArrayList<Square>();
        if (getPlayer() == currPlayer) {
            Square jumpSq1 = null;
            Square jumpSq2 = null;
            Square jumpSq3 = null;
            Square jumpSq4 = null;
            
            if (getRow() >= 0 && getRow() < 6 && getCol() >= 0 && getCol() < 6) {
                Square sq1 = board[(getRow() + 1)] [(getCol() + 1)];
                if (sq1 instanceof Checker) {
                    if (((Checker) sq1).getPlayer() == !getPlayer()) {
                        jumpSq1 = board[(getRow() + 2)] [(getCol() + 2)];
                    }
                }
            } 
            if (getRow() >= 0 && getRow() < 6 && getCol() > 1 && getCol() < 8) {
                Square sq2 = board[(getRow() + 1)] [(getCol() - 1)];
                if (sq2 instanceof Checker) {
                    if (((Checker) sq2).getPlayer() == !getPlayer()) {
                        jumpSq2 = board[(getRow() + 2)] [(getCol() - 2)];
                    }
                }
            } 
            if (getRow() > 1 && getRow() < 8 && getCol() >= 0 && getCol() < 6) {
                Square sq3 = board[(getRow() - 1)] [(getCol() + 1)];
                if (sq3 instanceof Checker) {
                    if (((Checker) sq3).getPlayer() == !getPlayer()) {
                        jumpSq3 = board[(getRow() - 2)] [(getCol() + 2)];
                    }
                }
            } 
            if (getRow() > 1 && getRow() < 8 && getCol() > 1 && getCol() < 8) {
                Square sq4 = board[(getRow() - 1)] [(getCol() - 1)];
                if (sq4 instanceof Checker) {
                    if (((Checker) sq4).getPlayer() == !getPlayer()) {
                        jumpSq4 = board[(getRow() - 2)] [(getCol() - 2)];
                    }
                }
            }
           
            if (jumpSq1 != null && !jumpSq1.isOccupied()) {
                arr.add(jumpSq1);
            }
           
            if (jumpSq2 != null && !jumpSq2.isOccupied()) {
                arr.add(jumpSq2);
            }
           
            if (jumpSq3 != null && !jumpSq3.isOccupied()) {
                arr.add(jumpSq3);
            }
           
            if (jumpSq4 != null && !jumpSq4.isOccupied()) {
                arr.add(jumpSq4);
            }
           
        }
        return arr;
    }
    
    /**
     * this is also another instance of dynamic dispatch
     * king checkers can move forwards AND backwards along diagonals
     * they can only move one space though without jumping
     */
    @Override
    public boolean isValidMoveNoJump(int startRow, int startCol, int endRow, int endCol, 
            Square endSquare, boolean currPlayer) {
        if (endSquare.isValidEndSpot() && this.isValidStartSpot(currPlayer)) {
            if ((startRow + 1 == endRow || startRow - 1 == endRow) 
                    && (startCol + 1 == endCol || startCol - 1 == endCol)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(super.getX(), super.getY(), super.getThickness(), super.getThickness());
        g.setColor(super.getCheckerColor());
        g.fillOval(super.getX() + 5, super.getY() + 5, RADIUS, RADIUS);
        g.setColor(Color.WHITE);
        g.drawLine(super.getX() + 30, super.getY() + 20, super.getX() + 30, super.getY() + 60);
        g.drawLine(super.getX() + 30, super.getY() + 40, super.getX() + 50, super.getY() + 60);
        g.drawLine(super.getX() + 30, super.getY() + 40, super.getX() + 50, super.getY() + 20);
    }
    
    @Override
    public String toString() {
        if (this.getColor().equals(Color.BLACK)) {
            return "4";
        } else {
            return "5";
        }
    }
    
}
