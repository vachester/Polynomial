import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PolynomialTest2 {
	Polynomial test = new Polynomial();
	@Before
	public void setUp() throws Exception {
		test.expression("x^2*y");
		test.restore();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDerivative() {
		test.derivative("!d/dy");
		test.combine();
		test.show();
		String result = test.toString();
		assertEquals("x^2", result);
	}

}
