/**
 * ByteCodeLoader is used to scan through the file and load all the ByteCodes
 * and its arguments into HashMaps.
 */
package interpreter;

import interpreter.bytecode.ByteCode;
import java.io.*;
import java.util.*;

/**
 *
 * @author ArtsemHoldvekht
 */
public class ByteCodeLoader 
{

    public Program program;
    public final BufferedReader source;
    public String currString;

    /**
     * Open the file and load it into the BufferedReader
     * @param programFile
     * @throws IOException 
     */
    public ByteCodeLoader(String programFile) throws IOException 
    {
        program = new Program();
        currString = "";
        source = new BufferedReader(new FileReader(programFile));
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
                    
                    if (CodeTable.getCode(byteCode) != null) 
                    {
                        String byteCodeInst = CodeTable.getCode(byteCode);
                        
                        //New Instance for every bytecode
                        ByteCode bytecode = (ByteCode) (Class.forName("interpreter.bytecode." + byteCodeInst).newInstance());

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
        }
        
        //resolve addresses to run the program
        program.resolveAddress();
        return program;
    }
}