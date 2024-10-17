package rainbow_tables.utils.reduction;

/**
 * Basic reduction function.
 * Truncates the hash to its last n characters (n being the size of the original hashed password)
 */
public class BasicReduction extends AReduction{
    
    /**
     * Boolean indicating if we start from the start (true) or the end (false) of the string
     */
    Boolean inverted;

    /**
     * Constructor of the basic reduction
     * @param inverted wether the reducing is inverted or not
     */
    public BasicReduction(boolean inverted){
        this.inverted=inverted;
    }

    @Override
    public String reduce(String s, int n) {
        String reducedS= this.matchSize(s, n);
        if(!this.inverted){ // take the n last characters
            return reducedS.substring(reducedS.length()-n);
        }
        else{ //take the n first characters
            return reducedS.substring(0, n);
        }
            
    }
    
}
