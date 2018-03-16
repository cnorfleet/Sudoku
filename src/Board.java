import java.util.ArrayList;
import java.util.InputMismatchException;

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

    public boolean isSolved()
    {
        for(Cell[] r : myBoard)
        {
            for(Cell c : r)
            {
                if(c.getVal() == 0 && c.getPossibilities().size() == 0)
                { throw new NullPointerException("Cell at " + c.getLocationString() + " doesn't have any possibilities"); }
                if(c.getVal() == 0)
                { return false; }
            }
        }
        return true;
    }
    public boolean isValid()
    {
        for(ArrayList<Cell> region : allRegions)
        {
            for(Cell c : region)
            {
                for(Cell c2 : region)
                {
                    if (c.getVal() != 0 && c != c2 && c.getVal() == c2.getVal())
                    { return false; }
                }
            }
        }
        return true;
    }

    public void solve()
    {
        while(!isSolved())
        {
            if(!isValid()) { break; }
            if(simpleSolve()) { continue; }
            if(onlyOptionSolve()) { continue; }
            if(groupingSpacesSolve()) { continue; }
            if(groupingPossibilitiesSolve()) { continue; }
            //if all else fails...
            bruteForceSolve();
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
    private boolean contains(ArrayList<Integer> A, int i)
    {
        for(int a : A)
        { if (i == a) { return true; } }
        return false;
    }

    public void setCellVal(int r, int c, int val)
    { myBoard[r][c].setVal(val); }

    public Board deepClone()
    {
        //make integer array from board
        int[][] a = new int[9][9];
        for(int r = 0; r < 9; r++)
        {
            for(int c = 0; c < 9; c++)
            { a[r][c] = myBoard[r][c].getVal(); }
        }
        //make new board
        return new Board(a);
    }
    public Cell[][] getBoard()
    { return myBoard; }
    public ArrayList<ArrayList<Cell>> getAllRegions()
    { return allRegions; }

    //simple solve where you look for single possibilities first
    private boolean simpleSolve()
    {
        boolean changed = false;
        for(Cell[] r : myBoard)
        {
            for(Cell c : r)
            {
                if(c.getVal() == 0)
                { changed = c.simpleSolve() || changed; }
            }
        }
        return changed;
    }
    //solve where square is the only square that can be a number in a region
    private boolean onlyOptionSolve()
    {
        boolean changed = false;
        for(Cell[] r : myBoard)
        {
            for(Cell c : r)
            {
                if(c.getVal() == 0)
                { changed = c.onlyOptionSolve() || changed; }
            }
        }
        return changed;
    }
    ////other things to remove possibilities:
    //grouping pairs, trios, n-groups of spaces which only have n-groups of possibilities
    private boolean groupingSpacesSolve()
    {
        boolean changed = false;
        for(ArrayList<Cell> region : allRegions)
        {
            //get unsolved cells in regions
            ArrayList<Cell> cells = new ArrayList<>();
            for (Cell c : region)
            {
                if(c.getVal() == 0)
                { cells.add(c); }
            }
            //create permutations of cells with size > 1
            ArrayList<ArrayList<Cell>> permutations = new ArrayList<>();
            for(int i = 0; i < cells.size(); i++)
            {
                ArrayList<ArrayList<Cell>> permutationsC = (ArrayList<ArrayList<Cell>>) permutations.clone();
                for(ArrayList<Cell> a : permutationsC)
                {
                    ArrayList<Cell> temp = (ArrayList<Cell>) a.clone();
                    temp.add(cells.get(i));
                    permutations.add(temp);
                }
                for(int j = 0; j < i; j++)
                {
                    ArrayList<Cell> temp = new ArrayList<>();
                    temp.add(cells.get(j));
                    temp.add(cells.get(i));
                    if (temp.size() != cells.size())
                    { permutations.add(temp); }
                }
            }
            //look for n-groups of permutations which correspond in size to n-group cells
            for(ArrayList<Cell> permutation : permutations)
            {
                //check each n-group
                ArrayList<Integer> allPos = new ArrayList<>();
                for (Cell c : permutation)
                {
                    for(Integer p : c.getPossibilities())
                    {
                        if(!contains(allPos, p))
                        { allPos.add(p); }
                    }
                }
                //if we have exactly the right number of possibilities, then only these squares can be the possibilities
                if(allPos.size() < permutation.size())
                { throw new InputMismatchException("This Board is Invalid"); }
                if(allPos.size() == permutation.size())
                {
                    //figure out what other squares we have in this region
                    ArrayList<Cell> toRemoveFrom = new ArrayList<>();
                    for(Cell c : cells)
                    {
                        if(!permutation.contains(c))
                        { toRemoveFrom.add(c); }
                    }
                    //remove these possibilities from the other squares
                    for(Cell c : toRemoveFrom)
                    {
                        for(int r : allPos)
                        {
                            if(c.containsPossibility(r))
                            { changed = true; }
                            c.removePossibility(r);
                        }
                    }
                }
            }
        }
        return changed;
    }
    //grouping pairs, trios, n-groups of possibilities which are only fulfilled by n-groups of spaces
    private boolean groupingPossibilitiesSolve()
    {
        boolean changed = false;
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
            //create permutations of possibilities with size > 1
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
                if(matching.size() < permutation.size())
                { throw new InputMismatchException("This Board is Invalid"); }
                if(matching.size() == permutation.size())
                {
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
                        {
                            if(c.containsPossibility(r))
                            { changed = true; }
                            c.removePossibility(r);
                        }
                    }
                }
            }
        }
        return changed;
    }
    //brute force algorithm
    private void bruteForceSolve()
    {
        //find cell with fewest possibilities
        int min = 10; Cell minCell = null;
        for(Cell[] r : myBoard)
        {
            for(Cell c : r)
            {
                if(c.getVal() == 0 && c.getPossibilities().size() < min)
                { minCell = c; min = c.getPossibilities().size(); }
            }
        }
        ArrayList<Integer> possibilities = minCell.getPossibilities();
        int r = minCell.getRow();
        int c = minCell.getCol();
        for(int p : possibilities)
        {
            Board newBoard = this.deepClone();
            newBoard.setCellVal(r,c,p);
            try { newBoard.solve(); }
            catch (Exception e) { continue; }
            if(newBoard.isSolved() && newBoard.isValid())
            {
                myBoard = newBoard.getBoard();
                allRegions = newBoard.getAllRegions();
                return;
            }
        }
        throw new InputMismatchException("This Board is Not Invalid");
    }
}