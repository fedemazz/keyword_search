import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Queue;

public class test2 {
    public static void main(String[]args){

        for (int i = 0; i < Math.pow(2, 3); i++){
            System.out.println(createBinaryID(i));
        }

        NodeSBT root = new NodeSBT(createBinaryID(0), new BitSet(3));
        
        BitSet bs1 = new BitSet();
        bs1.set(3);
        BitSet bs2 = new BitSet();
        bs1.set(3);

        
        
        Queue<BitSet> queue = new ArrayDeque<BitSet>();

        queue.add(bs1);
        queue.add(bs2);
    }

    private static String createBinaryID(int n){
        //passo il numero del nodo per identificare il suo codice binario
        String idString = Integer.toBinaryString(n);
        //se il codice binario del nodo ottenuto non è della lunghezza r (dimensione dell'ipercubo) allora aggiungo gli zeri necessari
        while (idString.length() < 3){
            idString = "0" + idString;
        }
        return idString;
    }
}