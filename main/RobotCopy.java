public class RobotCopy extends Robot {
    private int position[] = { 0, 0 };
    private int sizeX = 1; // Pixel Size from center of the robot to the edge
    private int sizeY = 1; // Pixel Size from center of the robot to the edge
    private int orientation = 0; // Degree of rotation. 0 means Slide is facing to the other alliance

    public boolean move(int amount, int direction) {
        // Return true if the robot can be moved in a certain direction
        return true;
    }

    public RobotCopy() {

    }

    public void setPosition(int x, int y) {
        position[0] = x;
        position[1] = y;
    }

    public void setSize(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
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

    public int collisionInfo(int direction) {

        // The input 'direction': 1-left, 2-right, 3-up, 4-down

        // Return 0 if robot is completely against the wall
        // Return the degree if robot is in some rotation and one corner of it is
        // against the wall
        // Return -1 if robot is not touching the wall in any way.

        if (orientation == 0) {
            // If robot is not rotated(therefore we don't need trigonometry)
            if (position[0] - sizeX < 1) {
                // orientation 0 and is less than 1 cm to the wall.
                return 0;
            } else {
                // There is more than 1 pixel gap
                return -1;
            }
        }

        // If there is any rotation, we would need to do some trigonometry to come up
        // with the 'bounding box'

        double leftMostX = position[0]
                - Math.abs((Math.sqrt(2) * sizeX) * Math.cos(Math.PI / 4 - orientation * Math.PI / 180));
        double rightMostX = position[0]
                + Math.abs((Math.sqrt(2) * sizeX) * Math.cos(Math.PI / 4 - orientation * Math.PI / 180));
        double upMostY = position[1]
                - Math.abs((Math.sqrt(2) * sizeY) * Math.cos(Math.PI / 4 - orientation * Math.PI / 180));
        double downMostY = position[1]
                + Math.abs((Math.sqrt(2) * sizeY) * Math.cos(Math.PI / 4 - orientation * Math.PI / 180));

        return 150;
    }
}

