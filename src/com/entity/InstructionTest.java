package com.entity;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class InstructionTest {
	
	Instruction my = new Instruction("x^2*y","!d/dx");
	@Before
	public void setUp() throws Exception {
		my.setItem();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDerivative() {
		my.derivative();
		my.toResult();
		String result = my.getResult();
		assertEquals("2*x*y", result);
	}

}
