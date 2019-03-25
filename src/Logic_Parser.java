import java.util.ArrayList;
import java.util.Stack;

    class ExpressionParser {

        // A utility function to check if 'c'
        // is an operator
        private boolean isOperator(String c) {
            return c.equals("&") || c.equals("|")
                    || c.equals("~");
        }

        ArrayList<Integer> evaluate_query(String query,Dictionary dict){
            Stack<ArrayList<Integer>> st = new Stack<>();
            ArrayList<Integer> t,term1,term2;
            ArrayList operands = Parser.tokenize(query);

            for(Object operand : operands){
                String token = operand.toString();

                if(!isOperator(token)){
                    st.push(dict.get(token));
                }
                else{
                    if(token.equals("~")){
                        term1 = st.pop();
                        t = Queries.negate(term1);
                        st.push(t);

                    }
                    else if(token.equals("&")) {
                        term1 = st.pop();
                        term2 = st.pop();
                        t = Queries.two_word_and(term1, term2);
                        st.push(t);
                    }
                    else{
                        term1 = st.pop();
                        term2 = st.pop();
                        t = Queries.or(term1, term2);
                        st.push(t);
                    }
                }
            }

            return st.pop();
        }

    }
