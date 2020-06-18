package dika.wardani.api.response

import com.google.gson.annotations.SerializedName
import dika.wardani.api.response.model.MovieItem

data class NowPlayingMovieResponse(
    @SerializedName("results")
    var result: List<MovieItem>
)