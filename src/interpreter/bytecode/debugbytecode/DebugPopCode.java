/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.bytecode.debugbytecode;
import interpreter.VirtualMachine;
import interpreter.debugger.DebuggerVM;
import interpreter.bytecode.PopCode;
import java.util.*;

/**
 *
 * @author Artsem
 */
public class DebugPopCode extends PopCode
{
    public DebugPopCode()
    {
        this.byteCodeName = "DebugPopCode";
    }
}
