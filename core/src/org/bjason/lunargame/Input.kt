package org.bjason.lunargame

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.FPSLogger
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.InputProcessor

object Input : InputAdapter() {

	var left = false
	var right = false
	var fire = false
	var thrust = false

	fun process() {
		val sensitive = 1.5f
		val accelY = (Gdx.input.getAccelerometerY() / sensitive).toInt();
		if (accelY != 0) {
			LunarGame.playerShip.rotate(accelY.toFloat());
		}

		if (left) LunarGame.playerShip.rotate(-1f);
		if (right) LunarGame.playerShip.rotate(1f);
		if (fire) LunarGame.playerShip.fire()
		val speed = if (thrust) {
			0.25f
		} else {
			-0.05f
		}
		LunarGame.playerShip.addThrust(speed)

	}

	override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
		if ( screenX < Gdx.graphics.width*0.25f ) thrust=true
		if ( screenX > Gdx.graphics.width*0.25f ) fire=true
		return false;
	}
	override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
		thrust=false
		fire=false
		return true;
	}

	fun action(keycode:Int,set:Boolean):Boolean {
		when (keycode) {
			Input.Keys.LEFT -> left = set;
			Input.Keys.RIGHT -> right = set
			Input.Keys.SPACE -> fire = set
			Input.Keys.SHIFT_LEFT -> thrust = set;
			else -> return false;
		}
		return true;
	}
	override fun keyDown(keycode: Int): Boolean {
		return action(keycode,true);
	}

	override fun keyUp(keycode: Int): Boolean {
		return action(keycode,false);
	}


	fun handleInput() {
		val speed = if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
			0.25f
		} else {
			-0.05f
		}
		LunarGame.playerShip.addThrust(speed)


		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			LunarGame.playerShip.rotate(-1f);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			LunarGame.playerShip.rotate(1f);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			LunarGame.playerShip.fire()
		}


	}

}