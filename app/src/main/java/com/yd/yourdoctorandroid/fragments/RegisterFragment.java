package com.yd.yourdoctorandroid.fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yd.yourdoctorandroid.BuildConfig;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.managers.AzureImageManager;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.models.AuthResponse;
import com.yd.yourdoctorandroid.networks.models.CommonErrorResponse;
import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.networks.services.RegisterPatientService;
import com.yd.yourdoctorandroid.utils.LoadDefaultModel;
import com.yd.yourdoctorandroid.utils.SharedPrefs;
import com.yd.yourdoctorandroid.utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {
    public static final String JWT_TOKEN = "JWT_TOKEN";
    public static final String USER_INFO = "USER_INFO";

    public static final String TAG = "RegisterFragment";
    public static final int REQUEST_PERMISSION_CODE = 1;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_CHOOSE_PHOTO = 2;
    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,15})";

    private String mImagePathToBeAttached;
    private Bitmap mImageToBeAttached;
    private String phoneNumber;
    private String filename;

    public RegisterFragment setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.iv_upload_avatar)
    CircleImageView ivUploadAvatar;

    @BindView(R.id.ed_fname)
    EditText edFname;
    @BindView(R.id.ed_mname)
    EditText edMname;
    @BindView(R.id.ed_lname)
    EditText edLname;
    @BindView(R.id.ed_phone)
    EditText edPhone;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.ed_confirm_password)
    EditText edCormfirmPassword;
    @BindView(R.id.ed_birthday)
    EditText edBirthday;
    @BindView(R.id.ed_address)
    EditText edAddress;

    @BindView(R.id.rg_gender)
    RadioGroup groupGender;

    @BindView(R.id.radio_male)
    RadioButton rbMale;
    @BindView(R.id.radio_other)
    RadioButton rbOther;
    @BindView(R.id.radio_female)
    RadioButton rbFmale;

    @BindView(R.id.btn_sign_up)
    CircularProgressButton btnSignUp;

    @BindView(R.id.til_fname)
    TextInputLayout tilFname;
    @BindView(R.id.til_mname)
    TextInputLayout tilMname;
    @BindView(R.id.til_lname)
    TextInputLayout tillname;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.til_confirm_password)
    TextInputLayout tilConfirmPassword;

    private Unbinder unbinder;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        setUp(view);
        return view;
    }

    private void setUp(View view) {
        filename = UUID.randomUUID().toString();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tb_main);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle(R.string.sign_up);
        toolbar.setTitleTextColor(getResources().getColor(R.color.primary_text));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        unbinder = ButterKnife.bind(this, view);
        edPhone.setText(phoneNumber);
        edPhone.setEnabled(false);
        setUpCalendar();
        ivUploadAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAttachImageDialog();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });
    }

    private void setUpCalendar() {
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateBirthDay(myCalendar);
            }

        };

        edBirthday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private String uploadImage() throws Exception {
        Log.d("UPLOAD", "uploadImage");
        final String imageName = AzureImageManager.randomString(10);
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            mImageToBeAttached.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();
            final ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
            final int imageLength = bs.available();

            final Handler handler = new Handler();

            Thread th = new Thread(new Runnable() {
                public void run() {

                    try {
                        final String fileName = AzureImageManager.UploadImage(imageName, bs, imageLength);
                        Log.d("UPLOAD", "Image");
                    } catch (Exception e) {
                        Log.d("UPLOAD", e.getMessage());
                        try {
                            throw new Exception("error");
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            });
            th.start();
        } catch (Exception ex) {
            Log.d("UPLOAD", ex.toString());
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
        return imageName;
    }

    private void updateBirthDay(Calendar myCalendar) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edBirthday.setText(sdf.format(myCalendar.getTime()));
    }

    private void onSubmit() {
        if (!onValidate()) {
            return;
        }
        btnSignUp.startAnimation();
        String avatar = null;
        onCreateUser(avatar);

    }

    private void onCreateUser(String avatar) {
        String fname = edFname.getText().toString();
        String mname = edMname.getText().toString();
        String lname = edLname.getText().toString();
        String password = edPassword.getText().toString();
        String birthday = edBirthday.getText().toString();
        String address = edAddress.getText().toString();
        int gender = getGender();
        Patient patient = new Patient(null, fname, mname, lname, phoneNumber, password, avatar, gender, birthday, address, 1,0, null);
        MultipartBody.Part avatarUpload = null;
        // Map is used to multipart the file using okhttp3.RequestBody
        File file = null;
        if (mImageToBeAttached != null) {
            file = Utils.persistImage(mImageToBeAttached, filename, getContext());
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            avatarUpload = MultipartBody.Part.createFormData("avatar", file.getName(), requestBody);
        }


        Log.d("CREATE USER", patient.toString());
        RegisterPatientService registerPatientService = RetrofitFactory.getInstance().createService(RegisterPatientService.class);
        final File finalFile = file;
        registerPatientService.register(avatarUpload, patient)
                .enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {

                        if (finalFile != null) {
                            try {
                                finalFile.delete();
                            } catch (Exception e) {
                            }
                        }
                        if (response.code() == 200 || response.code() == 201) {
                            SharedPrefs.getInstance().put(JWT_TOKEN, response.body().getJwtToken());
                            SharedPrefs.getInstance().put(USER_INFO, response.body().getPatient());
                            FirebaseMessaging.getInstance().subscribeToTopic(response.body().getPatient().getId());
                            LoadDefaultModel.getInstance().loadFavoriteDoctor( response.body().getPatient(), getActivity(), btnSignUp);

                        } else {
                            CommonErrorResponse commonErrorResponse = parseToCommonError(response);
                            if (commonErrorResponse.getError() != null) {
                                String error = Utils.getStringResourceByString(getContext(), commonErrorResponse.getError());
                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                Log.d("RESPONSE", error);
                            }
                            btnSignUp.revertAnimation();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        btnSignUp.revertAnimation();
                        if (finalFile != null) {
                            try {
                                finalFile.delete();
                            } catch (Exception e) {
                            }
                        }
                        if (t instanceof SocketTimeoutException) {
                            Toast.makeText(getActivity(), getResources().getText(R.string.error_timeout), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void enableAll() {
        edFname.setEnabled(true);
        edAddress.setEnabled(true);
        edCormfirmPassword.setEnabled(true);
        edPassword.setEnabled(true);
        edLname.setEnabled(true);
        edLname.setEnabled(true);
    }

    private void disableAll() {
        edFname.setEnabled(false);
        edAddress.setEnabled(false);
        edCormfirmPassword.setEnabled(false);
        edPassword.setEnabled(false);
        edLname.setEnabled(false);
        edLname.setEnabled(false);
    }

    private int getGender() {
        int checked = 1;
        if (rbMale.isChecked()) {
            checked = 1;
        }
        if (rbFmale.isChecked()) {
            checked = 2;
        }
        if (rbOther.isChecked()) {
            checked = 3;
        }
        return checked;
    }

    public CommonErrorResponse parseToCommonError(Response<AuthResponse> response) {
        CommonErrorResponse commonErrorResponse = new CommonErrorResponse();
        Gson gson = new GsonBuilder().create();
        try {
            commonErrorResponse = gson.fromJson(response.errorBody().string(), CommonErrorResponse.class);
        } catch (IOException e) {
            // handle failure to read error
        }
        return commonErrorResponse;
    }

    private boolean onValidate() {
        boolean isValidate = true;
        String fname = edFname.getText().toString();
        String mname = edMname.getText().toString();
        String lname = edLname.getText().toString();
        String password = edPassword.getText().toString();
        String confirmPassword = edCormfirmPassword.getText().toString();
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher;

        if (fname == null || fname.trim().length() == 0) {
            isValidate = false;
            tilFname.setError(getResources().getString(R.string.fname_required));
        } else {
            tilFname.setError(null);
        }

        if (lname == null || lname.trim().length() == 0) {
            isValidate = false;
            tillname.setError(getResources().getString(R.string.lname_required));
        } else {
            tillname.setError(null);
        }

        if (!pattern.matcher(password).matches()) {
            isValidate = false;
            tilPassword.setError(getResources().getString(R.string.password_rule));
        } else {
            tilPassword.setError(null);
        }
        if (!password.equals(confirmPassword)) {
            isValidate = false;
            tilConfirmPassword.setError(getResources().getString(R.string.confirm_password_error));
        } else {
            tilConfirmPassword.setError(null);
        }

        return isValidate;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void updateUI() {
        if (mImageToBeAttached != null) {
            ivAvatar.setImageBitmap(mImageToBeAttached);
        } else {
            ivAvatar.setImageResource(R.drawable.patient_avatar);
        }
    }

    //avatar
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_TAKE_PHOTO) {
            File file = new File(mImagePathToBeAttached);
            if (file.exists()) {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mImagePathToBeAttached, options);
                options.inJustDecodeBounds = false;
                mImageToBeAttached = BitmapFactory.decodeFile(mImagePathToBeAttached, options);
                try {
                    ExifInterface exif = new ExifInterface(mImagePathToBeAttached);
                    String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                    int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
                    int rotationAngle = 0;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
                    Matrix matrix = new Matrix();
                    matrix.setRotate(rotationAngle, (float) mImageToBeAttached.getWidth() / 2, (float) mImageToBeAttached.getHeight() / 2);
                    mImageToBeAttached = Bitmap.createBitmap(mImageToBeAttached, 0, 0, options.outWidth, options.outHeight, matrix, true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                file.delete();
            }
            mImagePathToBeAttached = null;
        } else if (requestCode == REQUEST_CHOOSE_PHOTO) {
            try {

                Uri uri = data.getData();
                ContentResolver resolver = getActivity().getContentResolver();
                int rotationAngle = getOrientation(getActivity(), uri);
                mImageToBeAttached = MediaStore.Images.Media.getBitmap(resolver, uri);
                Matrix matrix = new Matrix();
                matrix.setRotate(rotationAngle, (float) mImageToBeAttached.getWidth() / 2, (float) mImageToBeAttached.getHeight() / 2);
                mImageToBeAttached = Bitmap.createBitmap(mImageToBeAttached, 0, 0, mImageToBeAttached.getWidth(), mImageToBeAttached.getHeight(), matrix, true);
            } catch (IOException e) {
                Log.e(TAG, "Cannot get a selected photo from the gallery.", e);
            }
        }
        updateUI();
    }

    public int getOrientation(Context context, Uri photoUri) {
        /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }


    private void dispatchTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Log.e(TAG, "Cannot create a temp image file", e);
            }

            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getActivity(),
                        BuildConfig.APPLICATION_ID + ".provider", photoFile));
                if (checkPermission()) {
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                } else {
                    requestPermission();
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "TODO_LITE-" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(fileName, ".jpg", storageDir);
        mImagePathToBeAttached = image.getAbsolutePath();
        return image;
    }

    private void dispatchChoosePhotoIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_CHOOSE_PHOTO);
    }



    private void displayAttachImageDialog() {
        CharSequence[] items;
        if (mImageToBeAttached != null)
            items = new CharSequence[]{"Chụp ảnh", "Chọn ảnh", "Xóa ảnh"};
        else
            items = new CharSequence[]{"Chụp ảnh", "Chọn ảnh"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Ảnh đại diện");
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

    private void deleteCurrentPhoto() {
        if (mImageToBeAttached != null) {
            mImageToBeAttached.recycle();
            mImageToBeAttached = null;
            // ivAvatar.setImageResource(R.drawable.patient_avatar);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(getActivity(), "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new
                String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, REQUEST_PERMISSION_CODE);
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                CAMERA);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

}
