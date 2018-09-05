package com.yd.yourdoctorandroid.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.yd.yourdoctorandroid.BuildConfig;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import okhttp3.MultipartBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ImageUtils {
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_CHOOSE_PHOTO = 2;
    private static final int REQUEST_PERMISSION_CODE = 1;

    private String mImagePathToBeAttached;
    private Bitmap mImageToBeAttached;
    private String filename;
    MultipartBody.Part imageUpload;
    File file;
    FragmentActivity fragmentActivity;


    public ImageUtils(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }


    public void displayAttachImageDialog() {
        CharSequence[] items;

        items = new CharSequence[]{"Chụp ảnh", "Chọn ảnh"};

        AlertDialog.Builder builder = new AlertDialog.Builder(fragmentActivity);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    dispatchTakePhotoIntent();
                } else if (item == 1) {
                    dispatchChoosePhotoIntent();
                } else {
                    deleteCurrentPhoto();
                }
            }
        });
        builder.show();
    }


    private void dispatchTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(fragmentActivity.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Log.e("anhle", "Cannot create a temp image file", e);
            }

            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(fragmentActivity,
                        BuildConfig.APPLICATION_ID + ".provider", photoFile));
                if (checkPermission()) {
                    fragmentActivity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                } else {
                    requestPermission();
                }
            }
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(fragmentActivity, new
                String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, REQUEST_PERMISSION_CODE);
    }

    private void dispatchChoosePhotoIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        fragmentActivity.startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_CHOOSE_PHOTO);
    }

    private void deleteCurrentPhoto() {
        if (mImageToBeAttached != null) {
            mImageToBeAttached.recycle();
            mImageToBeAttached = null;
        }
    }

    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "TODO_LITE-" + timeStamp + "_";
        File storageDir = fragmentActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(fileName, ".jpg", storageDir);
        mImagePathToBeAttached = image.getAbsolutePath();
        return image;
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(fragmentActivity.getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(fragmentActivity.getApplicationContext(),
                CAMERA);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    public int getOrientation(Context context, Uri photoUri) {
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public String getmImagePathToBeAttached() {
        return mImagePathToBeAttached;
    }

    public Bitmap getmImageToBeAttached() {
        return mImageToBeAttached;
    }

    public String getFilename() {
        return filename;
    }

    public MultipartBody.Part getImageUpload() {

        filename = UUID.randomUUID().toString();
        if (mImageToBeAttached != null) {
            file = Utils.persistImage(mImageToBeAttached, filename, fragmentActivity.getApplicationContext());
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            imageUpload = MultipartBody.Part.createFormData("imageChat", file.getName(), requestBody);
        }
        return imageUpload;
    }

    public void setmImagePathToBeAttached(String mImagePathToBeAttached) {
        this.mImagePathToBeAttached = mImagePathToBeAttached;
    }

    public void setmImageToBeAttached(Bitmap mImageToBeAttached) {
        this.mImageToBeAttached = mImageToBeAttached;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void clearAll() {
        mImagePathToBeAttached = null;
        mImagePathToBeAttached = null;
        filename = null;
        file = null;
        imageUpload = null;
    }
}
