package n3vashis.example.discussrater;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


//chat activity for users to chat with other users
public class ChatActivity extends AppCompatActivity {

    private Firebase myDfb;
    private FirebaseAuth mAuth;
    private FirebaseListAdapter<Message> adapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.chat_layouts);

        Firebase.setAndroidContext(this);
        myDfb = new Firebase("https://DiscussRater.firebaseio.com");
        mAuth = FirebaseAuth.getInstance();

        //signInAnonymously();
        final EditText input = (EditText) findViewById(R.id.message_text);

        Button fab = (Button)findViewById(R.id.fab);

        //button sends message to database
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = input.getText().toString();
                myDfb.push().setValue(new Message("puf", message));
                input.setText("");
            }
        });
        startUi();

    }

    //loads the image to the ui
    public void startUi(){
        ListView listOfMessages = (ListView)findViewById(R.id.list);
        System.out.println("reached here");
        Query query = FirebaseDatabase.getInstance().getReference();

        FirebaseListOptions<Message> options =
                new FirebaseListOptions.Builder<Message>()
                        .setQuery(query, Message.class)
                        .setLayout(R.layout.texts)
                        .build();
        System.out.println("reached");
        adapter = new FirebaseListAdapter<Message>(options){

           @Override
            protected void populateView(View v, Message model, int position) {

               System.out.println("the message is " + model.getMessageText());
                TextView messageText = (TextView)v.findViewById(R.id.message_text_view);
                TextView messageUser = (TextView)v.findViewById(R.id.username_text_view);

                messageText.setText(model.getMessageUser());
                messageUser.setText(model.getMessageText());

            }
        };

        listOfMessages.setAdapter(adapter);


    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        //FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    //sign the user in anonymously
    private void signInAnonymously() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            System.out.println("task successful");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            Toast.makeText(ChatActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
