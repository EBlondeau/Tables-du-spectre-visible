package rainbow_tables.demos;

import java.util.ArrayList;

import rainbow_tables.generation.RainbowTableGenerator;
import rainbow_tables.utils.hashfuncs.IHashFunction;
import rainbow_tables.utils.hashfuncs.*;


public class DemoCreation {
    public static void main(String[] args) {
        IHashFunction hashFunc = new MD5();
        RainbowTableGenerator rg = new RainbowTableGenerator(hashFunc);
        rg.generate(3, new int[] {5,10,10,20,20}, new int[] {1000,1000,10000,10000,10000});
        rg.generate(6, 7, new int[] {1000,1000,10000,10000,10000});
        rg.generate(3, 200, new int[] {20000});
    }
}
