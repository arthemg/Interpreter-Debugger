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
class bpTracker
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

public class DebuggerVM extends VirtualMachine
{   
    BufferedReader sourceFile;
    Stack<FunctionEnvironmentRecord> fctEnvStack;
    HashSet<Integer> breakPoints;
    FunctionEnvironmentRecord topFctEnvRecord;
    Vector<bpTracker> sourceLineBpTracker;
    boolean enteredFct;
    boolean stepOutPending;
    boolean stopDebugger;
    
    public DebuggerVM(Program program, String sourceFile, HashSet<Integer> breakPoints) throws IOException, FileNotFoundException
    {
        super(program);
        //Initialize all the variables
        topFctEnvRecord = new FunctionEnvironmentRecord();
        sourceLineBpTracker = new Vector<>();
        this.breakPoints = breakPoints;
        
        FunctionEnvironmentRecord constructorFctEnvRecord = new FunctionEnvironmentRecord();
        constructorFctEnvRecord.setStartLine(1);
        int endLine = sourceLineBpTracker.size();
        constructorFctEnvRecord.setEndLine(endLine);
        fctEnvStack.push(constructorFctEnvRecord);
        
        //Read source file and initialize all the source lines and breakpoints in a vector
        this.sourceFile = new BufferedReader(new FileReader(sourceFile));
        String currentString = this.sourceFile.readLine();
        
        while(currentString != null)
        {
            bpTracker currentStringTrack = new bpTracker(currentString, false);
            sourceLineBpTracker.add(currentStringTrack);
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
            fctEnvStack.peek().setFunctionInfo(functionName, lastLine, lastLine);
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
}
