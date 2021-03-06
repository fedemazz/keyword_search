import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class service {

    public static void main(String[]args){
        
        /*System.out.println("Inserisci le dimensione dell'ipercubo:");
        Scanner scanner = new Scanner(System. in);
        int r=scanner.nextInt();*/

        System.out.println("Creo un ipercubo a 8 dimensioni");
        System.out.println("Le parole chiave disponibili sono: { a, b, c, d, e, f, g, h }");
        int r = 8;

        //creo l'ipercubo di r-dimensioni
        Hypercube hypercube = new Hypercube(r);

        //Simulo la connessione ad un nodo dell'ipercubo
        Node connectedNode = new Node();
        connectedNode = hypercube.getNode(randomNode(r));

        printLog(hypercube, connectedNode);
        loadCsv(hypercube, connectedNode);
        String choice;

        do{

            System.out.println("\n****MENU****");
			System.out.println("1. INSERT");
			System.out.println("2. SUPERSET SEARCH");
			System.out.println("3. PIN SEARCH");
			System.out.println("4. DELETE");
            System.out.println("5. EXIT");
            System.out.print("Fai una scelta (1, 2, 3, 4, 5): ");
            Scanner scan = new Scanner(System.in);
            choice = scan.nextLine();

            switch(choice){


                case "1": insert(hypercube, connectedNode);
                break;

                case "2": search(hypercube, connectedNode, r);
                break;

                case "3": pinSearch(hypercube, connectedNode, r);
                break;

                case "4":	//Exit
                break;

                case "5":	//Exit

					System.out.println("EXIT");
					break;

				default:
					break;

            }

        } while(!(choice.equals("5")));
        

    }

    private static void search(Hypercube hypercube, Node connectedNode, int r) {

        System.out.print("2. SUPERSET SEARCH: ");

        //set utilizzato per rappresentare il set di keyword
        Set<String> kStringSet = new HashSet<String>(); 
        //variabile per log
        String targetNodeId;
        ArrayList<String> idObjects;
        //chiedo le keyword da inserire all'utente
        kStringSet = insertKeywords();
        
        //visualizzo a schermo le keyword inserite
        System.out.print("Il set di keyword e': " + kStringSet);

        //visualizzo a schermo il nodo che si occupa della keyword
        targetNodeId = getStringSearched(connectedNode.generateBitSet(kStringSet), r);
        System.out.println("\nNodo che si occupa della keyword: " + targetNodeId);
        System.out.println("Cerco il nodo: " + targetNodeId);

        try {
            idObjects = new ArrayList<String>(connectedNode.requestObjects(kStringSet, 100));
            System.out.println(connectedNode.getObjects(hypercube, idObjects));
        } catch (NullPointerException e) {
            System.out.println("Nessun oggetto trovato");
        }
    }

    private static void pinSearch(Hypercube hypercube, Node connectedNode, int r){
        //set utilizzato per rappresentare il set di keyword
        Set<String> kStringSet = new HashSet<String>(); 
        //variabile per log
        String targetNodeId;
        ArrayList<String> idObjects;
        //chiedo le keyword da inserire all'utente
        kStringSet = insertKeywords();
        
        //visualizzo a schermo le keyword inserite
        System.out.print("Il set di keyword e': " + kStringSet);

        //visualizzo a schermo il nodo che si occupa della keyword
        targetNodeId = getStringSearched(connectedNode.generateBitSet(kStringSet), r);
        System.out.println("\nNodo che si occupa della keyword: " + targetNodeId);
        System.out.println("Cerco il nodo: " + targetNodeId);

        try {
            idObjects = new ArrayList<String>(connectedNode.requestObjects(kStringSet));
            System.out.println(connectedNode.getObjects(hypercube, idObjects));
        } catch (NullPointerException e) {
            System.out.println("Nessun oggetto trovato");
        }
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

    private static void loadCsv(Hypercube hypercube, Node connectedNode){
        String fileName = "script/test.csv";
        File file = new File(fileName);
        try{
            Scanner inputStream = new Scanner(file);
            while(inputStream.hasNext()){
                String data = inputStream.next();
                String [] values = data.split(",");
                Set<String> keySet = new HashSet<String>();
                for(String key : values[1].split("")){
                    keySet.add(key);
                }
                connectedNode.addObject(hypercube, keySet, values[0]);
            }
            inputStream.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
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

        System.out.println("Connesso al nodo: " + connectedNode.getId() + "\nI suoi neighbors sono:"); 
        for (Node node : connectedNode.getNeighbors()){
            System.out.print("  " + node.getId());
            }
    }
}
