package rainbow_tables.utils.reduction;

/**
 * Reduction by a chosen step. 
 * Reduces a String to steps.
 */
public class StepReduction extends AReduction{

    /**
     * The number of steps
     */
    private int steps;
    /**
     * Boolean indicating if we start from the start (true) or the end (false) of the string
     */
    private boolean inverted;

    /**
     * Constructor for the step reduction
     * @param steps the number of steps used for the String reduction
     * @param inverted wether the reduction is inverted or not
     */
    public StepReduction(int steps, boolean inverted){
        this.steps=steps;
        this.inverted=inverted;
    }

    /**
     * Basic constructor for step reduction
     * Takes a step of 2 and isnt inverted
     */
    public StepReduction(){
        this(2, false);
    }

    @Override
    public String reduce(String s, int n){
        String matchS=this.matchSize(s, n);
        String reducedS="";

        if(!this.inverted){
            for(int i = 0; i < n*steps ; i+=this.steps){
                reducedS+=matchS.charAt(i);
            }
        }

        else{
            for(int i = matchS.length()-1; i > matchS.length()-1-(n*steps) ; i-=this.steps){
                reducedS+=matchS.charAt(i);
                System.out.println("test");
            }
        }
        
        return reducedS;
    }
    
}
