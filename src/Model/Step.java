package Model;

 public enum Step {
    UPLEFT (-1, -1), UPRIGHT(1, -1), DOWNLEFT(-1,1), DOWNRIGHT(1,1),
    TAKEUPLEFT (-2, -2), TAKEUPRIGHT(2, -2), TAKEDOWNLEFT(-2,2), TAKEDOWNRIGHT(2,2);

    int x;
    int y;

    Step(int x, int y) {
        this.x = x;
        this.y = y;
    }



}
