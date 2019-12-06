package day3;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        List<Line> rope1, rope2;
        try {
            BufferedReader br = new BufferedReader(new FileReader("/home/dario/Desktop/PROGETTI/advent-of-code/day3/3.in")); //Open file
            rope1 = parseRope(br.readLine());
            rope2 = parseRope(br.readLine());
            firstStatement(rope1, rope2);
            secondStatement(rope1, rope2);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public static void firstStatement(List<Line> rope1, List<Line> rope2) {
        Point min = compareRopes(rope1, rope2);
        System.out.printf("\nManhattan Distance is: %d",min.manhattanDistance(Point.center)); //Print the requested number
    }


    public static List<Line> parseRope(String input) {

        List<Direction> directions = Arrays.stream(input.split(",")).map(s -> Direction.getDirection(s.charAt(0))).collect(Collectors.toList());
        List<Integer> lengths = Arrays.stream(input.split(",")).map(s -> s.substring(1)).map(Integer::parseInt).collect(Collectors.toList());
        List<Line> rope = new ArrayList<>();

        assert directions.size() == lengths.size();

        Point lastPoint = new Point(0,0);
        for(int i = 0; i < directions.size(); i++) {
            Line newLine = new Line(lastPoint, directions.get(i), lengths.get(i));
            lastPoint = newLine.getNWPoint().equals(lastPoint) ? newLine.getSEPoint() : newLine.getNWPoint();
            rope.add(newLine);
        }

        return rope;
    }

    public static Point compareRopes(List<Line> r1, List<Line> r2) {
        Point nearestPoint = new Point(Integer.MAX_VALUE / 3, Integer.MAX_VALUE / 3);
        for (Line l1 : r1) {
            for (Line l2 : r2) {
                nearestPoint = Stream.concat(Line.intersections(l1, l2).stream(), Stream.of(nearestPoint))
                        .filter(p -> !p.equals(Point.center))
                        .min(Comparator.comparingInt(x -> x.manhattanDistance(Point.center)))
                        .get();
            }
        }
        return nearestPoint;
    }

    public static void secondStatement(List<Line> r1, List<Line> r2) {
        ArrayList<Point> plist = new ArrayList<>();
        int distance1 = 0;
        int distance2 = 0;
        int minCumulative = Integer.MAX_VALUE;
        Point lastPoint1 = new Point(0,0);
        final AtomicReference<Point> reference1 = new AtomicReference<>();
        final AtomicReference<Point> reference2 = new AtomicReference<>(); //References for non final variables in lambda
        for (Line l1 : r1) {

            Point lastPoint2 = new Point(0,0);
            distance2 = 0;
            for (Line l2 : r2) {
                reference1.set(lastPoint1);
                reference2.set(lastPoint2);
                Optional<Point> nearestOpt = Line.intersections(l1,l2).stream()
                        .filter(p -> !p.equals(Point.center))
                        .min(Comparator.comparingInt(p -> p.manhattanDistance(reference1.get()) + p.manhattanDistance(reference2.get())));
                if(nearestOpt.isPresent()) {
                    Point nearest = nearestOpt.get();
                    minCumulative = Math.min(minCumulative, distance1 + distance2 + nearest.manhattanDistance(lastPoint1) + nearest.manhattanDistance(lastPoint2));

                }
                distance2 += l2.getLength();
                lastPoint2 = l2.getNWPoint().equals(lastPoint2) ? l2.getSEPoint() : l2.getNWPoint();

            }
            distance1 += l1.getLength();
            lastPoint1 = l1.getNWPoint().equals(lastPoint1) ? l1.getSEPoint() : l1.getNWPoint();
        }
        System.out.printf("\nMinimun wire distance: %d",minCumulative);
    }

}
