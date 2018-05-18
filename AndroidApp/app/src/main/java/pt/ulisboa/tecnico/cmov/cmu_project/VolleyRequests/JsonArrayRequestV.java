package pt.ulisboa.tecnico.cmov.cmu_project.VolleyRequests;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;

import pt.ulisboa.tecnico.cmov.cmu_project.Crypto;

/**
 * Created by tiago on 18-May-18.
 */

public class JsonArrayRequestV extends JsonArrayRequest {

    public JsonArrayRequestV(int method, String url, JSONArray jsonRequest, Response.Listener
            <JSONArray> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {

            JSONArray jsonResponse = null;

            if (Crypto.calculateHMAC(new String(response.data, "UTF-8")).equals(response.headers.get("INTEGRIDATE")))
                jsonResponse = new JSONArray(new String(response.data, "UTF-8"));


            return Response.success(jsonResponse,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

}
