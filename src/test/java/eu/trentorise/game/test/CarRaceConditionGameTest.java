package eu.trentorise.game.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;

import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.TeamState;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.services.PlayerService;

public class CarRaceConditionGameTest extends GameTest {

    @Autowired
    PlayerService playerSrv;

    private static final String GAME = "climb";
    private static final String DOMAIN = "my-domain";

    @Override
    public void initEnv() {
        TeamState team = new TeamState(GAME, "scuola");
        team.setName("school");
        team.setMembers(Arrays.asList("classroom"));
        playerSrv.saveTeam(team);

        team = new TeamState(GAME, "classroom");
        team.setName("classroom");
        team.setMembers(Arrays.asList("alice", "bob", "tracy", "luke"));
        playerSrv.saveTeam(team);

    }

    @Override
    public void defineGame() {
        List<GameConcept> concepts = new ArrayList<GameConcept>();
        PointConcept pc = new PointConcept("zeroImpact_wAdult_distance");
        LocalDate start = new LocalDate(2017, 2, 16);
        pc.addPeriod("daily", start.toDate(), 24 * 60 * 60 * 1000);
        pc.addPeriod("weekly", start.toDate(), 7 * 24 * 60 * 60 * 1000);
        concepts.add(pc);

        pc = new PointConcept("bonus_distance");
        pc.addPeriod("daily", start.toDate(), 24 * 60 * 60 * 1000);
        pc.addPeriod("weekly", start.toDate(), 7 * 24 * 60 * 60 * 1000);
        concepts.add(pc);

        pc = new PointConcept("zeroImpact_solo_trips");
        pc.addPeriod("daily", start.toDate(), 24 * 60 * 60 * 1000);
        pc.addPeriod("weekly", start.toDate(), 7 * 24 * 60 * 60 * 1000);
        concepts.add(pc);

        pc = new PointConcept("pedibus_trips");
        pc.addPeriod("daily", start.toDate(), 24 * 60 * 60 * 1000);
        pc.addPeriod("weekly", start.toDate(), 7 * 24 * 60 * 60 * 1000);
        concepts.add(pc);

        pc = new PointConcept("total_trips");
        pc.addPeriod("daily", start.toDate(), 24 * 60 * 60 * 1000);
        pc.addPeriod("weekly", start.toDate(), 7 * 24 * 60 * 60 * 1000);
        concepts.add(pc);

        pc = new PointConcept("car_distance");
        pc.addPeriod("daily", start.toDate(), 24 * 60 * 60 * 1000);
        pc.addPeriod("weekly", start.toDate(), 7 * 24 * 60 * 60 * 1000);
        concepts.add(pc);

        pc = new PointConcept("group_trips");
        pc.addPeriod("daily", start.toDate(), 24 * 60 * 60 * 1000);
        pc.addPeriod("weekly", start.toDate(), 7 * 24 * 60 * 60 * 1000);
        concepts.add(pc);

        pc = new PointConcept("zeroImpact_wAdult_trips");
        pc.addPeriod("daily", start.toDate(), 24 * 60 * 60 * 1000);
        pc.addPeriod("weekly", start.toDate(), 7 * 24 * 60 * 60 * 1000);
        concepts.add(pc);

        pc = new PointConcept("pandr_distance");
        pc.addPeriod("daily", start.toDate(), 24 * 60 * 60 * 1000);
        pc.addPeriod("weekly", start.toDate(), 7 * 24 * 60 * 60 * 1000);
        concepts.add(pc);

        pc = new PointConcept("no_car_classes");
        pc.addPeriod("daily", start.toDate(), 24 * 60 * 60 * 1000);
        pc.addPeriod("weekly", start.toDate(), 7 * 24 * 60 * 60 * 1000);
        concepts.add(pc);

        pc = new PointConcept("participation_count");
        pc.addPeriod("daily", start.toDate(), 24 * 60 * 60 * 1000);
        pc.addPeriod("weekly", start.toDate(), 7 * 24 * 60 * 60 * 1000);
        concepts.add(pc);

        pc = new PointConcept("bus_trips");
        pc.addPeriod("daily", start.toDate(), 24 * 60 * 60 * 1000);
        pc.addPeriod("weekly", start.toDate(), 7 * 24 * 60 * 60 * 1000);
        concepts.add(pc);

        pc = new PointConcept("bus_distance");
        pc.addPeriod("daily", start.toDate(), 24 * 60 * 60 * 1000);
        pc.addPeriod("weekly", start.toDate(), 7 * 24 * 60 * 60 * 1000);
        concepts.add(pc);

        pc = new PointConcept("pandr_trips");
        pc.addPeriod("daily", start.toDate(), 24 * 60 * 60 * 1000);
        pc.addPeriod("weekly", start.toDate(), 7 * 24 * 60 * 60 * 1000);
        concepts.add(pc);

        pc = new PointConcept("zeroImpact_solo_distance");
        pc.addPeriod("daily", start.toDate(), 24 * 60 * 60 * 1000);
        pc.addPeriod("weekly", start.toDate(), 7 * 24 * 60 * 60 * 1000);
        concepts.add(pc);

        pc = new PointConcept("car_trips");
        pc.addPeriod("daily", start.toDate(), 24 * 60 * 60 * 1000);
        pc.addPeriod("weekly", start.toDate(), 7 * 24 * 60 * 60 * 1000);
        concepts.add(pc);

        pc = new PointConcept("absence_count");
        pc.addPeriod("daily", start.toDate(), 24 * 60 * 60 * 1000);
        pc.addPeriod("weekly", start.toDate(), 7 * 24 * 60 * 60 * 1000);
        concepts.add(pc);

        pc = new PointConcept("pedibus_distance");
        pc.addPeriod("daily", start.toDate(), 24 * 60 * 60 * 1000);
        pc.addPeriod("weekly", start.toDate(), 7 * 24 * 60 * 60 * 1000);
        concepts.add(pc);

        pc = new PointConcept("total_distance");
        pc.addPeriod("daily", start.toDate(), 24 * 60 * 60 * 1000);
        pc.addPeriod("weekly", start.toDate(), 7 * 24 * 60 * 60 * 1000);
        concepts.add(pc);

        concepts.add(new BadgeCollectionConceptBuilder().setName("LegsToKangole").build());

        defineGameHelper(DOMAIN, GAME, Arrays.asList("NoCarCounterTest", "PlayerCalendarTrip",
                "ClassWalkTrip", "PedibusKidTrip", "PedibusVolunteerTrip"), concepts);

        loadClasspathRules(GAME,
                Arrays.asList("rules/climb/calendartrips.drl", "rules/climb/classday.drl",
                        "rules/climb/classtrips.drl", "rules/climb/constants",
                        "rules/climb/legsbadges.drl", "rules/climb/pedibus.drl",
                        "rules/climb/weeklytasks.drl"));

    }

    @Override
    public void defineExecData(List<ExecData> execList) {
        Map<String, Object> d = new HashMap<String, Object>();
        d.put("mode", "bus");
        d.put("meteo", "cloudy");
        d.put("school-date", "today");
        ExecData data = new ExecData(GAME, "PlayerCalendarTrip", "alice", d);
        execList.add(data);

        d = new HashMap<String, Object>();
        d.put("mode", "pandr");
        d.put("meteo", "cloudy");
        d.put("school-date", "today");
        data = new ExecData(GAME, "PlayerCalendarTrip", "bob", d);
        execList.add(data);

        d = new HashMap<String, Object>();
        d.put("mode", "bus");
        d.put("meteo", "cloudy");
        d.put("school-date", "today");
        data = new ExecData(GAME, "PlayerCalendarTrip", "tracy", d);
        execList.add(data);

        d = new HashMap<String, Object>();
        d.put("mode", "car");
        d.put("meteo", "cloudy");
        d.put("school-date", "today");
        data = new ExecData(GAME, "PlayerCalendarTrip", "luke", d);
        execList.add(data);

        // d = new HashMap<String, Object>();
        // data = new ExecData(GAME, "NoCarCounterTest", "classroom", d);
        // execList.add(data);

    }

    @Override
    public void analyzeResult() {}

}
