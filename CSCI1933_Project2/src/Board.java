// written by Shivali Mukherji mukhe105

import java.lang.Math;

public class Board {

    private Cell[][] board;
    private int boardSize;
    private Boat[] boats;
    private int numBoats;
    private boolean debugMode;

    public Board(int boardSize, boolean debugMode) {
        this.boardSize = boardSize;
        this.debugMode = debugMode;

        if (this.boardSize == 3) {
            this.numBoats = 1;
        } else if (this.boardSize == 6) {
            this.numBoats = 3;
        } else if (this.boardSize == 9) {
            this.numBoats = 5;
        }
        // initializes boat array
        this.boats = new Boat[this.numBoats];
        // initializes cell array
        this.board = new Cell[this.boardSize][this.boardSize];

        for (int i = 0; i < this.boardSize; i++) {
            for (int j = 0; j < this.boardSize; j++) {
                Cell newCell = new Cell(i, j, '-');
                board[i][j] = newCell;
            }
        }
    }

    public int[] getSizesofBoats(int numofBoats) {
        int[] sizes = null;
        if (numofBoats == 1) {
            sizes = new int[1];
            sizes[0] = 2;
        } else if (numofBoats == 3) {
            sizes = new int[3];
            sizes[0] = 2;
            sizes[1] = 3;
            sizes[2] = 4;
        } else if (numofBoats == 5) {
            sizes = new int[5];
            sizes[0] = 2;
            sizes[1] = 3;
            sizes[2] = 3;
            sizes[3] = 4;
            sizes[4] = 5;

        }
        return sizes;
    }

    public boolean randomOrientation() {
        int num = (int) Math.round(Math.random());
        if (num == 1) {
            return true;
        } else {
            return false;
        }
    }


    public void placeBoats() {
        int[] boatSizes = getSizesofBoats(this.numBoats);
        for (int k = 0; k < this.numBoats; k++) {
            boolean orientation = randomOrientation();
            boolean complete = true;
            // Get coordinates
            double randRow = (this.boardSize) * Math.random();
            double randCol = (this.boardSize) * Math.random();
            int x = (int) (Math.floor(randRow));
            int y = (int) (Math.floor(randCol));
            int sizeofBoat = boatSizes[k];


            Cell[] boatCells = new Cell[sizeofBoat];
            Boat newBoat = new Boat(sizeofBoat, orientation, boatCells);
            boats[k] = newBoat;


            if (orientation) {//Vertical orientation
                // checks if the boat will fit vertically on the bottom side of the grid,
                // subtracts until there can be a boat placed from y + boat.getBoatSize()
                while(y + newBoat.getSize() > this.boardSize) {
                    y--;
                }
                for (int j = y; j < y + newBoat.getSize(); j++) {
                    if (this.board[x][j].get_status() == 'B') { //find cells where a boat is present...
                        complete = false;
                        k--;
                        break;
                    }
                }
                if (complete) {
                    for (int j = y; j < y + newBoat.getSize(); j++) {
                        this.board[x][j].set_status('B');
                        boatCells[j - y] = this.board[x][j]; // adds the cell(coordinate) of the the boat until complete.
                    }
                }
            }

            if (!orientation) {//Horizontal orientation
                // checks if the ship will fit horizontally on the right side of the grid,
                // subtracts until there can be a boat placed from x + boat.getBoatSize()
                while(x + newBoat.getSize() > this.boardSize) {
                    x--;
                }
                for (int j = x; j < x + newBoat.getSize(); j++) {
                    if (this.board[j][y].get_status() == 'B') { //find cells where a boat is present...
                        complete = false;
                        k--;
                        break;
                    }
                }
                if (complete) {
                    for (int j = x; j < x + newBoat.getSize(); j++) {
                        this.board[j][y].set_status('B');
                        boatCells[j - x] = this.board[j][y]; // adds the cell(coordinate) of the the boat until complete.
                    }
                }
            }
        }
    }

    public int fire(int x, int y) {
        int score = 0;
        if((x < 0 || x >= this.boardSize) || (y < 0 || y >= this.boardSize)) {
            score = 0; // penalty: out of bounds
        }
        else if(board[x][y].get_status() == '-') {
            board[x][y].set_status('M');
            score = 1;
            // missed boat
        }
        else if(board[x][y].get_status() == 'B') {
            board[x][y].set_status('H');
            score = 2;
            // boat hit
        }
        else {
            score = 3; // penalty: redundant guess
        }
        return score;
    }

    public void missile(int x, int y) {
        fire(x - 1, y - 1);
        fire(x - 1, y);
        fire(x , y);
        fire(x , y + 1);
        fire(x , y - 1);
        fire(x + 1, y);
        fire(x + 1, y - 1);
        fire(x - 1, y + 1);
        fire(x + 1, y + 1);
    }

    public void drone() {
        boolean orientation = randomOrientation();
        double random = (this.boardSize) * Math.random();
        int randomIndex = (int)(Math.floor(random));
        int scannedCells = 0;

        if(orientation) {
            // Scans the rows starting with randomIndex
            for(int i = 0; i < this.boardSize; i++) {
                if(this.board[randomIndex][i].get_status() == 'B' || this.board[randomIndex][i].get_status() == 'H') {
                    scannedCells++;
                }
            }
            System.out.println("Drone has scanned: " + scannedCells + " targets in a row " + randomIndex + ".");
        }
        else {
            // Scans the columns starting with randomIndex
            for(int j = 0; j < this.boardSize; j++) {
                if(this.board[randomIndex][j].get_status() == 'B' || this.board[randomIndex][j].get_status() == 'H') {
                    scannedCells++;
                }
            }
            System.out.println("Drone has scanned: " + scannedCells + " targets in a row " + randomIndex + ".");
        }

    }

    public void submarine(int x, int y) {
        if (this.board[x][y].get_status() == '-') {
            this.board[x][y].set_status('M');
            System.out.println("Submarine attack on co-ordinate (" + x + ", " + y + ") MISSED the target");
        } else {
            boolean horizontalFound = false;
            boolean verticalFound = false;
            int horizontalCells = 0;
            int verticalCells = 0;
            //start with horizontal orientation
            //scan the x,y cell
            if (this.board[x][y].get_status() == 'B' || this.board[x][y].get_status() == 'H') {
                this.board[x][y].set_status('H');
                horizontalCells++;
            }
            //scan cells column-wise left side of (x,y) to find a horizontal boat
            for (int j=0; j < y; j++) {
                if (this.board[x][j].get_status() == 'B' || this.board[x][j].get_status() == 'H') {
                    this.board[x][j].set_status('H');
                    horizontalCells++;
                    horizontalFound = true;
                } else if (this.board[x][j].get_status() == 'M' || this.board[x][j].get_status() == '-') {
                    continue;
                }
            }
            //scan cells column-wise right side of (x,y) to find a horizontal boat
            for (int j=y+1; j < this.boardSize; j++) {
                if (this.board[x][j].get_status() == 'B' || this.board[x][j].get_status() == 'H') {
                    this.board[x][j].set_status('H');
                    horizontalCells++;
                    horizontalFound = true;
                } else if (this.board[x][j].get_status() == 'M' || this.board[x][j].get_status() == '-') {
                    break;
                }
            }
            System.out.println("Horizontal Boat Cells: " + horizontalCells);
            if (!horizontalFound) {
                //scan the x,y cell
                if (this.board[x][y].get_status() == 'B' || this.board[x][y].get_status() == 'H') {
                    this.board[x][y].set_status('H');
                    verticalCells++;
                }
                //go to vertical orientation
                //scan cells row-wise north side of (x,y) to find a vertical boat
                for (int i=0; i < x; i++) {
                    if (this.board[i][y].get_status() == 'B' || this.board[i][y].get_status() == 'H') {
                        this.board[i][y].set_status('H');
                        verticalCells++;
                        verticalFound = true;
                    } else if (this.board[i][y].get_status() == 'M' || this.board[i][y].get_status() == '-') {
                        continue;
                    }
                }
                //scan cells row-wise south side of (x,y) to find a vertical boat
                for (int i=x+1; i < this.boardSize; i++) {
                    if (this.board[i][y].get_status() == 'B' || this.board[i][y].get_status() == 'H') {
                        this.board[i][y].set_status('H');
                        verticalCells++;
                        verticalFound = true;
                    } else if (this.board[i][y].get_status() == 'M' || this.board[i][y].get_status() == '-') {
                        break;
                    }
                }
                System.out.println("Vertical Boat Cells: " + verticalCells);
            }
            if (horizontalFound || verticalFound) {
                System.out.println("Submarine attack on co-ordinate (" + x + ", " + y + ") SUNK the boat");
            }
        }
    }


    public int remainingBoats() {
        int remaining = 0;
        Cell[] spaces;
        boolean boatAlive = false;
        int boatCellCount = 0;
        for (int i=0; i < this.numBoats; i++) {
            spaces = this.boats[i].getSpaces();
            for (int space=0; space < spaces.length; space++) {
                if (spaces[space].get_status() == 'B') {
                    boatAlive = true;
                }
                boatCellCount++;
            }
            if (boatAlive) {
                remaining++;
                boatAlive = false;
            }
        }
        return remaining;
    }

    public String convertToString(boolean debugMode) {
        String boardDisplay = "\t";
        for (int j = 0; j < this.boardSize-1; j++){
            boardDisplay += "|" + j + "|" + "\t";
        }
        boardDisplay += "|" + (this.boardSize-1) + "|";
        for(int i = 0; i < this.boardSize; i++){
            boardDisplay+= "\n" + "|" + i + "|" + "\t";
            for (int j = 0; j < this.boardSize; j++){
                boardDisplay += " " + debug(debugMode, board[i][j].get_status()) + "\t";
            }
        }
        return boardDisplay;
    }


    private char debug(boolean debugMode, char c) {
        if(debugMode){
            return c;
        }
        else{
            switch(c){
                case 'H':
                    c = 'H';
                    break;
                case 'M':
                    c = 'M';
                    break;
                default:
                    c = '-';
                    break;
            }
            return c;
        }
    }
    public void display() {
        System.out.println(convertToString(this.isDebugMode()));
    }

    public void print() {
        System.out.println(convertToString(this.isDebugMode()));
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public int get_size() {
        return this.boardSize;
    }


}