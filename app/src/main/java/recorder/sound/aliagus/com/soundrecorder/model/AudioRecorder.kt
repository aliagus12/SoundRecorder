package recorder.sound.aliagus.com.soundrecorder.model

import android.app.Activity
import android.support.v4.app.Fragment
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.support.v4.startActivityForResult
import recorder.sound.aliagus.com.soundrecorder.lib.Library
import recorder.sound.aliagus.com.soundrecorder.soundrecorder.SoundRecorder

/**
 * Created by ali on 14/03/18.
 */
class AudioRecorder {

    private lateinit var activity: Activity

    private lateinit var fragment: Fragment

    private lateinit var filePath: String

    private var color: Int = 0

    private var requestCode = Library.requestCode

    private var source = Library.audioSource

    private var channel = Library.audioChannel

    private var sampleRate = Library.audioSampleRate

    private var autoStart: Boolean = Library.autoStart

    private var keepDisplayOn: Boolean = Library.keepDisplayOn

    companion object {
        fun with(activity: Activity): AudioRecorder {
            return AudioRecorder(activity)
        }

        fun with(fragment: Fragment): AudioRecorder {
            return AudioRecorder(fragment)
        }
    }

    private constructor(activity: Activity) {
        this.activity = activity
    }

    private constructor(fragment: Fragment) {
        this.fragment = fragment
    }

    fun setFilePath(filePath: String): AudioRecorder {
        this.filePath = filePath
        return this
    }

    fun setColor(color: Int): AudioRecorder {
        this.color = color
        return this
    }


    fun setSource(source: AudioSource): AudioRecorder {
        this.source = source
        return this
    }

    fun setRequestCode(requestCode: Int): AudioRecorder {
        this.requestCode = requestCode
        return this
    }

    fun setChannel(channel: AudioChannel): AudioRecorder {
        this.channel = channel
        return this
    }

    fun setSampleRate(sampleRate: AudioSampleRate): AudioRecorder {
        this.sampleRate = sampleRate
        return this
    }

    fun setAutoStart(autoStart: Boolean): AudioRecorder {
        this.autoStart = autoStart
        return this
    }

    fun setKeepDisplayOn(keepDisplayOn: Boolean): AudioRecorder {
        this.keepDisplayOn = keepDisplayOn
        return this
    }

    fun record() {
        activity.startActivityForResult<SoundRecorder>(requestCode,
                Library.EXTRA_FILE_PATH to filePath,
                Library.EXTRA_COLOR to color,
                Library.EXTRA_SOURCE to source,
                Library.EXTRA_CHANNEL to channel,
                Library.EXTRA_SAMPLE_RATE to sampleRate,
                Library.EXTRA_AUTO_START to autoStart,
                Library.EXTRA_KEEP_DISPLAY_ON to keepDisplayOn
        )
    }

    fun recordFromFragmet() {
        fragment.startActivityForResult<SoundRecorder>(requestCode,
                Library.EXTRA_FILE_PATH to filePath,
                Library.EXTRA_COLOR to color,
                Library.EXTRA_SOURCE to source,
                Library.EXTRA_CHANNEL to channel,
                Library.EXTRA_SAMPLE_RATE to sampleRate,
                Library.EXTRA_AUTO_START to autoStart,
                Library.EXTRA_KEEP_DISPLAY_ON to keepDisplayOn
        )
    }
}