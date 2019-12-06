package day5;

public class Fetcher {
    private Computer computer;
    private InstructionPrototype instruction;
    private int[] args;
    public Fetcher(Computer computer) throws IllegalFormatException {
        this.computer = computer;
        fetch();
    }
    private static final int POSITIONAL = 0;
    private static final int IMMEDIATE = 1;

    //Fetches argu
    private void fetch() throws IllegalFormatException {
        int opcode = computer.readMemory(computer.getPC());
        int modes = opcode / 100;
        int pureOPcode = opcode % 100;
        instruction = InstructionPrototype.getFromOpCode(pureOPcode); //get instruction prototype infos
        args = new int[instruction.getNumOfArgs()];
        for(int i = 0; i < instruction.getNumOfArgs(); i++) {
            if(instruction.writeArg(i+1)) { //IF an argument is the destination address where the result of an operation has to be written
                args[i] = computer.readMemory(computer.getPC() + i + 1);  //Then read it in "immediate" mode ignoring the parameter mode of that parameter
            } else {
                switch (modes % 10) {
                    case POSITIONAL: //Positional parameter mode -> read the argument the address specified in the parameter
                        args[i] = computer.readMemory(computer.readMemory(computer.getPC() + i + 1));
                        break;
                    case IMMEDIATE: //Immediate parameter mode -> read the argument directly from the parameter
                        args[i] = computer.readMemory(computer.getPC() + i + 1);
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

    public int[] getArgs() {
        return args;
    }
}
