package recorder.sound.aliagus.com.soundrecorder.utils

import com.cleveroad.audiovisualization.DbmHandler

/**
 * Created by ali on 15/03/18.
 */
class VisualizerHandler: DbmHandler<Float>() {
    override fun onDataReceivedImpl(data: Float?, layersCount: Int, dBmArray: FloatArray?, ampsArray: FloatArray?) {
        var amplitude = data?.div(100)
        if (amplitude!! <= 0.5) {
            amplitude = 0.0f
        } else if (amplitude > 0.5 && amplitude <= 0.6) {
            amplitude = 0.2f
        } else if (amplitude > 0.6 && amplitude <= 0.7) {
            amplitude = 0.6f
        } else if (amplitude > 0.7) {
            amplitude = 1f
        }
        try {
            dBmArray?.set(0, amplitude)
            ampsArray?.set(0, amplitude)
        } catch (e: Exception) { }
    }

    fun stop() {
        try {
            calmDownAndStopRendering()
        } catch (e: Exception) { }
    }

}