public class Item {
    private int row;
    private int col;
    private String name;
    String type;
    int stat;

    //empty slot
    public Item(){
        this.name = "";
        this.type = "empty";
        this.stat = 0;
        this.row = 0;
        this.col = 0;
    }

    //actual item
    public Item(String type, String name, int stat){
        this.name = name;
        this.type = type;
        this.stat = stat;
        this.row = 0; //this would put it off the map until changed
        this.col = 0;
    }

    public int getCol() {
        return this.col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return this.row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getStat() {
        return this.stat;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String toString(){
        return "^";
    }
}
