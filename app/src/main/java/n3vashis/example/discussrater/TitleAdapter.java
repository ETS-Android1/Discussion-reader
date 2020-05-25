package n3vashis.example.discussrater;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

//Adapter for the recyclerview
public class TitleAdapter extends
        RecyclerView.Adapter<TitleAdapter.ViewHolder>{


    private List<Model> titles;


    public TitleAdapter(List<Model> contacts) {
        titles = contacts;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public Button messageButton;
        public ImageView bookIcon;

        public ViewHolder(View itemView) {

            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.title_name);
            messageButton = (Button) itemView.findViewById(R.id.message_button);
            bookIcon = (ImageView) itemView.findViewById(R.id.imageView2);
        }
    }

    @Override
    public TitleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.title_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        final Model contact = titles.get(position);
        TextView textView = viewHolder.nameTextView;
        textView.setText(contact.getName());
        Button button = viewHolder.messageButton;
        button.setText ("Edit");
        button.setEnabled(true);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                contact.buttonClicked();
            }
        });

        //image is set as string in contact.base - check that it's not null
        if(contact.base != null){
            //System.out.println("not null");
            ImageView icon = (ImageView) viewHolder.bookIcon;
            Bitmap b = decodeBase64(contact.base); //decode image to bitmap
            icon.setImageBitmap(b);
        }
        else{
            ImageView icon = (ImageView) viewHolder.bookIcon;
            icon.setImageResource(R.drawable.bookicons);
        }



    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    //convert image that is base64 to a bitmap to  be used for image
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
