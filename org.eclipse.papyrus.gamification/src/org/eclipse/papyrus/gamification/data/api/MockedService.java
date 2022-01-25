package org.eclipse.papyrus.gamification.data.api;

import java.util.List;

import org.eclipse.papyrus.gamification.data.api.query.ActionRequestContent;
import org.eclipse.papyrus.gamification.data.api.response.CustomDataJson;
import org.eclipse.papyrus.gamification.data.api.response.PlayerStatusJson;
import org.eclipse.papyrus.gamification.data.di.RepositoryFactory;
import org.eclipse.papyrus.gamification.data.entity.Series;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.reactivex.Completable;
import io.reactivex.Single;

public class MockedService implements GameManagerService {

	Gson gson = new Gson();
	int requestCounter = 0;

	@Override
	public Single<PlayerStatusJson> getPlayerStatus(String gameId, String playerId) {
		String JSON = "{  \"playerId\": \"Tony\",\n" +
				"  \"gameId\": \"5f087a49857aba00013178cd\",\n" +
				"  \"state\": {\n" +
				"    \"PointConcept\": [\n" +
				"      {\n" +
				"        \"id\": \"1\",\n" +
				"        \"name\": \"moves\",\n" +
				"        \"score\": 5.0,\n" +
				"        \"periods\": {}\n" +
				"      },\n" +
				"      {\n" +
				"        \"id\": \"1\",\n" +
				"        \"name\": \"points\",\n" +
				"        \"score\": 20.0,\n" +
				"        \"periods\": {}\n" +
				"      },\n" +
				"      {\n" +
				"        \"id\": \"1\",\n" +
				"        \"name\": \"errors\",\n" +
				"        \"score\": 0.0,\n" +
				"        \"periods\": {}\n" +
				"      },\n" +
				"      {\n" +
				"        \"id\": \"1\",\n" +
				"        \"name\": \"gold coins\",\n" +
				"        \"score\": 2.0,\n" +
				"        \"periods\": {}\n" +
				"      }\n" +
				"    ]\n" +
				"  },\n" +
				"  \"levels\": [],\n" +
				"  \"inventory\": {\n" +
				"    \"challengeChoices\": [],\n" +
				"    \"challengeActivationActions\": 0\n" +
				"  },\n" +
				"  \"customData\": {\n" +
				"    \"password\": \"MYPASSWDENCRYPTED\",\n" +
				"    \"questionnaireFilled\": false,\n" +
				"    \"lastLevelDone\": \"Level 2\",\n" +
				"    \"levelsCompleted\": [\n" +
				"      {\n" +
				"        \"level\": 1,\n" +
				"        \"levelLabel\": \"Level 1\",\n" +
				"        \"earnedGoldCoins\": 3,\n" +
				"        \"earnedXP\": 234,\n" +
				"        \"timeToComplete\": 345,\n" +
				"        \"moveNumber\": 12,\n" +
				"        \"errors\": 0,\n" +
				"        \"logs\": \"……..\"\n" +
				"      },\n" +
				"      {\n" +
				"        \"level\": 2,\n" +
				"        \"levelLabel\": \"Level 1bis\",\n" +
				"        \"earnedGoldCoins\": 3,\n" +
				"        \"earnedXP\": 234,\n" +
				"        \"timeToComplete\": 345,\n" +
				"        \"moveNumber\": 12,\n" +
				"        \"errors\": 0,\n" +
				"        \"logs\": \"……..\"\n" +
				"      }\n" +
				"    ]\n" +
				"  }"
				+ "}";
		String JSON2 = "{  \"playerId\": \"Tony\",\n" +
				"  \"gameId\": \"5f087a49857aba00013178cd\",\n" +
				"  \"state\": {\n" +
				"    \"PointConcept\": [\n" +
				"      {\n" +
				"        \"id\": \"1\",\n" +
				"        \"name\": \"moves\",\n" +
				"        \"score\": 5.0,\n" +
				"        \"periods\": {}\n" +
				"      },\n" +
				"      {\n" +
				"        \"id\": \"1\",\n" +
				"        \"name\": \"points\",\n" +
				"        \"score\": " + ((int) (20d + (1.0 * requestCounter) * Math.random() * 50d)) + ",\n" +
				"        \"periods\": {}\n" +
				"      },\n" +
				"      {\n" +
				"        \"id\": \"1\",\n" +
				"        \"name\": \"errors\",\n" +
				"        \"score\": 0.0,\n" +
				"        \"periods\": {}\n" +
				"      },\n" +
				"      {\n" +
				"        \"id\": \"1\",\n" +
				"        \"name\": \"gold coins\",\n" +
				"        \"score\": " + ((int) (2d + (1.0 * requestCounter) * Math.random() * 3d)) + ",\n" +
				"        \"periods\": {}\n" +
				"      }\n" +
				"    ]\n" +
				"  },\n" +
				"  \"levels\": [],\n" +
				"  \"inventory\": {\n" +
				"    \"challengeChoices\": [],\n" +
				"    \"challengeActivationActions\": 0\n" +
				"  },\n" +
				"  \"customData\": {\n" +
				"    \"password\": \"MYPASSWDENCRYPTED\",\n" +
				"    \"questionnaireFilled\": false,\n" +
				"    \"lastLevelDone\": \"Level 2\",\n" +
				"    \"levelsCompleted\": [\n" +
				"    ]\n" +
				"  }"
				+ "}";
		System.out.println("------ Returning MOCKED Player Status response");
		PlayerStatusJson psj = gson.fromJson(JSON2, PlayerStatusJson.class);
		requestCounter++;
		return Single.just(psj);
	}

	@Override
	public Completable submitGameResult(String gameId, String actionId, ActionRequestContent content) {
		System.out.println("Calling submit game query with attributes : \n gameId : " +
				gameId + " \n actionId : " + actionId + "\n data : " + content.toString());
		return RepositoryFactory.getRetrofit().create(GameManagerService.class).submitGameResult(gameId, actionId, content);
		// return Completable.complete();
	}

	@Override
	public Single<List<Series>> getAvailableSeries(String url) {
		String input = "[\n" +
				"  {\n" +
				"    \"levels\": [\n" +
				"      {\n" +
				"        \"label\": \"Level 6\",\n" +
				"        \"modelPath\": \"/responses/Gamification/ConceptualModelingPapyrusGameSeries\",\n" +
				"        \"diagramName\": \"ConceptualModelingPapyrusGame-Level#06.\",\n" +
				"        \"gameClass\": \"org.eclipse.papyrus.gamification.games.oyo.Oyo\",\n" +
				"        \"statement\": \"Exercise: Geometric figures share common properties. In fact, some are very general and some are more specific. Use the inheritance link to sort the following figures from the most general to the most specific ones.\",\n"
				+
				"        \"videoToShowUrl\": \"\"\n" +
				"      }," +
				"      {\n" +
				"        \"label\": \"Level 2\",\n" +
				"        \"modelPath\": \"/responses/Gamification/ConceptualModelingPapyrusGameSeries\",\n" +
				"        \"diagramName\": \"ConceptualModelingPapyrusGame-Level#02.\",\n" +
				"        \"gameClass\": \"org.eclipse.papyrus.gamification.games.hangman.Hangman\",\n" +
				"        \"statement\": \"Exercise: Papyrus imports in each UML model a PrimitiveTypes package that contains the String, Integer or Boolean types. Use these types to define attributes in the classes of the previous level.\\nA book is characterized by a title (text), an edition year (integer) and a format (text). An author is characterized by a last name (text) and a first name (text). A production company has a name (text) and a country of domicile (text). Please use the Java naming convention, but for attributes this time (word1Word2Word3).\",\n"
				+
				"        \"videoToShowUrl\": \"\"\n" +
				"      },\n" +
				"      {\n" +
				"        \"label\": \"Level 3\",\n" +
				"        \"modelPath\": \"/responses/Gamification/ConceptualModelingPapyrusGameSeries\",\n" +
				"        \"diagramName\": \"ConceptualModelingPapyrusGame-Level#02.\",\n" +
				"        \"gameClass\": \"org.eclipse.papyrus.gamification.games.oyo.Oyo\",\n" +
				"        \"statement\": \"Exercise: Papyrus imports in each UML model a PrimitiveTypes package that contains the String, Integer or Boolean types. Use these types to define attributes in the classes of the previous level.\\nA book is characterized by a title (text), an edition year (integer) and a format (text). An author is characterized by a last name (text) and a first name (text). A production company has a name (text) and a country of domicile (text). Please use the Java naming convention, but for attributes this time (word1Word2Word3).\",\n"
				+
				"        \"videoToShowUrl\": \"\"\n" +
				"      },\n" +
				"      {\n" +
				"        \"label\": \"Level 4\",\n" +
				"        \"modelPath\": \"/responses/Gamification/ConceptualModelingPapyrusGameSeries\",\n" +
				"        \"diagramName\": \"ConceptualModelingPapyrusGame-Level#04.\",\n" +
				"        \"gameClass\": \"org.eclipse.papyrus.gamification.games.oyo.Oyo\",\n" +
				"        \"statement\": \"Exercise: The previous classes do not have a very proper name, as they mix the concept (e.g. Person) with the role they play (e.g. Author). Same for a company and its role of being the publisher of a certain book.  \\nThe classes have been renamed the classes. Please create the associations mentioned here. Multiplicity and names are not important here except the association name (convention Word1Word2...).\",\n"
				+
				"        \"videoToShowUrl\": \"\"\n" +
				"      },\n" +
				"      {\n" +
				"        \"label\": \"Level 5\",\n" +
				"        \"modelPath\": \"/responses/Gamification/ConceptualModelingPapyrusGameSeries\",\n" +
				"        \"diagramName\": \"ConceptualModelingPapyrusGame-Level#05.\",\n" +
				"        \"gameClass\": \"org.eclipse.papyrus.gamification.games.oyo.Oyo\",\n" +
				"        \"statement\": \"Exercise: The previous classes do not have a very proper name, as they mix the concept (e.g. Person) with the role they play (e.g. Author). Same for a company and its role of being the publisher of a certain book.  \\nThe classes have been renamed the classes. Please create the associations mentioned here. Multiplicity and names are not important here except the association name (convention Word1Word2...).\",\n"
				+
				"        \"videoToShowUrl\": \"\"\n" +
				"      },\n" +
				"      {\n" +
				"        \"label\": \"Level 6\",\n" +
				"        \"modelPath\": \"/responses/Gamification/ConceptualModelingPapyrusGameSeries\",\n" +
				"        \"diagramName\": \"ConceptualModelingPapyrusGame-Level#06.\",\n" +
				"        \"gameClass\": \"org.eclipse.papyrus.gamification.games.oyo.Oyo\",\n" +
				"        \"statement\": \"Exercise: Geometric figures share common properties. In fact, some are very general and some are more specific. Use the inheritance link to sort the following figures from the most general to the most specific ones.\",\n"
				+
				"        \"videoToShowUrl\": \"\"\n" +
				"      },\n" +
				"      {\n" +
				"        \"label\": \"Level 7\",\n" +
				"        \"modelPath\": \"/responses/Gamification/ConceptualModelingPapyrusGameSeries\",\n" +
				"        \"diagramName\": \"ConceptualModelingPapyrusGame-Level#07.\",\n" +
				"        \"gameClass\": \"org.eclipse.papyrus.gamification.games.hangman.Hangman\",\n" +
				"        \"statement\": \"Nothing to say\",\n" +
				"        \"videoToShowUrl\": \"\"\n" +
				"      },\n" +
				"      {\n" +
				"        \"label\": \"Level 8\",\n" +
				"        \"modelPath\": \"/responses/Gamification/ConceptualModelingPapyrusGameSeries\",\n" +
				"        \"diagramName\": \"ConceptualModelingPapyrusGame-Level#08.\",\n" +
				"        \"gameClass\": \"org.eclipse.papyrus.gamification.games.oyo.Oyo\",\n" +
				"        \"statement\": \"Nothing to say\",\n" +
				"        \"videoToShowUrl\": \"\"\n" +
				"      }\n" +
				"    ],\n" +
				"    \"name\": \"Master of Software Engineering\",\n" +
				"    \"seriesGameId\": \"5f885af1857aba00013f54e8\"\n" +
				"  }\n" +
				"]";
		System.out.println("------ Returning MOCKED Available Series response");
		return Single.just(gson.fromJson(input, new TypeToken<List<Series>>() {
		}.getType()));
	}

	@Override
	public Completable setPlayerCustomData(String gameId, String playerId, CustomDataJson content) {
		// TODO Auto-generated method stub
		return null;
	}
}
