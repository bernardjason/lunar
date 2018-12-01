package org.bjason.lunargame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Rectangle

abstract class Character {
	abstract val width: Int
	abstract val height: Int
	abstract val sprite: Sprite
	abstract val texture: Texture
	abstract val screenOffsetToOffsetCamera: Int
	abstract val rectangle: Rectangle
	var aboveGround1: Float = 0f
	var aboveGround2: Float = 0f
	var x: Float = 0f
	var y: Float = 0f
	val GRAVITY = true
	val GRAVITY_VALUE=20000f/Gdx.graphics.height

	fun gravity(): Boolean {
		aboveGround1 = TerrainHelper.getHeightAtPosition(x).toFloat() //- height 
		aboveGround2 = TerrainHelper.getHeightAtPosition(x + width).toFloat()// - height
		if (GRAVITY) {
			if (aboveGround1 < y && aboveGround2 < y) {
				y = y - GRAVITY_VALUE*Gdx.graphics.deltaTime
			} else if (aboveGround1 >= y) {
				y = y + 0.25f
				return true
			}
		}
		return false
	}

	open fun logic() {
		gravity();
	}

	open fun hit(c: Character) {

	}

	open fun draw(batch: Batch?) {
		sprite.y = y - screenOffsetToOffsetCamera
		sprite.x = x
		rectangle.setPosition(x, y)
		sprite.draw(batch)
	}

	fun dispose() {
		texture.dispose();
	}
}