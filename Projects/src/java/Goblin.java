public class Goblin {
    private int row;
    private int col;
    private int health;
    private int strength;
    public Goblin (int health, int strength){
        this.row = 2;
        this.col = 2;
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

    public String toString(){
        return "o";
    }

}
