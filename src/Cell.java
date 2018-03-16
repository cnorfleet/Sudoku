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
        regions = new ArrayList<>();
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
    {
        possibilities.remove(new Integer(i));
        if(myVal == 0 && possibilities.size() == 0)
        { throw new NullPointerException("No More Possibilities Left"); }
    }
    public boolean containsPossibility(int i)
    {
        for(int p : possibilities)
        {
            if(i == p)
            { return true; }
        }
        return false;
    }
    public void clearPossibilities()
    {
        while(possibilities.size() > 0)
        { possibilities.remove(0); }
    }

    public int getVal()
    { return myVal; }
    public int getRow()
    { return r; }
    public int getCol()
    { return c; }
    public void setVal(int val)
    {
        if(val == 0) { throw new IllegalArgumentException("Please Don't Set Me to Zero"); }
        myVal = val;
        updateNeighborsPossibilities();
        clearPossibilities();
    }

    @Override
    public String toString()
    { return Integer.toString(myVal); }

    public void connect(Cell c, char where)
    { connect(c, false, where); }
    public void connect(Cell c, boolean unidirectional, char where)
    {
        if (where != 'x')
        { neighbors.add(c); }
        //r is row, c is col, b is box, x is only box (already added to row/col)
        switch(where)
        {
            case 'r': row.add(c); break;
            case 'c': col.add(c); break;
            case 'b': case 'x': box.add(c); break;
        }
        if (!unidirectional)
        { c.connect(this, true, where); }
    }

    public boolean hasPossibility(int i)
    {
        for(int p : possibilities)
        { if (i == p) { return true; } }
        return false;
    }
    public ArrayList<Integer> getPossibilities()
    { return possibilities; }

    public void debugPrintNeighbors()
    {
        for(Cell c : neighbors)
        { System.out.print(c + " "); }
        System.out.println();
        for(Cell c : row)
        { System.out.print(c + " "); }
        System.out.println();
        for(Cell c : col)
        { System.out.print(c + " "); }
        System.out.println();
        for(Cell c : box)
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

    public ArrayList<ArrayList<Cell>> getRegions()
    { return regions; }

    public boolean simpleSolve()
    {
        if(possibilities.size() != 1)
        { return false;}
        myVal = possibilities.remove(0);
        updateNeighborsPossibilities();
        return true;
    }

    public boolean onlyOptionSolve()
    {
        for(int p : possibilities)
        {
            for(ArrayList<Cell> region : regions)
            {
                boolean found = false;
                for(Cell c : region)
                {
                    if(c.hasPossibility(p))
                    { found = true; break; }
                }
                if(!found)
                { myVal = p; updateNeighborsPossibilities(); clearPossibilities(); return true; }
            }
        }
        return false;
    }
}