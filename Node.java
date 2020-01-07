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
    private Map<String, String> objects; //coppia chiave valore, dove il valore in un implementazione reale sarebbe l'indirizzo di una transazione/canale iota
    private ArrayList<Node> neighbors; //in un implementazione reale sarebbero gli indirizzi?
    private Map<String, String> nodeList; //la lista degli id di tutti gli altri nodi 

    public Node(){
    }

    public Node (int n, int r){
        this.r = r;//r è la dimensione dell'ipercubo
        this.n = n; //numero del nodo che verrà trasformato in binario
        this.id = createBinaryID(n); //provare a togliere r
        this.bitset = createBitset(this.id);
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
            return this.getNeighbors().get(getRand()).findTargetNode(targetSet);
        }
    }

    public int getRand(){
        int x = (int)(Math.random()*(( (this.getNeighbors().size()-1) - 0 ) +1 )) + 0 ;
        return x;
    }

    //controllo se il primo bitset è contenuto nel secondo
    public static boolean isIncluded(BitSet bitSet1, BitSet bitSet2){
        BitSet includedBitSet = new BitSet();
        BitSet bs1Temp = new BitSet();
        BitSet bs2Temp = new BitSet();
        includedBitSet.or(bitSet1); 
        bs1Temp.or(bitSet1);
        bs2Temp.or(bitSet2);
        bs1Temp.and(bs2Temp);
        if ( bs1Temp.equals(includedBitSet)) {
            //System.out.println(bitSet1);
            //System.out.println(includedBitSet);
            return true;
        }
        return false;
    }

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
}

