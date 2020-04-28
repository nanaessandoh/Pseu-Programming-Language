package tests;

import pseu.codeStore.CodeTarget;
import pseu.codeStore.InstructionFactoryI;
import pseu.codeStore.InstructionI;
import pseu.common.CompileTimeError;
import pseu.common.SourceCoords;
import pseu.interpreter.AnalyzerAndGeneratorI;

public class MockAnalyser implements AnalyzerAndGeneratorI<MockAST>{

	@Override
	public <Instr extends InstructionI>
	void analyzeAndGenerate(
			MockAST block,
			InstructionFactoryI<Instr> f,
			CodeTarget<Instr> codeStore)
	throws CompileTimeError {
		SourceCoords sc = new SourceCoords("a.pseu", 1, 2, "a.pseu", 3, 4) ;
		codeStore.startNewSegment() ;
		// fun (a : Int, b : Int) : Int do a+b end fun
		codeStore.emit( f.pushString("a", sc) ) ;
		codeStore.emit( f.constructType("Int", 0, sc) ) ;
		codeStore.emit( f.pushString("b", sc) ) ;
		codeStore.emit( f.constructType("Int", 0, sc) ) ;
		codeStore.emit( f.constructType("Int", 0, sc) ) ;
		codeStore.emit( f.pushInt("1", sc) ) ;
		codeStore.emit( f.makeClosure(2, sc) ) ;
		
		codeStore.emit( f.pushInt("88", sc) ) ;
		codeStore.emit( f.pushInt("100", sc) ) ;
		codeStore.emit( f.makeTuple(2, sc) ) ;
		codeStore.emit( f.apply(sc) ) ;
		
		codeStore.startNewSegment() ;
		codeStore.emit( f.newFrame(0, sc) ) ;
		codeStore.emit( f.pushLocation(1, 0, sc) ) ;
		codeStore.emit( f.fetch(sc) ) ;
		codeStore.emit( f.pushString("binary(+)", sc) ) ;
		codeStore.emit( f.lookup( sc ) ) ;
		codeStore.emit( f.pushLocation(1, 1, sc) ) ;
		codeStore.emit( f.fetch(sc) ) ;
		codeStore.emit( f.apply( sc ) ) ;
		codeStore.emit( f.returnNow(sc) ) ;
		codeStore.emit( f.popFrame(sc) ) ;
	}
	
}
