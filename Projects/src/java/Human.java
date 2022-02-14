import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class Human {
    private int row;
    private int col;
    private int health;
    private int strength;
    private int defense;
    final int maxHealth;
    ArrayList<Item> inventory;
    final int maxinventory;

    public Human (int health, int strength){
        this.inventory = new ArrayList<>(); //new inventory. 20 slots. 3 each slot for type, name, and stat
        this.maxinventory = 20;
        Item empty = new Item();
        for (int i = 0; i<maxinventory; i++){
            this.inventory.add(empty);
        }
        this.maxHealth = 80;
        this.row = 4;
        this.col = 4;
        this.health = health;
        this.strength = strength;
    }

    public int getRow() {
        return this.row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return this.col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getStrength() {
        return this.strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDefense() {
        return this.defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public String toString(){
        return "@";
    }

    //checks that it won't hit a wall
    public boolean notOutOfBounds(String s, String[][] board){
        if (s.equals("a") && !board[this.getRow()][this.getCol()-1].equals("|")){
            return true;
        }else if(s.equals("d") && !board[this.getRow()][this.getCol()+1].equals("|")){
            return true;
        }else if (s.equals("w") && !board[this.getRow()-1][this.getCol()].equals("_")){
            return true;
        }else return s.equals("s") && !board[this.getRow() + 1][this.getCol()].equals("-");
    }

    //adds an item to the human's inventory
    public boolean addItemToInventory(Item item){

        for (int i = 5; i<this.maxinventory; i++){ //starts at the storage part of inventory
            if (this.inventory.get(i).getType().equals("empty")){ //if nothing is there, add the type, name, and stat of the
                this.inventory.set(i, item);
                System.out.println(item.getName() + " was added to your backpack");
                return true;
            }
        }
        System.out.println(item.getName() + " was not added to your backpack. Please make space in your backpack");
        return false;
    }

    //unequip an item
    public boolean unequip(int num){
        if(this.addItemToInventory(this.inventory.get(num))) {
            if (this.inventory.get(num).getType().equals("Weapon")) {
                this.setStrength(this.getStrength() - this.inventory.get(num).getStat()); //minus the current damage by the damage of the unequipped item
            } else {
                this.setDefense(this.getDefense() - this.inventory.get(num).getStat()); //minus defense
            }
            //get rid of item in equipped space
            System.out.println(this.inventory.get(num).getName() + " was unequipped");
            this.inventory.set(num, new Item());
            return true;
        }
        return false;
    }

    //equip an item from backpack
    public boolean equip(int num){
        //switch item at num with the item type
        int index = -1;
        switch (this.inventory.get(num).getType()) { //figure out where to equip the item too
            case "Weapon" -> index = 0;
            case "Helmet" -> index = 1;
            case "Chestplate" -> index = 2;
            case "Leggings" -> index = 3;
            case "Shoes" -> index = 4;
            default -> {
                System.out.println("Can't equip a potion");
                return false;
            }
        }

        Item temp = this.inventory.get(index);
        this.inventory.set(index, this.inventory.get(num));
        this.inventory.set(num, temp);

        //change stats
        if (index == 0){ //changes damage since weapon
            this.setStrength(this.getStrength()-temp.getStat()+this.inventory.get(index).getStat()); //take out the old stat, add the new stat
        }else{ //changes defense since armor
            this.setDefense(this.getDefense()-temp.getStat()+this.inventory.get(index).getStat());
        }
        return true;
    }

    public ArrayList<Item> loot(Item i, Scanner s, ArrayList<Item> itemsonboard){
        String answer = "";
        do{
            try {
                if(i.getType().equals("Potion")) System.out.println("Loot " + i.getName() +"? Type: " + i.getType() + " Healing: " + i.getStat() + "HP");
                else if(i.getType().equals("Weapon")) System.out.println("Loot " + i.getName() +"? Type: " + i.getType() + " Damage: " + i.getStat());
                else System.out.println("Loot " + i.getName() +"? Type: " + i.getType() + " Defense: " + i.getStat());

                answer = s.nextLine();
            } catch (Exception e) {
                System.out.println("Please input 'y' or 'n'"); //honestly don't know how you'd get this outide of control-something since everything else would be a string
            }
            if (!answer.equals("y") && !answer.equals("n")){ //ensures user enters y or n
                System.out.println("Please input 'y' or 'n'");
            }
        }while(!answer.equals("y") && !answer.equals("n")); //do it until a valid answer is given

        if (answer.equals("y")){
            this.addItemToInventory(i);
            itemsonboard.remove(i);
        }

        return itemsonboard;

    }

    public boolean usePotion(){
        boolean foundpotion = false;
        for (Item i : this.inventory){
            if (i.getType().equals("Potion")) {
                foundpotion = true;
                this.setHealth(this.getHealth()+i.getStat()); //"use" potion
                if (this.getHealth()>this.maxHealth){ //don't go above max health
                    this.setHealth(this.maxHealth);
                }
                System.out.println("Potion used, HP is now " + this.getHealth());
                this.inventory.remove(i); //remove the item and replace it with an empty space
                this.inventory.add(new Item());
                break;
            }
        }
        return foundpotion;
    }

    //prints inventory
    public void printinventory(){
        for (int i = 0; i<this.maxinventory; i++){
            //Equipped section
            if (i == 0){
                System.out.println("Equipped items:");
                System.out.println((i+1) + ". Weapon: " + this.inventory.get(i).getName());
            }else if (i == 1){
                System.out.println((i+1) + ". Helmet: " + this.inventory.get(i).getName());
            }
            else if (i == 2){
                System.out.println((i+1) + ". Torso: "+ this.inventory.get(i).getName());
            }
            else if (i == 3){
                System.out.println((i+1) + ". Legs: " + this.inventory.get(i).getName());
            }
            else if (i == 4){
                System.out.println((i+1) + ". Feet: " + this.inventory.get(i).getName());
            } else if (i ==5){
                System.out.println();
                System.out.println("In backback:");
            }

            //everything else isn't equipped
            if (i>4) {
                System.out.println((i + 1) + ". " + this.inventory.get(i).getName()); //prints out the slot number with the name of the item there
            }

        }
    }

}
