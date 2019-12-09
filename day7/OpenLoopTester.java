package day7;

import day5.Computer;

import java.util.List;
import java.util.Optional;

public class OpenLoopTester implements AmplifierTester {
    public long executeTest(List<Long> memory, int[] permutation) {
        Computer computer = new Computer(memory);
        long lastSignal = 0;
        for(int i = 0; i < permutation.length; i++) {
            computer.addInput((long) permutation[i]);
            computer.addInput(lastSignal);
            while(computer.getStatus() == Computer.StatusCode.RUNNING) {
                computer.step();

            }
            assert computer.getStdoutSize() == 1;
            Optional<Long> opt = computer.getFirstOutput();
            if(opt.isPresent())
                lastSignal = opt.get();
            else
                System.out.println("NO OUTPUT");
            computer.resetComputer();
        }
        return lastSignal;
    }
}
