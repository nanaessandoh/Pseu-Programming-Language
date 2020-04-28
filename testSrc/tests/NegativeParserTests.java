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
import pseu.parser.ParseException;

@RunWith(Parameterized.class)
public class NegativeParserTests {
	
	private String name ;
	private String input ;
	
	@Parameters( name = "{index}: {0}" )
	public static Iterable<? extends Object> data() {
		return Arrays.asList( new Object[][] {
			{"No semicolon after assign", "a := b c := d" },
			{"No semicolon after val decl", "val a : Int := 1 a,b := b,a"},
			{"No semicolon after var decl", "var a : Int  a,b := b,a"}
		} ) ;
	}
	
	public NegativeParserTests( String name, String input ) {
		this.name = name ;
		this.input = input ;
	}
	

	@Test
	public void test() {
		Reader reader = new StringReader( input ) ;
		PseuParser parser = new PseuParser( reader ) ;
		MockBuilder builder = new MockBuilder() ;
		parser.setBuilder( builder ) ;
		boolean ok = false ;
		try { 
			parser.File();
		} catch(ParseException t) {
			ok = true ;
		}
		assertTrue( ok ) ;
	}

	public String getName() {
		return name;
	}
}
