package recorder.sound.aliagus.com.soundrecorder.playlist

import recorder.sound.aliagus.com.soundrecorder.model.Recorder
import java.io.File

/**
 * Created by ali on 01/03/18.
 */
interface PlaylistFragmentContract {
    interface View {
        fun setLayout()
        fun loadDataToAdapter(listRecorder: ArrayList<Recorder>, listTypes: ArrayList<Int>, fileArrays: Array<File>)
    }

    interface Presenter {
        fun loadData()
        fun deleteFile(file: File)
    }
}