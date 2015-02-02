package com.yavuzcalisir.helpers;

import android.app.DownloadManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyLog;

/**
 * Created by yavuzcalisir on 02/02/15.
 */
public class VolleyHelper {

    private static void logToCurlRequest(Request<?> request) {
        StringBuilder builder = new StringBuilder();
        builder.append("curl request: curl ");
        builder.append("-X \"");
        switch (request.getMethod()) {
            case Request.Method.POST:
                builder.append("POST");
                break;
            case Request.Method.GET:
                builder.append("GET");
                break;
            case Request.Method.PUT:
                builder.append("PUT");
                break;
            case Request.Method.DELETE:
                builder.append("DELETE");
                break;
        }
        builder.append("\"");

        try {
            if (request.getBody() != null) {
                builder.append(" -D ");
                String data = new String(request.getBody());
                data = data.replaceAll("\"", "\\\"");
                builder.append("\"");
                builder.append(data);
                builder.append("\"");
            }
            for (String key : request.getHeaders().keySet()) {
                builder.append(" -H '");
                builder.append(key);
                builder.append(": ");
                builder.append(request.getHeaders().get(key));
                builder.append("'");
            }
            builder.append(" \"");
            builder.append(request.getUrl());
            builder.append("\"");

            VolleyLog.v(builder.toString());
        } catch (AuthFailureError e) {
            VolleyLog.wtf("Unable to get body of response or headers for curl logging");
        }
    }
}