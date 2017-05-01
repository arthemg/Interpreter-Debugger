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
    
    public void helpMenu()
    {
        System.out.println("|-------------------------HELP MENU-----------------------------|");
        System.out.println("|?            -Print Help Menu.                   e.g. ?        |");
        System.out.println("|sbp line#    -Set Break Point/s.                 e.g. sbp 1 5 9|");
        System.out.println("|cbp line#    -Remove Break Point/s.              e.g. cbp 1 5 9|");
        System.out.println("|cabp         -Cleares All Break Points.          e.g. cabp     |");
        System.out.println("|pvar         -Prints All local Variables.        e.g. pvar     |");
        System.out.println("|pr           -Prints entire program out.         e.g. pr       |");
        System.out.println("|so           -Step out of the function.          e.g. so       |");
        System.out.println("|c            -Continue program execution         e.g. c        |");
        System.out.println("|q            -Quit debugger.                     e.g. q        |");
        System.out.println("|---------------------------------------------------------------|");
    }
    
    public void userInput(String[] in) throws ByteCodeException
    {
        String input = in[0].toLowerCase();
        if(input == "?")
        {
            helpMenu();
        }
        else if(input == "sbp")
        {
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
            }
        }
        else if(input == "cbp")
        {
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
            }
        }
        else if(input == "cabp")
        {
            debugVM.clearAllBrakPOints();
        }
        else if(input == "pvar")
        {
            displayariables();
        }
        else if(input == "pr")
        {
            this.displayCodeSnipet();
        }
        else if(input == "so")
        {
            debugVM.stepOutPending();
            //check if the debugger still running
            if(debugVM.debuggerRunning())
            {
                this.displayCodeSnipet();
            }
        }
        else if(input == "c")
        {
           if(debugVM.debuggerRunning())
            {
                this.displayCodeSnipet();
            }
        }
        else if(input == "q")
        {
            System.out.println("Good Bye!");
            System.exit(1);
        }
        
    }

    private void displayariables() 
    {
   
        FunctionEnvironmentRecord topFcnEnvRecord = debugVM.getEnvironmentRecord();
        SymbolTable symTable = topFcnEnvRecord.getSymbolTable();
        
    }

    private void displayCodeSnipet() 
    {
        Vector<DebuggerVM.bpTracker> sourceCode = debugVM.getFunctionSourceCode();
        int currentLine = debugVM.getCurrentLine();
        int startLine = debugVM.getFuntctionStartLine();
        int endLine = debugVM.getFunctionEndLine();
        
        currentLine = currentLine - startLine;
        
        for(int i = 0; i<sourceCode.size(); i++)
        {
            String output="";
            DebuggerVM.bpTracker sourceLine = sourceCode.get(i);
            
            //check for break points
            if(sourceLine.getBreakPointBool())
            {
                output += " *";
            }
            else
            {
                output += " ";
            }
            
            output += String.format("%3d.", (i + startLine));
            output += String.format("%-50s", sourceLine.getSourceLine());
            
            //check if we are printing current line
            if(i == currentLine)
            {
                output +=String.format("-3s","<<<<<<------");
            }
            else
            {
                //output +=String.format("-3s"," ");
            }
            System.out.println(output);
        }
    }
    
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
    
}