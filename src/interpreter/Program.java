package interpreter;

import interpreter.bytecode.*;
import interpreter.bytecode.debugbytecode.DebugCallCode;
import java.util.*;



/**
 * 
 * @author ArtsemHoldvekht
 */
public class Program 
{

    /**
     * We will create multiple maps to store bytecodes and addresses of those codeLabels
     */
    private final HashMap<String, Integer> adressHash;            //Stores the hashMap of addresses
    private final Vector<ByteCode> vectorBC;                      //List of ByteCodes
    private final HashMap<String, Vector<ByteCode>> byteCodeHash; //Stores all byteCodes GoTo/Call/FalseBranch
    private int byteCodeAddress;

 
    public Program() 
    {
        vectorBC = new Vector<>();
        byteCodeHash = new HashMap<>();
        adressHash = new HashMap<>();
        byteCodeAddress = 0;
    }

    /**
     * Getter to get the address of the ByteCode
     * @param addr
     * @return
     * @throws ByteCodeException 
     */
    public ByteCode getByteCodeAddress(int addr) throws ByteCodeException 
    {
        if ((addr != 0) && (addr < vectorBC.size()))
        {
            return vectorBC.get(addr);
        }
        
        return vectorBC.get(addr);
    }
    
    
    public void resolveAddress() 
    {
        //Iterate through the hashmap each label and set addresses to those specific
        //codeLabels to deffirintiate between them
        adressHash.keySet().stream().forEach((addr) -> 
        {
            //get addres of the first ByteCode in hash
            int address = adressHash.get(addr);
            
            //"Loop" through all the key/value pairs in the hash
            Vector byteCodeInHash = byteCodeHash.get(addr);

            if (byteCodeInHash != null) {
                //use iterator to to simplyfire the process of looping through all the elements
                Iterator it = byteCodeInHash.iterator();
                
                while (it.hasNext())
                {
                    //cast each item to byteCode type
                    ByteCode byteCode = (ByteCode) it.next();
                    
                    //get bytecode name
                    String byteCodeName = byteCode.getByteCodeName();
                    
                    //switch to resolve addresses for all codeLabels
                    switch (byteCodeName) {
                        case "CallCode":
                            //cast Bytecode to a proper type
                            CallCode callCode = (CallCode) byteCode;
                            //set address
                            callCode.setAddress(Integer.toString(address));
                            //System.out.println("FalseBranchCode: "+address);
                            break;

                        case "FalseBranchCode":
                            FalseBranchCode falseBranchCode = (FalseBranchCode) byteCode;
                            falseBranchCode.setAddress(Integer.toString(address));
                            //System.out.println("FalseBranchCode: "+address);
                            break;

                        case "GotoCode":
                            GotoCode gotoCode = (GotoCode) byteCode;
                            gotoCode.setAddress(Integer.toString(address));
                        //System.out.println("GotoCode: "+address);
                        break;
                        case "DebugCallCode":
                            callCode = (DebugCallCode) byteCode;
                            callCode.setAddress(Integer.toString(address));
                    }
                }
            }
        });
}
    /**
     * Setter for the address for GotoCode, CallCode, FalseBranchCode
     * @param bytecode 
     */
    public void setByteCodeAddress(ByteCode bytecode) throws ByteCodeException
    {
        String byteCodeName = bytecode.getByteCodeName();

        if (byteCodeName.equals("CallCode") || byteCodeName.equals("FalseBranchCode") || byteCodeName.equals("GotoCode")|| byteCodeName.equals("DebugCallCode"))
        {
            String codeLabel = null;
            //Swtich to retrieve addresses based on the codeName and add them to map later
            switch(byteCodeName)
            {
                case "CallCode":
                    //cast to appropriate type of byte code
                    CallCode callCode = (CallCode) bytecode;
                    //get address
                    codeLabel = callCode.getAddress();
                    break;
                case "FalseBranchCode":
                    FalseBranchCode falseBranchCode = (FalseBranchCode) bytecode;
                    codeLabel = falseBranchCode.getAddress();
                    break;
                case "GotoCode":
                    GotoCode gotoByteCode = (GotoCode) bytecode;
                    codeLabel = gotoByteCode.getAddress();
                    break;
                case "DebugCallCode":
                    callCode = (DebugCallCode) bytecode;
                    codeLabel = callCode.getAddress();
            }
           
            if (!byteCodeHash.containsKey(codeLabel))
            {
                //Lable doesnt exist create new vector and append it to the
                //hashMap
                Vector<ByteCode> missingByteCode = new Vector<>();
                missingByteCode.add(bytecode);

                //add new label to the hashMap of bytecodes
                byteCodeHash.put(codeLabel, missingByteCode);
                
            }
            else
            {
                //if label exists add it to the vector
                Vector foundByteCode = byteCodeHash.get(codeLabel);

                foundByteCode.add(bytecode);
            }
        }
        //separate case for LabelCode since we might have duplicates
        if (byteCodeName.equals("LabelCode"))
        {
            LabelCode labelCode = (LabelCode) bytecode;
            String labelCodeName = labelCode.getLabel();
            if (!adressHash.containsKey(labelCodeName))
            {
                adressHash.put(labelCodeName, byteCodeAddress);
                
            }
        }
        //add byteceodes increment the address
        vectorBC.add(bytecode);
        byteCodeAddress++;

    }
}