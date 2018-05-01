package pt.ulisboa.tecnico.cmov.cmu_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ImageManager {
    //http://www.codexpedia.com/android/android-download-and-save-image-internally/

    /**
     * @param context
     * @param b
     * @param url
     */
    public void saveImage(Context context, Bitmap b, String url) {
        FileOutputStream foStream;
        String imageName = this.buildImName(url);
        try {
            foStream = context.openFileOutput(imageName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, foStream);
            foStream.close();
        } catch (Exception e) {
            Log.d("saveImage", "Exception 2, Something went wrong!");
            e.printStackTrace();
        }
    }

    public Bitmap loadImageBitmap(Context context, String imageName) {
        Bitmap bitmap = null;
        FileInputStream fiStream;
        try {
            fiStream = context.openFileInput(buildImName(imageName));
            bitmap = BitmapFactory.decodeStream(fiStream);
            fiStream.close();
        } catch (Exception e) {
            Log.d("saveImage", "Exception 3, Something went wrong!");
            e.printStackTrace();
        }
        return bitmap;
    }

    public boolean checkIfFileExists(Context context, String url) {
        Bitmap bitmap = null;
        FileInputStream fiStream;
        String imageName = buildImName(url);
        try {
            fiStream = context.openFileInput(imageName);
            bitmap = BitmapFactory.decodeStream(fiStream);
            fiStream.close();
            return true;
        } catch (Exception e) {
            Log.d("saveImage", "Exception 3, Something went wrong!");
            e.printStackTrace();
        }
        return false;
    }

    public String buildImName(String url) {
        String[] splitUrl = url.split("/");
        return splitUrl[splitUrl.length - 1];
    }
}
