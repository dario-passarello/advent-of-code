package day5;

public enum InstructionPrototype {

    ADD(1,3, 0b001),
    MUL(2,3, 0b001),
    INPUT(3,1, 0b01),
    OUTPUT(4,1, 0b00),
    JUMP_IF_TRUE(5,2,0b00),
    JUMP_IF_FALSE(6,2,0b00),
    LESS_THAN(7,3, 0b001),
    EQUALS(8,3,0b001),
    TERMINATE(99,0, 0b0),
    NOT_VALID(Integer.MAX_VALUE,0, 0b0);

    private final int opcode;       //The "pure" OP code of the instruction as indicated in the specs
    private final int numOfArgs;    //Number of argument that the instruction accepts
    private final int writeMask;    //Specifies what parameter is an address where the instruction is going to write

    private InstructionPrototype(int opcode, int numOfArgs, int writeMask) {
        this.opcode = opcode;
        this.numOfArgs = numOfArgs;
        this.writeMask = writeMask;
    }

    public static InstructionPrototype getFromOpCode(int opcode) {
        switch (opcode) {
            case 1:
                return ADD;
            case 2:
                return MUL;
            case 3:
                return INPUT;
            case 4:
                return OUTPUT;
            case 5:
                return JUMP_IF_TRUE;
            case 6:
                return JUMP_IF_FALSE;
            case 7:
                return LESS_THAN;
            case 8:
                return EQUALS;
            case 99:
                return TERMINATE;
            default:
                return NOT_VALID;
        }
    }

    public int getOpcode() {
        return opcode;
    }

    public int getNumOfArgs() {
        return numOfArgs;
    }

    public boolean writeArg(int argPos) {
        return (writeMask >> (numOfArgs - argPos)) % 2 == 1;
    }
}
