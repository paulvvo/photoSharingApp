package com.parse.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class Main3Activity extends AppCompatActivity {

    Intent getIntent;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        getIntent = getIntent();
        String name = getIntent.getStringExtra("username").toString();
        setTitle(name +"'s Feed");

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Image");
        query.whereEqualTo("username", name);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e ==null){
                    if(objects.size()>0){
                        for(ParseObject temp : objects){
                            ParseFile file  = (ParseFile) temp.get("image");
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if(e == null && data != null){
                                        Bitmap bitmap  = BitmapFactory.decodeByteArray(data, 0, data.length);

                                        ImageView imageView = new ImageView(getApplicationContext());
                                        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT
                                        ));
                                        linearLayout.addView(imageView);

                                        imageView.setImageBitmap(bitmap);
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });

        //imageView.setImageDrawable(getResources().getDrawable(R.drawable.b));

    }
}