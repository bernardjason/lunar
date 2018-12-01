package org.bjason.lunargame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

class Bullet(val playerBullet: Boolean, val startX: Float, val startY: Float, val direction: Vector2) : Character() {

	override val width = (if (playerBullet) 3 * LunarGame.scale else 5 * LunarGame.scale).toInt()
	override val height = width
	override val sprite: Sprite
	override var texture: Texture = Texture(0, 0, Pixmap.Format.RGB565)
	override val screenOffsetToOffsetCamera = Gdx.graphics.height / 2
	override val rectangle: Rectangle
	val LIVE_THREE_SECONDS = if (playerBullet) 3f else 5f
	var ttl = LunarGame.clicks + LIVE_THREE_SECONDS
	val speed = if (playerBullet) 400f else 100f

	init {
		x = startX;
		y = startY
		val pixmap = Pixmap(width, height, Pixmap.Format.RGB888)
		if (playerBullet)
			pixmap.setColor(Color.YELLOW)
		else
			pixmap.setColor(Color.PINK)
		pixmap.fill()
		texture = Texture(pixmap)
		sprite = Sprite(texture)
		sprite.setCenter(width / 2f, height / 2f)
		sprite.setPosition(startX, startY)
		sprite.setSize(width.toFloat(), height.toFloat())
		pixmap.dispose()
		sprite.x = startX
		sprite.y = startY
		rectangle = Rectangle(x, y, width.toFloat(), height.toFloat())
	}

	override fun logic() {
		val origy = y
		super.logic()
		y = origy
		if (aboveGround1 > y) {
			LunarGame.removeCharacter(this)
		}
		x = x + direction.x * Gdx.graphics.deltaTime * speed
		y = y + direction.y * Gdx.graphics.deltaTime * speed
		if (ttl < LunarGame.clicks) {
			LunarGame.removeCharacter(this)
		}
		val cleanup = mutableListOf<() -> Unit>()
		if (playerBullet) {
			LunarGame.enemies.map { e ->
				if (e.rectangle.overlaps(rectangle)) {
					e.hit(this)
					cleanup.add({
						LunarGame.removeCharacter(e)
						LunarGame.removeCharacter(this)
					})
				}
			}
		} else {
			if (rectangle.overlaps(LunarGame.playerShip.rectangle)) {
				ScoreCard.playerHit()
				LunarGame.playerShip.hit(this)
				val explosion = Explosion(x,y,16,16)
				LunarGame.characters.add(explosion)
				cleanup.add({
					LunarGame.removeCharacter(this)
				})
			}
		}
		LunarGame.fuel.map { f ->
			if (f.rectangle.overlaps(rectangle)) {
				f.hit(this)
				cleanup.add({ LunarGame.removeCharacter(f) })
			}
		}
		cleanup.map { it() }
	}

}
