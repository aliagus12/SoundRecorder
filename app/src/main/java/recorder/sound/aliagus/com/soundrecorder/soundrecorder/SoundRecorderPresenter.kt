package recorder.sound.aliagus.com.soundrecorder.soundrecorder

import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.support.annotation.RequiresApi
import java.io.File

/**
 * Created by ali on 05/03/18.
 */
class SoundRecorderPresenter(private var view: SoundRecorderContract.View) : SoundRecorderContract.Presenter {
    val path = File (Environment.getExternalStorageDirectory().path)
    lateinit var file: File
    val player = MediaPlayer()

    override fun initFile() {
        try {
            file = File.createTempFile("temporary", ".mp3", path)
        } catch (e: Exception) {

        }
    }

    override fun startRecorder(recorder: MediaRecorder) {
        recorder.setOutputFile(file.absolutePath)
        prepareRecorder(recorder)
        recorder.start()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun pauseRecorder(recorder: MediaRecorder) {
        prepareRecorder(recorder)
        recorder.pause()
    }

    override fun resumeRecorder(recorder: MediaRecorder) {
        prepareRecorder(recorder)
        recorder.resume()
    }

    override fun stopRecorder(recorder: MediaRecorder) {
        prepareRecorder(recorder)
        recorder.stop()
        recorder.release()
        try {
            player.setDataSource(file.absolutePath)
            player.prepare()
        } catch (e: Exception) {

        }
    }

    override fun playRecorder(recorder: MediaRecorder) {
        player.start()
    }

    override fun stopPlayRecorder(recorder: MediaRecorder) {
        player.stop()
    }

    private fun prepareRecorder(recorder: MediaRecorder) {
        try {
            recorder.prepare()
        } catch (e: Exception) {

        }
    }
}
