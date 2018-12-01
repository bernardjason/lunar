package org.bjason.lunargame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

class PlayerShip(val startX: Float, val startY: Float) : Character() {

	override val width = PlayerShip.width
	override val height = PlayerShip.height
	override val sprite: Sprite
	override val texture: Texture
	override val screenOffsetToOffsetCamera = 0
	override val rectangle: Rectangle
	val h = 16f
	val w = 16f
	val polygon = Polygon(
		floatArrayOf(
			0f, h,
			-w, -h,
			w, -h,
			-w / 2, 0f,
			w / 2, 0f
		)
	)

	var thrust = 0f
	val MAX_THRUST = 5f
	val direction = Vector2(0f, 1f)
	val UPRIGHT = 20f
	var keepTrackOfThrustDisplay = 0f
	var fireEveryClick = 0f

	init {
		x = startX;
		y = startY
		texture = textureAsset
		sprite = Sprite(texture)
		sprite.setPosition(startX, startY)
		sprite.setSize(width.toFloat(), height.toFloat())
		sprite.setCenter(width / 2f, height / 2f)
		sprite.x = Gdx.graphics.width / 2f
		sprite.y = startY
		rectangle = Rectangle(x, y, width.toFloat(), height.toFloat())
	}

	companion object {
		val width = (32 * LunarGame.scale).toInt()
		val height = (32 * LunarGame.scale).toInt()
		val textureAsset by lazy {
			val pixmap = Pixmap(width, height, Pixmap.Format.RGBA8888)
			pixmap.setColor(Color.CYAN)
			pixmap.drawLine(0, height - 1, width / 2, 0)
			pixmap.drawLine(width / 2, 0, width, height - 1)
			pixmap.setColor(Color.YELLOW)
			pixmap.drawLine(0, height - 1, width, height - 1)
			val texture = Texture(pixmap)
			pixmap.dispose()
			texture
		}
	}

	override fun hit(c: Character) {
		//val explosion = Explosion(x,y)
		//LunarGame.characters.add(explosion)
	}

	fun rotate(dir: Float) {
		sprite.rotate(-dir)
		direction.rotate(-dir)
		polygon.rotate(-dir)
	}

	fun addThrust(force: Float) {
		thrust += force
		if (thrust >= MAX_THRUST) thrust = MAX_THRUST
		if (thrust > 0 && keepTrackOfThrustDisplay - LunarGame.clicks < 0) {
			val size = (Math.random() * 1000 % 10).toInt()
			LunarGame.addCharacter(
				FuelTrail(
					size,
					thrust,
					x + (width - size) / 2,
					y + (height - size) / 2,
					this,
					direction.cpy()
				)
			)
			keepTrackOfThrustDisplay = LunarGame.clicks + 0.04f
		}
		if (thrust == 1f) Sound.thrust.playLoop()
		if (thrust < 1f) Sound.thrust.stop()
	}

	fun fire() {
		if (fireEveryClick < LunarGame.clicks) {

			val d = Vector2(0f, 1f)
			d.rotate(sprite.rotation)
			LunarGame.addCharacter(Bullet(true, x + width / 2, y + height / 2, direction.cpy()))
			fireEveryClick = LunarGame.clicks + 0.25f
			Sound.fire.play()
		}
	}

	override fun logic() {
		val oldx = x
		val oldy = y
		if (thrust < 0) thrust = 0f

		x = x + direction.x * thrust
		y = y + direction.y * thrust
		if (gravity()) {
			thrust = 0f
			x = oldx
			y = oldy
			y = y + 0.5f
			val angle = Math.abs((sprite.rotation + 360) % 360)
			if (angle < UPRIGHT || angle > 360 - UPRIGHT) {
			} else {
				ScoreCard.playerHit()
			}
		}

		polygon.setPosition(x, y)

		val cleanup = mutableListOf<() -> Unit>()
		val v = polygon.transformedVertices
		LunarGame.fuel.map { fuel ->
			var i = 0
			while (i < v.size) {
				val xxv = v[i++] + width / 2
				val yyv = v[i++] + height / 2
				if (fuel.rectangle.contains(xxv, yyv)) {
					println("got some fuel ${fuel.rectangle.x} ${x} ${fuel.rectangle.y} ${y}")
					ScoreCard.fuelCollected()
					fuel.hit(this)
					cleanup.add({ LunarGame.removeCharacter(fuel) })
					Sound.pickup.play()
				}
			}
		}
		LunarGame.enemies.map { enemy ->
			var i = 0
			while (i < v.size) {
				val xxv = v[i++] + width / 2
				val yyv = v[i++] + height / 2
				if (enemy.rectangle.contains(xxv, yyv)) {
					enemy.hit(this)
					println("**************** opps  hit ${enemy.rectangle.x} ${x} ${enemy.rectangle.y} ${y}")
					cleanup.add({ LunarGame.removeCharacter(enemy) })
					ScoreCard.playerHit()
					ScoreCard.playerHit()
					val explosion = Explosion(x, y)
					LunarGame.characters.add(explosion)
				}
			}
		}
		cleanup.map { it() }
	}

	override fun draw(batch: Batch?) {
		rectangle.x = x
		rectangle.y = y
		sprite.x = Gdx.graphics.width / 2f
		sprite.y = y - screenOffsetToOffsetCamera
		sprite.draw(batch)
	}

	class FuelTrail(
		val size: Int,
		val thrust: Float,
		val startX: Float,
		val startY: Float,
		val playerShip: PlayerShip,
		val direction: Vector2
	) : Character() {
		override val width = size
		override val height = size
		override val sprite: Sprite
		override val texture: Texture
		override val screenOffsetToOffsetCamera = Gdx.graphics.height / 2
		override val rectangle: Rectangle
		val startVector = Vector2(startX, startY)
		val switchOn = LunarGame.clicks + 0.05f
		val switchOff = LunarGame.clicks + Math.random() * thrust / 2
		var speedx = -10f
		var speedy = -10f

		init {
			x = startX;
			y = startY
			texture = getTex()
			sprite = Sprite(texture)
			sprite.setSize(width.toFloat(), height.toFloat())
			sprite.setCenter(width / 1f, height / 1f)
			sprite.setPosition(startX, startY)
			sprite.x = startX
			sprite.y = startY
			rectangle = Rectangle(x, y, width.toFloat(), height.toFloat())
		}

		companion object {
			val textures = mutableListOf<Texture>()
			var whichTexture = 0

			init {
				for (i in 0..10) {
					val base = 0.40f
					val width = 32;
					val height = 32;
					val pixmap = Pixmap(width, height, Pixmap.Format.RGB888)
					pixmap.setColor(Color(base + Math.random().toFloat(), 0f, base + Math.random().toFloat(), 1f))
					pixmap.fill()
					textures.add(Texture(pixmap))
					pixmap.dispose()
				}
			}

			fun getTex(): Texture {
				val t = textures[whichTexture++]
				if (whichTexture >= textures.size) whichTexture = 0
				return t;
			}
		}

		override fun logic() {
			if (LunarGame.clicks > switchOff) {
				LunarGame.removeCharacter(this)
			}
			x = x + direction.x * speedx
			y = y + direction.y * speedy
			speedx = speedx * 0.15f
			speedy = speedy * 0.15f
		}

		override fun draw(batch: Batch?) {
			if (LunarGame.clicks > switchOn) {
				super.draw(batch)
			}
		}
	}
}