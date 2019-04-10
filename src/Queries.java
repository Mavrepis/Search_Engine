import java.util.*;

class Queries {
    private static Set<Integer> doc_ID = new HashSet<>();
    Queries(MapDocId2Files map){
        for (int i=0; i<map.length(); i++){
            doc_ID.add(i);
        }
    }

    static ArrayList<Integer> intersect_postings(ArrayList<Posting> word1, ArrayList<Posting> word2) {
        ArrayList<Integer> answer = new ArrayList<>();
        ArrayList<Integer> word1_docID = new ArrayList<>();
        ArrayList<Integer> word2_docID = new ArrayList<>();
        for(Posting cell: word1){
            word1_docID.add(cell.getDocId());
        }
        for(Posting cell: word2){
            word2_docID.add(cell.getDocId());
        }
        return getIntegers(word1_docID, word2_docID, answer);
    }
    static ArrayList<Integer> intersect_integers(ArrayList<Integer> word1, ArrayList<Integer> word2) {
        ArrayList<Integer> answer = new ArrayList<>();
        return getIntegers(word1, word2, answer);
    }

    private static ArrayList<Integer> getIntegers(ArrayList<Integer> word1, ArrayList<Integer> word2, ArrayList<Integer> answer) {
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
        return answer;
    }

    @SafeVarargs
//    static ArrayList<Integer> or_posting(ArrayList<Posting>... words){
//        Set<Integer> answer = new HashSet<>();
//        for(ArrayList<Posting> postings :words){
//            for(Posting posting: postings){
//                answer.add(posting.getDocId());
//            }
//        }
//        return new ArrayList<>(answer);
//    }
    static ArrayList<Integer> or_integer(ArrayList<Integer>... words){
        Set<Integer> answer = new HashSet<>();
        for(ArrayList<Integer> word:words){
            answer.addAll(word);
        }
        return new ArrayList<>(answer);
    }

//    static ArrayList<Integer> negate_posting(ArrayList<Posting> word){
//        Set<Integer> answer = new HashSet<>(doc_ID);
//        for(Posting posting:word){
//            answer.remove(posting.getDocId());
//        }
//        return new ArrayList<>(answer);
//    }
    static ArrayList<Integer> negate_integer(ArrayList<Integer> word){
        Set<Integer> answer = new HashSet<>(doc_ID);
        answer.removeAll(word);
        return new ArrayList<>(answer);
    }
}
