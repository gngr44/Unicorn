package com.audio.unicorn.media;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.net.Uri;

public class AlbumArtProvider {

    public Bitmap getAlbumArtwork(Context context, long albumId) throws FileNotFoundException {
        final Uri artworkUri = Uri.parse("content://media/external/audio/albumart");
        final Uri uri = ContentUris.withAppendedId(artworkUri, albumId);
        InputStream in = context.getContentResolver().openInputStream(uri);
        Options options = new Options();
        return BitmapFactory.decodeStream(in, null, options);
    }

    public Bitmap getCircularAlbumArtwork(Context context, long albumId) throws FileNotFoundException {
        Bitmap bitmap = getAlbumArtwork(context, albumId);
        if (bitmap == null) {
            return null;
        }
        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_4444);
        BitmapShader shader = new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP);

        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setAntiAlias(true);

        Canvas canvas = new Canvas(circleBitmap);

        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        return circleBitmap;
    }
}
