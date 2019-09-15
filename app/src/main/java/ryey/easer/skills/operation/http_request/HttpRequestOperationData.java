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

import android.os.Build;
import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.Set;

import ryey.easer.Utils;
import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.SolidDynamicsAssignment;
import ryey.easer.commons.local_skill.operationskill.OperationData;
import ryey.easer.plugin.PluginDataFormat;
import ryey.easer.skills.operation.DynamicsEnabledString;

public class HttpRequestOperationData implements OperationData {

    private static final String K_REQUEST_METHOD = "requestMethod";
    private static final String K_URL = "url";
    private static final String K_REQUEST_HEADER = "requestHeader";
    private static final String K_CONTENT_TYPE = "contentType";
    private static final String K_POST_DATA = "postData";

    enum RequestMethod {
        GET,
        POST,
    }

    @NonNull
    final RequestMethod requestMethod;
    @NonNull
    final DynamicsEnabledString url;
    @NonNull
    final DynamicsEnabledString requestHeader;
    @NonNull
    final DynamicsEnabledString contentType;
    @NonNull
    final DynamicsEnabledString postData;

    HttpRequestOperationData(@NotNull RequestMethod requestMethod, @NotNull String url, @NotNull String requestHeader, @NotNull String contentType, @NotNull String postData) {
        this(requestMethod, new DynamicsEnabledString(url), new DynamicsEnabledString(requestHeader), new DynamicsEnabledString(contentType), new DynamicsEnabledString(postData));
    }

    HttpRequestOperationData(@NotNull RequestMethod requestMethod, @NotNull DynamicsEnabledString url, @NotNull DynamicsEnabledString requestHeader, @NotNull DynamicsEnabledString contentType, @NotNull DynamicsEnabledString postData) {
        this.requestMethod = requestMethod;
        this.url = url;
        this.requestHeader = requestHeader;
        this.contentType = contentType;
        this.postData = postData;
    }

    HttpRequestOperationData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    requestMethod = RequestMethod.valueOf(jsonObject.getString(K_REQUEST_METHOD));
                    url = new DynamicsEnabledString(jsonObject.getString(K_URL));
                    requestHeader = new DynamicsEnabledString(jsonObject.getString(K_REQUEST_HEADER));
                    contentType =new DynamicsEnabledString( jsonObject.getString(K_CONTENT_TYPE));
                    postData = new DynamicsEnabledString(jsonObject.getString(K_POST_DATA));
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new IllegalStorageDataException(e);
                }
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(K_REQUEST_METHOD, requestMethod);
            jsonObject.put(K_URL, url.toString());
            jsonObject.put(K_REQUEST_HEADER, requestHeader.toString());
            jsonObject.put(K_CONTENT_TYPE, contentType.toString());
            jsonObject.put(K_POST_DATA, postData.toString());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean isValid() {
        if (Utils.isBlank(url)) {
            return false;
        }

        if (requestMethod == RequestMethod.POST && Utils.isBlank(contentType)) {
            return false;
        }

        return true;
    }

    @Nullable
    @Override
    public Set<String> placeholders() {
        Set<String> placeholders = url.placeholders();
        placeholders.addAll(requestHeader.placeholders());
        placeholders.addAll(contentType.placeholders());
        placeholders.addAll(postData.placeholders());
        return placeholders;
    }

    @NonNull
    @Override
    public OperationData applyDynamics(SolidDynamicsAssignment dynamicsAssignment) {
        return new HttpRequestOperationData(requestMethod,
                url.applyDynamics(dynamicsAssignment),
                requestHeader.applyDynamics(dynamicsAssignment),
                contentType.applyDynamics(dynamicsAssignment),
                postData.applyDynamics(dynamicsAssignment));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequestOperationData that = (HttpRequestOperationData) o;
        return requestMethod.equals(that.requestMethod) &&
                url.equals(that.url) &&
                requestHeader.equals(that.requestHeader) &&
                contentType.equals(that.contentType) &&
                postData.equals(that.postData);
    }

    @Override
    public int hashCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.hash(requestMethod, url, requestHeader, contentType, postData);
        } else {
            return requestHeader.hashCode() * 31 * 31 * 31 * 31
                    + url.hashCode() * 31 * 31 * 31
                    + requestHeader.hashCode() * 31 * 31
                    + contentType.hashCode() * 31
                    + postData.hashCode();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(requestMethod);
        dest.writeString(url.str);
        dest.writeString(requestHeader.str);
        dest.writeString(contentType.str);
        dest.writeString(postData.str);
    }

    public static final Creator<HttpRequestOperationData> CREATOR = new Creator<HttpRequestOperationData>() {
        public HttpRequestOperationData createFromParcel(Parcel in) {
            return new HttpRequestOperationData(in);
        }

        public HttpRequestOperationData[] newArray(int size) {
            return new HttpRequestOperationData[size];
        }
    };

    private HttpRequestOperationData(Parcel in) {
        requestMethod = (RequestMethod) Objects.requireNonNull(in.readSerializable());
        url = new DynamicsEnabledString(Objects.requireNonNull(in.readString()));
        requestHeader = new DynamicsEnabledString(Objects.requireNonNull(in.readString()));
        contentType = new DynamicsEnabledString(Objects.requireNonNull(in.readString()));
        postData = new DynamicsEnabledString(Objects.requireNonNull(in.readString()));
    }
}
