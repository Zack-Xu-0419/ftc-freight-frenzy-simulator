package StationaryElements;

import GameElements.GameElement;

import java.util.ArrayList;

public class SharedHub extends StationaryElement {

    public ArrayList<GameElement> red = new ArrayList<>();
    public ArrayList<GameElement> blue = new ArrayList<>();

    public SharedHub(int x, int y) {
        super(x, y);
        elements.add(red);
        elements.add(blue);
    }

    // SideNum Documentation:
    // 0: Red Side, 1: Blue Side
    public void addElement(int sideNum, GameElement element) {
        elements.get(sideNum).add(element);
    }

    public int getTotalWeight(ArrayList<GameElement> elements) {
        int totalWeight = 0;
        for (int i = 0; i < elements.size(); i++) {
            totalWeight += elements.get(i).getWeight();
        }
        return totalWeight;
    }

}
