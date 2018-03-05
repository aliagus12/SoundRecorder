package recorder.sound.aliagus.com.soundrecorder.playlist

/**
 * Created by ali on 01/03/18.
 */
interface PlaylistFragmentContract {
    interface View {
        fun setLayout()
    }

    interface Presenter {
        fun loadData()
    }
}