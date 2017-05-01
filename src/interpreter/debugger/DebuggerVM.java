/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.debugger;

import interpreter.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author Artsem
 */

/**
 * bpTracker is used to group 2 fields the line of the source code and track
 * if there is a breakpoint set at that line similar to the SymbolTable
 *
 */
public class DebuggerVM extends VirtualMachine
{
    
public static class bpTracker
{
    boolean breakPoint;
    String sourceLine;
    
    public bpTracker(String sourceLine, boolean breakPoint)
    {
        this.sourceLine = sourceLine;
        this.breakPoint = breakPoint;
    }
    
    public void setBreakPointBool()
    {
        breakPoint = true;
    }
    
    public boolean getBreakPointBool()
    {
        return this.breakPoint;
    }
    
    public void setSourceLine(String line)
    {
        this.sourceLine = line;
    }
    
    public String getSourceLine()
    {
        return this.sourceLine;
    }
    
    public void unsetBreakPointBool()
    {
        breakPoint = false;
    }
    
}

   
    BufferedReader source;
    Stack<FunctionEnvironmentRecord> fctEnvStack;
    Set<Integer> breakPoints;
    FunctionEnvironmentRecord topFctEnvRecord;
    Vector<bpTracker> sourceLineBpTracker;
    boolean enteredFct =  false;
    boolean stepOutPending = false;
    boolean stopDebugger = false;
    int activationFrameSize = 0;
    
    public DebuggerVM(Program program, String sourceFile, Set<Integer> breakPoints) throws IOException, FileNotFoundException
    {
        super(program);
        //Initialize all the variables
        topFctEnvRecord = new FunctionEnvironmentRecord();
        sourceLineBpTracker = new Vector<>();
        this.breakPoints = breakPoints;
        fctEnvStack = new Stack<>();
        
        
        //Read source file and initialize all the source lines and breakpoints in a vector
        this.source = new BufferedReader(new FileReader(sourceFile));
        String currentString = "";
        do{
            currentString = this.source.readLine();
            if(currentString != null){
             
       
        
       
            bpTracker currentStringTrack = new bpTracker(currentString, false);
            sourceLineBpTracker.add(currentStringTrack);
            }
        }while(currentString != null);
        
        FunctionEnvironmentRecord constructorFctEnvRecord = new FunctionEnvironmentRecord();
        constructorFctEnvRecord.setStartLine(1);
        int endLine = sourceLineBpTracker.size();
        constructorFctEnvRecord.setEndLine(endLine);
        fctEnvStack.push(constructorFctEnvRecord);
    }
    
    @Override
    public void executeProgram() throws ByteCodeException
    {
         //Start the program
        byteCode = program.getByteCodeAddress(programCounter);
        progRunning = true; 
        
        while (progRunning)
        {
            if(stepOutPending)
            {
                if(activationFrameSize > fctEnvStack.size())
                {
                    stepOutPending = false;
                    break;
                }
            }
            //Start the counter
            programCounter++; 
            //Execture bytecode
            byteCode.execute(this);

            // check if Dumping is on if true print out the debugMessages
            if (dumping)
            {
                //do not print null messages
                //print bytecode debuggerMessage followed by the frames/stacks
                if (byteCode.dumpOut(this) != null)
                {
                    System.out.println(byteCode.dumpOut(this));
                }
                
                runStack.printDump();
                System.out.println();

            }
            //get another bytecode
            if (progRunning)
            {
                byteCode = program.getByteCodeAddress(programCounter);
            }
        }
    }
    
    public boolean setBreakPoint(int lineNum)
    {
        if(breakPoints.contains(lineNum))
        {
            sourceLineBpTracker.get(lineNum-1).setBreakPointBool();
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public boolean clearBreakPoint(int lineNum)
    {
        if(breakPoints.contains(lineNum))
        {   
            sourceLineBpTracker.get(lineNum-1).unsetBreakPointBool();
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public void clearAllBrakPOints()
    {
        Iterator it = breakPoints.iterator();
        
        while(it.hasNext())
        {
            int index = (int)it.next();
            if(index>0)
            {
              sourceLineBpTracker.get(index-1).unsetBreakPointBool();
            }
        }
    }
    
    public void pushSymbolOffset(String variable, int offset) 
    {
        if(!fctEnvStack.empty())
        {
            FunctionEnvironmentRecord topFctEnvRecord = fctEnvStack.peek();
            topFctEnvRecord.setVarVal(variable, offset);
        }
    }

    public void setFunctionInfo(String functionName, int firstLine, int lastLine) 
    {
        if(!fctEnvStack.empty())
        {
            fctEnvStack.peek().setFunctionInfo(functionName, firstLine, lastLine);
        }
        popCurrentFct();
        if(firstLine > 0)
        {   
            //if we hit set break point we stop
            if(sourceLineBpTracker.get(firstLine-1).breakPoint)
            {
                super.halt();
            }
        }
    }

    public void popCurrentFct()
    {
        this.enteredFct = false;
    }
    
    public void setCurrentFct() 
    {
        this.enteredFct = true;
    }
    
    public void newFctEnvRecord() 
    {
        FunctionEnvironmentRecord newFctEnv = new FunctionEnvironmentRecord();
        fctEnvStack.push(newFctEnv);
    }

    public void setCurrentLineNumber(int currentLine) 
    {
        if(!fctEnvStack.empty())
        {
            fctEnvStack.peek().setCurrentLineNumber(currentLine);
        }
        
    }

    public void pushSymbol(String symbol) 
    {
        if(!fctEnvStack.empty())
        {
            int stackPointer = this.runStack.getStackPointer();
            stackPointer--;
            FunctionEnvironmentRecord topEnvironmentRecord = fctEnvStack.peek();
            topEnvironmentRecord.setVarVal(symbol, stackPointer);
        }
    }
    
    public void doPop(int val)
    {
        if(!fctEnvStack.empty())
        {
            fctEnvStack.peek().doPop(val);
        }
    }

    public void popFctEnvRecord() 
    {
       if(!fctEnvStack.empty())
       {
           fctEnvStack.pop();
       }
    }
    
    public Vector<bpTracker> getFunctionSourceCode()
    {
        Vector<bpTracker> fctSourceCode = new Vector<>();
        int firstLine;
        int lastLine;
        
        if(!fctEnvStack.empty())
        {
            FunctionEnvironmentRecord workingFncRecord = fctEnvStack.peek();
            firstLine = workingFncRecord.getStartLine();
            lastLine = workingFncRecord.getEndLine();
            
            for(int i = (firstLine - 1); i < lastLine; i++)
            {
                fctSourceCode.add(sourceLineBpTracker.get(i));
            }            
        }
        return fctSourceCode;
    }
    
    public int getFuntctionStartLine()
    {
        FunctionEnvironmentRecord workingFncRecord = fctEnvStack.peek();
        int functionStartLine = workingFncRecord.getStartLine();
        
        return functionStartLine;
    }
    
    public int getFunctionEndLine()
    {
        FunctionEnvironmentRecord workingFncRecord = fctEnvStack.peek();
        int functionEndLine = workingFncRecord.getEndLine();
        
        return functionEndLine;
    }
    
    public int getCurrentLine()
    {
        if(!fctEnvStack.empty())
        {
            FunctionEnvironmentRecord workingFncRecord = fctEnvStack.peek();
            int functionCurrentLine = workingFncRecord.getEndLine();
            
            return functionCurrentLine;
        }
        else
        {
            return 0;
        }
    }
    
    public FunctionEnvironmentRecord getEnvironmentRecord()
    {
         FunctionEnvironmentRecord workingFncEnvRecord = fctEnvStack.peek();
         
         return workingFncEnvRecord;
    }
    
    public void stepOutPending()
    {
        stepOutPending = true;
        activationFrameSize = fctEnvStack.size();
    }
    
    public boolean debuggerRunning() throws ByteCodeException
    {
        if(!stopDebugger)
        {
            this.executeProgram();
            return true;
        }
        else
        {
            return false;
        }
    }

   
    
}