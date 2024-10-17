package rainbow_tables.utils;

import java.io.File;

import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Object representing a list of rainbow table contained in a folder.
 * Consists in a list of RainbowTable objects.
 */
public class RainbowTableList {

    private ArrayList<RainbowTable> rTables;

    /**
     * Constructor for the rainbow table list.
     * Creates a list of RainbowTable object by taking all the files in the folder in arguments.
     * @param rbtFolder the path to the folder of the rainbow table list.
     */
    public RainbowTableList(Path rbtFolder){
        try {
            File listOfSubTableFiles= rbtFolder.toFile();
        
            this.rTables=new ArrayList<>();

            for(String subTableName : listOfSubTableFiles.list()){
                this.rTables.add(new RainbowTable(rbtFolder.toString()+"/"+subTableName));
            }
        } catch (Exception e) {
            System.err.println("Folder doesnt exist");
            e.printStackTrace();
        }
    }

    public ArrayList<RainbowTable> getRainbowTables(){
        return this.rTables;
    }
       
}

