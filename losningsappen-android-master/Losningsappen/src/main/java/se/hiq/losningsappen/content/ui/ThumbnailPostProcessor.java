package se.hiq.losningsappen.content.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.nostra13.universalimageloader.core.process.BitmapProcessor;

import se.hiq.losningsappen.R;

/**
 * Created by petterstenberg on 2014-09-09.
 * //TODO Add class description
 */
public class ThumbnailPostProcessor implements BitmapProcessor {

    private final Context context;

    public ThumbnailPostProcessor(Context context) {
        this.context = context;
    }

    @Override
    public Bitmap process(Bitmap bitmap) {

        Bitmap overlay = BitmapFactory.decodeResource(context.getResources(), R.drawable.thumbnail_overlay);
        int diameter = (int) (bitmap.getHeight() * 0.65f);
        Bitmap scaledOverlay = Bitmap.createScaledBitmap(overlay, diameter, diameter, true);
        overlay.recycle();

        Bitmap overlayedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(overlayedBitmap);
        canvas.drawBitmap(bitmap, new Matrix(), null);
        canvas.drawBitmap(scaledOverlay, canvas.getWidth() / 2 - diameter / 2, canvas.getHeight() / 2 - diameter / 2, null);
        scaledOverlay.recycle();

        return overlayedBitmap;
    }
}
