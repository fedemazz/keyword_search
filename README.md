# keyword_search
Simulazione ricerca attraverso un set di keyword

Compilare tutti i file ed eseguire service.java

Verrà simulata la creazione di un ipercubo di dimensione r e verranno istanziati i nodi corrispondenti. 
Ogni nodo è rappresentato da un oggetto di tipo Node e predispone di un id, un bitset e la lista dei suoi neighbors. 

# Ricerca
Il main dell'applicativo è nel file "service.java". Qui è' possibile avviare una ricerca dando in input una serie di keyword.
Viene inizializzato un BitSet di lunghezza r (la stessa della dimensione dell'ipercubo). Inizialmente questo bitset è settato tutto a zero. Ogni volta che l'utente specifica una keyword, una funzione hash mappa la keyword in un numero da 0 ad r. Questo numero (chiamato kBit) è usato per settare a 1 il kBit-esimo bit del BitSet. 
Finito l'inserimento delle keyword si otterrà un BitSet con uno o diversi bit settati ad 1. 

In questo modo verrà definito il nodo che se ne occupa e si verrò reindirizzati ad esso. 
Il nodo responsabile creerà l'SBT relativo e cercherà nel suo index (un' hashmap) se ha oggetti coincidenti con le keyword. (nessun nodo ha oggetti nella sua tabella hash, quindi non verrà trovato alcun oggetto)


# Esempio di stampa SBT.
I nodi allo stesso livello fanno parte dello stesso livello dell'albero.
(E' lo stesso albero di figura 4b del paper)

SBT di 0100


     --0101
     
     --0110
     
           --0111
           
     --1100
     
           --1110
           
                 --1111
                 
           --1101
           
