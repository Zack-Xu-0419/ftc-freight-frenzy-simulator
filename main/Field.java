import GameElements.*;
import StationaryElements.*;

public class Field {
    int numOfDuck = 10;
    int numOfCube = 28;
    int numOfBall = 20;
    // postion of elements
    int x_allianceHub = 100;
    int y_allianceHub = 400;
    int x_carosel;
    int y_carosel;
    int x_sharedHub = 200;
    int y_sharedHub = 500;
    // arrays that used to store all game elements
    public Ball[] balls = new Ball[numOfBall];
    public Cube[] cubes = new Cube[numOfCube];
    public Duck[] ducks = new Duck[numOfDuck];

    public AllianceHub allianceHub = new AllianceHub(x_allianceHub, y_allianceHub);
    public Carosel carosel = new Carosel(x_carosel, y_carosel);
    public SharedHub sharedHub = new SharedHub(x_sharedHub, y_sharedHub);

    // size of the whole field
    public final int size = 900;
    // size of a grid
    public final int robotAreaSize = 150;

    // inistialize all the game element, set radom postion
    public Field() {
        for (int i = 0; i < numOfBall; i++) {
            int x, y;
            do {
                x = (int) (Math.random() * (robotAreaSize - 10));
                y = (int) (Math.random() * (robotAreaSize - 10));
            } while (!check(x, y));
            balls[i] = new Ball(x, y);

        }

        for (int i = 0; i < numOfCube; i++) {
            int x, y;
            do {
                x = (int) (Math.random() * (robotAreaSize - 10));
                y = (int) (Math.random() * (robotAreaSize - 10));
            } while (!check(x, y));
            if (i < 13) {
                cubes[i] = new Cube(x, y, 2);
            } else if (i < 23) {
                cubes[i] = new Cube(x, y, 3);
            } else {
                cubes[i] = new Cube(x, y, 4);
            }

        }

        /*
         * for(int i = 0; i < numOfDuck; i++){
         * int x, y;
         * do{
         * x = (int) (Math.random() * robotAreaSize);
         * y = (int) (Math.random() * robotAreaSize);
         * }while(!check(x, y));
         * ducks[i] = new Duck(x, y);
         * 
         * }
         */
    }

    // check whether this radom point has something on it
    // make sure different elements don't overlap
    private boolean check(int x, int y) {
        for (int i = 0; i < numOfBall && balls[i] != null; i++) {
            if (x > balls[i].getX() - 10 && x < balls[i].getX() + 10 && y > balls[i].getY() - 10
                    && y < balls[i].getY() + 10) {
                return false;
            }
        }
        for (int i = 0; i < numOfCube && cubes[i] != null; i++) {
            if (x > cubes[i].getX() - 10 && x < cubes[i].getX() + 10 && y > cubes[i].getY() - 10
                    && y < cubes[i].getY() + 10) {
                return false;
            }
        }
        /*
         * for(int i = 0; i < numOfDuck && ducks[i] != null; i++){
         * if(x > ducks[i].getX() - 10 && y == ducks[i].getY()){
         * return false;
         * }
         * }
         */
        return true;
    }
}
