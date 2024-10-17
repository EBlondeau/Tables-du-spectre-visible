package rainbow_tables.Markov;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Stream;

/*

un parser qui récupere le caractere actuel
un buffer qui garde la valeur précédente du parser
un tableau contenant (colonne et lignes) tout les caractere possible et le caractere vide (pour la premiere lettre)
    caractere lu en colonne
    buffer en ligne
une variable gardant le nombre de caractere parcouru

a chaque caracterer lu on rajoute 1 dans la case qui correspond
a la fin on parcours le tableau et on coverti chaque valeur pour avoir l'improbalité plutot que la proba


génération des mots de passe 
capitale d'improbalité




*/
public class TestMarkov {

    protected static Character[] alphabet1 = { null, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O',
            'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
            'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '~', '`', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '-', '+', '=', '{', '[', '}', ']', '|',
            '\\', ':', ';', '"', '<', ',', '>', '.', '?', '/'
    };

    protected static Character[] alphabetminuscule = {
            null, 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u',
            'v', 'w', 'x', 'y', 'z'
    };

    protected static Character[] alphabetmajuscule = { null, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M',
            'N', 'O',
            'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
    };

    protected static Character[] alphabetaplhanumerique = { null, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
            'L',
            'M', 'N', 'O',
            'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
            'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    };

    /**
     * fonction crééant un tableau servant pour l'evalution de l'improbabilité
     * 
     * @param alphabet alphabet utilisé (contenant le "")
     * @return tableau carré rempli de 1 de coté de taille de l'alphabet
     */
    public static double[][] creationTableau(Character[] alphabet) {
        double[][] tab = new double[alphabet.length][alphabet.length];
        for (int i = 0; i < alphabet.length; i++) {
            for (int j = 0; j < alphabet.length; j++) {
                tab[i][j] = 1;
            }
        }
        return tab;

    }

    /**
     * fonction prenant un string et mettant a jour le tableau en fonction du string
     * en rajoutant +1 a chaque occurence d'une combinaison precedent+actuel
     * 
     * @param tab      tableau initialisé par créationTableau
     * @param alphabet alphabet utilisé (contenant le "")
     * @param entry    string d'entrée
     * @return double[][]
     */
    public static double[][] parser(double[][] tab, Character[] alphabet, String entry) {
        Character current = entry.charAt(0);
        Character previous = null;
        tab[Arrays.asList(alphabet).indexOf(previous)][Arrays.asList(alphabet).indexOf(current)] += 1;

        for (int i = 1; i < entry.length(); i++) {

            current = entry.charAt(i);
            previous = entry.charAt(i - 1);
            if (Arrays.asList(alphabet).indexOf(current) != -1) {
                tab[Arrays.asList(alphabet).indexOf(previous)][Arrays.asList(alphabet).indexOf(current)] += 1;

            }

        }
        return tab;
    }

    /**
     * fonction convertissant les entier du tableau en improbabilité
     *
     * @param tab  tableau initialisé par créationTableau
     * @param size nombre de caractere lu pour la mise a jour du tableau
     */
    public static void improbalite(double[][] tab, int size) {
        for (int k = 0; k < tab[0].length; k++) {
            for (int j = 0; j < tab[0].length; j++) {
                tab[k][j] = (-10 * Math.log10(tab[k][j] / size));
            }
        }

    }

    /**
     * fonction evaluant une entrée et renvoyant le score obtenu apres lecture du
     * tableau
     * 
     * @param tab      tableau initialisé par creationTableau
     * @param alphabet alphabet utilisé (contenant le "")
     * @param entry    string d'entrée
     * @return score du string
     */
    public static double evaluate(double[][] tab, Character[] alphabet, String entry) {
        Character current = entry.charAt(0);
        Character previous = null;
        double score = 0.0;
        score += tab[Arrays.asList(alphabet).indexOf(previous)][Arrays.asList(alphabet).indexOf(current)];
        for (int i = 1; i < entry.length(); i++) {
            current = entry.charAt(i);
            previous = entry.charAt(i - 1);
            score += tab[Arrays.asList(alphabet).indexOf(previous)][Arrays.asList(alphabet).indexOf(current)];
        }

        return score;
    }

    /**
     * focntion permettant de donner un fichier texte en entrée de mettre a jour le
     * tableau grace au contenu du fichier
     * 
     * @param tab      tableau initialisé par creationTableau
     * @param f        fichier source
     * @param alphabet alphabet utilisé (contenant le "")
     * @return le tableau mis a jour
     * @throws FileNotFoundException
     */
    public static double[][] feedFromFile(double[][] tab, File f, Character[] alphabet)
            throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(f));

        Stream<String> lignes = br.lines();
        String[] lignesArray = lignes.toArray(String[]::new);
        long size = lignesArray.length;
        int total = 0;
        String[] temp = null;
        double[][] tab1 = tab;
        for (int i = 0; i < size; i++) {
            temp = lignesArray[i].split(" ");
            for (int j = 0; j < temp.length; j++) {
                total += temp[j].length();
                tab1 = parser(tab1, alphabet, temp[j]);
            }

        }
        improbalite(tab1, total);
        return tab1;
    }
    /*
     * public static TreeNode InitbuildTree(double maxImpro, double[][] tab,
     * Character[] alphabet) {
     * double currentImpro = maxImpro;
     * TreeNode tree = new TreeNode(null);
     * 
     * BuildTree(tree, currentImpro, tab, alphabet);
     * 
     * for (int i = 0; i < tree.children.size(); i++) {
     * System.out.println((TreeNode) tree.children.get(0));
     * 
     * BuildTree((TreeNode) tree.children.get(i), currentImpro, tab, alphabet);
     * }
     * 
     * return tree;
     * 
     * }
     * 
     * public static TreeNode BuildTree(TreeNode tree, double Impro, double[][] tab,
     * Character[] alphabet) {
     * System.out.println(Arrays.asList(alphabet).indexOf(tree.getData()) +
     * " let me out " + tree.getData());
     * double currentImpro = Impro;
     * for (int i = 0; i < alphabet.length; i++) {
     * if (currentImpro - tab[Arrays.asList(alphabet).indexOf(tree.getData())][i] >=
     * 0) {
     * // TreeNode node = new TreeNode<String>(String.valueOf(alphabet[i]));
     * System.out.println(tree.getData() + " BUILD ");
     * tree.addChild(new TreeNode(String.valueOf(alphabet[i])));
     * // BuildTree((TreeNode) tree.children.get(i) currentImpro -
     * // tab[Arrays.asList(alphabet).indexOf(tree.getData())][i], tab, alphabet);
     * }
     * }
     * 
     * return tree;
     * 
     * }
     * 
     * 
     * public static void BuildTree(double[][] tab, Character[] alphabet, double
     * capital) {
     * int sizeOfTab = tab.length;
     * TreeNode tree = new TreeNode(null, capital);
     * for (int j = 0; j < sizeOfTab; j++) {
     * char previous = alphabet[j];
     * for (int i = 0; i < sizeOfTab; i++) {
     * if (tab[i][j] - capital >= 0) {
     * TreeNode Child = new TreeNode(alphabet[i], 0);
     * 
     * tree.addChild(Child);
     * Child.setCapital(Child.parent.getCapital() - tab[i][j]);
     * 
     * }
     * 
     * }
     * 
     * }
     * }
     */

    /**
     * fonction permettant de créer un arbre a partir du tableau
     * 
     * @param tab      tableau initialisé par creationTableau
     * @param alphabet alphabet utilisé (contenant le "")
     * @param capital  capital d'improbabilité donné
     * @param j        ligne actuel
     * @param r        treenode stockant les résultat
     * @return
     */
    public static TreeNode BuildTree(double[][] tab, Character[] alphabet, double capital, int col,
            TreeNode r) {
        int sizeOfTab = tab.length;

        if (col < sizeOfTab) {

            for (int ligne = 0; ligne < sizeOfTab; ligne++) {
                if (capital - tab[ligne][col] >= 0) {
                    // création d'un noeuds enfant
                    TreeNode Child = new TreeNode(alphabet[col], capital - tab[ligne][col]);
                    // on ajoute a le noeuds a son parent
                    r.addChild(BuildTree(tab, alphabet, Child.getCapital(), col + 1, Child));

                }
            }

        }
        return r;
    }

    public static void BuildTreev2(double[][] tab, Character[] alphabet, double capital,
            TreeNode parent) {
        int sizeOfTab = tab.length;

        int p = Arrays.asList(alphabet).indexOf(parent.getChar());

        for (int i = 0; i < sizeOfTab; i++) {
            if (capital - tab[p][i] >= 0) {
                TreeNode n = new TreeNode(alphabet[i], capital - tab[p][i]);
                parent.addChild(n);
                BuildTreev2(tab, alphabet, capital - tab[p][i], n);

            }
        }

    }

    /**
     * fonction d'exploration en profondeur de l'arbre : chaque chemin jusqu'a une
     * feuille correspond a un mot possible
     * 
     * 
     * @param r noeud de l'arbre initialisé par BuildTree
     * @return string contenant tout les lettres dans les noeuds de l'arbre
     */
    public static void explorer(TreeNode r) {
        System.out.print(r.getChar());

        for (TreeNode t : r.getChildren()) {
            System.out.println(" || ");
            explorer(t);
        }

    }

    public static void recurseTree(TreeNode obj, String indent1) {
        System.out.println(indent1 + obj.getChar());
        if (obj.getChildren().size() > 0) {
            for (int i = 0; i < obj.getChildren().size(); i++) {
                recurseTree(obj.getChildren().get(i), "   ");
            }
        }
    }
}
