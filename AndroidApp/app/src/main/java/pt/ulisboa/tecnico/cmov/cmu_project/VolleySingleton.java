package pt.ulisboa.tecnico.cmov.cmu_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;


public class VolleySingleton {
    /**
     * Volley singleton
     */
    private static VolleySingleton mInstance;
    private RequestQueue requestQueue;
    private Context context;


    private VolleySingleton(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }

        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    /**
     * Request Image and setIt in UI
     *
     * @param url
     * @param imView
     */
    public void requestImage(final String url, final ImageView imView, final Context context) {
        final ImageManager imageManager = new ImageManager();

        if (!imageManager.checkIfFileExists(context, url)) {

            // Initialize a new ImageRequest
            ImageRequest imageRequest = new ImageRequest(url, // Image URL
                    new Response.Listener<Bitmap>() { // Bitmap listener
                        @Override
                        public void onResponse(Bitmap response) {
                            // Do something with response
                            imView.setImageBitmap(response);
                            imageManager.saveImage(context, response, url);
                        }
                    },

                    0, // Image width
                    0, // Image height
                    ImageView.ScaleType.CENTER_CROP, // Image scale type
                    Bitmap.Config.RGB_565, //Image decode configuration

                    new Response.ErrorListener() { // Error listener
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Do something with error response
                            error.printStackTrace();
                        }
                    }
            );

            // Add ImageRequest to the RequestQueue
            requestQueue.add(imageRequest);
        } else {

            Bitmap bitmap = imageManager.loadImageBitmap(context, url);
            imView.setImageBitmap(bitmap);
            Toast.makeText(context,"should put img",Toast.LENGTH_SHORT).show();
        }

    }

}


