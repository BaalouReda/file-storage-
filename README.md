# coffre fort
## Description
ce projet est un coffre fort virtuel qui permet de stocker des mots de passe et des notes en toute sécurité.
## Installation
1. Cloner le projet
2. Installer les dépendances
3. lancer le docker compose pour avoir une instance de mongo db
4. lancer le projet
5. la collection des apis est dans postman ( //TODO: ajouter la collection dans le projet)
## Details Techniques
- le projet est basé sur le stockage des données dans une base de données mongo db specialement dans GRIDFS pour les fichiers
pour plus details sur gridfs: https://docs.mongodb.com/manual/core/gridfs/
- il encrypt les fichier avant les stocker dans GRIDFS
- 
//TODO: ajouter les metadata dans les fichiers stockés
//TODO: ajouter le auditing dans la base
//TODO: ajouter  la signature eleptique pour les jwt pour les api calls ( a voir utilisation de spring security authorization server)
//TODO: ajouter les tests unitaires
//TODO: avoir l'ajout dun bucket GRIDFS pour chaque utilisateur
//TODO: ajouter la gestion des roles et des permissions