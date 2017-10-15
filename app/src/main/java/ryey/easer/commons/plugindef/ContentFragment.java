package ryey.easer.commons.plugindef;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ContentFragment extends Fragment {
    protected ContentLayout contentLayout;

    public static ContentFragment createInstance(ContentLayout contentLayout) {
        ContentFragment fragment = new ContentFragment();
        fragment.contentLayout = contentLayout;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return contentLayout;
    }

    public String desc() {
        return contentLayout.desc();
    }

    public void fill(StorageData data) {
        contentLayout.fill(data);
    }

    public StorageData getData() {
        return contentLayout.getData();
    }

    public void setEnabled(boolean enabled) {
        contentLayout.setEnabled(enabled);
    }
}
