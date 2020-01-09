import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Node {

    private int r; //dimensione ipercubo
    private int n; //numero del nodo 
    private String id; //stringa id del nodo, composta dal suo codice binario
    private BitSet bitset; //bitset del nodo, in particolare tiene traccia dei bit a 1
    private Map<ArrayList<String>,ArrayList<String>> objects; //coppia chiave valore, dove il valore in un implementazione reale sarebbe l'indirizzo di una transazione/canale iota
    private ArrayList<Node> neighbors; //in un implementazione reale sarebbero gli indirizzi?
    private Map<String, String> nodeList; //la lista degli id di tutti gli altri nodi 

    public Node(){
    }

    public Node (int n, int r){
        this.r = r;//r è la dimensione dell'ipercubo
        this.n = n; //numero del nodo che verrà trasformato in binario
        this.id = createBinaryID(n); 
        this.bitset = createBitset(this.id);
        this.objects = new HashMap<ArrayList<String>, ArrayList<String>>();
        this.neighbors = new ArrayList<Node>();
        this.nodeList = createNodeList();
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

    public Node findTargetNode(BitSet targetSet){
        System.out.println("Cerco nodo...");
        if (this.getOne().equals(targetSet)){
            return this;
        }
        else {
            return this.nearestNode(targetSet).findTargetNode(targetSet);
        }
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
    public NodeSBT createSBT (boolean init){

        NodeSBT root = new NodeSBT(this.getId(), this.getOne());
        for(Node entry : this.getNeighborsIncluded()) {                      
            //se il primo bit (posizione 0) è settato a 1 sono arrivato ad una foglia
            if (entry.getOne().nextSetBit(0) == 0) {
                    root.addChild(new NodeSBT(entry.getId(), entry.getOne()));
            }
                else {
                    //la prima volta aggiungo tutti i vicini con un bit di differenza all'albero (sono figli di root)
                    if (init) {
                        root.addChild(entry.createSBT(false));
                    } 
                    else {  
                        //caso ricorsivo per il livello superiore al secondo
                        //forse ci va anche questo controllo  this.getOne().previousSetBit(getR()) == entry.getOne().previousSetBit(getR())  
                        if (isChildren(this.getOne(), entry.getOne())){
                        root.addChild(entry.createSBT(false));
                        }
                    }
            }
        }
        return root;
    }

    //dato il set di keyword a cui appartiene e l'oggetto
    public void addObject(ArrayList<String> key, String value) {
        //se nell'index è gia presente la keyword, aggiungo l'oggetto alla lista di oggetti per quella keyword
        if (this.objects.containsKey(key)) {
            this.objects.get(key).add(value);
        } else {
            //altrimenti creo una nuova entry nell'index contente, per ora, solamente l'ogetto aggiunto
            ArrayList<String> object = new ArrayList<String>();
            object.add(value);
            this.objects.put(key, object);
        }
    }

    //dato il set di keyword in input restituisco tutti gli oggetti appartenenti a quel set
    public ArrayList<String> getObjects (ArrayList<String> key){
        if(this.objects.containsKey(key)){
            return this.objects.get(key);
        }
        return null;
    }
}

