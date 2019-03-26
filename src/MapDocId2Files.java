import java.io.Serializable;
import java.util.ArrayList;

public class MapDocId2Files implements Serializable {

    String [] map =null;
    private static final long serialVersionUID = 0x1b32faab5902bfa0L;
    public MapDocId2Files(int size) {
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

    public void print(ArrayList<Integer> docs){
        for (int docId : docs){
            System.out.println(get(docId));
        }
    }
}
