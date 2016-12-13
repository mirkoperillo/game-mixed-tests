package eu.trentorise.game.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.GameConcept;

public class I110GameTest extends GameTest {

	private static final String GAME = "i110";
	private static final String ACTION = "save_itinerary";

	@Override
	public void initEnv() {
	}

	@Override
	public void defineGame() {

		List<GameConcept> concepts = new ArrayList<GameConcept>();
		concepts.add(new PointConcept("green leaves"));

		defineGameHelper(GAME, Arrays.asList(ACTION), concepts);

		// loadClasspathRules(
		// GAME,
		// Arrays.asList("rules/" + GAME + "/sentinel.drl", "rules/"
		// + GAME + "/setScorePoints.drl"));

		loadClasspathRules(
				GAME,
				Arrays.asList("rules/" + GAME + "/sentinel.drl", "rules/"
						+ GAME + "/incrementPoints.drl"));
	}

	@Override
	public void defineExecData(List<ExecData> execList) {

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("walk", 6);
		execList.add(new ExecData(GAME, ACTION, "macgargan", data));

		// data = new HashMap<String, Object>();
		// data.put("walk", 4);
		// execList.add(new ExecData(GAME, ACTION, "macgargan", data));
		//
		// data = new HashMap<String, Object>();
		// data.put("walk", 2);
		// execList.add(new ExecData(GAME, ACTION, "macgargan", data));
	}

	@Override
	public void analyzeResult() {
	}
}
