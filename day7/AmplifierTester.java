package day7;

import java.util.List;

@FunctionalInterface
public interface AmplifierTester {
    int executeComputer(List<Integer> memory, int[] permutation);
}
