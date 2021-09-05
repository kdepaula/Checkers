    
public class CheckerGame {
    
    private boolean player1;
    private int numRedPieces = 12; //player 2 is red
    private int numBlackPieces = 12; //player 1 is black

    public CheckerGame() {
        reset();
    }

        /**
         * checkWinner checks whether the game has reached a win 
         * condition. checkWinner only looks for horizontal wins.
         * 
         * @return 0 if nobody has won yet, 1 if player 1 has won, and
         *            2 if player 2 has won, 3 if the game hits stalemate
         */
    public int checkWinner() {
        if (numRedPieces == 0) {
            return 2;
        } else if (numBlackPieces == 0) {
            return 1;
        }
        return 0;
    }
        
    public void decBlackPieces() {
        numBlackPieces = numBlackPieces - 1;
    }
        
    public void decRedPieces() {
        numRedPieces = numRedPieces - 1;
    }
        
    public void incBlackPieces() {
        numBlackPieces += numBlackPieces;
    }
        
    public void incRedPieces() {
        numRedPieces += numRedPieces;
    }
        
    public int getRedPieces() {
        return numRedPieces;
    }
        
    public int getBlackPieces() {
        return numBlackPieces;
    }
        
    public void setBlackPieces(int num) {
        numBlackPieces = num;
    }
        
    public void setRedPieces(int num) {
        numRedPieces = num;
    }
        
        /**
         * reset (re-)sets the game state to start a new game.
         */
    public void reset() {
        player1 = true;
        numRedPieces = 12;
        numBlackPieces = 12;
    }
        
        /**
         * getCurrentPlayer is a getter for the player
         * whose turn it is in the game.
         * 
         * @return true if it's Player 1's turn,
         * false if it's Player 2's turn.
         * 
         * player 1 is black checkers
         * player 2 is red checkers
         */
    public boolean getCurrentPlayer() {
        return player1;
    }
        
    public void changePlayer() {
        player1 = !player1;
    }
        
    public void setPlayer(boolean set) {
        player1 = set;
    }
        
        
        /**
         * This main method illustrates how the model is
         * completely independent of the view and
         * controller.  We can play the game from start 
         * to finish without ever creating a Java Swing 
         * object.
         * 
         * This is modularity in action, and modularity 
         * is the bedrock of the  Model-View-Controller
         * design framework.
         * 
         * Run this file to see the output of this
         * method in your console.
         */
    public static void main(String[] args) {
        
    }


}
