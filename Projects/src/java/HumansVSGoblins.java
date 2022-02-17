import java.util.*;

public class HumansVSGoblins {
    /*
    Directions:
    -Everything must be objects
    -You must override the toString method to represent each of the object
    -Create a grid for the game world
    -Use UTF characters for the players and goblins and the land
    -Game is turn based move: n/s/e/w
    -once a human and goblin collide, combat is initiated
    -combat uses math.random
    -Extras:
        -Human has inventory system
        -goblins have drops
        -stats can be modified by equipment
        -map gen random treasure chest after each round of combat
        -goblins pursue player
    -Add JUnit testing
     */
    /*
    Still need to do:
    -JUnit test
     */
    protected Human h;
    protected Goblin g;
    protected String[][] board;
    protected ArrayList<Item> potentialdrops;
    protected ArrayList<Item> itemsonboard;
    protected ArrayList<Item> chest;
    protected boolean goblinalive;
    protected boolean chestthere;
    protected int row;
    protected int col;


    //makes basic board. No human or goblin in it yet. Just walls and empty land
    public String[][] board(){
        String[][] board = new String[7][10];
        for (int i = 0; i<board.length; i++){
            for (int j = 0; j<board[i].length; j++){
                if (i == 0){ //top of board
                    board[i][j] = "_";
                }else if (i == board.length-1){ //bottom of board
                    board[i][j] = "-";
                }else if (j == 0 || j == board[i].length-1){ //left or right wall
                    board[i][j] = "|";
                }else{ //it's in the middle
                    board[i][j] = "*";
                }
            }
        }

        return board;
    }

    //Puts the human on the board. If goblin there, put it on the board. If chest, put it on as well. Then prints everything out
    public void printGame(){
        for (Item item : itemsonboard){
            board[item.getRow()][item.getCol()] = item.toString();
        }
        if (chestthere)
            board[row][col] = "=";
        if (goblinalive)
            board[g.getRow()][g.getCol()] = g.toString();
        board[h.getRow()][h.getCol()] = h.toString();
        //print the board
        for (String[] strings : board) {
            for (String string : strings) { //prints row
                System.out.print(string);
            }
            System.out.println();
        }
        System.out.println("Human Stats: \tHP: " + h.getHealth() + "\tAttack: " + h.getStrength() + "\tDefense: " + h.getDefense());
        System.out.println("Legend: \t\tH: Human(You), G: Goblin, ^: Item, =: Chest");
        System.out.println("Command help: \tw:up  a:left  s:down  d:right  r:use potion  i:open inventory  q:quit game");
    }

    //Creates the potential drops that enemies and chests may have.
    private ArrayList<Item> potentialDrops(){
        ArrayList<Item> drops = new ArrayList<>();
        drops.add(new Item("Weapon", "Bronze Sword", 3));
        drops.add(new Item("Weapon", "Iron Sword", 6));
        drops.add(new Item("Weapon", "Diamond Sword", 12));
        drops.add(new Item("Helmet", "Bronze Helmet", 1));
        drops.add(new Item("Helmet", "Iron Helmet", 3));
        drops.add(new Item("Helmet", "Steel Helmet", 5));
        drops.add(new Item("Chestplate", "Bronze Chestplate", 3));
        drops.add(new Item("Chestplate", "Iron Chestplate", 6));
        drops.add(new Item("Chestplate", "Steel Chestplate", 12));
        drops.add(new Item("Leggings", "Bronze Leggings", 2));
        drops.add(new Item("Leggings", "Iron Leggings", 4));
        drops.add(new Item("Leggings", "Steel Leggings", 8));
        drops.add(new Item("Shoes", "Bronze Shoes", 3));
        drops.add(new Item("Shoes", "Iron Shoes", 3));
        drops.add(new Item("Shoes", "Steel Shoes", 3));
        drops.add(new Item("Potion", "Health Potion", 20));

        return drops;
    }

    //Removes item at index i from potentialDrops unless it is a potion
    public void removeFromDrops(int i){
        if (!potentialdrops.get(i).getType().equals("Potion")) {
            potentialdrops.remove(i);
        }
    }

    //plays the game
    public void playGame(Scanner scanner){
        //create human, board, 1st goblin
        h = new Human(60, 3);
        int goblinhp = 40; //initial goblin hp
        int goblinstr = 1; //initial goblin strength
        g = new Goblin(goblinhp, goblinstr);
        board = board(); //the board
        potentialdrops = potentialDrops(); //potential drops from enemies/chests
        itemsonboard = new ArrayList<>(); //drops from enemies
        chest = new ArrayList<>(); //chest with 2 random items in it after enemy dies
        goblinalive = true; //is goblin alive
        chestthere = false; //is chest there
        boolean humandead = false; //determine if human is dead, and whether the player would like to start again
        boolean turnhappened; //determine if the human has used their turn or not
        row = 0; //row col for chest items
        col = 0;

        Scanner invScan = new Scanner(System.in); //scanner for inventory
        Scanner s = new Scanner(System.in); //scanner for quitting
        boolean quitcheck = false;//will change to true if user wants to quit
        printGame();//initially print the game

        //get input from the user on what they want to do
        while (true){
            turnhappened = false;
            String usercommand = "";
            boolean check; //checks if user hits a wall

            //get the command from the user on what to do
            do {
                check = true;
                try {
                    System.out.println("Command:");
                    usercommand = scanner.nextLine();
                    //lowercase it
                    usercommand = String.valueOf(Character.toLowerCase(usercommand.charAt(0)));

                } catch (Exception e) {
                    System.out.println("Invalid input");
                }
                //checks if valid command and only 1 letter
                if (!usercommand.matches("[wasdiqr]{1}")){
                    System.out.println("Invalid input");
                }else if(usercommand.matches("[wasd]{1}") && !h.notOutOfBounds(usercommand, board)){ //makes sure user doesn't hit a wall
                    System.out.println("You smash your head against a wall, pick a different direction to move");
                    check = false;
                }
            }while(!usercommand.matches("[wasdiqr]{1}") || !check);


            switch (usercommand) {
                //if wasd, they move accordingly
                case "w": //move up
                    board[h.getRow()][h.getCol()] = "*"; //resets the board at the human position
                    if (h.getRow() - 1 == g.getRow() && h.getCol() == g.getCol()) {//checks to see if would hit goblin
                        System.out.println(combat(true));
                    } else { //if not, move instead of attacking
                        h.setRow(h.getRow() - 1);
                    }
                    turnhappened = true; //used turn, goblin can move
                    break;
                case "a": //move left
                    board[h.getRow()][h.getCol()] = "*";
                    if (h.getCol() - 1 == g.getCol() && h.getRow() == g.getRow()) {
                        System.out.println(combat(true));
                    } else {
                        h.setCol(h.getCol() - 1);
                    }
                    turnhappened = true;
                    break;
                case "d": //move right
                    board[h.getRow()][h.getCol()] = "*";
                    if (h.getCol() + 1 == g.getCol() && h.getRow() == g.getRow()) {
                        System.out.println(combat(true));

                    } else {
                        h.setCol(h.getCol() + 1);
                    }
                    turnhappened = true;
                    break;
                case "s": //move down
                    board[h.getRow()][h.getCol()] = "*";
                    if (h.getRow() + 1 == g.getRow() && h.getCol() == g.getCol()) {
                        System.out.println(combat(true));

                    } else {
                        h.setRow(h.getRow() + 1);
                    }
                    turnhappened = true;
                    break;
                case "r":
                    //if r, use potion
                    //if player does not have potion, don't use it and don't use up player's turn
                    if(!h.usePotion()){
                        System.out.println("You have no health potions!");
                    }
                    break;
                case "i":
                    //if i, open inventory
                    inventory();
                    //put drops on board
                    //find out what the user dropped, if any
                    //difference between (potential drops, items on board, chest, and inventory) and potentialdrops()
                    if (!h.drops.isEmpty()){
                        for (Item i : h.drops){
                            i.setCol(h.getCol());
                            if(h.getRow()>2) i.setRow(h.getRow()-1);
                            else i.setRow(h.getRow()+1);
                            itemsonboard.add(i);
                        }
                        h.drops.clear();
                    }
                    break;
                //if q, quit game
                case "q":
                    //confirm they want to leave, if yes, leave the game and exit the method
                    quitcheck = yes(s);
                    break;
                default:
                    System.out.print("Something went wrong while moving");
                    break;
            }
            if (quitcheck) break; //quit game if user told you to
            if (h.getHealth() == 0){ //player died, game over, ask if they want to start again
                System.out.println("Game over");
                humandead = true;
                break;
            }
            if (!itemsonboard.isEmpty()) {
                for (int i = 0; i < itemsonboard.size(); i++) {
                    if (itemsonboard.get(i).getCol() == h.getCol() && itemsonboard.get(i).getRow() == h.getRow()) {
                        if(h.loot(itemsonboard.get(i), s, itemsonboard))//if looted, remove item from board
                            i--;
                            //itemsonboard.remove(i);
                    }
                }
            }
            //if human killed zombie after human initiated combat, make goblin drop a random droppable where he was, and make him disappear, and spawn a chest
            if (g.getHealth() == 0 && goblinalive){
                goblinDied();
            }

            //This section is if a chest is there and a human is standing on it
            if (chestthere){ //chest is out there
                if (chest.get(0).getCol() == h.getCol() && chest.get(0).getRow() == h.getRow()){ //if human is standing on chest
                    //loot the chest
                    lootChest();
                    //respawn the goblin
                    Random r = new Random();
                    do {
                        g.setRow(r.nextInt(board.length - 2) + 1);
                        g.setCol(r.nextInt(board[0].length - 2) + 1);
                    }while(g.getRow() == h.getRow() && g.getCol() == h.getCol());
                    goblinalive = true;
                    //goblin gets either boost in HP or strength
                    int i = r.nextInt(2);
                    if (i == 0){
                        goblinhp += 5;
                    }else {
                        goblinstr++;
                    }
                    g.setHealth(goblinhp);
                    g.setStrength(goblinstr);
                }

            }

            //Moving the gobin should it be alive and a turn happened
            if (goblinalive && turnhappened){
                goblinMove();
                if (h.getHealth() == 0){ //player died, game over, ask if they want to start again
                    System.out.println("Game over");
                    humandead = true;
                    break;
                }
            }


            //if goblin died after he initiated combat, make him drop a random droppable where he was, and make him disappear, and spawn a chest
            if (g.getHealth() == 0 && goblinalive){
                goblinDied();
            }


            //print updated game board
            printGame();
        }

        if (humandead){
            //ask if they want to play again
            if(yes(s))
               playGame(scanner);

        }
    }

    //loot from chest
    public void lootChest(){
        ArrayList<Item> temp = new ArrayList<>(); //temp "chest" storing everything that isn't looted
        for (int i = 0; i < chest.size(); i++) {
            Scanner sc1 = new Scanner(System.in);
            if (!h.loot(chest.get(i), sc1, chest)) { //if not looted, add it to the temp chest
                temp.add(chest.get(i));
            } else {
                i--;
            }
        }
        potentialdrops.addAll(temp); //add eveything to potential drops that wasn't looted
        chest.clear(); //empty the chest
        chestthere = false; //chest no longer there
    }

    //spawns chest in a random spot, add 2 random items to it
    public void goblinDied() {
        Random rand = new Random();
        int i = rand.nextInt(potentialdrops.size());
        Item drop = potentialdrops.get(i);
        removeFromDrops(i); //remove the item from potential drops unless potion
        drop.setCol(g.getCol());//put drop where goblin died
        drop.setRow(g.getRow());
        itemsonboard.add(drop);
        System.out.println("Goblin dropped " + drop.getName() + "!");
        g.setCol(0);
        g.setRow(0);
        goblinalive = false; //set goblin to dead
        //spawns chest in a random spot, add 2 random items to it
        rand = new Random();
        i = rand.nextInt(potentialdrops.size());
        Item chestdrop = potentialdrops.get(i);
        do {
            row = rand.nextInt(board.length - 2) + 1;
            col = rand.nextInt(board[0].length - 2) + 1;

        } while (row == h.getRow() && col == h.getCol()); //make sure the chest is not on a player
        chestdrop.setRow(row); //set the items to where the chest is
        chestdrop.setCol(col);
        chest.add(chestdrop);
        removeFromDrops(i); //remove the item from potential drops unless potion
        i = rand.nextInt(potentialdrops.size());
        chestdrop = potentialdrops.get(i);
        chestdrop.setRow(row);
        chestdrop.setCol(col);
        chest.add(chestdrop);
        removeFromDrops(i); //remove the item from potential drops unless potion
        chestthere = true;
    }

    //goblin moves
    public void goblinMove(){
        board[g.getRow()][g.getCol()] = "*"; //resets the board at Goblin position
        if(g.getRow()>h.getRow()){ //human is above goblin
            if (g.getRow() - 1 == h.getRow() && g.getCol() == h.getCol()) {//checks to see if goblin would hit human
                System.out.println("Goblin attacks human!");
                System.out.println(combat(false));
            } else { //if not, move instead of attacking
                System.out.println("Goblin moves up.");
                g.setRow(g.getRow() - 1);
            }
        }else if(g.getRow()<h.getRow()){ //human is below goblin
            if (g.getRow() + 1 == h.getRow() && g.getCol() == h.getCol()) {
                System.out.println("Goblin attacks human!");
                System.out.println(combat(false));

            } else {
                System.out.println("Goblin moves down.");
                g.setRow(g.getRow() + 1);
            }
        }else{ //goblin in same row
            if (g.getCol()>h.getCol()){ //human is to the left of goblin
                if (g.getCol() - 1 == h.getCol() && g.getRow() == h.getRow()) {
                    System.out.println("Goblin attacks human!");
                    System.out.println(combat(false));
                } else {
                    g.setCol(g.getCol() - 1);
                    System.out.println("Goblin moves left.");
                }
            }else{ //human is to the right of goblin. Impossible for it to be on top of human
                if (g.getCol() + 1 == h.getCol() && g.getRow() == h.getRow()) {
                    System.out.println("Goblin attacks human!");
                    System.out.println(combat(false));
                } else {
                    System.out.println("Goblin moves right.");
                    g.setCol(g.getCol() + 1);
                }
            }
        }
    }

    //Finds out whether the user really wants to quit or not
    public boolean yes(Scanner scanner){
        String answer = "";
        do{
            try {
                System.out.println("Are you sure you want to quit? (y or n)");
                answer = scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Please input 'y' or 'n'"); //honestly don't know how you'd get this outide of control-something since everything else would be a string
            }
            if (!answer.equals("y") && !answer.equals("n")){ //ensures user enters y or n
                System.out.println("Please input 'y' or 'n'");
            }
        }while(!answer.equals("y") && !answer.equals("n")); //do it until a valid answer is given

        return answer.equals("y");//If yes, return true
    }

    //does the combat. Returns whether anyone died. If not, it's an empty string
    public String combat(boolean humaninitcom){
        Random r = new Random();
        if (humaninitcom){ //if human started, they attack first
            humanAttack(r);

            if (g.getHealth()>0) //if goblin alive, counterattack
                goblinAttack(r);

        }else{ //goblin attacked first
            goblinAttack(r);

            if (h.getHealth()>0) //if human alive, counterattack
                humanAttack(r);
        }

        if (h.getHealth()==0) return "human has died";
        else if(g.getHealth() == 0) return "goblin has died";
        else return "";

    }

    //human attacks goblin
    public void humanAttack(Random r){
        int humanattack = r.nextInt((h.getStrength()+3)-(h.getStrength()-2))+h.getStrength()-2; //a random, +/- 2 for the human attack
        g.setHealth(g.getHealth()- humanattack); //does the damage
        if (g.getHealth()<0) //if below zero hp, set to 0
            g.setHealth(0);
        System.out.println("Human attacks goblin for " + humanattack +" damage. Goblin HP = " + g.getHealth());
    }

    //goblin attacks human
    public void goblinAttack(Random r){
        int goblinattack = r.nextInt((g.getStrength()+3)-(g.getStrength()-2))+g.getStrength()-2-h.getDefense();
        if (goblinattack <=0){ //goblin misses
            System.out.println("Goblin attack misses.");
        }else{ //goblin hits
            h.setHealth(h.getHealth()-goblinattack);
            if (h.getHealth()<0)
                h.setHealth(0);
            System.out.println("Goblin attacks human for " + goblinattack +" damage. Human HP = " + h.getHealth());
        }
    }

    //deals with the inventory system. User selects a spot, can choose to unequip, equip, drop, or go back to the game
    public void inventory(){
        Scanner s = new Scanner(System.in);
        //should they select an item, give it it's type, name, and stats. Then ask whether they want to equip/unequip that item
        h.printinventory();
        String input = "";
        int num = -1;
        boolean check;
        //ask what item they wish to select or press q to exit
        do {
            check = true;
            try {
                System.out.println("Select Item number or press q to exit inventory");
                input = s.nextLine();

            } catch (Exception e) {
                System.out.println("Invalid input");
            }
            input = input.toLowerCase(); //lowercase it
            //checks if q or a number between 1 and maxinvntory slots.
            if (!input.equals("q")){ //if not q, then must be a slot
                try {
                    num = Integer.parseInt(input);
                    if (num < 1 || num>h.maxinventory){ //if slot doesn't exist, check fails
                        System.out.println("Invalid input");
                        check = false;
                    }else if(h.inventory.get(num-1).getType().equals("empty")){ //if slot is empty, check fails
                        System.out.println("No item in slot");
                        check = false; //empty slot. failed check
                        num = -1; //reset num
                    }
                }catch (Exception e) {
                    System.out.println("Invalid input.");
                    check = false;
                }
            }
        }while(!check);


        //if selected an item, show item info and decide whether to equip or unequip the item, depending on where it's located
        if (num>0 && num<=h.maxinventory){
            System.out.println("Type: "+ h.inventory.get(num-1).getType());
            System.out.println("Name: "+ h.inventory.get(num-1).getName());
            if (h.inventory.get(num-1).getType().equals("Weapon"))
                System.out.println("Damage: "+ h.inventory.get(num-1).getStat());
            else System.out.println("Defense: "+ h.inventory.get(num-1).getStat());

            if (num <6){ //selected an equipped item
                System.out.println("u: unequip q: back");
                while (!s.hasNext("[uq]")) { //This line ensures that the user inputed y or n, if not, it'll continue to ask for the right input
                    System.out.println("Not valid input, please input 'u' or 'q'");
                    s.next();
                }
                String answer = s.next();
                if (answer.equals("u")){ //unequip item
                    h.unequip(num-1);
                }
            }else{ //selected an unequipped item
                if (!h.inventory.get(num-1).getType().equals("Potion")) { //if not potion
                    System.out.println("e: equip q: back d: drop");
                    while (!s.hasNext("[eqd]")) { //This line ensures that the user inputed y or n, if not, it'll continue to ask for the right input
                        System.out.println("Not valid input, please input 'e', 'd', or 'q'");
                        s.next();
                    }
                    String answer = s.next();
                    switch (answer) {
                        case "e" -> h.equip(num - 1);
                        case "d" -> h.drop(num - 1);
                        default -> {
                        }
                    }
                }else{
                    System.out.println("r: use potion q: back");
                    while (!s.hasNext("[rq]")) { //This line ensures that the user inputed y or n, if not, it'll continue to ask for the right input
                        System.out.println("Not valid input, please input 'r', or 'q'");
                        s.next();
                    }
                    String answer = s.next();
                    if ("r".equals(answer)) {
                        h.usePotion();
                    }
                }
            }
        }
        if (!input.equals("q")){ //so long as not q, stay in inventory
            inventory();
        }

    }


}
