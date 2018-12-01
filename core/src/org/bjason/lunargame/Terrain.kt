package org.bjason.lunargame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite

class Terrain(val startX: Float, startHeight: Int = 0, endHeight: Int = 0) {
	val heights = mutableListOf<Int>()
	val maxHeight = Gdx.graphics.height / 2
	val increaseBy = 20
	val texture: Texture
	val sprite: Sprite
	var x = startX
	var y = -TerrainHelper.HEIGHT.toFloat()
	val LAUNCH_ENEMY = Gdx.graphics.height.toFloat()

	var currentEnemy = EnemyShip(startX + 100f, LAUNCH_ENEMY)
	var currentFuel = Fuel( startX + (Math.random()* 10000 % TerrainHelper.maxWidth).toFloat(), Gdx.graphics.height*0.75f)

	init {
		heights.add(0, startHeight)
		for (i in 1..TerrainHelper.maxWidth / TerrainHelper.space - 1) {
			var next = heights[i - 1] + (Math.random() * 1000 % increaseBy - increaseBy / 2).toInt()
			if (next < TerrainHelper.minHeight) next = next + increaseBy / 2
			if (next >= maxHeight*0.9f) next = next - increaseBy / 2
			heights.add(next)
		}
		heights.add(endHeight)
		val pixmap = Pixmap(TerrainHelper.maxWidth.toInt(), TerrainHelper.HEIGHT, Pixmap.Format.RGB888)
		pixmap.setColor(Color.CYAN)
		var previousx = 0
		var previousy = heights[0] - TerrainHelper.minHeight
		var i = 0
		for (xx in 1..TerrainHelper.maxWidth + 1 step TerrainHelper.space) {
			pixmap.drawLine(previousx, previousy, xx, heights[i] - TerrainHelper.minHeight)
			previousx = xx
			previousy = heights[i] - TerrainHelper.minHeight
			i++
		}
		pixmap.drawLine(previousx, previousy, TerrainHelper.maxWidth, heights.last())

		texture = Texture(pixmap)
		sprite = Sprite(texture)
		sprite.setCenterY(TerrainHelper.HEIGHT / 2f)
		sprite.setPosition(x, y)
		sprite.setSize(TerrainHelper.maxWidth.toFloat(), TerrainHelper.HEIGHT.toFloat())
		pixmap.dispose()

		LunarGame.addCharacter(currentFuel)
		
		LunarGame.addCharacter(currentEnemy)
	}
	val DELAY_ENEMY=15f
	var launchEnemyCountDown=LunarGame.clicks+DELAY_ENEMY
	fun launchEnemy(): Boolean {
		
		if (launchEnemyCountDown < LunarGame.clicks ) {
			launchEnemyCountDown=LunarGame.clicks+DELAY_ENEMY
			return true;
		}
		return false;
	}

	fun logic() {
		if (currentEnemy.done == true && launchEnemy()) {
			if (Math.abs(LunarGame.playerShip.x - startX) < Gdx.graphics.width) {
				currentEnemy = EnemyShip(LunarGame.playerShip.x + 100f, LAUNCH_ENEMY)
				LunarGame.addCharacter(currentEnemy)
			}
		}
		if ( currentFuel.done ) {
			currentFuel = Fuel( startX + (Math.random()* 10000 % TerrainHelper.maxWidth).toFloat(), Gdx.graphics.height.toFloat()*1.10f)
			LunarGame.addCharacter(currentFuel)
		}
	}

	fun draw(batch: Batch?) {
		sprite.draw(batch)
	}

	fun dispose() {
		texture.dispose()
	}
}

object TerrainHelper {
	val terrains = mutableMapOf<Int, Terrain>()
	val maxWidth = 1000
	val minHeight = 10
	val HEIGHT = (Gdx.graphics.height*0.5f ).toInt()
	val space = 8

	fun getTerrains(x: Float): List<Terrain> {

		val toReturn = mutableListOf<Terrain>()

		for (around in x.toInt() - maxWidth * 2..x.toInt() + maxWidth step maxWidth) {
			val indexX = (around / maxWidth).toInt()
			val startHeight = if (terrains.contains(indexX - 1)) {
				terrains.get(indexX - 1)!!.heights[maxWidth / space]
			} else {
				200
			}
			val endHeight = if (terrains.contains(indexX)) {
				terrains.get(indexX)!!.heights[0]
			} else {
				200
			}

			toReturn.add(terrains.getOrPut(indexX) { Terrain(indexX * maxWidth.toFloat(), startHeight, endHeight) })
		}
		return toReturn;
	}

	fun getCurrentTerrain(x: Float, offset: Int = 0): Terrain {

		val indexX = (x / maxWidth).toInt()
		return terrains.getOrPut(indexX + offset) {
			val startHeight = if (terrains.contains(indexX - 1)) {
				terrains.get(indexX - 1)!!.heights[maxWidth / space]
			} else {
				200
			}
			val endHeight = if (terrains.contains(indexX)) {
				terrains.get(indexX)!!.heights[0]
			} else {
				200
			}
			Terrain(indexX * maxWidth.toFloat(),startHeight,endHeight) }
	}

	fun getHeightAtPosition(x: Float): Int {
		val t = if (x < 0) {
			getCurrentTerrain(x, -1)
		} else {
			getCurrentTerrain(x)
		}
		val relX = ((x - t.startX) / space).toInt()
		val height1 = t.heights[relX]
		val height2 = if (relX >= maxWidth / space) {
			val t2 = getCurrentTerrain(x)
			t2.heights[0]
		} else {
			t.heights[relX + 1]
		}
		val avg = (height1 + height2) / 2
		return HEIGHT - avg
	}


}