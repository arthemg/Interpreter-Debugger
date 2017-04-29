/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.debugger;



/**
 *
 * @author ArtsemHoldvekht
 */
public class FunctionEnvironmentRecord 
{
    private int startLine;
    private int endLine;
    private int currLine;
    private String funcName;
    private final SymbolTable symTable;
    
    public FunctionEnvironmentRecord()
    {
        startLine = -1;
        endLine = -1;
        currLine = -1;
        funcName = "-";
        symTable = new SymbolTable();
    }
    
    public void beginScope()
    {
        symTable.beginScope();
    }
    
    public void setFunctionInfo(String name, int startLine, int endLine)
    {
        this.funcName = name;
        this.startLine = startLine;
        this.endLine = endLine;
    }
    
    public void setCurrentLineNumber(int currentLine)
    {
        this.currLine = currentLine;
    }
     
    public void setVarVal(String sym, int val)
    {
        symTable.put(sym, val);
    }
    
    public void doPop(int val)
    {
        symTable.pop(val);
    }

    public int getStartLine() 
    {
        return startLine;
    }

    public void setStartLine(int startLine) 
    {
        this.startLine = startLine;
    }

    public int getEndLine() 
    {
        return endLine;
    }

    public void setEndLine(int endLine) 
    {
        this.endLine = endLine;
    }

    public String getFuncName() 
    {
        return funcName;
    }

    public void setFuncName(String funcName) 
    {
        this.funcName = funcName;
    }
     public int getCurrLine() 
    {
        return currLine;
    }
    
     public SymbolTable getSymbolTable()
     {
         return this.symTable;
     }
    
    public void dump()
    {
        String debugMessg = "(<" + symTable.toString() + ">,";
        if(funcName == null)
        {
            debugMessg +="-,";
        }
        else
        {
            debugMessg += funcName + ",";
        }
        if(startLine == -1)
        {
            debugMessg += "-,";
        }
        else
        {
            debugMessg += startLine + ",";
        }
        if(endLine == -1)
        {
            debugMessg += "-,";
        }
        else
        {
            debugMessg += endLine + ",";
        }
        if(currLine == -1) 
        {
            debugMessg += "-)";
        }
        else
        {
            debugMessg+= currLine + ")";
        }
        
        System.out.println(debugMessg);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
       FunctionEnvironmentRecord fctEnvRecord = new FunctionEnvironmentRecord();
       fctEnvRecord.beginScope();
       fctEnvRecord.dump();
       fctEnvRecord.setFunctionInfo("g", 1, 20);
       fctEnvRecord.dump();
       fctEnvRecord.setCurrentLineNumber(5);
       fctEnvRecord.dump();
       fctEnvRecord.setVarVal("a", 4);
       fctEnvRecord.dump();
       fctEnvRecord.setVarVal("b", 2);
       fctEnvRecord.dump();
       fctEnvRecord.setVarVal("c", 7);
       fctEnvRecord.dump();
       fctEnvRecord.setVarVal("a", 1);
       fctEnvRecord.dump();
       fctEnvRecord.doPop(2);
       fctEnvRecord.dump();
       fctEnvRecord.doPop(1);
       fctEnvRecord.dump();
    }
}
