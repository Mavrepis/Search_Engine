/*
Class used to support all needed queries.
Most of the methods have their overloaded counterpart for compatibility reasons
 */
import java.util.*;
import java.util.stream.IntStream;

import static java.util.Comparator.comparingDouble;

class Queries {

    private static Set<Integer> doc_ID = new HashSet<>();

    Queries(MapDocId2Files map) {
        for (int i = 0; i < map.length(); i++) {
            doc_ID.add(i);
        }
    }
// Support the Intersection between two Posting lists
    static ArrayList<Integer> intersect_postings(ArrayList<Posting> word1, ArrayList<Posting> word2) {
        ArrayList<Integer> answer = new ArrayList<>();
        ArrayList<Integer> word1_docID = new ArrayList<>();
        ArrayList<Integer> word2_docID = new ArrayList<>();
        // Keep only the docID information from each list
        for (Posting cell : word1) {
            word1_docID.add(cell.getDocId());
        }
        for (Posting cell : word2) {
            word2_docID.add(cell.getDocId());
        }
        return Intersect(word1_docID, word2_docID, answer);
    }
// Support the Intersection between two Integer lists
    static ArrayList<Integer> intersect_integers(ArrayList<Integer> word1, ArrayList<Integer> word2) {
        ArrayList<Integer> answer = new ArrayList<>();
        return Intersect(word1, word2, answer);
    }
// Extracted method from Intersect methods.
    private static ArrayList<Integer> Intersect(ArrayList<Integer> word1, ArrayList<Integer> word2, ArrayList<Integer> answer) {
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
// Support the Union multiple Posting lists
    static ArrayList<Integer> or_posting(ArrayList<Posting>... words) {
        Set<Integer> answer = new HashSet<>();
        for (ArrayList<Posting> postings : words) {
            for (Posting posting : postings) {
                answer.add(posting.getDocId());
            }
        }
        return new ArrayList<>(answer);
    }

// Support the Union multiple Integer lists
    static ArrayList<Integer> or_integer(ArrayList<Integer>... words) {
        Set<Integer> answer = new HashSet<>();
        for (ArrayList<Integer> word : words) {
            answer.addAll(word);
        }
        return new ArrayList<>(answer);
    }

// Support the Negation of a Posting list
    static ArrayList<Integer> negate_posting(ArrayList<Posting> word) {
        Set<Integer> answer = new HashSet<>(doc_ID);
        for (Posting posting : word) {
            answer.remove(posting.getDocId());
        }
        return new ArrayList<>(answer);
    }
// Support the Negation of a Integer list
    static ArrayList<Integer> negate_integer(ArrayList<Integer> word) {
        Set<Integer> answer = new HashSet<>(doc_ID);
        answer.removeAll(word);
        return new ArrayList<>(answer);
    }

// Calculates cosine similarity between a query and dictionary's documents
    static int[] cosine_score(ArrayList<String> query, HashMap<Integer, Float> Length,
                              Dictionary dict, HashMap<String, Float> df) {
        //Cumulative score array for each document
        float[] Scores = new float[Length.size()];

        for (String term : query) {
            //Calculate term's weight = ( log(N/df) )
            if (df.get(term) != null) {
                float weight_term_query = (float) Math.log(8000 / df.get(term));
                //Fetch term's inverted dictionary list
                ArrayList<Posting> term_postings = dict.get(term);
                for (Posting post : term_postings) {
                    //Update scores
                    Scores[post.getDocId()] += weight_term_query * post.getScore();
                }
            } else {
                return null;
            }
        }
        for (int i = 0; i < Scores.length; i++) {
            //Normalise scores array by dividing with document's length
            Scores[i] = Scores[i] / Length.get(i);
        }
        return TopN(Scores, 5);

    }
// Return the N results in descending order of score
    public static int[] TopN(final float[] input, final int n) {
        return IntStream.range(0, input.length)
                .boxed()
                //Sort elements by descending score
                .sorted(comparingDouble(i -> input[(int) i]).reversed())
                // "Create" IntStream based on input's sorted indexes
                .mapToInt(i -> i)
                //Keep n elements of IntStream
                .limit(n)
                .toArray();
    }
}
