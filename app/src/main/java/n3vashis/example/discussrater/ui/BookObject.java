package n3vashis.example.discussrater.ui;


import n3vashis.example.discussrater.R;

public enum BookObject {

    RED(0, R.layout.view_red),
    BLUE(1, R.layout.view_blue),
    GREEN(2, R.layout.view_green);

    private int mTitleResId;
    private int mLayoutResId;

   BookObject(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}
