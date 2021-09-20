package com.libgdx.battlearena.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.libgdx.battlearena.game.BattleArena;
import com.libgdx.battlearena.game.BattleArenaGame;
import com.libgdx.tests.Basic3DTest;
import com.libgdx.tests.CharacterTest;
import com.libgdx.tests.InverseKinematicsTest;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width=1920;
		config.height=1080;
		new LwjglApplication(new Basic3DTest(), config);
	}
}
