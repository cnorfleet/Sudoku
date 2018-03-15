public class Sudoku
{
    public static void main(String[] args)
    {
        Board b = new TestBoard(4);
        System.out.print("\n" + b);
        b.solve();
        System.out.println("\n-----------------------------\n-----------------------------");
        System.out.print("\n" + b);
    }

    public static int[][] getInputFromTyping()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter board:");
        int[][] board = new int[9][9];
        for (int row = 0; row < 9; row++)
        {
            try
            {
                String[] temp = scanner.nextLine().split(",");
                for (int col = 0; col < 9; col++)
                {
                    if (temp[col].equals("") || temp[col].equals(" "))
                    { board[row][col] = 0; } //filling holes
                    else { board[row][col] = Integer.valueOf(temp[col].trim()); }
                }
            }
            catch (Exception e)
            { throw e; }
        }
        return board;
    }
}