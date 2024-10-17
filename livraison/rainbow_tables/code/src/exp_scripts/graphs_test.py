from csv import reader
import os
import matplotlib.pyplot as plt

shared_data_path= "../../../experimentations/shared_data/"

# Créer une liste avec les différents paramètres d'une colonne
# utiliser pour les pivots et pour le paramètre testé
def makeListPivotingParam(liste, num):
    pivots = []
    for l in liste:
        if l[num] not in pivots:
            pivots.append(l[num])
    del pivots[0]
    return pivots

# Créer la liste avec le nombre de mot de passe pour un paramètre
def makeListNumMDP(liste):
    numMDP = []
    for l in liste:
        couple=[int(l[3]), int(l[4])]
        numMDP.append(couple)
    return numMDP

# Créer la liste avec le pourcentage de mot de passe trouvés
def makeListFindMDP(liste, num_test):
    for i in range(len(liste)):
        liste[i] = (liste[i][0]/liste[i][1])*100
    return liste

# Isole les lignes du fichier pour un paramètre donné
def makeListByParam(file, param):
    listeParam = []
    for l in file:
        if l[1] == param:
            listeParam.append(l)
    #Sort pour avoir des résultats corrects
    return listeParam

# Split le nom du fichier pour récupérer le paramètre voulu
def splitFileName(file_name):
    name_test = file_name.split("_")
    return name_test

# Créer le titre du graphique
def make_title(list_param):
    if list_param[0] == 'C':
        test = "du nombre de couleurs"
    elif list_param[0] == 'T':
        test = "de la taille de la table"
    elif list_param[0] == 'S':
        test = "de la taille des MDP"


    if list_param[1] == 'C':
        pivot = "nombre de couleurs"
    elif list_param[1] == 'T':
        pivot = "taille de la table"
    elif list_param[1] == 'S':
        pivot = "taille des mots de passe"
    
    title = "Efficacité en fonction "+test+ " ("+pivot+" en variation)."
    return title

# Créer le titre pour la légende (utiliser aussi pour le xlabel)
def make_legend(test):
    if test == "C":
        return "Nombre de couleurs"
    elif test == "T":
        return "Taille de la table"
    elif test == "S":
        return "Taille des mots de passe"
        

# Dessine un beau graphique
def compute_graph(file, file_name, expDir):
    name = splitFileName(file_name)
    # Créer les listes (pivot et paramètre test)
    pivots = makeListPivotingParam(file, 1)
    paramTest = makeListPivotingParam(file, 0)

    # On instancie une liste pour chaque courbe
    courbes = []
    for pivot in pivots:
        courbes.append(makeListFindMDP(makeListNumMDP(makeListByParam(file, pivot)), file[1][4]))

    for i in range(len(courbes)):
        plt.plot(paramTest, courbes[i], label = pivots[i])
        

    # Ajoute le titre et la légende
    plt.title(make_title(splitFileName(file_name)))
    
    plt.xlabel(make_legend(name[0]))
    plt.ylabel("Pourcentage de réussite (en %)")
    
    plt.legend(title = make_legend(name[1]))

    # Sauvegarde le graphique
    plt.savefig(expDir+"graphs/"+file_name[:-3], bbox_inches= "tight")
    plt.close()


#Cree un sous dossier graphs et y met tout les graphes de l'exp au chemin précisé en argument
def createGraphsForExpFolder(expDir):
    graphsPath=os.path.join(expDir, "graphs")
    if not os.path.exists(graphsPath):
        os.mkdir(graphsPath)
    
    
    for filenames in os.scandir(expDir):
        print(filenames.name)
        if(os.path.isfile(filenames)):
            with open(filenames.path, "r") as my_file:
            
                file_CSV = []
                file_reader = reader(my_file)
                for i in file_reader:
                    file_CSV.append(i)

                my_file.close()

            compute_graph(file_CSV, filenames.name, expDir)

# -- Main --
if __name__== "__main__":
    for dirNames in os.scandir(shared_data_path):
        if(os.path.isdir(dirNames)):
            createGraphsForExpFolder(shared_data_path+dirNames.name+"/")


    #createGraphsForExpFolder(shared_data_path+"exp_MD503-25-15_34_33/")
    #compute_graph(file_CSV, file_name)

