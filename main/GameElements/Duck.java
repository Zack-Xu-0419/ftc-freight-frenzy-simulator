package GameElements;

public class Duck extends GameElement{

    // Duck's set weight is 0, used for shared hub balance.
    public Duck(int xPosition, int yPostion){
        super(xPosition, yPostion, 0);
    }
}
