package org.eclipse.papyrus.gamification.view.common.swt;

import org.eclipse.papyrus.gamification.games.framework.communication.OnSecondsReadyItf;


public class JSOnSecondsReady extends JavascriptFunction {
	OnSecondsReadyItf onSecondsReadyItf;

	public JSOnSecondsReady(Browser browser, OnSecondsReadyItf onSecondsReadyItf) {
		super(browser, "onSecondsReady");
		this.onSecondsReadyItf = onSecondsReadyItf;
	}

	@Override
	public Object functionBody(Object[] args) {
		onSecondsReadyItf.onSecondsReady(Double.class.cast(args[0]));
		return "";
	}

}
