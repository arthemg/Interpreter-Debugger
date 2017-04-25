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
public class LabelCode extends ByteCode
{   
    private String label;
    
    public LabelCode()
    {
        super("LabelCode");
    }
    
    public void setLabel(String newLabel)
    {
        label = newLabel;
    }
    
    public String getLabel()
    {
        return this.label;
    }

    @Override
    public void execute(VirtualMachine vm){}

    @Override
    public void init(Vector<String> args) 
    {
        if(!args.isEmpty())
        {
            this.label = args.get(0);
        }
    }
    
    @Override
    public String dumpOut(VirtualMachine vm)
    {
        return this.getByteCode();
    }
}