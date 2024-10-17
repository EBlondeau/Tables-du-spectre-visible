package rainbow_tables.utils.reduction;

/**
 * Interface for reduction functions
 */
public abstract class AReduction {

    /**
     * The reduction function. Used to reduce a String to another string of a chosen size.
     * @param s the String to reduce
     * @param n the size to reduce the String to
     * @return the reduced String
     */
    public abstract String reduce(String s, int n);

    /**
     * Repeats a hash until its size is matching or above the size of the original password
     * Used for hashing methods with a small number of bits, realistically wont be used 
     * @param n the size to match the hash to
     * @return the resized hash
     */
    public String matchSize(String s, int n){
        String matchedS=s;
        while(matchedS.length()-n<=0){
            matchedS=matchedS+s;
        }
        return matchedS;
    }
}
