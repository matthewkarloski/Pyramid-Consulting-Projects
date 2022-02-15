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
    what to do:
        -Keep track of goblin/human location with int attributes, row, col
            -Can't go below 1 (Hits wall)
            -can't go above max (hits wall)
            -Should they collide (on top of each other), initiate combat, but don't move the characters
                -each have a strength attribute, random from -2/+2 that attribute, then minus defense to decide damage
                    -lowest an attack can be is 0, which if true should be considered a "miss"
                    -if goblin health is below 0, human wins and game continues
                    -else if human health is below 0, human dies and the game ends
        -Inventory system will be ArrayList<Items>. Each Item will have a name, type, and stat attribute
            -armor type(add to defense attribute. Iron, steel, diamond variants):
                -chestplate
                -helmet
                -legs
                -feet
            -weapon type(add to strength. Iron, steel, diamond variants):
                -sword
            -potion type (add health. Make more common.)
            -don't spawn something the human has, except potions
            -need a way to equip/unequip loot
                -make first 5 slots the equipped, last 15 spots are unequipped/potions
            -print inventory when 'i' is pressed
        -Once combat is over and human won. Spawn a chest randomly on map (not on human though)
            -once chest is collected, randomly spawn another goblin at edge of the map to start a combat loop
                -don't spawn at or next to player
        -each turn, the goblin moves towards the human after the human moves
            -if row or col attribute for goblin is higher than human, move -1 that attribute
            -if lower, move +1 that attribute
        -each turn
            -player decides to move, heal, or leave the game
                -move- wasd
                    ad - +/- 1 from col (move left right)
                    ws - +/- 1 from row (move up down)
                -inventory - i - does not waste a turn unequipping/equipping. Can access it at beginning of turn
                -use potion - r - heals player
                -leave game - q - quits game
     */
    /*
    Still need to do:
    -goblin moves toward human at end of human turn
        -if below human go up
        -if above human, go down
        -etc.
        -Have turn be a boolean
            -goblin only moves if boolean is true
            -map only prints if boolean is true
        -Move check if human dead until after goblin moves, incase human dies from combat with goblin
    -Fix scanner bug 'invalid input' in inventory
        -add another scanner
    -Add info to GUI
        -Strength
        -HP
        -Defense
        -Legend (H: Human(You), G: Goblin, ^: Item, =: Chest)
        -Command help(w: up a:left s:down d: right r:use potion i:open inventory q:quit game)
    -Potential bug: Selecting empty slot in inventory
     */

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
    public void printGame(Goblin goblin, Human human, String[][] board, ArrayList<Item> items, int chestrow, int chestcol, boolean goblinthere, boolean chestthere){
        for (Item item : items){
            board[item.getRow()][item.getCol()] = item.toString();
        }
        if (chestthere)
            board[chestrow][chestcol] = "=";
        if (goblinthere)
            board[goblin.getRow()][goblin.getCol()] = goblin.toString();
        board[human.getRow()][human.getCol()] = human.toString();
        //print the board
        for (String[] strings : board) {
            for (String string : strings) { //prints row
                System.out.print(string);
            }
            System.out.println();
        }
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

    public ArrayList<Item> removeFromDrops(ArrayList<Item> drops, int i){
        if (!drops.get(i).getType().equals("Potion")) {
            drops.remove(i);
        }
        return drops;
    }

    //plays the game
    public void playGame(Scanner scanner){
        //create human, board, 1st goblin
        Human h = new Human(60, 3);
        int goblinhp = 40; //initial goblin hp
        int goblinstr = 1; //initial goblin strength
        Goblin g = new Goblin(goblinhp, goblinstr);
        String[][] board = board(); //the board
        ArrayList<Item> potentialdrops = potentialDrops(); //potential drops from enemies/chests
        ArrayList<Item> itemsonboard = new ArrayList<>(); //drops from enemies
        ArrayList<Item> chest = new ArrayList<>(); //chest with 2 random items in it after enemy dies
        boolean goblinalive = true; //is goblin alive
        boolean chestthere = false; //is chest there
        boolean humandead = false; //determine if human is dead, and whether the player would like to start again
        int row = 0; //row col for chest items
        int col = 0;

        Scanner invScan = new Scanner(System.in); //scanner for inventory
        Scanner s = new Scanner(System.in); //scanner for quitting
        boolean quitcheck = false;//will change to true if user wants to quit
        printGame(g, h, board, itemsonboard, row, col, goblinalive, chestthere);//initially print the game

        //get input from the user on what they want to do
        while (true){
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
                    board[h.getRow()][h.getCol()] = "*"; //resets the board
                    if (h.getRow() - 1 == g.getRow() && h.getCol() == g.getCol()) {//checks to see if would hit goblin
                        System.out.println(combat(h, g, true));
                    } else { //if not, move instead of attacking
                        h.setRow(h.getRow() - 1);
                    }
                    break;
                case "a": //move left
                    board[h.getRow()][h.getCol()] = "*";
                    if (h.getCol() - 1 == g.getCol() && h.getRow() == g.getRow()) {
                        System.out.println(combat(h, g, true));
                    } else {
                        h.setCol(h.getCol() - 1);
                    }
                    break;
                case "d": //move right
                    board[h.getRow()][h.getCol()] = "*";
                    if (h.getCol() + 1 == g.getCol() && h.getRow() == g.getRow()) {
                        System.out.println(combat(h, g, true));

                    } else {
                        h.setCol(h.getCol() + 1);
                    }
                    break;
                case "s": //move down
                    board[h.getRow()][h.getCol()] = "*";
                    if (h.getRow() + 1 == g.getRow() && h.getCol() == g.getCol()) {
                        System.out.println(combat(h, g, true));

                    } else {
                        h.setRow(h.getRow() + 1);
                    }
                    break;
                case "r":
                    //if r, use potion
                    //if player does not have potion, don't use it and don't use up player's turn
                    h.usePotion();
                    break;
                case "i":
                    //if i, open inventory
                    inventory(h, invScan);
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
            if (g.getHealth() == 0 && goblinalive){ //if goblin died, make him drop a random droppable where he was, and make him disappear
                Random rand = new Random();
                int i = rand.nextInt(potentialdrops.size());
                Item drop = potentialdrops.get(i);
                potentialdrops = removeFromDrops(potentialdrops, i); //remove the item from potential drops unless potion
                drop.setCol(g.getCol());
                drop.setRow(g.getRow());
                itemsonboard.add(drop);
                System.out.println("Goblin dropped " + drop.getName() + "!");
                g.setCol(0);
                g.setRow(0);
                goblinalive=false;
                //spawns chest, add 2 random items to it
                i = rand.nextInt(potentialdrops.size());
                Item chestdrop = potentialdrops.get(i);
                do {
                    row = rand.nextInt(board.length-2)+1;
                    col = rand.nextInt(board[0].length-2)+1;

                }while (row ==h.getRow() && col == h.getCol());
                chestdrop.setRow(row);
                chestdrop.setCol(col);
                chest.add(chestdrop);
                potentialdrops = removeFromDrops(potentialdrops, i); //remove the item from potential drops unless potion
                i = rand.nextInt(potentialdrops.size());
                chestdrop = potentialdrops.get(i);
                chestdrop.setRow(row);
                chestdrop.setCol(col);
                chest.add(chestdrop);
                potentialdrops = removeFromDrops(potentialdrops, i); //remove the item from potential drops unless potion
                chestthere = true;
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
            if (chestthere){ //chest is out there
                if (chest.get(0).getCol() == h.getCol() && chest.get(0).getRow() == h.getRow()){ //if human is standing on chest
                    //loot the chest
                    ArrayList<Item> temp = new ArrayList<>(); //temp "chest" storing everything that isn't looted
                    for (int i = 0; i < chest.size(); i++){
                        Scanner sc1= new Scanner(System.in);
                        if (!h.loot(chest.get(i),sc1, chest)){ //if looted, remove item from chest
                            temp.add(chest.get(i));
                        }else{
                            i--;
                        }
                    }
                    //take out the chest
                    potentialdrops.addAll(temp);
                    chest.clear();
                    chestthere = false;
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

                //if r, use potion
                    //if player does not have potion, don't use it and don't use up player's turn
            //goblin then moves towards user
                //check to be sure combat isn't initiated
            //repeat last 2 main things
            printGame(g, h, board, itemsonboard, row, col, goblinalive, chestthere);
        }

        if (humandead){
            //ask if they want to play again
            if(yes(s))
               playGame(scanner);

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
    public String combat(Human human, Goblin goblin, boolean humaninitcom){
        Random r = new Random();
        if (humaninitcom){ //if human started, they attack first
            humanAttack(human, goblin, r);

            if (goblin.getHealth()>0) //if goblin alive, counterattack
                goblinAttack(human, goblin, r);

        }else{ //goblin attacked first
            goblinAttack(human, goblin, r);

            if (human.getHealth()>0) //if human alive, counterattack
                humanAttack(human, goblin, r);
        }

        if (human.getHealth()==0) return "human has died";
        else if(goblin.getHealth() == 0) return "goblin has died";
        else return "";

    }

    //human attacks goblin
    public void humanAttack(Human human, Goblin goblin, Random r){
        int humanattack = r.nextInt((human.getStrength()+3)-(human.getStrength()-2))+human.getStrength()-2; //a random, +/- 2 for the human attack
        goblin.setHealth(goblin.getHealth()- humanattack); //does the damage
        if (goblin.getHealth()<0) //if below zero hp, set to 0
            goblin.setHealth(0);
        System.out.println("Human attacks goblin for " + humanattack +" damage. Goblin HP = " + goblin.getHealth());
    }

    //goblin attacks human
    public void goblinAttack(Human human, Goblin goblin, Random r){
        int goblinattack = r.nextInt((goblin.getStrength()+3)-(goblin.getStrength()-2))+goblin.getStrength()-2-human.getDefense();
        if (goblinattack <=0){ //goblin misses
            System.out.println("Goblin attack misses.");
        }else{ //goblin hits
            human.setHealth(human.getHealth()-goblinattack);
            if (human.getHealth()<0)
                human.setHealth(0);
            System.out.println("Goblin attacks human for " + goblinattack +" damage. Human HP = " + human.getHealth());
        }
    }

    //deals with the inventory system
    public void inventory(Human human, Scanner s){

        //should they select an item, give it it's type, name, and stats. Then ask whether they want to equip/unequip that item
        human.printinventory();
        String input = "";
        int num = -1;

        //ask what item they wish to select or press q to exit
        do {
            try {
                System.out.println("Select Item number or press q to exit inventory");
                input = s.nextLine();

            } catch (Exception e) {
                System.out.println("Invalid input");
            }
            input = input.toLowerCase();
            //checks if q or a number between 1 and maxinvntory slots.
            if (!input.equals("q")){
                try {
                    num = Integer.parseInt(input);
                    if (num < 0 || num>human.maxinventory){
                        System.out.println("Invalid input");
                    }
                }catch (Exception e) {
                    System.out.println("Invalid input.");
                }
            }
        }while(!input.equals("q") && !(num > 0 && num<=human.maxinventory));

        //if selected an item, show item info and decide whether to equip or unequip the item, depending on where it's located
        if (num>0 && num<=human.maxinventory){
            System.out.println("Type: "+ human.inventory.get(num-1).getType());
            System.out.println("Name: "+ human.inventory.get(num-1).getName());
            if (human.inventory.get(num-1).getType().equals("Weapon"))
                System.out.println("Damage: "+ human.inventory.get(num-1).getStat());
            else System.out.println("Defense: "+ human.inventory.get(num-1).getStat());

            if (num <6){ //selected an equipped item
                System.out.println("u: unequip q: back");
                while (!s.hasNext("[uq]")) { //This line ensures that the user inputed y or n, if not, it'll continue to ask for the right input
                    System.out.println("Not valid input, please input 'u' or 'q'");
                    s.next();
                }
                String answer = s.next();
                if (answer.equals("u")){ //unequip item
                    human.unequip(num-1);
                }
            }else{ //selected an unequipped item
                System.out.println("e: equip q: back d: drop");
                while (!s.hasNext("[eqd]")) { //This line ensures that the user inputed y or n, if not, it'll continue to ask for the right input
                    System.out.println("Not valid input, please input 'e', 'd', or 'q'");
                    s.next();
                }
                String answer = s.next();
                switch (answer){
                    case "e":
                        human.equip(num-1);
                        break;
                    case "d":
                        human.drop(num-1);
                        break;
                    default:
                        break;
                }
            }
        }
        if (!input.equals("q")){ //so long as not q, stay in inventory
            inventory(human, s);
        }

    }


}
