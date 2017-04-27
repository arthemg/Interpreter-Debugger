/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.bytecode.debugbytecode;

import interpreter.VirtualMachine;
import interpreter.debugger.DebuggerVM;
import interpreter.bytecode.LitCode;
import java.util.*;
/**
 *
 * @author Artsem
 */
public class DebugLitCode extends LitCode
{
    String variable;
    int offset;
    boolean pushVariable;
    
    public DebugLitCode()
    {
        this.byteCodeName = "DebugLitCode";
    }
    
    @Override
    public void execute(VirtualMachine vm)
    {
        //Cast VirtualMachine into DebuggerVM
        DebuggerVM debugVM = (DebuggerVM) vm;
        
        //check if we need to push the variable
        if(pushVariable)
        {
            debugVM.pushSymbol();
        }
    }
    
    public void init(Vector<String> args)
    {
        super.init(args);
        if(!args.isEmpty())
        {
            if(args.size() == 2)
            {
                pushVariable = true;
                offset = Integer.parseInt(args.get(0));
                variable = args.get(1);
            }
        }
    }
}
