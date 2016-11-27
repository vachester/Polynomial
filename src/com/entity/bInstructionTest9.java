package com.entity;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.control.InstructionControl;

public class bInstructionTest9 {

	InstructionControl my = new InstructionControl("x","!324 x=2");
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
		assertEquals("Format error! Please input again!", result);
	}

}
