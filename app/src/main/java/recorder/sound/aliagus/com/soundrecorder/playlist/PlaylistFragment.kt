package recorder.sound.aliagus.com.soundrecorder.playlist

import android.app.Dialog
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.util.DisplayMetrics
import android.view.View
import recorder.sound.aliagus.com.soundrecorder.R

/**
 * Created by ali on 01/03/18.
 */
class PlaylistFragment: BottomSheetDialogFragment(), PlaylistFragmentContract.View {

    private val presenter: PlaylistFragmentPresenter by lazy {
        PlaylistFragmentPresenter(this)
    }

    lateinit var viewRoot: View

    override fun setupDialog(dialog: Dialog?, style: Int) {
        viewRoot = View.inflate(context, R.layout.fragment_playlist, null)
        dialog?.setContentView(viewRoot)
        setLayout()
        presenter.loadData()
    }

    override fun setLayout() {
        val layoutParams = (viewRoot!!.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.setMargins(0, 0, 0, 0)
        val parent = viewRoot.parent as View
        parent.fitsSystemWindows = true
        val bottomSheetBehavior = BottomSheetBehavior.from(parent)
        viewRoot.measure(0, 0)
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels
        bottomSheetBehavior.peekHeight = screenHeight
        layoutParams.height = screenHeight
        parent.layoutParams = layoutParams
    }
}