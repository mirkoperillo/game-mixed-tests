package eu.trentorise.game.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.GameConcept;

public class ChildExplorerGame extends GameTest {

    private static final String GAME_ID = "childExplorer";
    private static final String ACTION_ID = "child_distance";

    private static final String DOMAIN = "my-domain";

    @Override
    public void initEnv() {}

    @Override
    public void defineGame() {
        List<GameConcept> concepts = new ArrayList<>();
        concepts.add(new PointConcept("passi"));
        concepts.add(new PointConcept("alberi"));
        concepts.add(new BadgeCollectionConcept("medaglie"));
        defineGameHelper(DOMAIN, GAME_ID, Arrays.asList(ACTION_ID), concepts);

        loadClasspathRules(GAME_ID, Arrays.asList("rules/" + GAME_ID + "/punti.drl",
                "rules/" + GAME_ID + "/badges.drl"));
    }

    @Override
    public void defineExecData(List<ExecData> execList) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("distance", 1);
        ExecData ex = new ExecData(GAME_ID, ACTION_ID, "child1", data);
        execList.add(ex);

        data = new HashMap<String, Object>();
        data.put("distance", 10000);
        ex = new ExecData(GAME_ID, ACTION_ID, "child1", data);
        execList.add(ex);

        // data = new HashMap<String, Object>();
        // data.put("distance", 50.90d);
        // ex = new ExecData(GAME_ID, ACTION_ID, "child1", data);
        // execList.add(ex);

        data = new HashMap<String, Object>();
        data.put("distance", 19949);
        ex = new ExecData(GAME_ID, ACTION_ID, "child1", data);
        execList.add(ex);

        data = new HashMap<String, Object>();
        data.put("distance", 9500);
        ex = new ExecData(GAME_ID, ACTION_ID, "child1", data);
        execList.add(ex);
    }

    @Override
    public void analyzeResult() {}

}
