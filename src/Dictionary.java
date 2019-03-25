import java.lang.reflect.Array;
import java.util.*;

class Dictionary implements java.io.Serializable{
;
    HashMap<String, ArrayList<Integer>> dict;
    private static final long serialVersionUID = 0x1b32faab5902bfa3L;
    Dictionary(){
        dict = new HashMap<>();
    }

    void insert(String word, int docID){
        if(dict.containsKey(word)){
            if(!(dict.get(word).contains((docID)))){
                dict.get(word).add(docID);
            }
        }
        else{
            ArrayList<Integer> docId_list = new ArrayList<>();
            docId_list.add(docID);
            dict.put(word,docId_list);
        }
    }
    void print(){
        if (dict==null)
            return;
        Set<String> s = dict.keySet();
        for (String word : s) {
            ArrayList<Integer> a = dict.get(word);
            System.out.print(word + "->");
            for (Integer integer : a) {
                System.out.print(integer + " ");
            }
            System.out.println();
        }
    }

    public ArrayList<Integer> get(String word){
        return dict.get(word);
    }

}
