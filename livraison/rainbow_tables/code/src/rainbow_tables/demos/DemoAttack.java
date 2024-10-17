package rainbow_tables.demos;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import rainbow_tables.attack.RainbowTableListCracker;
import rainbow_tables.utils.RainbowTableList;

public class DemoAttack {
    public static void main(String[] args) {
        long debut = System.currentTimeMillis();
       
        // La RainbowTable se créée avec le nom de fichier passé en arg du programme
        // On créé le cracker avec la table qu'on utilise et on ajoute l'empreinte hash qu'on veut tester
        //RainbowTableCraker tableCraker = new RainbowTableCraker(table, table.getHashFunction().hash(args[1]));
        RainbowTableList tList= new RainbowTableList(Paths.get("../data/rainbowtables/"+args[0]+"/"));
        RainbowTableListCracker tListCracker= new RainbowTableListCracker(tList);
        //  -- TEST --

        ArrayList<String> e = new ArrayList<>();
        System.out.println("Hache recherche: " + tList.getRainbowTables().get(0).getHashFunction().hash(args[1]));
        e.add(tList.getRainbowTables().get(0).getHashFunction().hash(args[1]));
        ArrayList<String> bool = tListCracker.listCrackerBulk(e);
        if(!bool.isEmpty()) {
            System.out.println("---------------------------");
            System.out.println("Le mot de passe correspondant est : "+bool.get(0));
            System.out.println(tList.getRainbowTables().get(0).getHashFunction().hash(bool.get(0)));
            System.out.println("---------------------------");
        }
        else{
            System.out.println("Empreinte non trouvée dans cette table");
        }
            
        // affiche le temps
        SimpleDateFormat sdf = new SimpleDateFormat("'Temps d''execution = 'mm:ss:S");
        System.out.println(sdf.format(System.currentTimeMillis()-debut));
    }
}
