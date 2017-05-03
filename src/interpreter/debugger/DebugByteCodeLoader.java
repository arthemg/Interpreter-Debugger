/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.debugger;

import interpreter.ByteCodeException;
import interpreter.ByteCodeLoader;
import interpreter.CodeTable;
import interpreter.Program;
import interpreter.bytecode.ByteCode;
import java.io.IOException;
import java.util.*;


/**
 *
 * @author Artsem
 */
public class DebugByteCodeLoader extends ByteCodeLoader 
{
    Set<Integer> breakPoints; //Set to keep track of the breakpoint Lines
    
    public DebugByteCodeLoader(String programFile) throws IOException 
    {
        super(programFile);
        this.breakPoints = new HashSet<>();
    }
    
    /**
     * Get the set of all lines that can have breakpoints
     * @return 
     */
    public Set<Integer> getBreakPoints()
    {
        return breakPoints;
    }
    

    /**
     * Walk line by line check if code exists and create codes newInstance
     * also init all if any argumetns are included with the bytecode
     * @return
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws ByteCodeException 
     */
    public Program loadCodes() throws IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, ByteCodeException 
    {       
        
        ByteCode bytecode = null;
        
        while (currString != null) 
        {
            //Read the next line
            currString = source.readLine();
            //init vector to store args after the Bytecode
            Vector<String> args = new Vector();
            
            if (currString != null) 
            {
                //Split String at the white spaces
                String[] splitString = currString.split("\\s");
                
                
                //if string not empty we start reading ByteCodes and create instanses
                if (splitString.length != 0) 
                {
                    //ByteCode exists we store it and create a class
                    String byteCode = splitString[0];
                    
                   
                    if(DebugCodeTable.getDebugCode(byteCode) != null)
                    {
                        if(byteCode.equals("LINE"))
                        {
                            breakPoints.add(Integer.parseInt(splitString[1]));
                        }
                        
                        String debugByteCodeInst = DebugCodeTable.getDebugCode(byteCode);
                        bytecode = (ByteCode) (Class.forName("interpreter.bytecode.debugbytecode." + debugByteCodeInst).newInstance());
                    }
                    else if (CodeTable.getCode(byteCode) != null) 
                    {
                        String byteCodeInst = CodeTable.getCode(byteCode);
                        
                        //New Instance for every bytecode
                        bytecode = (ByteCode) (Class.forName("interpreter.bytecode." + byteCodeInst).newInstance());
                    }
                        //Add args to bytecode
                        for (int i = 1; i < splitString.length; i++) 
                        {
                            args.add(splitString[i]);
                        }
                        //Call init for the ByteCode with its args
                        bytecode.init(args);

                        //Set all the byte code in the Constructs in interpreter.bytecode
                        bytecode.setByteCode(currString);
                        
                        //Used in part to resolve and set addresses for the Call/FalseBranch/Goto
                        program.setByteCodeAddress(bytecode);
                    }
                    else
                    {
                        System.out.println("There was an error! Good Bye");
                        System.exit(0);
                    }
                }
            }
        
        //resolve addresses to run the program
        program.resolveAddress();
        return program;
    }
}
