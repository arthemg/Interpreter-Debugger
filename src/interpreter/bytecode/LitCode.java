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
public class LitCode extends ByteCode 
{
    private int val;
    private int args;
    private String comment;
    public LitCode()
    {
        super("LitCode");
    }

    @Override
    public void execute(VirtualMachine vm) 
    {
       vm.push(this.val);
    }

    @Override
    public void init(Vector<String> args) 
    {
        if(args.size() == 1)
        {
            this.args = 1;
            this.val = Integer.parseInt(args.get(0));
            this.comment = null;
        }
        else
        {
            this.args = 2;
            this.val = Integer.parseInt(args.get(0));
            this.comment = args.get(1);
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
            String debugMessage = this.getByteCode() + " int " + this.comment;
            return debugMessage;
        }
    }
}