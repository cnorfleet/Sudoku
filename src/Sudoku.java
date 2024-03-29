import java.io.*;
import java.util.Scanner;

public class Sudoku
{
    public static void main(String[] args) throws FileNotFoundException
    {
        Board b = new Board(getInputFromFile("board.txt"));
        System.out.print("\n" + b);
        b.solve();
        System.out.println("\n-----------------------------\n-----------------------------");
        System.out.print("\n" + b); //*/
    }

    public static int[][] getInputFromTyping()
    {
        Scanner scanner = new Scanner(System.in);
        int[][] board = new int[9][9];
        System.out.println("Please enter board:");
        for (int r = 0; r < 9; r++)
        {
            String line = scanner.nextLine();
            for (int c = 0; c < 9; c++)
            {
                if (line.length() == 0)
                { board[r][c] = 0; continue; }
                char n = line.charAt(0);
                line = line.substring(1).trim();
                if (n == ' ')
                { board[r][c] = 0; }
                else { board[r][c] = Integer.valueOf("" + n); }
            }
        }
        return board;
    }
    public static int[][] getInputFromFile(String fileName) throws FileNotFoundException
    {
        Scanner inputFile = new Scanner(new File(fileName));
        int[][] board = new int[9][9];
        for (int r = 0; r < 9; r++)
        {
            String line = inputFile.nextLine().trim();
            for (int c = 0; c < 9; c++)
            {
                board[r][c] = Integer.valueOf("" + line.charAt(0));
                line = line.substring(1).trim();
            }
        }
        inputFile.close();
        return board;
    }
}