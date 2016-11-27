package com.entity;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.control.InstructionControl;

public class bInstructionTest {

	InstructionControl my = new InstructionControl("x*2","!simplify x=2");
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
		assertEquals("4", result);
	}

}
