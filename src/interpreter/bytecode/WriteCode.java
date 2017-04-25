/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.bytecode;

import interpreter.*;
import java.util.*;
/**
 *
 * @author ArtsemHoldvekht
 */
public class WriteCode extends ByteCode 
{
    public WriteCode()
    {
        super("WriteCode");
    }
    
    @Override
    public void execute(VirtualMachine vm) 
    {
        vm.writeVal();
    }

    @Override
    public void init(Vector<String> args){}
    
    @Override
    public String dumpOut(VirtualMachine vm)
    {
        return this.getByteCode();
    }
    
}