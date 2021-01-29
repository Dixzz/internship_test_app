package org.evolution.test.async;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.widget.ImageView;

import com.caverock.androidsvg.SVG;

import org.evolution.test.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<String, Void, Drawable> {
    final ThreadLocal<ImageView> bmImage = new ThreadLocal<ImageView>();

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage.set(bmImage);
    }

    private static Bitmap blurImage(Context context, Bitmap inputBitmap) {
        float BLUR_RADIUS = 15f;

        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        tmpOut.destroy();
        tmpIn.destroy();
        theIntrinsic.destroy();
        rs.finish();

        return outputBitmap;
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    protected Drawable doInBackground(String... urls) {

        String urldisplay = urls[0];
        Drawable mIcon11 = null;
        try {
            URL url = new URL(urldisplay);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            SVG svg = SVG.getFromInputStream(inputStream);
            mIcon11 = new PictureDrawable(svg.renderToPicture());
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Drawable result) {
        if (result != null)
            bmImage.get().setImageBitmap(blurImage(bmImage.get().getContext(), drawableToBitmap(result)));
        else bmImage.get().setImageResource(R.drawable.rest);
    }
}