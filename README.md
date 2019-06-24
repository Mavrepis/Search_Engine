# Search Engine
Basic search engine university project. This was created in the context of "Information Retrieval" lesson.

Special _thank you_ to my professor **Christos Doulkeridis** who guided me through the process of creating this.

### Functionality

This engine supports the following:
- [x] [Existence queries](#existence-queries)
- [x] [Boolean Expression queries](#boolean-expression-queries)
- [x] [Ranked search](#ranked-search)

### Details

The collection of texts used for demonstration purposes is included in the folder _"/data"_ and is a subset of much larger
Reuters news collection.
Furthermore, for the creation of the inverted index, document frequency  and document length structures, HashMap has been used
as 
it supports _insertion_ and _retrieval_ with __O(1)__ time and we do not care for range queries.

In the scope of this project a custom boolean expression parser was implemented, which is included in the file _"Logic_Parser"_
in order to process queries as such:

__( A & B ) | ( C & D )__ where A, B, C, D are terms of the given collection.

On the first run the program will iteratively search the given folder for text files and parse them one by one, extracting all their words.

### Existence queries

An existence query is really simple, as it can be answered with a lookup on the Dictionary structure which is a HashMap,
making it rather effective. The user can type the term of his/her choice and a list of all the files containing the selected
term will be returned.

### Boolean Expression queries
The user is prompted to enter his/her query in the form __( A & B ) | ( C & ~ D )__ where A, B, C, D are the terms of interest.

Firstly, the program transforms the query from it's infix form _(as shown above)_ to a postfix form __( A B & C D ~ & | )__
[infix to postfix](https://www.geeksforgeeks.org/stack-set-2-infix-to-postfix/).

Then, the query is processed and excecuted in the implied postfix order. The corresponding partial answers are stored to a Stack
and its' final two elements are combined in the way indicated by the last symbol to lead to the final result of the query.

Finally, the result containing the final document ids is used to find the paths of the corresponding files.

### Ranked search
For the ranked search there is a need to define a new metric of relevance between the given query and
the documents in our collection which is the __Score__ value. In order to achieve this goal, we need to create a new Class
called Posting which has the docID and its' corresponding score. Based on that, a new inverse index of this form 
**_(Term->ArrayList<Posting>)_** is created.

The scoring used in this project is [TF-IDF](https://en.wikipedia.org/wiki/Tf%E2%80%93idf) scoring with normalization
using the documents' length. Furthermore, a method for displaying only the Top N results is employed in order to avoid the
sorting of multiple documents with zero score.

