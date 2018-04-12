package recorder.sound.aliagus.com.soundrecorder.model

import android.media.MediaRecorder

/**
 * Created by ali on 14/03/18.
 */
enum class AudioSource {
    MIC,
    CAMCORDER;

    val source: Int
        get() {
            return when (this) {
                CAMCORDER -> MediaRecorder.AudioSource.CAMCORDER
                else -> MediaRecorder.AudioSource.MIC
            }
        }
}