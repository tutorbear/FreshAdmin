package com.example.freshadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.freshadmin.databinding.ActivityVerifyBoostBinding;
import com.example.freshadmin.utils.FullScreenHelper;
import com.parse.ParseObject;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;

public class VerifyBoost extends AppCompatActivity {
    ActivityVerifyBoostBinding binding;

    ParseObject obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityVerifyBoostBinding.inflate(getLayoutInflater())).getRoot());

        FullScreenHelper fullScreenHelper = new FullScreenHelper(this, binding.button22, binding.button23);

        binding.youtubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                fullScreenHelper.enterFullScreen();
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                fullScreenHelper.exitFullScreen();
            }
        });
        obj = getIntent().getParcelableExtra("obj");
        setYoutubeVideo("5tcVKUzrUjQ");
//        setYoutubeVideo(obj.getString("videoId"));
    }

    void setYoutubeVideo(String youtubeLink) {
        binding.youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.cueVideo(youtubeLink, 0);
            }
        });
    }
}