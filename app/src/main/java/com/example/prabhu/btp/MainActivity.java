package com.example.prabhu.btp;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Uri outputFileUri;
    private View mView;
    private View mViewpdf;
    AssetManager am;
    Context context;
    public final static String EXTRA_MESSAGE = "com.example.prabhu.btp.message";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //Toolbar will now take on default Action Bar characteristics

        setSupportActionBar(toolbar);
        ViewCompat.setElevation(toolbar,10);
        am=getAssets();
        context=MainActivity.this;
        CopyTask copyTask=new CopyTask(context,am);
        copyTask.execute();
        mView = findViewById(R.id.mainView);
        mView.setOnClickListener(this);
        mViewpdf=findViewById(R.id.mainView1);
        mViewpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainPDFActivity.class);
                startActivity(intent);
            }
        });


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


    }

    private void selectImage() {
        final String fname = "img_"+ System.currentTimeMillis() + ".jpg";
        final File sdImageMainDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }
        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_PICK);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, 1);
    }

    @Override
    public void onClick(View view) {
        if(mView == view) {
            selectImage();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                final boolean isCamera;
                if (data == null || data.getData() == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }
                Uri selectedImageUri;
                if (isCamera) {
                    selectedImageUri = outputFileUri;
                    nextStep(selectedImageUri.toString());

                } else {
                    selectedImageUri = data == null ? null : data.getData();
                    nextStep(selectedImageUri.toString());
                }
            }
        }
    }

    private void nextStep(String file) {
        Intent intent = new Intent(this, CropAndRotate.class);
        intent.putExtra(EXTRA_MESSAGE, file);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.home_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if(id==R.id.item_about){
            startActivity(new Intent(this,AboutUs.class));
        }
        return super.onOptionsItemSelected(item);
    }

}
