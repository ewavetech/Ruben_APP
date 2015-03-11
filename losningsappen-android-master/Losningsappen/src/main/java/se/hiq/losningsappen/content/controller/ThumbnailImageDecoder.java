package se.hiq.losningsappen.content.controller;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.decode.ImageDecoder;
import com.nostra13.universalimageloader.core.decode.ImageDecodingInfo;

import java.io.IOException;

import wseemann.media.FFmpegMediaMetadataRetriever;

/**
 * Created by petterstenberg on 2014-09-08.
 * //TODO Add class description
 */

public class ThumbnailImageDecoder implements ImageDecoder {

    public static final int THUMBNAIL_WIDTH = 640;

    @Override
    public Bitmap decode(ImageDecodingInfo imageDecodingInfo) throws IOException {

        Bitmap tempBitmap;
        Bitmap lightweightBitmap = null;
        Bitmap smallerBitmap = null;

        FFmpegMediaMetadataRetriever metadataRetriever = new FFmpegMediaMetadataRetriever();

        try {
            metadataRetriever.setDataSource(imageDecodingInfo.getOriginalImageUri());

            tempBitmap = metadataRetriever.getFrameAtTime(1000000,
                    FFmpegMediaMetadataRetriever.OPTION_CLOSEST);
            lightweightBitmap = tempBitmap.copy(Bitmap.Config.RGB_565, false);
            tempBitmap.recycle();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            metadataRetriever.release();
            metadataRetriever = null;
        }

        if (lightweightBitmap != null) {
            double widthHeightRatio = (double) lightweightBitmap.getHeight() / (double) lightweightBitmap.getWidth();

            smallerBitmap = Bitmap.createScaledBitmap(lightweightBitmap, THUMBNAIL_WIDTH, (int) (THUMBNAIL_WIDTH * widthHeightRatio), false);
            lightweightBitmap.recycle();
        }

        return smallerBitmap;
    }
}
