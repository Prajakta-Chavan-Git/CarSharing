package createObjects;


import storedobjects.Car;
import storedobjects.Query;
import storedobjects.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QueryFactory {

    private ArrayList<Query> queryList;
    private int amount;

    public QueryFactory(int amount){

        this.amount=amount;
    }

    public ArrayList<Query> create(){
        queryList = new ArrayList<>();
        for (int i = 0; i < amount; i++){
            Query query= new Query();
            query.setParameter(generateParam());
            query.setRadius(randInt(100,500));
            queryList.add(query);
        }
        return queryList;
    }

    private List<String> generateParam() {
        ArrayList<String> parameters = new ArrayList<>();
        parameters.add("seats:" + randInt(1,7));
        parameters.add("manufacturer:" + CarFactory.getRandomBrand());
        parameters.add("fuelType:" + CarFactory.getRandomFuel());
        parameters.add("type:" + CarFactory.getRandomType());
        return parameters;
    }


    public static int randInt(int min, int max){
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}
