/*
        This implementation of  a basic search engine supports boolean retrieval based on term,
        retrieval using a logical expression such as ( A & B ) | ( C & D) (using custom expression parser)
        and ranked search of queries using cosine similarity measure.

        The scoring for each term in ranked search is 1+log(tf)*log(N/df) and Length normalization is used,
        where length of a document is defined as  √(∑(1+log(tf))^2 * log(N/df)^2 )
*/

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Runner {

    @SuppressWarnings("SuspiciousMethodCalls")
    public static void main(String[] args) {
        final long startTime = System.nanoTime();
        Parser p = new Parser();
        // Instance of Inverted index
        Dictionary dict = new Dictionary();
        // Mapping of Document ID's to Files
        MapDocId2Files map = new MapDocId2Files(0);
        // Used to keep document's length  √(∑(1+log(tf))^2 * log(N/df)^2 )
        HashMap<Integer, Float> doc_length = new HashMap<>();
        // Used to keep document frequencies   { d ∈ D : t ∈ d }
        HashMap<String, Float> df = new HashMap<>();

        // Paths to serializable files for all structures needed
        File hash_map = new File("dictionary.ser");
        File map_docId = new File("map.ser");
        File doc_lengths = new File("doc_lengths.ser");
        File doc_frequencies = new File("doc_frequencies.ser");

        // If everything  needed exists
        if (hash_map.exists() && map_docId.exists() && doc_lengths.exists() && doc_frequencies.exists()) {
            System.out.println("Loading dictionary!");
            try {
                FileInputStream fis_hash = new FileInputStream("dictionary.ser");
                ObjectInputStream ois_hash = new ObjectInputStream(fis_hash);
                dict = (Dictionary) ois_hash.readObject();
                ois_hash.close();
                fis_hash.close();

                FileInputStream fis_map = new FileInputStream("map.ser");
                ObjectInputStream ois_map = new ObjectInputStream(fis_map);
                map = (MapDocId2Files) ois_map.readObject();
                fis_map.close();
                ois_map.close();

                FileInputStream fis_doc_length = new FileInputStream("doc_lengths.ser");
                ObjectInputStream ois_doc_length = new ObjectInputStream(fis_doc_length);
                doc_length = (HashMap<Integer, Float>) ois_doc_length.readObject();
                fis_doc_length.close();
                ois_doc_length.close();

                FileInputStream fis_doc_freq = new FileInputStream("doc_frequencies.ser");
                ObjectInputStream ois_doc_freq = new ObjectInputStream(fis_doc_freq);
                df = (HashMap<String, Float>) ois_doc_freq.readObject();
                fis_doc_freq.close();
                ois_doc_freq.close();

            } catch (IOException ioe) {
                ioe.printStackTrace();
                return;
            } catch (ClassNotFoundException c) {
                System.out.println("Class not found");
                c.printStackTrace();
                return;
            }

        } else {
            // Change this to your own path to data folder
            try (Stream<Path> walk = Files.walk(Paths.get("C:\\Users\\phili\\IntelliJProjects\\Search_Engine\\data"))) {

                // Lists all .txt files inside a folder
                List<String> result = walk.map(Path::toString)
                        .filter(f -> f.endsWith(".txt")).collect(Collectors.toList());
                System.out.println("Creating dictionary!");
                map = new MapDocId2Files(result.size());

                // Intermediate HashMap, used for document's length calculation
                HashMap<Integer, HashMap<String, Integer>> term_squared = new HashMap<>();

                // Iteratively parse fetched files
                for (String path : result) {

                    ArrayList words = p.parseFile(path);

                    map.add(result.indexOf(path), path);

                    HashMap<String, Integer> term_frequencies = new HashMap<>();
                    // Create frequency HashMap for each term
                    for (Object word : words) {

                        if (!term_frequencies.containsKey(word)) {

                            term_frequencies.put(word.toString(), 1);

                            // Update document frequency HashMap
                            if (df.containsKey(word.toString())) {
                                df.put(word.toString(), df.get(word.toString()) + 1);
                                continue;
                            }
                            df.put(word.toString(), 1.0f);
                        } else {
                            //noinspection SuspiciousMethodCalls
                            term_frequencies.put(word.toString(), term_frequencies.get(word) + 1);
                        }
                    }
                    term_squared.put(result.indexOf(path), term_frequencies);

                    /*
                    Insert to inverted index (dictionary) with score
                    for each posting its term frequency, needs to be
                    done here because document frequency HashMap
                    is not yet complete and final score is calculated as
                    score = 1+log(tf) * log(N/df)
                     */

                    for (Map.Entry<String, Integer> entry : term_frequencies.entrySet()) {
                        String word = entry.getKey();
                        float value = entry.getValue();
                        // term_frequency = 1 + log(tf)
                        value = (float) ((float) 1 + Math.log(value));
                        dict.insert(word, result.indexOf(path), value);
                    }
                }

                for (Map.Entry<Integer, HashMap<String, Integer>> entry : term_squared.entrySet()) {

                    int docId = entry.getKey();
                    HashMap<String, Integer> document_terms_freq = entry.getValue();
                    // Sum is initialised here (document scope)
                    float sum = 0;
                    for (Map.Entry<String, Integer> inner_hash_entry : document_terms_freq.entrySet()) {
                        String term = inner_hash_entry.getKey();
                        Integer tf = inner_hash_entry.getValue();
                        // sum = ∑(1+log(tf))^2 * log(N/df)^2 )
                        sum += Math.pow((1 + Math.log(tf)), 2) * Math.pow(Math.log(8000 / df.get(term)), 2);
                    }
                    // Add to document length HashMap
                    doc_length.put(docId, (float) Math.pow(sum, 0.5));
                }

                /* Block to update inverted index using information
                from document frequency HashMap
                 */

                // Entry -> Term's document frequency
                for (Map.Entry<String, Float> entry : df.entrySet()) {
                    // Entry_posting -> List with term's appearance (docID,score)
                    ArrayList<Posting> entry_posting = dict.get(entry.getKey());
                    for (Posting posting : entry_posting) {
                        float current_score = posting.getScore();
                        // Change each posting score to its final score
                        posting.setScore(current_score * (float) Math.log(8000 / entry.getValue()));
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {

                FileOutputStream fos = new FileOutputStream("dictionary.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(dict);
                oos.close();
                fos.close();

                FileOutputStream fos_map = new FileOutputStream("map.ser");
                ObjectOutputStream oos_map = new ObjectOutputStream(fos_map);
                oos_map.writeObject(map);
                oos_map.close();
                fos_map.close();

                FileOutputStream fos_doc_length = new FileOutputStream("doc_lengths.ser");
                ObjectOutputStream oos_doc_length = new ObjectOutputStream(fos_doc_length);
                oos_doc_length.writeObject(doc_length);
                oos_doc_length.close();
                fos_doc_length.close();

                FileOutputStream fos_doc_freq = new FileOutputStream("doc_frequencies.ser");
                ObjectOutputStream oos_doc_freq = new ObjectOutputStream(fos_doc_freq);
                oos_doc_freq.writeObject(df);
                oos_doc_freq.close();
                fos_doc_freq.close();

                System.out.println("HashMap Serialization has been completed.");

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
/*        Boolean retrieval
//      Get word's list of appearances.
        System.out.println(dict.get("response"));
        Queries q = new Queries(map);

//      Fetch posting lists for four words
        ArrayList<Posting> list_first_word = dict.get("world");
        ArrayList<Posting> list_second_word = dict.get("quota");
        ArrayList<Posting> list_third_word = dict.get("year");
        ArrayList<Posting> list_fourth_word = dict.get("response");

//      Get a list of common appearances of two words.
        map.print(Queries.intersect_postings(list_first_word, list_second_word));

//        Get a list of union of two words.
        map.print(Queries.or_posting(list_first_word, list_second_word));

//        Negation of a word
        System.out.println(Queries.negate_posting(list_first_word));

        //       Complex query (A^B) V (C ^ D)
        System.out.println(
                Queries.or_integer(
                        Queries.intersect_postings(list_first_word, list_second_word),
                        Queries.intersect_postings(list_second_word, list_third_word)
                )
        );
        System.out.println(q.or_posting(list_first_word, list_second_word, list_third_word, list_fourth_word));
        for (Map.Entry entry : doc_length.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        ExpressionParser ep = new ExpressionParser();
        System.out.println("Please enter your query like this: ( A & ~ B ) | ( C & D )");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            String query = br.readLine().toLowerCase();
            map.print(ep.evaluate_query(Parser.infix_to_Postfix(query), dict));
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

// Ranked Search
        System.out.println("Type your search terms: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            String query = br.readLine().toLowerCase();
            ArrayList<String> terms = Parser.tokenize(query);
            final long q_timestart = System.nanoTime();
            map.print(Queries.cosine_score(terms, doc_length, dict, df));
            final long q_timestop = System.nanoTime();
            System.out.println("Query time: " + (q_timestop - q_timestart) * 0.000000001 + " seconds");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final long endTime = System.nanoTime();
        System.out.println("Total execution time: " + (endTime - startTime) * 0.000000001 + " seconds");
    }

}
