package day7;

import java.util.List;

@FunctionalInterface
public interface AmplifierTester {
    int executeTest(List<Integer> memory, int[] permutation);
}
