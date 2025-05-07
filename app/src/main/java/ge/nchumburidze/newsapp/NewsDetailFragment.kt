import android.os.Bundle
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import ge.nchumburidze.newsapp.R
import ge.nchumburidze.newsapp.databinding.ArticleFragmentBinding

class NewsDetailFragment : Fragment(R.layout.article_fragment) {

    private lateinit var binding: ArticleFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = ArticleFragmentBinding.bind(view)

        val title = arguments?.getString("title")
        val body = arguments?.getString("body")
        val thumbnail = arguments?.getString("thumbnail")

        binding.titleOfArticle.text = title
        binding.bodyOfArticle.text = HtmlCompat.fromHtml(body ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY)

        if (!thumbnail.isNullOrEmpty()) {
            Glide.with(requireContext())
                .load(thumbnail)
                .into(binding.newsImage1)
        }

        binding.goBack.setOnClickListener {
            parentFragmentManager.popBackStack() // Navigates back to the previous fragment
        }

    }
}
