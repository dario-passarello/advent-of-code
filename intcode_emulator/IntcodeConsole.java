package intcode_emulator;

import day5.Computer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class IntcodeConsole {
    public static void main(String args[]) {
        try{
            BufferedReader br = new BufferedReader(new FileReader(args[0])); //Open file
            List<Integer> code = Arrays.stream(br.readLine().split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            Computer comp = new Computer(code);
            console(comp);
        }
        catch(IOException e) {
            e.printStackTrace();
        }

    }

    public static void console(Computer computer) {
        boolean exit = false;
        boolean debug = false;
        while(!exit) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("console >");
            String command  = scanner.nextLine();
            if(command.equals("run")) {
                run(computer, scanner, debug,false);
            }
            else if(command.equals("step")) {

                run(computer, scanner, true,true);
            }
            else if(command.equals("file")) {
                in_redirect(computer, scanner);
            }
            else if(command.equals("reset")) {
                computer.resetComputer();
            }
            else if(command.equals("quit")) {
                exit = true;
            }
            else if(command.equals("debug")) {
                debug = !debug;
                System.out.println("Debug is now turned " + (debug ? "on" : "off"));
            }
            else {
                System.out.println("No such command!");
            }
        }
    }

    public static void run(Computer computer, Scanner scanner, boolean debug, boolean step) {
        Set<Integer> breakpoints = new HashSet<Integer>();
        boolean breakmode = false;
        while(computer.getStatus() != Computer.StatusCode.ERROR && computer.getStatus() != Computer.StatusCode.TERMINATED) {
            while(computer.getStatus() == Computer.StatusCode.RUNNING && (!step || (breakmode && !breakpoints.contains(computer.getPC())))){
                computer.step();
                computer.getFirstOutput().ifPresent(System.out::println);
            }
            if(step) {

                computer.step();
                computer.getFirstOutput().ifPresent(System.out::println);
                boolean stepEnd = false;
                if(debug) System.out.printf("PC=%d, mem[PC] = %d\n",computer.getPC(),computer.readMemory(computer.getPC()));
                while(!stepEnd) {
                    System.out.print("step >");
                    String str = scanner.nextLine();
                    if (str.matches("^b(reakpoint)?\\s*-?\\d+$")) {
                        breakpoints.add(Integer.parseInt(str.split("^b\\s*")[1]));
                    } else if (str.matches("^(rb)|(remove-breakpoint)\\s*-?\\d+$")) {
                        breakpoints.remove(Integer.parseInt(str.split("^b\\s*")[1]));
                    } else if (str.matches("(ns)|(no-step)")) {
                        step = false;
                        stepEnd = true;
                        breakmode = true;
                    } else if (str.matches("^p(rint)?\\s*\\d+$")) {
                        int address = Integer.parseInt(str.split("^p(rint)?\\s*")[1]);
                        System.out.printf("$%d = %d\n", address, computer.readMemory(address));
                    } else if (str.matches("s(top)?")) {
                        computer.forceStop();
                    } else if (str.matches("") || str.matches("s(tep)?")) {
                        breakmode = false;
                        stepEnd = true;
                    } else {
                        System.out.println("No such command");
                    }
                }
            }
            if(computer.getStatus() == Computer.StatusCode.WAIT_INPUT) {
                boolean input = false;
                while(!input) {
                    if(debug) System.out.printf("PC=%d, read $%d\n",computer.getPC(),computer.readMemory(computer.getPC() + 1));
                    System.out.print("stdin >");
                    String str = scanner.nextLine();
                    if(str.matches("-?\\d+")) {
                        computer.addInput(Integer.parseInt(str));
                        input = true;
                    }
                    else if (str.equals("quit")) {
                        input = true;
                        computer.forceStop();
                    }
                    else {
                        System.out.println("Errore");
                    }
                }
            }

        }
        switch (computer.getStatus()) {
            case TERMINATED:
                System.out.println("Program Terminated!");
                break;
            case ERROR:
                System.out.printf("Runtime Error! PC=%d\n",computer.getPC());
                break;
        }
    }

    public static void in_redirect(Computer computer, Scanner scanner) {
        System.out.print("file >");
        String path = scanner.nextLine();
        try{
            BufferedReader br = new BufferedReader(new FileReader(path)); //Open file
            List<Integer> list = Arrays.stream(br.readLine().split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            computer.addInput(list);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
