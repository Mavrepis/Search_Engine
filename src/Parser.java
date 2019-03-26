import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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

    static String infix_to_Postfix(String infix){
        HashMap<String,Integer> precedence = new HashMap<>();
        precedence.put("~",3);
        precedence.put("&",2);
        precedence.put("|",1);
        precedence.put("(",0);

        Stack<String> opStack = new Stack<>();
        List<String> postfixList = new ArrayList<>();
        List<String> tokenList;
        tokenList = tokenize(infix);
        String top;

        for (String token: tokenList){
            if (Character.isLetterOrDigit(token.charAt(0))){
                postfixList.add(token);
            }
            else if(token.equals("(")){
                opStack.push(token);
            }
            else if(token.equals(")")){
                top = opStack.pop();
                while (top.equals("(")){
                    postfixList.add(top);
                    top = opStack.pop();
                }
            }
            else{
                while(!opStack.isEmpty() && (precedence.get(opStack.peek()) >= precedence.get(token) ) ) {
                    postfixList.add(opStack.pop());
                }
                opStack.push(token);
            }
        }
        while(!opStack.isEmpty()){
            postfixList.add(opStack.pop());
        }
        return String.join(" ",postfixList);
    }


}
