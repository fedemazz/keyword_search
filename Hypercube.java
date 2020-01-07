import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Hypercube {
    private int r; //le dimensioni dell'ipercubo
    
    private Map <String, Node> nodes = new HashMap<>();

    public Hypercube(int r) {
        this.r = r;
        double nNode = Math.pow(2,r);

        //devo creare tanti nodi quanto è grande nNode 
        for (int i = 0; i < nNode; i++){
            Node node = new Node(i,r);
            nodes.put (node.getId(), node);
       }

       for (Map.Entry<String, Node> entry : nodes.entrySet()) {
           entry.getValue().setNeighbors(nodes);
       }
    }

    public int getR(){
        return r;
    }

    public Map <String, Node> getNodes(){
        return nodes;
    }

    public Set<String> getNodeList(){
        return nodes.keySet();
    }
    
    public Node getNode(String idNode) {
        return nodes.get(idNode);
    }

    public boolean hasNode(String idNode) {
        return nodes.containsKey(idNode);
    }

    public Node connectTo(String idNode) {
        return nodes.get(idNode);
    }
}
