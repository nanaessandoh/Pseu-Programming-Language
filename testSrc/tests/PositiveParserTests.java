package tests;

import static org.junit.Assert.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


import pseu.parser.PseuParser;

@RunWith(Parameterized.class)
public class PositiveParserTests {
	
	private String name ;
	private String input ;
	
	@Parameters( name = "{index}: {0}" )
	public static Iterable<Object[]> data() {
		return Arrays.asList( new Object[][] {
			{"No semicolon after if", "if a then a := b else end if b := a" },
			{"Semicolon after if",    "if a then a := b ; else end if ; b := a"},
			{"No semicolon after fun decl", "fun f() do end fun    1" },
			{"No semicolon after fun decl", "fun f() do end fun ;  1" },
		} ) ;
	}
	
	public PositiveParserTests( String name, String input ) {
		this.name = name ;
		this.input = input ;
	}
	

	@Test
	public void test() {
		Reader reader = new StringReader( input ) ;
		PseuParser parser = new PseuParser( reader ) ;
		MockBuilder builder = new MockBuilder() ;
		parser.setBuilder( builder ) ;
		try { 
			parser.File();
		} catch( Throwable t) {
			System.out.println( t.getMessage() ) ;
			throw new AssertionError(t) ;
		}
	}

	public String getName() {
		return name;
	}
}
