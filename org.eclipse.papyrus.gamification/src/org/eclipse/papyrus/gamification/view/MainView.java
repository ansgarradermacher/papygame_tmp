package org.eclipse.papyrus.gamification.view;

import org.eclipse.papyrus.gamification.view.common.OsCheck;
import org.eclipse.papyrus.gamification.view.common.swt.Browser;
import org.eclipse.papyrus.gamification.view.login.LoginView;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

public class MainView extends ViewPart {

	Label label;
	Browser browser;
	private ViewManager viewManager;

	@Override
	public void createPartControl(Composite parent) {

		/*
		 * WebView browser = new WebView();
		 * WebEngine engine = browser.getEngine();
		 * engine.setJavaScriptEnabled(true);
		 * Fx2SwtAdapter.getInstance().addToSwt(parent, browser);
		 */

		// FXCanvas fxCanvas = new FXCanvas(parent, SWT.SCROLL_PAGE);
		// parent.setLayout(new FillLayout());
		// Scene scene = new Scene(browser);
		// fxCanvas.setScene(scene);

		// fxCanvas.setBounds(0, 0, 1000, 800);

		// TODO : I've been forced to to-do this! Help me!
		/*
		 * PlayerModel.getInstance();
		 * PlayerModel.deleteInstance();
		 * PlayerModel.getInstance();
		 */
		System.out.println("Working with Java : " + System.getProperty("java.runtime.version"));

		System.out.println("OS DETECTED IS : " + OsCheck.getOperatingSystemType());

		browser = new Browser(parent);
		browser.setBounds(0, 0, 1000, 800);

		this.viewManager = new ViewManager(browser);

		// Init with login view
		viewManager.displayView(new LoginView());

	}

	@Override
	public void setFocus() {
		browser.setFocus();
	}


}
