package storedobjects;

import org.bson.Document;

public class Comment implements IStoreableObject {
    private String objectID;
    private String text;
    private User writer;

    @Override
    public Document toDocument() {
        Document doc = new Document();
        if (objectID != null)
            doc.append("objectID", objectID);
        if (text != null)
            doc.append("text", text);
        if (writer != null)
            doc.append("writer", writer);
        return doc;
    }
}
