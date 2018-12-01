package org.bjason.lunargame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.MathUtils

class EnemyShip(val startX: Float, val startY: Float) : Character() {

	override val width = (64 * LunarGame.scale).toInt()
	override val height = (32 * LunarGame.scale).toInt()
	override val sprite: Sprite
	override val texture: Texture
	override val screenOffsetToOffsetCamera = Gdx.graphics.height / 2
	var onGround = false
	override val rectangle: Rectangle
	val move = Vector2(0f, 0f)
	val MAX_CHASE = 5f
	var clickCounter = MAX_CHASE
	var ttl = MAX_CHASE * 8f
	val MAX_SPEED = 100f
	var done = false
	var chasePlayer = true
	var fireEveryClick = LunarGame.clicks + 1f
	val LOWEST = Gdx.graphics.height * 0.75f

	init {
		x = startX;
		y = startY
		texture = textureAsset
		sprite = Sprite(texture)
		sprite.setCenter(width / 2f, height / 2f)
		sprite.setPosition(startX, startY)
		sprite.setSize(width.toFloat(), height.toFloat())
		sprite.x = startX
		sprite.y = startY
		rectangle = Rectangle(x, y, width.toFloat(), height.toFloat())
	}

	companion object {
		val textureAsset by lazy {
			Texture(Gdx.files.internal("data/enemy.png"))
		}
	}

	override fun hit(c: Character) {
		done = true
		val explosion = Explosion(x,y)
		LunarGame.characters.add(explosion)
		Sound.explode.play()
		if (c is Bullet && c.playerBullet) {
			ScoreCard.enemyHit()
		}
	}

	fun fire() {

		if (Math.abs(LunarGame.playerShip.x - x) < Gdx.graphics.width / 2) {

			if (fireEveryClick < LunarGame.clicks) {
				val d = Vector2(LunarGame.playerShip.x - x, LunarGame.playerShip.y - y)
				d.nor()
				LunarGame.addCharacter(Bullet(false, x + width / 2, y + height / 2, d))
				fireEveryClick = LunarGame.clicks + 0.75f
			}
		}
	}

	override fun logic() {
		//super.logic()
		fire()
		clickCounter = clickCounter - Gdx.graphics.deltaTime
		ttl = ttl - Gdx.graphics.deltaTime

		if (clickCounter < 0) {
			chasePlayer = !chasePlayer
			clickCounter = (Math.random() * 1000 % MAX_CHASE).toFloat()
			if (!chasePlayer) clickCounter = clickCounter / 2
		}
		if (chasePlayer) {
			move.x = LunarGame.playerShip.x - x
		} else {
			move.x = startX
		}
		move.y = LunarGame.playerShip.y - y

		move.nor()
		move.scl(MAX_SPEED)

		y = y + move.y * Gdx.graphics.deltaTime
		x = x + move.x * Gdx.graphics.deltaTime

		if (y <= LOWEST) y = LOWEST

		if (ttl < 0) {
			move.y = MAX_SPEED * 2f
			move.x = 0f
			y = y + move.y * Gdx.graphics.deltaTime
			val awayX = Math.abs(LunarGame.playerShip.x - x)
			val awayY = Math.abs(LunarGame.playerShip.y - y)
			if (awayX > Gdx.graphics.width || awayY > Gdx.graphics.height) {
				done = true
				LunarGame.removeCharacter(this)
			}
		}
		val cleanup = mutableListOf<() -> Unit>()

		LunarGame.fuel.map { fuel ->
			if (fuel.rectangle.overlaps(rectangle)) {
				println("Sorry!!! got some fuel ${fuel.rectangle.x} ${x} ${fuel.rectangle.y} ${y}")
				cleanup.add({ LunarGame.removeCharacter(fuel) })
			}
		}
		cleanup.map { it() }
	}
}
