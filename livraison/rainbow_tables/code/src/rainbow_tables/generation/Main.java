package rainbow_tables.generation;

import java.io.File;
import java.util.ArrayList;

import rainbow_tables.utils.hashfuncs.IHashFunction;
import rainbow_tables.utils.hashfuncs.MD5;
import rainbow_tables.utils.hashfuncs.PearsonHashing;
import rainbow_tables.utils.hashfuncs.SHA1;
import rainbow_tables.utils.hashfuncs.SHA256;
import rainbow_tables.utils.reduction.AReduction;
import rainbow_tables.utils.reduction.BasicReduction;
import rainbow_tables.utils.reduction.SeededReduction;
import rainbow_tables.utils.reduction.StepReduction;

public class Main {
    public static void main(String[] args) {
        ArrayList<AReduction> ar = new ArrayList<>();
        ar.add(new BasicReduction(false));
        IHashFunction hashFunc = new PearsonHashing();
        RainbowTableGenerator rg = new RainbowTableGenerator(hashFunc);
        rg.generate(3, new int[] {5,10,10,20,20}, new int[] {1000,1000,10000,10000,10000});
        //rg.generate(new File("../data/clear_Passwordlist/Length5.txt"));
        // String aaron = "Aaron";
        // String hashed = hashFunc.hash(aaron);
        // String reduced = ar.get(0).reduce(hashed, aaron.length());
        // hashed = hashFunc.hash(reduced);
        // reduced = ar.get(0).reduce(hashed, aaron.length());
        // hashed = hashFunc.hash(reduced);
        // reduced = ar.get(0).reduce(hashed, aaron.length());

        // hashed = hashFunc.hash(reduced);

        // System.out.println(hashed);

    }
}
