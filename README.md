# Spring Boot - Caching with Hazelcast (cluster)

--------------------------------------

2 cases:
* Using an *on instance" Cache. Basic use case of Spring Cache. Represented in UserService.
* Using an *All-In* Cache. That's represented in CountryService. All instances are stored in a replicated cache.



## Cache Spring distribué
Dans notre exemple, les utilisateurs sont des données qui:
* changent régulièrement
* sont souvent lues
* sont potentiellement très nombreuses.

On utilise le cache comme une utilisation classique : 
* Les requetes pour retourner des collections ne sont pas cachées
* Le cache est utilisé pour cacher les instances spécifiques
* Le cache est distribué



## All-In Cache "manuel" répliqué

Dans notre exemple, les "Pays" sont des données qui : 
* Ne changent pas souvent (très peu d'écriture)
* Sont très souvent lues (beaucoup de lecture)
* ne sont pas nombreuse (<200) et dont on sait qu'il ne variera jamais beaucoup.

C'est le bon cas d'usage pour le cache "All-In" : Je stocke toutes la collection.
Ce n'est pas le cas d'usage classique des caches. Un cache n'est pas une base de données, et ne doit normallement pas stocker l'intégralité d'une collection.

De plus, comme on souhaite un accès rapide, on privilégie ici une RecplicatedMap.


Ce cas n'est pas pris en compte par Spring Cache. Le CountryService gère ce cas spécifiquement en s'appuyant directement sur une instance replicatedMap géré via une injection de hazelcastInstance.

voir le code pour plus de détails.


## Tests
Lancer 2 instances: 

```
mvn spring-boot:run -Dserver.port=8080
mvn spring-boot:run -Dserver.port=8081

```

faire joujou avec les URL 
```
GET /countries
POST /countries

GET /users/JOD
PUT /users/JOD
DELETE /users/JOD
```

sur les 2 instances et vérifier que les données retournées par le cache sont bien les mêmes.

Attention : pour le test, j'utilise H2 et du coup, comme c'est une base mémoire, chaque instance possède ses propres données (il y'aura donc dans ce cas une désynchro entre base et cache!)
Sur une appli en prod ou les 2 instances tapent sur une meme base ça sera OK !

A savoir : des tests sur Postgre ne sont pas hyper significatif en local a cause du cache de requete Postgre (Shared_mem).
-> je suis donc resté sur H2 et j'ai simulé des réponses longues avec des sleeps positionné avec AOP.