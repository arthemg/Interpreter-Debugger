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
public class ReturnCode extends ByteCode 
{
    private int args;
    private int returnAddr;
    private String comment;

    public ReturnCode() 
    {
        super("ReturnCode");
    }

    @Override
    public void execute(VirtualMachine vm) 
    {
        vm.popFrame();
        returnAddr = vm.popAddress();
        vm.setProgramCounter(returnAddr);
    }

    @Override
    public void init(Vector<String> args) 
    {
        if(!args.isEmpty())
        {
            this.comment = args.get(0);
            this.args = args.size();
        }
    }
    
    @Override
    public String dumpOut(VirtualMachine vm)
    {
        if(this.args == 0)
        {
            return this.getByteCode();
        }
        else
        {   
            int peekStack = vm.peek();
            String debugMessage = this.getByteCode()+ "("+ peekStack + ")";
            return debugMessage;
            
            
        }
    }
}