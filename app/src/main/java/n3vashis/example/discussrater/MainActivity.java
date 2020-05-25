package n3vashis.example.discussrater;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;



public class MainActivity extends AppCompatActivity implements Observer {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private volatile int numTitles = 0;
    private String name = "";
    String baseImage;
    ArrayList<Model> titles;
    TextView begText;
    ImageView begImage;
    DbClass db = new DbClass(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Resources res = getResources();

        //recyclerview to hold list of books
        recyclerView = (RecyclerView) findViewById(R.id.rvContacts);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        titles = Model.createContactsList(numTitles);
        mAdapter = new TitleAdapter(titles);
        recyclerView.setAdapter(mAdapter);

        Map<String,String> listBook =  db.loadExisting();
        System.out.println(listBook.size());
        numTitles = listBook.size();
        begText = (TextView) findViewById(R.id.textView6);
        begImage = (ImageView) findViewById(R.id.imageView);

        if(listBook.size() > 0){
            begText.setVisibility(begText.GONE);
            begImage.setVisibility(begImage.GONE);
        }

        //load the books from the database on creation
        int count = 0;
        for (Map.Entry<String,String> pair : listBook.entrySet()){

            String temp = pair.getValue();
            Model m = new Model(pair.getKey(),pair.getValue(),count);
            //System.out.println("the value is " + pair.getValue());
            m.addObserver(this);
            titles.add(m);
            mAdapter.notifyDataSetChanged();
            count++;
        }


    }


    public void addBook(String s){
        String author = ""; //author name initially empty to be edited by user in next page
        db.insert(s, author, 0,null);
        //System.out.println("num " + numTitles);

        Cursor rs = db.getData(numTitles);
        if(rs != null && rs.moveToFirst()) {
            rs.moveToFirst();
            name = rs.getString(rs.getColumnIndex(DbClass.BOOKS_COLUMN_NAME));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.poll:
                startPoll();  //voting page
                break;
            case R.id.load:
                onLoadPressed();  //open edit dialog
                break;
            case R.id.chat:
                startChat();      //chat page
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public void startPoll(){
        Intent i = new Intent(this, PollActivity.class);
        startActivityForResult(i,0);
    }

    public void startChat(){
        Intent i = new Intent(this, ChatActivity.class);
        startActivity(i);
    }

    //dialog to enter book name
    public void onLoadPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter book name");

        final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout, null);
        builder.setView(customLayout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText editText = customLayout.findViewById(R.id.editText);
                String s = editText.getText().toString();
                createNewBook(s);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void createNewBook(String s){
        numTitles++;
        Model m = new Model(s,null,numTitles);
        m.addObserver(this);

        //add to the list of books
        titles.add(m);
        mAdapter.notifyDataSetChanged();
        //scroll to the bottom on creation
        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
        begText.setVisibility(begText.GONE);
        begImage.setVisibility(begImage.GONE);
        addBook(s); //add book to database as well
    }

    @Override
    public void update(Observable o, Object arg) {
        Model m = (Model) o;
        //System.out.println(m.buttonC);
        if(m.buttonC == true) {
            //System.out.println("here");
            Intent intent = new Intent(this, PopupActivity.class);
            intent.putExtra("title",name);
            intent.putExtra("id",m.id); //let edit activity know id so it can retrieve book
            startActivityForResult(intent,0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,null);
        // Check which request we're responding to
        if (requestCode == 0) {
            // Make sure the request was successful
            if(resultCode == Activity.RESULT_OK){

                int id = data.getIntExtra("id",0);
                //edit activity send the image as a string to be set later
                baseImage = data.getStringExtra("uri");
                if(baseImage != null) {
                    storeBitmapInDB(baseImage, id);
                }
            }
        }
    }

    //function for storing image in db and setting it
    public void storeBitmapInDB(String convert,int id){
        db.updateContact(id,convert);
        System.out.println("result");
        titles.get(id - 1).setImage(convert); //set image in list adapter
        mAdapter.notifyDataSetChanged();

    }



}
