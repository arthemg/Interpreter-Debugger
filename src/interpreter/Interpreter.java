package interpreter;

import interpreter.debugger.DebugByteCodeLoader;
import interpreter.debugger.DebugCodeTable;
import interpreter.debugger.ui.UI;
import java.io.*;
import java.util.*;

/**
 * <pre>
 * 
 *  
 *   
 *     Interpreter class runs the interpreter:
 *     1. Perform all initializations
 *     2. Load the bytecodes from file
 *     3. Run the virtual machine
 *     
 *   
 *  
 * </pre>
 */
public class Interpreter {

	ByteCodeLoader bcl;

	public Interpreter(String codeFile) {
		try {
			CodeTable.init();
			bcl = new ByteCodeLoader(codeFile);
		} catch (IOException e) {
			System.out.println("**** " + e);
		}
	}

	void run() throws IOException, ByteCodeException, InstantiationException, ClassNotFoundException,IllegalAccessException {
		Program program = bcl.loadCodes();
		VirtualMachine vm = new VirtualMachine(program);
		vm.executeProgram();
	}

	public static void main(String args[]) throws IOException, ByteCodeException, InstantiationException, ClassNotFoundException,IllegalAccessException {
                String sourceFile = "";	
                String byteCodeFile ="";
                if (args.length == 0) {
			System.out.println("***Incorrect usage, try: java interpreter.Interpreter <file>");
			System.exit(1);
		}
                
                if(args[0].equals("-d"))
                {
                    sourceFile = args[1] + ".x";
                    byteCodeFile = args[1] + ".x.cod";
                    
                    CodeTable.init();
                    DebugCodeTable.init();
                    
                    DebugByteCodeLoader bcl = new DebugByteCodeLoader(byteCodeFile);
                    Program program = bcl.loadCodes();
                    
                    Set<Integer> breakPoints = bcl.getBreakPoints();
                    
                    new UI(program, sourceFile, breakPoints).run();
                }
                else
                {
                    byteCodeFile = args[0];
                    (new Interpreter(byteCodeFile)).run();
                }
	}
}