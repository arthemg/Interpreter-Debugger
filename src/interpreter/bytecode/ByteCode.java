package interpreter.bytecode;

import interpreter.VirtualMachine;
import java.util.*;
/*  The ByteCode Abstract class is the Abstract Syntax Tree representation;
 *  used for printing/debugging
/**
 *
 * @author ArtsemHoldvekht
 */
public abstract class ByteCode 
{   
    private String byteCodeName;  //name of the byte code
    private String byteCode;      // actuall byte code
   
   /**
    * Default constructor to set the name of byte code
    * used laterin program to resolve addresses and label
    * @param byteCodeName 
    */
    public ByteCode(String byteCodeName)
    {
        this.byteCodeName = byteCodeName;
    }
    
    /**
     * Setter for the Byte Code Name
     * @param newName 
     */
    public void setByteCodeName(String newName)
    {
        this.byteCodeName = newName;
    }
    /**
     * Set the name for the bytecode e.g. HaltCode, LitCode etc
     * @return 
     */
    public String getByteCodeName()
    {
        return this.byteCodeName;
    }
    /**
     * Setter for actuall byte code e.g. LIT, HALT etc
     * @param newByteCode 
     */
    public void setByteCode(String newByteCode)
    {
        byteCode = newByteCode;
    }
    
    /**
     * Get actual bytecode
     * @return 
     */
    public String getByteCode()
    {
        return byteCode;
    }
    
    /**
     * Abstract methods to be implemented for each ByteCode
     * @param vm 
     */
    public abstract void execute(VirtualMachine vm);
    
    public abstract void init(Vector<String> args);
    
    //Method to print debug messages
    public abstract String dumpOut(VirtualMachine vm);
    
    
    
}
