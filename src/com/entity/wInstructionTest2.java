package com.entity;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class wInstructionTest2 {
	
	Instruction my = new Instruction("x^2*y","!d/dy");
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
		assertEquals("x^2", result);
	}

}
