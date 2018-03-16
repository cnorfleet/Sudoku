import static org.junit.Assert.*;
import org.junit.Test;
import java.io.FileNotFoundException;

public class SudokuTest
{
    @Test
    public void testSolveBlank() {
        int[][] b = {{0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0}};
        Board B = new Board(b);
        B.solve(true);
        assertTrue(B.isValid() && B.isSolved() && B.foundMultSolutions);
    }
    @Test
    public void testSolve1() {
        int[][] b = {{6,0,0,1,0,8,2,0,3},
                {0,2,0,0,4,0,0,9,0},
                {8,0,3,0,0,5,4,0,0},
                {5,0,4,6,0,7,0,0,9},
                {0,3,0,0,0,0,0,5,0},
                {7,0,0,8,0,3,0,0,2},
                {0,0,1,7,0,0,9,0,6},
                {0,8,0,0,3,0,0,2,0},
                {3,0,2,9,0,4,0,0,5}};
        int[][] s = {{6,4,5,1,9,8,2,7,3},
                {1,2,7,3,4,6,5,9,8},
                {8,9,3,2,7,5,4,6,1},
                {5,1,4,6,2,7,3,8,9},
                {2,3,8,4,1,9,6,5,7},
                {7,6,9,8,5,3,1,4,2},
                {4,5,1,7,8,2,9,3,6},
                {9,8,6,5,3,1,7,2,4},
                {3,7,2,9,6,4,8,1,5}};
        Board B = new Board(b);
        B.solve(true);
        assertTrue(B.equals(new Board(s)) && B.foundUniqueSolution);
    }
    @Test
    public void testSolve2() {
        int[][] b = {{5,3,0,0,7,0,0,0,0},
                {6,0,0,1,9,5,0,0,0},
                {0,9,8,0,0,0,0,6,0},
                {8,0,0,0,6,0,0,0,3},
                {4,0,0,8,0,3,0,0,1},
                {7,0,0,0,2,0,0,0,6},
                {0,6,0,0,0,0,2,8,0},
                {0,0,0,4,1,9,0,0,5},
                {0,0,0,0,8,0,0,7,9}};
        int[][] s = {{5,3,4,6,7,8,9,1,2},
                {6,7,2,1,9,5,3,4,8},
                {1,9,8,3,4,2,5,6,7},
                {8,5,9,7,6,1,4,2,3},
                {4,2,6,8,5,3,7,9,1},
                {7,1,3,9,2,4,8,5,6},
                {9,6,1,5,3,7,2,8,4},
                {2,8,7,4,1,9,6,3,5},
                {3,4,5,2,8,6,1,7,9}};
        Board B = new Board(b);
        B.solve(true);
        assertTrue(B.equals(new Board(s)) && B.foundUniqueSolution);
    }
    @Test
    public void testSolve3() {
        int[][] b = {{0,0,0,9,0,7,0,0,0},
                {9,0,0,0,0,0,0,0,8},
                {0,3,0,4,0,5,0,2,0},
                {3,0,7,0,4,0,2,0,6},
                {0,0,0,5,0,9,0,0,0},
                {8,0,9,0,2,0,1,0,3},
                {0,7,0,6,0,4,0,3,0},
                {2,0,0,0,0,0,0,0,9},
                {0,0,0,1,0,2,0,0,0}};
        int[][] s = {{4,8,2,9,1,7,3,6,5},
                {9,1,5,2,6,3,4,7,8},
                {7,3,6,4,8,5,9,2,1},
                {3,5,7,8,4,1,2,9,6},
                {6,2,1,5,3,9,8,4,7},
                {8,4,9,7,2,6,1,5,3},
                {1,7,8,6,9,4,5,3,2},
                {2,6,4,3,5,8,7,1,9},
                {5,9,3,1,7,2,6,8,4}};
        Board B = new Board(b);
        B.solve(true);
        assertTrue(B.equals(new Board(s)) && B.foundUniqueSolution);
    }
    @Test
    public void testSolve4() {
        int[][] b = {{0,0,0,0,3,7,6,0,0},
                {0,0,0,6,0,0,0,9,0},
                {0,0,8,0,0,0,0,0,4},
                {0,9,0,0,0,0,0,0,1},
                {6,0,0,0,0,0,0,0,9},
                {3,0,0,0,0,0,0,4,0},
                {7,0,0,0,0,0,8,0,0},
                {0,1,0,0,0,9,0,0,0},
                {0,0,2,5,4,0,0,0,0}};
        int[][] s = {{9,5,4,1,3,7,6,8,2},
                {2,7,3,6,8,4,1,9,5},
                {1,6,8,2,9,5,7,3,4},
                {4,9,5,7,2,8,3,6,1},
                {6,8,1,4,5,3,2,7,9},
                {3,2,7,9,6,1,5,4,8},
                {7,4,9,3,1,2,8,5,6},
                {5,1,6,8,7,9,4,2,3},
                {8,3,2,5,4,6,9,1,7}};
        Board B = new Board(b);
        B.solve(true);
        assertTrue(B.equals(new Board(s)) && B.foundUniqueSolution);
    }
    @Test
    public void testFileSolve50Unique() throws FileNotFoundException {
        int count = 0, countUnique = 0;
        for (int i = 1; i <= 50; i++)
        {
            Board b = new Board(Sudoku.getInputFromFile("tests/testBoards/" + i + ".txt"));
            b.solve();
            if (b.isSolved())
            {
                count++;
                if(b.foundUniqueSolution)
                { countUnique++; }
            }
        }
        assertTrue(count == 50);
        assertTrue(countUnique == 50);
    }
}