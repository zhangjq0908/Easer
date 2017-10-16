package ryey.easer.plugins.event.battery;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.EnumSet;

import ryey.easer.commons.IllegalXmlException;
import ryey.easer.commons.XmlHelper;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.event.TypedEventData;

import static ryey.easer.plugins.event.battery.BatteryEventPlugin.pname;

public class BatteryEventData extends TypedEventData {

    protected Integer battery_status = null;

    {
        default_type = EventType.is;
        availableTypes = EnumSet.of(EventType.is, EventType.is_not);
    }

    public BatteryEventData() {
    }

    public BatteryEventData(Integer battery_status) {
        this.battery_status = battery_status;
    }

    public BatteryEventData(Integer battery_status, EventType type) {
        this.battery_status = battery_status;
        setType(type);
    }

    @Override
    public Class<? extends EventPlugin> pluginClass() {
        return BatteryEventPlugin.class;
    }

    @Override
    public Object get() {
        return battery_status;
    }

    @Override
    public void set(Object obj) {
        if (obj instanceof Integer) {
            battery_status = (Integer) obj;
        } else {
            throw new RuntimeException("illegal data");
        }
    }

    @Override
    public void parse(XmlPullParser parser, int version) throws IOException, XmlPullParserException, IllegalXmlException {
        String str_data = XmlHelper.EventHelper.readSingleSituation(parser);
        Integer int_data = Integer.parseInt(str_data);
        set(int_data);
        EventType type = XmlHelper.EventHelper.readLogic(parser);
        setType(type);
    }

    @Override
    public void serialize(XmlSerializer serializer) throws IOException {
        Integer int_status = (Integer) get();
        if (int_status != null) {
            String status = int_status.toString();
            XmlHelper.EventHelper.writeSingleSituation(serializer, pname(), status);
            XmlHelper.EventHelper.writeLogic(serializer, type());
        }
    }

    @Override
    public boolean isValid() {
        if ((battery_status == BatteryStatus.charging) || (battery_status == BatteryStatus.discharging))
            return true;
        return false;
    }
}
