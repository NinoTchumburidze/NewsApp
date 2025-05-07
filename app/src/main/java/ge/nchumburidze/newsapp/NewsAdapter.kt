package ge.nchumburidze.newsapp

import Article
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import ge.nchumburidze.newsapp.R
import ge.nchumburidze.newsapp.databinding.NewsItemBinding


class NewsAdapter(
    private var articles: List<Article>,
    private val onItemClick: (Article) -> Unit
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(private val binding: NewsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            binding.newsTitle.text = article.webTitle

            binding.newsDescription.text = HtmlCompat.fromHtml(
                article.fields.standfirst ?: "",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            ).toString()

            val imageUrl = article.fields.thumbnail
            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(binding.newsImage.context)
                    .load(imageUrl)
                    .into(binding.newsImage)
            } else {
                binding.newsImage.setImageDrawable(null) // Clear image if null
            }

            binding.root.setOnClickListener {
                onItemClick(article)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int = articles.size

    fun setArticles(newArticles: List<Article>) {
        articles = newArticles
        notifyDataSetChanged()
    }
}