package day7;

import day5.Computer;

import java.util.List;
import java.util.Optional;

public class ClosedLoopTester implements AmplifierTester {
    public int executeComputer(List<Integer> memory, int[] permutation) {
        Computer[] loop = new Computer[permutation.length];
        //Configure Phase settings for all amplifiers
        for(int i = 0; i < loop.length; i++) {
            loop[i] = new Computer(memory);
            loop[i].addInput(permutation[i]);
            loop[i].step(); //Apply phase configuration
        }
        int signalValue = 0;
        while(loop[loop.length - 1].getStatus() == Computer.StatusCode.RUNNING) {
            for(int i = 0; i < loop.length; i++) {
                loop[i].addInput(signalValue);
                while(loop[i].getStdoutSize() == 0 && loop[i].getStatus() == Computer.StatusCode.RUNNING) {
                    loop[i].step();
                }
                Optional<Integer> opt = loop[i].getFirstOutput();
                if(opt.isPresent())
                    signalValue = opt.get();
            }
        }
        return signalValue;
    }
}
