/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 * 
 * This game adheres to a Model-View-Controller design framework.  This
 * framework is very effective for turn-based games.  We STRONGLY 
 * recommend you review these lecture slides, starting at slide 8, 
 * for more details on Model-View-Controller:  
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, Game initializes the view,
 * implements a bit of controller functionality through the reset
 * button, and then instantiates a GameBoard.  The GameBoard will
 * handle the rest of the game's view and controller functionality, and
 * it will instantiate a TicTacToe object to serve as the game's model.
 */
public class Game implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Draughts");
        frame.setLocation(100, 100);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // Game board
        final GameBoard board = new GameBoard(status);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we define it as an
        // anonymous inner class that is an instance of ActionListener with its actionPerformed()
        // method overridden. When the button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.reset();
            }
        });
        control_panel.add(reset);
        
        //add save game button to control panel.
//it writes the current state to a file
//add button "open saved game" will open the game state the
//way it was when you saved
        
        final JButton save = new JButton("Save Game");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.saveGame();
            }
        });
        control_panel.add(save);
        
        final JButton open = new JButton("Open Saved Game");
        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.openSavedGame();
            }
        });
        control_panel.add(open);
        
        final JButton undo = new JButton("Undo Move");
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.undoMove();
            }
        });
        control_panel.add(undo);
        
        final JTextArea textArea = new JTextArea("Checkers Game Rules:\n"
                + "The opponent with the darker pieces moves first.\n "
                + "In this case, the black checkers will go first.\n"
                + "Pieces must stay on the dark squares.\n "
                + "In this case, the checkers must stay on the blue squares.\n"
                + "To move a piece, click and drag on the piece you would like to move.\n"
                + "Unless you are capturing an opponent, checker pieces"
                + " can only move one diagonal space \n"
                + "forward relative to their starting position.\n"
                + "To capture an opposing piece, jump over it by moving two diagonal spaces \n"
                + "in the direction of the opposing piece.\n"
                + "The space on the other side of your opponent's piece must be empty \n"
                + "for you to make a jump. \n"
                + "You can make multiple jump, meaning you can jump"
                + " over multiple of your opponent's piece.\n "
                + "To do this, you move your piece once over the opponent's first piece.\n"
                + "Then, move again over the second piece of your opponent.\n"
                + "Jumps are forced.\nThis means that if any type of jump is available "
                + "you must take it.\n"
                + "You may only move your pieces when it is your turn.\n"
                + "The turn is indicated at the bottom of the screen.\n"
                + "A turn ends with a piece moves one space forward or\n"
                + "when any one type of jump is fully completed.\n"
                + "If your piece reaches the last row on your opponent's side, \n"
                + "it becomes a King Piece. \n"
                + "King Pieces are indicated with a K. \n"
                + "King pieces may move one space forward or backward. \n"
                + "King Pieces can move forward and backward when capturing an opponent.\n"
                + "There is no limit to how many king pieces a player may have.\n"
                + "To win the game, you must capture all of your opponent's pieces. \n"
                + "When you win the game, it will be displayed on the bottom of the screen.\n"
                + "To start a new game, hit reset.\n"
                + "To save a game, hit save game.\n"
                + "You may open this game at any point in time later.\n"
                + "Whoever's turn it was when the game was saved \n"
                + "will have their turn when the game is reopened.\n"
                + "To undo a move, hit undo move. \n"
                + "If you open a saved game, you can undo the moves \n"
                + "that were played in the saved game."
               );
        textArea.setEditable(false);
        
        final JPanel directionsPanel = new JPanel();
        directionsPanel.add(textArea);
        frame.add(directionsPanel, BorderLayout.EAST);
        
        //add undo button to the control panel
        

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}