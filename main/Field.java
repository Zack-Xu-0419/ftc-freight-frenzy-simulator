import GameElements.*;
import StationaryElements.*;

public class Field {
    int numOfDuck = 10;
    int numOfCube = 20;
    int numOfBall = 10;
    int x_alliance;
    int y_allianceHub;
    int x_carosel;
    int y_carosel;
    int x_sharedHub;
    int y_sharedHub;
    public Ball[] balls = new Ball[numOfBall];
    public Cube[] cubes = new Cube[numOfCube];
    public Duck[] ducks = new Duck[numOfDuck];
    public AllianceHub allianceHub = new AllianceHub(x_alliance, y_allianceHub);
    public Carosel carosel = new Carosel(x_carosel, y_carosel);
    public SharedHub sharedHub = new SharedHub(x_sharedHub, y_sharedHub);
    private final int size = 100;
    private final int robotAreaSize = 10;
    public Field(){
        for(int i = 0; i < numOfBall; i++){
            int x, y;
            do{
                x = (int)Math.random() * (size - robotAreaSize) + robotAreaSize;
                y = (int)Math.random() * (size - robotAreaSize) + robotAreaSize;
            }while(check(x, y));
            balls[i] = new Ball(x, y);

        }
        
        for(int i = 0; i < numOfCube; i++){
            int x, y;
            do{
                x = (int)Math.random() * (size - robotAreaSize) + robotAreaSize;
                y = (int)Math.random() * (size - robotAreaSize) + robotAreaSize;
            }while(check(x, y));
            //balls[i] = new Ball(x, y);
            
        }
    }
    //check whether this radom point has something on it
    private boolean check(int x, int y){
        if(x < robotAreaSize && y <robotAreaSize) return false;
        if(x == x_alliance && y== y_allianceHub)return false;
        if(x == x_carosel && y == y_carosel)return false;
        if(x == x_sharedHub && y == y_sharedHub)return false;
        for(int i = 0; i < numOfBall && balls[i] != null; i++){
            if(x == balls[i].getX() && y == balls[i].getY()){
                return false;
            }
        }
        for(int i = 0; i < numOfCube && cubes[i] != null; i++){
            if(x == cubes[i].getX() && y == cubes[i].getY()){
                return false;
            }
        }
        for(int i = 0; i < numOfDuck && ducks[i] != null; i++){
            if(x == ducks[i].getX() && y == ducks[i].getY()){
                return false;
            }
        }
        return true;
    }

}
