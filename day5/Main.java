package day5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        try{
            BufferedReader br = new BufferedReader(new FileReader("/home/dario/Desktop/PROGETTI/advent-of-code/day5/5.in")); //Open file
            List<Integer> list = Arrays.stream(br.readLine().split(","))
                    .map(s -> Integer.parseInt(s))
                    .collect(Collectors.toList());
            Computer comp = new Computer(list);
            System.out.println("FIRST STATEMENT");
            firstStatement(comp);
            System.out.println("SECOND STATEMENT");
            comp.resetComputer();
            secondStatement(comp);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void firstStatement(Computer comp) {
        comp.addInput(1);
        while(comp.getStatus() == Computer.StatusCode.RUNNING) {
            comp.step();
            comp.getFirstOutput().ifPresent(System.out::println);
        }
        System.out.println(comp.getStatus().toString());
    }

    public static void secondStatement(Computer comp) {
        comp.addInput(5);
        while(comp.getStatus() == Computer.StatusCode.RUNNING) {
            comp.step();
            comp.getFirstOutput().ifPresent(System.out::println);
        }
        System.out.println(comp.getStatus().toString());
    }
}
