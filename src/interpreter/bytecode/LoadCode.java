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
public class LoadCode extends ByteCode 
{
    private int offset;
    private int args;
    private String comment;
    
    public LoadCode()
    {
        super("LoadCode");
    }
    
    @Override
    public void execute(VirtualMachine vm)
    {
        vm.loadStackOffset(this.offset);
    }
    
    @Override
    public void init(Vector<String> args)
    {
        if(!args.isEmpty())
        {
            this.offset = Integer.parseInt(args.get(0));
            this.args = args.size();
            
            if(args.size() == 2)
            {
                this.comment = args.get(1);
            }
            
        }
    }
    
    @Override
    public String dumpOut(VirtualMachine vm)
    {
        if(this.args == 1)
        {
            return this.getByteCode();
        }
        else
        {
            String debugMessage = this.getByteCode() + " " + this.comment;
            return debugMessage;
        }
    }
    
}