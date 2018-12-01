package org.bjason.lunargame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator

object ScoreCard {
	val DEAD=20
	val font by lazy {
		val generator = FreeTypeFontGenerator(Gdx.files.internal("data/OpenSans-Light.ttf"));
		val parameter = FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = (16 * LunarGame.scale).toInt();
		parameter.color = Color.BLACK
		parameter.borderColor=Color.WHITE
		val font = generator.generateFont(parameter);
		generator.dispose();
		font
	}

	fun block(colour:Color):Texture {
		val pixmap = Pixmap(2, 2, Pixmap.Format.RGB888)
		pixmap.setColor(colour)
		pixmap.fill()
		val t = Texture(pixmap)
		pixmap.dispose()
		return t
	}	
	val green by lazy  { block(Color.GREEN) }
	val red by lazy  { block(Color.RED) }

	var playerHit = 0
	var fuelCollected = 0
	var enemyHit = 0

	fun reset() {
		playerHit = 0
		fuelCollected = 0
		enemyHit = 0
	}

	fun playerHit() {
		playerHit++
	}

	fun hitTooManyTimes(): Boolean = playerHit > DEAD

	fun enemyHit() {
		enemyHit++
	}

	fun fuelCollected() {
		fuelCollected++
		if (playerHit <= 0) playerHit = playerHit - 5
		else playerHit = playerHit - 5
	}

	val statusLineWidth=Gdx.graphics.width.toFloat()
	val offBottom=20f
	fun draw(batch: Batch) {
		val y = offBottom + font.getCapHeight()
	
		batch.draw(green,0f,0.toFloat(),statusLineWidth,y)
		if ( playerHit > 0 ) {
			val w = statusLineWidth/DEAD
			batch.draw(red,0f,0.toFloat(),w*playerHit,y)
			
		}
		font.draw(batch, "playerHit ${playerHit} fuelCollected ${fuelCollected} enemyHit ${enemyHit}", offBottom, y-5)
	}
}