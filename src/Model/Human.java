package Model;

import java.util.HashMap;
import java.util.Scanner;

public class Human extends Player {

    Scanner scan;

    public Human (boolean playerOne){
        this.playerOne = playerOne;
        pieces = new HashMap<>();
        scan = new Scanner(System.in);
    }

    @Override
    public Position[] getUserCommand() {
        System.out.println("What piece would you like to move? Type coordinates (row, col).");
        Position from = new Position(scan.nextInt(),scan.nextInt());
        System.out.println("Where would you like to move it? Type coordinates (row, col).");
        Position to = new Position(scan.nextInt(),scan.nextInt());

        return new Position[] {from,to};
    }
}
