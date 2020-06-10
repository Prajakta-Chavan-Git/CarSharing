package createObjects;

import storedobjects.Rating;

import java.util.ArrayList;
import java.util.Random;

public class ReviewFactory {

    private ArrayList<Rating> ratingList;
    private int amount;

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public ArrayList<Rating> getRatingList() {
        return ratingList;
    }

    public void setRatingList(ArrayList<Rating> ratingList) {
        this.ratingList = ratingList;
    }

    public ReviewFactory(int amount){
        this.amount = amount;
        this.ratingList = createReviews();
    }

    public ArrayList<Rating> createReviews(){
        ArrayList reviewL = new ArrayList<Rating>();
        for (int i = 0; i < getAmount(); i++) {
            int clean = randInt(0,5);
            int realy = randInt(0,5);
            int comfort = randInt(0,5);
            String comment = generateComment();
            Rating rating = new Rating(clean,comfort,realy,comment);
        }
        return reviewL;
    }
    private static int randInt(int min, int max){
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    private String generateComment(){
        String comment;
        int randomNum = randInt(0,10);
        switch (randomNum){
            case 0:
                comment = "Very clean car.";
                break;
            case 1:
                comment = "Nice and comftable!";
                break;
            case 2:
                comment = "Very reliable and easy to drive";
                break;
            case 3:
                comment = "I like this car";
                break;
            case 4:
                comment = "Wow, really nice and fast car";
                break;
            case 5:
                comment = "Nice car";
                break;
            case 6:
                comment = "Had no problem with this car";
                break;
            case 7:
                comment = "Some problems, but it was still fine";
                break;
            case 8:
                comment = "I could be more clean...";
                break;
            case 9:
                comment = "The breakes are not working as intended";
                break;
            case 10:
                comment = "Defective, please repair this car";
                break;
            default:
                comment = "Undefined";
                break;
        }
        return comment;
    }
}
