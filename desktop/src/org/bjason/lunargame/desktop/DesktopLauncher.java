package org.bjason.lunargame.desktop;

import org.bjason.lunargame.MainGame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height=900;
		config.width=1680;
		MainGame.INSTANCE.setDesktop(true);
		new LwjglApplication(MainGame.INSTANCE, config);
	}
}
