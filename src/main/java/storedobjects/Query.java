package storedobjects;

import java.util.ArrayList;
import java.util.List;

public class Query {
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
}
