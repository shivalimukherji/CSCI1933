// written by Shivali Mukherji mukhe105

import java.util.Scanner;

public class Game {

    public static void showBoard(Board board) {
        if(board.isDebugMode()) {
            board.display();
        }
        else {
            board.print();
        }
    }

    public static void main(String[] args) {
        int turns = 0;
        int powerPoints = 0;
        boolean debugMode = false;
        Board board = null;
        Scanner scan = new Scanner(System.in);
        System.out.println("Debug Mode On? (y or n):");
        String mode = scan.nextLine();
        if (mode.equalsIgnoreCase("y")) {
            debugMode = true;
        } else {
            debugMode = false;
        }

        System.out.println("Enter game mode (beginner, intermediate or expert): ");
        String choice = scan.nextLine();
        if (choice.equalsIgnoreCase("beginner")) {
            board = new Board(3, debugMode);
            board.placeBoats();
            powerPoints = 1; //Beginner gets 1 power point(s)
        } else if (choice.equalsIgnoreCase("intermediate")) {
            board = new Board(6, debugMode);
            board.placeBoats();
            powerPoints = 3; //Intermediate gets 3 power point(s)
        } else if (choice.equalsIgnoreCase("expert")) {
            board = new Board(9, debugMode);
            board.placeBoats();
            powerPoints = 5; //Expert gets 5 power point(s)
        }
        if (board != null) {
            showBoard(board);
            int boatsRemain = board.remainingBoats();
            System.out.println("Boats placed on board: " + boatsRemain + " \n");
            boolean skipTurn = false;
            while(boatsRemain > 0) {
                turns++;
                if (skipTurn) {
                    System.out.println("Turn" + turns + " Skipped!");
                    skipTurn = false;
                    continue;
                }

                System.out.println("How would you like to attack? Choose from (fire, missile, drone and submarine): ");
                String attackMode = scan.nextLine();
                if (attackMode.equalsIgnoreCase("fire")) {
                    System.out.println("Enter attacking co-ordinate(s), enter integer(s), x and y, like this: x,y");
                    String coords = scan.nextLine();
                    String[] xypair = coords.split(",");
                    if (xypair != null && xypair.length != 2) {
                        System.out.println("Invalid input, try again!");
                        continue;
                    }
                    int x = Integer.parseInt(xypair[0].trim());
                    int y = Integer.parseInt(xypair[1].trim());
                    int result = board.fire(x, y);
                    if (result == 0) {
                        System.out.println("Turn " + turns + " Co-ordinate (" + x + ", " + y + ") is Out of Bounds, next turn skipped!");
                        skipTurn = true;
                    } else if (result == 1) {
                        System.out.println("Turn " + turns + " Co-ordinate (" + x + ", " + y + ") MISSED the target");
                    } else if (result == 2) {
                        System.out.println("Turn " + turns + " Co-ordinate (" + x + ", " + y + ") HIT the target");
                    } else if (result == 3) {
                        System.out.println("Turn " + turns + " Co-ordinate (" + x + ", " + y + ") is Redundant, next turn skipped!");
                        skipTurn = true;
                    }
                    showBoard(board);
                    boatsRemain = board.remainingBoats();
                    if (boatsRemain ==0) {
                        System.out.println("Turn " + turns + " Co-ordinate (" + x + ", " + y + "), sunk the last boat. Game ends!!");
                        break;
                    }
                } else if (attackMode.equalsIgnoreCase("missile")) {
                    if (powerPoints == 0) {
                        System.out.println("Missile has been used maximum amount of times!");
                        showBoard(board);
                    } else {
                        System.out.println("Where would you like to launch your missile?");
                        System.out.println("Enter attacking co-ordinate(s), x and y, like this: x,y");
                        String coords = scan.nextLine();
                        String[] xypair = coords.split(",");
                        if (xypair != null && xypair.length != 2) {
                            System.out.println("Invalid input, try again!");
                            continue;
                        }
                        int x = Integer.parseInt(xypair[0].trim());
                        int y = Integer.parseInt(xypair[1].trim());
                        board.missile(x, y);
                        if (powerPoints >0) {
                            powerPoints--;
                        }
                        showBoard(board);
                        boatsRemain = board.remainingBoats();
                        if (boatsRemain ==0) {
                            System.out.println("Turn " + turns + " Co-ordinate (" + x + ", " + y + "), sunk the last boat. Game ends!!");
                            break;
                        }
                    }

                } else if (attackMode.equalsIgnoreCase("drone")) {
                    if (powerPoints == 0) {
                        System.out.println("Drone has been used maximum amount of times!");
                        showBoard(board);
                    } else {
                        board.drone();
                        if (powerPoints >0) {
                            powerPoints--;
                        }
                        showBoard(board);
                    }
                } else if (attackMode.equalsIgnoreCase("submarine")) {
                    if (powerPoints == 0) {
                        System.out.println("Submarine has been used maximum amount of times!");
                        showBoard(board);
                    } else {
                        boolean validTarget = false;
                        int x=-1;
                        int y=-1;
                        System.out.println("Where would you like to attack with the submarine?");
                        while (!validTarget) {
                            System.out.println("Enter attacking co-ordinate(s), x and y, like this: x,y");
                            String coords = scan.nextLine();
                            String[] xypair = coords.split(",");
                            if (xypair != null && xypair.length != 2) {
                                System.out.println("Invalid input, try again!");
                                continue;
                            }
                            x = Integer.parseInt(xypair[0].trim());
                            y = Integer.parseInt(xypair[1].trim());
                            if((x < 0 || x >= board.get_size()) || (y < 0 || y >= board.get_size())) {
                                System.out.println("Target Out of Bounds, try again!");
                                continue;
                            }
                            validTarget = true;
                        }
                        if (validTarget) {
                            board.submarine(x, y);
                            if (powerPoints > 0) {
                                powerPoints--;
                            }
                            showBoard(board);
                            boatsRemain = board.remainingBoats();
                            if (boatsRemain ==0) {
                                System.out.println("Turn " + turns + " Co-ordinate (" + x + ", " + y + "), sunk the last boat. Game ends!!");
                                break;
                            }
                        }
                    }
                }

            }
        }
        scan.close();
    }



}

