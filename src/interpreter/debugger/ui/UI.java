/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.debugger.ui;

import interpreter.*;
import interpreter.debugger.*;
import java.io.IOException;
import java.util.*;


/**
 *
 * @author Artsem
 */
public class UI 
{
    DebuggerVM debugVM;
    
    public UI(Program program, String sourceFile, Set<Integer> breakPoints) throws IOException 
    {
        debugVM = new DebuggerVM(program, sourceFile, breakPoints);
    }

    /**
     * Quick prompt so the user knows program is waiting for the input
     */
    public void prompt()
    {
        System.out.print(">");
    }
    
    /**
     * Print out Help menu with commands and examples
     */
    public void helpMenu()
    {
        System.out.println("|-------------------------HELP MENU-------------------------------------|");
        System.out.println("|?            -Print Help Menu.                   e.g. ?                |");
        System.out.println("|sbp line#    -Set Break Point/s.                 e.g. sbp 1 5 9        |");
        System.out.println("|cbp line#    -Remove Break Point/s.              e.g. cbp 1 5 9        |");
        System.out.println("|cabp         -Cleares All Break Points.          e.g. cabp             |");
        System.out.println("|pvar         -Prints All local Variables.        e.g. pvar             |");
        System.out.println("|pbp          -Prints All set Break points.       e.g. pbp              |");
        System.out.println("|pr           -Prints current function executed.  e.g. pr               |");
        System.out.println("|pra          -Prints entireprogram.              e.g. pr               |");
        System.out.println("|so           -Step out of the function.          e.g. so               |");
        System.out.println("|sov          -Step over the function.            e.g. sov              |");
        System.out.println("|tr 1/0       -Turn tracing on/off. 1=on, 0=off   e.g. tr 1 or tr 0     |");
        System.out.println("|si           -Step into the function.            e.g. si               |");
        System.out.println("|pcs          -Print Call Stack.                  e.g. pcs              |");
        System.out.println("|c            -Continue program execution         e.g. c                |");
        System.out.println("|q            -Quit debugger.                     e.g. q                |");
        System.out.println("|-----------------------------------------------------------------------|");
    }
    
    /**
     * Switch to process user's input
     * @param in
     * @throws ByteCodeException 
     */
    public void userInput(String[] in) throws ByteCodeException
    {
        String input = in[0].toLowerCase();
        switch (input) {
            case "?":
                helpMenu();
                break;
            case "sbp":
                for(int i = 1; i < in.length; i++)
                {
                    int bpLine = Integer.parseInt(in[i]);
                    if(debugVM.setBreakPoint(bpLine))
                    {
                        
                        System.out.println("Break Point has been set at line: "+ bpLine);
                    }
                    else
                    {
                        System.out.println("Unable to set Break Point at line: "+ bpLine);
                    }
                }   break;
            case "cbp":
                for(int i = 1; i < in.length; i++)
                {
                    int bpLine = Integer.parseInt(in[i]);
                    if(debugVM.clearBreakPoint(bpLine))
                    {
                        System.out.println("Break Point has been cleared at line: "+ bpLine);
                    }
                    else
                    {
                        System.out.println("Unable to clear Break Point at line: "+ bpLine);
                    }
                }   break;
            case "cabp":
                debugVM.clearAllBreakPOints();
                System.out.println("All Break Points have been cleared.");
                break;
            case "pvar":
                displayVariables();
                break;
            case "pbp":
                listCurrentBreakPointSettings();
                break;
            case "pr":
                this.displayCodeSnipet();
                break;
            case "pra":
                this.displayAllSourceCode();
                break;
            case "so":
                debugVM.stepOutPending();
                //check if the debugger still running
                if(debugVM.debuggerRunning())
                {
                    displayCodeSnipet();
                }   break;
            case "sov":
                debugVM.stepOverPending();
                if(debugVM.debuggerRunning())
                {
                    displayCodeSnipet();
                }break;
            case "tr":
                int check = Integer.parseInt(in[1]);
                if(check == 1)
                {
                    debugVM.traceOn();
                    System.out.println("Tracing is on.");
                }
                else if(check == 0)
                {
                    debugVM.traceOff();
                    System.out.println("Tracing is off.");
                }
                else
                {
                   System.out.println("Please use 1 to turn taracing on or 2 to turn tracing off."); 
                }
                break;
                        
            case "si":
                debugVM.stepInPending();
                if(debugVM.debuggerRunning())
                {
                    displayCodeSnipet();
                }break;
            case "pcs":
                printCallStack();
                break;
            case "c":
                if(debugVM.debuggerRunning())
                {
                    displayCodeSnipet();
                }   break;
            case "q":
                System.out.println("Good Bye!");
                System.exit(1);
            default:
                break;
        }
        
    }

    /**
     * Print the list of all available local variables
     */
    private void displayVariables() 
    {
   
        FunctionEnvironmentRecord topFcnEnvRecord = debugVM.getEnvironmentRecord();
        SymbolTable symTable = topFcnEnvRecord.getSymbolTable();
        
        //get the set of all teh symbol keys
        Set<String> keySet = symTable.keys();

        if(!keySet.isEmpty())
        {
            keySet.forEach((String symbol) -> 
            {
                int offset = (int) symTable.get(symbol);
                int value = debugVM.getOffset(offset);
                System.out.println("Local Variables: "+symbol + "="+ value);
            });
        }
        
        else
        {
            System.out.println("There are no local variable to display.");
        }
        
    }

    /**
     * Displays the current function execution code or the whole program on the
     * first run
     */
    public void displayCodeSnipet() 
    {
        Vector<DebuggerVM.bpTracker> sourceCode = debugVM.getFunctionSourceCode();
        int currentLine = debugVM.getCurrentLine();
        int startLine = debugVM.getFuntctionStartLine();
        
        currentLine = currentLine - startLine;
        
        for(int i = 0; i<sourceCode.size(); i++)
        {
            String output=" ";
            DebuggerVM.bpTracker sourceLine = sourceCode.get(i);
            
            //check for break points
            if(sourceLine.getBreakPointBool())
            {
                output = "*";
            }
            else
            {
                output =" ";
                
            }
            
            output += String.format("%3d.", (i + startLine));
            output += String.format("%-35s", sourceLine.getSourceLine());
            
            //check if we are printing current line
            if(i == currentLine)
            {
                output +=String.format("<<<<<<------");
            }
            else
            {
                output +=String.format(" ");
            }
            System.out.println(output);
        }
    }
    
    /**
     * Displays the whole program regardless of execution point or state.
     */
    public void displayAllSourceCode()
    {
         Vector<DebuggerVM.bpTracker> sourceCode = debugVM.getAllSourceLine();
        int currentLine = debugVM.getCurrentLine();
        
        for(int i = 0; i<sourceCode.size(); i++)
        {
            String output=" ";
            DebuggerVM.bpTracker sourceLine = sourceCode.get(i);
            
            //check for break points
            if(sourceLine.getBreakPointBool())
            {
                output = "*";
            }
            else
            {
                output =" ";
                
            }
            
            output += String.format("%3d.", (i + 1));
            output += String.format("%-35s", sourceLine.getSourceLine());
            
            //check if we are printing current line
            if(i == currentLine)
            {
                output +=String.format("<<<<<<------");
            }
            else
            {
                output +=String.format(" ");
            }
            System.out.println(output);
        }
    }
    
    /** 
     * Run method to be executed from the interpreter main program
     * @throws ByteCodeException 
     */
    public void run() throws ByteCodeException
    {
        String userInput;
        Scanner in = new Scanner(System.in);
        
        displayCodeSnipet();
        System.out.println("Type ? for help");
        
        do 
        {            
            this.prompt();
            userInput = in.nextLine();
            
            String[] splitUserInput = userInput.split("\\s");
            if(!(splitUserInput[0].equals("q")))
            {
                userInput(splitUserInput);
            }
        } while (!userInput.equals("q"));
    }
    
    /**
     * Printing the call stack trace
     */
    public void printCallStack()
    {
        Vector <FunctionEnvironmentRecord> vectorFncEnvRecords = debugVM.getAllEnvironmentRecords();
        FunctionEnvironmentRecord topFunctionEnvironmentRecord;
        int size = vectorFncEnvRecords.size() - 1;
        
        for(int i = size; i >= 0; i--)
        {
            topFunctionEnvironmentRecord = vectorFncEnvRecords.get(i);
            String fctName = topFunctionEnvironmentRecord.getFuncName();
            int lineNum = topFunctionEnvironmentRecord.getCurrLine();
            System.out.println(fctName + ": " + lineNum);
        }
        
    }
    
    /**
     * Prints the list of all break points
     */
    public void listCurrentBreakPointSettings()
    {
        Vector<Integer> breakPoints = debugVM.getAllBreakPoints();
        System.out.println("CURRENT BREAK POINTS:");
        for(int i = 0; i <breakPoints.size(); i++)
        {
            int lineNum = breakPoints.get(i);
            System.out.println("Break Point at line: " + (lineNum+1));
        }
    }
    
}