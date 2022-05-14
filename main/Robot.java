public class Robot {
    private int position[] = { 0, 0 };
    private int sizeX = 50; // Pixel Size from center of the robot to the edge
    private int sizeY = 50; // Pixel Size from center of the robot to the edge
    private int orientation = 0; // Degree of rotation. 0 means Slide is facing to the other alliance
    private int speed = 5;

    Robot() {

    }

    public void setPosition(int positionX, int positionY) {
        this.position[0] = positionX;
        this.position[1] = positionY;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
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

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public void setSize(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public boolean move(int direction) {

        // The input 'direction': 1-left, 2-right, 3-up, 4-down

        // Return 0 if robot is completely against the wall
        // Return the degree if robot is in some rotation and one corner of it is
        // against the wall
        // Return -1 if robot is not touching the wall in any way.

        // Calculate "Bounding Box"
        double leftMostX = position[0]
                - Math.abs((Math.sqrt(2) * sizeX) * Math.cos(Math.PI / 4 - orientation % 90 * Math.PI / 180));
        double rightMostX = position[0]
                + Math.abs((Math.sqrt(2) * sizeX) * Math.cos(Math.PI / 4 - orientation % 90 * Math.PI / 180));
        double upMostY = position[1]
                - Math.abs((Math.sqrt(2) * sizeY) * Math.cos(Math.PI / 4 - orientation % 90 * Math.PI / 180));
        double downMostY = position[1]
                + Math.abs((Math.sqrt(2) * sizeY) * Math.cos(Math.PI / 4 - orientation % 90 * Math.PI / 180));

        switch (direction) {
            case 1:
                if (orientation % 90 == 0) {
                    // If robot is not rotated(therefore we don't need trigonometry)
                    if (position[0] - sizeX < 1) {
                        // orientation 0 and is less than 1 cm to the wall.
                        return false;
                    } else {
                        // There is more than 1 pixel gap
                        this.position[0] -= speed;
                        return true;
                    }
                } else {
                    // If robot is rotated
                    if (leftMostX < 1) {
                        rotateAgainstWall();
                        System.out.println("LeftReached");
                        return false;
                    } else {
                        this.position[0] -= speed;
                    }
                }
                break;
            case 2:
                if (orientation % 90 == 0) {
                    // If robot is not rotated(therefore we don't need trigonometry)
                    // 900 is the screen size
                    if (900 - position[0] - sizeX < 1) {
                        // orientation 0 and is less than 1 cm to the wall.
                        return false;
                    } else {
                        // There is more than 1 pixel gap
                        this.position[0] += speed;
                        return true;
                    }
                } else {
                    // If robot is rotated
                    if (rightMostX > 900) {
                        rotateAgainstWall();
                        return false;
                    } else {
                        System.out.println("MovingRight");
                        this.position[0] += speed;
                    }
                }
                break;
            case 3:
                if (orientation % 90 < 1) {
                    // If robot is not rotated(therefore we don't need trigonometry)
                    // 900 is the screen size
                    if (position[1] - sizeY < 1) {
                        // orientation 0 and is less than 1 cm to the wall.
                        return false;
                    } else {
                        // There is more than 1 pixel gap
                        this.position[1] -= speed;
                        return true;
                    }
                } else {
                    // If robot is rotated
                    if (upMostY < 1) {
                        rotateAgainstWall();
                        return false;
                    } else {
                        this.position[1] -= speed;
                    }
                }
                break;
            case 4:
                if (orientation % 90 < 1) {
                    // If robot is not rotated(therefore we don't need trigonometry)
                    // 900 is the screen size
                    if (900 - position[1] - sizeY < 1) {
                        // orientation 0 and is less than 1 cm to the wall.
                        return false;
                    } else {
                        // There is more than 1 pixel gap
                        this.position[1] += speed;
                        return true;
                    }
                } else {
                    // If robot is rotated
                    if (downMostY > 900) {
                        rotateAgainstWall();
                        return false;
                    } else {
                        System.out.println(downMostY);
                        this.position[1] += speed;
                    }
                }
            default:
                return false;
        }

        return false;
    }

    public boolean rotateAgainstWall() {
        if (this.orientation % 90 > 45) {
            while (orientation % 90 != 0) {
                this.orientation++;
            }
        } else {
            while (orientation % 90 != 0) {
                this.orientation--;
            }
        }
        this.move(1);
        return true;
    }
}
