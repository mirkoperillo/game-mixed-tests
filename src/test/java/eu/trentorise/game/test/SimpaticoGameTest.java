package eu.trentorise.game.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;


public class SimpaticoGameTest extends GameTest {

    private static final String GAME = "simpatico";

    @Override
    public void initEnv() {

    }

    @Override
    public void defineGame() {
        defineGameHelper(GAME,
                Arrays.asList("login", "registration", "make-question", "answer-question"),
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

    }

    @Override
    public void analyzeResult() {
        assertionPoint(GAME, 85.0, "player1", "karma points");

    }

}
