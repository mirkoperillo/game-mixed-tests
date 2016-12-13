package eu.trentorise.game.test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetupChEnv extends GameTest {

	@Override
	public void initEnv() {
		String gameid = "challengeGame";
		// player clint barton
		// ch1 ch7
		Map<String, Object> customData = new HashMap<String, Object>();

		// ch1 - in progress
		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.DAY_OF_MONTH, -1);

		customData.put("ch_1_startChTs", calendar.getTimeInMillis());

		calendar.add(Calendar.DAY_OF_MONTH, 2);

		customData.put("ch_1_endChTs", calendar.getTimeInMillis());
		customData.put("ch_1_target", 10);
		customData.put("ch_1_Km_traveled_during_challenge", 3);
		customData.put("ch_1_type", "ch1");
		customData.put("ch_1_mode", "train");
		customData.put("ch_1_bonus", 100);
		customData.put("ch_1_success", false);
		customData.put("ch_1_point_type", "green leaves");

		// ch7 - success
		calendar = new GregorianCalendar();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		customData.put("ch_2_startChTs", calendar.getTimeInMillis());

		calendar.add(Calendar.DAY_OF_MONTH, 2);
		customData.put("ch_2_endChTs", calendar.getTimeInMillis());

		customData.put("ch_2_endChTs", calendar.getTimeInMillis());
		customData.put("ch_2_target", "final_badge");
		customData.put("ch_2_type", "ch7");
		customData.put("ch_2_bonus", 50);
		customData.put("ch_2_success", true);
		customData.put("ch_2_point_type", "green leaves");

		// ch3 - failed
		calendar = new GregorianCalendar();
		calendar.add(Calendar.DAY_OF_MONTH, -2);
		customData.put("ch_4_startChTs", calendar.getTimeInMillis());

		calendar.add(Calendar.DAY_OF_MONTH, -1);

		customData.put("ch_4_endChTs", calendar.getTimeInMillis());
		customData.put("ch_4_target", 5);
		customData.put("ch_4_counter", 2);
		customData.put("ch_4_type", "ch3");
		customData.put("ch_4_bonus", 50);
		customData.put("ch_4_success", false);
		customData.put("ch_4_point_type", "green leaves");

		savePlayerState(
				gameid,
				"clint barton",
				Arrays.asList(new PointConceptBuilder().setName("green leaves")
						.setScore(0d).build()), customData);

		// player james rhodes
		// no challenge
		savePlayerState(
				gameid,
				"james rhodes",
				Arrays.asList(new PointConceptBuilder().setName("green leaves")
						.setScore(0d).build()));

		// player remy lebeau
		// ch9
		customData = new HashMap<String, Object>();

		// ch9 -success
		calendar = new GregorianCalendar();
		calendar.add(Calendar.DAY_OF_MONTH, -1);

		customData.put("ch_3_startChTs", calendar.getTimeInMillis());

		calendar.add(Calendar.DAY_OF_MONTH, 2);
		customData.put("ch_3_endChTs", calendar.getTimeInMillis());

		customData.put("ch_3_endChTs", calendar.getTimeInMillis());
		customData.put("ch_3_target", 10);
		customData.put("ch_3_type", "ch9");
		customData.put("ch_3_recommendations_sent_during_challenges", 11);
		customData.put("ch_3_target", 10);
		customData.put("ch_3_bonus", 50);
		customData.put("ch_3_success", true);
		customData.put("ch_3_point_type", "green leaves");

		savePlayerState(
				gameid,
				"remy lebeau",
				Arrays.asList(new PointConceptBuilder().setName("green leaves")
						.setScore(0d).build()), customData);

	}

	@Override
	public void defineGame() {
	}

	@Override
	public void defineExecData(List<ExecData> execList) {
	}

	@Override
	public void analyzeResult() {
	}

}
