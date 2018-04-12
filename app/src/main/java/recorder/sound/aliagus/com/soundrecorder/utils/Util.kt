package recorder.sound.aliagus.com.soundrecorder.utils

import android.graphics.Color
import android.media.AudioFormat
import android.os.Handler
import recorder.sound.aliagus.com.soundrecorder.model.AudioChannel
import recorder.sound.aliagus.com.soundrecorder.model.AudioSampleRate
import recorder.sound.aliagus.com.soundrecorder.model.AudioSource

/**
 * Created by ali on 15/03/18.
 */
class Util {
    companion object {
        private val handler = Handler()
        fun wait(millis: Int?, callback: Runnable?) {
            millis?.toLong()?.let { handler.postDelayed(callback, it) }
        }

        fun getMic(
                source: AudioSource?,
                channel: AudioChannel?,
                sampleRate: AudioSampleRate?
        ): omrecorder.AudioSource {
            return omrecorder.AudioSource.Smart(
                    source?.source!!,
                    AudioFormat.ENCODING_PCM_16BIT,
                    channel?.channel!!,
                    sampleRate?.sampleRate!!
            )
        }

        fun isBrightColor(color: Int?): Boolean {
            if (android.R.color.transparent == color) {
                return true
            }
            val rgb = intArrayOf(Color.red(color!!), Color.green(color), Color.blue(color))
            val brightness = Math.sqrt(
                    rgb[0].toDouble() * rgb[0].toDouble() * 0.241 +
                            rgb[1].toDouble() * rgb[1].toDouble() * 0.691 +
                            rgb[2].toDouble() * rgb[2].toDouble() * 0.068).toInt()
            return brightness >= 200
        }

        fun getDarkerColor(color: Int): Int {
            val factor = 0.8f
            val a = Color.alpha(color)
            val r = Color.red(color)
            val g = Color.green(color)
            val b = Color.blue(color)
            return Color.argb(a,
                    Math.max((r * factor).toInt(), 0),
                    Math.max((g * factor).toInt(), 0),
                    Math.max((b * factor).toInt(), 0))
        }

        fun formatSeconds(seconds: Int): String {
            return getTwoDecimalsValue(seconds / 3600) + ":" +
                    getTwoDecimalsValue(seconds / 60) + ":" +
                    getTwoDecimalsValue(seconds % 60)
        }

        private fun getTwoDecimalsValue(value: Int): String {
            return if (value in 0..9) {
                "0$value"
            } else {
                "$value"
            }
        }
    }
}