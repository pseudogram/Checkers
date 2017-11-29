package Model;

import javafx.geometry.Pos;

public class Position {
    int x;
    int y;

    Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    Position(Position pos) {
        x = pos.x;
        y = pos.y;
    }

    public void add (Step step){
        this.x += step.x;
        this.y += step.y;
    }

    public void minus (Step step) {
        this.x -= step.x;
        this.y -= step.y;
    }

    public void minus (Position pos) {
        x -= pos.x;
        y -= pos.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (x != position.x) return false;
        return y == position.y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

}
