/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.debugger;

import interpreter.*;
import interpreter.bytecode.*;
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
    boolean stepInPending = false;
    boolean stepOverPending = false;
    boolean fncTracingPending = false;
    boolean pause = false;
    boolean stopDebugger = false;
    boolean tracing = false;
    int activationFrameSize = 0;
    int stepLine = 0;
    
    public DebuggerVM(Program program, String sourceFile, Set<Integer> breakPoints) throws IOException, FileNotFoundException
    {
        super(program);
        
        //Initialize all the variables
        int endLine;
        topFctEnvRecord = new FunctionEnvironmentRecord();
        FunctionEnvironmentRecord constructorFctEnvRecord;
        sourceLineBpTracker = new Vector<>();
        this.breakPoints = breakPoints;
        fctEnvStack = new Stack<>();
        
        
        //Read source file and initialize all the source lines and breakpoints in a vector
        this.source = new BufferedReader(new FileReader(sourceFile));
        String currentString;
        do{
            currentString = this.source.readLine();
            if(currentString != null)
            {
                bpTracker currentStringTrack = new bpTracker(currentString, false);
                sourceLineBpTracker.add(currentStringTrack);
            }
        }while(currentString != null);
        
        constructorFctEnvRecord = new FunctionEnvironmentRecord();
        endLine = sourceLineBpTracker.size();
        
        //set start and end lines push first record onto the stack
        constructorFctEnvRecord.setStartLine(1);
        constructorFctEnvRecord.setEndLine(endLine);
        fctEnvStack.push(constructorFctEnvRecord);
    }
    
    /**
     * Overrides and extends the execution of the interpreter in the debugger mode
     * @throws ByteCodeException 
     */
    @Override
    public void executeProgram() throws ByteCodeException
    {
         //Start the program
        byteCode = program.getByteCodeAddress(programCounter);
        progRunning = true; 
        
        while (progRunning)
        {
            int currentLine = fctEnvStack.peek().getCurrLine();
            int stackSize = fctEnvStack.size();
            
            if(stepOutPending)
            {
                if(activationFrameSize  > stackSize)
                {
                    stepOutPending = false;
                    pause = true;
                }
            }
            //Check if the line changed and activationframe size increased or decreased
            else if(stepOverPending)
            {
                if((stepLine != currentLine) && (activationFrameSize > stackSize) || (activationFrameSize == stackSize))
                {
                    stepOverPending = false;
                    pause = true;
                }
            }
            //check for change in activation frame or the current line
            else if(stepInPending)
            {
                if((activationFrameSize < stackSize) || (stepLine != currentLine) && (activationFrameSize == stackSize))
                {
                    stepInPending = false;
                    pause = true;
                }
            }
            
            //pause the debugger so we can pricess the function adn also check if the 
            //next bytecode formal or not will depend if we continue or stop
            if(pause)
            {
                if(!enteredFct)
                {
                    if(!checkForFormalByteCode(byteCode))
                    {
                        progRunning = false;
                        pause = false;
                        break;
                    }
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
    
    /**
     * Set break point on a specific line
     * @param lineNum
     * @return 
     */
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
    
    /**
     * Remove a break point form a specific line
     * @param lineNum
     * @return 
     */
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
    
    /**
     * Remove all the break points that were set
     */
    public void clearAllBreakPOints()
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
    
    /**
     * Push symbol at the offset
     * @param variable
     * @param offset 
     */
    public void pushSymbolOffset(String variable, int offset) 
    {
        if(!fctEnvStack.empty())
        {
            FunctionEnvironmentRecord topFctEnvRecord = fctEnvStack.peek();
            topFctEnvRecord.setVarVal(variable, offset);
        }
    }

    /**
     * Set function info if we are tracing also output the name and the value
     * @param functionName
     * @param firstLine
     * @param lastLine 
     */
    public void setFunctionInfo(String functionName, int firstLine, int lastLine) 
    {
        if(!fctEnvStack.empty())
        {
            fctEnvStack.peek().setFunctionInfo(functionName, firstLine, lastLine);
        }
        
        popCurrentFct();
        
        if(!runStack.empty() && tracing)
        {
            displayTrace(functionName, runStack.peek());
        }
    }

    /**
     * unset the current function
     */
    public void popCurrentFct()
    {
        this.enteredFct = false;
    }
    
    /** set current function
     * 
     */
    public void setCurrentFct() 
    {
        this.enteredFct = true;
    }
    
    /**
     * add new Function Environment record to the stack
     */
    public void newFctEnvRecord() 
    {
        FunctionEnvironmentRecord newFctEnv = new FunctionEnvironmentRecord();
        fctEnvStack.push(newFctEnv);
    }

    /**
     * Set current line
     * @param currentLine 
     */
    public void setCurrentLineNumber(int currentLine) 
    {
        if(!fctEnvStack.empty())
        {
            fctEnvStack.peek().setCurrentLineNumber(currentLine);
        }
        if(!(currentLine < 1))
        {
            if(sourceLineBpTracker.get(currentLine-1).breakPoint)
            {
                pauseExecution();
            }
        }
        
    }

    /**
     * Push symbol on top
     * @param symbol 
     */
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
    
    /** pop symbol
     * 
     * @param val 
     */
    public void doPop(int val)
    {
        if(!fctEnvStack.empty())
        {
            fctEnvStack.peek().doPop(val);
        }
    }
/**
 * Remove Function Environment record
 */
    public void popFctEnvRecord() 
    {
       if(!fctEnvStack.empty())
       {
           fctEnvStack.pop();
       }
    }
    
    /**
     * get the entire program source code, also watch for the nested calls
     * @return 
     */
    public Vector<bpTracker> getFunctionSourceCode()
    {
        Vector<bpTracker> fctSourceCode = new Vector<>();
        int firstLine;
        int lastLine;
        
        if(!fctEnvStack.empty())
        {
            FunctionEnvironmentRecord workingFncRecord = fctEnvStack.peek();
            if(!(workingFncRecord.getStartLine() <= -1))
            {
                firstLine = workingFncRecord.getStartLine();
                lastLine = workingFncRecord.getEndLine();
            
                for(int i = (firstLine - 1); i < lastLine; i++)
                {
                    fctSourceCode.add(sourceLineBpTracker.get(i));
                }
                
            }
            else
            {
                String nestedFcn = "Nested Function:";
                nestedFcn += workingFncRecord.getFuncName() + "()";
                bpTracker nestedFcnRecord = new bpTracker(nestedFcn, false);

                fctSourceCode.add(nestedFcnRecord);
            }
                    
        }
        else
        {
            fctSourceCode = sourceLineBpTracker;
        }
        return fctSourceCode;
    }
    
    /**
     * Getter for function starting line
     * @return 
     */
    public int getFuntctionStartLine()
    {
        FunctionEnvironmentRecord workingFncRecord = fctEnvStack.peek();
        int functionStartLine = workingFncRecord.getStartLine();
        
        return functionStartLine;
    }
    
    /**
     * Getter for the last line of the function
     * @return 
     */
    public int getFunctionEndLine()
    {
        FunctionEnvironmentRecord workingFncRecord = fctEnvStack.peek();
        int functionEndLine = workingFncRecord.getEndLine();
        
        return functionEndLine;
    }
    
    /**
     * Getter for the current line
     * @return 
     */
    public int getCurrentLine()
    {
        if(!fctEnvStack.empty())
        {
            FunctionEnvironmentRecord workingFncRecord = fctEnvStack.peek();
            int functionCurrentLine = workingFncRecord.getCurrLine();
            
            return functionCurrentLine;
        }
        else
        {
            return 0;
        }
    }
    
    /**
     * Returns the top Function Environment Record
     * @return 
     */
    public FunctionEnvironmentRecord getEnvironmentRecord()
    {
         FunctionEnvironmentRecord workingFncEnvRecord = fctEnvStack.peek();
         
         return workingFncEnvRecord;
    }
    
    /**
     * Returns vector with all the records on the stack
     * @return 
     */
    public Vector<FunctionEnvironmentRecord> getAllEnvironmentRecords()
    {
        Vector<FunctionEnvironmentRecord> vectorFncEnvRecords = new Vector<>();
        for(int i = 0; i < fctEnvStack.size(); i++)
        {
            vectorFncEnvRecords.add(fctEnvStack.get(i));
        }
        
        return vectorFncEnvRecords;
    }
    
    
   /**
    * Stores entire source program for later use by the UI
    * @return 
    */ 
    public Vector<bpTracker> getAllSourceLine()
    {
        Vector<bpTracker> allSourceLines = sourceLineBpTracker;
        return allSourceLines;
    }
    
    /**
     * Setter for stepOut
     */
    public void stepOutPending()
    {
        stepOutPending = true;
        activationFrameSize = fctEnvStack.size();
    }
    
    /**
     * Condition to check if the debugger is running
     * @return
     * @throws ByteCodeException 
     */
    public boolean debuggerRunning() throws ByteCodeException
    {
        if(!stopDebugger)
        {
            this.executeProgram();
            return true;
        }
        else
        {
            System.out.println("Nothing to display.");
            return false;
        }
    }
    
    /**
     * Halt the program and debugger
     */
    @Override
    public void halt()
    {
        super.halt();
        stopDebugger = true;
    }
    
    /**
     * Check for "DebugFormalCode" so we can continue execution
     * @param code
     * @return 
     */
    public boolean checkForFormalByteCode(ByteCode code)
    {
        String bcName = code.getByteCodeName();
        return bcName.equals("DebugFormalCode");
    }
    
    /**
     * Get vector of all break points in the program
     * @return 
     */
    public Vector<Integer> getAllBreakPoints()
    {
        Vector<Integer> vectorBPs = new Vector<>();
        for(int i = 0; i < sourceLineBpTracker.size(); i++)
        {
            if(sourceLineBpTracker.get(i).breakPoint)
            {
                vectorBPs.add(i);
            }
        }
        return vectorBPs;
    }
    
    /**
     * Setter for stepOver
     */
    public void stepOverPending()
    {
        stepOverPending = true;
        activationFrameSize = fctEnvStack.size();
        stepLine = fctEnvStack.peek().getCurrLine();
    }
    
    /**
     * Setter for stepIn
     */
    public void stepInPending()
    {
        stepInPending = true;
        activationFrameSize = fctEnvStack.size();
        stepLine = fctEnvStack.peek().getCurrLine();
    }
    
    /**
     * Pause the Debugger to process functions mainly used for formals
     */
    public void pauseExecution()
    {
        pause = true;
    }
    
    /**
     * Setter for the tracing/on
     */
    public void traceOn() 
    {
        tracing = true;
    }

    /**
     * Setter for the tracing/off
     */
    public void traceOff() 
    {
        tracing = false;
    }
    
    /**
     * Display the function that is being traced name and arguments
     * @param functionName
     * @param argument 
     */
    private void displayTrace(String functionName, int argument) 
    {  
        String output ="";
        FunctionEnvironmentRecord workingRecord;
        int stackSize = fctEnvStack.size();
        
       
        //Strip the << identifactions from the name for cleaner output
        String splitString[] = functionName.split("<");
        String fcnName = splitString[0];
        
       for(int i = 1; i < stackSize-1; i++)
       {
           output += " ";
       }
       
       if(!fctEnvStack.empty())
       {
           workingRecord = fctEnvStack.peek();
           
           if(workingRecord.getFuncName().equals("Read"))
           {
               output += fcnName + "()";
           }
           else
           {
               output += fcnName + "(" + argument + ")";
           }
       }
       System.out.println(output);
       
    }
    
    /**
     * Display the return values of the functions if we are tracing the program
     */
    public void displayReturnTrace()
    {
        String output ="";
        String fcnName ="";
        FunctionEnvironmentRecord workingRecord;
        int stackSize = fctEnvStack.size();
        int returnValue = 0;
        
        if(this.tracing)
        {
            
            
            for(int i = 1; i < stackSize-1; i++)
            {
                output += " ";
            }
            
            if(!fctEnvStack.empty())
            {
                workingRecord = fctEnvStack.peek();
                fcnName = workingRecord.getFuncName();
                
                //Strip the << identifactions from the name for cleaner output
                String splitString[] = fcnName.split("<");
                String strippedFcnName = splitString[0];
            
                if(!runStack.empty())
                {
                    returnValue = runStack.peek();
                }
                
                output += "exit " + strippedFcnName + ": " + returnValue;
                System.out.println(output);
            }
        }
        
    }
    
    
}