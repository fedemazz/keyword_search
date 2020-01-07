import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class service {

    public static void main(String[]args){
        
        System.out.println("Inserisci le dimensione dell'ipercubo:");
        Scanner scanner = new Scanner(System. in);
        int r=scanner.nextInt();

        //creo l'ipercubo di r-dimensioni
        Hypercube hypercube = new Hypercube(r);

        //Simulo la connessione ad un nodo dell'ipercubo
        Node connectedNode = new Node();
        connectedNode = hypercube.getNode(randomNode(r));

        printLog(hypercube, connectedNode);

        //definisco un set di keyword
        //Set<String> K = createKSet();

        String decision;
        boolean yn = true;

        //bitset utilizzato per rappresentare il set di keyword
        BitSet kSet = new BitSet(r);
        ArrayList<String> kStringSet = new ArrayList<String>(); 
        String targetNodeId;
        
        Scanner scannerKey = new Scanner(System.in);
        System.out.println("Per quale parola chiave vuoi cercare?");

        while(yn){
            
            //recupero parola chiave inserita dall'utente
            kStringSet.add(scannerKey.nextLine());
            //converto la parola chiave in un numero tra 0 e r
            int kBit = hashFunction(kStringSet.get(kStringSet.size()-1), r);
            // setto il k-esimo bit del bitset di ricerca kSet ad 1
            kSet.set(kBit); 

            System.out.println("Inserire altra keyword?  yes or no");
            decision = scannerKey.nextLine();
    
            switch(decision){
            case "yes":
                yn = true;
                break;
            case "no": 
                yn = false;
                break;
            default : 
                System.out.println("please enter again ");
            }
        }
        scannerKey.close();
        
        System.out.print("Il set di keyword e': {");
        for (String entry : kStringSet){
            System.out.print(entry + ",");
        }
        //creo stringa del targetNode per un log
        targetNodeId = getStringSearched(kSet, r);
        System.out.println("}\nNodo che si occupa della keyword: " + targetNodeId);
        System.out.println("Cerco il nodo: " + targetNodeId);

        //collego il servizio non più con il nodo casuale ma con il nodo che si occupa del set di chiavi
        connectedNode = connectedNode.findTargetNode(kSet);
        if (connectedNode != null){
            System.out.println("Ho trovato il nodo: " + connectedNode.getId());

            System.out.println("Spanning binomial tree: ");
            printTree(connectedNode.createSBT(true), "--");
        }
        else {
            System.out.println("Non è stato possibile collegarsi con il nodo");
        }

        if(connectedNode.getObjects(kStringSet) != null){
            ArrayList<String> searchedObjects = new ArrayList<String>(connectedNode.getObjects(kStringSet));
                for (String entry : searchedObjects){
                    System.out.println(entry);
                }
        } else {
            System.out.println("La ricerca non ha prodotto risultati");
        }

    }

    
    private static int hashFunction(String key, int r){
        return key.hashCode()%r;
    }

    //non serve, sono i nodi che verificano se hanno le keyword
    //creo set di keyword di test (una keyword per ogni lettera dell'alfabeto)
    /*private static Set<String> createKSet(){
        String stringSet ="abcdefghijklmnopqrstuvwxyz";
        Set<String> K = new HashSet<String>();
        for (int i=0; i<stringSet.length(); i++){
        K.add(String.valueOf(stringSet.charAt(i)));   
        }
        return K;
    }*/

    private static String getStringSearched(BitSet bs, int r){
        String searched="";
        for (int i = 0; i < r; i++){
            if(bs.get(i) == true){
                searched = "1" + searched;
            }
            else searched = "0" + searched;
        }
        return searched; 
    }

    public static String randomNode(int r){
    String idString = Integer.toBinaryString((int)(Math.random()*(Math.pow(2,r))));
        while (idString.length() < r){
            idString = "0" + idString;
        }
        return idString;
    }

    public static void printTree(NodeSBT root, String bo){ 
        if (root.getFather() == null){
            System.out.println(root.getId());
        }
        for (NodeSBT entry : root.getChildren()){
            System.out.println("    " + bo + entry.getId());
        if(!entry.getChildren().isEmpty()){
            printTree(entry, "      " + bo);
        }
        }
    }

    private static void printLog(Hypercube hypercube, Node connectedNode){
         //stampe debug
         System.out.println("Ipercubo creato");
         System.out.println("I nodi creati sono: \n" + connectedNode.getNodeList());

        System.out.println("Connesso al nodo: " + connectedNode.getId() + "\nI suoi neighbors sono:"); 
        for (Node node : connectedNode.getNeighbors()){
            System.out.println(node.getId());
            }
    }
}
