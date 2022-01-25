package org.eclipse.papyrus.gamification.view.common.swt;

public class JSPrint extends JavascriptFunction {

	public JSPrint(Browser browser) {
		super(browser, "print");
	}

	@Override
	public Object functionBody(Object[] args) {
		String out = String.class.cast(args[0]);
		System.out.println(out);
		return "";
	}

}
