package createObjects;

import storedobjects.Address;
import storedobjects.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class AddressFactory {

    private Address address;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public AddressFactory(){
        this.address = getRandomAddress();
    }

    private Address getRandomAddress(){
        Address address;
        String street;
        String city;
        int randomNum = randInt(1,10);
        int randomNum2 = randInt(1,10);
        int houseNr = randInt(1,200);
        String hnr = Integer.toString(houseNr);
        int postal = randInt(10000,99999);
        String pnr = Integer.toString(postal);
        switch (randomNum){
            case 1:
                street = "Hauptstraße";
                break;
            case 2:
                street = "Feldgasse";
                break;
            case 3:
                street = "Bahnhofstraße";
                break;
            case 4:
                street = "Hauptstraße";
                break;
            case 5:
                street = "Dorfstraße";
                break;
            case 6:
                street = "Schulstraße";
                break;
            case 7:
                street = "Talweg";
                break;
            case 8:
                street = "Zigeunerstraße";
                break;
            case 9:
                street = "Haydnstraße";
                break;
            case 10:
                street = "Karl Marx Straße";
                break;
            default:
                street = "Gartenstraße";
                break;
        }
        switch (randomNum2){
            case 1:
                city = "Heidelberg";
                break;
            case 2:
                city = "Düsseldorf";
                break;
            case 3:
                city = "Berlin";
                break;
            case 4:
                city = "Hamburg";
                break;
            case 5:
                city = "Mannheim";
                break;
            case 6:
                city = "Köln";
                break;
            case 7:
                city = "Bremen";
                break;
            case 8:
                city = "Leibzig";
                break;
            case 9:
                city = "Trier";
                break;
            case 10:
                city = "Karlsruhe";
                break;
            default:
                city = "Freiburg";
                break;
        }
        address = new Address(city,street,hnr,pnr);
        return address;
    }

    private static int randInt(int min, int max){
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

}
