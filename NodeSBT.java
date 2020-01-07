import java.util.ArrayList;
import java.util.BitSet;

public class NodeSBT {
    private ArrayList<NodeSBT> children = null;
    private NodeSBT father = null;
    private String id;
    private BitSet bitset;
    private boolean finished;

    public NodeSBT(String id, BitSet bitset) {
        this.children = new ArrayList<>();
        this.id = id;
        this.bitset = bitset;
        this.finished = false;
    }

    public void addChild(NodeSBT child) {
        children.add(child);
    }

    public NodeSBT getFather(){
        return father;
    }

    public ArrayList<NodeSBT> getChildren(){
        return children;
    }

    public BitSet getBS(){
        return bitset;
    }

    public String getId(){
        return id;
    }

    public Boolean getFinished(){
        return finished;
    }
    
    public void setFather(NodeSBT father){
        this.father = father;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setFinished(){
        this.finished = true;
    }
}