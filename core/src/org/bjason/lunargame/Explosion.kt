package org.bjason.lunargame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Rectangle

class Explosion(val startX: Float, val startY: Float,override val width:Int=Explosion.width,override val height:Int=Explosion.width) : Character() {

	//override val width = Explosion.width
	//override val height = Explosion.height
	override val sprite: Sprite
	override val texture: Texture
	override val screenOffsetToOffsetCamera = Gdx.graphics.height / 2
	override val rectangle: Rectangle
	var ttl = LunarGame.clicks + 3

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
		val width = (32 * LunarGame.scale).toInt()
		val height = (32 * LunarGame.scale).toInt()
		val colourlist = arrayListOf(Color.RED, Color.YELLOW, Color.ORANGE)
		val textureAsset by lazy {
			var i = 0
			val pixmap = Pixmap(width, height, Pixmap.Format.RGBA8888)
			for (xx in 0..width) {
				for (yy in 0..height) {
					pixmap.setColor(colourlist[i++])
					if (i >= colourlist.size) i = 0
					if (Math.random() * 1000 % 1000 > 900) {
						pixmap.fillRectangle(xx, yy, 2, 2)
					}
				}
			}
			val texture = Texture(pixmap)
			pixmap.dispose()
			texture
		}
	}

	override fun draw(batch: Batch?) {
		sprite.y = y - screenOffsetToOffsetCamera
		sprite.x = x
		rectangle.setPosition(x, y)
		sprite.rotate(5f)
		sprite.draw(batch)
	}

	override fun logic() {
		if (LunarGame.clicks > ttl) {
			LunarGame.removeCharacter(this)
		}
		val shrink = 0.9f
		sprite.setSize(sprite.width * shrink, sprite.height * shrink)
		x = x - shrink * 3// 1.5f
		y = y + shrink * 1// 1.5f
	}
}