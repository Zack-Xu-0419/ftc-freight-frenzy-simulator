package GameElements;

public class GameElement {

    // All instance variables are protected so that the child class can access them

    protected final int weight;
    protected int x;
    protected int y;
    protected boolean isPickedUp;

    // The constructor is protected so that no one can create an abstract game
    // element
    protected GameElement(int x, int y, int weight) {
        this.x = x;
        this.y = y;
        this.weight = weight;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWeight() {
        return weight;
    }

    // Changes the element's position
    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isPickedUp() {
        return isPickedUp;
    }

    public void setPickedUp(boolean isPickedUp) {
        this.isPickedUp = isPickedUp;
    }

}
