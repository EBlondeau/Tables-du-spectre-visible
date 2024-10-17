package rainbow_tables.utils.hashfuncs;

public class Main {
    public static void main(String[] args) {
        PearsonHashing p= new PearsonHashing();
        System.out.println(p.hash("Hello, wordl!"));
    }
}
