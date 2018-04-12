package recorder.sound.aliagus.com.soundrecorder.soundrecorder

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import com.cleveroad.audiovisualization.DbmHandler
import kotlinx.android.synthetic.main.activity_audio_recorder.*
import omrecorder.AudioChunk
import omrecorder.OmRecorder
import omrecorder.PullTransport
import omrecorder.Recorder
import org.jetbrains.anko.*
import org.jetbrains.anko.design.snackbar
import recorder.sound.aliagus.com.soundrecorder.R
import recorder.sound.aliagus.com.soundrecorder.lib.Library
import recorder.sound.aliagus.com.soundrecorder.model.AudioChannel
import recorder.sound.aliagus.com.soundrecorder.model.AudioSampleRate
import recorder.sound.aliagus.com.soundrecorder.model.AudioSource
import recorder.sound.aliagus.com.soundrecorder.playlist.PlaylistFragment
import recorder.sound.aliagus.com.soundrecorder.utils.Util
import recorder.sound.aliagus.com.soundrecorder.utils.VisualizerHandler
import java.io.File
import java.util.*

class SoundRecorder : AppCompatActivity(), SoundRecorderContract.View,
        PullTransport.OnAudioChunkPulledListener,
        MediaPlayer.OnCompletionListener {

    private val soundRecorderPresenter: SoundRecorderPresenter by lazy {
        SoundRecorderPresenter(this, this)
    }

    private val requestCode = 101

    private lateinit var playListFragment: PlaylistFragment
    val TAG = javaClass.simpleName

    private var filePath: String? = null
    private var source: AudioSource? = null
    private var channel: AudioChannel? = null
    private var sampleRate: AudioSampleRate? = null
    private var color: Int = 0
    private var autoStart: Boolean = false
    private var keepDisplayOn: Boolean = false

    private var player: MediaPlayer? = null
    private var recorder: Recorder? = null
    private var visualizerHandler: VisualizerHandler? = null

    private var timer: Timer? = null
    private var saveMenuItem: MenuItem? = null
    private var listMenuItem: MenuItem? = null
    private var recorderSecondsElapsed: Int = 0
    private var playerSecondsElapsed: Int = 0
    private var isRecording = false
    private var isFromList = false
    private var fileRecord: File? = null
    private var alertDialog: DialogInterface? = null
    private var isFirst = true

    companion object {
        const val VISIBLE = 1
        const val GONE = 2
        const val ID_TEXT_NEW_NAME = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_recorder)
        initSavedInstanceState(savedInstanceState)
        if (keepDisplayOn) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        initActionBar()
    }

    override fun initSavedInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            filePath = savedInstanceState.getString(Library.EXTRA_FILE_PATH)
            source = savedInstanceState.getSerializable(Library.EXTRA_SOURCE) as AudioSource
            channel = savedInstanceState.getSerializable(Library.EXTRA_CHANNEL) as AudioChannel
            sampleRate = savedInstanceState.getSerializable(Library.EXTRA_SAMPLE_RATE) as AudioSampleRate
            color = savedInstanceState.getInt(Library.EXTRA_COLOR)
            autoStart = savedInstanceState.getBoolean(Library.EXTRA_AUTO_START)
            keepDisplayOn = savedInstanceState.getBoolean(Library.EXTRA_KEEP_DISPLAY_ON)
        } else {
            filePath = intent.getStringExtra(Library.EXTRA_FILE_PATH)
            source = intent.getSerializableExtra(Library.EXTRA_SOURCE) as AudioSource
            channel = intent.getSerializableExtra(Library.EXTRA_CHANNEL) as AudioChannel
            sampleRate = intent.getSerializableExtra(Library.EXTRA_SAMPLE_RATE) as AudioSampleRate
            color = intent.getIntExtra(Library.EXTRA_COLOR, Color.BLACK)
            autoStart = intent.getBooleanExtra(Library.EXTRA_AUTO_START, false)
            keepDisplayOn = intent.getBooleanExtra(Library.EXTRA_KEEP_DISPLAY_ON, false)
        }
        Log.d(TAG, color.toString())

    }

    override fun initActionBar() {
        toolbar.setNavigationIcon(R.drawable.ic_clear)
        toolbar.inflateMenu(R.menu.audio_recorder)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        saveMenuItem = toolbar.menu.findItem(R.id.action_save)
        listMenuItem = toolbar.menu.findItem(R.id.record_list)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_save -> {
                    selectAudio()
                    true
                }

                R.id.record_list -> {
                    playListFragment = PlaylistFragment()
                    playListFragment.show(
                            supportFragmentManager,
                            playListFragment.tag
                    )
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    override fun initVisualizerView() {
        content.backgroundColor = Util.getDarkerColor(color)
        image_button_restart.visibility = View.GONE
        image_button_play.visibility = View.GONE
        listMenuItem?.isVisible = true
        saveMenuItem?.isVisible = false

        if (Util.isBrightColor(color)) {
            ContextCompat.getDrawable(this, R.drawable.ic_clear)?.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP)
            ContextCompat.getDrawable(this, R.drawable.ic_check)?.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP)
            status.setTextColor(Color.BLACK)
            timer_recorder.setTextColor(Color.BLACK)
            image_button_restart.setColorFilter(Color.BLACK)
            image_button_record.setColorFilter(Color.BLACK)
            image_button_play.setColorFilter(Color.BLACK)
        }
    }

    /*override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (autoStart && !isRecording) {
            toggleRecording(null)
        }
    }*/

    fun toggleRecording(v: View?) {
        stopPlaying()
        Util.wait(100, Runnable {
            if (isRecording) {
                pauseRecording()
            } else {
                resumeRecording()
            }
        })

    }

    fun togglePlaying(v: View?) {
        pauseRecording()
        Util.wait(100, Runnable {
            if (isPlaying()) {
                stopPlaying()
            } else {
                startPlaying(filePath)
            }
        })
    }

    override fun isPlaying(): Boolean {
        return try {
            player != null && player!!.isPlaying && !isRecording
        } catch (e: Exception) {
            false
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            initVisualizerView()
            visualizerView?.onResume()
        } catch (e: Exception) {
        }
    }

    override fun onPause() {
        restartRecording(null)
        try {
            visualizerView?.onPause()
        } catch (e: Exception) {
        }
        super.onPause()
    }

    override fun onDestroy() {
        restartRecording(null)
        setResult(Activity.RESULT_CANCELED)
        try {
            visualizerView?.onPause()
        } catch (e: Exception) {
        }
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString(Library.EXTRA_FILE_PATH, filePath)
        outState?.putInt(Library.EXTRA_COLOR, color)
        super.onSaveInstanceState(outState)
    }

    override fun startPlaying(filePath: String?) {
        try {
            setComponentVisibility(false, false, View.GONE, View.GONE)
            stopRecording()
            player = MediaPlayer()
            player?.setDataSource(filePath)
            player?.prepare()
            player?.start()
            visualizerView?.linkTo(DbmHandler.Factory.newVisualizerHandler(this, player!!))
            visualizerView?.post({
                player?.setOnCompletionListener(this@SoundRecorder)
            })
            timer_recorder.text = "00:00:00"
            status.setText(R.string.playing)
            status.visibility = View.VISIBLE
            image_button_play.setImageResource(R.drawable.ic_stop)
            playerSecondsElapsed = 0
            startTimer()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun setComponentVisibility(
            visibilitySaveMenu: Boolean?,
            visibilityListMenu: Boolean?,
            visibilityButtonRecord: Int?,
            visibilityButtonRestart: Int?
            ) {
        visibilitySaveMenu?.let { saveMenuItem?.isVisible = visibilityListMenu!! }
        visibilityListMenu?.let { listMenuItem?.isVisible = visibilityListMenu!! }
        visibilityButtonRestart?.let { image_button_restart.visibility = visibilityButtonRestart!!}
        visibilityButtonRecord?.let { image_button_record.visibility = visibilityButtonRecord!!}
    }

    override fun stopPlaying() {
        listMenuItem?.isVisible = false
        saveMenuItem?.isVisible = !isFirst
        isFirst = false
        status.text = ""
        status.visibility = View.INVISIBLE
        image_button_play.setImageResource(R.drawable.ic_play)
        visualizerView?.release()
        player?.stop()
        player?.reset()
        stopTimer()
    }

    override fun stopPlayingFromList() {
        listMenuItem?.isVisible = true
        saveMenuItem?.isVisible = false
        status.text = ""
        status.visibility = View.INVISIBLE
        image_button_play.setImageResource(R.drawable.ic_play)
        image_button_play.visibility = View.VISIBLE
        visualizerView.release()
        player?.stop()
        player?.reset()
        stopTimer()
        isFromList = false
    }

    override fun pauseRecording() {
        if (!isFinishing) {
            saveMenuItem?.isVisible = true
            listMenuItem?.isVisible = false

        }
        isRecording = false
        status.setText(R.string.paused)
        status.visibility = View.VISIBLE
        image_button_restart.visibility = View.VISIBLE
        image_button_play.visibility = View.VISIBLE
        image_button_record.setImageResource(R.drawable.ic_rec)
        image_button_play.setImageResource(R.drawable.ic_play)
        visualizerView.release()
        visualizerHandler?.stop()
        recorder?.pauseRecording()
        stopTimer()
    }

    override fun resumeRecording() {
        saveMenuItem?.isVisible = false
        listMenuItem?.isVisible = false
        fileRecord = File(filePath)
        isRecording = true
        status.setText(R.string.recording)
        status.visibility = View.VISIBLE
        image_button_restart.visibility = View.GONE
        image_button_play.visibility = View.GONE
        image_button_record.setImageResource(R.drawable.ic_pause)
        image_button_play.setImageResource(R.drawable.ic_play)
        visualizerHandler = VisualizerHandler()
        visualizerView.linkTo(visualizerHandler!!)
        if (recorder == null) {
            timer_recorder.text = "00:00:00"
            recorder = OmRecorder.wav(
                    PullTransport.Default(Util.getMic(
                            source,
                            channel,
                            sampleRate
                    ),
                            this@SoundRecorder
                    ),
                    fileRecord
            )
        }
        recorder?.resumeRecording()
        startTimer()
    }

    fun restartRecording(v: View?) {
        when {
            isRecording -> stopRecording()
            isPlaying() -> stopPlaying()
            else -> {
                visualizerHandler = VisualizerHandler()
                visualizerView?.linkTo(visualizerHandler!!)
                visualizerView?.release()
                visualizerHandler?.stop()
            }
        }
        saveMenuItem?.isVisible = false
        listMenuItem?.isVisible = true
        status.visibility = View.GONE
        image_button_restart.visibility = View.GONE
        image_button_play.visibility = View.GONE
        image_button_record.setImageResource(R.drawable.ic_rec)
        timer_recorder.text = "00:00:00"
        recorderSecondsElapsed = 0
        playerSecondsElapsed = 0
        fileRecord?.delete()
    }

    override fun stopRecording() {
        listMenuItem?.isVisible = false

        visualizerView?.release()
        visualizerHandler?.stop()
        recorderSecondsElapsed = 0
        recorder?.stopRecording()
        recorder = null
        stopTimer()
    }

    override fun startTimer() {
        stopTimer()
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                updateTimer()
            }
        }, 0, 1000)
    }

    override fun stopTimer() {
        timer?.cancel()
        timer?.purge()
        timer = null
    }

    private fun updateTimer() {
        runOnUiThread {
            if (isRecording) {
                recorderSecondsElapsed++
                timer_recorder.text = Util.formatSeconds(recorderSecondsElapsed)
            } else if (isPlaying()) {
                playerSecondsElapsed++
                timer_recorder.text = Util.formatSeconds(playerSecondsElapsed)
            }
        }
    }

    override fun selectAudio() {
        stopPlaying()
        fileRecord?.let { onSaving(it) }
        //setResult(Activity.RESULT_OK)
        //finish()
    }


    override fun onAudioChunkPulled(audioChunk: AudioChunk?) {
        val amplitude = if (isRecording) audioChunk?.maxAmplitude()?.toFloat() else 0f
        visualizerHandler?.onDataReceived(amplitude)
    }

    override fun onCompletion(p0: MediaPlayer?) {
        if(isFromList) {
            stopPlayingFromList()
        } else {
            stopPlaying()
        }
        setComponentVisibility(null, null, View.VISIBLE, View.VISIBLE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "fileRecord $requestCode")
        Log.d(TAG, "fileRecord ${this.requestCode}")
        if (requestCode == this.requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "fileRecord $fileRecord")
            } else {
                Log.d(TAG, data.toString() + " " + "not saved!")
            }
        }
    }

    private fun onSaving(file: File) {
        if (alertDialog == null) {
            stopRecording()
            alertDialog = alert {
                var mEditTextNewName: EditText? = null
                customView {
                    linearLayout {
                        mEditTextNewName = editText {
                            hint = file.name
                        }
                    }
                }
                positiveButton(Library.save) {
                    var newFileName = mEditTextNewName!!.text.toString()
                    soundRecorderPresenter.saveRecorder(file, if (newFileName != "") newFileName else file.name)
                    onStop()
                    onResume()
                    snackbar(
                            coordinator_dashboard,
                            if (newFileName != "") newFileName + " " + Library.saved else file.name + " " + Library.saved
                    )
                }
                negativeButton(Library.discard) {
                    soundRecorderPresenter.discardRecorder(file)
                    onStop()
                    onResume()
                }
            }.show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        fileRecord?.delete()
        moveTaskToBack(true)
        finish()
    }

    fun playRecorderFromList(absolutePath: String) {
        this.filePath = absolutePath
        image_button_play.performClick()
        this.isFromList = true
    }
}
