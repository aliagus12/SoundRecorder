package recorder.sound.aliagus.com.soundrecorder.soundrecorder

import android.media.MediaRecorder

/**
 * Created by ali on 05/03/18.
 */
interface SoundRecorderContract {
    interface View {
        fun initComponent()
        fun onStartRecorder()
        fun onPauseRecorder()
        fun onResumeRecorder()
        fun onStopRecorder()
        fun onPlayRecorder()
        fun onPlaylistRecorder()
        fun onStopPlayRecorder()
        fun setComponent()
    }

    interface Presenter {
        fun initFile()
        fun startRecorder(recorder: MediaRecorder)
        fun pauseRecorder(recorder: MediaRecorder)
        fun resumeRecorder(recorder: MediaRecorder)
        fun stopRecorder(recorder: MediaRecorder)
        fun playRecorder(recorder: MediaRecorder)
        fun stopPlayRecorder(recorder: MediaRecorder)
    }
}