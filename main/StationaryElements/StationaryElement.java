package StationaryElements;

import GameElements.GameElement;

import java.util.ArrayList;

public class StationaryElement {

    public ArrayList<ArrayList<GameElement>> elements = new ArrayList<>();
    //recored the position of the element
    protected int x;
    protected int y;

    protected StationaryElement(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void addElement(int num, GameElement element) {
        elements.get(num).add(element);
    }
}
