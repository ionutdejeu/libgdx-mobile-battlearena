package com.libgdx.battlearena.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import io.socket.client.IO;
import io.socket.client.Socket;

public class BattleArena extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	private Socket socket;
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		connectSocket();
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	private void connectSocket(){
		try{
			socket = IO.socket("ws://localhost:3000");
			socket.connect();
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
}
