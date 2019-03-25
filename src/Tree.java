import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

    // Java program for expression tree
    class Node {

        String value;
        Node left, right;

        Node(String  item) {
            value = item;
            left = right = null;
        }
    }

    class ExpressionTree {

        // A utility function to check if 'c'
        // is an operator

        boolean isOperator(String  c) {
            if (c .equals("&") || c .equals("|")
                    || c .equals("~"))  {
                return true;
            }
            return false;
        }

        // Utility function to do inorder traversal
        void inorder(Node t) {
            if (t != null) {
                inorder(t.left);
                System.out.print(t.value + " ");
                inorder(t.right);
            }
        }
        // Utility function to do postorder traversal
        void postorder(Node t){
            if (t != null) {
                postorder(t.right);
                System.out.print(t.value + " ");
                postorder(t.left);
            }
        }

        void orderlevel(Node root){
            Queue<Node> queue = new LinkedList<>();
            queue.add(root);
            while(!queue.isEmpty()){

                Node temp_node = queue.poll();
                System.out.print(temp_node.value+ " ");
                if(temp_node.left !=null){
                    queue.add(temp_node.left);
                }
                else if(temp_node.right !=null){
                    queue.add(temp_node.right);
                }

            }
        }

        // Returns root of constructed tree for given
        // postfix expression
        Node constructTree(String query) {
            Stack<Node> st = new Stack();
            Node t, t1, t2;

            ArrayList operands = Parser.tokenize(query);

            // Traverse through every character of
            // input expression
            for (Object operand : operands) {
                String token = operand.toString();
                // If operand, simply push into stack
                if (!isOperator(token)) {
                    t = new Node(token);
                    st.push(t);
                } else // operator
                {
                    t = new Node(token);

                    if (token.equals("~")) {
                        t1 = st.pop();
                        t.right = t1;
                        st.push(t);
                        continue;
                    }
                    t1 = st.pop();      // Remove top
                    t2 = st.pop();

                    t.right = t1;
                    t.left = t2;

                    st.push(t);
                }
            }

            t = st.peek();
            st.pop();

            return t;
        }

    }
