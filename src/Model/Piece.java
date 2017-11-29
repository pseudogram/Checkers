package Model;

public class Piece {
    Boolean isPlayerOne;
    Boolean king;
    Position pos;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Piece piece = (Piece) o;

        if (isPlayerOne != null ? !isPlayerOne.equals(piece.isPlayerOne) : piece.isPlayerOne != null) return false;
        if (king != null ? !king.equals(piece.king) : piece.king != null) return false;
        return pos.equals(piece.pos);
    }

    @Override
    public int hashCode() {
        int result = isPlayerOne != null ? isPlayerOne.hashCode() : 0;
        result = 31 * result + (king != null ? king.hashCode() : 0);
        result = 31 * result + pos.hashCode();
        return result;
    }

    public Piece(Boolean isPlayerOne, Position pos) {
        this.isPlayerOne = isPlayerOne;
        this.pos = pos;

        this.king = false;
    }

    public Piece(Boolean isPlayerOne, Boolean king, Position pos) {
        this.isPlayerOne = isPlayerOne;
        this.king = king;
        this.pos = pos;
    }

    public void move(Position pos){
        this.pos = pos;
    }

    public Position getPos() {
        return pos;
    }

    public boolean isKing(){
        return king;
    }

    public void setKing(boolean king) {
        this.king = king;
    }

    public void setPos(int x, int y){

    }
}
