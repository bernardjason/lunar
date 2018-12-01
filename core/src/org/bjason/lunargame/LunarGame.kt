package org.bjason.lunargame;

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.FPSLogger
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.InputProcessor

class LunarGame() : ApplicationAdapter() {


	override fun create() {
		LunarGame.characters.clear()
		LunarGame.bullets.clear()
		LunarGame.enemies.clear()
		LunarGame.fuel.clear()
		Gdx.input.setInputProcessor(Input)
		ScoreCard.reset()
		LunarGame.playerShip.x=0f
		LunarGame.playerShip.y=Gdx.graphics.height*0.75f
		BackGround.setup(LunarGame.playerShip.x)
	}

	override fun render() {
		clicks = clicks + Gdx.graphics.deltaTime

		Input.process()
		
		cam.position.x = playerShip.x

		cam.update()
		batch.setProjectionMatrix(cam.combined)

		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		for (c in characters.toList()) {
			c.logic()
		}

		batch.begin()
		BackGround.draw(batch,playerShip.x)

		TerrainHelper.getTerrains(playerShip.x).map {
			it.logic()
			it.draw(batch)
		}
		for (c in characters) {
			c.draw(batch)
		}
		batch.end()

		playerShip.logic()

		fixedBatch.begin()
		playerShip.draw(fixedBatch)
		ScoreCard.draw(fixedBatch)
		fixedBatch.end()
		fps.log()
		if (ScoreCard.hitTooManyTimes()) {
			MainGame.endGame("Final score\nenemeies hit ${ScoreCard.enemyHit}\nFuel collected ${ScoreCard.fuelCollected}")
		}
	}
	override fun pause() {
		//wont resume as not using assetmanager
		System.exit(0)
	}

	override fun dispose() {
		batch.dispose()
		fixedBatch.dispose()
	}
	
	
	companion object {
		var clicks = 0f
		val scale by lazy { val s=Gdx.graphics.width / 800f ; if ( s < 1 ) 1f else s }
		val playerShip by lazy { PlayerShip(0f, Gdx.graphics.height*0.75f) }
		val characters = mutableListOf<Character>()
		val bullets = mutableListOf<Character>()
		val enemies = mutableListOf<Character>()
		val fuel = mutableListOf<Character>()
		val fps = FPSLogger()
		val batch by lazy { SpriteBatch() }
		val fixedBatch by lazy { SpriteBatch() }

		val cam by lazy {

			val w = Gdx.graphics.getWidth();
			val h = Gdx.graphics.getHeight()
			val cam = OrthographicCamera(w.toFloat(), h.toFloat());
			cam.position.set(0f, 0f, 0f)
			cam.update()
			cam

		}

		fun addCharacter(b:Bullet) {
			characters.add(b)
			bullets.add(b)
		}
		fun addCharacter(e:EnemyShip) {
			characters.add(e)
			enemies.add(e)
		}
		fun addCharacter(f:Fuel) {
			characters.add(f)
			fuel.add(f)
		}
		fun addCharacter(f:PlayerShip.FuelTrail) {
			characters.add(f)
		}
		fun removeCharacter(c:Character) {
			when(c) {
				is Bullet -> bullets.remove(c)
				is EnemyShip -> enemies.remove(c)
				is Fuel -> fuel.remove(c)
			}
			characters.remove(c)
		}

	}

	
}
