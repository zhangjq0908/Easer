/*
 * Copyright (c) 2016 - 2019 Rui Zhao <renyuneyun@gmail.com>
 *
 * This file is part of Easer.
 *
 * Easer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Easer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Easer.  If not, see <http://www.gnu.org/licenses/>.
 */

package ryey.easer.skills.operation.http_request;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ryey.easer.skills.operation.OperationLoader;

public class HttpRequestLoader extends OperationLoader<HttpRequestOperationData> {
    public HttpRequestLoader(Context context) {
        super(context);
    }

    @Override
    public void _load(@NonNull HttpRequestOperationData data, @NonNull OnResultCallback callback) {
        HttpTask task = new HttpTask(callback);
        task.execute(data);
    }

    private static class HttpTask extends AsyncTask<HttpRequestOperationData, Void, Boolean> {

        private final OnResultCallback callback;

        HttpTask(OnResultCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(HttpRequestOperationData... httpRequestOperationData) {
            final HttpRequestOperationData data = httpRequestOperationData[0];

            HttpURLConnection urlConnection = null;

            try {
                final URL url = new URL(data.url.raw);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod(data.requestMethod.name());

                // set request header
                final String[] headerLines = data.requestHeader.raw.split("\r?\n");
                for (String headerLine : headerLines) {
                    if (headerLine.contains(":")) {
                        final String[] parts = headerLine.split(":", 2);
                        urlConnection.addRequestProperty(parts[0].trim(), parts[1].trim());
                    }
                }

                switch (data.requestMethod) {
                    case GET:
                        break;

                    case POST:
                        // set header for POST request
                        urlConnection.setDoOutput(true);
                        urlConnection.addRequestProperty("Content-Type", data.contentType.raw);
                        urlConnection.setFixedLengthStreamingMode(data.postData.raw.length());

                        // send POST data
                        try (final DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream())) {
                            out.writeBytes(data.postData.raw);
                            out.flush();
                        }
                        break;
                }

                // read response
                try (final InputStream inputStream = urlConnection.getInputStream()) {
                    //noinspection StatementWithEmptyBody
                    while (inputStream.read() != -1);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            callback.onResult(result);
        }
    }
}
