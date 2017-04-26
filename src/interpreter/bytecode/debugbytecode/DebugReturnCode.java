/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.bytecode.debugbytecode;
import interpreter.bytecode.ReturnCode;
import java.util.*;
import interpreter.debugger.DebuggerVM;

/**
 *
 * @author Artsem
 */
public class DebugReturnCode extends ReturnCode
{
   public DebugReturnCode()
   {
       this.byteCodeName = "DebugReturnCode";
   }
}
