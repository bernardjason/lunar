package org.bjason.lunargame

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.utils.Align

class BannerScreen(val msg: String? = null) : ApplicationAdapter() {

	val font by lazy {
		val generator = FreeTypeFontGenerator(Gdx.files.internal("data/OpenSans-Light.ttf"));
		val parameter = FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = (32 * LunarGame.scale).toInt();
		parameter.color = Color.YELLOW

		val font = generator.generateFont(parameter);
		generator.dispose();
		font
	}

	fun block(colour: Color): Texture {
		val pixmap = Pixmap(2, 2, Pixmap.Format.RGB888)
		pixmap.setColor(colour)
		pixmap.fill()
		val t = Texture(pixmap)
		pixmap.dispose()
		return t
	}

	val fingerPrint by lazy { Texture(Gdx.files.internal("data/fingerprint.png")) }
	val phone by lazy { Texture(Gdx.files.internal("data/phone.png")) }

	val thrust by lazy { block(Color.RED) }
	val fire by lazy { block(Color.YELLOW) }


	val message by lazy {
		if (msg == null) {
			"Fly around picking up alien fuel and shoot enemy ships. " +
					(
							if (MainGame.desktop) "Desktop use arrow keys, shift and space.\n"
							else "Tilt phone to steer, bottom screen press for thrust and fire.\n"
							) +
					"Touch screen or space to start."
		} else msg
	}

	val batch by lazy { SpriteBatch() }

	override fun create() {
	}

	var rotate = 0f
	var rotateDir = 1f
	var showFinger = 4f
	fun animatePhoneFinger(batch: Batch, y: Float, h: Float) {
		val pw = phone.width.toFloat()
		val ph = phone.height.toFloat()
		val x = Gdx.graphics.width / 2 - pw / 2
		batch.draw(phone, x, y, pw / 2, ph / 2, pw, ph, 1f, 1f, rotate, 0, 0, pw.toInt(), ph.toInt(), false, false)
		if (Math.round(rotate) == 0 && showFinger > 0) {
			val yy = h + 20f
			showFinger = showFinger - Gdx.graphics.deltaTime
			if (showFinger > 2f) {
				batch.draw(fire, Gdx.graphics.width * 0.25f, 0f, Gdx.graphics.width.toFloat() * 0.5f, h)
				font.setColor(Color.BLACK)
				font.draw(batch, "FIRE", Gdx.graphics.width * 0.50f, h - font.capHeight / 2)

				batch.draw(fingerPrint, x + pw * 0.55f, yy, h, h)
				font.color = Color.YELLOW
				font.draw(batch, "fire", x + h + pw * 0.55f, yy + font.capHeight)
			} else {
				batch.draw(thrust, 0f, 0.toFloat(), Gdx.graphics.width * 0.25f, h)
				font.setColor(Color.BLACK)
				font.draw(
					batch, " THRUST", 0f, h - font.capHeight / 2
				)
				batch.draw(fingerPrint, x + h / 2, yy, h, h)
				font.color = Color.YELLOW
				font.draw(batch, "  thrust", x + h, yy + font.capHeight)

			}
		} else {
			showFinger = 3f
			rotate = rotate + rotateDir
			if (rotate > 25) rotateDir = -1f
			if (rotate < -25) rotateDir = 1f

		}

	}

	override fun render() {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		val h = font.capHeight * 2
		if (!MainGame.desktop) {
			animatePhoneFinger(batch, h, h)
		}



		font.setColor(Color.CYAN)
		font.draw(
			batch,
			"Welcome to Lunar Game",
			Gdx.graphics.width * 0.25f,
			Gdx.graphics.height * 0.95f,
			Gdx.graphics.width * 0.5f,
			Align.center,
			false
		);
		font.setColor(Color.YELLOW)
		font.draw(
			batch,
			message,
			Gdx.graphics.width * 0.05f,
			Gdx.graphics.height * 0.75f,
			Gdx.graphics.width * 0.90f,
			Align.left,
			true
		);
		batch.end();

	}

	override fun pause() {
		System.exit(0)
	}

	override fun dispose() {
	}

}