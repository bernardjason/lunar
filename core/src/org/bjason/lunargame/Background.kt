package org.bjason.lunargame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch

object BackGround {

	val textureAsset1 by lazy { Texture(Gdx.files.internal("data/sky1.png")) }
	val textureAsset2 by lazy { Texture(Gdx.files.internal("data/sky2.png")) }
	val textureAsset3 by lazy { Texture(Gdx.files.internal("data/sky3.png")) }
	var x1=0f
	var x2=0f
	var x=0f
	val y=-100f

	fun setup(x:Float) {
		x1=x-Gdx.graphics.width/2
		x2=x+Gdx.graphics.width/2
		this.x=x
	}
	val textures1 by lazy { arrayListOf(textureAsset1,textureAsset2,textureAsset1,textureAsset3,textureAsset1)}
	val textures2 by lazy { arrayListOf(textureAsset2,textureAsset1,textureAsset3,textureAsset1)}
	var t1 =textures1[0] 
	var t2 =textures2[1] 
	var i1=0
	var i2=1
	fun draw(batch: Batch,x:Float) {
		val diff =this.x - x
		x1=x1+diff
		x2=x2+diff
		this.x=x
		if ( x1 < x-Gdx.graphics.width*1.5f ) {
			x1=x+Gdx.graphics.width/2
			i1=i1+1 ; if ( i1 >= textures1.size ) i1=0
			t1=textures1[i1]

		}
		if ( x1 > x+Gdx.graphics.width*0.5f ) {
			x1=x-Gdx.graphics.width*1.5f
			i1=i1-1 ; if ( i1 < 0  ) i1=textures1.size-1
			t1=textures1[i1]
		}
		batch.draw(t1, x1, y, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
		
		
		if ( x2 < x-Gdx.graphics.width*1.5f ) {
			x2=x+Gdx.graphics.width/2
			i2=i2+1 ; if ( i2 >= textures2.size ) i2=0
			t2=textures2[i2]
		}
		if ( x2 > x+Gdx.graphics.width*0.5f ) {
			x2=x-Gdx.graphics.width*1.5f
			i2=i2-1 ; if ( i2 < 0  ) i2=textures2.size-1
			t2=textures2[i2]
		}
		batch.draw(t2, x2, y, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
	}
}