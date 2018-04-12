package recorder.sound.aliagus.com.soundrecorder.soundrecorder

import android.app.Activity
import android.os.Environment
import recorder.sound.aliagus.com.soundrecorder.lib.Library
import java.io.File

/**
 * Created by ali on 05/03/18.
 */
class SoundRecorderPresenter(private var activity: Activity, private var view: SoundRecorderContract.View) : SoundRecorderContract.Presenter {
    val path = File (Environment.getExternalStorageDirectory().path + "/" + Library.baseFile)
    lateinit var filePath: String
    private val TAG = javaClass.simpleName
    var requestCode = 101


    override fun initFile() {
        try {
            path.mkdirs()
        } catch (e: Exception) {

        }
    }

    override fun startRecorder(color: Int) {
    }

    override fun saveRecorder(file: File, newFileName: String) {
        val newFile = File(file.parentFile, newFileName + Library.extension)
        file.renameTo(newFile)
    }

    override fun discardRecorder(file: File) {
        file.delete()
    }
}
