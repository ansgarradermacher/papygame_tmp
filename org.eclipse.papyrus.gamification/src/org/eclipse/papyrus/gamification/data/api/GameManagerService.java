package org.eclipse.papyrus.gamification.data.api;

import java.util.List;

import org.eclipse.papyrus.gamification.data.api.query.ActionRequestContent;
import org.eclipse.papyrus.gamification.data.api.response.CustomDataJson;
import org.eclipse.papyrus.gamification.data.api.response.PlayerStatusJson;
import org.eclipse.papyrus.gamification.data.entity.Series;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface GameManagerService {

	@GET
	Single<List<Series>> getAvailableSeries(@Url String fullUrl);

	@GET("/gamification-v3/data/game/{gameId}/player/{playerId}")
	Single<PlayerStatusJson> getPlayerStatus(@Path("gameId") String gameId, @Path("playerId") String playerId);

	// POST /exec/game/{gameId}/action/{actionId}
	@POST("/gamification-v3/exec/game/{gameId}/action/{actionId}")
	Completable submitGameResult(@Path("gameId") String gameId,
			@Path("actionId") String actionId,
			@Body ActionRequestContent content);


	/// data/game/{gameId}/player/{playerId}/custom
	@PUT("/gamification-v3/data/game/{gameId}/player/{playerId}/custom")
	Completable setPlayerCustomData(@Path("gameId") String gameId, @Path("playerId") String playerId, @Body CustomDataJson content);


}
