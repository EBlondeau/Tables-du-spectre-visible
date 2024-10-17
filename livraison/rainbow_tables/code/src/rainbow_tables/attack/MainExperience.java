package rainbow_tables.attack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import rainbow_tables.utils.RainbowTableList;

public class MainExperience {

    public static void main(String[] args) throws Exception {

        BufferedReader reader = new BufferedReader(new FileReader(args[0])); 
        String line = null; 

        long debut = System.currentTimeMillis();

        int nbHashFound = 0;
        for(int i=0; i<1000; i++){
            line = reader.readLine();
            System.out.println(i);
            //while ((line = reader.readLine()) != null) { 
                RainbowTableList tList= new RainbowTableList(Paths.get("../data/rainbowtables/MD5_3_8_c40e32e9-a2dc-40ce-b15e-289b8779841d/"));
                RainbowTableListCracker tListCracker = new RainbowTableListCracker(tList);

                String bool = tListCracker.listCracker(tList.getRainbowTables().get(0).getHashFunction().hash(line));
                if(bool != null) {
                    nbHashFound ++;
                    /*System.out.println("---------------------------");
                    System.out.println("Le mot de passe correspondant est : "+bool);
                    System.out.println("---------------------------");*/
                }
            
            //} 
        }
        reader.close();

        System.out.println("Le programme à trouvé "+nbHashFound+" mdp ! Ce qui donne "+nbHashFound*100/10000+"% de réussite !");

        SimpleDateFormat sdf = new SimpleDateFormat("Temps d'execution = mm:ss:S");
        System.out.println(sdf.format(System.currentTimeMillis()-debut));

       }
}
