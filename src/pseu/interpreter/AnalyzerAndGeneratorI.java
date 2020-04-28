package pseu.interpreter;

import pseu.codeStore.CodeTarget;
import pseu.codeStore.InstructionFactoryI;
import pseu.codeStore.InstructionI;
import pseu.common.CompileTimeError;

public interface AnalyzerAndGeneratorI<AST> {

	
	/**  Do all compilation compilation steps after tree building.
	 * <p> The analyze method should be generic with respect to the type of the instructions.
	 * <p> This will let you test your analyzer with a mock instruction type.
	 * It will also allow us to create code with one team's analyzer and run that
	 * code with another team's virtual machine.  For example if Team X has
	 * an AST type of AST_X and team Y has a instruction type Instr_Y we should
	 * be able to do the following.
	 * <pre>
	 *      // Analyze with Team X's analyzer, using team Y's instruction factory.
	 *      	CodeStore&lt;Instr_Y&gt; codeStore = new CodeStore&lt;Instr_Y&gt;() ;
	 *      	Analyzer&lt;AST_X&gt;  analyzer = new Analyzer_X() ;
	 *      	analyzer.&lt;Instr_Y&gt;analyze(ast, instructionFactory, codeStore) ;
	 *      
	 *      // Execute on team Y's virtual machine.
	 *          PSAM_I&lt;Instr_Y&gt; psam = new PSAM_Y() ;
	 *      	psam.run( codeStore ) ;
	 * </pre>
	 * <p>
	 * This way of doing things ensures that there is no coupling between your
	 * analyzer and your (or anyone else's) virtual machine.  I.e. neither depends
	 * (at compile time) on the other.  However your instruction type may depend on
	 * the virtual machine, which allows you to use the command pattern.
	 * 
	 * @param ast  The thing produced by the builder.
	 * @param instrFactory  A factory for instructions of type Instr
	 * @param codeStore A place to put instructions of type Instr
	 * 
	 * @throws CompileTimeError  If there are any errors in the code detected.
	 */
	<Instr extends InstructionI>
	void analyzeAndGenerate( AST block,
			      InstructionFactoryI<Instr> instrFactory,
			      CodeTarget<Instr> codeStore )
	throws CompileTimeError ;	
}
