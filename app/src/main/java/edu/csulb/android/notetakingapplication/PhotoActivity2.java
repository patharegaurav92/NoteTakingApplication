package edu.csulb.android.notetakingapplication;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class PhotoActivity2 extends AppCompatActivity {
TextView captionTextView;
    ImageView photo;
    Button delete,edit;
    Uri file;
    Context ctx = this;
    DatabaseOperations dop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent receivePhoto = getIntent();
        Bundle receiveBundle = receivePhoto.getExtras();
        String caption = receiveBundle.getString("caption");
        final String path  = receiveBundle.getString("path");
        final String photoNo = receiveBundle.getString("photoNo");
        Log.v("exxxxxxxxxxxxxxx",photoNo);
        //Toast.makeText(getBaseContext(),"Path "+path+" Caption"+caption,Toast.LENGTH_LONG).show();
        captionTextView = (TextView) findViewById(R.id.caption_text_view);
        photo = (ImageView) findViewById(R.id.photo_image_view);
        delete = (Button) findViewById(R.id.delete_button);
        edit = (Button) findViewById(R.id.edit_button);
        //back = (Button) findViewById(R.id.back_button);
        captionTextView.setText(caption);
        File file1 = new File(path);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap b = BitmapFactory.decodeFile(file1.getAbsolutePath(), options);
        photo.setImageBitmap(b);
        dop = new DatabaseOperations(ctx);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dop.getWritableDatabase();
                String where = TableData.TableInfo.PHOTO_NUMBER + "=?";
                String[] whereArgs = {photoNo};
                db.delete(TableData.TableInfo.TABLE_NAME,where,whereArgs);
                db.close();

                File fdelete = new File(path);
                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        System.out.println("file Deleted :" + path);
                    } else {
                        System.out.println("file not Deleted :" + path);
                    }
                }

                Intent view = new Intent(PhotoActivity2.this,ListActivity.class);
                startActivity(view);

            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PhotoActivity2.this,AddPhotoActivity.class);
                Bundle status = new Bundle();
                status.putInt("status",2);
                status.putString("photoNo",photoNo);
                i.putExtras(status);
                startActivity(i);
            }
        });

    }
}
