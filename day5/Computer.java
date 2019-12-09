package day5;


import java.util.*;

public class Computer {
    private int PC;                 //Program counter register
    private int RB;                 //Relative Base Register
    private int stepCount;          //Number of step() invocations
    private final List<Long>  ROM;   //Preserves the initial input, for resetting purpose
    private Map<Integer,Long> memory;       //Stores the actual state of the instruction and data memory
    private Queue<Long> stdin;       //Stores the queue of inputs. Whenever an INPUT instruction is called, pops the first value from it
    private Queue<Long> stdout;      //Same as above but with OUTPUT
    private StatusCode status;          //Stores the actual status code of the COMPUTER
    public enum StatusCode {        //STATUS CODES:
        RUNNING,        //Computer is running normally
        WAIT_INPUT,     //Stdin is empty, awaiting input
        TERMINATED,     //Computer has reached opcode '99'
        ERROR           //Computer has reached an illegal OP code
    }

    public Computer(List<Long> memory) {
        this.stepCount = 0;
        this.PC = 0;
        this.RB = 0;
        this.memory = memoryHashMapFromList(memory);
        this.ROM = new ArrayList<>(memory);
        this.stdin = new ArrayDeque<>();
        this.stdout = new ArrayDeque<>();
        this.status = StatusCode.RUNNING;
    }

    private HashMap<Integer, Long> memoryHashMapFromList(List<Long> memory) {
        HashMap<Integer, Long> memoryMap = new HashMap<>();
        for(int i = 0; i < memory.size(); i++) {
            memoryMap.put(i,memory.get(i));
        }
        return memoryMap;
    }

    public void addInput(Long a) {

        stdin.add(a);
        if(status == StatusCode.WAIT_INPUT && !stdin.isEmpty()) status = StatusCode.RUNNING;
    }

    public void addInput(List<Long> a) {
        stdin.addAll(a);
        if(status == StatusCode.WAIT_INPUT && !stdin.isEmpty()) status = StatusCode.RUNNING;
    }

    public long readMemory(long address) {
        if(memory.containsKey((int) address)) {
            return memory.get((int) address);
        }
        else {
            return 0;
        }
    }

    private void writeMemory(long address, long value) {
        if(address >= 0) {
            memory.put((int) address,value);
        }
        else {
            System.out.println("Negative addresses are not allowed!");
            status = StatusCode.ERROR;
        }
    }

    public StatusCode step() {
        try {
            Fetcher fetcher = new Fetcher(this);
            long[] args = fetcher.getArgs();
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
                        PC = (int) args[1] - fetcher.getInstruction().getNumOfArgs() - 1;
                    status = StatusCode.RUNNING;
                    break;
                case JUMP_IF_FALSE:
                    if(args[0] == 0)
                        PC =  (int) args[1] - fetcher.getInstruction().getNumOfArgs() - 1;
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
                case REBASE:
                    RB += args[0];
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

    public Optional<Long> getFirstOutput() {
        if(stdout.isEmpty()) return Optional.empty();
        else return Optional.of(stdout.poll());
    }

    public int getStdoutSize() {
        return stdout.size();
    }

    public int getPC() {
        return PC;
    }

    public int getRB() {
        return RB;
    };


    public StatusCode getStatus() {
        return this.status;
    }

    public void forceStop() {
        this.status = StatusCode.TERMINATED;
    }


    public void resetComputer() {
        PC = 0;
        RB = 0;
        memory = memoryHashMapFromList(ROM);
        status = StatusCode.RUNNING;
        stdin = new ArrayDeque<>();
        stdout = new ArrayDeque<>();
        stepCount = 0;
    }



}
