package com.entity;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.control.InstructionControl;

public class bInstructionTest3 {

	InstructionControl my = new InstructionControl("x","!simplify");
	@Before
	public void setUp() throws Exception {
		my.toInstruction();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSimplify() {
		String result = my.getResult();
		assertEquals("x", result);
	}

}
