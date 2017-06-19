package ryey.easer.plugins.reusable;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LeftNRight extends LinearLayout {
    TextView textView;
    EditText editText;

    public LeftNRight(Context context) {
        super(context);
        textView = new TextView(context);
        editText = new EditText(context);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        editText.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        setOrientation(HORIZONTAL);
        addView(textView);
        addView(editText);
    }

    public void setLeftText(CharSequence text) {
        textView.setText(text);
    }

    public void setLeftText(@StringRes int resid) {
        textView.setText(resid);
    }

    public void setRightHint(CharSequence hint) {
        editText.setHint(hint);
    }

    public void setRightHint(@StringRes int resid) {
        editText.setHint(resid);
    }

    public void setRightText(CharSequence text) {
        editText.setText(text);
    }

    public void setRightText(@StringRes int resid) {
        editText.setText(resid);
    }

    public EditText getEditText() {
        return editText;
    }
}
