package exp_scripts;

import java.io.FileWriter;
import java.lang.reflect.Array;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.ArrayList;

import rainbow_tables.attack.RainbowTableListCracker;
import rainbow_tables.generation.RainbowTableGenerator;
import rainbow_tables.utils.RainbowTableList;
import rainbow_tables.utils.hashfuncs.IHashFunction;
import rainbow_tables.utils.wordgen.IWordGenerator;

/**
 * Runnable object for experimentation.
 * It's just an adaptation of Exp.testRTable, to make it runnable 
 * and allow us to execute it on many threads.
 */
public class ExpThread implements Runnable{

    private Thread t;
    private  String threadName;
    private RainbowTableGenerator rGenerator;
    private int passSize;
    private int nbColors;
    private int tableSize;
    private double nbOfPassToTest;
    private String testDataName;
    private FileWriter resultWriter;
    private String testSubPath;
    private IWordGenerator wordGenToTest;
    private IHashFunction hashFuncToTest;

    public ExpThread(String threadName, RainbowTableGenerator rGenerator, int passSize, int nbColors, int tableSize, double nbOfPassToTest, String testDataName, FileWriter resultWriter, String testSubPath, IWordGenerator wordGenToTest, IHashFunction hashFuncToTest) {
        this.threadName = threadName;
        this.rGenerator = rGenerator;
        this.passSize = passSize;
        this.nbColors = nbColors;
        this.tableSize = tableSize;
        this.nbOfPassToTest = nbOfPassToTest;
        this.testDataName = testDataName;
        this.resultWriter = resultWriter;
        this.testSubPath = testSubPath;
        this.wordGenToTest = wordGenToTest;
        this.hashFuncToTest = hashFuncToTest;
    }
   
    

    @Override
    public void run() {
        TestRTable.launchTest(rGenerator, passSize, nbColors, tableSize, nbOfPassToTest, testDataName, resultWriter, testSubPath, wordGenToTest, hashFuncToTest);
    }

    public void start(){
        System.out.println("Starting " +  threadName );
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }

    public boolean isAlive(){
        return this.t.isAlive();
    }
    
}
