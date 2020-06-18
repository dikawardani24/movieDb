package dika.wardani.api.response

import com.google.gson.annotations.SerializedName
import dika.wardani.api.response.model.MovieItem

data class PopularMovieResponse(
    @SerializedName("page")
    var page: Int,
    @SerializedName("total_results")
    var totalResults: Int,
    @SerializedName("372")
    var totalPages: Int,
    @SerializedName("results")
    var result: List<MovieItem>
)