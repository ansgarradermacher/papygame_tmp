package org.eclipse.papyrus.gamification.view.login;

import org.eclipse.papyrus.gamification.data.Logger;
import org.eclipse.papyrus.gamification.view.common.swt.Browser;
import org.eclipse.papyrus.gamification.view.common.swt.JavascriptFunction;

public class JSLogBrowserJava extends JavascriptFunction {

	Browser browser;

	public JSLogBrowserJava(Browser browser) {
		super(browser, "logBrowserJava");
		this.browser = browser;
	}

	@Override
	public Object functionBody(Object[] args) {

		try {
			System.out.println("Calling JS LOG BROWSER");
			Logger.getInstance().logDebug(this.getClass(), "Browser Information", String.class.cast(args[0]));
		} catch (Exception ex) {
			ex.printStackTrace();
			Logger.getInstance().logError(this.getClass(), ex, "try to log JS Browser");
		}

		return "";
	}

}