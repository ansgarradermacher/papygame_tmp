package org.eclipse.papyrus.gamification.view.login;

import org.eclipse.papyrus.gamification.view.common.swt.Browser;
import org.eclipse.papyrus.gamification.view.common.swt.JavascriptFunction;

public class JSSubmitLogin extends JavascriptFunction {

	Browser browser;
	LoginSubmitInterface loginSubmitInterface;

	public JSSubmitLogin(Browser browser, LoginSubmitInterface loginItf) {
		super(browser, "submitLogin");
		this.browser = browser;
		this.loginSubmitInterface = loginItf;
	}


	@Override
	public Object functionBody(Object[] args) {

		try {
			// new TestClass().run();
			String login = String.class.cast(args[0]);
			String password = String.class.cast(args[1]);
			String confirmPassword = String.class.cast(args[2]);
			String sponsor = String.class.cast(args[3]);
			if (!login.trim().isEmpty() && !password.trim().isEmpty()) {
				if ((confirmPassword == null) || (confirmPassword.trim().isEmpty())) {
					// Try login
					loginSubmitInterface.onLoginSubmitted(login, password);
				} else {
					if (password.equals(confirmPassword)) {
						// Try create account
						loginSubmitInterface.onCreateAccountSubmitted(login, password, confirmPassword, sponsor);
					} else {
						// Show error matching passwords
						browser.evaluate("showErrorMatchingPasswords()");
					}
				}
			} else {
				// Error field missing
				browser.evaluate("showErrorCredentials()");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}


		return "";
	}

	interface LoginSubmitInterface {
		void onLoginSubmitted(String login, String password);

		void onCreateAccountSubmitted(String login, String password, String confirmPassword, String sponsor);
	}
}