package n3vashis.example.discussrater;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.Iterator;
import java.util.Set;

import pub.devrel.easypermissions.EasyPermissions;

//Activity for users to vote on books
public class PollActivity extends AppCompatActivity {

    private String URI = "uriKey";
    private Uri suri = null;
    ViewPager viewPager;
    CustomPagerAdapter cp;
    SharedPreferences mSharedPreference;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poll_layouts);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        cp = new CustomPagerAdapter(this);
        viewPager.setAdapter(cp);

        Button vone = (Button) findViewById(R.id.vone);
        vone.setEnabled(true);
        vone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ;
            }
        });

        //images retrieved from PopupActivty through shared preferences
        mSharedPreference = getSharedPreferences("mypref",
                Context.MODE_PRIVATE);
        //images of the books passed as a set
        Set<String> hset = mSharedPreference.getStringSet("set",null);

        if(hset != null) {
            Iterator<String> it = hset.iterator();
            int count = 0;

            while (it.hasNext()) {
                String base = it.next();
                ImageView im = new ImageView(this);
                if (base != null) { //image to set
                    //System.out.println("added");
                    Bitmap b = decodeBase64(base);
                    im.setImageBitmap(b); //set the image as the decoded bitmap
                    cp.addView(im, count);
                    cp.notifyDataSetChanged();
                    count++;
                }
            }
        }

    }

    public void addView (View newPage)
    {
        int pageIndex = cp.addView (newPage);
        viewPager.setCurrentItem (pageIndex, true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        ImageView i = (ImageView) findViewById(R.id.imageView6);
        suri = data.getData();
        PollActivity.this.grantUriPermission
                (PollActivity.this.getPackageName(),suri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }

    //decode base64 string to bitmap
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}

