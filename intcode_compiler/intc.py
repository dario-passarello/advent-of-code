import sys
import re
import argparse


def parse_intcode(filename):
    pFile = open(filename,'r')
    intcode = {}
    symb_table = {}
    mem_address = 0 #current memory address
    line_number = 1 #current line number -> for error messages
    ended = False   #true if "end" statement reached, after "end" all other lines are ignored
    #Parsing stage:
    #-> Obtain an array with value or reference to symbols
    #-> Obtain a table with all
    for line_raw in pFile:
        line = line_raw.strip("\n") 
        int_match = re.match("^(?::\w+)|(?:-?\d+)\s*-?\s*(?::\s*\w+)?\s*$",line) #regex for matching <:symbol_reference | Integer> [-]:<symbol_declarations>
        istr_match = re.match("^-?\d*\s*x\s*\d*\s*$",line)                       
        end_match  = re.match("^end\s*$",line)

        ret_dict = {} #dictionary that contains all the information parsed from the current line

        if int_match != None:   
        	ret_dict.update(process_int_match(int_match))
        elif istr_match != None:
        	ret_dict.update(process_istr(istr_match))
        elif end_match != None:
            ended = True
            break
        else:
        	ret_dict["error"] = "Malformed Instruction"

        if "error" in ret_dict:
        	print("Error in line: " + str(line_number) + " - Error desc: " + ret_dict["error"])
        	return None

        if "value" in ret_dict: 
        	intcode[mem_address] = (ret_dict["value"]) #copy the value
        elif "symbol_ref" in ret_dict:
        	intcode[mem_address] = (ret_dict["symbol_ref"]) #copy a reference to a symbol. it will be replaced in the "linking" stage
        

        if "symbol" in ret_dict:
            if ret_dict["symbol"] not in symb_table: #add a new symbol to the symbol table
               symb_table[ret_dict["symbol"]] = mem_address
            else:
                print("Error in line: " + str(line_number) + " - Error desc: Duplicated symbol declaration (" + str(ret_dict["symbol"] + ")"))
                return None

        if "iterated_value" in ret_dict:
        	for i in range(ret_dict["times"]):
        		intcode[mem_address] = (ret_dict["iterated_value"])
        		mem_address += 1

        line_number += 1
        mem_address += 1
    if ended == False:
        print("Error: \"end\" Expected")
    #Linking stage
    #Translate every symbol reference in the code to an Integer address
    for address in intcode:
    	if not check_int(intcode[address]):
    		if intcode[address] in symb_table:
    			intcode[address] = symb_table[intcode[address]]
    		else:
    			print("Error in address: " + str(address) + " - No such symbol (" + str(intcode[address]) + ")")
    			return None

    return intcode

def print_intcode(filename,intcode):
    pFile = open(filename,"w")
    if intcode == None:
        print("Some errors occurred: Can't compile Intcode!")
        return 1
    for i in range(len(intcode) - 1):
        pFile.write(str(intcode[i]) + ",")
    pFile.write(str(intcode[len(intcode) - 1]))
    return 0

def process_int_match(match):
	comp = re.split("\s+-?\s*",match.group(0))
	ret_dict = {}
	if check_int(comp[0]):
		ret_dict["value"] = int(comp[0])
	else:
		ret_dict["symbol_ref"] = comp[0][1:]
	if len(comp) == 2 and comp[1] != "":
		ret_dict["symbol"] = comp[1][1:]
	return ret_dict

def process_istr(match):
    comp = re.split("x",match.group(0))
    ret_dict = {}
    if check_int(comp[0]) and check_int(comp[1]):
        ret_dict["iterated_value"] = int(comp[0])
        ret_dict["times"] = int(comp[1])
    else:
        ret_dict["error"] = "Malformed Integer in Instruction"
    return ret_dict

def check_int(s):
	try:
		int(s)
		return True
	except ValueError:
		return False

def main():

    parser = argparse.ArgumentParser(description='Compiles Intcode Human Readable Files in Intcode')
    parser.add_argument('input_file',help='the input Human Readable Intcode File')
    parser.add_argument('output_file',help='the output Intcode File')

    args = parser.parse_args()

    print_intcode(args.output_file,parse_intcode(args.input_file))

main()