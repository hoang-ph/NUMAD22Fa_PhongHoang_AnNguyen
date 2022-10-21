package edu.northeastern.nowornever.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IFreeToGame {

    @GET("games")
    Call<List<Game>> getGames();
}
