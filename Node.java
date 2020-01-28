import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Node {

    private int r; //dimensione ipercubo
    private int n; //numero del nodo 
    private String id; //stringa id del nodo, composta dal suo codice binario
    private BitSet bitset; //bitset del nodo, in particolare tiene traccia dei bit a 1
    private Hashtable<String, Integer> keywordsMap;
    private ArrayList<Node> neighbors; //in un implementazione reale sarebbero gli indirizzi?
    private Map<Integer, String> references; //gli oggetti che hanno in comune la keyword di cui si occupa un nodo
    private Map<String, String> objects; //gli oggetti mantenuti dal nodo (quelli che sono stati caricati da esso)

    public Node(){
    }

    public Node (int n, int r){
        this.r = r;//r è la dimensione dell'ipercubo
        this.n = n; //numero del nodo che verrà trasformato in binario
        this.id = createBinaryID(n); 
        this.bitset = createBitset(this.id);
        this.keywordsMap = createKeywordsMap();
        this.neighbors = new ArrayList<Node>();
        this.references = new Hashtable<Integer, String>();
        this.objects = new HashMap<String, String>();
    }

    //identifico codice id del nodo
    private String createBinaryID(int n){
        //passo il numero del nodo per identificare il suo codice binario
        String idString = Integer.toBinaryString(n);
        //se il codice binario del nodo ottenuto non è della lunghezza r (dimensione dell'ipercubo) allora aggiungo gli zeri necessari
        while (idString.length() < getR()){
            idString = "0" + idString;
        }
        return idString;
    }

    //passo l' id del nodo e ottengo un bitset dove tengo traccia dei bit ad 1
    private BitSet createBitset(String id){
        return BitSet.valueOf(new long[] { Long.parseLong(id, 2) });
    }

    private Hashtable<String, Integer> createKeywordsMap(){
        String keys = "abcdefghijklmnopqrstuvwxyz";
        Hashtable<String, Integer> result = new Hashtable<String, Integer>();
        for (int i=0; i<getR(); i++){
            result.put(Character.toString(keys.charAt(i)), i);
        }
        return result;
    }


    //setto i vicini del nodo
    public void setNeighbors(Map <String, Node> nodes) {
        //scorro l'hashMap contenente tutti i nodi dell'ipercubo istanziati 
       //e li confronto agli stessi, per trovare i "neighbors" dei vari nodi
       //cioè quelli che differiscono di un bit rispetto al nodo trattato
        for (Map.Entry<String, Node> entry : nodes.entrySet()) {
            if (differOneBit(this.getN(), entry.getValue().getN())){
            neighbors.add(entry.getValue());
            }
        }
       }

    //metodo per verificare se due numeri differiscono solo di un bit
    static boolean differOneBit(int a, int b) { 
        int x = a ^ b;
        return x!= 0 && ((x & (x - 1)) == 0); 
    }

    public int getR(){
        return this.r;
    }

    public int getN(){
        return this.n;
    }

    public String getId(){
        return this.id;
    }

    public BitSet getOne(){
        return this.bitset;
    }

    public BitSet getZero(String id){
        return null;
    }

    //restituisco i vicini del nodo
    public ArrayList<Node> getNeighbors(){
        return this.neighbors;
    }

    //restituiscono i vicini del nodo i cui bitset includono il nodo stesso
    //tra tutti i vicini prendo solo quelli che soddisfano la condizione isIncluded
    public ArrayList<Node> getNeighborsIncluded(){
        ArrayList<Node> neighborsIncluded = new ArrayList<Node>();
        for (Node neighbor : this.getNeighbors()) {
            if (isIncluded(this.getOne(), neighbor.getOne())) {
                neighborsIncluded.add(neighbor);
            }
        }
        return neighborsIncluded;
    }

    public Collection<String> getReference() {
        return this.references.values();
    }

    public String getObject(String key) {
        if (this.objects.containsKey(key)){
            return this.objects.get(key);
        } else return null;
    }

    public boolean hasNeighbor(String id){
        for (Node neighbor : this.getNeighbors()) {
            if (neighbor.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasNeighbor(BitSet idBitSet){
        for (Node neighbor : this.getNeighbors()) {
            if (neighbor.getOne().equals(idBitSet)) {
                return true;
            }
        }
        return false;
    }


    public Node findTargetNode(BitSet bitSet){
        //System.out.println("Cerco nodo...");
        if (this.getOne().equals(bitSet)){
            return this;
        }
        else {
            return this.nearestNode(bitSet).findTargetNode(bitSet);
        }
    }

    public BitSet generateBitSet(Set<String> keySet){
        BitSet kSet = new BitSet();
        for (String entry : keySet){
            //inizialmente designavo il bit con un'hash function
            //int kBit = hashFunction(entry, r);
            //ora lo deciso attraverso un mapping gia prestabilito
            if (this.keywordsMap.containsKey(entry)){
            int kBit = this.keywordsMap.get(entry);
            // setto il k-esimo bit del bitset di ricerca kSet ad 1
                kSet.set(kBit); 
            }
        }
        if (kSet.nextSetBit(0) == -1){
            return null;
        } else {
            return kSet;}
    }

    /*private static int hashFunction(String key, int r){
        return key.hashCode()%r;
    }*/

    public Node nearestNode(BitSet targetSet){
        BitSet tSet = new BitSet();
        tSet.or(targetSet);

        for(Node entry : this.getNeighbors()){
            if (xor(targetSet, entry.getOne()).cardinality() < xor(targetSet, this.getOne()).cardinality()){
                return entry;
            }
        }
        return null;
    }

    private BitSet xor(BitSet bsTarget, BitSet bsNeigh){
        BitSet bs1 = new BitSet();
        BitSet bsXor = new BitSet();
        bs1.or(bsTarget);
        bsXor.or(bsNeigh);
        bsXor.xor(bs1);
        return bsXor;
    }



    //controllo se il primo bitset è contenuto nel secondo
    //01100 e 01101. il primo è contenuto nel secondo
    public static boolean isIncluded(BitSet bitSet1, BitSet bitSet2){
        BitSet includedBitSet = new BitSet();
        BitSet bs1Temp = new BitSet();
        BitSet bs2Temp = new BitSet();
        includedBitSet.or(bitSet1); 
        bs1Temp.or(bitSet1);
        bs2Temp.or(bitSet2);
        bs1Temp.and(bs2Temp);
        if ( bs1Temp.equals(includedBitSet)) {
            return true;
        }
        return false;
    }

    //controllo se bitset2 può essere inserito come figlio di bitset 1 nell'SBT
    //il criterio è che il bit di differena tra bs1 e bs2 sia a destra dell'ultimo bit di bs1
    //01100 e 01110. bs2 è children
    //01100 e 11000. bs2 non è children
    public boolean isChildren(BitSet bitSet1, BitSet bitSet2){
        BitSet childrenBitSet = new BitSet();
        BitSet bs1Temp = new BitSet();
        BitSet bs2Temp = new BitSet();
        childrenBitSet.or(bitSet1); 
        bs1Temp.or(bitSet1);
        bs2Temp.or(bitSet2);
        bs1Temp.xor(bs2Temp);
        if(bs1Temp.nextSetBit(0) < childrenBitSet.nextSetBit(childrenBitSet.nextSetBit(0)+1)){
            return true;
        }
        return false;
    }


    //creo l'sbt relativo ad un set di keyword cercato
    //il set di keyword cercato è quello su cui è chiamato questo metodo
    public NodeSBT generateSBT (boolean init){
        
        NodeSBT root = new NodeSBT(this.getId(), this.getOne());
        for(Node entry : this.getNeighborsIncluded()) {                      
            //se il primo bit (posizione 0) è settato a 1 sono arrivato ad una foglia
            //se va nel primo posto vuoto 
            //if (entry.getOne().nextSetBit(0) == 0) {
            if(entry.getOne().nextSetBit(this.getOne().nextClearBit(0)) == this.getOne().nextClearBit(0)){
                    root.addChild(new NodeSBT(entry.getId(), entry.getOne()));
            }
                else {
                   if (init){
                    root.addChild(entry.generateSBT(false));
                   }
                        //caso ricorsivo per il livello superiore al secondo
                        //forse ci va anche questo controllo  this.getOne().previousSetBit(getR()) == entry.getOne().previousSetBit(getR())  
                      else { 
                          if (isChildren(this.getOne(),  entry.getOne())){
                        root.addChild(entry.generateSBT(false));
                       }          
                    }         
            }
        }
        return root;
    }

    public NodeSBT generateSBT2 (boolean init){
        
        NodeSBT root = new NodeSBT(this.getId(), this.getOne());

        for(Node entry : this.getNeighborsIncluded()) {                      
            //se il primo bit (posizione 0) è settato a 1 sono arrivato ad una foglia
            //se va nel primo posto vuoto 
            //if (entry.getOne().nextSetBit(0) == 0) {
            if(entry.getOne().nextSetBit(this.getOne().nextClearBit(0)) == this.getOne().nextClearBit(0)){
                    root.addChild(new NodeSBT(entry.getId(), entry.getOne()));
            }
                else {
                   if (init){
                    root.addChild(entry.generateSBT(false));
                   }
                        //caso ricorsivo per il livello superiore al secondo
                        //forse ci va anche questo controllo  this.getOne().previousSetBit(getR()) == entry.getOne().previousSetBit(getR())  
                      else { 
                        if (isChildren(this.getOne(),  entry.getOne())){
                        root.addChild(entry.generateSBT(false));
                       }          
                    }         
            }
        }
        return root;
    }

    /*public NodeSBT controlSBT(NodeSBT root){

        if (root.getFather()!=null){
            for (NodeSBT entry : root.getChildren()){
                if(!isChildren(root.getFather().getBS(), entry.getBS()))
                root.deleteChild(entry);
                if(!entry.getChildren().isEmpty()){
                    controlSBT(entry);
                }
        }
    }else  return null;
    }*/

    public static String getMd5(String input) { 
        try { 
  
            // Static getInstance method is called with hashing MD5 
            MessageDigest md = MessageDigest.getInstance("MD5"); 
  
            // digest() method is called to calculate message digest 
            //  of an input digest() return array of byte 
            byte[] messageDigest = md.digest(input.getBytes()); 
  
            // Convert byte array into signum representation 
            BigInteger no = new BigInteger(1, messageDigest); 
  
            // Convert message digest into hex value 
            String hashtext = no.toString(16); 
            while (hashtext.length() < 32) { 
                hashtext = "0" + hashtext; 
            } 
            return hashtext; 
        }  
  
        // For specifying wrong message digest algorithms 
        catch (NoSuchAlgorithmException e) { 
            throw new RuntimeException(e); 
        } 
    } 

    public void addObject(Hypercube hypercube, Set<String> oKey, String oValue){
        String idObject = getMd5(oValue);
        //inserisco l'oggetto nella lista objects del nodo che ha avviato la richiesta di inserimento
        this.objects.put(idObject, oValue);

        //inserisco l'associazione reference <σ, u> nel nodo L(σ) (in questo caso come detto lo inserisco in una hash table comune che esegue il mapping DHT)
        Insert(hypercube, idObject, this.getId());

        //eseguire il controllo se la copia esiste

        //inserisco nel nodo che si occupa del set di keyword la coppia <Kσ, σ>
        Insert(findTargetNode(generateBitSet(oKey)), oKey, idObject);
    }

    private void Insert(Hypercube hypercube, String idObject, String idNode){
        hypercube.addMapping(idObject, idNode);
    }

    //metodo per aggiungere alla lista di reference la coppia <Kσ, σ>
    //va aggiunta nel nodo che si occupa di Kσ, chiamato responsible
    private void Insert(Node responsible, Set<String> oKey, String idObject){
        responsible.addReference(oKey, idObject);
    }

    private void addReference(Set<String> oKey, String idObject) {
        //se nelle reference è gia presente la keyword, aggiungo l'oggetto alla lista di oggetti per quella keyword
        /*if (this.references.containsKey(oKey)) {
            this.references.get(oKey).add(idObject);
        } else {
            //altrimenti creo una nuova entry nell'index contente, per ora, solamente la reference dell'ogetto aggiunto
            ArrayList<String> object = new ArrayList<String>();
            object.add(idObject);
            this.references.put(oKey, object);
        }*/
        this.references.put(this.references.size(), idObject);
    }

    //22/01/19 
    // modificato modo con cui ottengo posizione del bit settato ad 1
    //ora ho solo un set di keyword per ogni nodo, 
    //non è più necessario richiedere al nodo la lista di solo gli oggetti con quelle keyword ma posso chiedere quegli oggetti
       public ArrayList<String> requestObjects(Set<String> keySet, int c){
        //l'utente chiede al nodo a cui è connesso di trovare il nodo che si occupa del keyset e restituire gli oggetti per quella keyword
        ArrayList<String> result = new ArrayList<>(this.findTargetNode(generateBitSet(keySet)).T_QUERY(generateBitSet(keySet), c, this.getOne()));
        return result;
    }

    public ArrayList<String> T_QUERY(BitSet keySet, int c, BitSet nodeCollecter){
        ArrayList<String> result = new ArrayList<>(this.getReference());
        if (this.getReference().size() >= c) {
        return result;
        } else {
            
            NodeSBT root = generateSBT(true);
            Queue<NodeSBT> queue = new LinkedList<>(root.BFS());
            Queue<NodeSBT> queue2 = new LinkedList<>(root.BFS());

            
            System.out.println("Cerco anche in altri nodi...");

           while(!queue2.isEmpty()){
                System.out.println(queue2.remove().getId());
            }

            while(!queue.isEmpty()){
                                BitSet newSet = queue.remove().getBS();
                                result.addAll(this.findTargetNode(newSet).T_QUERY(newSet, c - result.size(), nodeCollecter, queue));
                                if (result.size() >= c) {
                                    return result;
                                }
            }
            return result;
        }
    }

    public ArrayList<String> T_QUERY(BitSet keySet, int c, BitSet nodeCollecter, Queue<NodeSBT> queue){
        return new ArrayList<>(this.getReference());
    }

    //metodo che permette, una volta ottenuti gli id degli oggetti, di recuperare gli oggetti dal nodo in cui sono contenuti
    public ArrayList<String> getObjects(Hypercube hypercube, ArrayList<String> idObjects){
        ArrayList<String> result = new ArrayList<String>();
       //per ogni oggetto
       for (String id : idObjects){
        //cerco il nodo che mantiene l'oggetto σ (ci arrivo attraverso l'hash table <σ, u>)          
        //e recupero da lui l'oggetto vero e proprio
        result.add(this.findTargetNode(createBitset(hypercube.getMapping(id))).getObject(id));
    }
    return result;
    }

}

