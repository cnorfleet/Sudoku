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
}