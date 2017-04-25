/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter;

import interpreter.bytecode.ByteCode;
import java.util.*;
/**
 *
 * @author ArtsemHoldvekht
 */
public class VirtualMachine 
{
    private final Program program;
    private boolean dumping;
    private boolean progRunning;
    private int programCounter;
    private final RunTimeStack runStack;
    private final Stack<Integer> stackAddress;
    private ByteCode byteCode;

    public VirtualMachine(Program program) 
    {
        this.program = program;
        progRunning = false;
        dumping = false;
        stackAddress = new Stack<>();
        runStack = new RunTimeStack();
        programCounter = 0;
    }
    
    /**
     * Peek at the top of VM
     * @return 
     */
    public int peek()
    {
        return runStack.peek();
    }
    
    /**
     * Pop to frame on VM
     * @return 
     */
    public int pop()
    {
        return runStack.pop();
    }
    
    /**
     * push new val on VM
     * @param val
     * @return 
     */
    public int push(int val)
    {
        runStack.push(val);
        return runStack.peek();
    }
    
    /**
     * Set offset
     * @param offset
     * @return 
     */
    public int setOffset(int offset)
    {
        return runStack.setOffset(offset);
    }
    
    /**
     * Get offset
     * @param offset
     * @return 
     */
    public int getOffset(int offset)
    {
        return runStack.getOffset(offset);
    }
    
    /**
     * Set ProgramCounter
     * @param pc 
     */
    
    public void setProgramCounter(int pc)
    {
        programCounter = pc;
        progRunning = true;
    }
    
    /**
     * Push new address onto stack
     */
    public void pushAddress()
    {
        stackAddress.push(programCounter);
    }
    
    /**
     * Remove address form stack
     * @return 
     */
    public int popAddress()
    {
        return stackAddress.pop();
    }
    
    /**
     * Retrieve all args
     * @return 
     */
    public Vector<String> getArgs()
    {
        return runStack.getArgs();
    }
    
    /**
     * Read function to take users input for Read() Byte Code
     * @return 
     */
    public int readInput()
    {
        int input;
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter Integer: ");
        input = in.nextInt();
        
        return input;
    }
    
    /**
     * Prit value during Write Code
     */
    public void writeVal()
    {
        int val = runStack.peek();
        System.out.println(val);
       
    }
    
    /**
     * Set dump option on
     */
    public void dumpIsOn()
    {
        dumping = true;
    }
    
    /**
     * Set Dump option off
     */
    public void dumpIsOff()
    {
        dumping = false;
    }
    
    /**
     * Add new frame onto the stack
     * @param offset 
     */
    public void addFrame(int offset)
    {
        runStack.addFrame(offset);
    }
    
    /**
     * Remove top frame
     */
    public void popFrame()
    {
        runStack.popFrame();
    }
    
    
    /** 
     * Start program and program counter
     * if dumping is on also print the DebuggerMessages
     * @throws ByteCodeException 
     */
     public void executeProgram() throws ByteCodeException 
    {      
        //Start the program
        byteCode = program.getByteCodeAddress(programCounter);
        progRunning = true; 
        
        while (progRunning)
        {
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
}