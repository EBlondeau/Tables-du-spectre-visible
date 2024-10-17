package rainbow_tables.utils.wordgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class containing methods to generate random words with a set alphabet.
 * The alphabet consists of all characters allowed in passwords on most
 * websites.
 */
public class MotGenerator implements IWordGenerator {

    private Character[] alphabet;

    public MotGenerator() {
        this.alphabet = Alphabets.alphabetMinuscule;
    }

    /**
     * @param length longueur du mot a générer
     * @return un mot aléatoire de longueur length
     */

    public String motAleatoire(int length) {
        String mot = "";

        while (mot.length() < length) {
            Random r = new Random();
            r.nextInt(alphabet.length);
            mot += alphabet[r.nextInt(alphabet.length)];

        }

        return mot;
    }

    /**
     * @param length longueur du mot à générer
     * @param seed   seed du générateur d'aléatoire
     * @return mot aléatoire de longueur length
     */
    public String motAleatoire(int length, int seed) {
        String mot = "";
        int i = 0;
        Random r = new Random(seed);
        while (mot.length() < length) {
            r.nextInt(alphabet.length);
            mot += alphabet[r.nextInt(alphabet.length)];
            i++;
        }

        return mot;
    }

    /**
     * @param length longueur du mot à générer
     * @param c      première lettre du mot
     * @return mot aléatoire de longueur length et commencant par la lettre c
     */
    public String motAleatoire(int length, char c) {
        String mot = "" + c;

        while (mot.length() < length - 1) {
            Random r = new Random();
            r.nextInt(alphabet.length);
            mot += alphabet[r.nextInt(alphabet.length)];

        }

        return mot;
    }

    /**
     * @param length longueur du mot à générer
     * @param size   taille de la liste de mot
     * @return liste de size mot de longueur length
     */
    public List<String> ListeMot(int length, int size) {
        List<String> liste = new ArrayList<>();
        while (liste.size() < size) {
            liste.add(this.motAleatoire(length));

        }

        return liste;
    }

    /**
     * 
     * @param length longueur du mot
     * @param size   taille de la liste
     * @param c      premiere lettre des mots
     * @return liste de size mot de longueur length et commencant par c
     */
    public List<String> ListeMot(int length, int size, char c) {
        List<String> liste = new ArrayList<>();
        while (liste.size() < size) {
            liste.add(motAleatoire(length, c));

        }

        return liste;
    }

    public Character[] getAlphabet() {
        return this.alphabet;
    }
}
