package recorder.sound.aliagus.com.soundrecorder.lib

import android.graphics.Color
import android.os.Environment
import recorder.sound.aliagus.com.soundrecorder.model.AudioChannel
import recorder.sound.aliagus.com.soundrecorder.model.AudioSource
import recorder.sound.aliagus.com.soundrecorder.model.AudioSampleRate

/**
 * Created by ali on 05/03/18.
 */
class Library {
    companion object {
        const val temporary = "temporary"
        const val extension = ".mp3"
        const val baseFile = "SoundRecorder"
        const val save = "save"
        const val discard = "discard"
        const val saved = " saved!"
        const val deleted = "file deleted!"
        const val EXTRA_FILE_PATH = "filePath"
        const val EXTRA_COLOR = "color"
        const val EXTRA_SOURCE = "source"
        const val EXTRA_CHANNEL = "channel"
        const val EXTRA_SAMPLE_RATE = "sampleRate"
        const val EXTRA_AUTO_START = "autoStart"
        const val EXTRA_KEEP_DISPLAY_ON = "keepDisplayOn"
        val filePath = Environment.getExternalStorageDirectory()
        val audioSource = AudioSource.MIC
        val audioChannel = AudioChannel.STEREO
        var audioSampleRate = AudioSampleRate.HZ_44100
        var color = Color.parseColor("#546E7A")
        var requestCode = 0
        var autoStart = false
        var keepDisplayOn = false

    }
}