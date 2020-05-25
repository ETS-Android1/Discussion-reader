package n3vashis.example.discussrater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import n3vashis.example.discussrater.ui.BookObject;

//adapter for the viewpager - holds images of the books
class CustomPagerAdapter extends PagerAdapter {
    private Context mContext;

    private ArrayList<View> views = new ArrayList<View>();
    public CustomPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        View v = views.get (position);
        collection.addView (v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public int getItemPosition (Object object)
    {
        int index = views.indexOf (object);
        if (index == -1)
            return POSITION_NONE;
        else
            return index;
    }


    public int addView (View v, int position)
    {
        views.add (position, v);
        return position;
    }

    public int addView (View v)
    {
        return addView (v, views.size());
    }

    public int removeView (ViewPager pager, View v)
    {
        return removeView (pager, views.indexOf (v));
    }

    public int removeView (ViewPager pager, int position)
    {
        pager.setAdapter (null);
        views.remove (position);
        pager.setAdapter (this);

        return position;
    }

}

