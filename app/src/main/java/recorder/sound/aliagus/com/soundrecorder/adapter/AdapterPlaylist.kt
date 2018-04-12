package recorder.sound.aliagus.com.soundrecorder.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.costom_content.view.*
import recorder.sound.aliagus.com.soundrecorder.R
import recorder.sound.aliagus.com.soundrecorder.model.Recorder
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by ali on 01/03/18.
 */
class AdapterPlaylist(private var listener: ListenerRecorder) : RecyclerView.Adapter<AdapterPlaylist.ViewHolder>() {

    var isLoadingAdded = false
    var listRecorder = ArrayList<Recorder>()
    private val TAG = javaClass.simpleName

    companion object {
        const val RECORDER_CONTENT = 1
        const val LOADING_CONTENT = 2
        const val CONTENT = 1
        const val LOADING = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            RECORDER_CONTENT -> {
                ViewHolderContent(layoutInflater.inflate(R.layout.costom_content, null), listener)
            }
            else -> {
                ViewHolderLoading(layoutInflater.inflate(R.layout.costum_loading, null))
            }
        }
    }

    override fun getItemCount(): Int {
        return listRecorder.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == listRecorder.size - 1 && isLoadingAdded) LOADING else CONTENT
    }

    private lateinit var viewHolderContent: ViewHolderContent

    private lateinit var viewHolderLoading: ViewHolderLoading

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        when (viewType) {
            RECORDER_CONTENT -> {
                viewHolderContent = holder as ViewHolderContent
                val recorder = listRecorder[position]
                viewHolderContent.bind(recorder)
                viewHolderContent.itemView.tag = recorder
            }

            LOADING_CONTENT -> {
                //viewHolderLoading = holder as ViewHolderLoading
            }
        }
    }

    open class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    class ViewHolderContent(itemView: View?, private var listener: ListenerRecorder) : ViewHolder(itemView), View.OnClickListener {
        init {
            val recorder = itemView?.tag
            itemView?.tag = recorder
            itemView?.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            Log.d("testing", "view1ww ${view?.tag}")
            listener.onClick(view)
        }

        @SuppressLint("SimpleDateFormat")
        fun bind(recorder: Recorder) {
            val date = recorder.date?.let { Date(it) }
            val simpleDateFormat = SimpleDateFormat("dd/MM/yy")
            val decimalFormat = DecimalFormat("#.##")
            var size = when {
                recorder.size!! > 1000000 -> {
                    decimalFormat.format(recorder.size!! / 1000000.00).toString() + " Mb"
                }
                recorder.size!! > 1000 -> {
                    decimalFormat.format(recorder.size!! / 1000.00).toString() + " Kb"
                }
                else -> {
                    recorder.size.toString() + " bytes"
                }
            }
            itemView.title_content.text = recorder.name
            itemView.date_content.text = simpleDateFormat.format(date)
            itemView.size_content.text = size
        }
    }

    class ViewHolderLoading(itemView: View?) : ViewHolder(itemView)

    interface ListenerRecorder {
        fun onClick(view: View?)
    }

    fun remove(adapterPosition: Int) {
        listRecorder.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }

    fun restore(recorder: Recorder, position: Int) {
        listRecorder.add(position, recorder)
        notifyItemInserted(position)
    }

    fun add(record: Recorder) {
        listRecorder.add(record)
        notifyItemInserted(listRecorder.size - 1)
    }

    fun addAllList(newListRecorder: List<Recorder>) {
        newListRecorder.forEach {
            add(it)
        }
    }

    private fun remove(record: Recorder) {
        val position = listRecorder.indexOf(record)
        if (position > -1) {
            listRecorder.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        isLoadingAdded = false
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    private fun getItem(position: Int): Recorder {
        return listRecorder[position]
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(Recorder(null, null, null))
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position = listRecorder.size - 1
        listRecorder.removeAt(position)
        notifyItemRemoved(position)
    }
}