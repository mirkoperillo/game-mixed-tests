package eu.trentorise.game.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.GameConcept;

public class MultipleScoreUpdateGameTest extends GameTest {

    private static final String GAME_ID = "multiple_score_update";
    private static final String ACTION_ID = "child_distance";
    private static final String DOMAIN = "my-domain";

    @Override
    public void initEnv() {}

    @Override
    public void defineGame() {
        List<GameConcept> concepts = new ArrayList<>();
        concepts.add(new PointConcept("passi"));
        concepts.add(new BadgeCollectionConcept("medaglie"));
        defineGameHelper(DOMAIN, GAME_ID, Arrays.asList(ACTION_ID), concepts);

        try {
            loadClasspathRules(GAME_ID, "rules/" + GAME_ID);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void defineExecData(List<ExecData> execList) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("distance", 10);
        ExecData ex = new ExecData(GAME_ID, ACTION_ID, "child1", data);
        execList.add(ex);

    }

    @Override
    public void analyzeResult() {
        assertionPoint(GAME_ID, 30d, "child1", "passi");

    }
}
