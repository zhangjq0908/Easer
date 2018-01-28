package ryey.easer.core.data.storage.backend.json.profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationPlugin;
import ryey.easer.core.data.ProfileStructure;
import ryey.easer.core.data.storage.backend.IOUtils;
import ryey.easer.core.data.storage.backend.Parser;
import ryey.easer.plugins.PluginRegistry;

class ProfileParser implements Parser<ProfileStructure> {

    ProfileStructure profile = new ProfileStructure();

    public ProfileStructure parse(InputStream in) throws IOException, IllegalStorageDataException {
        try {
            profile = new ProfileStructure();
            JSONObject jsonObject = new JSONObject(IOUtils.inputStreamToString(in));
            int version = jsonObject.optInt(C.VERSION);
            profile.setName(jsonObject.optString(C.NAME));
            JSONArray jsonArray = jsonObject.optJSONArray(C.OPERATION);
            parseOperations(jsonArray, version);
            return profile;
        } catch (JSONException e) {
            throw new IllegalStorageDataException(e);
        }
    }

    private void parseOperations(JSONArray jsonArray, int version) throws JSONException, IllegalStorageDataException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String spec = jsonObject.optString(C.SPEC);
            if (Utils.isBlank(spec)) {
                throw new IllegalStorageDataException("Illegal Item: No Spec");
            }
            String content = jsonObject.optString(C.DATA);
            OperationPlugin plugin = PluginRegistry.getInstance().operation().findPlugin(spec);
            OperationData data = plugin.dataFactory().parse(content, C.Format.JSON, version);
            profile.set(plugin.id(), data);
        }
    }
}
