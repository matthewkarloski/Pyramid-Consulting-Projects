import java.util.Scanner;

public class RunHumanVSGoblin {
    public static void main(String[] args){
        HumansVSGoblins test = new HumansVSGoblins();
        /*Human h = new Human(60, 3);
        Goblin g = new Goblin(60, 1);
        test.printGame(g,h, test.board(), 1, 1, true, true);
        test.combat(h,g,true);
        Item sword = new Item("sword", "Bane of Apocolypse", 20);
        Item helm = new Item("helmet", "Big Brain", 5);
        System.out.println(h.addItemToInventory(sword));
        System.out.println(h.addItemToInventory(helm));
        h.printinventory();
         */
        Scanner s = new Scanner(System.in);
        test.playGame(s);
        s.close();
    }
}
