// written by Shivali Mukherji mukhe105

public class Cell {
    private int row;
    private int col;
    /**
     * '-': Has not been guessed, no boat present
     * 'B': Has not been guessed, boat present
     * 'H': Has been guessed, boat present
     * 'M': Has been guessed, no boat present
     */
    private char status;

    public Cell(int row, int col, char status) {
        this.row = row;
        this.col = col;
        this.status = status;

    }

    public void set_status(char c) {
        this.status = c;
    }

    public char get_status() {
        return this.status;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.col;
    }
}