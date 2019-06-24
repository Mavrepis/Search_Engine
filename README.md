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
it supports _insertion_ and _retrieval_ with __O(1)__ time. One other choice considered for this project was the usage of Binary trees as the structure for the inverted index, which would provide the capability of range queries _(ex. all terms wa-wo)_ increasing the cost of search and insertion at __O(logn)__. 

In the scope of this project a custom boolean expression parser was implemented, which is included in the file _"Logic_Parser"_
in order to process queries as such:

__( A & B ) | ( C & D )__ where A, B, C, D are terms of the given collection.

### First Run

The program will iteratively search the given folder for text files and parse them one by one, extracting all their words through tokenization excluding common words, numbers, tabs, spaces and other special characters. After this, the term frequencies and the document frequencies tables are updated and the terms are inserted into the inverted index. The Inverted index can be either a __HashMap<String,ArrayList<Integer\>>__ if there is only the need to answer boolean questions and the support for ranked queries is not requiered or a  __HashMap<String,ArrayList<Posting\>>__ where the custom class Posting consists of docID and score fields. When all of the documents are processed and the document frequency table is complete, the score of each Posting in the Dictionary is multiplied by _log(N/df(t))_, formerly being the term frequency, forming the term-document final score.

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
In order to rank the result of a query there is a need to define a new metric of relevance between the given query and
the documents in our collection which is the __Score__ value. In order to achieve this goal, we need to create a new Class
called Posting which has the docID and its' corresponding score. Based on that, a new inverse index of this form 
**_(Term->ArrayList\<Posting>)_** is created.

In this project the (vector-space)[https://en.wikipedia.org/wiki/Vector_space_model] model is used to decribe both the queries and the documents. The similarity between a document and a query is expressed by the the angle formed by the two but in practise it's much is easier to calculate the cosine of their angle. As all the documents will be in the first quarter of the cartesian axes (there cannot be a negative term in a document) the cosine will be positive and monotonically increasing which allow the usage of the cosine for the calculation of query-document similarity.

The scoring used in this project is [TF-IDF](https://en.wikipedia.org/wiki/Tf%E2%80%93idf) scoring with normalization
using the documents' length. Furthermore, as all the documents will receive a score between [0,1] a method for displaying only the Top N results is employed in order to avoid the sorting of multiple documents with zero score.

