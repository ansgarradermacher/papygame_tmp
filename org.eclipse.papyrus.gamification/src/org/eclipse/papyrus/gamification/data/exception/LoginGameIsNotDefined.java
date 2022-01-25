package org.eclipse.papyrus.gamification.data.exception;

public class LoginGameIsNotDefined extends Exception{

	@Override
	public String getMessage() {
		return "Trying to perform a Game Operation before registering a user id.";
	}

}
