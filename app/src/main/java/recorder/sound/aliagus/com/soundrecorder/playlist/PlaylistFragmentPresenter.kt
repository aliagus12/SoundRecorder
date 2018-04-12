package recorder.sound.aliagus.com.soundrecorder.playlist

import android.os.Environment
import recorder.sound.aliagus.com.soundrecorder.lib.Library
import recorder.sound.aliagus.com.soundrecorder.model.Recorder
import java.io.File

/**
 * Created by ali on 01/03/18.
 */
class PlaylistFragmentPresenter(var view: PlaylistFragmentContract.View) : PlaylistFragmentContract.Presenter {

    private val TAG = javaClass.simpleName

    override fun loadData() {
        val filePath = File(Environment.getExternalStorageDirectory().path + "/" + Library.baseFile)
        var fileArrays = filePath.listFiles()
        var listRecorder = ArrayList<Recorder>()
        var listTypes = ArrayList<Int>()
        fileArrays.sortByDescending { it.lastModified() }
        fileArrays.forEach {
            listRecorder.add(Recorder(
                    it.name,
                    it.lastModified(),
                    it.length()
            ))
        }
        view.loadDataToAdapter(listRecorder, listTypes, fileArrays)
    }

    override fun deleteFile(file: File) {
        file.delete()
    }

}