package day11;

import day3.Point;
import day5.Computer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        try{
            BufferedReader br = new BufferedReader(new FileReader("/home/dario/Desktop/PROGETTI/advent-of-code/day11/11.in")); //Open file
            List<Long> list = Arrays.stream(br.readLine().split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            System.out.println("FIRST STATEMENT");
            firstStatement(list);
            System.out.println("SECOND STATEMENT");
            secondStatement(list);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    public static void firstStatement(List<Long> code) {
        Robot r = new Robot(code, new HashSet<>());
        while(r.getStatus() == Robot.RobotStatus.RUNNING) {
            r.step();
        }
        System.out.println(r.getVisitedSize());
    }
    public static void secondStatement(List<Long> code) {
        HashSet<Point> start = new HashSet<>();
        start.add(new Point(0,0));  //Sets (0,0) to white
        Robot r = new Robot(code, start);
        while(r.getStatus() == Robot.RobotStatus.RUNNING) {
            r.step();
        }
        drawSet(r.getWhitePoints());

    }

    public static void drawSet(Set<Point> sp){
        //Find boundaries of the robot paint job
        int min_x = sp.stream().min(Comparator.comparingInt(Point::getX)).get().getX();
        int max_x = sp.stream().max(Comparator.comparingInt(Point::getX)).get().getX();
        int min_y = sp.stream().min(Comparator.comparingInt(Point::getY)).get().getY();
        int max_y = sp.stream().max(Comparator.comparingInt(Point::getY)).get().getY();
        //Print the result inside that boundaries
        for(int y = min_y; y <= max_y;y++) {
            System.out.println("");
             for(int x = max_x; x >= min_x; x--) {
                System.out.print(sp.contains(new Point(x,y)) ? "█" : " ");
            }
        }
    }
}
