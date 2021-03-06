package com.libgdx.battlearena.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.libgdx.battlearena.GUI.Controller;


public class BattleArena2 extends ApplicationAdapter {



	public static final float PPM = 100;
	public static SpriteBatch batch;
	World world;
	float lerp = 0.1f;
	OrthographicCamera cam;
	Viewport viewport;
	Box2DDebugRenderer b2dr;
	Body player;
	Controller controller;

	@Override
	public void create () {


		cam = new OrthographicCamera();

		viewport = new FitViewport(800 / PPM, 480 / PPM, cam);
		batch = new SpriteBatch();
		world = new World(new Vector2(0, 0), true);
		b2dr = new Box2DDebugRenderer();
		createGround();
		createPlayer();
		controller = new Controller(batch);

	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		viewport.update(width, height);
		controller.resize(width, height);
	}
	public void handleInput(){
		if(controller.touchStarted()){
			Vector2 s = controller.speed().nor();
			player.setLinearVelocity(s);
		}

	}

	public void update(float dt){
		handleInput();
		world.step(dt, 6, 2);
		Vector3 position = cam.position;
		position.x += (player.getPosition().x - position.x) * lerp * dt;
		position.y += (player.getPosition().y - position.y) * lerp * dt;
		//cam.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

		cam.update();
	}

	@Override
	public void render () {
		update(Gdx.graphics.getDeltaTime());
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		b2dr.render(world, cam.combined);
		if(Gdx.app.getType() == Application.ApplicationType.Android)
			controller.draw();
	}

	public void createGround(){
		BodyDef bdef = new BodyDef();
		bdef.position.set(viewport.getWorldWidth() / 2, 0);
		bdef.type = BodyDef.BodyType.StaticBody;
		Body b2body = world.createBody(bdef);

		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(viewport.getWorldWidth() / 2, 20 / PPM);

		fdef.shape = shape;
		b2body.createFixture(fdef);
	}
	public void createPlayer(){
		BodyDef bdef = new BodyDef();
		bdef.position.set(viewport.getWorldWidth() / 2, 80 / PPM);
		bdef.type = BodyDef.BodyType.DynamicBody;
		player = world.createBody(bdef);

		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(40 / PPM, 40 / PPM);

		fdef.shape = shape;
		player.createFixture(fdef);
	}
}
