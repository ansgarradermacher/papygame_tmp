package org.eclipse.papyrus.gamification.view.common;

public class ViewManagerNotInitializedException extends Exception{

	@Override
	public String getMessage() {
		return "Trying to perform a Game Operation before registering a user id.";
	}

}
