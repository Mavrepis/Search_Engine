import java.util.ArrayList;
import java.util.Stack;

//    class ExpressionParser {
//
//        private boolean isOperator(String c) {
//            return c.equals("&") || c.equals("|") || c.equals("~");
//        }
//
//        ArrayList<Integer> evaluate_query(String query,Dictionary dict){
//
//            //Stack of lists of words appearances
//            Stack<ArrayList<Integer>> st = new Stack<>();
//            ArrayList<Integer> t,term1,term2;
//            ArrayList operands = Parser.tokenize(query);
//
//            for(Object operand : operands){
//
//                String token = operand.toString();
//                if(!isOperator(token)){
//                    //Add word's list of appearances to stack
//                    st.push(dict.get(token));
//                }
//                else{
//                    if(token.equals("~")){
//                        //Negation applies only to one operand
//                        term1 = st.pop();
//                        t = Queries.negate(term1);
//                        //Add result to stack
//                        st.push(t);
//                    }
//                    else if(token.equals("&")) {
//                        //Pop first two elements of stack
//                        term1 = st.pop();
//                        term2 = st.pop();
//                        //Perform intersection algorithm
//                        t = Queries.intersect(term1, term2);
//                        //Add result to stack
//                        st.push(t);
//                    }
//                    else{ // Operator is | (OR)
//                        term1 = st.pop();
//                        term2 = st.pop();
//                        //Perform or algorithm
//                        t = Queries.or(term1, term2);
//                        //Add result to stack
//                        st.push(t);
//                    }
//                }
//            }
//            //Last element in stack is the query's result
//            return st.pop();
//        }
//
//    }
