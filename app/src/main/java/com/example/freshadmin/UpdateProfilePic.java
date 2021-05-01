package com.example.freshadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.freshadmin.databinding.ActivityUpdateProfilePicBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class UpdateProfilePic extends AppCompatActivity {
    ActivityUpdateProfilePicBinding binding;
    byte[] bytesProPic, bytesSmallProPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProfilePicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }

    public void uploadProPic(View view) {
        ImagePicker.Companion.with(this)
                .galleryOnly()
                .crop(1f, 1f)//Crop image(Optional), Check Customization for more option
                .compress(512)//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "Called", Toast.LENGTH_SHORT).show();

        if(resultCode == RESULT_OK && data != null){

            String path = ImagePicker.Companion.getFilePath(data);
            Glide.with(this)
                    .asBitmap()
                    .load(path)
                    .fitCenter()
                    .transform(new CircleCrop())
                    .into(new CustomTarget<Bitmap>(650, 650) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            binding.proPic.setImageBitmap(resource);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            resource.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            bytesProPic = stream.toByteArray();
                            Toast.makeText(UpdateProfilePic.this, "Hi", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });

            Glide.with(this)
                    .asBitmap()
                    .load(path)
                    .fitCenter()
                    .transform(new CircleCrop())
                    .into(new CustomTarget<Bitmap>(150, 150) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            resource.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            bytesSmallProPic = stream.toByteArray();
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }
    }

    public void submitPic(View view) {
        HashMap<String,Object> params = new HashMap<>();
        params.put("phone",binding.phone.getText().toString().trim());
        params.put("proPic",bytesProPic);
        params.put("proPicSmall",bytesSmallProPic);
        ParseCloud.callFunctionInBackground("UpdateProPic", params, (object, e) -> {
            if (e==null){
                Toast.makeText(UpdateProfilePic.this, "Done", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(UpdateProfilePic.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}