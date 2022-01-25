package org.eclipse.papyrus.gamification.data.repository.dashboard;

import java.util.concurrent.Callable;

import org.eclipse.papyrus.gamification.data.preferences.PreferencesManager;

import io.reactivex.Completable;
import io.reactivex.Single;

public class DashboardLocalDataSource {

	PreferencesManager preferencesManager;


	public DashboardLocalDataSource(PreferencesManager preferencesManager) {
		this.preferencesManager = preferencesManager;
	}

	public Completable setVideoShown(String playerId) {
		return Completable.fromCallable(new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				preferencesManager.setVideoShown(playerId);
				return null;
			}
		});

	}

	public Single<Boolean> hasVideoBeenShown(String playerId) {
		return Single.fromCallable(new Callable<Boolean>() {

			@Override
			public Boolean call() throws Exception {
				// TODO Auto-generated method stub
				return preferencesManager.hasVideoBeenDisplayed(playerId);
			}

		});
	}

}
