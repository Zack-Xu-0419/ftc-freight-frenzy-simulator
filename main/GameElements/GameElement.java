package GameElements;

public class GameElement {

    protected final int weight;
    protected int x;
    protected int y;

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

}
