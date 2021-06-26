# Search Engine
Basic search engine university project. This was created in the context of "Information Retrieval" course.

Special _thank you_ to my professor **Christos Doulkeridis** who guided me through the process of creating this.

### Functionality

This engine supports the following:
- [x] [Existence queries](#existence-queries)
- [x] [Boolean Expression queries](#boolean-expression-queries)
- [x] [Ranked search](#ranked-search)

### Details

The collection of texts used for demonstration purposes is included in the folder _"/data"_ and is a subset of much larger
Reuters news collection.
Furthermore, for the creation of the inverted index, document frequency table (which is used to aggregate the appearance of a term in the collection), term frequency table (which is used to aggregate the appearnce of a term inside a document) and document length structures, HashMap has been used as 
it supports _insertion_ and _retrieval_ with __O(1)__ time. One other choice considered for this project was the usage of Binary trees as the structure for the inverted index, which would provide the capability of range queries _(ex. all terms wa-wo)_ increasing the cost of search and insertion at __O(logn)__. 

In the scope of this project a custom boolean expression parser was implemented, which is included in the file _"Logic_Parser"_
in order to process queries as such:

__( A & B ) | ( C & D )__ where A, B, C, D are terms of the given collection.

### Asumptions

While a real-world engine would use a crawler to fetch new documents for insertion in the search engine's database and multiple techniques to optimally insert terms in the inverted index as lemmatization, stop-word removal, stemming and compression, I chose not to implement those for simplicity's sake. However, these techniques can be implemented in the near future to improve the efficiency and the fucntionality of this project. For example, stop-word removal could be as simple as adding to the delimiter list of the tokenization the stop-word list.

This search engine supports reading only text files (.txt) from the given directory but there are multiple libraries in JAVA to add the functionality of reading text through PDF files [pdf box](http://www.pdfbox.org/). Even though that this search engine can support multiple languages due to it's simplicity, it has only been tested with English documents.

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


![equation](https://latex.codecogs.com/gif.latex?%5Cbegin%7Bbmatrix%7D%20%5C%5C%20or%20%5C%5Cand%20%5C%5C%20not%20%5C%5C%20D%20%5C%5C%20C%20%5C%5C%20and%20%5C%5C%20A%20%5C%5C%20B%20%5Cend%7Bbmatrix%7D%20%5Cxrightarrow%7B%5Ctext%7BA%20and%20B%7D%7D%20%5Cbegin%7Bbmatrix%7D%20%5C%5C%20-%20%5C%5Cor%20%5C%5C%20and%20%5C%5C%20not%20%5C%5C%20D%20%5C%5C%20C%20%5C%5C%20r%20%5Cend%7Bbmatrix%7D%20%5Cxrightarrow%7B%5Ctext%7Bnot%20D%7D%7D%20%5Cbegin%7Bbmatrix%7D%20%5C%5C%20-%20%5C%5C%20or%20%5C%5C%20and%20%5C%5C%20not%20D%20%5C%5C%20C%20%5C%5C%20r%20%5Cend%7Bbmatrix%7D%20%5Cxrightarrow%7B%5Ctext%7BC%20and%20%28not%20D%29%7D%7D%20%5Cbegin%7Bbmatrix%7D%20%5C%5C%20-%20%5C%5C%20-%20%5C%5C%20-%20%5C%5C%20or%20%5C%5C%20r2%20%5C%5C%20r%20%5Cend%7Bbmatrix%7D%20%5Cxrightarrow%7B%5Ctext%7Br%20or%20r2%7D%7D%20%5Cbegin%7Bbmatrix%7D%20%5C%5C%20-%20%5C%5C%20-%20%5C%5C%20-%20%5C%5C%20-%20%5C%5C%20-%20%5C%5C%20r3%20%5Cend%7Bbmatrix%7D%20%5Cxrightarrow%7B%5Ctext%7BANSWER%7D%7D)

Finally, the result containing the final document ids is used to find the paths of the corresponding files.

### Ranked search
In order to rank the result of a query there is a need to define a new metric of relevance between the given query and
the documents in our collection which is the __Score__ value. In order to achieve this goal, we need to create a new Class
called Posting which has the docID and its' corresponding score. Based on that, a new inverse index of this form 
**_(Term->ArrayList\<Posting>)_** is created.

In this project the (vector-space)[https://en.wikipedia.org/wiki/Vector_space_model] model is used to decribe both the queries and the documents. 
The similarity between a document and a query is expressed by the the angle formed by the two but in practise it's much is easier to calculate the cosine of their angle. As all the documents will be in the first quarter of the cartesian axes (there cannot be a negative term in a document) the cosine will be positive and monotonically increasing which allow the usage of the cosine for the calculation of query-document similarity.
#### Cosine Similiraty

![equation](https://latex.codecogs.com/gif.latex?similarity%20%3D%5Ccos%28theta%29%3D%20%5Cfrac%7BDi%5Ccdot%20q%7D%7B%5Cleft%20%5C%7C%20Di%20%5Cright%20%5C%7C%5Ccdot%20%5Cleft%20%5C%7C%20q%20%5Cright%20%5C%7C%7D%3D%5Cfrac%7B%5Csum%20Diq%7D%7B%5Csqrt%28%5Csum%28Di%5E2%29%29%5Ccdot%5Csqrt%28%5Csum%28q%5E2%29%29%7D)

The scoring used in this project is [TF-IDF](https://en.wikipedia.org/wiki/Tf%E2%80%93idf) scoring with normalization
using the documents' length. Furthermore, as all the documents will receive a score between [0,1] a method for displaying only the Top N results is employed in order to avoid the sorting of multiple documents with zero score.

