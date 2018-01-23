package eu.trentorise.game.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;


public class SimpaticoGameTest extends GameTest {

    private static final String GAME = "simpatico";

    @Override
    public void initEnv() {
        Map<String, Object> customData = new HashMap<>();
        customData.put("loginCount", 9);
        customData.put("lastLogin", "2018-01-22");
        savePlayerState(GAME, "player1",
                Arrays.asList(
                        new PointConceptBuilder().setName("karma points").setScore(0d).build()),
                customData);

    }

    @Override
    public void defineGame() {
        defineGameHelper(GAME,
                Arrays.asList("login", "registration", "make-question", "answer-question", "like",
                        "update-description"),
                Arrays.asList(
                        new PointConceptBuilder().setName("karma points").setScore(0d).build(),
                        new BadgeCollectionConceptBuilder().setName("medals").build()));

        try {
            loadClasspathRules(GAME, "rules/" + GAME);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }

    }

    @Override
    public void defineExecData(List<ExecData> execList) {
        ExecData action = new ExecData(GAME, "login", "player1", null);
        execList.add(action);

        action = new ExecData(GAME, "registration", "player1", null);
        execList.add(action);

        action = new ExecData(GAME, "make-question", "player1", null);
        execList.add(action);

        action = new ExecData(GAME, "answer-question", "player1", null);
        execList.add(action);

        action = new ExecData(GAME, "like", "player1", null);
        execList.add(action);

        action = new ExecData(GAME, "update-description", "player1", null);
        execList.add(action);

    }

    @Override
    public void analyzeResult() {
        assertionPoint(GAME, 165.0, "player1", "karma points");

    }

}
