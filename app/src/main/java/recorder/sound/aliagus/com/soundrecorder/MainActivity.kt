package recorder.sound.aliagus.com.soundrecorder

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import recorder.sound.aliagus.com.soundrecorder.lib.Library
import recorder.sound.aliagus.com.soundrecorder.model.AudioChannel
import recorder.sound.aliagus.com.soundrecorder.model.AudioRecorder
import recorder.sound.aliagus.com.soundrecorder.model.AudioSampleRate
import recorder.sound.aliagus.com.soundrecorder.model.AudioSource
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), MainActivityContract.View {

    private val mainActivityPresenter: MainActivityPresenter by lazy {
        MainActivityPresenter(this)
    }
    private val REQUEST_PERMISSION_ALL = 101
    val requestCode = 102
    val path = File (Environment.getExternalStorageDirectory().path + "/" + Library.baseFile)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hideNavigationBar()
    }

    override fun onResume() {
        super.onResume()
        chekPermission()
    }

    override fun chekPermission() {
        val permissionStorageReadGranted = ContextCompat
                .checkSelfPermission(applicationContext, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        val permissionStorageWriteGranted = ContextCompat
                .checkSelfPermission(applicationContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        val permissionRecordAudioGranted = ContextCompat
                .checkSelfPermission(applicationContext, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED

        if (!permissionRecordAudioGranted || !permissionStorageReadGranted || !permissionStorageWriteGranted) {
            makeRequest()
        } else {
            initAudioRecorder()
        }
    }

    override fun makeRequest() {
        ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.RECORD_AUDIO
                ), REQUEST_PERMISSION_ALL
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION_ALL -> {
                var isPermitted = false
                grantResults.forEach { granted ->
                    if (granted == PackageManager.PERMISSION_GRANTED) {
                        isPermitted = true
                    }
                }
                if (isPermitted) {
                    initAudioRecorder()
                } else {
                    moveTaskToBack(true)
                    finish()
                }
            }
        }
    }

    override fun initAudioRecorder() {
        try {
            path.mkdirs()
            var dateNow = Calendar.getInstance().time
            val simpleDateFormat = SimpleDateFormat("dd_MM_yyyy_HH_mm_ss")
            val date = simpleDateFormat.format(dateNow)
            val filePath = path.absolutePath+ "/" + date + ".mp3"
            AudioRecorder.with(this@MainActivity)
                    .setFilePath(filePath)
                    .setColor(android.R.color.holo_red_dark)
                    .setRequestCode(requestCode)
                    .setSource(AudioSource.MIC)
                    .setChannel(AudioChannel.STEREO)
                    .setSampleRate(AudioSampleRate.HZ_48000)
                    .setAutoStart(false)
                    .setKeepDisplayOn(true)
                    .record()
        } catch (e: Exception) { }
    }

    private fun hideNavigationBar() {
        var flags: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            (View.SYSTEM_UI_FLAG_LOW_PROFILE
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        } else {
            (View.SYSTEM_UI_FLAG_LOW_PROFILE
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        }
        window.decorView.systemUiVisibility = flags
    }
}