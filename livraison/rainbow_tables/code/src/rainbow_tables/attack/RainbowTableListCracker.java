package rainbow_tables.attack;

import java.util.ArrayList;

import rainbow_tables.utils.RainbowTable;
import rainbow_tables.utils.RainbowTableList;

/**
 * Cracker for a RainbowTableList.
 */
public class RainbowTableListCracker{

    private RainbowTableList rTableList;
    /**
     * Constructor for the cracker
     * @param tableList the RainbowTableList to operate the crack on
     */
    public RainbowTableListCracker(RainbowTableList tableList) {
        this.rTableList= tableList;
    }

    /**
     * Method to crack a hash
     * @param hash the hash to crack   
     * @return the result of the crack (if it worked, the clear password, if it didnt, says it)
     */
    public String listCracker(String hash){
        ArrayList<RainbowTable> rTables= rTableList.getRainbowTables();
        for(int i=0; i< rTables.size(); i++){
            
            RainbowTableCracker tableCraker = new RainbowTableCracker(rTables.get(i));
            try {
                //System.out.println("Going through passwords of length " + rTables.get(i).getPassLength());
                String bool = tableCraker.crackHash(rTables.get(i).getPassLength(), hash);
                if(bool!=null){
                    return bool;
                }
            } catch (Exception e) {
                System.err.println("Table doesnt exist");
                e.printStackTrace();
            }
            
        }
        return null;
    }

    public ArrayList<String> listCrackerBulk(ArrayList<String> hashes){
        ArrayList<RainbowTable> rTables= rTableList.getRainbowTables();
        for(int i=0; i< rTables.size(); i++){
            RainbowTableCracker tableCraker = new RainbowTableCracker(rTables.get(i));
            try {
                //System.out.println("Going through passwords of length " + rTables.get(i).getPassLength());
                ArrayList<String> bool = tableCraker.crackHashBulk(rTables.get(i).getPassLength(), hashes);
                return bool;
                
            } catch (Exception e) {
                System.err.println("Table doesnt!!!! exist");
                e.printStackTrace();
            }
            
        }
        return null;

    }

    
    
}
