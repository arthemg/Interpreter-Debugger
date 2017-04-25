/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter;

import java.util.*;
/**
 *
 * @author ArtsemHoldvekht
 */
public class RunTimeStack 
{
    private int stackPointer;
    private Vector runStack;
    private Stack<Integer> framePointer;
    
    /**
     * Default constructor
     */
    
    public RunTimeStack()
    {
        stackPointer = 0;
        runStack =  new Vector<>();
        framePointer = new Stack<>();
    }
    
    /**
     * Peek at the top of the stack
     * @return 
     */
    
    
    public int peek()
    {
        int peekStack = (int) runStack.get(stackPointer - 1);
        return peekStack;
    }
    
    /**
     * Pop the top of the stack
     * @return 
     */
    public int pop()
    {   
        //storing value for future use with frames
        int popedVal = (int) runStack.get(stackPointer -1);
        runStack.set(stackPointer - 1, 0);
        stackPointer--;
        return popedVal;
    }
    
    /**
     * Push new value on top of the stack
     * @param val
     * @return 
     */
    public int push(int val)
    {
        runStack.add(stackPointer, val);
        stackPointer++;
        return val;
    }
    
    /**
     * Add new frame to the correct offset
     * @param offset 
     */
    public void addFrame(int offset)
    {
        //compute correct offset with the correct pointer
        int newFrame = (stackPointer - 1) - offset;
        
        framePointer.add(newFrame);
    }
    
    /**
     * Pop top frame
     */
    public void popFrame()
    {
        //save the value returned from a function
        int returnedVal = this.pop();
        //adjust the current stackpointer with the old frame
        this.stackPointer = (framePointer.pop()) + 1;
        
        //add the value received at the end of the fucntion
        this.push(returnedVal);
    }
    
    /**
     * Check if we have anything on the stack if not set frame to zero 
     * otherwise set the offset and the values
     * @param offset
     * @return 
     */
    public int setOffset(int offset)
    {
       int topFrame = 0;
       
       if(!framePointer.isEmpty())
       {
           topFrame = framePointer.peek();
       }
       
       //value to be set to the correct offset
       int setVal = this.pop();
       
       //adjust frame+offset and the new value
       runStack.set(topFrame + offset, setVal);
       
       return setVal;
    }
    
    /**
     * Check if theare any frames on stack if so return the top value
     * and push it onto the stack
     * @param offset
     * @return 
     */
    public int getOffset(int offset)
    {
        int topFrame = 0;
        
        if(!framePointer.isEmpty())
        {
            //make sure we addjust by +1 to use correct count
            topFrame = (framePointer.peek()) + 1;
        }
       
        
        int getVal  = (int)runStack.get(topFrame + offset);
        this.push(getVal);
        return getVal;
        
    }
    
    /**
     * Walk through all args in the runstack and return vector of said args
     * @return 
     */
    public Vector getArgs()
    {
        int start = 0;
        int stop = stackPointer;
        Vector args = new Vector();
        
        if(!framePointer.isEmpty())
        {
            start = (framePointer.peek() + 1);
        }
       
        
        for(int i = start; i < stop; i++)
        {
            args.add(runStack.get(i));
        }
        
        return args;
    }
    
    /**
     * Splited dump function in 2 parts due to messy code and too many for loops
     *
     * @param begin
     * @param end
     */
    private void formatDumpOut(int begin, int end) 
    {
        System.out.print('[');
        for (int i = begin; i <= end; i++)
        {
            System.out.print(runStack.get(i));
            if (i != end)
            {
                System.out.print(',');
            }
        }
        System.out.print(']');
    }
    
    /**
     * Output the debugger messages
     */
    public void printDump() 
    {
        int frameSize = framePointer.size();

        if (frameSize != 0)
        {
            int startPos = 0;
            for (int i = 0; i < frameSize; i++)
            {   
                int nextFrame = framePointer.get(i);
                formatDumpOut(startPos, nextFrame);
                startPos = nextFrame + 1;

            }
            formatDumpOut(startPos, stackPointer - 1);
        }
        else
        {
            formatDumpOut(0, stackPointer - 1);
        }

    }
}