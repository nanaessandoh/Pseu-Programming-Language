package tests;



import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import pseu.tsn.psam.Instruction;
import pseu.tsn.psam.InstructionFactory;
import pseu.tsn.psam.PSAM;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NegativeExecutionTestsTSN extends NegativeExecutionTests<Instruction> {

	@BeforeAll
	public void setup() {
		this.f = new InstructionFactory() ;
		this.machine = new PSAM() ;
	}
}
