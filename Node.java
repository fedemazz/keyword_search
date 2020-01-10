import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class Node {

    private int r; //dimensione ipercubo
    private int n; //numero del nodo 
    private String id; //stringa id del nodo, composta dal suo codice binario
    private BitSet bitset; //bitset del nodo, in particolare tiene traccia dei bit a 1
    private ArrayList<Node> neighbors; //in un implementazione reale sarebbero gli indirizzi?
    private Map<String, String> nodeList; //la lista degli id di tutti gli altri nodi 
    private Map<Set<String>, ArrayList<String>> references; //coppia chiave valore, dove il valore in un implementazione reale sarebbe l'indirizzo di una transazione/canale iota
    private Map<String, String> objects;

    public Node(){
    }

    public Node (int n, int r){
        this.r = r;//r è la dimensione dell'ipercubo
        this.n = n; //numero del nodo che verrà trasformato in binario
        this.id = createBinaryID(n); 
        this.bitset = createBitset(this.id);
        this.neighbors = new ArrayList<Node>();
        this.nodeList = createNodeList();
        this.references = new HashMap<Set<String>, ArrayList<String>>();
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

    private Map<String, String> createNodeList(){
        Map<String, String> list = new  HashMap<String, String>();
        for (int i = 0; i <Math.pow(2, getR()); i++){
            String currentID = createBinaryID(i);
            list.put(currentID, "indirizzo: " + currentID);
        }
        return list;
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

    public Set<String> getNodeList(){
        return nodeList.keySet();
    }

    public ArrayList<String> getReference(Set<String> key) {
        if (this.references.containsKey(key)){
            return this.references.get(key);
        } else return null;
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
            int kBit = hashFunction(entry, r);
            // setto il k-esimo bit del bitset di ricerca kSet ad 1
            kSet.set(kBit); 
        }
        return kSet;
    }

    
    private static int hashFunction(String key, int r){
        return key.hashCode()%r;
    }

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
        if(bs1Temp.nextSetBit(0) <= childrenBitSet.nextSetBit(0)){
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
            if (entry.getOne().nextSetBit(0) == 0) {
                    root.addChild(new NodeSBT(entry.getId(), entry.getOne()));
            }
                else {
                    //la prima volta aggiungo tutti i vicini con un bit di differenza all'albero (sono figli di root)
                    if (init) {
                        root.addChild(entry.generateSBT(false));
                    } 
                    else {  
                        //caso ricorsivo per il livello superiore al secondo
                        //forse ci va anche questo controllo  this.getOne().previousSetBit(getR()) == entry.getOne().previousSetBit(getR())  
                        if (isChildren(this.getOne(), entry.getOne())){
                        root.addChild(entry.generateSBT(false));
                        }
                    }
            }
        }
        return root;
    }

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
        if (this.references.containsKey(oKey)) {
            this.references.get(oKey).add(idObject);
        } else {
            //altrimenti creo una nuova entry nell'index contente, per ora, solamente la reference dell'ogetto aggiunto
            ArrayList<String> object = new ArrayList<String>();
            object.add(idObject);
            this.references.put(oKey, object);
        }
    }

    public ArrayList<String> getObjects(Hypercube hypercube, Set<String> keySet, int c){
        ArrayList<String> result = new ArrayList<String>();
        //hash table references <Kσ, σ>
        //do il set di keyword in input al nodo che lo gestisce (K)
        //ottengo una lista di id. {σ1....σn}
        //Ogni id si riferisce ad un oggetto differente. Tutti gli oggetti hanno in comune il set di keyword 
        ArrayList<String> reference = new ArrayList<String>(this.findTargetNode(generateBitSet(keySet)).getReference(keySet));

        //controllo sul conteggio dei risultati 
        //se inferiore ai risultati attesi esplorare SBT
        NodeSBT sbtRoot = generateSBT(true);

        //se ho risultati
        if (reference != null){
            //per ogni oggetto
            for (String idObject : reference){
                //cerco il nodo che mantiene l'oggetto σ (ci arrivo attraverso l'hash table <σ, u>)          
                //e recupero da lui l'oggetto vero e proprio
                result.add(this.findTargetNode(createBitset(hypercube.getMapping(idObject))).getObject(idObject));
            }
        }
        return result;
    }


}

