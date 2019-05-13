import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
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
            String file="output.html";
            File htmlfile = new File(file);
            StringBuilder sb = new StringBuilder();
            try {
                Files.deleteIfExists(Paths.get("../output.html"));
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                try {
                    for (int docId : docs) {
                        sb.append("<a href=\"");
                        sb.append(get(docId).substring(57).replace("\\","/"));
                        sb.append("\">");
                        sb.append(get(docId));
                        sb.append("</a>");
                        sb.append(System.getProperty("line.separator"));
                    }
                    bw.write("<html><head><title>New Page</title></head><body>" +
                            "<p>"+sb+"</p>" +
                            "</body></html>");
                    bw.close();
                    Desktop.getDesktop().browse(htmlfile.toURI());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else System.out.println("No results");
    }
}
