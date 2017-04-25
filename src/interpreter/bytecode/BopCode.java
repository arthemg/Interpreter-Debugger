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
public class BopCode extends ByteCode 
{
    private String operation;

    public BopCode()
    {
        super("BopCode");
    }
    
    @Override
    public void execute(VirtualMachine vm) 
    {
        String oper = this.operation;   
        int operand1;
        int operand2;
        int result = 0;
        operand2 = vm.pop();
        operand1 = vm.pop();
        
        switch(oper)
        {
            case"+":
                result = operand1 + operand2;
                break;
            case"-":
                result = operand1 - operand2;
                break;
            case"/":
                result = operand1 / operand2;
                break;
            case "*":
                result = operand1 * operand2;
                break;
            case "==":
                if(operand1 == operand2)
                {
                    result = 1;
                }
                else
                {
                    result = 0;
                }
                break;
            case "!=":
                if(operand1 != operand2)
                {
                    result = 1;
                }
                else
                {
                    result = 0;
                }
                break;
            case "<=":
                if(operand1 <= operand2)
                {
                    result = 1;
                }
                else
                {
                    result = 0;
                }
                break;
            case">":
                if(operand1 > operand2)
                {
                    result = 1;
                }
                else
                {
                    result = 0;
                }
                break;
            case">=":
                if(operand1 >= operand2)
                {
                    result = 1;
                }
                else
                {
                    result = 0;
                }
                break;
            case"<":
                if(operand1 < operand2)
                {
                    result = 1;
                }
                else
                {
                    result = 0;
                }
                break;
            case"|":
                if((operand1 == 0) && (operand2==0))
                {
                    result = 0;
                }
                else
                {
                    result = 1;
                }
                break;
            case"&":
                if((operand1 == 1) && (operand2==1))
                {
                    result = 1;
                }
                else
                {
                    result = 0;
                }
                break;
        }
        vm.push(result);
      
    }

    @Override
    public void init(Vector<String> args) 
    {
        if(!args.isEmpty())
        {
            this.operation = args.get(0);
        }
    }
    
    @Override
    public String dumpOut(VirtualMachine vm)
    {
        return this.getByteCode();
    }
    
}
