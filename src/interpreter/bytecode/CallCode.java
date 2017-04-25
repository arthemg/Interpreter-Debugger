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
public class CallCode extends ByteCode 
{
    private String addr;
    
    public CallCode()
    {
        super("CallCode");
    }
    
    public void setAddress(String address)
    {
        
        this.addr = address;
        
    }
    
    public String getAddress()
    {
       
        return this.addr;
    }
    
    
   
    @Override
    public void execute(VirtualMachine vm) 
    {
        vm.pushAddress();
        vm.setProgramCounter(Integer.parseInt(this.addr));
    }

    @Override
    public void init(Vector<String> args) 
    {
        if(!args.isEmpty())
        {
            this.addr = args.get(0);
        }
    }

    
    @Override
    public String dumpOut(VirtualMachine vm) 
    {
        Vector argList = vm.getArgs();
        
        String debugMessage = this.getByteCode() + "(";

        //Add all args to the debug message
        for (int i = 0; i < argList.size(); i++)
        {
            debugMessage += argList.get(i);
            if (i != (argList.size() - 1))
            {
                debugMessage += ",";
            }
        }
        debugMessage += ")";
        return debugMessage;
    }
}