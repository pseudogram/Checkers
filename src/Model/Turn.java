package Model;

import java.util.ArrayList;
import java.util.Optional;

public class Turn {
    private Position from;
    private Position to;
    private Piece taken;
    private Boolean crowned; // If a piece is crowned during a turn.
    private Step step;
    private Boolean playerOne;
    private Boolean changePlayer;
    /**

     * If there were extra jumps possible at the end of a turn, they must be saved here so they
     * can be undone.
     */
    private Optional<ArrayList<Turn>> extraJumps;

    public void setExtraJumps(ArrayList<Turn> extraJumps) {
        this.extraJumps.ofNullable(extraJumps);
    }

    public Position getFrom() {
        return from;

    }

    public void setChangePlayer(Boolean changePlayer) {
        this.changePlayer = changePlayer;
    }

    public Position getTo() {

        return to;
    }

    public Piece getTaken() {
        return taken;
    }

    public Boolean getCrowned() {
        return crowned;
    }

    public Step getStep() {
        return step;
    }

    public Boolean getChangePlayer() {
        return changePlayer;
    }

    public Optional<ArrayList<Turn>> getExtraJumps() {
        return extraJumps;
    }

    public void setTaken(Piece taken) {
        this.taken = taken;
    }

    public void setCrowned(Boolean crowned) {
        this.crowned = crowned;
    }


//    public Turn (Position from, Position to, Piece taken, Boolean crowned){
//        this.from = from;
//        this.to = to;
//        this.taken = taken;
//        this.crowned = crowned;
//    }

    public Turn (Piece piece, Step step){
        this.from = new Position(piece.getPos());
        this.to = new Position(from.x+step.x,from.y+step.y);
        this.playerOne = piece.isPlayerOne;

        // False unless stated otherwise
        this.crowned = false;
        this.changePlayer = true;
        this.extraJumps.empty();

        this.step = step;
    }

    /**
     * Returns true if player one executed this move.
     * @return
     */
    public Boolean getPlayerOne() {
        return playerOne;
    }
//    public Turn (Position from, Position to){
//        this.from = from;
//        this.to = to;
//    }

    @Override
    public String toString() {
        return "Turn "+ from + " --> " + to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Turn turn = (Turn) o;

        if (!from.equals(turn.from)) return false;
        return to.equals(turn.to);
    }

    @Override
    public int hashCode() {
        int result = from.hashCode();
        result = 31 * result + to.hashCode();
        return result;
    }
}
