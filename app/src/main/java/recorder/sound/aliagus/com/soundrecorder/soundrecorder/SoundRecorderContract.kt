package recorder.sound.aliagus.com.soundrecorder.soundrecorder

import android.os.Bundle
import java.io.File

/**
 * Created by ali on 05/03/18.
 */
interface SoundRecorderContract {
    interface View {
        fun initSavedInstanceState(savedInstanceState: Bundle?)
        fun initActionBar()
        fun initVisualizerView()
        fun stopPlaying()
        fun pauseRecording()
        fun resumeRecording()
        fun selectAudio()
        fun isPlaying(): Boolean
        fun startPlaying(filePath: String?)
        fun stopRecording()
        fun startTimer()
        fun stopTimer()
        fun stopPlayingFromList()
        fun setComponentVisibility(visibilitySaveMenu: Boolean?, visibilityListMenu: Boolean?, visibilityButtonRecord: Int?, visibilityButtonRestart: Int?)
    }

    interface Presenter {
        fun initFile()
        fun startRecorder(color: Int)
        fun saveRecorder(file: File, name: String)
        fun discardRecorder(file: File)
    }
}