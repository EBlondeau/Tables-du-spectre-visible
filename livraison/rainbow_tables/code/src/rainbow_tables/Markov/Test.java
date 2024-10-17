package rainbow_tables.Markov;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import rainbow_tables.utils.hashfuncs.IHashFunction;
import rainbow_tables.utils.hashfuncs.MD5;
import rainbow_tables.utils.hashfuncs.SHA1;
import rainbow_tables.utils.hashfuncs.SHA256;
import rainbow_tables.utils.hashfuncs.SHA3;
import rainbow_tables.utils.reduction.AReduction;
import rainbow_tables.utils.reduction.BasicReduction;

public class Test {
        protected static Character[] alphabet = { null, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                        'N', 'O',
                        'P',
                        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
                        't', 'u',
                        'v', 'w', 'x', 'y', 'z'

        };

        protected static Character[] alphabet2 = { null, '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',

        };
        protected static Character[] alphabet3 = { null, 'a', 'b', 'c',

        };
        protected static Character[] alphabet4 = { null, 'a', 'b'

        };
        protected static Character[] alphabet1 = { null, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                        'M',
                        'N', 'O',
                        'P',
                        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
                        't', 'u',
                        'v', 'w', 'x', 'y', 'z',
                        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                        '~', '`', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '-', '+', '=', '{', '[', '}',
                        ']', '|',
                        '\\', ':', ';', '"', '<', ',', '>', '.', '?', '/', '\'',
        };

        public static void main(String[] args) throws Exception {
                /*
                 * File f = new
                 * File("livraison/rainbow_tables/data/clear_Passwordlist/CommonPassword.txt");
                 * double[][] tab = TestMarkov.creationTableau(alphabet1);
                 * // System.out.println(Arrays.deepToString(tab));
                 * if (f.exists()) {
                 * TestMarkov.feedFromFile(tab, f, alphabet1);
                 * System.out.println("oui");
                 * System.out.println(Arrays.deepToString(tab));
                 * 
                 * System.out.println(TestMarkov.evaluate(tab, alphabet1, ","));
                 * }
                 * System.out.println("non");
                 * 
                 * String s = "aaaabbbcccaca";
                 * double[][] tab = TestMarkov.creationTableau(alphabet3);
                 * TestMarkov.parser(tab, alphabet3, s);
                 * TestMarkov.improbalite(tab, s.length());
                 * // System.out.println(Arrays.deepToString(tab));
                 * TreeNode t = TestMarkov.InitbuildTree(20.0, tab, alphabet3);
                 * /*
                 * Iterator<TreeNode> iterate = t.children.iterator();
                 * while (iterate.hasNext()) {
                 * System.out.println(iterate.next());
                 * System.out.println("alors peut etre ");
                 * }
                 * 
                 * 
                 * for (int i = 0; i < t.children.size(); i++) {
                 * TreeNode n = (TreeNode) t.children.get(i);
                 * Iterator<TreeNode> iterate = n.children.iterator();
                 * while (iterate.hasNext()) {
                 * System.out.println(iterate.next().getData().toString());
                 * System.out.println("alors peut etre ");
                 * }
                 * System.out.println("bah mon reuf");
                 * 
                 * System.out.println(n.getData() + " ta mere");
                 * 
                 * }
                 * 
                 * System.out.println(t.data);
                 */

                double[][] tab2 = {
                                { 1.0, 1.0, 1.0 },
                                { 1.0, 1.0, 1.0 },
                                { 1.0, 1.0, 1.0 }
                };
                TreeNode r = new TreeNode(null, 2);
                TestMarkov.BuildTreev2(tab2, alphabet4, r.getCapital(), r);
                /*
                 * for (TreeNode t : r.getChildren()) {
                 * System.out.print(t.getChar() + " ");
                 * for (TreeNode t2 : t.getChildren()) {
                 * System.out.print(t2.getChar() + " 2 ");
                 * }
                 * System.out.println();
                 * }
                 */

                // System.out.println(r2);

                TreeNode racine = new TreeNode(null, 0);
                TreeNode a1 = new TreeNode('a', 0);
                TreeNode b1 = new TreeNode('b', 0);
                racine.addChild(a1);
                racine.addChild(b1);

                TestMarkov.recurseTree(racine, "");
        }

}
