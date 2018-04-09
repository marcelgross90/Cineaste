package de.cineaste.android.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.MenuItem;
import android.view.WindowManager;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.cineaste.android.util.Constants;
import de.cineaste.android.R;
import de.markusfisch.android.scalingimageview.widget.ScalingImageView;

public class PosterActivity extends AppCompatActivity {

    public final static String POSTER_PATH = "posterPath";

    private ScalingImageView poster;
    private String posterPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        posterPath = intent.getStringExtra(POSTER_PATH);

        poster = new ScalingImageView(this);
        setTransitionNameIfNecessary();
        poster.setImageResource(R.drawable.placeholder_poster);
        setContentView(poster);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        displayPoster();
    }

    @TargetApi(21)
    private void setTransitionNameIfNecessary(){
        poster.setTransitionName("poster");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return true;
    }

    private void displayPoster() {
        Picasso.with(this)
                .load(getPosterUrl(Constants.Companion.getPOSTER_URI_SMALL()))
                .error(R.drawable.placeholder_poster)
                .into(poster, new Callback() {
                    @Override
                    public void onSuccess() {
                        final Drawable placeHolder = poster.getDrawable();
                        setBackgroundColor(((BitmapDrawable) placeHolder).getBitmap());
                        Picasso.with(PosterActivity.this)
                                .load(getPosterUrl(Constants.Companion.getPOSTER_URI_ORIGINAL()))
                                .placeholder(placeHolder)
                                .into(poster, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        Snackbar.make(poster, R.string.poster_reloaded, Snackbar.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onError() {
                                        poster.setImageDrawable(placeHolder);
                                    }
                                });
                    }

                    @Override
                    public void onError() {
                        displayPoster();
                    }
                });
    }

    private void setBackgroundColor(Bitmap moviePoster) {
        Palette.PaletteAsyncListener paletteAsyncListener = new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@NonNull Palette palette) {
                Palette.Swatch swatch = palette.getDominantSwatch();
                if (swatch == null) {
                    return;
                }

                getWindow().getDecorView().setBackgroundColor(swatch.getRgb());
            }
        };

        Palette.from(moviePoster).generate(paletteAsyncListener);
    }


    private String getPosterUrl(String postUri) {
        return postUri
                    .replace("<posterName>", posterPath != null ?
                            posterPath : "/")
                    .replace("<API_KEY>", getString(R.string.movieKey));
    }
}
