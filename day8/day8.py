IMAGE_WIDTH = 25
IMAGE_HEIGHT = 6

def first_statement(filename):
    max_1, max_2, min_0 = -1, -1, 999999999

    pFile = open(filename,'r')
    instr = pFile.readline()
    pFile.close()

    img_state, count_0, count_1, count_2 = 0, 0, 0, 0

    for c in instr: 
        #check if the end of the layer is reached
        if img_state < IMAGE_WIDTH*IMAGE_HEIGHT: 
            img_state += 1
        else: 
            if count_0 < min_0:
                max_1, max_2, min_0 = count_1, count_2, count_0
            count_0, count_1, count_2 = 0, 0, 0
            #reset counters
            img_state = 1
        #increment the pixel's occourences count
        if c == '0':
            count_0 += 1
        if c == '1':
            count_1 += 1
        if c == '2':
            count_2 += 1
    return max_1*max_2

def second_statement(filename):
    layer = [2 for i in range(0,IMAGE_WIDTH*IMAGE_HEIGHT)]

    pFile = open(filename,'r')
    instr = pFile.readline()
    pFile.close()

    img_state = 0
    for c in instr:
        #check if the end of the layer is reached
        if img_state < IMAGE_WIDTH*IMAGE_HEIGHT:
            img_state += 1
        else:
            img_state = 1
        #if the pixel is transparent check if there is an opaque pixel
        if layer[img_state - 1] == 2:
            if c == '0':
                layer[img_state - 1] = 0
            if c == '1':
                layer[img_state - 1] = 1
    #print the message
    for i in range(0,IMAGE_HEIGHT):
        print("")
        for j in range(0,IMAGE_WIDTH):
            print(1 if layer[i*IMAGE_WIDTH + j] == 1 else " ",end='')


print("FIRST STATEMENT: " + str(first_statement("8.in")))
print("SECOND STATEMENT")
second_statement("8.in")
print("")
