package pt.ulisboa.tecnico.cmov.cmu_project.VolleyRequests;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import pt.ulisboa.tecnico.cmov.cmu_project.Crypto;

/**
 * Created by tiago on 18-May-18.
 */

public class JsonObjectRequestV extends JsonObjectRequest {

    public JsonObjectRequestV(int method, String url, JSONObject jsonRequest, Response.Listener
            <JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {

            JSONObject jsonResponse = null;

            if (Crypto.calculateHMAC(new String(response.data, "UTF-8")).equals(response.headers.get("INTEGRIDATE")))
                jsonResponse = new JSONObject(new String(response.data, "UTF-8"));


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
