package org.bjason.lunargame

import com.badlogic.gdx.Gdx

object Sound {

	class LoadSound(file: String) {
		val sound: com.badlogic.gdx.audio.Sound
		var id = -999L
		var playing = false

		init {
			sound = Gdx.audio.newSound(Gdx.files.internal(file));
		}

		fun play() {
			id = sound.play()
		}

		fun playLoop() {
			if (!playing) {
				sound.play()
				//sound.setLooping(id, true)
				sound.loop()
				playing = true
			}
		}

		fun stop() {
			//sound.setLooping(id,false)
			sound.stop()
			playing = false
		}
	}

	val thrust by lazy {
		LoadSound("data/thrust.wav")
	}

	val fire by lazy {
		LoadSound("data/fire.wav");
	}
	val explode by lazy {
		LoadSound("data/explosion.wav");
	}
	val pickup by lazy {
		LoadSound("data/pickup.wav");
	}
}