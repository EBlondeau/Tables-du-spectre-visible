package rainbow_tables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Stream;

import rainbow_tables.utils.hashfuncs.IHashFunction;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

public class Trilongueur {

    File hashFile = new File("livraison/rainbow_tables/tables/MD5.txt");
    File f = new File(
            "/home/jules/Documents/Java/Projet_2/les-tables-du-spectre-visible/livraison/rainbow_tables/tables/liste_francais.txt");

    public static void Hash(File hashFile, File sourceFile, IHashFunction hash, int length)
            throws IOException, NoSuchAlgorithmException {
        BufferedReader br = new BufferedReader(new FileReader(sourceFile));
        Stream<String> lignes = br.lines();
        String[] lignesArray = lignes.toArray(String[]::new);
        long size = lignesArray.length;

        FileWriter wr = new FileWriter(hashFile);
        for (int i = 0; i < size; i++) {
            if (lignesArray[i].length() == length) {
                /*
                 * String hashtext=hash.hash(lignesArray[i]);
                 * wr.write(hashtext+"\n");
                 * System.out.println(hashtext);
                 */
                wr.write(lignesArray[i] + "\n");
            }

        }
        wr.close();
        br.close();

    }

}
