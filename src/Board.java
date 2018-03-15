public class Board
{
    private Cell[][] myBoard;

    public Board(int[][] b)
    {
        initializeBoard(b);
    }

    private void initializeBoard(int[][] b)
    {
        //create board
        myBoard = new Cell[9][9];
        for(int r = 0; r < 9; r++)
        {
            for (int c = 0; c < 9; c++)
            {
                //initialize value
                myBoard[r][c] = new Cell(b[r][c],r,c);
                //connect with row
                for(int c2 = 0; c2 < c; c2++)
                { myBoard[r][c].connect(myBoard[r][c2], 'r'); }
                //connect with col
                for(int r2 = 0; r2 < r; r2++)
                { myBoard[r][c].connect(myBoard[r2][c], 'c'); }
                //connect with box - note that row and col are already connected
                for(int r2 = (r/3)*3; r2 < (r/3)*3+3; r2++)
                {
                    for(int c2 = (c/3)*3; c2 < (c/3)*3+3; c2++)
                    {
                        if(myBoard[r2][c2] != null && !(r2 == r && c2 == c))
                        {
                            if(r2 == r || c2 == c)
                            { myBoard[r][c].connect(myBoard[r2][c2], 'x'); }
                            else
                            { myBoard[r][c].connect(myBoard[r2][c2], 'b'); }
                        }
                    }
                }
            }
        }
        //initial possibilities update
        for(Cell[] r : myBoard)
        {
            for(Cell c : r)
            {
                c.updateNeighborsPossibilities();
            }
        }
    }

    @Override
    public String toString()
    {
        String out = "";
        for (int row = 0; row < 9; row++)
        {
            for (int col = 0; col < 9; col++)
            {
                if (myBoard[row][col].getVal() != 0) { out += myBoard[row][col]; }
                else { out += "_"; }
                if (col != 8 && col % 3 != 2) { out += ", "; }
                else if (col % 3 == 2) { out += " | "; }
            }
            out += "\n";
            if (row != 8 && row % 3 == 2)
            { out += "-----------------------------\n"; }
        }
        return out;
    }

    public Cell debugGetCell(int r, int c)
    { return myBoard[r][c]; }

    public boolean isSolved()
    {
        for(Cell[] r : myBoard)
        {
            for(Cell c : r)
            {
                if(c.getVal() == 0)
                { return false; }
                if(c.debugNoPossibilities())
                { throw new NullPointerException("Cell at " + c.debugLocationString() + " doesn't have any possibilities"); }
            }
        }
        return true;
    }

    public void solve()
    {
        while(!isSolved())
        {
            System.out.println("aaa");
            boolean changed = false;
            //try simple solve where you look for single possibilities first
            for(Cell[] r : myBoard)
            {
                for(Cell c : r)
                {
                    if(c.getVal() == 0)
                    { changed = c.simpleSolve() || changed; }
                }
            }
            if(changed) { continue; }

            //next try solve where square is the only square that can be a number in a region
            for(Cell[] r : myBoard)
            {
                for(Cell c : r)
                {
                    if(c.getVal() == 0)
                    { changed = c.onlyOptionSolve() || changed; }
                }
            }
            if(changed) { continue; }

            //try other things to remove possibilities
            //else
            break;
        }
    }
}