/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.bytecode.debugbytecode;

import interpreter.VirtualMachine;
import interpreter.debugger.DebuggerVM;
import interpreter.bytecode.ByteCode;
import java.util.*;
/**
 *
 * @author Artsem
 */
public class DebugLineCode extends ByteCode 
{
    int currentLine;

    public DebugLineCode() 
    {
        super("DebugLineCode");
    }

    @Override
    public void execute(VirtualMachine vm) 
    {
        //Cast VirtualMachine into DebuggerVM
        DebuggerVM debugVM = (DebuggerVM) vm;
        
        debugVM.setCurrentLineNumber(currentLine);
    }

    @Override
    public void init(Vector<String> args) 
    {
        if(!args.isEmpty())
        {
            currentLine = Integer.parseInt(args.get(0));
        }
    }

    @Override
    public String dumpOut(VirtualMachine vm) 
    {
        return this.getByteCode();
    }
    
    
}
