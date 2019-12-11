package day11;

import day3.Point;
import day5.Computer;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Robot {
    private Computer controller;        //The intcode computer that controls the robot
    private Point position;             //The (x,y) position of the robot in the plane
    private Set<Point> visitedPoints;   //The set of all visited points
    private Set<Point> whitePoints;     //The set of all points painted in white
    private int orientation;            //The current heading of the robot
    private RobotStatus status;

    public enum RobotStatus {
        RUNNING,
        TERMINATED,
        ERROR
    }

    private static final int DIRECTION_UP = 0;
    private static final int DIRECTION_RIGHT = 1;
    private static final int DIRECTION_DOWN = 2;
    private static final int DIRECTION_LEFT = 3;

    public Robot(List<Long> intcode, Set<Point> startingCondition) {
        position = new Point(0,0);
        controller = new Computer(intcode);
        visitedPoints = new HashSet<>();
        visitedPoints.add(position);
        whitePoints = startingCondition;
        orientation = DIRECTION_UP;
        status = RobotStatus.RUNNING;
    }

    private void rotateLeft() {
        orientation = Math.floorMod(orientation - 1, 4);
    }

    private void rotateRight() {
        orientation = Math.floorMod(orientation + 1, 4);
    }

    private void moveForward() {
        int x_next = position.getX();
        int y_next = position.getY();
        switch (orientation) {
            case DIRECTION_UP:
                y_next -= 1;
                break;
            case DIRECTION_DOWN:
                y_next += 1;
                break;
            case DIRECTION_LEFT:
                x_next -= 1;
                break;
            case DIRECTION_RIGHT:
                x_next += 1;
                break;
            default:
                System.out.println(orientation);
        }
        position = new Point(x_next,y_next);

    }

    public void step() {
        controller.addInput(whitePoints.contains(position) ? 1L : 0L);      //Read the color of the cell where the robot is standing, and add it to the controller input queue
        visitedPoints.add(position);                                        //Add the current position to the visited positons
        while(controller.getStatus() == Computer.StatusCode.RUNNING) {
            controller.step();                                              //Execute controller code until it requires more input
        }
        if(controller.getStatus() == Computer.StatusCode.WAIT_INPUT) {
            Optional<Long> optColor = controller.getFirstOutput();          //Get the first output in the output queue, that's the new color of the current cell
            Optional<Long> optRotate = controller.getFirstOutput();         //Get the second output in the output queue, that's the rotation that the robot has to do
            if(optColor.isPresent() && optRotate.isPresent()) {
                if(optColor.get() == 1L) {
                    whitePoints.add(position);
                }
                else if(optColor.get() == 0L){
                    whitePoints.remove(position);
                }
                else {
                    System.out.println("CONTROLLER COLOR OUTPUT IS NOT 0 or 1");
                    status = RobotStatus.ERROR;
                    return;
                }
                if(optRotate.get() == 1L){
                    rotateLeft();
                }
                else if(optRotate.get() == 0L) {
                    rotateRight();
                }
                else {
                    System.out.println("CONTROLLER ROTATION OUTPUT IS NOT 0 or 1");
                    status = RobotStatus.ERROR;
                    return;
                }
                moveForward();                                              //Move the robot forward in his new direction
                status = RobotStatus.RUNNING;                               //The robot continues to RUN his code, set hiss status accordingly
            }
            else
            {
                System.out.println("EXPECTED CONTROLLER OUTPUT BUT INSTEAD GOT INPUT REQUEST");
                status = RobotStatus.ERROR;
                return;
            }
        }
        else if(controller.getStatus() == Computer.StatusCode.TERMINATED) status = RobotStatus.TERMINATED; //If the controller reaches 99 the robot has terminated his job
        else status = RobotStatus.ERROR;
    }

    public RobotStatus getStatus() {
        return status;
    }

    public int getVisitedSize() {
        return visitedPoints.size();
    }

    public Set<Point> getWhitePoints() {
        return new HashSet<>(whitePoints);
    }

}
