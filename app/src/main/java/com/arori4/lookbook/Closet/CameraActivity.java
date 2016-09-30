package com.arori4.lookbook.Closet;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.arori4.lookbook.R;

import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Defines the camera class
 */
public class CameraActivity extends Activity implements SurfaceHolder.Callback {
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;

    private static final int REQUEST_CAMERA_PERMISSION = 38237;
    private static final int REQUEST_EXTERNAL_STORAGE_PERMISSION = 415203652;
    private static final int REQUEST_INTERNET_PERMISSION = 33;

    Camera.PictureCallback rawCallback;
    Camera.ShutterCallback shutterCallback;
    Camera.PictureCallback jpegCallback;

    ImageView overlay;
    String EXTRA_TYPE_STRING;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_camera);

        //Ask for permissions
        if (android.support.v4.content.ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
                android.support.v4.app.ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                        REQUEST_CAMERA_PERMISSION);
        }
        if (android.support.v4.content.ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            android.support.v4.app.ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE_PERMISSION);
        }
        if (android.support.v4.content.ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            android.support.v4.app.ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},
                    REQUEST_INTERNET_PERMISSION);
        }

        EXTRA_TYPE_STRING = getIntent().getStringExtra(Clothing.EXTRA_TYPE_STRING);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();

        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        //Set the overlay image
        overlay = (ImageView)findViewById(R.id.overlay);
        switch (EXTRA_TYPE_STRING) {
            case Clothing.TOP:
                overlay.setImageResource(R.drawable.top);
                break;
            case Clothing.BOTTOM:
                overlay.setImageResource(R.drawable.pants);
                break;
            case Clothing.ACCESSORY:
                overlay.setImageResource(R.drawable.accessory);
                break;
            case Clothing.SHOE:
                overlay.setImageResource(R.drawable.sneaker);
                break;
            case Clothing.BODY:
                overlay.setImageResource(R.drawable.dress);
                break;
            case Clothing.HAT:
                overlay.setImageResource(R.drawable.cap);
                break;
            case Clothing.JACKET:
                overlay.setImageResource(R.drawable.hooded_jacket);
                break;
        }



        jpegCallback = new PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream = null;
                try {
                    id = String.format("/sdcard/%d.png", System.currentTimeMillis());
                    outStream = new FileOutputStream(id);

                    outStream.write(data);
                    outStream.close();
                    Toast.makeText(getApplicationContext(), "Picture Saved", Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(getBaseContext(), AddClothingActivity.class);
                    intent.putExtra("photo_id",id);
                    intent.putExtra("Category",EXTRA_TYPE_STRING);
                    finish();
                    startActivity(intent);
                }
            }
        };

    }

    public void captureImage(View v) throws IOException {
        camera.takePicture(shutterCallback, rawCallback, jpegCallback);
    }

    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open();
            Camera.Parameters param = camera.getParameters();
            param.setPreviewSize(720, 1280);
            camera.setDisplayOrientation(90);
            camera.setParameters(param);

            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the contacts-related task you need to do.
                    surfaceCreated(surfaceHolder);

                } else {
                    System.err.println("Oh no! You can't use the camera!");
                }
            }
            case REQUEST_EXTERNAL_STORAGE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the contacts-related task you need to do.
                } else {
                    System.err.println("Oh no! You can't use the SD card!");
                }
            }
            case REQUEST_INTERNET_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the contacts-related task you need to do.
                } else {
                    System.err.println("Oh no! You can't use the internet!");
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}