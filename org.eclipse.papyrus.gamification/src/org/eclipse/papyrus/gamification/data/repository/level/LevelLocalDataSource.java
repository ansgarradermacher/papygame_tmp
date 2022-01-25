package org.eclipse.papyrus.gamification.data.repository.level;

import org.eclipse.papyrus.gamification.data.preferences.PreferencesManager;
import org.eclipse.papyrus.gamification.modelutils.ResponseModel;

public class LevelLocalDataSource {

	PreferencesManager preferencesManager;



	public LevelLocalDataSource(PreferencesManager preferencesManager) {
		super();
		this.preferencesManager = preferencesManager;
	}

	public ResponseModel loadResponseModel(String modelPath, boolean isReponseModelInternal) {
		return new ResponseModel(modelPath, isReponseModelInternal);
	}

}
