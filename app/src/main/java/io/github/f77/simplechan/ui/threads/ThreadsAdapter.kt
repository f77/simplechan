package io.github.f77.simplechan.ui.threads

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import io.github.f77.simplechan.R
import io.github.f77.simplechan.bloc_utils.EventsAwareInterface
import io.github.f77.simplechan.bloc_utils.event.Events
import io.github.f77.simplechan.entities.ThreadEntity
import io.github.f77.simplechan.entities.attachment.AttachmentEntityInterface
import io.github.f77.simplechan.entities.attachment.ImageAttachment
import io.github.f77.simplechan.events.threads.ThreadInformationClickedEvent
import io.github.f77.simplechan.ui.interfaces.HasGlideRequestManager
import java.text.SimpleDateFormat
import java.util.*

class ThreadsAdapter(
    private val eventsHandler: EventsAwareInterface,
    override var glideRequestManager: RequestManager
) :
    RecyclerView.Adapter<ThreadsAdapter.ThreadsViewHolder>(),
    HasGlideRequestManager,
    ListPreloader.PreloadModelProvider<String> {
    var dataset: List<ThreadEntity> = mutableListOf()

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class ThreadsViewHolder(itemView: View, eventsHandler: EventsAwareInterface) : RecyclerView.ViewHolder(itemView) {
        val circularProgressDrawable: CircularProgressDrawable
        val textViewPosition: TextView = itemView.findViewById(R.id.position)
        val textViewId: TextView = itemView.findViewById(R.id.id)
        val textViewTime: TextView = itemView.findViewById(R.id.time)
        val textViewComment: TextView = itemView.findViewById(R.id.comment)
        val cardViewPostImage: CardView = itemView.findViewById(R.id.imageCard)
        val imageViewPostImage: ImageView = itemView.findViewById(R.id.image)
        val layoutThreadInformation: ConstraintLayout = itemView.findViewById(R.id.threadInformation)
        val textViewPostsCount: TextView = itemView.findViewById(R.id.postsCount)
        val textViewFilesCount: TextView = itemView.findViewById(R.id.filesCount)

        init {
            // All view are gone by default.
            textViewPosition.apply { visibility = View.GONE }
            textViewId.apply { visibility = View.GONE }
            textViewTime.apply { visibility = View.GONE }
            textViewComment.apply { visibility = View.GONE }
            cardViewPostImage.apply { visibility = View.GONE }
            layoutThreadInformation.apply { visibility = View.GONE }
        }

        init {
            // Add callbacks.
            itemView.setOnClickListener(View.OnClickListener {
                eventsHandler.addEvent(Events.itemClicked(adapterPosition))
            })
            itemView.setOnLongClickListener(View.OnLongClickListener {
                eventsHandler.addEvent(Events.itemLongClicked(adapterPosition))
                true
            })
            layoutThreadInformation.setOnClickListener {
                eventsHandler.addEvent(ThreadInformationClickedEvent(adapterPosition))
            }
        }

        init {
            // @TODO: Maybe we just don't need a progress bar for images.
            // Init progress element.
            circularProgressDrawable = CircularProgressDrawable(imageViewPostImage.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
        }
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ThreadsViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_thread, parent, false)
        // set the view's size, margins, paddings and layout parameters
        // ...
        return ThreadsViewHolder(
            view,
            eventsHandler
        )
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ThreadsViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val thread = dataset[position]

        holder.textViewPosition.apply {
            text = "#" + (holder.adapterPosition + 1).toString()
            visibility = View.VISIBLE
        }

        holder.textViewId.apply {
            text = thread.opPost.id
            visibility = View.VISIBLE
        }

        thread.opPost.timestamp?.let {
            val formattedTime: String = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
                .format(Date(it.toLong() * 1000))

            holder.textViewTime.apply {
                text = formattedTime
                visibility = View.VISIBLE
            }
        }

        thread.opPost.comment?.let {
            //holder.textViewComment.text = it
            holder.textViewComment.apply {
                //text = it
                text = HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_COMPACT)
                visibility = View.VISIBLE
            }

            holder.textViewComment.post {
                println("LINES: " + holder.textViewComment.lineCount)
            }
        }

        thread.totalPostsCount?.let {
            holder.textViewPostsCount.apply {
                text = "POSTS: $it"
                visibility = View.VISIBLE
                holder.layoutThreadInformation.visibility = View.VISIBLE
            }
        }

        thread.totalFilesCount?.let {
            holder.textViewFilesCount.apply {
                text = "FILES: $it"
                visibility = View.VISIBLE
                holder.layoutThreadInformation.visibility = View.VISIBLE
            }
        }

        // Bind thumbnails of attachments.
        for (attachment in thread.opPost.attachments) {
            holder.imageViewPostImage.apply {
                getGlideBuilder(attachment.thumbnailUrl)!!.into(this)
                holder.cardViewPostImage.visibility = View.VISIBLE
            }

            break // now we have only 1 image per thread.
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataset.size

    override fun getPreloadItems(position: Int): MutableList<String> {
        return getUrlsOfAttachmentsThumbnails(dataset[position].opPost.attachments)
    }

    override fun getPreloadRequestBuilder(item: String): RequestBuilder<*>? = getGlideBuilder(item)

    /**
     * Get URLs of attachments per position.
     */
    private fun getUrlsOfAttachmentsThumbnails(attachments: List<AttachmentEntityInterface>): MutableList<String> {
        val imageAttachmentUrls: MutableList<String> = mutableListOf()

        for (attachment in attachments) {
            if (attachment !is ImageAttachment) {
                continue
            }

            imageAttachmentUrls.add(attachment.thumbnailUrl)
            break // now we have only 1 image per thread.
        }

        return imageAttachmentUrls
    }

    /**
     * Get Glide builder for image.
     */
    private fun getGlideBuilder(item: String): RequestBuilder<*>? {
        return glideRequestManager
            .load(item)
            .centerCrop()
    }
}
