package edu.csulb.android.notetakingapplication;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.cacheColorHint;
import static android.R.attr.path;
import static edu.csulb.android.notetakingapplication.R.id.add;
import static edu.csulb.android.notetakingapplication.R.id.imageView;
import static java.lang.Boolean.FALSE;

public class AddPhotoActivity extends AppCompatActivity {

    ImageView image;
    Uri file;
    TextView previewImageText;
    Button takePhoto,savePhoto,delete;
    Context ctx= this;
    EditText caption_editText;
    String caption;
    String ph_path;
    Boolean newPicTaken = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        previewImageText = (TextView) findViewById(R.id.textView2);
        takePhoto = (Button) findViewById(R.id.click_photo_button);
        caption_editText = (EditText) findViewById(R.id.editText);
        savePhoto = (Button) findViewById(R.id.save_button);
        image= (ImageView) findViewById(imageView);
        savePhoto.setVisibility(View.INVISIBLE);
        previewImageText.setVisibility(View.INVISIBLE);
        Intent recstatus = getIntent();
        final Bundle receiveStatus = recstatus.getExtras();
        int status = receiveStatus.getInt("status");
        if(status == 1) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                takePhoto.setEnabled(false);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
            savePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("AddPhotoActivity","from main");
                    File filePath = new File(file.getPath());

                    caption = caption_editText.getText().toString();
                    if (caption.matches("") || filePath.getAbsolutePath().matches("")) {
                        Toast.makeText(getBaseContext(), "You did not enter a caption", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    DatabaseOperations db = new DatabaseOperations(ctx);
                    db.putInformation(db, caption_editText.getText().toString(), filePath.getAbsolutePath());
                   // Toast.makeText(getBaseContext(), "Photo added to database Successful", Toast.LENGTH_SHORT).show();
                    Intent view = new Intent(AddPhotoActivity.this, MainActivity.class);
                    startActivity(view);
                    finish();
                }
            });

        }
        else if(status == 2){
            DatabaseOperations dop = new DatabaseOperations(ctx);
            Cursor cr=  dop.getPhotoInformation(dop,receiveStatus.getString("photoNo"));
            String caption1=null;
                     String ph_path = null;
            while(cr.moveToNext()){
                caption1= cr.getString(0);
                ph_path = cr.getString(1);
            }
            savePhoto.setVisibility(View.VISIBLE);
            File file1 = new File(ph_path);
//            Uri capturedUri = Uri.fromFile(file1);
//            image.setImageURI(capturedUri);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 6;
            Bitmap b = BitmapFactory.decodeFile(file1.getAbsolutePath(), options);
            image.setImageBitmap(b);
            previewImageText.setVisibility(View.VISIBLE);

            caption_editText.setText(caption1);
            final String finalPh_path = ph_path;
            savePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("AddPhotoActivity","from edit");
                    File filePath;
                    if(newPicTaken) {
                        filePath = new File(file.getPath());
                        newPicTaken =false;
                    }else{
                        filePath = new File(finalPh_path);
                    }
                    caption = caption_editText.getText().toString();
                    if (caption.matches("") || filePath.getAbsolutePath().matches("")) {
                        Toast.makeText(getBaseContext(), "You did not enter a caption", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    DatabaseOperations dop = new DatabaseOperations(ctx);
                    SQLiteDatabase db = dop.getWritableDatabase();
                    String where = TableData.TableInfo.PHOTO_NUMBER + "=?";
                    String[] whereArgs = {receiveStatus.getString("photoNo")};
                    ContentValues cv = new ContentValues();
                    cv.put(TableData.TableInfo.PHOTO_CAPTION,caption);
                    cv.put(TableData.TableInfo.PHOTO_PATH,filePath.getAbsolutePath());
                    db.update(TableData.TableInfo.TABLE_NAME,cv,where,whereArgs);
                    //Toast.makeText(getBaseContext(), "Photo added to database Successful", Toast.LENGTH_SHORT).show();
                    Intent displayPhoto = new Intent(AddPhotoActivity.this,PhotoActivity2.class);
                    Bundle photoBundle = new Bundle();
                    photoBundle.putString("caption",caption);
                    photoBundle.putString("path",filePath.getAbsolutePath());
                    photoBundle.putString("photoNo",receiveStatus.getString("photoNo"));
                    displayPhoto.putExtras(photoBundle);
                    startActivity(displayPhoto);
                }
            });
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePhoto.setEnabled(true);
            }
        }
    }
        public void takePicture(View view){
            newPicTaken=true;
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            file = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                   "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
           //file = FileProvider.getUriForFile(this, "edu.csulb.android.notetakingapplication.provider", createFileDir());
            String s = file.getPath();
            Log.v("File Path","File path is"+s);
            takePicture.putExtra(MediaStore.EXTRA_OUTPUT,file);
            takePicture.putExtra("return-data", true);
            startActivityForResult(takePicture,100);
        }
    private void addPhotoToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(file);
        this.sendBroadcast(mediaScanIntent);
    }
    private static File createFileDir(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "NoteTaking");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                Log.d("NoteTaking", "failed to create directory");
                return null;
            }
        }


        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getAbsolutePath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==100 && resultCode==RESULT_OK) {
            if (file != null) {
                String path1 = file.getPath();
                if (path1 != null){



                    File file1 = new File(path1);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 6;
                    Bitmap b = BitmapFactory.decodeFile(file1.getAbsolutePath(), options);
                    image.setImageBitmap(b);

                }
                savePhoto.setVisibility(View.VISIBLE);
                previewImageText.setVisibility(View.VISIBLE);
            }
        }
    }
}
