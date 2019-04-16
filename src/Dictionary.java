import java.util.*;

class Dictionary implements java.io.Serializable{
;
    private HashMap<String, ArrayList<Posting>> dict;
    private static final long serialVersionUID = 0x1b32faab5902bfa3L;

    Dictionary() {
        dict = new HashMap<>();
    }

    void insert(String word, int docID, float term_freq){
        Posting post = new Posting(docID,term_freq);
        if(dict.containsKey(word)){
            ArrayList<Posting> posting_list = dict.get(word);
            boolean exist = false;
            for(Posting post_cell: posting_list){
                if(post_cell.getDocId()==docID){
                    exist=true;
                }
            }
            if(!exist){
                dict.get(word).add(post);
            }
        }
        else{
            ArrayList<Posting> Posting_list = new ArrayList<>();
            Posting_list.add(post);
            dict.put(word,Posting_list);
        }
    }

    void print(){
        if (dict==null)
            return;
        Set<String> s = dict.keySet();
        for (String word : s) {
            ArrayList<Posting> a = dict.get(word);
            System.out.print(word + "->");
            for (Posting value : a) {
                System.out.print(value.getDocId() + " " + value.getScore() + ", ");
            }
            System.out.println();
        }
    }

    public ArrayList<Posting> get(String word){
        return dict.get(word);
    }

}
