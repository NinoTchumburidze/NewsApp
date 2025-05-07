data class NewsResponse(
    val response: Content
)

data class Content(
    val status: String,
    val total: Int,
    val results: List<Article>
)
data class Category(
    val sectionId: String,  // This is used for API calls
    val sectionName: String      // This is sectionName, used for UI display
)

data class Article(
    val id: String,
    val sectionId: String,
    val webTitle: String, // Title of the article
    val webUrl: String, // URL of the article
    val fields: ArticleFields // The fields returned (e.g., headline, body, etc.)
)

data class ArticleFields(
    val headline: String?, // Title of the article
    val standfirst: String?, // Subtitle or introduction paragraph
    val body: String?, // Full body content
    val thumbnail: String? // Thumbnail URL
)

data class SingleNewsResponse(
    val response: SingleContent
)

data class SingleContent(
    val content: NewsItem
)


data class NewsItem(
    val id: String,
    val webTitle: String,
    val webUrl: String,
)
