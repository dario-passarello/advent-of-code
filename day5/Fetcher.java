package day5;

public class Fetcher {
    private Computer computer;
    private InstructionPrototype instruction;
    private long[] args;
    public Fetcher(Computer computer) throws IllegalFormatException {
        this.computer = computer;
        fetch();
    }
    private static final int POSITIONAL = 0;
    private static final int IMMEDIATE = 1;
    private static final int RELATIVE = 2;

    //Fetches arguments
    private void fetch() throws IllegalFormatException {
        long opcode = computer.readMemory(computer.getPC());
        long modes = opcode / 100;
        long pureOPcode = opcode % 100;
        instruction = InstructionPrototype.getFromOpCode((int) pureOPcode); //get instruction prototype infos
        args = new long[instruction.getNumOfArgs()];
        for(int i = 0; i < instruction.getNumOfArgs(); i++) {
            if(instruction.writeArg(i+1)) { //IF an argument is the destination address where the result of an operation has to be written
                switch ((int) (modes % 10)) {
                    case POSITIONAL:
                    case IMMEDIATE:
                        args[i] = computer.readMemory(computer.getPC() + i + 1);  //Then read it in "immediate" mode ignoring the parameter mode of that parameter
                        break;
                    case RELATIVE:
                        args[i] = computer.readMemory(computer.getPC() + i + 1) + computer.getRB();
                        break;
                    default:
                        throw new IllegalFormatException();
                }

            } else {
                switch ((int) (modes % 10)) {
                    case POSITIONAL: //Positional parameter mode -> read the argument the address specified in the parameter
                        args[i] = computer.readMemory((int) computer.readMemory(computer.getPC() + i + 1));
                        break;
                    case IMMEDIATE: //Immediate parameter mode -> read the argument directly from the parameter
                        args[i] = computer.readMemory(computer.getPC() + i + 1);
                        break;
                    case RELATIVE:
                        args[i] = computer.readMemory((int) computer.readMemory(computer.getPC() + i + 1) + computer.getRB());
                        break;
                    default: //In case of error launch the exception
                        throw new IllegalFormatException();
                }
            }
            modes = modes / 10;
        }
    }

    public InstructionPrototype getInstruction() {
        return instruction;
    }

    public long[] getArgs() {
        return args;
    }
}
