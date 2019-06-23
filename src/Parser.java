/*
Class used to parse file and extract terms from it.
Also provides method to transform a infix expression to Postfix
 */
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

        // Hash Map for operator precedence
        HashMap<String,Integer> precedence = new HashMap<>();
        precedence.put("~",3);
        precedence.put("&",2);
        precedence.put("|",1);
        precedence.put("(",0);

        // Keep an operator's stack
        Stack<String> opStack = new Stack<>();
        // List for the final postfix notation
        List<String> postfixList = new ArrayList<>();
        //Tokens of infix string notation
        List<String> tokenList;
        tokenList = tokenize(infix);
        // Top of stack for precedence comparison
        String top;

        for (String token: tokenList){
            // Using first character of token ( to save some lines )
            if (Character.isLetterOrDigit(token.charAt(0))){
                postfixList.add(token);
            }
            //Add to stack to keep track of operators
            else if(token.equals("(")){
                opStack.push(token);
            }
            else if(token.equals(")")){
                top = opStack.pop();
                // Pop operators from stack until the corresponding parenthesis
                while (!top.equals("(")){
                    //Add to postfix string
                    postfixList.add(top);
                    top = opStack.pop();
                }
            }
            // Token is operator
            else{
                // While precedence of  Operator's stack top element is bigger than token's
                while(!opStack.isEmpty() && (precedence.get(opStack.peek()) >= precedence.get(token) ) ) {
                    //Add top to postfix string
                    postfixList.add(opStack.pop());
                }
                //Add token (  operator ) to stack
                opStack.push(token);
            }
        }
        while(!opStack.isEmpty()){
            //Add remaining stack elements to postfix
            postfixList.add(opStack.pop());
        }
        //Add spaces between each element of postfixList
        return String.join(" ",postfixList);
    }


}
