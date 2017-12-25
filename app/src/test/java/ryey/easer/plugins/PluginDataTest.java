package ryey.easer.plugins;

import org.junit.Test;

import ryey.easer.commons.C;
import ryey.easer.commons.plugindef.DataFactory;
import ryey.easer.commons.plugindef.PluginDef;
import ryey.easer.commons.plugindef.StorageData;
import ryey.easer.plugins.operation.broadcast.BroadcastOperationPlugin;

import static org.junit.Assert.assertEquals;

public class PluginDataTest {

    @Test
    public void testPluginData() throws Exception {
        for (PluginDef plugin : PluginRegistry.getInstance().all().getAllPlugins()) {
            if (plugin instanceof BroadcastOperationPlugin)
                continue;
            DataFactory factory = plugin.dataFactory();
            StorageData dummyData = factory.dummyData();
            for (C.Format format : C.Format.values()) {
                String serialized_data = dummyData.serialize(format);
                StorageData parsed_data = factory.parse(serialized_data, format, C.VERSION_CURRENT);
                assertEquals(dummyData, parsed_data);
            }
        }
    }

}
