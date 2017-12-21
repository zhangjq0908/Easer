package ryey.easer.plugins.event.connectivity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.util.ArraySet;

import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;

import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.XmlHelper;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.PluginRegistry;
import ryey.easer.plugins.event.TypedEventData;

public class ConnectivityEventData extends TypedEventData {

    private Set<Integer> connectivity_type = new ArraySet<>();

    {
        default_type = EventType.any;
        availableTypes = EnumSet.of(EventType.any, EventType.none);
    }

    public ConnectivityEventData() {
    }

    public ConnectivityEventData(Set<Integer> connectivity_type) {
        this.connectivity_type = connectivity_type;
    }

    ConnectivityEventData(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    @NonNull
    @Override
    public Object get() {
        return connectivity_type;
    }

    @Override
    public void set(@NonNull Object obj) {
        connectivity_type.clear();
        if (obj instanceof String[]) {
            Set<Integer> selected_types = new ArraySet<>(((String[]) obj).length);
            for (String str : (String []) obj) {
                selected_types.add(Integer.parseInt(str.trim()));
            }
            set(selected_types);
        } else if (obj instanceof Set) {
            for (Object o : (Set) obj) {
                if (o instanceof Integer)
                    connectivity_type.add((Integer) o);
                else
                    Logger.wtf("Data is Set but element <%s> is not Integer", o);
            }
        } else {
            throw new RuntimeException("illegal data");
        }
    }

    @Override
    public void parse(XmlPullParser parser, int version) throws IOException, XmlPullParserException, IllegalStorageDataException {
        set(XmlHelper.EventHelper.readMultipleSituation(parser));
        EventType type = XmlHelper.EventHelper.readLogic(parser);
        setType(type);
    }

    @Override
    public void serialize(XmlSerializer serializer) throws IOException {
        if (!isValid()) {
            Logger.wtf("Invalid ConnectivityEventData shouldn't be serialized");
        }
        Set<Integer> selected_types = (Set<Integer>) get();
        XmlHelper.EventHelper.writeMultipleSituation(serializer, PluginRegistry.getInstance().event().findPlugin(this).id(),
                Utils.set2strlist(selected_types).toArray(new String[0]));
        XmlHelper.EventHelper.writeLogic(serializer, type());
    }

    @Override
    public void parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        connectivity_type.clear();
        switch (format) {
            default:
                try {
                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        connectivity_type.add(jsonArray.getInt(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new IllegalStorageDataException(e.getMessage());
                }
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull C.Format format) {
        String res;
        switch (format) {
            default:
                JSONArray jsonArray = new JSONArray();
                for (Integer v : connectivity_type) {
                    jsonArray.put(v);
                }
                res = jsonArray.toString();
        }
        return res;
    }

    @Override
    public boolean isValid() {
        if (connectivity_type.size() > 0)
            return true;
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(new ArrayList<>(connectivity_type));
    }

    public static final Parcelable.Creator<ConnectivityEventData> CREATOR
            = new Parcelable.Creator<ConnectivityEventData>() {
        public ConnectivityEventData createFromParcel(Parcel in) {
            return new ConnectivityEventData(in);
        }

        public ConnectivityEventData[] newArray(int size) {
            return new ConnectivityEventData[size];
        }
    };

    private ConnectivityEventData(Parcel in) {
        ArrayList<Integer> list = new ArrayList<>();
        in.readList(list, null);
        connectivity_type = new ArraySet<>(list);
    }
}
