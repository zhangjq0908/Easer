package ryey.easer.core.data.storage.backend.json.profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;

import ryey.easer.commons.C;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationPlugin;
import ryey.easer.core.data.ProfileStructure;
import ryey.easer.core.data.storage.backend.Serializer;
import ryey.easer.core.data.storage.backend.UnableToSerializeException;
import ryey.easer.plugins.PluginRegistry;

public class ProfileSerializer implements Serializer<ProfileStructure> {

    public String serialize(ProfileStructure profile) throws UnableToSerializeException {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(C.NAME, profile.getName());
            jsonObject.put(C.VERSION, C.VERSION_CURRENT);
            jsonObject.put(C.OPERATION, serialize_operation(profile));
            return jsonObject.toString();
        } catch (JSONException e) {
            throw new UnableToSerializeException(e.getMessage());
        }
    }

    JSONArray serialize_operation(ProfileStructure profile) throws JSONException {
        JSONArray json_operations = new JSONArray();
        for (OperationPlugin plugin : PluginRegistry.getInstance().operation().getAllPlugins()) {
            Collection<OperationData> possibleData = profile.get(plugin.id());
            if (possibleData != null) {
                for (OperationData data : possibleData) {
                    JSONObject single_data_object = new JSONObject();
                    single_data_object.put(C.SPEC, plugin.id());
                    String serialized_data = data.serialize(C.Format.JSON);
                    single_data_object.put(C.DATA, serialized_data);
                    json_operations.put(single_data_object);
                }
            }
        }
        return json_operations;
    }
}
