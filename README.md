# Projet De Programmation Tsuro ( VP2B )

# Tsuro ( VP2B )

![Visual_IdentityTsuro](https://github.com/user-attachments/assets/3aefd015-14ac-4f0a-adc9-cdf6cce80ef9)

# Exécuter le projet: 

## Sur Terminal : Exécutez ce script ( copier cette commande directement et assurez vous d'avoir installé le JDK Java) 
./run.sh 

-> Assurez vous d'avoir ajouter les droits d'exécutions au script avec :

chmod +x run.sh

-> Si vous n'avez pas la commande jar alors exécuter le script de secours 

./run2.sh

## Exécuter le jar : 
sinon cliquez deux fois sur le jar pour jouer directement 


## Si vous voulez exécuter le jeu sur un IDE :

### Intellij Idea : 
        Ouvrez le dossier du jeu 
        Accédez aux Paramètres -> ProjectStructure -> Modules -> Add Module 
        Sélectionner le dossier src comme sources
        accédez à Dependencies et ajouter les deux Jar dans le dossier lib
        Exécutez la classe TsuroGame.java

### VS Code :
    Ouvrez le Dossier du jeu 
    Éxécuter la classe TsuroGame.java Directement 

# Exécuter les Tests :
./runTests.sh

-> Assurez vous d'avoir ajouter les droits d'exécutions au script avec :

chmod +x run.sh


## Auteurs
 - Arkam Rayane 
 - Cherchour Naim
 - Chioukh Sami
 - Kecman Dejan
 - Lounici Sifaks 



### Comment jouer :

Tsuro est un jeu de plateau 6x6 qui peut être joué en mode multijoueur (de 2 à 8 joueurs) ou en solo contre un bot. 
Le jeu propose trois variantes : CLASSIC, LONGEST_PATH, et TIMER. Le but du jeu est de placer une tuile parmi trois proposées dans le deck,
avec la possibilité de les faire pivoter. Votre pion suivra le chemin tracé par la tuile posée. 
Si vous sortez du plateau, vous perdez. 
Si vous entrez en collision avec un autre pion (joueur), vous perdez tous les deux.
Si vous êtes le seul survivant, VOUS GAGNEZ LA PARTIE.

-> La liste des Règles est accessible sur le menu principal en cliquant sur le bouton ?

-> À vous de choisir la meilleure tuile et la stratégie la plus efficace pour rester en jeu 
   et éliminer les autres joueurs.


### Variantes de jeu :

# CLASSIC :
        Restez le dernier en jeu pour gagner.
# LONGEST_PATH : 
        Parcourez le plus long chemin possible. Même si vous perdez en sortant du plateau, vous pouvez gagner si vous avez le parcours le plus long.

# TIMER :
        Vous avez 20 secondes pour placer une tuile. Si le temps s'écoule, une tuile aléatoire est placée automatiquement.


### Tuiles Magiques :

# CLASSIC et TIMER : 
Cliquer sur une tuile magique (fond doré) vous permet de sauter votre tour sans placer de tuile, vous permettant de rester en jeu plus longtemps.

# LONGEST_PATH : 
Les tuiles magiques vous permettent de poser deux tuiles consécutivement pour agrandir votre parcours.

### Fonctionnalités implémentées :

## Sauvegarde :
        Vous pouvez sauvegarder vos parties et y accéder lors de votre session. 
        N'oubliez pas votre MOT DE PASSE ! Vous pouvez reprendre vos parties sauvegardées depuis votre profil.

## Le Bot :
Le bot est un adversaire qui place une tuile automatiquement après vous. 
Il dispose également de trois tuiles qu'il teste pour choisir la meilleure, 
en utilisant l'algorithme MinMax, afin de rester sur le plateau et tenter de vous éliminer. 
Tout dépend des tuiles qui lui sont proposées, donc vous pouvez gagner facilement ou difficilement !

## Tests :
Les tests unitaires avec JUnit 5 ont permis de s'assurer que plusieurs fonctionnalités du modèle et de la vue étaient correctes, 
comme la génération et la rotation des tuiles.

## Menu
Vous pouvez consulter les règles du jeu dans le menu principal en cliquant sur le bouton ?.
Vous pouvez également désactiver le son des clics et du jeu dans la page Options.

    
        
    
