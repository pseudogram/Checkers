package Model;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;

public class Robot extends Player {

    private CheckersBoard board;
    private final int MAX_DEPTH;


    public Robot (boolean playerOne, CheckersBoard board, int MAX_DEPTH){
        this.playerOne = playerOne;
        this.pieces = new HashMap<>();
        this.board = board;
        this.MAX_DEPTH = MAX_DEPTH;
    }

    @Override
    public Position[] getUserCommand() {
        int bestScore = startEvaluation();
        int num = Integer.MIN_VALUE;
        Turn bestTurn = null;
        for (TurnScore ts : successorEvaluations) {
            if(ts.score > num) bestTurn = ts.turn;
        }
        if (bestTurn == null) return null;
        Position[] move = new Position[] {bestTurn.getFrom(),bestTurn.getTo()};
        return move;
    }


    ArrayList<TurnScore> successorEvaluations;

    public int startEvaluation(){
        successorEvaluations = new ArrayList<>();
        boolean lastPlayer;
        try{
            lastPlayer = board.getHistory().peek().getPlayerOne();
        } catch (EmptyStackException e) {
            lastPlayer = false;
        }
        int bestscore = minimax(0, lastPlayer); // depth, player
        return bestscore;
    }

    public int assessBoard(boolean lastPlayerTurn) {
        // The heuristic
        if (lastPlayerTurn) {
            return board.getPlayerOne().remainingPieces() - board.getPlayerTwo().remainingPieces();
        } else {
            return board.getPlayerTwo().remainingPieces() - board.getPlayerOne().remainingPieces();
        }
    }


    // it the last turn executed was player 1
    public int minimax(int depth, boolean lastPlayerTurn) {

        // check if either player has won and return zero-sum result
        if (MAX_DEPTH <= depth || board.isGameOver()){
            return assessBoard(lastPlayerTurn);
        }
//        if(xHasWon())
//            return +1;
//        if(oHasWon())
//            return -1;

        // Return a list of possible turns
        List<Turn> possibleTurns = board.successor();
        if (possibleTurns.isEmpty())
            return assessBoard(lastPlayerTurn);


        // because the game tree is only 9 levels deep, we store the results in a list
        // do not do this with more complex problems!
        List<Integer> scores = new ArrayList<>();
        Turn last = null;
        //Loop through all available positions
        for (int i=0; i < possibleTurns.size(); ++i)
        {
            // determine all board positions that aren't occupied
            Turn turn = possibleTurns.get(i);
            //if (board.playerOneTurn)//(player == 1) { //X's turn: get the highest result returned by minimax

            // place a piece at the first available position for player 1
            board.executeTurn(turn);
//            System.out.printf("Player %s executed move\n",turn.playerOne?1:2);

            boolean playerOneLast = turn.getPlayerOne();

            // get the minimax evaluation result for making the previous move
            int currentScore = minimax(depth + 1, playerOneLast); // Increase depth here
            // put the score for that path in the list
            scores.add(currentScore);
            // store a mapping of complete evaluations (at depth 0) and their scores
            if (depth == 0){ successorEvaluations.add(new TurnScore(currentScore, turn)); }

            board.undoTurn();
//            System.out.printf("Player %s undone move, set to player\n",turn.playerOne?1:2);
            last = turn;
        }

//
//            else if (player == 2) {//O's turn: get the lowest result returned by minimax
//                placePiece(turn, 2);
//                int currentScore = minimax(depth + 1, 1); // increase depth here
//                // put the score for that path in the list
//                scores.add(currentScore);
//            }
//            board[turn.x][turn.y] = 0; //Reset this turn
//        }
 /**********       if (depth == 0) System.out.printf("Player %s turn next\n",!(last.playerOne^last
            .changePlayer)?1:2);*/
        return getMax(scores);
//        return super.playerOne ? getMax(scores) : getMin(scores);
        // the above expression uses a ternary operator and is equivalent to this code:
        // if (player == 1)
        //    return getMax(scores);
        // else
        //    return getMin(scores);
    }

    private int getMax(List<Integer> list){
        // return the maximum value in the list
        int max = Integer.MIN_VALUE;

        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) > max) {
                max = list.get(i);
            }
        }
        return max;
    }

}

class TurnScore{
    int score;
    Turn turn;

    public TurnScore(int score, Turn turn){
        this.score = score;
        this.turn = turn;
    }
}
