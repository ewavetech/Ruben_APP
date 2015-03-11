package se.hiq.losningsappen.chaptertest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.PictureDrawable;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.nostra13.universalimageloader.core.assist.ContentLengthInputStream;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by petterstenberg on 2014-09-09.
 * //TODO Add class description
 */

public class SVGLoader extends BaseImageDownloader {

    public static final float paddingFactor = 0.9f;
    private final Context context;

    public SVGLoader(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected InputStream getStreamFromOtherSource(String imageUri, Object extra) throws IOException {

        SVG svg;
        ByteArrayInputStream bs = null;

        try {
            svg = SVG.getFromAsset(context.getAssets(), imageUri);
        } catch (SVGParseException e) {
            e.printStackTrace();
            throw new IOException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        assert svg != null;
        PictureDrawable drawable = new PictureDrawable(svg.renderToPicture());
        float heightWidthRatio = drawable.getIntrinsicHeight() / (float) drawable.getIntrinsicWidth();
        float screenWidth = context.getResources().getDisplayMetrics().widthPixels;

        int width = (int) (screenWidth * paddingFactor);
        width = (width % 2 == 0) ? width : width + 1;
        int height = (int) (screenWidth * heightWidthRatio * paddingFactor);
        height = (width % 2 == 0) ? height : height + 1;

        Rect targetSize = new Rect(0, 0, width, height);

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawPicture(drawable.getPicture(), targetSize);

        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] bitmapdata = bos.toByteArray();
            bs = new ByteArrayInputStream(bitmapdata);
        } finally {
            assert bos != null;
            bos.close();
        }

        return new ContentLengthInputStream(bs, bs.available());
    }
}
