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
        String choice;

        do{

            System.out.println("\n****MENU****");
			System.out.println("1. INSERT");
			System.out.println("2. SEARCH");
			System.out.println("3. DELETE");
            System.out.println("4. EXIT");
            System.out.print("Fai una scelta (1, 2, 3, 4): ");
            Scanner scan = new Scanner(System.in);
            choice = scan.nextLine();

            switch(choice){


                case "1": insert(hypercube, connectedNode);
                break;

                case "2": search(hypercube, connectedNode, r);
                break;

                case "3": //delete
                break;

                case "4":	//Exit

					System.out.println("EXIT");
					break;

				default:
					break;

            }

        } while(!(choice.equals("4")));
        

    }

    private static void search(Hypercube hypercube, Node connectedNode, int r) {

        System.out.print("2. SEARCH: ");

        //set utilizzato per rappresentare il set di keyword
        Set<String> kStringSet = new HashSet<String>(); 
        //variabile per log
        String targetNodeId;
        
        //chiedo le keyword da inserire all'utente
        kStringSet = insertKeywords();
        
        //visualizzo a schermo le keyword inserite
        System.out.print("Il set di keyword e': " + kStringSet);

        //visualizzo a schermo il nodo che si occupa della keyword
        targetNodeId = getStringSearched(connectedNode.generateBitSet(kStringSet), r);
        System.out.println("\nNodo che si occupa della keyword: " + targetNodeId);
        System.out.println("Cerco il nodo: " + targetNodeId);

        try {
            System.out.println(connectedNode.getObjects(hypercube, kStringSet, 10));
        } catch (NullPointerException e) {
            System.out.println("Nessun oggetto trovato");
        }
        
        //collego il servizio non più con il nodo casuale ma con il nodo che si occupa del set di chiavi
        /*connectedNode = connectedNode.findTargetNode(connectedNode.generateBitSet(kStringSet));
        if (connectedNode != null){
            System.out.println("Ho trovato il nodo: " + connectedNode.getId());
            System.out.println("Spanning binomial tree di: " + connectedNode.getId());
            printTree(connectedNode.createSBT(true), "--");
        }
        else {
            System.out.println("Non è stato possibile collegarsi con il nodo");
        }*/



       /* if(connectedNode.getObjects(kStringSet) != null){
            ArrayList<String> searchedObjects = new ArrayList<String>(connectedNode.getObjects(kStringSet));
                for (String entry : searchedObjects){
                    System.out.println(entry);
                }
        } else {
            System.out.println("La ricerca non ha prodotto risultati");
        }*/
    }

    private static void insert(Hypercube hypercube, Node connectedNode){

        System.out.print("1. INSERT: ");

        Set<String> key = new HashSet<String>(insertKeywords());

        Scanner scan = new Scanner(System.in);
        System.out.println("Inserisci il contenuto dell'oggetto");
        String valueObject  = scan.nextLine();
        connectedNode.addObject(hypercube, key, valueObject);
    }

    private static Set<String> insertKeywords(){
        String decision;
        boolean yn = true;
        Set<String> kStringSet = new HashSet<String>(); 
        Scanner scannerKey = new Scanner(System.in);
        System.out.println("Inserisci la parola chiave");

        while(yn){
            
            //recupero parola chiave inserita dall'utente
            kStringSet.add(scannerKey.nextLine());
            

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
        return kStringSet;
    }

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
            System.out.println("     " + bo + entry.getId());
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
            System.out.print("  " + node.getId());
            }
    }
}
