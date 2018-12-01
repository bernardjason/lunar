package org.bjason.lunargame

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys

object MainGame : ApplicationAdapter() {

	var desktop=false
	var start = true
	var currentGame: ApplicationAdapter = BannerScreen()
	override fun create() {
		currentGame.create()
	}

	override fun render() {
		currentGame.render()
		if (start == true && (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE))) {
			val l = LunarGame()
			l.create()
			currentGame = l
			start = false
		}
	}

	fun endGame(message: String) {
		start = true
		currentGame = BannerScreen(message)
	}

	override fun pause() {
		System.exit(0)
	}

	override fun dispose() {
		currentGame.dispose()
	}

}