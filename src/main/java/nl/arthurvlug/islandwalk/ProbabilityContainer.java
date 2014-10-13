package nl.arthurvlug.islandwalk;

import java.util.Arrays;

import lombok.AllArgsConstructor;

/**
 * Object that contains the survival chance on each position on the island
 * @author arthur
 *
 */
@AllArgsConstructor
public class ProbabilityContainer {
	private double[][] probabilities;
	
	public ProbabilityContainer(int size) {
		this.probabilities = new double[size][size];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				this.probabilities[i][j] = 1;
			}
		}
	}

	public double get(int x, int y) {
		return probabilities[x][y];
	}

	public void set(int i, int j, double value) {
		probabilities[i][j] = value;
	}
	
	@Override
	public boolean equals(Object obj) {
		ProbabilityContainer other = (ProbabilityContainer) obj;
		boolean deepEquals = Arrays.deepEquals(probabilities, other.probabilities);
		return deepEquals;
	}
	
	@Override
	public String toString() {
		return Arrays.deepToString(probabilities);
	}
}
