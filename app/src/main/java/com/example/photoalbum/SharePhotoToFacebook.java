package com.example.photoalbum;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;

public class SharePhotoToFacebook extends AppCompatActivity {

    LoginButton loginButton;
    ShareButton shareButton;
    Button backButton;
    CallbackManager callbackManager;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_share_photo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String photoUriString = (String) getIntent().getSerializableExtra("photo");

        loginButton = findViewById(R.id.loginButton);
        shareButton = findViewById(R.id.shareButton);
        backButton = findViewById(R.id.backButton3);
        imageView = findViewById(R.id.imageView);

        imageView.setImageURI(Uri.parse(photoUriString));

        callbackManager = CallbackManager.Factory.create();

        backButton.setOnClickListener(l -> {
            finish();
        });

    }



    @Override
    protected  void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode,resultCode,data);


        Bitmap bitmap = drawableToBitmap(imageView.getDrawable());


        SharePhoto sharePhoto = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .build();

        SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
        .addPhoto(sharePhoto)
        .build();

        shareButton.setShareContent(sharePhotoContent);
    }


    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        }
        else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}