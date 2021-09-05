# Checkers

CIS 120 Game Project

# =: Core Concepts :=

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. Subtyping. I made an overall square class that extends JComponent. Checker extends Square. KingChecker extends Checker. 
  Dynamic dispatch is present in my implementation of the subtyping core concept. 
  The "jumpPossibilities" method is a crucial method in the squares classes. The Jump Possibilities determines all the possible
  squares where one could make a jump given the current arrangement of the board. If no jumps are possible it returns an empty array. 
  Otherwise, it returns an array with the valid squares. Dynamic dispatch was employed because the Square, Checker, and King Checker
  classes all had different functions. For instance, the square class always returned an empty array because a square
  cannot make jumps. The Checker class checked for jumps in the "forward" direction of the given player. However,
  the king checker classes looked in all 4 possible directions for the jump. This is because king checkers have different move
  possibilities so they needed a distinct action from the checker class. This method was extremely useful when it came
  to writing a method in game board called "jump exists", which returned an array of all the squares on the board
  that could make a jump at any given time. In the jump exists method, I used the jump possibilities method on every square
  in the checker board. This is where dynamic dispatch came in. Even though all of the objects were squares and had a jump possibilities
  method, the method that was actually run depended on the dynamic type of the object in the board.
  Overall, this component was essential to my game because jumps in checkers are forced, as in, if there is a jump
  available you must take it.

  2. Arrays - The checker board is a 2D array of squares. This makes sense because a game board in real life is an array of squares.
  Some of the squares are checker pieces, some are blank squares, and some are king checker pieces.
  This was appropriate for my design because a game board is similar to an array. There are distinct squares that may
  or may not contain game pieces. I considered making an array of another type such as an array of integers, but ultimately 
  an array of squares best represents the checkers game because there is no need to assign arbitrary numbers to represent squares.

  3. Collections - I used a LinkedList to stores the previous moves. This allowed me to create an undo
  button so that the user could undo a move that is a mistake. I stored an array of integers in the LinkedList.
  A collection was great in this case because you can undo an unlimited number of moves. An array only has a finite set 
  of possible space so you could not undo unlimited moves. 
  The array of integers in the Linked List
  contained the information that I would need to undo the move including the starting column, 
  ending column, starting row, ending row, player who made the move, and type of the deleted checker (if a 
  jump occured during the move). I used int arrays in the collection because I only needed a set amount of information for each move
  to make the undo occur. 
  A Linked List was the best type of collection to use because I was able to add
  moves to the beginning of the list, and I removed moves from the head of the list after the undo buton was pressed.
  A set would not make sense here because there can be repeat moves in checkers. A map is not as good either because 
  there are not values that go together in a way that makes sense. The collection is encapsulation properly because 
  it is a private field and you cannot access it outside of the Game Board class directly. You can only undoMoves()
  using that public method, save a game, and open a saved game.

  4. File I/O - I wrote to a file to save the state of the game. This is saved in "save.checkers".
  The first line of the file is the saved moves from the previous game. Each move is separated by a semicolon.
  Within each move, information is separated by spaces. The first 4 numbers of the move indicate the initial and final
  rows and columns that were moved. The 5th number indicates the player at the time, and the 6th number indicates whether 
  a king checker or regular checker pieces was jumped over if applicable. 
  
  The 2nd to 9th lines of the file save the state of the board. It can be seen if you open "save.checkers". 
  The 0s are white squares, 1s are blue squares, 2s are black checkers, 3s are red checkers, 4s are black king checkers,
  and 5s are red king checkers.
  
  The 10th line of the file saves the player whos turn it was when the game ended. True means it was player 1 and false
  means it was player 2. 
  
  You can open a saved game any point in time using the "open saved game button". This then reads from the file to return the game
  back to the saved state. Initially, I was only going to save the state of the board. However, in my feedback, I was told that I needed to save
  two different states. Therefore, I also chose to save the turn of the player. This made for two separate saved states. 
  Furthermore, I also chose to save the moves from the saved game so you can undo moves from the saved game. This adds
  up to three separate states being written to and read from the file. 

# =: Your Implementation :=

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
My Square class extends JComponent. A square is a filled in rectangle on the board. 
The Checker class extends Square. A checker is a piece on the board. It is a filled in ellipse on top of a filled in rectangle.
A checker piece can only move on the diagonals (the blue squares). Also, it can only move in the "forward" direction relative to
where it starts.
The KingChecker class extends Checker. A king checker inherits all the methods of the checker. However, a king checker can move backwards
and forwards relative to its starting position on the board. 
The gameboard is the board. This is where the action listeners are. Also, this is what paints the components on to the screen.
The checker game has some overall layouts such as the number of pieces and check for winner. This is functionally equivalent to the original
tic tac toe game board class.
Game is where I actually make the JFrame. This is more or less unchanged from the tic tac toe game code. I added a save game, open saved game, 
and undo button.


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  I was trying really hard to make sure I did subtyping properly so it took me a long to plan out all the methods I was going to make
  in the checkers and king checkers classes. This was my primary challenge. It ended up working out well. 
  I probably could have found a way to make checkers without a class for checker pieces. This was one of my initial difficulties because
  making a checkers class meant I had to move the pieces rather than just keep the pieces stagnant and change the color of the ellipse. 
  But, in the end it actually worked well, and I am glad I chose to make checkers objects.
  Also, File I/O is hard so I had a hard time saving the game. This difficulty didn't have anything to do with my game design and mainly 
  had to do with the fact that I personally find File I/O to be a more difficult concept. Also, I spent a really long time
  trying to figure out why my save game was not working only to realize that I was using == instead of .equals on strings.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
  
  There is some separation of functionality. The board game deals with objects on the board. The Game class
  deals with adding objects to the JFrame. The Squares classes deal with moves associated to a single object.
  The file parser and iterator deals with reading and writing files. 
  
  I made a concrete effort to make things encapsulated.
  For instance, I moved the board variable into the gameboard class. Originally, it was in the checkergame class
  as a leftover part of the tic tac toe game template. It also has a getBoard() method originally in the CheckerGame class.
  However, I moved the board variable and the getBoard method to the gameboard class so that I could use the actual board rather than
  break encapsulation by getting the board from the checker game class. I also made sure to return a clone of the board
  in the getboard method so that I did not break encapsulation.
  Also, the moves collection is a private field that is encapsulated. It is changed through the record move and undo move
  buttons. The undo moves button is accessed in the Game class.
  However, encapsulation is not broken because
  you cannot modify the collection directly and break the game.
  
  I would refactor the jumpPossibilities() methods because they are sort of repetitive. Also, I would make more helper functions to make
  the action listener in the board less chaotic looking. I could probably have combined it into some sort of other method. 

# =: External Resources :=

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
  
  https://ctycms.com/mn-rochester/docs/checkers-instructions.pdf

  https://stackoverflow.com/questions/9510099/moving-objects-around-in-a-multi-dimensional-array
  
  https://stackoverflow.com/questions/685521/multiline-text-in-jlabel#:~:text=You%20can%20use%20JTextArea%20and,normal%20read%2Donly%20multiline%20text.&text=It%20is%20possible%20to%20use%20(basic)%20CSS%20in%20the%20HTML.
  
