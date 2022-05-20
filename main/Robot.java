
public class Robot {
    private int position[] = { 0, 0 };
    private int sizeX = 50; // Pixel Size from center of the robot to the edge
    private int sizeY = 50; // Pixel Size from center of the robot to the edge
    private int orientation = 0; // Degree of rotation. 0 means Slide is facing to the other alliance
    private int speed = 5;
    private int maxSlideLength = 400;
    private int currentSlideLength = 0;
    private int oldX = 0;
    private int oldY = 0;
    public boolean slideExtended = false;
    public boolean intakeDownLeft = false;
    public boolean intakeDownRight = false;
    public boolean hasGameElement = false;

    Robot() {

    }

    public void setPosition(int positionX, int positionY) {
        this.position[0] = positionX;
        this.position[1] = positionY;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public int getMaxSlideLength() {
        return maxSlideLength;
    }

    public int getCurrentSlideLength() {
        return currentSlideLength;
    }

    public void setCurrentSlideLength(int newSlideLength) {
        currentSlideLength = newSlideLength;
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

    public int[] getPositionAtSlideEnd() {
        int[] result = {
                (int) (this.getPosition()[0] - Math.sin(Math.toRadians(orientation)) * (currentSlideLength + this.sizeX)),
                (int) (this.getPosition()[1] + Math.cos(Math.toRadians(orientation)) * (currentSlideLength + this.sizeY)) };
        return result;
    }

    public boolean move(int direction) {

        speed = 5;
        if (position[0] > 100 - sizeX && position[0] < 800 + sizeX) {
            if (position[1] > 275 - sizeY && position[1] < 325 + sizeY) {
                speed = 1;
            }
        }

        if ((position[0] > 275 - sizeX && position[0] < 325 + sizeX)
                || (position[0] > 575 - sizeX && position[0] < 625 + sizeX)) {
            if (position[1] > 100 - sizeY && position[1] < 275 + sizeY) {
                speed = 1;
            }
        }

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

        int X = position[0] - sizeX;
        int Y = position[1] + sizeY;
        if (
        // If collide with carosel
        (X < (int) (75. / 2 * Math.sqrt(2))
                && Y > (int) (900 - 75. / 2 * Math.sqrt(2)))
                ||
                // If collide with Shared shipping hub
                (X > 300 && X < 500
                        && Y > 100 && Y < 300)
                ||
                // If collide with Alliance shipping hub
                (X > 150 && X < 350
                        && Y > 475 && Y < 675)

        ) {
            position[0] = oldX;
            position[1] = oldY;
        }
        oldX = position[0];
        oldY = position[1];

        switch (direction) {
            case 1:
                if (orientation % 90 == 0) {
                    if ((orientation / 90) % 2 == 0) {
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
                        if (position[0] - sizeY < 1) {
                            // orientation 0 and is less than 1 cm to the wall.
                            return false;
                        } else {
                            // There is more than 1 pixel gap
                            this.position[0] -= speed;
                            return true;
                        }
                    }
                } else {
                    // If robot is rotated
                    if (sizeX <= sizeY) {
                        if (leftMostX < 10) {
                            rotateAgainstWall(direction);
                            System.out.println("LeftReached");
                            return false;
                        } else {
                            this.position[0] -= speed;
                        }
                    } else {
                        if (leftMostX < 0) {
                            rotateAgainstWall(direction);
                            System.out.println("LeftReached");
                            return false;
                        } else {
                            this.position[0] -= speed;
                        }
                    }
                }
                break;
            case 2:
                if (orientation % 90 == 0) {
                    if ((orientation / 90) % 2 == 0) {
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
                        // If robot is not rotated(therefore we don't need trigonometry)
                        // 900 is the screen size
                        if (900 - position[0] - sizeY < 1) {
                            // orientation 0 and is less than 1 cm to the wall.return false;
                            return false;
                        } else {
                            // There is more than 1 pixel gap
                            this.position[0] += speed;
                            return true;
                        }
                    }
                } else {
                    // If robot is rotated
                    if (rightMostX > 900) {
                        rotateAgainstWall(direction);
                        return false;
                    } else {
                        System.out.println(position[0]);
                        if (sizeX <= sizeY) {
                            // If robot is rotated
                            if (rightMostX > 900 - 10) {
                                rotateAgainstWall(direction);
                                return false;
                            } else {
                                System.out.println("MovingRight");
                                this.position[0] += speed;
                            }
                        } else {
                            if (rightMostX > 900 + 10) {
                                rotateAgainstWall(direction);
                                return false;
                            } else {
                                System.out.println("MovingRight");
                                this.position[0] += speed;
                            }
                        }
                    }
                    break;
                }
            case 3:
                if (orientation % 90 == 0) {
                    if ((orientation / 90) % 2 == 0) {
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
                        // If robot is not rotated(therefore we don't need trigonometry)
                        // 900 is the screen size
                        if (position[1] - sizeX < 1) {
                            // orientation 0 and is less than 1 cm to the wall.
                            return false;
                        } else {
                            // There is more than 1 pixel gap
                            this.position[1] -= speed;
                            return true;
                        }
                    }
                } else {
                    if (sizeX <= sizeY) {
                        // If robot is rotated
                        if (upMostY < 0) {
                            rotateAgainstWall(direction);
                            return false;
                        } else {
                            this.position[1] -= speed;
                        }
                    } else {
                        // If robot is rotated
                        if (upMostY < 10) {
                            rotateAgainstWall(direction);
                            return false;
                        } else {
                            this.position[1] -= speed;
                        }
                    }
                }
                break;
            case 4:
                if (orientation % 90 == 0) {
                    if ((orientation / 90) % 2 == 0) {
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
                        // If robot is not rotated(therefore we don't need trigonometry)
                        // 900 is the screen size
                        if (900 - position[1] - sizeX < 1) {
                            // orientation 0 and is less than 1 cm to the wall.
                            return false;
                        } else {
                            // There is more than 1 pixel gap
                            this.position[1] += speed;
                            return true;
                        }
                    }
                } else {
                    if (sizeX <= sizeY) {
                        // If robot is rotated
                        if (downMostY > 900) {
                            rotateAgainstWall(direction);
                            return false;
                        } else {
                            System.out.println(downMostY);
                            this.position[1] += speed;
                        }
                    } else {
                        // If robot is rotated
                        if (downMostY > 900 - 10) {
                            rotateAgainstWall(direction);
                            return false;
                        } else {
                            System.out.println(downMostY);
                            this.position[1] += speed;
                        }
                    }
                }
            default:
                return false;
        }

        // barrer

        return false;
    }

    public boolean rotateAgainstWall(int direction) {
        if (this.orientation % 90 > 45) {
            if (orientation % 90 != 0) {
                setOrientation(orientation + 1);
            }
        } else {
            if (orientation % 90 != 0) {
                setOrientation(orientation - 1);
            }
        }
        this.move(direction);
        return true;
    }
}
