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
public class DebugFormalCode extends ByteCode
{   
    String variable;
    String value;
    int offset;
    
    public DebugFormalCode()
    {
        super("DebugFormalCode");
    }

    @Override
    public void execute(VirtualMachine vm) 
    {
        //Cast VirtualMachine into DebuggerVM
        DebuggerVM debugVM = (DebuggerVM) vm;
        
        offset = Integer.parseInt(value);
        debugVM.pushSymbolOffset(variable, offset);
    }

    @Override
    public void init(Vector<String> args) 
    {
        if(!args.isEmpty())
        {
            this.variable = args.get(0);
            this.value = args.get(1);
        }
    }

    @Override
    public String dumpOut(VirtualMachine vm) 
    {
        return this.getByteCode();
    }
    
}
