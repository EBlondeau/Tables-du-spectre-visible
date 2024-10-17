package rainbow_tables.utils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

import rainbow_tables.utils.hashfuncs.IHashFunction;
import rainbow_tables.utils.reduction.AReduction;
import rainbow_tables.utils.reduction.SeededReduction;
import rainbow_tables.utils.wordgen.IWordGenerator;

/**
 * Rainbow table object structure.
 * A rainbow table consists of a file, and parameters that come from this file name.
 * 
 */
public class RainbowTable {

    private File rbtfile;
    private IHashFunction hashFunction;
    private IWordGenerator wordGenerator;
    private int colors;
    private ArrayList<AReduction> reductionMethod;
    private int passLength;

    public RainbowTable(String tablePath) throws Exception {

        // Parse the name of the file and apply checks on it (like the extension)
        // and give values to the attributs of this class relative to the file name
        try {
            this.rbtfile = new File(tablePath);

            String fileName = rbtfile.getName();
            String fileExtension = fileName.substring(fileName.length() - 4);

            // Check file extension
            if (!fileExtension.equalsIgnoreCase(".rbt")) {
                throw new Exception("invalid file extension");
            }

            // Create a table of parameters
            String[] tableParameters = fileName.substring(0, fileName.length() - 4).split("_");

            // Generate the hashFunction instance dynamically
            Class<?> c1 = Class.forName("rainbow_tables.utils.hashfuncs." + tableParameters[0]);
            Constructor<?> construct1 = c1.getConstructors()[0];
            Object obj1 = construct1.newInstance();
            this.hashFunction = (IHashFunction) obj1;

            // Generate the wordGenerator instance dynamically
            Class<?> c2 = Class.forName("rainbow_tables.utils.wordgen." + tableParameters[4]);
            Constructor<?> construct2 = c2.getConstructors()[0];
            Object obj2 = construct2.newInstance();
            this.wordGenerator = (IWordGenerator) obj2;

            this.colors = Integer.parseInt(tableParameters[2]);

            this.passLength = Integer.parseInt(tableParameters[1]);

            int offset=-1;
            this.reductionMethod = new ArrayList<>();

            // Creates the list of reductions.
            for(int i=0; i<=colors; i++){
                this.reductionMethod.add(new SeededReduction(offset++ , this.wordGenerator));
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        
        /**
          System.out.println(this.colors);
          System.out.println(this.rows);
          System.out.println(this.hashFunction.getClass().getSimpleName());
          System.out.println(this.hashFunction.getClass().getName());
        **/

    }

    public IHashFunction getHashFunction(){
        return this.hashFunction;
    }

    public File getTableFile(){
        return this.rbtfile;
    }
    
    public int getColors() {
        return this.colors;
    }

    public ArrayList<AReduction> getReductionMethod() {
        return this.reductionMethod;
    }

    public int getPassLength(){
        return this.passLength;
    }
}
