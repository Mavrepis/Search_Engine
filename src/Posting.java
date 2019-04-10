import java.io.Serializable;

public class Posting implements Serializable {
    private int docId;
    private float score;

    int getDocId() {
        return docId;
    }

    float getScore() {
        return score;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    void setScore(float score) {
        this.score = score;
    }

    Posting(int docId, float score) {
        this.docId = docId;
        this.score = score;
    }
}


