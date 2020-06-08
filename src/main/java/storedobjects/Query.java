package storedobjects;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Query implements IStoreableObject {
    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    private String objectID;
    private List<String> parameter;
    private int radius;

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setParameter(List<String> parameter) {
        this.parameter = parameter;
    }

    public List<String> getParameter() {
        return parameter;
    }

    public Query() {
        parameter = new ArrayList<>();
    }

    public void addParam(String param){
        parameter.add(param);
    }

    @Override
    public Document toDocument() {
        Document doc = new Document();
        if(parameter != null){
            doc.append("parameter", parameter);
        }
        if(radius ==0){
            doc.append("radius", radius);
        }
        return doc;
    }
}
