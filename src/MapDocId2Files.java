import java.io.Serializable;
import java.util.ArrayList;

public class MapDocId2Files implements Serializable {

    private String [] map;
    private static final long serialVersionUID = 0x1b32faab5902bfa0L;
    MapDocId2Files(int size) {
        map = new String[size];
    }

    public void add(int pos, String filename){
        map[pos] = filename;
    }
    public String get(int pos){
        return map[pos];
    }

    public int length(){
        return map.length;
    }

    void print(ArrayList<Integer> docs){
        if (!docs.isEmpty()) {
            for (int docId : docs) {
                System.out.println(get(docId));
            }
        }
        else System.out.println("No results");
    }
    void print(int[] docs){
        if (docs!=null && docs.length!=0) {
            for (int docId : docs) {
                System.out.println(get(docId));
            }
        }
        else System.out.println("No results");
    }
}
