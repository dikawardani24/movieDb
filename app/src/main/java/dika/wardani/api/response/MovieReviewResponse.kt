package dika.wardani.api.response

import com.google.gson.annotations.SerializedName
import dika.wardani.api.response.model.ReviewItem

data class MovieReviewResponse(
    @SerializedName("id")
    val id : Int,
    @SerializedName("page")
    val page : Int,
    @SerializedName("results")
    val reviews : List<ReviewItem>,
    @SerializedName("total_pages")
    val totalPages : Int,
    @SerializedName("total_results")
    val totalResults : Int
)