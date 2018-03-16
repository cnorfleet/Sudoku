import java.util.ArrayList;

public class Board
{
    private Cell[][] myBoard;
    private ArrayList<ArrayList<Cell>> allRegions;

    public Board(int[][] b)
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

        //set up allRegions
        allRegions = new ArrayList<>();
        for(Cell[] r : myBoard) //rows
        {
            ArrayList<Cell> row = new ArrayList<>();
            for(Cell c : r)
            { row.add(c); }
            allRegions.add(row);
        }
        for(int c = 0; c < 9; c++) //cols
        {
            ArrayList<Cell> col = new ArrayList<>();
            for(int r = 0; r < 9; r++)
            { col.add(myBoard[r][c]); }
            allRegions.add(col);
        }
        for(int r = 0; r < 3; r++) //boxes
        {
            for(int c = 0; c < 3; c++)
            {
                ArrayList<Cell> box = new ArrayList<>();
                for(int r2 = r*3; r2 < (r*3)+3; r2++)
                {
                    for (int c2 = c*3; c2 < (c*3)+3; c2++)
                    {
                        box.add(myBoard[r2][c2]);
                    }
                }
                allRegions.add(box);
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
        while(!isSolved()) //could also use while(true), which would reduce time for each loop but require running the loop an extra time
        {
            boolean changed = false;
            //simple solve where you look for single possibilities first
            for(Cell[] r : myBoard)
            {
                for(Cell c : r)
                {
                    if(c.getVal() == 0)
                    { changed = c.simpleSolve() || changed; }
                }
            }
            if(changed) { continue; }

            //solve where square is the only square that can be a number in a region
            for(Cell[] r : myBoard)
            {
                for(Cell c : r)
                {
                    if(c.getVal() == 0)
                    { changed = c.onlyOptionSolve() || changed; }
                }
            }
            if(changed) { continue; }

            ////other things to remove possibilities:
            //grouping pairs, trios, n-groups of spaces which only have n-groups of possibilities

            //grouping pairs, trios, n-groups of possibilities which are only fulfilled by n-groups of spaces
            for(ArrayList<Cell> region : allRegions)
            {
                //get possibilities in region
                ArrayList<Integer> p = new ArrayList<>();
                for(int i = 1; i <= 9; i++)
                {
                    for(Cell c : region)
                    {
                        if (c.hasPossibility(i))
                        { p.add(i); break; }
                    }
                }
                //create permutations of possibilities of size > 1
                ArrayList<ArrayList<Integer>> permutations = new ArrayList<>();
                for(int i = 0; i < p.size(); i++)
                {
                    ArrayList<ArrayList<Integer>> permutationsC = (ArrayList<ArrayList<Integer>>) permutations.clone();
                    for(ArrayList<Integer> a : permutationsC)
                    {
                        ArrayList<Integer> temp = (ArrayList<Integer>) a.clone();
                        temp.add(p.get(i));
                        permutations.add(temp);
                    }
                    for(int j = 0; j < i; j++)
                    {
                        ArrayList<Integer> temp = new ArrayList<>();
                        temp.add(p.get(j));
                        temp.add(p.get(i));
                        if (temp.size() != p.size())
                        { permutations.add(temp); }
                    }
                }
                //look for n-groups of spaces which correspond in size to n-group permutations
                for(ArrayList<Integer> permutation : permutations)
                {
                    //check each n-group
                    ArrayList<Cell> matching = new ArrayList<>();
                    for(Cell c : region)
                    {
                        if(c.getVal() != 0 || !containAnySame(c.getPossibilities(), permutation))
                        { continue; }
                        matching.add(c);
                    }
                    //if we only have exactly the right number of squares, then they must all be in the permutation
                    if(matching.size() <= permutation.size())
                    {
                        changed = true;
                        //figure out what other things the squares must not be
                        ArrayList<Integer> toRemove = new ArrayList<>();
                        for(int i : p)
                        {
                            if(!contains(permutation, i))
                            { toRemove.add(i); }
                        }
                        //remove those things
                        for(Cell c : matching)
                        {
                            for(int r : toRemove)
                            { c.removePossibility(r); }
                        }
                    }
                }
            }
            if(changed) { continue; }

            //else
            break;
        }
    }

    private boolean containAnySame(ArrayList<Integer> A, ArrayList<Integer> B)
    {
        for(int a : A)
        {
            for(int b : B)
            {
                if (a == b)
                { return true; }
            }
        }
        return false;
    }
    private boolean AContainsAnyNotInB(ArrayList<Integer> A, ArrayList<Integer> B)
    {
        for(int a : A)
        {
            boolean found = false;
            for(int b : B)
            {
                if(a == b)
                { found = true; break; }
            }
            if(!found)
            { return false; }
        }
        return true;
    }
    private boolean contains(ArrayList<Integer> A, int i)
    {
        for(int a : A)
        { if (i == a) { return true; } }
        return false;
    }
}