/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.debugger;
import interpreter.CodeTable;

import interpreter.ByteCodeException;

/**
 *
 * @author Artsem
 */
public class DebugCodeTable {
     private static final java.util.HashMap<String,String> codeTable = new java.util.HashMap<>();

    /**
     * Init function to add bytecodes
     */
    public static void init()
    {
        codeTable.put("CALL", "DebugCallCode");
        codeTable.put("FORMAL", "DebugFormalCode");
        codeTable.put("FUNCTION", "DebugFunctionCode");
        codeTable.put("LINE", "DebugLineCode");
        codeTable.put("LIT", "DebugLitCode");
        codeTable.put("POP", "DebugPopCode");
        codeTable.put("RETURN", "DebugReturnCode");
    }
    
    
    /**
     * Getter method to retrieve byteCode or throw exception
     * @param byteCode
     * @return
     * @throws ByteCodeException 
     */
    public static String getDebugCode(String byteCode)
    {
        if(codeTable.containsKey(byteCode))
        {
            return codeTable.get(byteCode);
        }
        else
        {
            return null;
        }
    }
}
