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
public class ArgsCode extends ByteCode 
{
    private int args;

    public ArgsCode() 
    {
        super("АrgsCode");
    }
    
    @Override
    public void execute(VirtualMachine vm) 
    {
        vm.addFrame(this.args);
    }
 
    @Override
    public void init(Vector<String> args) 
    {
        if(!args.isEmpty())
        {
            this.args = Integer.parseInt(args.get(0));
        }
    }
    

    @Override
    public String dumpOut(VirtualMachine vm)
    {
        return this.getByteCode();
    }
}