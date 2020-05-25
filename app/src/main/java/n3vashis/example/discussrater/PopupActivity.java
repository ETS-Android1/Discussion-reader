package n3vashis.example.discussrater;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

//Activity for user to edit details about book
public class PopupActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    //permissions need to read and write images from device
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    Model m;
    SharedPreferences sharedpreferences;
    //text fields
    TextView name;
    TextView author;
    TextView link;
    RatingBar mBar;
    Uri selectedImage;

    //string keys to be passed in to shared preferences
    public static final String mypreference = "mypref";
    public static final String Name = "nameKey";
    public static final String Author = "authorKey";
    public static final String Link = "linkKey";
    public static final String Rating = "ratingKey";
    public static final String URI = "uriKey";
    Uri suri;
    Bitmap bitmap;
    public static float rating = 0.0f;
    private static int RESULT_LOAD_IMAGE = 1;
    private String data = "";
    private volatile int id;
    Button b2;
    ImageButton iButton;
    String base;
    Map<String,String> listBook;
    DbClass dbHandler;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);

        dbHandler = new DbClass(getApplicationContext());

        b2 = (Button) findViewById(R.id.button4);
        iButton = (ImageButton) findViewById(R.id.imageButton2);
        name = (TextView) findViewById(R.id.editTextPhone);
        author= (TextView) findViewById(R.id.editTextStreet);
        link = (TextView) findViewById(R.id.editTextEmail);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();
        editor.putInt("size",0);

        //grab data passed in from MainActivity when it starts intent
        Bundle extraInfo = getIntent().getExtras();
        data = extraInfo.getString("title");
        id = extraInfo.getInt("id");
        name.setText(data);

        //load the data from the database
        loadData();

        mBar = (RatingBar) findViewById(R.id.ratingBar);
        mBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rate,
                                        boolean fromUser) {
                System.out.println("The rating is " + rate);
                rating = rate;
            }
        });

        //button saves the data entered by the user into the database
        b2.setEnabled(true);
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
             EditText editText = (EditText) findViewById(R.id.editTextStreet);
             String authorName = editText.getText().toString();
             //System.out.println("the author name is " + authorName);
             dbHandler.updateAuthor(id,authorName); //save in database
            }
        });

        //button that allows user to choose image from their device
        iButton.setEnabled(true);
        iButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                i.setType("image/*");
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

    }

    public void loadData(){

        listBook =  dbHandler.loadExisting(); //Map that loads all books from database
        //System.out.println("the book size is " + listBook.size());
        //System.out.println("the id is: " + id);

        name = (TextView) findViewById(R.id.editTextPhone);
        author = (TextView) findViewById(R.id.editTextStreet);
        link = (TextView) findViewById(R.id.editTextEmail);
        mBar = (RatingBar) findViewById(R.id.ratingBar);


        Cursor res = dbHandler.getData(id);
        if(res != null && res.moveToFirst()) {
            res.moveToFirst();
            //retrieve and set the image
            String temp = res.getString(res.getColumnIndex(DbClass.BOOKS_COLUMN_IMAGE));
            if (temp != null) {
                Bitmap b = decodeBase64(temp);
                ImageView i = (ImageView) findViewById(R.id.imageView6);
                i.setImageBitmap(b);
            }
            //retrieve and set title
            String title = res.getString(res.getColumnIndex(DbClass.BOOKS_COLUMN_NAME));
            name.setText(title);

            //retrieve and set author
            String auth = res.getString(res.getColumnIndex(DbClass.BOOKS_COLUMN_AUTHOR));
            author.setText(auth);

            //retrieve and set rating bar
            Float rate = res.getFloat(res.getColumnIndex(DbClass.BOOKS_COLUMN_VOTE));
            mBar.setRating(rate);
        }


    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        //if the user changes data notify the MainActivity
        returnIntent.putExtra("result",name.getText().toString());
        returnIntent.putExtra("uri",base); //image passed as base64 string
        returnIntent.putExtra("id",id); //id so the MainActivity can find book in database
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        System.out.println("Permission has been denied");
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        System.out.println("Permission has been granted");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(101)
    private void smsTask() {
        ImageView i = (ImageView) findViewById(R.id.imageView6);
        if (EasyPermissions.hasPermissions(this,galleryPermissions)) {
            setImageView(i,suri);
        } else {
            EasyPermissions.requestPermissions(this, "Access for storage",
                    101, galleryPermissions);
        }
    }

    //@AfterPermissionGranted(101)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        ImageView i = (ImageView) findViewById(R.id.imageView6);
        suri = data.getData();

        PopupActivity.this.grantUriPermission
               (PopupActivity.this.getPackageName(),suri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

        //request permission to change image
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            if (EasyPermissions.hasPermissions(this, galleryPermissions)) {
                setImageView(i,suri);

            } else {
                EasyPermissions.requestPermissions(this, "Access for storage",
                        101, galleryPermissions);
            }
        }

    }

    public void setImageView(ImageView i,Uri u){
        try {
            Set bookIcon = convertMapSet(listBook);
            selectedImage = u;
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            i.setImageBitmap(bitmap);

            SharedPreferences.Editor editor = sharedpreferences.edit();
            base = encodeTobase64(bitmap);

            if(bookIcon != null) {
                bookIcon.add(base);
                //dbHandler.updateContact(id,base);
            }
            //add the image to shared preferences so the PollActivity can access it
            editor.putStringSet("set",bookIcon);
            editor.commit();

        }catch(IOException e){
            System.out.println("the error is "+ e);
        }
    }

    //convert the map "listbook" of the books to a set so it can be passed into sharedpreferences
    //to be used by the PollActivity so it can set books in view pager
    public Set<String> convertMapSet(Map<String,String> m){

        if(m != null) {
            //System.out.println("map is not nul");
            Set<String> s = new HashSet<>();
            for (Map.Entry<String, String> pair : m.entrySet()) {
                System.out.println("created");
                String temp = pair.getValue();
                s.add(temp);
            }
            return s;
        }
        else {
            return null;
        }
    }

    //encode bitmap to base64 string so it's easier to pass
    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    //decode base64 string to bitmap
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}


