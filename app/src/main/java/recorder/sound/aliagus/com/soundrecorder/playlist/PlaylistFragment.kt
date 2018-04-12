package recorder.sound.aliagus.com.soundrecorder.playlist

import android.app.Dialog
import android.os.Environment
import android.os.Handler
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.DisplayMetrics
import android.view.View
import kotlinx.android.synthetic.main.fragment_playlist.view.*
import org.jetbrains.anko.design.longSnackbar
import recorder.sound.aliagus.com.soundrecorder.R
import recorder.sound.aliagus.com.soundrecorder.adapter.AdapterPlaylist
import recorder.sound.aliagus.com.soundrecorder.adapter.RecyclerItemTouchHelper
import recorder.sound.aliagus.com.soundrecorder.lib.Library
import recorder.sound.aliagus.com.soundrecorder.model.Recorder
import recorder.sound.aliagus.com.soundrecorder.soundrecorder.SoundRecorder
import recorder.sound.aliagus.com.soundrecorder.utils.PaginationScrollListener
import java.io.File
import java.util.*


/**
 * Created by ali on 01/03/18.
 */
class PlaylistFragment : BottomSheetDialogFragment(),
        PlaylistFragmentContract.View,
        AdapterPlaylist.ListenerRecorder,
        RecyclerItemTouchHelper.ListenerRecyclerItemTouchHelper {

    private val presenter: PlaylistFragmentPresenter by lazy {
        PlaylistFragmentPresenter(this)
    }
    private val TAG = javaClass.simpleName
    private lateinit var viewRoot: View
    private var listRecorder = ArrayList<Recorder>()
    private var listTypes = ArrayList<Int>()
    var isLoading = false
    var currentPage = 0
    var isLastPage = false
    private var totalPage: Int = 0
    var timer: Timer? = null


    override fun setupDialog(dialog: Dialog?, style: Int) {
        viewRoot = View.inflate(context, R.layout.fragment_playlist, null)
        dialog?.setContentView(viewRoot)
        setLayout()
        presenter.loadData()
    }

    override fun setLayout() {
        val layoutParams = (viewRoot.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.setMargins(0, 0, 0, 0)
        val parent = viewRoot.parent as View
        parent.fitsSystemWindows = true
        val bottomSheetBehavior = BottomSheetBehavior.from(parent)
        viewRoot.measure(0, 0)
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels - 100
        bottomSheetBehavior.peekHeight = screenHeight
        layoutParams.height = screenHeight
        parent.layoutParams = layoutParams
    }

    private lateinit var adapterPlaylist: AdapterPlaylist

    private lateinit var listFiles: Array<File>

    override fun loadDataToAdapter(listRecorder: ArrayList<Recorder>, listTypes: ArrayList<Int>, fileArrays: Array<File>) {
        this.listRecorder = listRecorder
        this.listTypes = listTypes
        this.listFiles = fileArrays
        adapterPlaylist = AdapterPlaylist(this)
        val linearLayoutManager = LinearLayoutManager(context)
        val itemAnimator = DefaultItemAnimator()
        val dividerItemDecoration = DividerItemDecoration(context, linearLayoutManager.orientation)
        viewRoot.recycler_playlist.layoutManager = linearLayoutManager
        viewRoot.recycler_playlist.addItemDecoration(dividerItemDecoration)
        viewRoot.recycler_playlist.itemAnimator = itemAnimator
        viewRoot.recycler_playlist.adapter = adapterPlaylist
        viewRoot.recycler_playlist.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager){
            override fun loadMoreItems() {
                isLoading = true
                currentPage += 1
                Handler().postDelayed({ loadNextPage() }, 1000 * 5)
            }

            override fun getTotalPageCount(): Int {
                totalPage = listRecorder.size / 20
                return totalPage
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }
        })
        Handler().postDelayed({ loadFirstPage() }, 1000)

        val itemTouchHelper = RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(viewRoot.recycler_playlist)
    }

    private fun loadFirstPage() {
        var newListRecorder = createListRecorder(adapterPlaylist.itemCount)
        adapterPlaylist.addAllList(newListRecorder)
        if (currentPage < totalPage) {
            adapterPlaylist.addLoadingFooter()
        } else {
            isLastPage = true
        }
    }

    private fun createListRecorder(itemCount: Int): List<Recorder> {
        val newListRecorder = ArrayList<Recorder>()
        if (listRecorder.size > 20) {
            for (i in 0..19) {
                if (((itemCount - 1) + i) < listRecorder.size) {
                    val record = listRecorder[if (itemCount == 0) itemCount + i else ((itemCount - 1) + i)]
                    newListRecorder.add(record)
                }
            }
        } else {
            for (i in 0..(listRecorder.size - 1)) {
                if (((itemCount - 1) + i) < listRecorder.size) {
                    val record = listRecorder[if (itemCount == 0) itemCount + i else ((itemCount - 1) + i)]
                    newListRecorder.add(record)
                }
            }
        }
        return newListRecorder
    }

    private fun loadNextPage() {
        var newListRecorder = createListRecorder(adapterPlaylist.itemCount)
        adapterPlaylist.removeLoadingFooter()
        isLoading = false
        adapterPlaylist.addAllList(newListRecorder)
        if (currentPage != totalPage && newListRecorder.isNotEmpty()) {
            adapterPlaylist.addLoadingFooter()
        } else {
            isLastPage = true
        }
    }

    override fun onClick(view: View?) {
        val recorder = view?.tag as Recorder
        val path = File (Environment.getExternalStorageDirectory().path + "/" + Library.baseFile + "/" + recorder.name)
        ((activity) as SoundRecorder).playRecorderFromList(path.absolutePath)
        dismissAllowingStateLoss()
        /*val mediaPlayer = MediaPlayer()

        mediaPlayer.setDataSource(path.absolutePath)
        mediaPlayer.prepare()
        mediaPlayer.start()
        startTimer()*/
    }

    private fun startTimer() {
        stopTimer()
        timer = Timer()
        timer?.scheduleAtFixedRate(object: TimerTask(){
            override fun run() {
                updateTimer()
            }

        }, 0, 1000)
    }

    private fun stopTimer() {
        timer?.cancel()
        timer?.purge()
        timer = null
    }

    private fun updateTimer() {
    }

    override fun onSwipeItem(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int) {
        if (viewHolder is AdapterPlaylist.ViewHolderContent) {
            val recorder = listRecorder[position]
            adapterPlaylist.remove(position)
            longSnackbar(
                    viewRoot.coordinatorLayout_playlist,
                    recorder.name + " removed from list",
                    "Undo"
            ) {
                adapterPlaylist.restore(recorder, position)
            }
        }
    }
}