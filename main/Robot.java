public class Robot {
    private int position[] = { 0, 0 };
    private int sizeX = 1; // Pixel Size from center of the robot to the edge
    private int sizeY = 1; // Pixel Size from center of the robot to the edge
    private int orientation = 0; // Degree of rotation. 0 means Slide is facing to the other alliance

    public boolean move(int amount, int direction) {
        // Return true if the robot can be moved in a certain direction
        return true;
    }

    public int[] getPosition() {
        return position;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getOrientation() {
        return orientation;
    }
}
