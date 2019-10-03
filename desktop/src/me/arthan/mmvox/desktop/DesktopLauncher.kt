package me.arthan.mmvox.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import me.arthan.mmvox.MMVox

fun main(arg: Array<String>) {
    val config = LwjglApplicationConfiguration()
    LwjglApplication(MMVox(), config)
}
