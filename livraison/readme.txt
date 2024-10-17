Arborescence de rainbow_tables:
-code : code source du projet
-data : données variées ainsi que rainbow table créées dans le sous dossier "rainbow_tables"
-experimentations : résultats des experimentations

---------------------------------------------------------------

Pour lancer les commandes ANT, se placer dans code/. Les commandes disponibles sont:

/// Commandes d'utilitée ///

- ant prepare : prépare le dépot du projet (créé les répertoire manquants)
- ant clean : supprime le contenu des dossier bin et doc
- ant cleanExp : supprime le contenu des dossiers data/rainbow_tables et experimentations/data (clean les fichier d'experimentations)
- ant compile : clean, prepare, puis compile le code dans le répertoire bin
- ant javadoc : créé la javadoc et la place dans le répertoire doc

/// Commandes de demo ///

- ant runDemoCreation : lance la démo de la création de plusieurs rainbow tables

/// Commandes de demo (après compilation)///

- java -cp "bin" rainbow_tables.demos.DemoReductionHash : lance la démo sur la boucle reduction/hachage
- java -cp "bin" rainbow_tables.demos.DemoMultipleReduction : lance la démo sur l'effet d'avoir de multiple fonctions de réduction
- java -cp "bin" rainbow_tables.demos.DemoAttack <nom de la table> <mot de passe> : lance une démo sur l'attaque sur la table mise en argument et le haché du MDP
