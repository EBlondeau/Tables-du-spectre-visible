package rainbow_tables.generation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

import rainbow_tables.utils.hashfuncs.IHashFunction;
import rainbow_tables.utils.reduction.AReduction;
import rainbow_tables.utils.reduction.SeededReduction;
import rainbow_tables.utils.wordgen.IWordGenerator;
import rainbow_tables.utils.wordgen.MotGenerator;

/**
 * Classic Rainbow Table Generator
 * Has a number of iteration, an assigned hashing method and a list of reduction
 * method.
 */
public class RainbowTableGenerator {

    /**
     * Number of iteration through the list of reduction method of the Rainbow Table
     */
    private int nbIteration;
    /**
     * Hash function used to encode the rainbow table
     */
    private IHashFunction hashMethod;

    /**
     * List of reduction methods, also called Colors
     */
    private ArrayList<AReduction> reductionMethod;

    private IWordGenerator wordGenerator;

    /**
     * Path of the rainbow tables
     */
    public final static String rTablesPath="../data/rainbowtables/";

    /**
     * Subpath of rainbow tables.
     * Used to aggregate all rTables created in a test
     * into one folder.
     */
    private String rTableSubPath;

    /**
     * Constructor for the RainbowTable generator that doesnt take any reduction
     * method as a parameter,
     * Reductions are created in the generate() method
     * 
     * @param hashMethod  the hash method used to encode the Rainbow tabmle
     */
    public RainbowTableGenerator(IHashFunction hashMethod) {
        this(hashMethod, "", new MotGenerator());
        //Remainings of simpler times.
        //Reduction list is now adjusted on each rTable generation.
       /*int offset = -1;
        this.reductionMethod = new ArrayList<>();

        // Creates the list of reductions.
        for (int i = 0; i <= nbIteration; i++) {
            this.reductionMethod.add(new SeededReduction(offset++));
        }*/
    }
    
    /**
     * Constructor for the rainbowTable generator
     * @param hashMethod the hashMethod used in the rainbowtable generator
     * @param rTableSubPath the subpath of the rTable
     * @param wordGenerator the word generator used in the rTable
     */
    public RainbowTableGenerator(IHashFunction hashMethod, String rTableSubPath, IWordGenerator wordGenerator){
        this.hashMethod=hashMethod;
        this.rTableSubPath=rTableSubPath;
        this.wordGenerator=wordGenerator;
    }

    /**
     * Generates a rainbow table using the parameters of this instance.
     * Puts it in a file with a signature of:
     * "<hashMethode name> _ <nb of colors> _ <nb of lines> _ <hashCode of the clear
     * passwords file>.rbt"
     * 
     * @param passwordClear the clear passwords file used to generate the table
     * @return true if the operation succeeded, false otherwise.
     */
    public String generate(File passwordClear) {
        // Create an name for the file, also serves as an identifier.
        String fName = "" + hashMethod.getClass().getSimpleName() + "_" + nbIteration + "_" + passwordClear.hashCode()
                + ".rbt";

        // Try creating the file and do things with it
        try {

            File rTable = new File(rTablesPath + rTableSubPath + fName);

            // Try creating the file
            if (rTable.createNewFile()) {
                System.out.println("File Created: " + fName);
            } else {
                System.out.println("File already exists.");
            }

            // Init writer on the rainbow table file and reader on the clear password data
            // file
            FileWriter rWriter = new FileWriter(rTable);
            BufferedReader pReader = new BufferedReader(new FileReader(passwordClear));

            // Init variables for password and hashed
            String pass;
            String hashed = "";
            int nbLine = 0;

            // Read the file line by line until what's read is equals to null
            while ((pass = pReader.readLine()) != null) {
                String nPass = pass;
                // Hash/Reduce/Hash/Reduce... nbIterations*nbColors.
                for (int i = 0; i <= this.nbIteration; i++) {
                    hashed = this.hashMethod.hash(nPass);
                    String reduced = this.reductionMethod.get(i % reductionMethod.size()).reduce(hashed, pass.length());
                    nPass = reduced;
                }
                // Write the clear password and the superreduced hash.
                rWriter.write(pass + " " + nPass + "\n");
                nbLine++;
            }

            Path srcPath = Paths.get(rTablesPath + rTableSubPath + fName);

            rWriter.close();
            pReader.close();

            try {
                String fName2 = "" + hashMethod.getClass().getSimpleName() + "_" + nbIteration + "_" + nbLine + "_"
                        + passwordClear.hashCode() + ".rbt";
                Files.move(srcPath, srcPath.resolveSibling(fName2));
                System.out.println("File successfuly renamed to" + fName2);
            } catch (Exception e) {
                System.err.println("Couldnt rename file");
                e.printStackTrace();
            }

            return fName;
        } catch (IOException e) {
            System.out.println("An error has occured");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Generates a rainbow table using the parameters of this instance.
     * 
     * Puts it in a folder with a signature of:
     * "<hashMethode name> _ <starting size of passwords> _ <numbers of diff sizes
     * of passwords> _ <a random UUID>"
     * In this folder will be a bunch of .rbt files with this signature:
     * "<hashMethod name> _ <length of passwords in this file> _ <colors> _ <nb of
     * lines>";
     * 
     * @param gen                the generation method
     * @param startLength        the starting length of passwords
     * @param colorPerPassLength list of colors to have different number of color
     *                           per password length
     * @param sizePerPassLength  list of sizes to have different number of lines per
     *                           password length
     * @return true if the operation succeeded, false otherwise.
     */
    public String generate(int startLength, int[] colorPerPassLength, int[] sizePerPassLength) {

        // Check if the arguments are correct
        int sizePLength = 0;
        int sizeCLength = 0;
        for (int i : sizePerPassLength) {
            sizePLength++;
        }
        for (int i : colorPerPassLength) {
            sizeCLength++;
        }

        if (sizePLength != sizeCLength) {
            System.err.println("Error: length lists must be of the same size");
            return null;
        }

        //

        int max=0;
        for(int i: colorPerPassLength){
            if(i>max) max=i;
        }

        this.nbIteration=max;
        this.setReductions(max);

        String fName = "" + hashMethod.getClass().getSimpleName() + "_" + startLength + "_" + sizePLength + "_" +
                UUID.randomUUID().toString();

        try {
            Path rTable = Paths.get(rTablesPath + rTableSubPath + fName + "/");

            // Try creating the directory
            if (Files.createDirectories(rTable) != null) {
                System.out.println("Folder Created: " + fName);
            } else {
                System.out.println("Folder already exists.");
            }

            // Iterate through the list of sizes of password
            // Create a file with the convenient informations in the name for
            // each size of password.
            for (int k = 0; k < sizePLength; k++) {

                // The current password length. Made it so that we can start at a password length other than 0
                int currentPLength = k + startLength;

                String pieceName = "" + hashMethod.getClass().getSimpleName() + "_" + (currentPLength) + "_"
                        + colorPerPassLength[k] + "_" + sizePerPassLength[k] + "_" + 
                        wordGenerator.getClass().getSimpleName() + "_" + ".rbt";

                File rTablePiece = new File(rTablesPath + rTableSubPath + fName + "/" + pieceName);

                // Try to create the new file.
                if (rTablePiece.createNewFile()) {
                    System.out.println("File created: " + pieceName);
                } else {
                    System.out.println("File already exists.");
                }

                // Init writer on the rainbow table file
                FileWriter rWriterPiece = new FileWriter(rTablePiece);

                // Init the hashed and password variables.
                String hashed = "";
                String pass;

                // Iterate from 0 to the size defined at sizePerPassLength[k]
                // Create a random password of the size  currentPassLength
                for (int i = 0; i <= sizePerPassLength[k]; i++) {
                    pass = this.wordGenerator.motAleatoire(currentPLength);
                    String nPass = pass;
                    // Do the reduction colorPerPassLength[k] times
                    for (int j = 0; j <= colorPerPassLength[k]; j++) {
                        hashed = this.hashMethod.hash(nPass);
                        String reduced = this.reductionMethod.get(j % reductionMethod.size()).reduce(hashed,
                                currentPLength);
                        nPass = reduced;
                    }
                    // Once done, write on the line the initial randomly created password
                    // in the first column, and the same password gone through X hash->reduction cycle
                    // in the second column;
                    rWriterPiece.write(pass + " " + nPass + "\n");
                }
                rWriterPiece.close();
            }
            return fName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Generates a rainbow table using the parameters of this instance.
     * 
     * Puts it in a folder with a signature of:
     * "<hashMethode name> _ <starting size of passwords> _ <numbers of diff sizes
     * of passwords> _ <a random UUID>"
     * In this folder will be a bunch of .rbt files with this signature:
     * "<hashMethod name> _ <length of passwords in this file> _ <colors> _ <nb of
     * lines>";
     * 
     * @param gen               the generation method
     * @param startLength       the starting length of passwords
     * @param colors            number of colors
     * @param sizePerPassLength list of sizes to have different number of lines per
     *                          password length
     * @return true if the operation succeeded, false otherwise.
     */
    public String generate(int startSize, int colors, int[] sizePerPassLength) {
        int listSize = 0;
        for (int i : sizePerPassLength) {
            listSize++;
        }
        int[] colorlist = new int[listSize];
        for (int i = 0; i < listSize; i++) {
            colorlist[i] = colors;
        }
        return this.generate(startSize, colorlist, sizePerPassLength);
    }

    public String generate(int startSize, int colors, int size) {
        return this.generate(startSize, new int[] {colors}, new int[] {size});
    }

    /**
     * Generates a rainbow table using the parameters of this instance,
     * starting with a password length of 1.
     * 
     * Puts it in a folder with a signature of:
     * "<hashMethode name> _ <starting size of passwords> _ <numbers of diff sizes
     * of passwords> _ <a random UUID>"
     * In this folder will be a bunch of .rbt files with this signature:
     * "<hashMethod name> _ <length of passwords in this file> _ <colors> _ <nb of
     * lines>";
     * 
     * @param gen                the generation method
     * @param colorPerPassLength list of colors to have different number of color
     *                           per password length
     * @param sizePerPassLength  list of sizes to have different number of lines per
     *                           password length
     * @return true if the operation succeeded, false otherwise.
     */
    public String generate(int[] colorPerPassLength, int[] sizePerPassLength) {
        return this.generate(1, colorPerPassLength, sizePerPassLength);
    }

    /**
     * Generates a rainbow table using the parameters of this instance,
     * starting with a password length of 1.
     * 
     * Puts it in a folder with a signature of:
     * "<hashMethode name> _ <starting size of passwords> _ <numbers of diff sizes
     * of passwords> _ <a random UUID>"
     * In this folder will be a bunch of .rbt files with this signature:
     * "<hashMethod name> _ <length of passwords in this file> _ <colors> _ <nb of
     * lines>";
     * 
     * @param gen                the generation method
     * @param startLength        the starting length of passwords
     * @param colorPerPassLength list of colors to have different number of color
     *                           per password length
     * @param sizePerPassLength  list of sizes to have different number of lines per
     *                           password length
     * @return true if the operation succeeded, false otherwise.
     */
    public String generate(int colors, int[] sizePerPassLength) {
        return this.generate(1, colors, sizePerPassLength);
    }

    private void setReductions(int nbReduc){
        int offset = -1;
        this.reductionMethod = new ArrayList<>();

        // Creates the list of reductions.
        for (int i = 0; i <= nbIteration; i++) {
            this.reductionMethod.add(new SeededReduction(offset++, this.wordGenerator));
        }
    }
}
