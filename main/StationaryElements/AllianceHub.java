package StationaryElements;

import GameElements.GameElement;

import java.util.ArrayList;

public class AllianceHub extends StationaryElement {

    public ArrayList<GameElement> levelOne = new ArrayList<>();
    public ArrayList<GameElement> levelTwo = new ArrayList<>();
    public ArrayList<GameElement> levelThree = new ArrayList<>();

    public AllianceHub(int x, int y) {
        super(x, y);
        elements.add(levelOne);
        elements.add(levelTwo);
        elements.add(levelThree);
    }

    public void addElement(int level, GameElement element) {
        elements.get(level).add(element);
    }

}
