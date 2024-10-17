package rainbow_tables.demos;

import java.util.Scanner;

import rainbow_tables.utils.hashfuncs.IHashFunction;
import rainbow_tables.utils.hashfuncs.MD5;
import rainbow_tables.utils.reduction.AReduction;
import rainbow_tables.utils.reduction.SeededReduction;
import rainbow_tables.utils.wordgen.MotGenerator;

public class DemoReductionHash {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        System.out.print("Quoi reduire? : ");
        String toReduce=  scanner.nextLine();
        System.out.print("Combien de fois? :");
        String i= scanner.nextLine();
        int k=Integer.parseInt(i);

        System.out.println();

        AReduction reduction= new SeededReduction(0, new MotGenerator());
        IHashFunction hashFunc= new MD5();

        String current=toReduce;
        int size= toReduce.length();

        for(int j=1; j<=k; j++){
            current=hashFunc.hash(current);
            System.out.print(current + " -R-> ");
            current=reduction.reduce(current, size);
            System.out.print(current);
            if(j<k){
                System.out.println(" -H-> ");
            }   
        }

        scanner.close();
    }
}
