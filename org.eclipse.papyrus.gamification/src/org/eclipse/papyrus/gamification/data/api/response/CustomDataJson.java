package org.eclipse.papyrus.gamification.data.api.response;

import java.util.ArrayList;
import java.util.List;

public class CustomDataJson {
	String level;
	String password = "";
	boolean questionnaireFilled;
	int invitations;
	List<LevelCompletedJson> levelsCompleted;
	boolean LastGameSuccess;

	public CustomDataJson(String password) {
		super();
		this.password = password;
		this.questionnaireFilled = false;
	}

	public String getLevel() {
		return level;
	}

	public String getPassword() {
		return password;
	}

	public boolean isQuestionnaireFilled() {
		return questionnaireFilled;
	}

	public List<LevelCompletedJson> getLevelsCompleted() {
		if (levelsCompleted == null) {
			levelsCompleted = new ArrayList<>();
		}
		return levelsCompleted;
	}


	public int getInvitations() {
		return invitations;
	}

	public void setInvitations(int invitations) {
		this.invitations = invitations;
	}

	@Override
	public String toString() {
		return "CustomDataJson [level=" + level + ", password=" + password + ", questionnaireFilled=" + questionnaireFilled + ", levelsCompleted=" + levelsCompleted + "]";
	}

	public boolean isLastGameSuccess() {
		return LastGameSuccess;
	}


}
