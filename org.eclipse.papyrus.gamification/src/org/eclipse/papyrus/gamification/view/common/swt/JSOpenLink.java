package org.eclipse.papyrus.gamification.view.common.swt;

import org.eclipse.papyrus.gamification.games.framework.communication.OnOpenLinkItf;


public class JSOpenLink extends JavascriptFunction {
	OnOpenLinkItf openLinkItf;

	public JSOpenLink(Browser browser, OnOpenLinkItf openLinkItf) {
		super(browser, "openLink");
		this.openLinkItf = openLinkItf;
	}

	@Override
	public Object functionBody(Object[] args) {
		openLinkItf.onOpenLink(args[0].toString());
		return "";
	}

}
