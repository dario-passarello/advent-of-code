package day7;

import java.util.List;

@FunctionalInterface
public interface AmplifierTester {
    long executeTest(List<Long> memory, int[] permutation);
}
