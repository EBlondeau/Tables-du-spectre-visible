package rainbow_tables.utils.wordgen;

public interface IWordGenerator {
    
    public String motAleatoire(int length);
    public String motAleatoire(int length, int seed);
    public Character[] getAlphabet();
    
}
