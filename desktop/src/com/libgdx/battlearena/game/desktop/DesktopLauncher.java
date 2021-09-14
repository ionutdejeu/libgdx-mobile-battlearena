package com.libgdx.battlearena.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.libgdx.battlearena.game.BattleArena;
import com.libgdx.battlearena.game.BattleArenaGame;
import com.libgdx.tests.CharacterTest;
import com.libgdx.tests.InverseKinematicsTest;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new BattleArenaGame(), config);
	}
}
