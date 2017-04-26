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
    
    public DebugFormalCode()
    {
        super("DebugFormalCode");
    }

    @Override
    public void execute(VirtualMachine vm) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void init(Vector<String> args) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String dumpOut(VirtualMachine vm) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
