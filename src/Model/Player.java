package Model;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import static Model.CheckersBoard.DEBUG;
import static Model.CheckersBoard.pos2board;

public abstract class Player {

    boolean playerOne;
    HashMap<Integer, Piece> pieces;

    abstract public Position[] getUserCommand();

    public int remainingPieces(){
        return pieces.size();
    }

    public Iterator<Map.Entry<Integer,Piece>> getPieces(){
        return pieces.entrySet().iterator();
    }

    /**
     * Returns the total number of pieces left
     *
     * @return
     */
    public int piecesLeft(){
        return pieces.size();
    }

    public Stack<Piece> getPiecesStack(){
        Stack<Piece> pieceStack = new Stack<>();
        for(Map.Entry<Integer,Piece> piece: pieces.entrySet()) {
            pieceStack.push(piece.getValue());
        }
        return pieceStack;
    }

    public void movePiece(Position from, Position to){
        Piece piece = pieces.remove(pos2board(from));
        pieces.put(pos2board(to),piece);
    }

    public void addPiece(Piece piece){
        if(2<DEBUG) System.out.printf("Adding Player %s piece to space %s\n",piece.isPlayerOne?1:2, piece.getPos().toString());
        pieces.put(pos2board(piece.getPos()),piece);
    }

    public void removePiece(Piece piece){
        pieces.remove(pos2board(piece.getPos()));
    }
}

