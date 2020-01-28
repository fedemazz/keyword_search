public class TritSet {

    private String value;
    private int r;

    public TritSet(){
    }

    public TritSet(int n, int r){
        this.value = normalize(createTrits(n),r);
    }

    public TritSet(TritSet ts, int r){
        this.value = normalize(ts.getValue(),r);
    }

    public TritSet(String ts, int r){
        this.value = normalize(ts,r);
    }
    

    public String getValue(){
        return value;
    }

    private String createTrits(int n){
        //String character = Integer.toString(n % 3);
        if (n < 3) {
            return Integer.toString(n % 3);
        } else return createTrits(n/3) + Integer.toString(n % 3);
    }

    private String normalize(String trits, int r){
        while (trits.length() < r){
            trits = "0" + trits;
        }
        return trits;
    }

    public boolean differOneTrit(TritSet toCompare) { 
        // Find lengths of given strings 
    int m = this.value.length(), n = toCompare.value.length(); 
  
    int count = 0; // Count of edits 
  
    int i = 0, j = 0; 
    while (i < m && j < n) { 
        if (((Character.getNumericValue(this.value.charAt(i)) - Character.getNumericValue(toCompare.value.charAt(j)) == 0))) { 
                i++; 
                j++; 
        }  else if((Character.getNumericValue(this.value.charAt(i)) - Character.getNumericValue(toCompare.value.charAt(j)) == 1) || ((Character.getNumericValue(this.value.charAt(i)) - Character.getNumericValue(toCompare.value.charAt(j)) == -1 ))){ 
            i++; 
            j++; 
            count++;
        } else return false; 
    }   

    if (count == 1){
    return  true;
    } else return false;
    }

    public boolean differOneTrit(String toCompare) { 
        // Find lengths of given strings 
    int m = this.value.length(), n = toCompare.length(); 
  
    int count = 0; // Count of edits 
  
    int i = 0, j = 0; 
    while (i < m && j < n) { 
        if (((Character.getNumericValue(this.value.charAt(i)) - Character.getNumericValue(toCompare.charAt(j)) == 0))) { 
                i++; 
                j++; 
        }  else if((Character.getNumericValue(this.value.charAt(i)) - Character.getNumericValue(toCompare.charAt(j)) == 1) || ((Character.getNumericValue(this.value.charAt(i)) - Character.getNumericValue(toCompare.charAt(j)) == -1 ))){ 
            i++; 
            j++; 
            count++;
        } else return false; 
    }   

    if (count == 1){
    return  true;
    } else return false;
    }

    //controllo se il primo bitset è contenuto nel secondo
    //01100 e 01101. il primo è contenuto nel secondo
    public boolean isIncludedIn(TritSet toCompare){
        int i = 0;
        while(i < this.getValue().length() && i < toCompare.getValue().length()){
            if (Character.getNumericValue(toCompare.value.charAt(i)) < Character.getNumericValue(this.value.charAt(i))){
                return false;
            }
            i++;
        }
        return true;
    }

    public boolean equals(TritSet toCompare){
        int i = 0;
        while(i < this.getValue().length() && i < toCompare.getValue().length()){
            if (Character.getNumericValue(toCompare.value.charAt(i)) != Character.getNumericValue(this.value.charAt(i))){
                return false;
            }
            i++;
        }
        return true;
    }

    public TritSet xor(TritSet toCompare){
        int i=0;
        String kSetString = "";
        for (int j = 0; j < toCompare.getValue().length(); j++){
            kSetString = kSetString +"0";
        }
        StringBuilder result = new StringBuilder(kSetString);
        while(i < this.getValue().length() && i < toCompare.getValue().length()) {
            if(Character.getNumericValue(toCompare.value.charAt(i)) ==  Character.getNumericValue(this.value.charAt(i))){
                result.setCharAt(i, '0'); 
            }   else {        
                    if(Math.abs(Character.getNumericValue(toCompare.value.charAt(i)) - Character.getNumericValue(this.value.charAt(i))) == 1) {
                        result.setCharAt(i, '1');
                    } else if(Math.abs(Character.getNumericValue(toCompare.value.charAt(i)) - Character.getNumericValue(this.value.charAt(i))) == 2){
                        result.setCharAt(i, '2');
                    }
                }
                i++;
            }
        return new TritSet(result.toString(),this.r);
    }

    public int cardinality(){
        int i = 0;
        int card = 0;
        while(i < this.getValue().length()) {
            if(Character.getNumericValue(this.value.charAt(i)) == 1 || Character.getNumericValue(this.value.charAt(i)) == 2){
                card ++;
            }
            i++;
        }
            return card;
    }

    public int nextSetTrit(int pos){
        int i = this.value.length()- 1 - pos;
        while (i>=0) {
            if(Character.getNumericValue(this.value.charAt(i)) == 1 || Character.getNumericValue(this.value.charAt(i)) == 2){
                return Math.abs(i - (this.value.length() - 1));
            }
           i--;
        }
        return -1;
    }

    
        
        public static void main (String[]args) {

            /* solo per test
            for (int i = 0; i < Math.pow(3,4); i++){
            TritSet trits1 = new TritSet(i, 4);
            System.out.println(trits1.getValue());
            }

            System.out.println("aaaaaaaaaaaaaaa");
            
            TritSet trits2 = new TritSet(5,4);
            TritSet trits3 = new TritSet(12,4);
            System.out.println(trits2.getValue());
            System.out.println(trits3.getValue());
            System.out.println(trits2.isIncludedIn(trits3));

            System.out.println("Prova xor");
            System.out.println(trits2.getValue());
            System.out.println(trits3.getValue());
            System.out.println(trits2.xor(trits3).getValue());
            

            System.out.println("Prova cardinality");
            System.out.println(new TritSet(0,4).getValue());
            System.out.println(new TritSet(0,4).cardinality());
            
            System.out.println(new TritSet(2,4).getValue());
            System.out.println(new TritSet(2,4).cardinality());

            System.out.println(new TritSet(4,4).getValue());
            System.out.println(new TritSet(4,4).cardinality());

            
            System.out.println(new TritSet(17,4).getValue());
            System.out.println(new TritSet(17,4).cardinality());
            System.out.println("prova nextSetTrit");
            System.out.println(new TritSet("0010",4).nextSetTrit(0));
            System.out.println(new TritSet("0011",4).nextSetTrit(2));
            System.out.println(new TritSet("0020",4).nextSetTrit(1));
            System.out.println(new TritSet("0120",4).nextSetTrit(3));
            System.out.println(new TritSet("1020",4).nextSetTrit(0));
            System.out.println(new TritSet("1000",4).nextSetTrit(0));
            System.out.println(new TritSet("0100",4).nextSetTrit(0));
            System.out.println(new TritSet("2000",4).nextSetTrit(0));
            System.out.println(new TritSet("0200",4).nextSetTrit(0));*/

        }
}