package createObjects;

import storedobjects.Address;
import storedobjects.User;
import storedobjects.Comment;

import java.util.ArrayList;
import java.util.Random;
import java.util.Date;

public class UserFactory {

    private ArrayList<User> userList;
    private int amount;

    public ArrayList<User> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<User> carList) {
        this.userList = userList;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public UserFactory(int amount){
        this.amount  = amount;
        this.userList = createUsers();
    }

    public ArrayList<User> createUsers(){
        ArrayList userL = new ArrayList<User>();
        for (int i = 0; i <= getAmount() ; i++) {
            Date date = new Date(randInt(19,102),randInt(1,12),randInt(1,30));
            int phoneNr = randInt(1000000, 999999999);
            String pnr = Integer.toString(phoneNr);
            double lat = randDouble(-90.0,90.0);
            double longi = randDouble(-180.0,180.0);
            String name = getRandomName();
            Address address = getRandomAddress();
            String email = generateEmail(name);
            String bankacc = generateBankAccount();
            String status = getRandomStatus();
            ArrayList commentList = generateComments();
            //CarFactory factory = new CarFactory(randInt(0,5));
            User newUser = new User(date,null, name, address , pnr, email, bankacc, status, null);
            userL.add(newUser);
            System.out.println(newUser.getAddress());
            System.out.println(newUser.getBankAccount());
            System.out.println(newUser.getBirthDate());
            System.out.println(newUser.getEmail());
            System.out.println(newUser.getName());
            System.out.println(newUser.getPhoneNumber());
            System.out.println(newUser.getStatus());

        }
        return userL;
    }

    private String getRandomName(){
        String name;
        int randomNum = randInt(0,20);
        switch (randomNum){
            case 1:
                name = "Marcel";
                break;
            case 2:
                name = "Max";
                break;
            case 3:
                name = "Maurice";
                break;
            case 4:
                name = "Peter";
                break;
            case 5:
                name = "Sven";
                break;
            case 6:
                name = "Tobias";
                break;
            case 7:
                name = "Rodrigo";
                break;
            case 8:
                name = "Eric";
                break;
            case 9:
                name = "Robin";
                break;
            case 10:
                name = "Patrick";
                break;
            case 11:
                name = "Erica";
                break;
            case 12:
                name = "Sarah";
                break;
            case 13:
                name = "Sophia";
                break;
            case 14:
                name = "Alice";
                break;
            case 15:
                name = "Jenna";
                break;
            case 16:
                name = "Morgane";
                break;
            case 17:
                name = "Dominique";
                break;
            case 18:
                name = "Elisa";
                break;
            case 19:
                name = "Kelly";
                break;
            case 20:
                name = "Christine";
                break;
            default:
                name = "Amanada";
                break;
        }
        return name;
    }

    private String generateEmail(String name){
        String email;
        String ending;
        int randomNum = randInt(0,4);
        switch (randomNum){
            case 0:
                ending = "@gmail.com";
                break;
            case 1:
                ending = "@outlook.com";
                break;
            case 2:
                ending = "@yahoo.com";
                break;
            case 3:
                ending = "@hotmail.com";
                break;
            case 4:
                ending = "@live.com";
                break;
            default:
                ending = "@defaultmail.com";
                break;
        }
        email = name + ending;
        return email;
    }

    private String generateBankAccount(){
        String bank;
        int nr1 = randInt(100000000,999999999);
        String bnr1 = Integer.toString(nr1);
        int nr2 = randInt(100000000,999999999);
        String bnr2 = Integer.toString(nr2);
        bank = "DE" + bnr1 + bnr2;
        return bank;
    }

    private Address getRandomAddress(){
        AddressFactory address = new AddressFactory();
        return address.getAddress();
    }

    private String getRandomStatus(){
        String status;
        int randomNum = randInt(0,1);
        switch (randomNum){
            case 0:
                status = "User";
                break;
            case 1:
                status = "Lender";
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
