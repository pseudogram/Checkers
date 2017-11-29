package Model;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Stack;

/**
 * This class is represents a checkers board. It handles the representation of the game and
 * ensures that rules of the game are followed during play.
 * <p>
 * The {@link CheckersBoard} class contains a successor function that checks for all the valid moves
 * in the current state.
 * <p>
 * <p>
 * Implementation:
 * - The board is represented as an {@link Piece} array of size 64.
 * - It holds 2 Players, that can be either of type {@link Human} or {@link Robot}.
 * - To identify the current player, a boolean is used. If the it is Player 1's turn the boolean
 * is set to true.
 * - ... extraJump
 *
 * Search:
 * -<b>ERROR</b> to find incorrect implementations,
 * -<b>UPDATE</b> for implementations that can be improved.
 * -<b>DELETE</b> for implementations that can be removed.
 */
public class CheckersBoard {

	/**
	 * Used by the Model objects to print debugging statements if of a high enough importance.
	 * <p>
	 * Grades:
	 * 0	= Top level debug statements. Always print.
	 * 1	= Basic
	 * 2	= Lower
	 * 3	= Very Low
	 * 4	= Print Recursions
	 */
	static int DEBUG = 0;

	/**
	 * Holds the {@link Piece} 's in the boards current state
	 */
	private Piece[] board = new Piece[64];

	/**
	 * Player 1
	 */
	private Player playerOne;

	/**
	 * Player 2
	 */
	private Player playerTwo;

	/**
	 * True if it is Player 1's turn in the games current state
	 */
	private boolean playerOneTurn;

	/**
	 * If a capturing move has taken place, and there is a another valid capture move possible,
	 * this {@link ArrayList} holds all of the valid moves in {@link Turn} objects.
	 */
	private Optional<ArrayList<Turn>> extraJump; // Possibly use instead:

	/**
	 * Keeps a {@link Stack} of all the executed {@link Turn}s.
	 */
	private Stack<Turn> history;

	// ===============================================================================================
	//
	//                                        Setup the board (Constructors)
	//
	// ===============================================================================================

	/**
	 * Player 1 always starts at the top of the  board. Player 2 at the bottom.
	 *
	 * @param playerOneStart boolean - true if Player 1 is to start the game
	 * @param playerOne Player - the Player 1 Object
	 * @param playerTwo Player - the Player 2 Object
	 */
	public CheckersBoard(boolean playerOneStart, Player playerOne, Player playerTwo) {
		if(DEBUG>0) System.out.println("CheckersBoard.addPlayers: Setting up board with P1 & P2");

		playerOneTurn = playerOneStart;
		history = new Stack<>();
		addPlayers(playerOne,playerTwo);
		extraJump = Optional.empty();
	}

	/**
	 * Player 1 always starts at the top of the  board. Player 2 at the bottom.
	 * <p>
	 * If this constructor is used, Players must be added using {@link #addPlayers}
	 * before the game starts.
	 *
	 * @param playerOneStart true if Player 1 is to start the game
	 */
	public CheckersBoard(boolean playerOneStart) {
		playerOneTurn = playerOneStart;
		history = new Stack<>();
		extraJump = Optional.empty();
	}

	public void addPlayers(Player one, Player two) {
		this.playerOne = one;
		this.playerTwo = two;

		if(DEBUG>0) System.out.println("CheckersBoard.addPlayers: Setting up board with P1 & P2");


		int loc = 0;

		// Lay Player 1 Pieces at top
		for (int i = 0; i < 12; i++) {
			Piece piece = new Piece(true, board2pos(loc));
			playerOne.addPiece(piece);
			board[loc] = piece;
			if (i == 3) loc += 1;
			if (i == 7) loc -= 1;
			loc += 2;
		}

		loc = 63;
		// Lay Player 2 Pieces
		for (int i = 0; i < 12; i++) {
			Piece piece = new Piece(false, board2pos(loc));
			playerTwo.addPiece(piece);
			board[loc] = piece;
			if (i == 3) loc -= 1;
			if (i == 7) loc += 1;
			loc -= 2;
		}
	}


	// ===============================================================================================
	//
	//                              			Getters and Setters
	//
	// ===============================================================================================

	/**
	 * Returns the Player 1 Object.
	 *
	 * @return {@link Player} Player 1 Object
	 */
	Player getPlayerOne() {
		return playerOne;
	}

	/**
	 * Returns the Player 1 Object.
	 *
	 * @return {@link Player} Player 1 Object
	 */
	Player getPlayerTwo() {
		return playerTwo;
	}


	/**
	 * Returns true if it is Player 1's turn int the games current state.
	 *
	 * @return {@link boolean} true if Player 1's turn.
	 */
	public boolean isPlayerOneTurn() {
		return playerOneTurn;
	}

	// =================================================================================================================
	//
	//                                          Coordinate translations
	//
	// =================================================================================================================

	/**
	 * Converts cartesian coordinates to an integer position in the board array.
	 *
	 * @param pos - a {@link Position} object
	 * @return {@link Integer} position corresponding to position in board array
	 */
	static int pos2board(Position pos) {
		return pos.x + pos.y * 8;
	}

	/**
	 * ERROR -  does not consider wrapping of board!
	 *
	 * Converts cartesian vector the integer position  the board
	 *
	 * @param step - a {@link Step} enum
	 * @return {@link Integer} position corresponding to position in board array
	 */
	static int pos2board(Step step) {
		return step.x + step.y * 8;
	}

	/**
	 * Converts an integer position in the board array to cartesian coordinates.
	 *
	 * @param pos - n integer position in bored array
	 * @return {@link Position} corresponding to a point on board
	 */
	private static Position board2pos(int pos) {
		int x = pos % 8;
		int y = (pos - x) / 8;
		return new Position(x, y);
	}


	// =================================================================================================================
	//
	//                                          Add / Remove / Move pieces
	//
	// =================================================================================================================

	/**
	 * Returns true if the space given in the board is taken by a piece.
	 *
	 * @param pos - a {@link Position} on the board
	 * @return {@link boolean}, true if the corresponding {@link Position} in the board array is empty
	 */
	public boolean isFree(Position pos) {
		return board[pos2board(pos)] == null;
	}

	/**
	 * Returns true if the space given in the board is taken by a piece.
	 *
	 * @param pos - a {@link Integer} position in the board array
	 * @return boolean, true if the {@link Integer} in the board array is empty
	 */
	private boolean isFree(int pos) {
		return board[pos] == null;
	}

	/**
	 * Adds a piece to the board and adds the piece to the corresponding {@link Player} object so
	 * it can track the piece
	 *
	 * @param isPlayerOne - true if the the piece is player one.
	 * @param pos - pos a {@link Integer} position in the board array
	 */
	private void addNewPiece(boolean isPlayerOne, int pos) {
		Piece piece = new Piece(isPlayerOne, board2pos(pos));
		if (isPlayerOne) {
			playerOne.addPiece(piece);
		} else {
			playerTwo.addPiece(piece);
		}
		board[pos] = piece;
	}

	/**
	 * DELETE
	 * @param isPlayerOne DELETE
	 * @param pos DELETE
	 */
	public void addPiece(boolean isPlayerOne, Position pos) {
		Piece piece = new Piece(isPlayerOne, pos);
		if (isPlayerOne) {
			playerOne.addPiece(piece);
		} else {
			playerTwo.addPiece(piece);
		}
		board[pos2board(pos)] = piece;
	}

	/**
	 * Used to add a piece that may have previously been removed from the board.
	 * <p>
	 * Checks board space is empty. Before placing
	 *
	 * @param piece - a {@link Player}'s {@link Piece}
	 */
	public void addPiece(Piece piece) {
		Position pos = piece.getPos();
		if (isFree(pos)) {
			board[pos2board(pos)] = piece;
			if (piece.isPlayerOne) {
				playerOne.addPiece(piece);
			} else {
				playerTwo.addPiece(piece);
			}
		} else {
			throw new RuntimeException("Attempting to add piece in an occupied space");
		}
	}

	/**
	 * Removes a piece from the checkers board, and the appropriate players hand and returns it.
	 *
	 * @param pos - a {@link Position} on the board
	 * @return {@link Piece} the Piece found at the position
	 */
	public Piece removePiece(Position pos) {

		Piece piece = board[pos2board(pos)];
		if (piece.isPlayerOne) {
			playerOne.removePiece(piece);
		} else {
			playerTwo.removePiece(piece);
		}
		board[pos2board(pos)] = null;
		return piece;
	}
//
//	/**
//	 * DELETE - Not actually used
//	 * UPDATE - Replace nulls with optionals
//	 *
//	 * Returns the step made if it is legal. Else returns null
//	 *
//	 * @param from
//	 * @param to
//	 * @return
//	 */
//	private Step getStep(Piece piece, Position from, Position to) {
//		if(DEBUG>3) System.out.printf("CheckersBoard.getStep: Attempting to move from %s -> %s",
//				from.toString(), to.toString());
//
//		Step moveMade = null;
//
//		// Determine step made
//		// Check move is a legal move
//		int x = to.x - from.x;
//		int y = to.y - from.y;
//
//		for (Step step : Step.values()) {
//			if (step.x == x && step.y == y) {
//				moveMade = step;
//				break;
//			}
//		}
//
//		// Not a move within the scope of possible moves
//		if (moveMade == null) return null;
//
//		if (piece.isKing()) return moveMade;
//
//		// possible player 1 moves
//		if (playerOneTurn) {
//			switch (moveMade) {
//				case TAKEDOWNLEFT:
//					return moveMade;
//				case TAKEDOWNRIGHT:
//					return moveMade;
//				case DOWNLEFT:
//					return moveMade;
//				case DOWNRIGHT:
//					return moveMade;
//
//				default:
//					return null;
//			}
//		} else {
//			switch (moveMade) {
//				case TAKEUPLEFT:
//					return moveMade;
//				case TAKEUPRIGHT:
//					return moveMade;
//				case UPLEFT:
//					return moveMade;
//				case UPRIGHT:
//					return moveMade;
//
//				default:
//					return null;
//			}
//		}
//	}

//
//	/**
//	 * DELETE
//	 *
//	 * Returns the piece to take if it is valid. Otherwise not.
//	 *
//	 * @param from
//	 * @param step
//	 * @return
//	 */
//	private Piece validTake(Position from, Step step) {
//
//		int takenPos = 0;
//		System.out.println(takenPos);
//		takenPos += pos2board(from);
//		System.out.println(takenPos);
//		takenPos += pos2board(step);
//		System.out.println(takenPos);
//
//		if (isFree(takenPos)) {
//			throw new RuntimeException(String.format("Trying to jump and empty space %s", takenPos));
//		} else {
//			if (board[takenPos].isPlayerOne == playerOneTurn) {
//				throw new RuntimeException("Trying to jump players own piece");
//			}
//			return board[takenPos];
//		}
//	}

	/**
	 * Used by the CheckersBoard.validStep function.
	 *
	 * When applying a step to a piece, this method checks:
	 * - that the space moving from and to are within bounds.
	 * - the space a piece being moved to on the board is empty.
	 * - jumping a piece the piece is the opposing player.
	 *
	 * Used by the function Valid step
	 *
	 * @param piece - a {@link Player}'s {@link Piece}
	 * @param step - a {@link Step} enum
	 * @return
	 */
	private boolean checkStep(Piece piece, Step step) {
		// Get before and after the Step made
		Position _from = piece.getPos();
		Position _to = new Position(_from);
		_to.add(step);

		// Check within bounds
		if (DEBUG > 3) System.out.printf("CheckersBoard.checkStep: Checking move in bounds %s --> %s\n",
				_from.toString(), _to.toString());
		if (_from.x < 0 || 7 < _from.x || _from.y < 0 || 7 < _from.y  // Check `from` within bounds
				|| _to.x < 0 || 7 < _to.x || _to.y < 0 || 7 < _to.y) {  // Check `to` within bounds
			return false;
		}

		// Convert to Positions to integers to index the board
		if (DEBUG > 3) System.out.println("CheckersBoard.checkStep: Converting Positions to ints");
		int from = pos2board(_from);
		int to = from + pos2board(step);
		int opponent;

		if (DEBUG > 3)
			System.out.printf("CheckersBoard.checkStep: Checking space %s is free & and if valid " +
					"capture move that jumped piece is opposing player.\n", _to.toString());
		switch (step) {
			// to space free
			case DOWNLEFT:
			case DOWNRIGHT:
			case UPLEFT:
			case UPRIGHT:
				if (isFree(to)) return true;
				break;

			// Check jumping an opposing player
			case TAKEDOWNLEFT:
				opponent = from + pos2board(Step.DOWNLEFT);
				if (!isFree(opponent)) {
					if (isFree(to) && board[opponent].isPlayerOne != piece.isPlayerOne) return true;
				}
				break;
			case TAKEDOWNRIGHT:
				opponent = from + pos2board(Step.DOWNRIGHT);
				if (!isFree(opponent)) {
					if (isFree(to) && board[opponent].isPlayerOne != piece.isPlayerOne) return true;
				}
				break;
			case TAKEUPRIGHT:
				opponent = from + pos2board(Step.UPRIGHT);
				if (!isFree(opponent)) {
					if (isFree(to) && board[opponent].isPlayerOne != piece.isPlayerOne) return true;
				}
				break;
			case TAKEUPLEFT:
				opponent = from + pos2board(Step.UPLEFT);
				if (!isFree(opponent)) {
					if (isFree(to) && board[opponent].isPlayerOne != piece.isPlayerOne) return true;
				}
				break;

		}

		return false;

	}

	/**
	 * Returns <b>true</b> if the Step a piece is making is a valid for that piece. It does not
	 * implement any rules of the game, other than ensuring a piece makes the correct move for its
	 * current state.
	 *
	 * Eg.
	 *  - Kings can move in any direction
	 * 	- Player 1 men can only move downwards, Player 2 men can only move upwards
	 * 	- If caputuring, a piece must be jumping an opposing player.
	 *
	 * @param piece - a {@link Player}'s {@link Piece}
	 * @param step - a {@link Step} enum
	 * @return {@link boolean}, true if the piece can make the step
	 */
	private boolean validStep(Piece piece, Step step) {
		// If it is not a king
		if (!piece.isKing()) {
			// if it is a player one piece
			if (piece.isPlayerOne) {
				switch (step) {
					case TAKEDOWNLEFT:
					case TAKEDOWNRIGHT:
					case DOWNLEFT:
					case DOWNRIGHT:
						if (DEBUG > 3)
							System.out.println("CheckersBoard.validStep: Checking valid Player 1 move.");
						return checkStep(piece, step);
					default:
						return false;
				}
				// If it is a player two piece
			} else {
				switch (step) {
					case TAKEUPLEFT:
					case TAKEUPRIGHT:
					case UPRIGHT:
					case UPLEFT:
						if (DEBUG > 3)
							System.out.println("ChekersBoard.validStep: Checking valid Player 2 move.");
						return checkStep(piece, step);
					default:
						return false;
				}
			}
			// If the piece is a king
		} else {
			if (DEBUG > 3) System.out.println("CheckersBoard.validStep: Checking valid King move.");
			return checkStep(piece, step);
		}
	}


	public Stack<Turn> getHistory() {
		return history;
	}

	/**
	 * Returns an {@link ArrayList} of all the valid Turns that can be made in the current state.
	 * If the last player to move performed a capture move, and the there is one or multiple more
	 * capture moves available, the function returns the possible moves.
	 *
	 * @return {@link ArrayList}&lt;{@link Turn}&gt; corresponding to all possible moves that can
	 * be made in the current state.
	 */
	public ArrayList<Turn> successor() {

		// If there is another possible move for the last piece that moved. Move it.
		if (extraJump.isPresent()) {
			if (DEBUG > 1)
				System.out.printf("CheckersBoard.successor: Player %s last moved a piece that can make a " +
						"another jump.\n", isPlayerOneTurn() ? 1 : 2);
			return extraJump.get();
		}

		// Set player to the player who's turn it is currently
		Player player = isPlayerOneTurn() ? getPlayerOne() : getPlayerTwo();

		Stack<Piece> pieces = player.getPiecesStack();
		if (DEBUG > 3)
			System.out.printf("CheckersBoard.successor: There are a total of %s pieces in the the " +
							"player %s HashMap\n", pieces.size(), isPlayerOneTurn() ? 1 : 2);

		ArrayList<Turn> moves = new ArrayList<>();

		if (DEBUG > 3) System.out.println("CheckersBoard.successor: Searching for Mandatory moves");
		// Check all pieces for mandatory moves (captures) moves adding them to the list.
		for (Piece piece : pieces) {
			if (validStep(piece, Step.TAKEDOWNLEFT)) moves.add(new Turn(piece, Step.TAKEDOWNLEFT));
			if (validStep(piece, Step.TAKEDOWNRIGHT)) moves.add(new Turn(piece, Step.TAKEDOWNRIGHT));
			if (validStep(piece, Step.TAKEUPRIGHT)) moves.add(new Turn(piece, Step.TAKEUPRIGHT));
			if (validStep(piece, Step.TAKEUPLEFT)) moves.add(new Turn(piece, Step.TAKEUPLEFT));
		}

		if (DEBUG > 3) System.out.println("CheckersBoard.successor: Searching for Mandatory moves");
		// If there are no mandatory moves search for ordinary moves.
		if (moves.size() == 0) {
			for (Piece piece : pieces) {
				if (validStep(piece, Step.DOWNLEFT)) moves.add(new Turn(piece, Step.DOWNLEFT));
				if (validStep(piece, Step.DOWNRIGHT)) moves.add(new Turn(piece, Step.DOWNRIGHT));
				if (validStep(piece, Step.UPRIGHT)) moves.add(new Turn(piece, Step.UPRIGHT));
				if (validStep(piece, Step.UPLEFT)) moves.add(new Turn(piece, Step.UPLEFT));
			}
		}

		// return the moves
		return moves;
	}

	/**
	 * Executes a {@link Turn} object moving the Piece in it. Ensure that you pass a turn object that
	 * has had the {@link Piece} and {@link Position}s from & to (or the Step) validated.
	 *
	 * It also updates a {@link Turn} object and adds it to the {@link #history} stack.
	 *
	 * After the {@link Turn} hs
	 * Returns null if there is no mandatory second jumps
	 *
	 * @param turn
	 * @return
	 */
	public ArrayList<Turn> executeTurn(Turn turn) {
		history.push(turn);

		if(DEBUG > 2) System.out.printf("CheckersBoard.executeTurn: Moving piece from %s -> %s\n",
				turn.getFrom(), turn.getTo());

		// move the piece:
		// - in the piece state
		// - on the board
		// - in the player HashMap
		Piece ally = removePiece(turn.getFrom());
		ally.move(turn.getTo());
		addPiece(ally);

		// If the piece has crossed the board crown it and end the turn there.
		if (crossedBoard(ally, turn.getTo())) {
			if(DEBUG>3) System.out.printf("executeTurn: Crowning King at Pos %s\n", turn.toString());
			turn.setCrowned(true);;
		}

		// If piece taken. Save the piece in the Turn object
		Position enemyPos = new Position(turn.getFrom());

		// Save piece taken and remove from board
		switch (turn.getStep()) {
			case TAKEDOWNRIGHT:
				enemyPos.add(Step.DOWNRIGHT);
				turn.setTaken(removePiece(enemyPos));
				break;
			case TAKEDOWNLEFT:
				enemyPos.add(Step.DOWNLEFT);
				turn.setTaken(removePiece(enemyPos));
				break;
			case TAKEUPLEFT:
				enemyPos.add(Step.UPLEFT);
				turn.setTaken(removePiece(enemyPos));
				break;
			case TAKEUPRIGHT:
				enemyPos.add(Step.UPRIGHT);
				turn.setTaken(removePiece(enemyPos));
				break;
			default:
				// Standard move so end the turn there and swap players
				turn.setChangePlayer(true);
				swapPlayer();
				return null;
		}


		ArrayList<Turn> jumpAgain = new ArrayList<>();

		// if piece can take return possible moves
		if (validStep(ally, Step.TAKEDOWNRIGHT)) jumpAgain.add(new Turn(ally, Step.TAKEDOWNRIGHT));
		if (validStep(ally, Step.TAKEDOWNLEFT)) jumpAgain.add(new Turn(ally, Step.TAKEDOWNLEFT));
		if (validStep(ally, Step.TAKEUPLEFT)) jumpAgain.add(new Turn(ally, Step.TAKEUPLEFT));
		if (validStep(ally, Step.TAKEUPRIGHT)) jumpAgain.add(new Turn(ally, Step.TAKEUPRIGHT));

		// if there player changes from turn to turn
		turn.setChangePlayer( jumpAgain.size() == 0);

		if (jumpAgain.size() > 0) {
			turn.setChangePlayer(false); // Change player is true by default
			turn.setExtraJumps(jumpAgain); // Add the extra jumps to the turn object
			return jumpAgain; // return the extra jumps
		}
		else{
			if(DEBUG>-1) System.out.println("CheckersBoard.executeTurn: this shouldn't be executed");
			swapPlayer();
			return null;
		}
	}


//    /**
//     * Check its the right players turn
//     *
//     * Only move to a free space.
//     * Only move a piece
//     *
//     * @param from
//     * @param to
//     * @return boolean if player1 turn next
//     */
//    public Turn makeTurn(Position from, Position to){
//
//        boolean legal = true;
//
//        // positions in the board within bounds
//        if (!(0 <= from.x && from.x <=7 && 0 <= from.y && from.y <=7
//                && 0 <= to.x && to.x <=7 && 0 <= to.y && to.y <=7)) {
//            throw new IndexOutOfBoundsException("Positions given are outside of the board");
//        }
//
//        // there is a piece at from
//        if (isFree(from)) {
//            throw new RuntimeException(String.format("No piece in at (%s,%s)",from.x,from.y));
//        }
//
//        // is it the correct players turn to move that piece
//        if (board[pos2board(from)].isPlayerOne != playerOneTurn){
//            throw new RuntimeException("Trying to move the other players piece");
//        }
//
//        // Check space moving to is free
//        if (!isFree(to)) {
//            throw new RuntimeException(String.format("Space to move occupied (%s,%s)",from.x,from.y));
//        }
//
//        // grab players piece
//        Piece piece = board[pos2board(from)];
//
//        // Legal step
//        Step step = getStep(piece, from, to);
//        if (step == null) {
//            throw new RuntimeException("Invalid move");
//        }
//
//        // If it is a take check, jumping opposing player, and return that piece
//        Piece taken;
//
//
//        switch (step) {
//            case TAKEUPLEFT:
//                System.out.println("start take up left");
//                taken = validTake(from,Step.UPLEFT);
//                System.out.println("take up left");
//                break;
//            case TAKEDOWNLEFT:
//                System.out.println("start take down left");
//                taken = validTake(from,step.DOWNLEFT);
//                System.out.println("take down left");
//                break;
//            case TAKEDOWNRIGHT:
//                System.out.println("start take down right");
//                taken = validTake(from,Step.DOWNRIGHT);
//                System.out.println("take down right");
//                break;
//            case TAKEUPRIGHT:
//                System.out.println("start take up right");
//                taken = validTake(from,Step.UPRIGHT);
//                System.out.println("take up right");
//                break;
//
//                default:
//                    taken = null;
//                    System.out.println("dont take");
//        }
//
//        // will the piece be crowned if it makes this move.
//        boolean crowned = crossedBoard(piece, to);
//
//        return new Turn(from,to,taken,crowned);
//    }
//
//    public Turn validateTurn(Turn turn){
//        return makeTurn(turn.from,turn.to);
//    }
//
//    public void doTurn(Turn turn){
//        // move on board (update both locations)
//        Piece movedPiece = removePiece(turn.from);
//
//        // Move piece pos in piece
//        movedPiece.move(turn.to);
//
//        // add piece back to board in right place
//        addPiece(movedPiece);
//
//        // update player locations in hashmap
//        if (playerOneTurn){
//            playerOne.movePiece(turn.from, turn.to);
//        } else {
//            playerTwo.movePiece(turn.from, turn.to);
//        }
//
//        if (turn.taken != null) {
//            removePiece(turn.taken.pos);
//        }
//
//        // Check for another valid move
////        if (turn.taken == null) {
////            playerOneTurn = !playerOneTurn;
////        }
//        if (turn.crowned){
//            movedPiece.king = true;
//        }
//
//        // update who's Turn next
////        if(posible succession)
////        take unless crowned
//        // player one
//        playerOneTurn = !playerOneTurn;
//    }

	/**
	 * Undo the last move executed on the board.
	 */
	public void undoTurn() {
		// Remove last turn from history stack
		Turn turn = history.pop();

		// Move the current players piece back to where it was:
		// - in the piece state
		// - on the board
		// - in the player HashMap
		Piece ally = removePiece(turn.getTo());
		ally.move(turn.getFrom());
		addPiece(ally);

		// If piece taken. Put back
		if (turn.getTaken() != null) addPiece(turn.getTaken());

		// Dethrone a king if they were crowned on this turn
		if (turn.getCrowned()) ally.setKing(false);

		// Change players if neccessary
		if(turn.getChangePlayer()) swapPlayer();

		extraJump.ofNullable(turn.getExtraJumps());
	}


	// =================================================================================================================
	//
	//                                          Rules to Follow
	//
	// =================================================================================================================

	public boolean isGameOver() {
		return playerOne.pieces.size() == 0 || playerTwo.pieces.size() == 0;
	}

	/**
	 * @param piece - a {@link Player}'s {@link Piece}
	 * @param to    Where it is moving to
	 * @return
	 */
	private boolean crossedBoard(Piece piece, Position to) {
		if (!piece.king) {
			if (piece.isPlayerOne && pos2board(to) > 55) {
				piece.setKing(true);
				return true;
			} else if (!piece.isPlayerOne && pos2board(to) < 8) {
				piece.setKing(true);
				return true;
			}
		}
		return false;
	}

	private void swapPlayer() {
		playerOneTurn = !playerOneTurn;
	}

	// =================================================================================================================
	//
	//                                          Display and Run
	//
	// =================================================================================================================


	/**
	 * Converts the Pieces to text, used to represent game state in terminal.
	 *
	 * @param x x position
	 * @param y y position
	 * @return
	 */
	private String cPiece(int x, int y) {
		int i = pos2board(new Position(x, y));
		if (board[i] == null) {
			return " ";
		} else if (board[i].isPlayerOne && board[i].king) {
			return "R";
		} else if (board[i].isPlayerOne && !board[i].king) {
			return "r";
		} else if (!board[i].isPlayerOne && board[i].king) {
			return "B";
		} else if (!board[i].isPlayerOne && !board[i].king) {
			return "b";
		} else {
			return "  ERROR  ";
		}
	}


	@Override
	public String toString() {
		String b = "";
		b += "                   Player 1     \n";
		b += "                     ROW       \n";
		b += "        | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | \n";
		b += "     ------------------------------------\n";
		for (int y = 0; y < 8; y++) {
			if (y == 4) {
				b += " COL  " + y + " | ";
			} else {
				b += "      " + y + " | ";
			}
			for (int x = 0; x < 8; x++) {
				b += "" + cPiece(x, y) + " | ";
			}
			b += "\n";
			b += "     ------------------------------------\n";
		}
		b += "                   Player 2       ";

		return b;
	}

	/**
	 * If the transition is possible step, returns the Step.
	 * <p>
	 * Otherwise returns null
	 *
	 * @param from
	 * @param to
	 * @return
	 */
	private Step convert2step(Position from, Position to) {
		int x = to.x - from.x;
		int y = to.y - from.y;

		for (Step step : Step.values()) {
			if (x == step.x && y == step.y) return step;
		}
		return null;
	}

	private Turn validateCommand(Position from, Position to) {
		// Check that a step has been given.
		Step step = convert2step(from, to);
		if (step == null) {
			if (DEBUG > 0) System.out.println("Invalid move, not a legal step");
			return null;
		}

		// Check there is a piece at the location
		if (isFree(from)) return null;
		Piece piece = board[pos2board(from)];

		// Correct piece being moved;
		if (piece.isPlayerOne != playerOneTurn) return null;

		if (validStep(piece, step)) {
			Turn userTurn = new Turn(piece, step);

			// Check the turn is a valid successor then return it.
			for (Turn t : successor()) {
				if (userTurn.equals(t)) return userTurn;
			}
		}
		return null;
	}

	public void startNewGame() {

		while (!isGameOver()) {
			Position[] command;
			Turn turn = null;
			if (playerOneTurn) {
				System.out.println("Player 1's turn");
				if (DEBUG > 0 && extraJump.isPresent())
					System.out.println("You must perform a mandatory jump now.");
				for (int i = 0; i < 3; i++) {
					command = playerOne.getUserCommand();
					if (DEBUG > -1) {
						System.out.printf("Should be Player 1's turn is Player %s's turn\n", playerOneTurn ? 1 : 2);
					}
					turn = validateCommand(command[0], command[1]);
					if (turn == null) {

						System.out.printf("Incorrect command from %s --> %s. Please try again. Attempt %s/3. \n", command[0], command[1], i + 1);
						String possMoves = "";
						for (Turn t : successor()) {
							possMoves += t.toString() + "\n";
						}
						System.out.printf(possMoves);
					} else {
						break;
					}
				}
				extraJump.ofNullable(executeTurn(turn));

			} else {
				System.out.println("Player 2's turn");

				if (DEBUG > 0 && extraJump.isPresent())
					System.out.println("You must perform a mandatory jump now.");
				for (int i = 0; i < 3; i++) {
					command = playerTwo.getUserCommand();
					if (DEBUG > -1) {
						System.out.printf("Should be Player 2's turn is Player %s's turn\n", playerOneTurn ? 1 : 2);
					}

					turn = validateCommand(command[0], command[1]);
					if (turn == null) {
						System.out.printf("Incorrect command from %s --> %s. Please try again. Attempt %s/3.\n", command[0], command[1], i + 1);
						String possMoves = "";
						for (Turn t : successor()) {
							possMoves += t.toString() + "\n";
						}
						System.out.printf(possMoves);
					} else {
						break;
					}
				}
				extraJump.ofNullable(executeTurn(turn));

			}

//			if (!extraJump.isPresent()) swapPlayer();
			System.out.println(toString());
		}
	}


	public static void main(String[] args) {
//        divide(4,0);
//        System.out.println(String.format("hello %s","World"));
		boolean playerOneStart = true;
//        CheckersBoard game = new CheckersBoard(playerOneStart,new Human(playerOneStart),new Robot(!playerOneStart,null,3));

		CheckersBoard game = new CheckersBoard(playerOneStart);
//		Player one = new Human(playerOneStart);
//        Player two = new Human(!playerOneStart);
		Player one = new Robot(playerOneStart, game, 2);
		Player two = new Robot(!playerOneStart, game, 5);
		game.addPlayers(one, two);

		System.out.println(game.toString());

		game.startNewGame();

		System.out.printf("Game over. Player %s Wins!\n", game.playerOne.pieces.size() == 0 ? 2 : 1);

	}

	public static int[] rand() {
		int[] r = {0};
		return r;
	}

	public static int divide(int a, int b) {
		int c = -1;

		try {
			c = a / b;
		} catch (Exception e) {
			System.err.print("exp ");
		} finally {
			System.err.print("fin ");
		}
		return c;
	}

}

