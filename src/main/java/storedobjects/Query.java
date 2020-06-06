package storedobjects;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Query implements IStoreableObject {
    private List<String> parameter;

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
        return doc;
    }
}
