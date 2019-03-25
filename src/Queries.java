import java.util.*;

class Queries {
    Set<Integer> doc_ID = new HashSet<>();
    public Queries(MapDocId2Files map){
        for (int i=0; i<map.length(); i++){
            doc_ID.add(i);
        }
    }

    ArrayList<Integer> two_word_and(ArrayList<Integer> word1, ArrayList<Integer> word2) {

        ArrayList<Integer> answer = new ArrayList<>();

        ListIterator<Integer> word1_iter = word1.listIterator();
        ListIterator<Integer> word2_iter = word2.listIterator();

        while (word1_iter.hasNext() && word2_iter.hasNext()) {

            int current_docId1 = word1.get(word1_iter.nextIndex());
            int current_docId2 = word2.get(word2_iter.nextIndex());

            if (current_docId1 == current_docId2) {
                answer.add(word1_iter.next());
                word2_iter.next();
            } else if (current_docId1 < current_docId2) {
                word1_iter.next();
            } else {
                word2_iter.next();
            }
        }
        if (answer.isEmpty()) {
            System.out.print("No results");
        }
        return answer;
    }

    ArrayList<Integer> three_word_and(ArrayList<Integer> word1, ArrayList<Integer> word2, ArrayList<Integer> word3) {
        ArrayList<Integer> answer = new ArrayList<>();
        int a = word1.size();
        int b = word2.size();
        int c = word3.size();
        if (a == Math.min(Math.min(a, b), c) && (b == Math.min(b, c))) // a is min, b smaller than c
            answer = two_word_and(two_word_and(word1, word2), word3);
        else if (a == Math.min(Math.min(a, b), c) && (c == Math.min(b, c))) //a is min, c smaller than b
            answer = two_word_and(two_word_and(word1, word3), word2);
        else if (b == Math.min(Math.min(a, b), c) && (a == Math.min(a, c))) //b is min, a smaller than c
            answer = two_word_and(two_word_and(word2, word1), word3);
        else if (b == Math.min(Math.min(a, b), c) && (c == Math.min(a, c))) //b is min, c smaller than a
            answer = two_word_and(two_word_and(word2, word3), word1);
        else if (c == Math.min(Math.min(a, b), c) && (a == Math.min(a, b))) //c is min, a smaller than b
            answer = two_word_and(two_word_and(word3, word1), word2);
        else if (c == Math.min(Math.min(a, b), c) && (b == Math.min(a, b))) //c is min, b smaller than a
            answer = two_word_and(two_word_and(word3, word2), word1);
        return answer;
    }

    @SafeVarargs
    final ArrayList<Integer> n_word_and( ArrayList<Integer>... words){
        List<ArrayList<Integer>> word_list = new ArrayList<>(Arrays.asList(words));
        Set<Integer> answer = new HashSet<>(doc_ID);
        word_list.sort(Comparator.comparingInt(ArrayList::size));
        for(ArrayList<Integer> word : word_list ){
            answer.removeAll(negate(word));
        }
        return new ArrayList<>(answer);
    }

    @SafeVarargs
    final ArrayList<Integer> or(ArrayList<Integer>... words){
        Set<Integer> answer = new HashSet<>();
        for(ArrayList<Integer> word:words){
            answer.addAll(word);
        }
        return new ArrayList<>(answer);
    }

    ArrayList<Integer> negate(ArrayList<Integer> word){
        Set<Integer> answer = new HashSet<>(doc_ID);
        answer.removeAll(word);
        return new ArrayList<>(answer);
    }
}
