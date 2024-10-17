package rainbow_tables.attack;

import java.lang.reflect.Array;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import rainbow_tables.utils.RainbowTableList;

public class Main {

    public static void main(String[] args) throws Exception {

        /**
         * Variable pour contrôler le temps
         * Elle s'instencie à 0 et s'affiche en milliseconde
         */ 
        long debut = System.currentTimeMillis();
       
        // La RainbowTable se créée avec le nom de fichier passé en arg du programme
        // On créé le cracker avec la table qu'on utilise et on ajoute l'empreinte hash qu'on veut tester
        //RainbowTableCraker tableCraker = new RainbowTableCraker(table, table.getHashFunction().hash(args[1]));
        RainbowTableList tList= new RainbowTableList(Paths.get("../data/rainbowtables/PearsonHashing_6_1_8c17facf-100b-4810-8e24-b608ef9fadd2/"));
        RainbowTableListCracker tListCracker= new RainbowTableListCracker(tList);
        //  -- TEST --
        ArrayList<String> e = new ArrayList<>();
        System.out.println(tList.getRainbowTables().get(0).getHashFunction().hash(args[0]));
        e.add(tList.getRainbowTables().get(0).getHashFunction().hash(args[0]));
        ArrayList<String> bool = tListCracker.listCrackerBulk(e);
        if(bool != null) {
            System.out.println("---------------------------");
            System.out.println("Le mot de passe correspondant est : "+bool.get(0));
            System.out.println(tList.getRainbowTables().get(0).getHashFunction().hash(bool.get(0)));
            System.out.println("---------------------------");
        }
        else{
            System.out.println("pas trouvé");
        }
            
        // affiche le temps
        SimpleDateFormat sdf = new SimpleDateFormat("'Temps d''execution = 'mm:ss:S");
        System.out.println(sdf.format(System.currentTimeMillis()-debut));

        // Exemple de RainbowTable : "livraison/rainbow_tables/data/rainbowtables/SHA1_10000_2041_-299980994.rbt"
        // Exemple d'empreinte hash : afafb738461b35f1eb36194c21cb463113dfced4
    }
}
