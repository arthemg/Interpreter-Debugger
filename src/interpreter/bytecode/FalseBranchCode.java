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
public class FalseBranchCode extends ByteCode
{
    private String jump; //jump to this address
    
    public FalseBranchCode()
    {
        super("FalseBranchCode");
    }
    
    public void setAddress(String address)
    {
        this.jump = address;
    }
    
    public String getAddress()
    {
        return this.jump;
    }
    
    @Override
    public void execute(VirtualMachine vm)
    {
        int branch = vm.pop();
        if(branch == 0)
        {
            vm.setProgramCounter(Integer.parseInt(jump));
        }
    }
    
    @Override
    public void init(Vector<String> args)
    {
        if(!args.isEmpty())
        {
            this.jump = args.get(0);
        }
    }
    
    @Override
    public String dumpOut(VirtualMachine vm)
    {
        return this.getByteCode();
    }
    
}
