package createObjects;

import storedobjects.Car;
import storedobjects.Comment;

import java.awt.*;
import java.util.Random;

import java.util.ArrayList;

public class CarFactory {

    private ArrayList<Car> carList;
    private int amount;

    public ArrayList<Car> getCarList() {
        return carList;
    }

    public void setCarList(ArrayList<Car> carList) {
        this.carList = carList;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public CarFactory(int amount){
        this.amount  = amount;
        this.carList = createCars();
    }

    public ArrayList<Car> createCars(){
        ArrayList carL = new ArrayList<Car>();
        for (int i = 0; i <= getAmount() ; i++) {
            int seats = randInt(2,7);
            double fuel = randDouble(3.0,20.0);
            double lat = randDouble(-90.0,90.0);
            double longi = randDouble(-180.0,180.0);
            String manufacturer = getRandomBrand();
            String type = getRandomType();
            String status = getRandomStatus();
            String fuelType = getRandomFuel();
            ArrayList commentList = generateComments();
            Car newCar = new Car(seats,null,manufacturer,type,fuel,longi,lat,status,fuelType);
            carL.add(newCar);
        }
        return carL;
    }

    private String getRandomBrand(){
        String brand;
        int randomNum = randInt(0,10);
        switch (randomNum){
            case 1:
                brand = "BMW";
                break;
            case 2:
                brand = "Audi";
                break;
            case 3:
                brand = "Renault";
                break;
            case 4:
                brand = "Mercedes";
                break;
            case 5:
                brand = "Citroen";
                break;
            case 6:
                brand = "Porsche";
                break;
            case 7:
                brand = "Volkswagen";
                break;
            case 8:
                brand = "Ford";
                break;
            case 9:
                brand = "Opel";
                break;
            case 10:
                brand = "Hyundai";
                break;
            default:
                brand = "Seat";
                break;
        }
        return brand;
    }

    private String getRandomType(){
        String type;
        int randomNum = randInt(1,8);
        switch (randomNum){
            case 1:
                type = "Cabrio";
                break;
            case 2:
                type = "Coupe";
                break;
            case 3:
                type = "Limousine";
                break;
            case 4:
                type = "Pick-up";
                break;
            case 5:
                type = "Truck";
                break;
            case 6:
                type = "SUV";
                break;
            case 7:
                type = "Transporter";
                break;
            case 8:
                type = "Combi";
                break;
            default:
                type = "Undefined";
                break;
        }
        return type;
    }

    private String getRandomFuel(){
        String type;
        int randomNum = randInt(1,6);
        switch (randomNum){
            case 1:
                type = "Diesel";
                break;
            case 2:
                type = "Electric";
                break;
            case 3:
                type = "Petrol";
                break;
            case 4:
                type = "Hybrid";
                break;
            case 5:
                type = "Ethanol";
                break;
            case 6:
                type = "Hydrogene";
                break;
            default:
                type = "Undefined";
                break;
        }
        return type;
    }

    private String getRandomStatus(){
        String status;
        int randomNum = randInt(0,2);
        switch (randomNum){
            case 0:
                status = "Available";
                break;
            case 1:
                status = "Occupied";
                break;
            case 2:
                status = "Defective";
                break;
            default:
                status = "Undefined";
                break;
        }
        return status;
    }

    private ArrayList<Comment> generateComments(){
        String comment;
        ArrayList commentList = new ArrayList<Comment>();
        int randomNum = randInt(0,30);
        for (int i = 0; i <= randomNum; i++) {
            comment = "Test Comment" + i;
            commentList.add(comment);
        }
        return commentList;
    }

    private static int randInt(int min, int max){
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    private static double randDouble(double min, double max){
        Random rand = new Random();
        double randomNum =  min + (max - min) * rand.nextDouble();
        return randomNum;
    }

}
