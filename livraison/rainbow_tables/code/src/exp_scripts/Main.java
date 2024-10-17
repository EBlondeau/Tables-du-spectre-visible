package exp_scripts;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import rainbow_tables.utils.hashfuncs.IHashFunction;
import rainbow_tables.utils.hashfuncs.MD5;
import rainbow_tables.utils.hashfuncs.PearsonHashing;
import rainbow_tables.utils.hashfuncs.SHA3;
import rainbow_tables.utils.wordgen.MotGenerator;

public class Main {
    public static void main(String[] args) {
        ArrayList<int[]> passSizeSample= new ArrayList<>();
        passSizeSample.add(new int[]{3});
        passSizeSample.add(new int[]{4});
        passSizeSample.add(new int[]{5});
        passSizeSample.add(new int[]{6});
     
        ArrayList<int[]> nbColorSample= new ArrayList<>();
        nbColorSample.add(new int[] {3});
        nbColorSample.add(new int[] {4});
        nbColorSample.add(new int[] {5});
        nbColorSample.add(new int[] {10});
        nbColorSample.add(new int[] {20});
        nbColorSample.add(new int[] {50});
        ArrayList<int[]> tableSizeSample= new ArrayList<>();
        tableSizeSample.add(new int[] {100});
        tableSizeSample.add(new int[] {1000});
        tableSizeSample.add(new int[] {10000});
        tableSizeSample.add(new int[] {50000});
        tableSizeSample.add(new int[] {200000});

        DateTimeFormatter dtf= DateTimeFormatter.ofPattern("MM-dd-HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();

        IHashFunction hashFunc= new PearsonHashing();
        String testName="exp_"+hashFunc.getClass().getSimpleName()+"_"+dtf.format(now);

        Exp exp= new Exp(passSizeSample, nbColorSample, tableSizeSample, hashFunc, testName + "/", new MotGenerator());
    
        //exp.startTableSizeTests(new int[] {3, 4, 5, 6, 7}, new int[] {2, 3, 5, 10, 20});
        //exp.startPassSizeTests(new int[] {2,3}, new int[] {1000});
        //exp.startColorSizeTests(new int[] {3,4,5,6,7}, new int[]{1000, 2000, 5000, 10000});
        
        exp.startThreadedPassSizeTests(new int[] {1,2,4,7,10,20}, new int[] {1000, 2000, 5000, 10000});
        exp.startThreadedColorTests(new int[] {3,4,5,6}, new int[] {1000, 2000, 5000, 10000});
        exp.startThreadedTableSizeTests(new int[] {3,4,5,6}, new int[] {1,2,4,7,10,20});
    }
}
