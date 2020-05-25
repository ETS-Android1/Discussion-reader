package n3vashis.example.discussrater.ui;


import android.content.res.Resources;
import android.content.res.TypedArray;
import android.media.Image;
import android.text.Layout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import n3vashis.example.discussrater.R;

public class BookObject extends AppCompatActivity{


    private int mTitleResId;
    private int mLayoutResId;
    private int index = 0;

    Resources res  = getResources();
    TypedArray ta = res.obtainTypedArray(R.array.layouts);
    //ArrayList<View> ra = new ArrayList<>();

   BookObject(int titleResId, int layoutResId, ImageView i) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
        RelativeLayout rl = createLayout(i);
        //ra.add(rl);
        //index = ra.size() - 1;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

    public RelativeLayout createLayout(ImageView image) {
        RelativeLayout rl = new RelativeLayout(this);
        RelativeLayout.LayoutParams firstImageParams = new RelativeLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        firstImageParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        rl.addView(image,firstImageParams);
        return rl;
    }
}
