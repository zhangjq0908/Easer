package ryey.easer.plugins.event.connectivity;

import android.support.v4.util.ArraySet;

import com.orhanobut.logger.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

import ryey.easer.Utils;
import ryey.easer.commons.IllegalXmlException;
import ryey.easer.commons.XmlHelper;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.PluginRegistry;
import ryey.easer.plugins.event.TypedEventData;

public class ConnectivityEventData extends TypedEventData {

    protected Set<Integer> connectivity_type = new ArraySet<>();

    {
        default_type = EventType.any;
        availableTypes = EnumSet.of(EventType.any, EventType.none);
    }

    public ConnectivityEventData() {
    }

    public ConnectivityEventData(Set<Integer> connectivity_type) {
        this.connectivity_type = connectivity_type;
    }

    public ConnectivityEventData(Set<Integer> connectivity_type, EventType type) {
        this.connectivity_type = connectivity_type;
        setType(type);
    }

    @Override
    public Object get() {
        return connectivity_type;
    }

    @Override
    public void set(Object obj) {
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
    public void parse(XmlPullParser parser, int version) throws IOException, XmlPullParserException, IllegalXmlException {
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
        XmlHelper.EventHelper.writeMultipleSituation(serializer, PluginRegistry.getInstance().event().findPlugin(this).name(),
                Utils.set2strlist(selected_types).toArray(new String[0]));
        XmlHelper.EventHelper.writeLogic(serializer, type());
    }

    @Override
    public boolean isValid() {
        if (connectivity_type.size() > 0)
            return true;
        return false;
    }
}
