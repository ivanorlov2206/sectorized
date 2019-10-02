package net.programmingeasy.kickout.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.programmingeasy.kickout.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 540;
		config.height = 960;
		new LwjglApplication(new MyGdxGame(), config);
	}
}
