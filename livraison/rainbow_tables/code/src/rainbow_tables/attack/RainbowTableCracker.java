package rainbow_tables.attack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import rainbow_tables.utils.RainbowTable;


/**
 * Object representing a rainbow table cracker
 * Consists of methods used in cracking of a hash
 */
public class RainbowTableCracker {
    
    private RainbowTable table;
    
    public RainbowTableCracker(RainbowTable table) {
        this.table = table;
    }

    /**
     * Regarde si l'empreinte est dans la table à la dernière ligne
     */
    public String isInTable(String current, int passLength) throws IOException {
        // Lance un lecteur sur un ficher .rbt
        BufferedReader reader = new BufferedReader(new FileReader(this.table.getTableFile())); 
        String line = null; 

        while ((line = reader.readLine()) != null) { 
            // On récupère la deuxième colonne de la ligne
            String clair = line.substring(passLength+1, (passLength*2)+1);
            //System.out.println(line);
            //On check l'égalité, si c'est bon on return le clair

            if (clair.equals(current)) { 
                reader.close();
                return line.substring(0, passLength);
            } 
            //Sinon on continue pour toutes les lignes
        } 
        reader.close();
        return null;
    }

    /**
     * Check if a list of reduced hash is in a table.
     * Avoids reopening the fileReader and repeated readLines
     * @param currents list of current reduced hashed
     * @param passLength length of passwords
     * @return list of found reduced hash
     * @throws IOException
     */
    public HashMap<String, ArrayList<Integer>> isInTableBulk(ArrayList<String> currents, int passLength){

        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.table.getTableFile())); 
            String line=null;
            // Initialize hashmap
            // Corresponds to : 
            //  -key= the clear password we're gonna have to compute
            //  -value= a list of indexes on which the password has been found
            HashMap<String, ArrayList<Integer>> res= new HashMap<>();

            // Read all lines
            while((line=reader.readLine()) !=null){
                //Get the clear pwd on this line
                String clair = line.substring(passLength+1, (passLength*2)+1);

                //Instantiate a new empty arrayList, which will serve as list of indexes
                // of found passwords
                ArrayList<Integer> c= new ArrayList<>();
                int index=0;

                // For all passwords we're looking for...
                for(String current: currents){
                    // If the password we're looking for is equals to the clear password, we found it!
                    // add it to the index
                    
                    if (clair.equals(current)) { 
                        //System.out.println("WOA");
                        c.add(index);
                    } 
                    index++;
                }
                // Put the list of indexes in the hashmap
                if(c.size()>0){
                    res.put(line.substring(0, passLength), c);
                }
            }
            reader.close();
            return res;
        } catch (Exception e) {
            System.err.println("une erreur est survenue");
            e.printStackTrace();
        }
        return null;
        
    }

    /*
     * Créer le mot de passe final qui correspond à l'empreinte hash recherchée
     * On utilise le clair du bout de la ligne et on va recréer la chaine avec le nombre d'itérations
     * de recherche (index)
     */
    public String computeFinal(String clair, int index, String hash, int passLength) {
        String finalHash = clair;
        for(int i = 0; i <= index -1; i++) {
            String hashed = this.table.getHashFunction().hash(finalHash);
            finalHash = this.table.getReductionMethod().get(i%this.table.getReductionMethod().size()).reduce(hashed, passLength);
        }
        /* 
            Pour eviter les collisions, on vérifie si l'empreinte qu'on a trouvée et
            à celle recherchée
            Si c'est bon on renvoie le mot de passe
            Sinon, on renvoie null
        */
        if(!this.table.getHashFunction().hash(finalHash).equals(hash)){
            //System.out.println("Collision!");
        }
        return this.table.getHashFunction().hash(finalHash).equals(hash) ? finalHash : null ;
    }

    /**
     * Effectue la recherche dans la table
     */
    public String crackHash(int passLength, String hash) throws IOException {
        String current="";
        // La recherche se fait pour toutes les couleurs de la table
        for (int i = table.getColors() +1 ; i >= 0; i--) {
            String hashStep=hash;
            // Effectue le nombre de réductions suivant l'étape de la recherche
            for(int j=i; j<=table.getColors(); j++){
                current=this.table.getReductionMethod().get(j).reduce(hashStep, passLength);
                hashStep=this.table.getHashFunction().hash(current);
            }
            String mdp=this.isInTable(current, passLength);
            // Si on trouve une similitude dans les empreintes ...
            if(mdp!= null) {
                // ... on va construire le mot de passe et vérifié si il est bon
                String hashCracked = computeFinal(mdp, i, hash, passLength);
                if(hashCracked != null) return hashCracked;  
                // Youpi on a trouvé !!       
            }
        }
        return null;
    }

    public ArrayList<String> crackHashBulk(int passLength, ArrayList<String> hashes){

        ArrayList<String> res= new ArrayList<>();
        ArrayList<String> ogHashes= new ArrayList<>();
        //Deep copy of the passwords we're looking to crack
        for(String hash: hashes){
            ogHashes.add(new String(hash));
        }

        //int index=-1;
        //pre-reduce everything according to rTable format.
        /*for(String hash: hashes){
            index++;
            hashes.set(index, this.table.getReductionMethod().get(this.table.getColors()).reduce(hash, passLength));
        }*/

        for(int i=table.getColors()+1; i>0; i--){
            int index2=-1;
            for(String hash:ogHashes){
                index2++;
                hashes.set(index2, this.table.getReductionMethod().get(i-1).reduce(hash, passLength));
                for(int j=i; j<=table.getColors(); j++){
                        String newH= hashes.get(index2);
                        hashes.set(index2, this.table.getHashFunction().hash(newH));
                        newH=hashes.get(index2);
                        hashes.set(index2, this.table.getReductionMethod().get(j).reduce(newH, passLength));
                }
            }
            
            //System.out.println(ogHashes.size() + "," + hashes.size());
            
            
            HashMap<String, ArrayList<Integer>> pwds= isInTableBulk(hashes, passLength);
            ArrayList<Integer> foundIndex=new ArrayList<>();
            for(String pwd: pwds.keySet()){
                //System.out.println("pwd: "+pwd);
                ArrayList<Integer> indexes= pwds.get(pwd);
                for(int indexe: indexes){
                        //System.out.println("index:"+indexe);
                        String hashCracked=computeFinal(pwd, i-1, ogHashes.get(indexe), passLength);
                        if(hashCracked != null){
                            if(!res.contains(hashCracked)){
                                //System.out.println("MAYBE????");
                                res.add(hashCracked); 
                            }
                            if(!foundIndex.contains(indexe)){
                                foundIndex.add(indexe);
                            }
                            
                        }
                }
            }

            //Small optimization, remove already found hashes from lists
            //System.out.println(foundIndex.size());
            Collections.sort(foundIndex);
            for(int k=foundIndex.size()-1; k>=0; k--){
                //foundIndex.get(k);
                ogHashes.remove(foundIndex.get(k)+0);
                hashes.remove(foundIndex.get(k)+0);
            }

        }
        //System.out.println(doublon);
        return res;
    }
}