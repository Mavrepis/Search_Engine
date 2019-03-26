import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class Runner {


    public static void main(String[] args) {
        final long startTime = System.currentTimeMillis();
        Parser p = new Parser();

        Dictionary dict = new Dictionary();
        MapDocId2Files map = new MapDocId2Files(0);

        File hash_map = new File("hashmap.ser");
        File map_docId = new File("map.ser");

        if (hash_map.exists() && map_docId.exists() ) {
            System.out.println("Loading dictionary!");
            try {
                FileInputStream fis_hash = new FileInputStream("hashmap.ser");
                ObjectInputStream ois_hash = new ObjectInputStream(fis_hash);
                dict = (Dictionary) ois_hash.readObject();
                ois_hash.close();
                fis_hash.close();
                FileInputStream fis_map = new FileInputStream("map.ser");
                ObjectInputStream ois_map = new ObjectInputStream(fis_map);
                map = (MapDocId2Files) ois_map.readObject();
                fis_map.close();
                ois_map.close();

            }
            catch (IOException ioe) {
                ioe.printStackTrace();
                return;
            } catch (ClassNotFoundException c) {
                System.out.println("Class not found");
                c.printStackTrace();
                return;
            }

        } else {
            try (Stream<Path> walk = Files.walk(Paths.get("C:\\Users\\phili\\IntelliJProjects\\Search_Engine\\data"))) {

                List<String> result = walk.map(Path::toString)
                        .filter(f -> f.endsWith(".txt")).collect(Collectors.toList());
                System.out.println("Creating dictionary!");
                map = new MapDocId2Files(result.size());
                for (String file : result) {
                    ArrayList words = p.parseFile(file);
                    map.add(result.indexOf(file),file);
                    for (Object word : words) {
                        dict.insert(word.toString(), result.indexOf(file));
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {

                FileOutputStream fos = new FileOutputStream("hashmap.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(dict);
                oos.close();
                fos.close();

                FileOutputStream fos_map = new FileOutputStream("map.ser");
                ObjectOutputStream oos_map = new ObjectOutputStream(fos_map);
                oos_map.writeObject(map);
                oos_map.close();
                fos_map.close();
                System.out.println("Serialized HashMap and Map data is saved in hashmap.ser and map.ser");

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        Queries q = new Queries(map);
//        dict.print();
//        //Get word's list of appearances.
//        System.out.println(dict.get("response"));
//
//        ArrayList<Integer> list_first_word = dict.get("all");
//        ArrayList<Integer> list_second_word = dict.get("already");
//        ArrayList<Integer> list_third_word = dict.get("year");
//        ArrayList<Integer> list_fourth_word = dict.get("response");
//
//        // Get a list of common appearances of two words.
//        System.out.println(Queries.intersect(list_first_word, list_second_word));
//
//        //Get a list of common appearances of three words.
//        System.out.println(q.three_word_and(list_first_word, list_second_word,list_third_word));
//
//        //Get a list of union of two words.
//        System.out.println(Queries.or(list_first_word,list_second_word));
//
//        //Negation of a word
//        System.out.println(Queries.negate(list_first_word));
//
//        //Complex query (A^B) V (C ^ D)
//        System.out.println(
//                Queries.or(Queries.intersect(list_first_word,list_second_word),Queries.intersect(list_second_word,list_third_word))
//        );
//        System.out.println(q.n_word_and(list_first_word,list_second_word,list_third_word,list_fourth_word));
        final long endTime = System.currentTimeMillis();
        ExpressionParser et = new ExpressionParser();
        System.out.println("Please enter your query like this: ( A & ~B ) | ( C & D )");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            String query = br.readLine();
            System.out.println(et.evaluate_query(Parser.infix_to_Postfix(query),dict));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Total execution time: " + (endTime - startTime)*0.001 +" seconds");
    }

}
