package edu.northeastern.nowornever.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IFreeToGame {

    @GET("games")
    Call<List<Game>> getGames(@Query("sort-by") String sortBy);

    @GET("games")
    Call<List<Game>> getGamesByPlatform(@Query("platform") String platform,
                              @Query("sort-by") String sortBy);

    @GET("games")
    Call<List<Game>> getGamesByGenre(@Query("category") String genre,
                                        @Query("sort-by") String sortBy);

    @GET("games")
    Call<List<Game>> getGamesByPlatformAndGenre(@Query("platform") String platform,
                              @Query("category") String genre,
                              @Query("sort-by") String sortBy);
}
