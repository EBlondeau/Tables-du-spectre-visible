package exp_scripts;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import rainbow_tables.attack.RainbowTableListCracker;
import rainbow_tables.generation.RainbowTableGenerator;
import rainbow_tables.utils.RainbowTableList;
import rainbow_tables.utils.hashfuncs.IHashFunction;
import rainbow_tables.utils.wordgen.IWordGenerator;
import rainbow_tables.utils.wordgen.MotGenerator;

/** */
public class Exp{
    
    private final static String expPath="../experimentations/data/";
    private final double percentageToTest = 0.001;
    private final int constantPassSample=100000;
    private final int constantTableSize=10000;
    private final int constantPassSize=4;
    private final int constantNbColors=3;
    

    private ArrayList<int[]> passSizeSample = new ArrayList<>();
    private ArrayList<int[]> nbColorSample = new ArrayList<>();
    private ArrayList<int[]> tableSizeSample = new ArrayList<>();
    private IHashFunction hashFuncToTest;
    private IWordGenerator wordGenToTest;
    private final int percentagedPassSample;
    private String testSubPath;

    public Exp(ArrayList<int[]> passSizeSample, ArrayList<int[]> nbColorSample, ArrayList<int[]> tableSizeSample,
            IHashFunction hashFuncToTest, String testSubPath, IWordGenerator wordGenToTest) {
        this.passSizeSample = passSizeSample;
        this.nbColorSample = nbColorSample;
        this.tableSizeSample = tableSizeSample;
        this.hashFuncToTest = hashFuncToTest;
        this.testSubPath = testSubPath;
        this.wordGenToTest= wordGenToTest;

        //Create the experimentation folder
        try {
            Path testFolder= Paths.get(Exp.expPath+this.testSubPath);
            
            if (Files.createDirectories(testFolder) != null) {
                System.out.println("Folder Created: " + this.testSubPath);
            } else {
                System.out.println("Folder already exists.");
            }            
        } catch (Exception e) {
            System.err.println("Une erreur est survenue lors de la création du dossier d'exp");
            e.printStackTrace();
        }
        this.percentagedPassSample=(int)(Math.pow(this.wordGenToTest.getAlphabet().length, constantPassSize) * 0.3);
    }

    /**
     * Tests on password size.
     * @param colorSizeVariation variations on the nb of colors
     * @param tableSizeVariation variation on the size of the table
     * @return true if tests went smoothly, false otherwise.
     */
    public boolean startPassSizeTests(int[] colorSizeVariation, int[] tableSizeVariation) {
        // RainbowTableGenerator
        RainbowTableGenerator generator = new RainbowTableGenerator(hashFuncToTest, this.testSubPath, this.wordGenToTest);
        String testDataName;

        testDataName="S_C_T_" + constantPassSample + "_" + this.wordGenToTest.getClass().getSimpleName();
        FileWriter sct= createTestCsv(testDataName);
        //Variation on colorSizes, test on a fixed number of password to have meaningful data.
        for (int colorSizeVariations : colorSizeVariation) {
            for (int[] passSizeSamples : this.passSizeSample) {
                TestRTable.launchTest(generator, 
                        passSizeSamples[0], // Parameter to test
                        colorSizeVariations, // Variating Parameter
                        constantTableSize, // Constant parameter
                        (int)(Math.pow(this.wordGenToTest.getAlphabet().length, passSizeSamples[0]) * percentageToTest),
                        testDataName,
                        sct,
                        this.testSubPath, this.wordGenToTest, this.hashFuncToTest);
            }
        }
        try {
            sct.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        testDataName="S_T_C_" + constantPassSample + "_" + this.wordGenToTest.getClass().getSimpleName();
        FileWriter stc= createTestCsv(testDataName);
        // Variation on tableSizes, test on a fixed number of password to have meaningful data.
        for (int tableSizeVariations : tableSizeVariation) {
            for (int[] passSizeSamples : this.passSizeSample) {
                TestRTable.launchTest(generator, 
                        passSizeSamples[0], // Parameter to test
                        constantNbColors, // Constant parameter 
                        tableSizeVariations, // Variating Parameter
                        (int)(Math.pow(this.wordGenToTest.getAlphabet().length, passSizeSamples[0]) * percentageToTest),
                        testDataName,
                        stc,
                        this.testSubPath, this.wordGenToTest, this.hashFuncToTest);
            }
        }
        try {
            stc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Tests on password sizes, but multithreaded. Vroom.
     * The ExecutorService runs both variation before shutdown,
     * thus removing a sync barrier.
     * @param colorSizeVariation variation on nb of colors
     * @param tableSizeVariation variation on size of table
     * @return true if everything went smoothly, false otherwise
     */
    public boolean startThreadedPassSizeTests(int[] colorSizeVariation, int[] tableSizeVariation) {
        
        // Create executor service with a fixed number of threads, corresponding to the nb of cpu cores of the machine
        ExecutorService executorService= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        
        // Create and initialize the CSV file used to log results
        String firstTestDataName="S_C_T_" + constantPassSample + "_" + this.wordGenToTest.getClass().getSimpleName();
        FileWriter sct= createTestCsv(firstTestDataName);

        //Variation on colorSizes, test on a fixed number of password to have meaningful data.
        for (int colorSizeVariations : colorSizeVariation) {
            for (int[] passSizeSamples : this.passSizeSample) {
                // Create a rTable generator for the thread we're going to create, NEEDED.
                RainbowTableGenerator generator = new RainbowTableGenerator(hashFuncToTest, this.testSubPath, this.wordGenToTest);
                // Create and execute the new thread using executorService
                executorService.execute(new ExpThread("" + colorSizeVariations + passSizeSamples, 
                    generator, 
                    passSizeSamples[0], // Tested parameter
                    colorSizeVariations, // Variating parameter 
                    constantTableSize,  // Constant parameter
                    (int)(Math.pow(this.wordGenToTest.getAlphabet().length, passSizeSamples[0]) * percentageToTest), 
                    firstTestDataName, 
                    sct, 
                    this.testSubPath, this.wordGenToTest, this.hashFuncToTest)); 
            }
        }

        // Create and initialize the CSV file used to log results
        String secondTestDataName="S_T_C_" + constantPassSample + "_" + this.wordGenToTest.getClass().getSimpleName();
        FileWriter stc= createTestCsv(secondTestDataName);

        // Variation on tableSize, test on a fixed number of password to have meaningful results
        for (int tableSizeVariations : tableSizeVariation) {
            for (int[] passSizeSamples : this.passSizeSample) {
                // Create a rTable generator for the thread we're going to create, NEEDED.
                RainbowTableGenerator generator = new RainbowTableGenerator(hashFuncToTest, this.testSubPath, this.wordGenToTest);
                // Create and execute the new thread using executorService
                executorService.execute(new ExpThread("" + tableSizeVariations + passSizeSamples, 
                    generator, 
                    passSizeSamples[0], // Tested parameter
                    constantNbColors, // Constant parameter 
                    tableSizeVariations,  // Variating parameter
                    (int)(Math.pow(this.wordGenToTest.getAlphabet().length, passSizeSamples[0]) * percentageToTest), 
                    secondTestDataName, 
                    stc, 
                    this.testSubPath, this.wordGenToTest, this.hashFuncToTest)); 
            }
        }

        //Await for tasks to be done
        try {
            //shutdown the executorService
            executorService.shutdown();
            //Make sure that everything is done before continuing
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        //Close the file writers
        try {
            sct.close();
            stc.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    /**
     * Tests on colorSize
     * @param passSizeVariation variation on password size
     * @param tableSizeVariation variation on table size
     * @return true if everything went smoothly, false otherwise
     */
    public boolean startColorSizeTests(int[] passSizeVariation, int[] tableSizeVariation) {

        RainbowTableGenerator generator = new RainbowTableGenerator(hashFuncToTest, this.testSubPath, this.wordGenToTest);

        String testDataName;

        testDataName="C_S_T_" + constantPassSample + "_" + this.wordGenToTest.getClass().getSimpleName();
        FileWriter cst= createTestCsv(testDataName);
        // Variation on password size, test on a fixed number of password to have
        // meaningful data
        for (int passSizeVariations : passSizeVariation) {
            for (int[] colorSamples : this.nbColorSample) {
                TestRTable.launchTest(generator, 
                        passSizeVariations, // Variating parameter
                        colorSamples[0], // Parameter to test
                        constantTableSize, // Constant parameter
                        constantPassSample,
                        testDataName,
                        cst,
                        this.testSubPath, this.wordGenToTest, this.hashFuncToTest);
            }
        }

        try {
            cst.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        testDataName="C_T_S_" + percentagedPassSample + "_" + this.wordGenToTest.getClass().getSimpleName();
        FileWriter cts= createTestCsv(testDataName);
        // Variation on table size, test on a percentage of password of length 3
        for (int tableSizeVariations : tableSizeVariation) {
            for (int[] colorSamples : this.nbColorSample) {
                TestRTable.launchTest(generator,
                        constantPassSize, // Constant parameter
                        colorSamples[0], // Parameter to test
                        tableSizeVariations, // Variating parameter
                        percentagedPassSample,
                        testDataName,
                        cts,
                        this.testSubPath, this.wordGenToTest, this.hashFuncToTest);
            }
        }

        try {
            cts.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Tests on nb of colors, but multithreaded. Vroom.
     * The ExecutorService runs both variation before shutdown,
     * thus removing a sync barrier.
     * @param passSizeVariation variation on passwordSize
     * @param tableSizeVariation variation on size of table
     * @return true if everything went smoothly, false otherwise
     */
    public boolean startThreadedColorTests(int[] passSizeVariation, int[] tableSizeVariation){
        // Create executor service with a fixed number of threads, corresponding to the nb of cpu cores of the machine
        ExecutorService executorService= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        
        // Create and initialize the CSV file used to log results
        String firstTestDataName="C_S_T_" + constantPassSample + "_" + this.wordGenToTest.getClass().getSimpleName();
        FileWriter cst= createTestCsv(firstTestDataName);

        //Variation on colorSizes, test on a fixed number of password to have meaningful data.
        for (int passSizeVariations : passSizeVariation) {
            for (int[] nbColorSamples : this.nbColorSample) {
                // Create a rTable generator for the thread we're going to create, NEEDED.
                RainbowTableGenerator generator = new RainbowTableGenerator(hashFuncToTest, this.testSubPath, this.wordGenToTest);
                // Create and execute the new thread using executorService
                executorService.execute(new ExpThread("" + passSizeVariations + nbColorSamples, 
                    generator, 
                    passSizeVariations, // Variating parameter
                    nbColorSamples[0], // Tested parameter 
                    constantTableSize,  // Constant parameter
                    (int)(Math.pow(this.wordGenToTest.getAlphabet().length, passSizeVariations) * percentageToTest), 
                    firstTestDataName, 
                    cst, 
                    this.testSubPath, this.wordGenToTest, this.hashFuncToTest)); 
            }
        }

        // Create and initialize the CSV file used to log results
        String secondTestDataName="C_T_S_" + percentagedPassSample + "_" + this.wordGenToTest.getClass().getSimpleName();
        FileWriter cts= createTestCsv(secondTestDataName);

        // Variation on tableSize, test on a percentaged number of password to have meaningful results
        for (int tableSizeVariations : tableSizeVariation) {
            for (int[] nbColorSamples : this.nbColorSample) {
                // Create a rTable generator for the thread we're going to create, NEEDED.
                RainbowTableGenerator generator = new RainbowTableGenerator(hashFuncToTest, this.testSubPath, this.wordGenToTest);
                // Create and execute the new thread using executorService
                executorService.execute(new ExpThread("" + tableSizeVariations + nbColorSamples, 
                    generator, 
                    constantPassSize, // Constant parameter
                    nbColorSamples[0], // Tested parameter 
                    tableSizeVariations,  // Variating parameter
                    percentagedPassSample, 
                    secondTestDataName, 
                    cts, 
                    this.testSubPath, this.wordGenToTest, this.hashFuncToTest)); 
            }
        }

        //Await for tasks to be done
        try {
            //shutdown the executorService
            executorService.shutdown();
            //Make sure that everything is done before continuing
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        //Close the file writers
        try {
            cst.close();
            cts.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Tests on tableSize
     * @param passSizeVariation variation on password size
     * @param colorSizeVariation variation on color size
     * @return true if everything went smoothly, false otherwise
     */
    public boolean startTableSizeTests(int[] passSizeVariation, int[] colorSizeVariation){

        RainbowTableGenerator generator = new RainbowTableGenerator(hashFuncToTest, this.testSubPath, this.wordGenToTest);
        String testDataName;

        testDataName="T_S_C_" + constantPassSample + "_" + this.wordGenToTest.getClass().getSimpleName();
        FileWriter tsc= createTestCsv(testDataName);
        //Variation on password size, test on a fixed number of password to have meaningful data
        for (int passSizeVariations : passSizeVariation) {
            for (int[] tableSizeSamples : this.tableSizeSample) {
                TestRTable.launchTest(generator,
                        passSizeVariations, // Variating parameter
                        constantNbColors, 
                        tableSizeSamples[0], // Parameter to test
                        constantPassSample,
                        testDataName,
                        tsc,
                        this.testSubPath, this.wordGenToTest, this.hashFuncToTest);
            }
        }

        testDataName="T_C_S_" + percentagedPassSample + "_" + this.wordGenToTest.getClass().getSimpleName();
        FileWriter tcs= createTestCsv(testDataName);
        //Variation on colorSizes
        for (int colorSizeVariations : colorSizeVariation) {
            for (int[] tableSizeSamples : this.tableSizeSample) {
                TestRTable.launchTest(generator,
                        constantPassSize, 
                        colorSizeVariations, // Variating parameter
                        tableSizeSamples[0], // Parameter to test
                        percentagedPassSample,
                        testDataName,
                        tcs,
                        this.testSubPath, this.wordGenToTest, this.hashFuncToTest);
            }
        }

        try {
            tcs.close();
            tsc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * Tests on table sizes, but multithreaded. Vroom.
     * The ExecutorService runs both variation before shutdown,
     * thus removing a sync barrier.
     * @param passSizeVariation variation on passwordSize
     * @param colorSizeVariation variation on nb of colors
     * @return true if everything went smoothly, false otherwise
     */
    public boolean startThreadedTableSizeTests(int[] passSizeVariation, int[] colorSizeVariation){
        // Create executor service with a fixed number of threads, corresponding to the nb of cpu cores of the machine
        ExecutorService executorService= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        
        // Create and initialize the CSV file used to log results
        String firstTestDataName="T_S_C_" + constantPassSample + "_" + this.wordGenToTest.getClass().getSimpleName();
        FileWriter tsc= createTestCsv(firstTestDataName);

        //Variation on colorSizes, test on a fixed number of password to have meaningful data.
        for (int passSizeVariations : passSizeVariation) {
            for (int[] tableSizeSamples : this.tableSizeSample) {
                // Create a rTable generator for the thread we're going to create, NEEDED.
                RainbowTableGenerator generator = new RainbowTableGenerator(hashFuncToTest, this.testSubPath, this.wordGenToTest);
                // Create and execute the new thread using executorService
                executorService.execute(new ExpThread("" + passSizeVariations + tableSizeSamples, 
                    generator, 
                    passSizeVariations, // Variating parameter
                    constantNbColors, // Constant parameter 
                    tableSizeSamples[0],  // Tested parameter
                    (int)(Math.pow(this.wordGenToTest.getAlphabet().length, passSizeVariations) * percentageToTest), 
                    firstTestDataName, 
                    tsc, 
                    this.testSubPath, this.wordGenToTest, this.hashFuncToTest)); 
            }
        }

        // Create and initialize the CSV file used to log results
        String secondTestDataName="T_C_S_" + percentagedPassSample + "_" + this.wordGenToTest.getClass().getSimpleName();
        FileWriter tcs= createTestCsv(secondTestDataName);

        // Variation on tableSize, test on a percentaged number of password to have meaningful results
        for (int colorSizeVariations : colorSizeVariation) {
            for (int[] tableSizeSamples : this.tableSizeSample) {
                // Create a rTable generator for the thread we're going to create, NEEDED.
                RainbowTableGenerator generator = new RainbowTableGenerator(hashFuncToTest, this.testSubPath, this.wordGenToTest);
                // Create and execute the new thread using executorService
                executorService.execute(new ExpThread("" + colorSizeVariations + tableSizeSamples, 
                    generator, 
                    constantPassSize, // Constant parameter
                    colorSizeVariations, // Variating parameter 
                    tableSizeSamples[0],  // Tested parameter
                    percentagedPassSample, 
                    secondTestDataName, 
                    tcs, 
                    this.testSubPath, this.wordGenToTest, this.hashFuncToTest)); 
            }
        }

        //Await for tasks to be done
        try {
            //shutdown the executorService
            executorService.shutdown();
            //Make sure that everything is done before continuing
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        //Close the file writers
        try {
            tsc.close();
            tcs.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    /**
     * Function creating a csv file for test results and initializing it
     * with column names.
     * Also return a file writer for this csv file.
     * The column names are as followed:
     * <Tested parameter>, <Variating parameter>, <Constant parameter>, pwdCracked
     * @param testDataName the name of the csv file
     * @return a file writer for this csv file
     */
    private FileWriter createTestCsv(String testDataName){
        try {
            File testDataFile= new File(Exp.expPath+this.testSubPath+testDataName+".csv");
            if(testDataFile.createNewFile()){
                System.out.println("Fichier exp créé : " + testDataName);
            }
            else{
                System.err.println("Le fichier existe déjà");
                return null;
            }

            FileWriter fw= new FileWriter(testDataFile);
            // Take the first character since they contain the T, S and C
            String top= testDataName.substring(0, 6);
            top= top.replace('_', ',');
            // Add a column for the number of cracked password.
            top+="pwdCracked";
            top+=",sample\n";
            fw.write(top);
            return fw;
        } catch (Exception e) {
            System.err.println("Une erreur est survenue lors de la création des fichier d'exp");
            e.printStackTrace();
            return null;
        }
    }


}
