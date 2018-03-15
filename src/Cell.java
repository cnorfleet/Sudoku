import java.util.ArrayList;

public class Cell
{
    private int myVal;
    private int r, c;
    private ArrayList<Cell> neighbors; //should eventually replace this with an array of length 20 since fixed size
    private ArrayList<Cell> row;
    private ArrayList<Cell> col;
    private ArrayList<Cell> box;
    private ArrayList<ArrayList<Cell>> regions; //can't make an array of arraylists?
    private ArrayList<Integer> possibilities; //should eventually replace this with a boolean array or something

    public Cell(int val, int r, int c)
    {
        myVal = val;
        this.r = r; this.c = c;
        initializeNeighbors();
        initializePossibilities();
    }

    private void initializeNeighbors()
    {
        neighbors = new ArrayList<>();
        row = new ArrayList<>();
        col = new ArrayList<>();
        box = new ArrayList<>();
        regions.add(row);
        regions.add(col);
        regions.add(box);
    }
    private void initializePossibilities()
    {
        possibilities = new ArrayList<>();
        if (myVal == 0)
        {
            for(int i = 1; i <= 9; i++)
            { possibilities.add(i); }
        }
    }

    public void updateNeighborsPossibilities()
    {
        if(myVal == 0) { return; }
        for(Cell n : neighbors)
        { n.removePossibility(myVal); }
    }
    public void removePossibility(int i)
    { possibilities.remove(new Integer(i)); }

    public int getVal()
    { return myVal; }

    @Override
    public String toString()
    { return Integer.toString(myVal); }

    public void connect(Cell c, char where)
    { connect(c, false, where); }
    public void connect(Cell c, boolean unidirectional, char where)
    {
        neighbors.add(c);
        switch(where)
        {
            case 'r': row.add(c); break;
            case 'c': col.add(c); break;
            case 'b': col.add(c); break;
        }
        if (!unidirectional)
        { c.connect(this, true, where); }
    }

    public void debugPrintNeighbors()
    {
        for(Cell c : neighbors)
        { System.out.print(c + " "); }
        System.out.println();
    }
    public void debugPrintPossibilities()
    {
        for(Integer p : possibilities)
        { System.out.print(p + " "); }
        System.out.println();
    }
    public boolean debugNoPossibilities()
    { return (myVal == 0 && possibilities.size() == 0); }
    public String debugLocationString()
    { return (r + ", " + c); }

    public boolean simpleSolve()
    {
        if(possibilities.size() != 1)
        { return false;}
        myVal = possibilities.remove(0);
        updateNeighborsPossibilities();
        return true;
    }
}