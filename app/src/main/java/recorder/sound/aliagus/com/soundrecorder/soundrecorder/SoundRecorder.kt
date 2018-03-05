package recorder.sound.aliagus.com.soundrecorder.soundrecorder

import android.media.MediaRecorder
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_sound_recorder.*
import recorder.sound.aliagus.com.soundrecorder.R
import recorder.sound.aliagus.com.soundrecorder.playlist.PlaylistFragment
import recorder.sound.aliagus.com.soundrecorder.utils.Animated

class SoundRecorder : AppCompatActivity(), SoundRecorderContract.View, View.OnClickListener {

    private val soundRecorderPresenter: SoundRecorderPresenter by lazy {
        SoundRecorderPresenter(this)
    }

    private lateinit var playListFragment: PlaylistFragment
    lateinit var recorder: MediaRecorder
    companion object {
        const val VISIBLE = 1
        const val GONE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sound_recorder)
        initComponent()
        setComponent()
        soundRecorderPresenter.initFile()
    }

    override fun initComponent() {
        recorder = MediaRecorder()
    }

    override fun setComponent() {
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        floatingActionStartRecord.setOnClickListener(this)
        floatingActionPauseRecorder.setOnClickListener(this)
        floatingActionResumeRecorder.setOnClickListener(this)
        floatingActionStopRecorder.setOnClickListener(this)
        floatingActionPlayRecorder.setOnClickListener(this)
        floatingActionStopPlayRecorder.setOnClickListener(this)
        floatingActionPlayListRecorder.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        view?.id.let {
            when (it) {
                R.id.floatingActionStartRecord -> {
                    onStartRecorder()
                }

                R.id.floatingActionStopRecorder -> {
                    onStopRecorder()
                }

                R.id.floatingActionPlayRecorder -> {
                    onPlayRecorder()
                }

                R.id.floatingActionPlayListRecorder -> {
                    onPlaylistRecorder()
                }

                R.id.floatingActionStopPlayRecorder -> {
                    onStopPlayRecorder()
                }

                R.id.floatingActionPauseRecorder -> {
                    onPauseRecorder()
                }

                R.id.floatingActionResumeRecorder -> {
                    onResumeRecorder()
                }
            }
        }
    }

    override fun onStartRecorder() {
        Animated.animatedView(floatingActionStartRecord, GONE)
        Animated.animatedView(floatingActionStopRecorder, VISIBLE)
        Animated.animatedView(floatingActionPlayRecorder, GONE)
        Animated.animatedView(floatingActionPlayListRecorder, GONE)
        Animated.animatedView(floatingActionPauseRecorder, VISIBLE)
        soundRecorderPresenter.startRecorder(recorder)
    }

    override fun onPauseRecorder() {
        Animated.animatedView(floatingActionPauseRecorder, GONE)
        Animated.animatedView(floatingActionResumeRecorder, VISIBLE)
        soundRecorderPresenter.pauseRecorder(recorder)
    }

    override fun onResumeRecorder() {
        Animated.animatedView(floatingActionPauseRecorder, VISIBLE)
        Animated.animatedView(floatingActionResumeRecorder, GONE)
        soundRecorderPresenter.resumeRecorder(recorder)
    }

    override fun onStopRecorder() {
        Animated.animatedView(floatingActionStartRecord, VISIBLE)
        Animated.animatedView(floatingActionStopRecorder, GONE)
        Animated.animatedView(floatingActionPlayRecorder, VISIBLE)
        Animated.animatedView(floatingActionPlayListRecorder, VISIBLE)
        Animated.animatedView(floatingActionResumeRecorder, GONE)
        Animated.animatedView(floatingActionPauseRecorder, GONE)
        soundRecorderPresenter.stopRecorder(recorder)

    }

    override fun onPlayRecorder() {
        Animated.animatedView(floatingActionPlayRecorder, GONE)
        Animated.animatedView(floatingActionStopPlayRecorder, VISIBLE)
        Animated.animatedView(floatingActionPlayListRecorder, GONE)
        Animated.animatedView(floatingActionStartRecord, GONE)
        soundRecorderPresenter.playRecorder(recorder)
    }

    override fun onStopPlayRecorder() {
        Animated.animatedView(floatingActionPlayRecorder, VISIBLE)
        Animated.animatedView(floatingActionStopPlayRecorder, GONE)
        Animated.animatedView(floatingActionPlayListRecorder, VISIBLE)
        Animated.animatedView(floatingActionStartRecord, VISIBLE)
        soundRecorderPresenter.stopPlayRecorder(recorder)
    }

    override fun onPlaylistRecorder() {
        playListFragment = PlaylistFragment()
        playListFragment.show(
                supportFragmentManager,
                playListFragment.tag
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }
}
