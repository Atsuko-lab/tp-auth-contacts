Q1 : Si on avait enlever le userID, chaque personne verrait les contacts des autres.

Q2 : Il y aurait un problème, une erreur car une clé étrangère est lié à la clé primaire de l'utilisateur donc si on supprime un utilisateur, ça devrait supprimer également les contacts lié à cette utilisateur, ce que phpmyadmin / mysql refuse car la clé étrangère protège l'intégrité des données.

Q3 : un utilisateur malveillant, si il connait l'id d'un contact ou même si il ne le connaissait pas (en tapant au pif un id), pourrait supprimer ou modifier un contact qui ne lui appartient pas. user_id ajoute une sécurité car cela prend uniquement en compte l'id de l'utilisateur connecté.