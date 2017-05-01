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
public class DebugFunctionCode extends ByteCode 
{
    String functionName;
    int firstLine;
    int lastLine;

    public DebugFunctionCode() 
    {
        super("DebugFormalCode");
    }

    @Override
    public void execute(VirtualMachine vm) 
    {
        //Cast VirtualMachine into DebuggerVM
        DebuggerVM debugVM = (DebuggerVM) vm;
        
        //Set new function info
        debugVM.setFunctionInfo(functionName, firstLine, lastLine);
        //Remove the top fucntion "current Function"
        debugVM.popCurrentFct();
    }

    @Override
    public void init(Vector<String> args)
    {
        if(!args.isEmpty())
        {
            functionName = args.get(0);
            firstLine = Integer.parseInt(args.get(1));
            lastLine = Integer.parseInt(args.get(2));
        }
    }

    @Override
    public String dumpOut(VirtualMachine vm) 
    {
        return this.getByteCode();
    }
}
