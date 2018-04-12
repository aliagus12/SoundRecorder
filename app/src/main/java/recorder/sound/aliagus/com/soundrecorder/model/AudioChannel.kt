package recorder.sound.aliagus.com.soundrecorder.model

import android.media.AudioFormat

/**
 * Created by ali on 14/03/18.
 */
enum class AudioChannel {
    STEREO,
    MONO;

    val channel: Int
        get() {
            return when (this) {
                MONO -> AudioFormat.CHANNEL_IN_MONO
                else -> AudioFormat.CHANNEL_IN_STEREO
            }
        }
}