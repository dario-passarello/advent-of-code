package day7;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        try{
            BufferedReader br = new BufferedReader(new FileReader("/home/dario/Desktop/PROGETTI/advent-of-code/day7/7.in")); //Open file
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
    public static void firstStatement(List<Long> memory) {
        int[] a = {0,1,2,3,4};
        AmplifierTester openLoopTester = new OpenLoopTester();
        System.out.println(maxSignal(openLoopTester, memory, a, 5,5));
    }


    public static void secondStatement(List<Long> memory) {
        int[] a = {5,6,7,8,9};
        AmplifierTester closedLoopTest = new ClosedLoopTester();
        System.out.println(maxSignal(closedLoopTest, memory, a, 5,5));
    }


    /**
     * @
     */
    public static long maxSignal(AmplifierTester tester, List<Long> memory, int[] a, int size, int n) {
        if(size == 1) {
            return tester.executeTest(memory, a);
        }
        long max = Long.MIN_VALUE;
        for(int i = 0; i < size; i++) {

            long sig = maxSignal(tester, memory, a, size - 1, n);
            if(sig > max) max = sig;
            if(size % 2 == 1) {
                int temp = a[0];
                a[0] = a[size - 1];
                a[size - 1] = temp;
            }
            else
            {
                int temp = a[i];
                a[i] = a[size - 1];
                a[size - 1] = temp;
            }
        }
        return max;
    }


















}
