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

public class DumpCode extends ByteCode 
{
    String check;

    public DumpCode() 
    {
       super("DumpCode");
    }

    @Override
    public void execute(VirtualMachine vm) 
    {
        if(this.check.equals("ON"))
        {
            vm.dumpIsOn();
        }
        else
        {
            vm.dumpIsOff();
        }
    }

    @Override
    public void init(Vector<String> args) 
    {
        if(!args.isEmpty())
        {
            this.check = args.get(0);
        }
    }

    @Override
    public String dumpOut(VirtualMachine vm) 
    {
        return null;
    }
    
}
