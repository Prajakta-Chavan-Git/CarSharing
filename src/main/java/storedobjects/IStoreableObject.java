package storedobjects;
import org.bson.Document;

public interface IStoreableObject {

    public Document toDocument();
}
