package me.arthan.mmvox

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.math.Vector3
import kotlin.experimental.and
import kotlin.math.sqrt

class MMVox : ApplicationAdapter(), InputProcessor {

    private var camera: PerspectiveCamera? = null
    private var modelBatch: ModelBatch? = null
    private var modelBuilder: ModelBuilder? = null
    private var box: Model? = null
    private var modelInstance: ModelInstance? = null
    private var environment: Environment? = null

    private val position: Vector3 = Vector3()

    private val modelInstances: ArrayList<ModelInstance> = ArrayList()

    override fun create() {
        camera = PerspectiveCamera(75f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        camera!!.position.set(0f, 14f, 3f)
        camera!!.lookAt(0f, 11f, 0f)
        camera!!.near = 0.1f
        camera!!.far = 300f

        modelBatch = ModelBatch()
        modelBuilder = ModelBuilder()
        box = modelBuilder!!.createBox(1f, 1f, 1f,
                Material(ColorAttribute.createDiffuse(Color.BLUE)),
                VertexAttributes.Usage.Position.toLong() or VertexAttributes.Usage.Normal.toLong())
        for (x in -10 .. 10) {
            for (y in -10 .. 10) {
                for (z in -10 .. 10) {
                    modelInstances.add(ModelInstance(box, x.toFloat(), y.toFloat(), z.toFloat()))
//                    modelInstances[-1].getNode()
                }
            }
        }
        val player = modelBuilder!!.createBox(1f, 1f, 1f,
                Material(ColorAttribute.createDiffuse(Color.GREEN)),
                VertexAttributes.Usage.Position.toLong() or VertexAttributes.Usage.Normal.toLong())
        modelInstance = ModelInstance(player, 0f, 11f, 0f)

        environment = Environment()
        environment!!.set(ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1f))

        Gdx.input.inputProcessor = this

    }

    private fun isVisible(cam: Camera, instance: ModelInstance): Boolean {
        instance.transform.getTranslation(position)
        position.add(Vector3(0.5f, 0.5f, 0.5f))
        return cam.frustum.sphereInFrustum(position, 2f)
    }

    override fun render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
        camera!!.update()
        modelBatch!!.begin(camera)
        for (model in modelInstances) {
            if (isVisible(camera!!, model))
                modelBatch!!.render(model, environment)
        }
        modelBatch!!.render(modelInstance, environment)
        modelBatch!!.end()
        print(Gdx.graphics.framesPerSecond.toString() + "\n")
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        val pressed = character.toLowerCase()
        if (pressed == 'q') {
            camera!!.rotateAround(modelInstance!!.transform.getTranslation(Vector3()), Vector3(0f, 1f, 0f), 1f)
            modelInstance!!.transform.rotate(0f, 1f, 0f, 1f)
        }
        if (pressed == 'e') {
            camera!!.rotateAround(modelInstance!!.transform.getTranslation(Vector3()), Vector3(0f, 1f, 0f), -1f)
            modelInstance!!.transform.rotate(0f, 1f, 0f, -1f)
        }
        return true
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun keyDown(keycode: Int): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }
}
