// written by Shivali Mukherji mukhe105

public class Boat {
    private int size = 3;
    private boolean orientation;
    private Cell[] spaces = new Cell[size];

    public Boat(int size, boolean orientation, Cell[] spaces) {
        this.size = size;
        this.orientation = orientation;
        this.spaces = spaces;

    }

    public int getSize() {
        return this.size;
    }


    public boolean getOrientation() {
        return this.orientation;

    }

    public void setSpaces(Cell[] newCells) {
        this.spaces = newCells;
    }

    public Cell[] getSpaces() {
        return this.spaces;
    }


}