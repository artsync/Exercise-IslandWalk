package nl.arthurvlug.islandwalk;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class State {
	private final int x;
	private final int y;
	private final int numSteps;
}
