package StationaryElements;

public class Carosel extends StationaryElement {

    private int numDuck = 10;

    public Carosel(int x, int y) {
        super(x, y);
    }

    public void removeDuck() {
        if (numDuck > 0) {
            numDuck--;
        }
    }

    public int getDucks() {
        return numDuck;
    }

}
