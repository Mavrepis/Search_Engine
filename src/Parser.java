import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

class Parser {

    ArrayList parseFile(String file){
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader r = new BufferedReader(new FileReader(file));
            String line;
            while ((line=r.readLine()) != null){
                builder.append(line.toLowerCase()).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tokenize(builder.toString());
    }

    static ArrayList tokenize(String text){
        StringTokenizer st = new StringTokenizer(text," .!?,\t\n;()[]{}-\"123456789'/0ό");
        String Token;
        ArrayList<String> token_list = new ArrayList<>();
        while(st.hasMoreTokens()){
            Token = st.nextToken();
            token_list.add(Token);
        }
        return token_list;
    }


}
