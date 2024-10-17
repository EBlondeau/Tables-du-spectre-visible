package exp_scripts;

import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.ArrayList;

import rainbow_tables.attack.RainbowTableListCracker;
import rainbow_tables.generation.RainbowTableGenerator;
import rainbow_tables.utils.RainbowTableList;
import rainbow_tables.utils.hashfuncs.IHashFunction;
import rainbow_tables.utils.wordgen.IWordGenerator;

public class TestRTable {
    
    /**
     * Function testing a rainbow table using the generator and parameters passed in argument
     * @param rGenerator the rTable generator
     * @param passSize the length of passwords
     * @param nbColors the nb of colors
     * @param tableSize the size of the table
     * @param nbOfPassToTest the nb of passwords to test
     * @param testDataName the name of the test result file
     * @param resultWriter the writer for the test result file
     * @param testSubPath the sub path of the test
     * @param wordGenToTest the word generator to test
     * @param hashFuncToTest the hashfunction to test
     * @return true if everything goes smoothly, false otherwise
     */

    public static boolean launchTest(RainbowTableGenerator rGenerator, int passSize, int nbColors, int tableSize, double nbOfPassToTest, String testDataName, FileWriter resultWriter, String testSubPath, IWordGenerator wordGenToTest, IHashFunction hashFuncToTest){
         // File has been created, we continue

        // Initialize a counter for the number of password cracked.
        int counter = 0;
        String rtName = rGenerator.generate(passSize, nbColors, tableSize);
        RainbowTableList tList = new RainbowTableList(
                Paths.get(RainbowTableGenerator.rTablesPath + testSubPath + rtName + "/"));
        RainbowTableListCracker tListCracker = new RainbowTableListCracker(tList);

        ArrayList<String> pwdsToTest= new ArrayList<>();
        ArrayList<String> found= new ArrayList<>();

        // Test <percentageToTest> % of passwords
        int status = 0;
        for (int i = 0; i < nbOfPassToTest; i++) {
            String wordToTest = wordGenToTest.motAleatoire(passSize);
            pwdsToTest.add(hashFuncToTest.hash(wordToTest));
        }

        ArrayList<String> res= tListCracker.listCrackerBulk(pwdsToTest);
        counter=res.size();
        System.out.println();
        System.out.println("Params: " +
                "C(" + nbColors + ")" +
                "T(" + tableSize + ")" +
                "L(" + passSize + ")" +
                "Sample(" + nbOfPassToTest + ")" +
                "WordGen(" + wordGenToTest.getClass().getSimpleName() + ")" +
                ", " + counter + " password cracked");
        
        String result = prepareResults(testDataName, nbColors, tableSize, passSize, counter, nbOfPassToTest);
        try {
            resultWriter.write(result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Prepare the results to fit to the testDataName.
     * @param testDataName the name of the test
     * @param nbColors the number of colors to 
     * @param tableSize the size of the table
     * @param passSize the password size
     * @param pwdCracked the counter of password cracked for this tests
     * @return the prepared results in the shape of: <Tested parameter>, <Variating parameter>, <Constant parameter>, pwdCracked
     */
    private static String prepareResults(String testDataName, int nbColors, int tableSize, int passSize, int pwdCracked, double nbOfPassToTest){
        String res="";  
        res=testDataName.substring(0, 5);
        res=res.replace("C", Integer.toString(nbColors));
        res=res.replace("T", Integer.toString(tableSize));
        res=res.replace("S", Integer.toString(passSize));
        res+="_"+Integer.toString(pwdCracked);
        res+="_"+Integer.toString((int)nbOfPassToTest) + "\n";
        res=res.replace('_', ',');
        return res;
    }
}
