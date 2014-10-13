package nl.arthurvlug.islandwalk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class WalkTest {
	@Test
	public void testCreateArray1x1_0() {
		Walk walk = new Walk(1);
		ProbabilityContainer probabilityContainer = walk.createArray(0);
		ProbabilityContainer expected = new ProbabilityContainer(new double[][] {
				{ 1.0 }
		});
		assertEquals(expected, probabilityContainer);
	}

	@Test
	public void testCreateArray1x1_1() {
		Walk walk = new Walk(1);
		ProbabilityContainer probabilityContainer = walk.createArray(1);
		ProbabilityContainer expected = new ProbabilityContainer(new double[][] {
				{ 0.0 }
		});
		assertEquals(expected, probabilityContainer);
	}

	@Test
	public void testCreateArray2x2_0() {
		Walk walk = new Walk(2);
		ProbabilityContainer probabilityContainer = walk.createArray(0);
		ProbabilityContainer expected = new ProbabilityContainer(new double[][] {
				{ 1.0, 1.0 },
				{ 1.0, 1.0 }
		});
		assertEquals(expected, probabilityContainer);
	}

	@Test
	public void testCreateArray2x2_1() {
		Walk walk = new Walk(2);
		ProbabilityContainer probabilityContainer = walk.createArray(1);
		ProbabilityContainer expected = new ProbabilityContainer(new double[][] {
				{ 0.5, 0.5 },
				{ 0.5, 0.5 }
		});
		assertEquals(expected, probabilityContainer);
	}

	@Test
	public void testCreateArray3x3_1() {
		Walk walk = new Walk(3);
		ProbabilityContainer probabilityContainer = walk.createArray(1);
		ProbabilityContainer expected = new ProbabilityContainer(new double[][] {
				{ 0.5, 0.75, 0.5 },
				{ 0.75, 1.0, 0.75 },
				{ 0.5, 0.75, 0.5 }
		});
		assertEquals(expected, probabilityContainer);
	}

	@Test
	public void testCreateArray3x3_2() {
		Walk walk = new Walk(3);
		ProbabilityContainer probabilityContainer = walk.createArray(2);
		ProbabilityContainer expected = new ProbabilityContainer(new double[][] {
				{ 0.375, 0.5, 0.375 },
				{ 0.5, 0.75, 0.5 },
				{ 0.375, 0.5, 0.375 }
		});
		assertEquals(expected, probabilityContainer);
	}

	@Test
	public void testCreateArray2x2_1000() {
		int STEPS = 1000;

		Walk walk = new Walk(2);
		ProbabilityContainer probabilityContainer = walk.createArray(STEPS);

		double survivalProbabilty = Math.pow(0.5, STEPS);
		ProbabilityContainer expected = new ProbabilityContainer(new double[][] {
				{ survivalProbabilty, survivalProbabilty },
				{ survivalProbabilty, survivalProbabilty }
		});
		assertTrue(survivalProbabilty > 0.0);
		assertEquals(expected, probabilityContainer);
	}
}
