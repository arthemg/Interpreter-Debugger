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
public class PopCode extends ByteCode 
{
    public int stackPop;
    
    public PopCode()
    {
        super("PopCode");
    }
    
    @Override
    public void execute(VirtualMachine vm)
    {
        for(int i = 0; i < this.stackPop; i++)
        {
            vm.pop();
        }
    }
    
    @Override
    public void init(Vector<String> args)
    {
        if(!args.isEmpty())
        {
            this.stackPop = Integer.parseInt(args.get(0));
        }
    }
    
    @Override
    public String dumpOut(VirtualMachine vm)
    {
        return this.getByteCode();
    }
}
