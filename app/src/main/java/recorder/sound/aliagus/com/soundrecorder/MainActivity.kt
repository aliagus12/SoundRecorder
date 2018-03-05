package recorder.sound.aliagus.com.soundrecorder

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import org.jetbrains.anko.intentFor
import recorder.sound.aliagus.com.soundrecorder.soundrecorder.SoundRecorder

class MainActivity : AppCompatActivity(), MainActivityContract.View {

    private val mainActivityPresenter: MainActivityPresenter by lazy {
        MainActivityPresenter(this)
    }
    private val REQUEST_PERMISSION_ALL = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hideNavigationBar()
        chekPermission()
    }

    override fun onResume() {
        super.onResume()
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
            startActivity(intentFor<SoundRecorder>())
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
                    startActivity(intentFor<SoundRecorder>())
                } else {
                    moveTaskToBack(true)
                    finish()
                }
            }
        }
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