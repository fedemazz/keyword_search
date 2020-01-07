# keyword_search
Simulazione ricerca attraverso un set di keyword

Compilare tutti i file ed eseguire service.java

Verrà simulata la creazione di un ipercubo dove ogni nodo sarà rappresentato da un oggetto di tipo Node. 
Ogni nodo predispone di un id, un bitset e la lista dei suoi neighbors. 
E' possibile avviare una ricerca dando in input una serie di keyword. Data la serie di keyword K, verrà definito il nodo che se ne occupa e si verrò reindirizzati ad esso. 
Il nodo responsabile creerà anche l'SBT relativo. 


Esempio di stampa SBT
I nodi allo stesso livello fanno parte dello stesso livello dell'albero.
(E' lo stesso albero di figura 4b del paper)

0100

     --0101
     
     --0110
     
           --0111
           
     --1100
     
           --1110
           
                 --1111
                 
           --1101
           
