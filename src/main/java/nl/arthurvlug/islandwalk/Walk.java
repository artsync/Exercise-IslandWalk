package nl.arthurvlug.islandwalk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

/**
 * There is an island which is represented by square matrix NxN. 
 * A person on the island is standing at any given co-ordinates (x,y).
 * He can move in any direction one step right, left, up, down on the island.
 * If he steps outside the island, he dies. 
 * 
 * Let island is represented as (0,0) to (N-1,N-1) (i.e NxN matrix) & person is standing at given co-ordinates (x,y).
 * He is allowed to move n steps on the island (along the matrix).
 * What is the probability that he is alive after he walks n steps on the island? 
 * 
 * Write a psuedocode & then full code for function 
 * " float probabilityofalive(int x,int y, int n) ".
 * 
 * See: http://www.careercup.com/question?id=15556758
 * 
 * @author arthur
 *
 */
@Log4j
@AllArgsConstructor
public class Walk {
	private static final int THREADS = 8;
	private int islandSize;

	/**
	 * Calculates all probabilities for the island, given we have to take `numSteps` steps.
	 * 
	 * @param numSteps The amount of steps we need to take
	 * @return An object that contains the probability for each field.
	 */
	ProbabilityContainer createArray(int numSteps) {
		// Do dynamic programming
		ProbabilityContainer result = new ProbabilityContainer(islandSize);
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREADS);
		for(int step = 0; step <= numSteps; step++) {
			result = calculateArrayForStep(step, result, executor);
		}
		executor.shutdown();
		return result;
	}

	/**
	 * Calculate all fields on the element given we have `numSteps` steps left. 
	 * @param numSteps The amount of steps we still need to take
	 * @param executor The ThreadPoolExecutor
	 * @param previousResult The probability for each field, assuming we had `numSteps`-1 steps left.
	 * @return The calculated survival chances for the whole island after `numSteps` steps.
	 */
	private ProbabilityContainer calculateArrayForStep(final int numSteps, final ProbabilityContainer previousResult, ThreadPoolExecutor executor) {
		final ProbabilityContainer newProbabilityContainer = new ProbabilityContainer(islandSize);
		
		List<Callable<Void>> tasks = new ArrayList<>();
		for(int x = 0; x < islandSize; x++) {
			final int finalX = x;
			for(int y = 0; y < islandSize; y++) {
				final int finalY = y;
				tasks.add(new Callable<Void>() {
					public Void call() {
						try {
							// Calculate the score for the current state
							double newSurvivalScore = calculateSurvivalScore(new State(finalX, finalY, numSteps), previousResult);
							newProbabilityContainer.set(finalX, finalY, newSurvivalScore);
							return null;
						} catch (Exception e) {
							log.error(e);
							throw new RuntimeException(e);
						}
					}
				});
			}
		}
		try {
			executor.invokeAll(tasks);
		} catch (InterruptedException e) {
			log.error(e);
			throw new RuntimeException(e);
		}
		
		return newProbabilityContainer;
	}

	/**
	 * Calculates the chance of survival by recursion (dynamic programming).
	 * 
	 * @param state The current state
	 * @param previousResult The probability for each field, assuming we had `numSteps`-1 steps left.
	 * @return The chance of survival
	 */
	private double calculateSurvivalScore(State state, ProbabilityContainer previousResult) {
		if (!isInRange(state.getX(), state.getY())) {
			// We're dead.
			return 0;
		}
		if (state.getNumSteps() == 0) {
			// We're still alive. We don't have to do any steps, so we're fine.
			return 1;
		}

		double sumSurvivalChance = 0;

		// Go left, right, up, down
		sumSurvivalChance += survivalChance(state.getX() - 1, state.getY(), previousResult);
		sumSurvivalChance += survivalChance(state.getX() + 1, state.getY(), previousResult);
		sumSurvivalChance += survivalChance(state.getX(), state.getY() - 1, previousResult);
		sumSurvivalChance += survivalChance(state.getX(), state.getY() + 1, previousResult);

		return sumSurvivalChance / 4;
	}

	/**
	 * Calculates the survival chance given the coordinates and the chance of survival with 1 step less
	 * @param x The x position
	 * @param y The y position
	 * @param previousResult The probability for each field, assuming we had `numSteps`-1 steps left.
	 * @return
	 */
	private double survivalChance(int x, int y, ProbabilityContainer previousResult) {
		if(isInRange(x, y)) {
			return previousResult.get(x, y);
		} else {
			return 0;
		}
	}

	/**
	 * Returns whether the given coordinates are on the island
	 * @param x The x position
	 * @param y The y position
	 * @return True if the coordinates are on this island. False otherwise.
	 */
	private boolean isInRange(int x, int y) {
		return x >= 0 && y >= 0 && x < islandSize && y < islandSize;
	}
}
