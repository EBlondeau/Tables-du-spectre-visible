package rainbow_tables.demos;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

import rainbow_tables.utils.hashfuncs.IHashFunction;
import rainbow_tables.utils.hashfuncs.MD5;
import rainbow_tables.utils.reduction.AReduction;
import rainbow_tables.utils.reduction.SeededReduction;
import rainbow_tables.utils.wordgen.MotGenerator;

public class DemoMultipleReduction {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        System.out.print("Quoi reduire? : ");
        String toReduce=  scanner.nextLine();
        System.out.print("Combien de fois? :");
        String i= scanner.nextLine();
        int k=Integer.parseInt(i);

        System.out.println();

        ArrayList<AReduction> rList = new ArrayList<>();
        for(int j=1; j<=k; j++){
            rList.add(new SeededReduction(j-1, new MotGenerator()));
        }

        AReduction reduction= new SeededReduction(0, new MotGenerator());
        IHashFunction hashFunc= new MD5();

        String current1=toReduce;
        String current2= toReduce;
        int size= toReduce.length();

        for(int j=1; j<=k; j++){
            current1=hashFunc.hash(current1);
            current2=hashFunc.hash(current2);
            System.out.print(" -R-> ");
            current1=reduction.reduce(current1, size);
            System.out.print(current1);
            System.out.print(" -R"+(j-1)+"-> ");
            current2=rList.get(j-1).reduce(current2, size);
            System.out.print(current2);
            System.out.println();
        }

        scanner.close();
     }
}
    

