package se.hiq.losningsappen.content.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.nostra13.universalimageloader.core.assist.ContentLengthInputStream;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import wseemann.media.FFmpegMediaMetadataRetriever;

/**
 * Created by petterstenberg on 2014-09-08.
 * //TODO Add class description
 */
public class ThumbnailDownloader extends BaseImageDownloader {

    public ThumbnailDownloader(Context context) {
        super(context);
    }

    public ThumbnailDownloader(Context context, int connectTimeout, int readTimeout) {
        super(context, connectTimeout, readTimeout);
    }

    @Override
    protected InputStream getStreamFromNetwork(String imageUri, Object extra) throws IOException {

        ByteArrayInputStream bs = null;

        Bitmap downloadedBitmap = null;
        FFmpegMediaMetadataRetriever metadataRetriever = new FFmpegMediaMetadataRetriever();

        try {
            metadataRetriever.setDataSource(imageUri);

            downloadedBitmap = metadataRetriever.getFrameAtTime(1000000,
                    FFmpegMediaMetadataRetriever.OPTION_CLOSEST);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            ThumbnailPagerAdapter.ignoreList.add(imageUri);
        } finally {
            metadataRetriever.release();
            metadataRetriever = null;
        }

        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            downloadedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
            byte[] bitmapdata = bos.toByteArray();
            bs = new ByteArrayInputStream(bitmapdata);
        } finally {
            assert bos != null;
            bos.close();
        }

        return new ContentLengthInputStream(bs, bs.available());
    }

    private boolean isVideoUri(Uri uri) {
        String mimeType = context.getContentResolver().getType(uri);

        if (mimeType == null) {
            return false;
        }

        return mimeType.startsWith("video/");
    }
}
