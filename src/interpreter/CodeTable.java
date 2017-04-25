/**
 * The template was borrowed from Lexer program. Hashmap created to store all the 
 * */
package interpreter;

/**
 *
 * @author ArtsemHoldvekht
 */
public class CodeTable 
{
    private static final java.util.HashMap<String,String> codeTable = new java.util.HashMap<>();

    /**
     * Init function to add bytecodes
     */
    public static void init()
    {
        codeTable.put("HALT", "HaltCode");
        codeTable.put("POP", "PopCode");
        codeTable.put("HALT", "HaltCode");
        codeTable.put("POP", "PopCode");
        codeTable.put("FALSEBRANCH", "FalseBranchCode");
        codeTable.put("GOTO", "GotoCode");
        codeTable.put("STORE", "StoreCode");
        codeTable.put("LOAD", "LoadCode");
        codeTable.put("LIT", "LitCode");
        codeTable.put("ARGS", "ArgsCode");
        codeTable.put("CALL", "CallCode");
        codeTable.put("RETURN", "ReturnCode");
        codeTable.put("BOP", "BopCode");
        codeTable.put("READ", "ReadCode");
        codeTable.put("WRITE", "WriteCode");
        codeTable.put("LABEL", "LabelCode");
        codeTable.put("DUMP", "DumpCode");
    }
    
    
    /**
     * Getter method to retrieve byteCode or throw exception
     * @param byteCode
     * @return
     * @throws ByteCodeException 
     */
    public static String getCode(String byteCode) throws ByteCodeException
    {
        if(codeTable.containsKey(byteCode))
        {
            return codeTable.get(byteCode);
        }
        else
        {
            throw new ByteCodeException("Code not found, check if the code exists: " + byteCode);
        }
    }
}