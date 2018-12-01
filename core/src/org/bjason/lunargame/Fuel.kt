package org.bjason.lunargame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

class Fuel(val startX: Float, val startY: Float) : Character() {

	override val width = (48 * LunarGame.scale).toInt()
	override val height = (48 * LunarGame.scale).toInt()
	override val sprite: Sprite
	override val texture: Texture
	override val screenOffsetToOffsetCamera = Gdx.graphics.height / 2
	var onGround = false
	override val rectangle: Rectangle
	var done = false
	val speed = 25f

	init {
		x = startX;
		y = startY


		texture = textureAsset
		sprite = Sprite(texture)
		sprite.setCenter(0f, 0f)
		sprite.setPosition(startX, startY)
		sprite.setSize(width.toFloat(), height.toFloat())
		sprite.x = startX
		sprite.y = startY
		rectangle = Rectangle(x, y, width.toFloat(), height.toFloat())
	}

	companion object {
		val textureAsset by lazy {
			Texture(Gdx.files.internal("data/fuel.png"))
		}
	}

	override fun logic() {
		if (!onGround) super.logic()
		if (aboveGround1 < y && aboveGround2 < y) {
			y = y - speed * Gdx.graphics.deltaTime
		} else {
			onGround = true
		}
	}

	override fun hit(c: Character) {
		if (c is Bullet && c.playerBullet) {
			val explosion = Explosion(x, y)
			LunarGame.characters.add(explosion)
			Sound.explode.play()
		}
		done = true
	}

}
