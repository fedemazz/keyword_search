import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Queue;

public class NodeSBT {
    private ArrayList<NodeSBT> children = null;
    private NodeSBT father = null;
    private String id;
    private BitSet bitset;

    public NodeSBT(String id, BitSet bitset) {
        this.children = new ArrayList<>();
        this.id = id;
        this.bitset = bitset;
    }

    public void addChild(NodeSBT child) {
        child.setFather(this);
        children.add(child);
    }

    
    public void deleteChild(NodeSBT child){
        children.remove(child);
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
    
    public void setFather(NodeSBT father){
        this.father = father;
    }

    public void setId(String id){
        this.id = id;
    }


    public void printTree(NodeSBT root, String bo){ 
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

    public void printTreeBFS(){
        Queue<NodeSBT> queue = new ArrayDeque<NodeSBT>();
        queue.add(this);
        while(!queue.isEmpty()){
            NodeSBT currentNode = queue.remove();
            System.out.println(queue.remove().getId());
            queue.addAll(currentNode.getChildren());
        }
    }


    public Queue<NodeSBT> BFS(){
        Queue<NodeSBT> queue = new ArrayDeque<NodeSBT>();
        Queue<NodeSBT> visited = new ArrayDeque<NodeSBT>();
        queue.addAll(this.getChildren());
        while(!queue.isEmpty()){
            NodeSBT currentNode = queue.remove();
            visited.add(currentNode);
            queue.addAll(currentNode.getChildren());
        }
        return visited;
    }

}