package rainbow_tables.generation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import rainbow_tables.utils.hashfuncs.IHashFunction;
import rainbow_tables.utils.wordgen.IWordGenerator;
import rainbow_tables.utils.wordgen.MotGenerator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

public class HashGenerator {

    /**
     * @param hashFile   fichier d'éciture
     * @param sourceFile fichier source
     * @param hash       fonction de hashage
     *                   prend un fichier contentant du texte en entrée et écrit
     *                   dans un autre le texte passé dans une fonction de hashage
     *                   donné
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static void Hash(File hashFile, File sourceFile, IHashFunction hash)
            throws IOException, NoSuchAlgorithmException {
        BufferedReader br = new BufferedReader(new FileReader(sourceFile));
        Stream<String> lignes = br.lines();
        String[] lignesArray = lignes.toArray(String[]::new);
        long size = lignesArray.length;

        FileWriter wr = new FileWriter(hashFile);
        for (int i = 0; i < size; i++) {

            String hashtext = hash.hash(lignesArray[i]);
            wr.write(hashtext + "\n");
            // System.out.println(hashtext);

        }
        wr.close();
        br.close();

    }

    /**
     * @param hashFile fichier d'écriture
     * @param list     list source
     * @param hash     fonction de hashage
     *                 prend une liste de string en entrée et écrit
     *                 dans un autre le texte passé dans une fonction de hashage
     *                 donné
     * 
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static void Hash(File hashFile, List<String> list, IHashFunction hash)
            throws IOException, NoSuchAlgorithmException {

        FileWriter wr = new FileWriter(hashFile);
        for (int i = 0; i < list.size(); i++) {

            String hashtext = hash.hash(list.get(i));
            wr.write(hashtext + "\n");
            // System.out.println(hashtext);

        }
        wr.close();

    }

    /**
     * @param hashFile   fichier d'écriture
     * @param sourceFile fichier source
     * @param hash       fonction de hashage
     *                   prend un fichier contentant du texte en entrée et écrit
     *                   dans un autre le clair et le texte passé dans une fonction
     *                   de hashage
     *                   donné
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static void MDPetHash(File hashFile, File sourceFile, IHashFunction hash)
            throws IOException, NoSuchAlgorithmException {
        BufferedReader br = new BufferedReader(new FileReader(sourceFile));
        Stream<String> lignes = br.lines();
        String[] lignesArray = lignes.toArray(String[]::new);
        long size = lignesArray.length;

        FileWriter wr = new FileWriter(hashFile);
        for (int i = 0; i < size; i++) {

            String hashtext = hash.hash(lignesArray[i]);

            wr.write(lignesArray[i] + " " + hashtext + "\n");
            // System.out.println(lignesArray[i] + " " + hashtext);

        }
        wr.close();
        br.close();

    }

    /**
     * @param hashFile fichier d'écriture
     * @param list     list source
     * @param hash     fonction de hashage
     *                 prend une liste de string en entrée et écrit
     *                 dans un autre le clair et le texte passé dans une fonction
     *                 de hashage
     *                 donné
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static void MDPetHash(File hashFile, List<String> list, IHashFunction hash)
            throws IOException, NoSuchAlgorithmException {

        FileWriter wr = new FileWriter(hashFile);
        for (int i = 0; i < list.size(); i++) {

            String hashtext = hash.hash(list.get(i));

            wr.write(list.get(i) + " " + hashtext + "\n");
            // System.out.println(lignesArray[i]);

        }
        wr.close();

    }

    /**
     * @param hashFile   fichier d'écriture
     * @param sourceFile fichier source
     * @param hash       fonction de hashage
     * @param length     longueur choisis des mots
     *                   prend un fichier contentant du texte en entrée et écrit
     *                   dans un autre les mots de longueur choisi passé dans une
     *                   fonction
     *                   de hashage
     *                   donné
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static void Hash(File hashFile, File sourceFile, IHashFunction hash, int length)
            throws IOException, NoSuchAlgorithmException {
        BufferedReader br = new BufferedReader(new FileReader(sourceFile));
        Stream<String> lignes = br.lines();
        String[] lignesArray = lignes.toArray(String[]::new);
        long size = lignesArray.length;

        FileWriter wr = new FileWriter(hashFile);
        for (int i = 0; i < size; i++) {
            if (lignesArray[i].length() == length) {

                String hashtext = hash.hash(lignesArray[i]);
                wr.write(hashtext + "\n");
                System.out.println(hashtext);

                wr.write(lignesArray[i] + "\n");
            }

        }
        wr.close();
        br.close();

    }

    /**
     * @param hashFile fichier d'écriture
     * @param hash     fichier de hashage
     * @param lenght   longueur des mots aléatoires
     * @param size     nombre de mots
     *                 génère un nombre size de mots aléatoire de longueur lenghth
     *                 et les écrit ainsi que leur hash
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static void MDPetHash(File hashFile, IHashFunction hash, int lenght, int size)
            throws IOException, NoSuchAlgorithmException {

        FileWriter wr = new FileWriter(hashFile);
        IWordGenerator wordGenerator = new MotGenerator();
        for (int i = 0; i < size; i++) {
            String mot = wordGenerator.motAleatoire(lenght);
            String hashed = hash.hash(mot);
            wr.write(mot + " " + hashed + "\n");
        }

        wr.close();
    }

    /**
     * @param hashFile fichier d'écriture
     * @param hash     fichier de hashage
     * @param lenght   longueur des mots aléatoires
     * @param size     nombre de mots
     *                 génère un nombre size de mots aléatoire de longueur lenghth
     *                 et écrit leur hash
     * 
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static void Hash(File hashFile, IHashFunction hash, int lenght, int size)
            throws IOException, NoSuchAlgorithmException {

        FileWriter wr = new FileWriter(hashFile);
        IWordGenerator wordGenerator= new MotGenerator();
        for (int i = 0; i < size; i++) {
            String mot = wordGenerator.motAleatoire(lenght);
            String hashed = hash.hash(mot);
            wr.write(hashed + "\n");
        }

        wr.close();
    }
}
