/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.bytecode.debugbytecode;

import interpreter.VirtualMachine;
import interpreter.debugger.DebuggerVM;
import interpreter.bytecode.PopCode;

/**
 *
 * @author Artsem
 */
public class DebugPopCode extends PopCode
{
    public DebugPopCode()
    {
        this.byteCodeName = "DebugPopCode";
    }
    
    @Override
    public void execute(VirtualMachine vm)
    {
        super.execute(vm);
        
        //Cast VirtualMachine into DebuggerVM
        DebuggerVM debugVM = (DebuggerVM) vm;
        
        debugVM.doPop(val);
        
    }
}
