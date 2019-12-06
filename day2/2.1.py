pFile = open("2.in")
program = pFile.readline()

ints = []
ints = list(map(lambda i: int(i), program.split(",")))
print(ints)
def execute(ints_arr, n, v):
    ints = ints_arr.copy()
    pc = 0
    terminate = False
    ints[1] = n
    ints[2] = v
    while not terminate:
        if ints[pc] == 99:
            #print("Terminated by HALT instruction")
            terminate = True
        elif ints[pc] == 1:
            ints[ints[pc + 3]] = ints[ints[pc + 1]] + ints[ints[pc + 2]]
            pc += 4
        elif ints[pc] == 2:
            ints[ints[pc + 3]] = ints[ints[pc + 1]] * ints[ints[pc + 2]]
            pc += 4
        else:
            print("ERRORE")
            terminate = True
    return ints[0]
for i in range(0,99):
    for j in range(0,99):
        if(execute(ints,i,j) == 19690720):
            print(i * 100 + j)

