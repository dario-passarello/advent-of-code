package day5;


import java.util.*;

public class Computer {
    private int PC;                 //Program counter register
    private int stepCount;          //Number of step() invocations
    private final ArrayList<Integer> ROM;   //Preserves the initial input, for resetting purpose
    private List<Integer> memory;       //Stores the actual state of the instruction and data memory
    private Queue<Integer> stdin;       //Stores the queue of inputs. Whenever an INPUT instruction is called, pops the first value from it
    private Queue<Integer> stdout;      //Same as above but with OUTPUT
    private StatusCode status;          //Stores the actual status code of the COMPUTER
    public enum StatusCode {        //STATUS CODES:
        RUNNING,        //Computer is running normally
        WAIT_INPUT,     //Stdin is empty, awaiting input
        TERMINATED,     //Computer has reached opcode '99'
        ERROR           //Computer has reached an illegal OP code
    }

    public Computer(List<Integer> memory) {
        this.stepCount = 0;
        this.PC = 0;
        this.memory = new ArrayList<>(memory);
        this.ROM = new ArrayList<>(memory);
        this.stdin = new ArrayDeque<>();
        this.stdout = new ArrayDeque<>();
        this.status = StatusCode.RUNNING;
    }

    public void addInput(Integer a) {
        stdin.add(a);
    }

    public void addInput(List<Integer> a) {
        stdin.addAll(a);
    }

    public int readMemory(int address) {
        return memory.get(address);
    }

    public void writeMemory(int address, int value) {
        if(address >= 0 && address < memory.size())
            memory.set(address,value);
        else
            printMemory();


    }

    public StatusCode step() {
        try {
            Fetcher fetcher = new Fetcher(this);
            int[] args = fetcher.getArgs();
            switch (fetcher.getInstruction()) {
                case ADD:
                    writeMemory(args[2], args[0] + args[1]);
                    status = StatusCode.RUNNING;
                    break;
                case MUL:
                    writeMemory(args[2], args[0] * args[1]);
                    status = StatusCode.RUNNING;
                    break;
                case INPUT:
                    if(stdin.isEmpty())
                        status = StatusCode.WAIT_INPUT;
                    else {
                        writeMemory(args[0], stdin.poll());
                        status = StatusCode.RUNNING;
                    }
                    break;
                case OUTPUT:
                    stdout.add(args[0]);
                    status = StatusCode.RUNNING;
                    break;
                case JUMP_IF_TRUE:
                    if(args[0] != 0)
                        PC = args[1] - fetcher.getInstruction().getNumOfArgs() - 1;
                    status = StatusCode.RUNNING;
                    break;
                case JUMP_IF_FALSE:
                    if(args[0] == 0)
                        PC = args[1] - fetcher.getInstruction().getNumOfArgs() - 1;
                    status = StatusCode.RUNNING;
                    break;
                case LESS_THAN:
                    if(args[0] < args[1])
                        writeMemory(args[2], 1);
                    else
                        writeMemory(args[2], 0);
                    status = StatusCode.RUNNING;
                    break;
                case EQUALS:
                    if(args[0] == args[1])
                        writeMemory(args[2], 1);
                    else
                        writeMemory(args[2], 0);
                    status = StatusCode.RUNNING;
                    break;
                case TERMINATE:
                    status = StatusCode.TERMINATED;
                    break;
                default:
                    status = StatusCode.ERROR;
                    break;
            }
            if(status == StatusCode.RUNNING) PC += fetcher.getInstruction().getNumOfArgs() + 1;
            if(status == StatusCode.ERROR)
                System.out.printf("Step: %d > ERRORE\n",stepCount);
            stepCount++;
            return status;
        }
        catch (IllegalFormatException ife) {
            ife.printStackTrace();
            status = StatusCode.ERROR;
        }
        return this.status;
    }

    public Optional<Integer> getFirstOutput() {
        if(stdout.isEmpty()) return Optional.empty();
        else return Optional.of(stdout.poll());
    }

    public int getPC() {
        return PC;
    }

    public StatusCode getStatus() {
        return this.status;
    }

    public void printMemory() {
        for (Integer integer : memory) {
            System.out.printf("%d, ", integer);
        }
    }

    public void resetComputer() {
        PC = 0;
        memory = new ArrayList<>(ROM);
        status = StatusCode.RUNNING;
        stdin = new ArrayDeque<>();
        stdout = new ArrayDeque<>();
        stepCount = 0;
    }



}
