package dika.wardani.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dika.wardani.databinding.ItemReviewBinding
import dika.wardani.domain.Review
import kotlinx.android.synthetic.main.item_review.view.*

class ReviewItemAdapter(
    private val context: Context
): RecyclerView.Adapter<ReviewItemAdapter.ViewHolder>() {
    var reviews: ArrayList<Review> = ArrayList()
    var onOpenReviewPageListener: OnOpenReviewPageListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ItemReviewBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = reviews[position]
        holder.bind(review)

        onOpenReviewPageListener?.run {
            holder.itemView.openPageBtn.setOnClickListener {
                onOpenReviewPage(review)
            }
        }
    }

    class ViewHolder(
        private val binding: ItemReviewBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(review: Review) {
            binding.run {
                authorTv.text = review.author
                contentTv.text = review.content
            }
        }
    }

    interface OnOpenReviewPageListener {
        fun onOpenReviewPage(review: Review)
    }
}