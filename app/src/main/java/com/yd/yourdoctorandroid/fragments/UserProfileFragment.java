package com.yd.yourdoctorandroid.fragments;


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
import android.graphics.Paint;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.yd.yourdoctorandroid.BuildConfig;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.managers.AzureImageManager;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.networks.models.AuthResponse;
import com.yd.yourdoctorandroid.networks.models.CommonErrorResponse;
import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.utils.ImageUtils;
import com.yd.yourdoctorandroid.utils.SharedPrefs;
import com.yd.yourdoctorandroid.utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment implements View.OnClickListener {

    public static final String JWT_TOKEN = "JWT_TOKEN";
    public static final String USER_INFO = "USER_INFO";

    public static final int TYPE_EDIT = 1;
    public static final int TYPE_CANCEL = 2;

    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,15})";

    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.iv_upload_avatar)
    CircleImageView ivUploadAvatar;

    @BindView(R.id.tb_main)
    Toolbar tbMain;
    @BindView(R.id.ed_fname)
    EditText edFname;
    @BindView(R.id.ed_mname)
    EditText edMname;
    @BindView(R.id.ed_lname)
    EditText edLname;
    @BindView(R.id.ed_phone)
    EditText edPhone;
    @BindView(R.id.ed_birthday)
    EditText edBirthday;
    @BindView(R.id.ed_address)
    EditText edAddress;
    @BindView(R.id.ed_remainMoney)
    EditText ed_remainMoney;
    @BindView(R.id.rl_mainButton)
    RelativeLayout rlMainButton;
    @BindView(R.id.rl_YesNoButton)
    RelativeLayout rlYesNoButton;

    @BindView(R.id.btn_no)
    Button btnNo;

    @BindView(R.id.btn_yes)
    Button btnYes;

    @BindView(R.id.btn_change_password)
    Button btnChangePassword;

    @BindView(R.id.btn_edit_profile)
    Button btnEditProfile;

    @BindView(R.id.rg_gender)
    RadioGroup groupGender;

    @BindView(R.id.radio_male)
    RadioButton rbMale;
    @BindView(R.id.radio_other)
    RadioButton rbOther;
    @BindView(R.id.radio_female)
    RadioButton rbFmale;


    @BindView(R.id.til_fname)
    TextInputLayout tilFname;
    @BindView(R.id.til_mname)
    TextInputLayout tilMname;
    @BindView(R.id.til_lname)
    TextInputLayout tillname;
    @BindView(R.id.till_remainMoney)
    TextInputLayout till_remainMoney;

    private Unbinder unbinder;

    private boolean isChangeInfo;

    //handle password
    ProgressBar progress_change_password;
    AlertDialog dialogChangePassword;

    //handle image
    ImageUtils imageUtils;
    public static final int REQUEST_PERMISSION_CODE = 1;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_CHOOSE_PHOTO = 2;

    Patient currentPatient;


    public UserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        setupUI(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void setupUI(View view) {

        unbinder = ButterKnife.bind(this, view);
        currentPatient = SharedPrefs.getInstance().get(USER_INFO, Patient.class);
        isChangeInfo = false;

        tbMain.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        tbMain.setTitle(R.string.profile_page);
        tbMain.setTitleTextColor(getResources().getColor(R.color.primary_text));
        tbMain.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenManager.backFragment(getFragmentManager());
            }
        });
        edPhone.setEnabled(false);
        rlMainButton.setVisibility(View.VISIBLE);
        rlYesNoButton.setVisibility(View.GONE);

        btnChangePassword.setOnClickListener(this);
        btnEditProfile.setOnClickListener(this);
        ivUploadAvatar.setOnClickListener(this);
        btnNo.setOnClickListener(this);
        btnYes.setOnClickListener(this);

        imageUtils = new ImageUtils(getActivity());
        setUpCalendar();
        setScreenFunction(TYPE_CANCEL);


    }

//    private void setDataProfile(Patient currentPatient){
//        edFname.setText(currentPatient.getfName());
//        edMname.setText(currentPatient.getmName());
//        edLname.setText(currentPatient.getlName());
//        Picasso.with(getContext()).load(currentPatient.getAvatar().toString()).transform(new CropCircleTransformation()).into(ivAvatar);
//        edPhone.setText(currentPatient.getPhoneNumber());
//        edAddress.setText(currentPatient.getAddress());
//        edBirthday.setText(currentPatient.getBirthday());
//        ed_remainMoney.setText(currentPatient.getRemainMoney() + " đ");
//        switch (currentPatient.getGender()){
//            case 1:{
//                rbMale.setChecked(true);
//                rbFmale.setChecked(false);
//                rbOther.setChecked(false);
//                break;
//            }
//            case 2:{
//                rbMale.setChecked(false);
//                rbFmale.setChecked(true);
//                rbOther.setChecked(false);
//                break;
//            }
//            case 3:{
//                rbMale.setChecked(false);
//                rbFmale.setChecked(false);
//                rbOther.setChecked(true);
//                break;
//            }
//        }
//
//        disableAll();
//    }

    private void setScreenFunction(int type) {
        if (type == TYPE_EDIT) {
            enableAll();
            rlMainButton.setVisibility(View.GONE);
            rlYesNoButton.setVisibility(View.VISIBLE);
        } else if (type == TYPE_CANCEL) {
            isChangeInfo = false;
            edFname.setText(currentPatient.getfName());
            edMname.setText(currentPatient.getmName());
            edLname.setText(currentPatient.getlName());
            Picasso.with(getContext()).load(currentPatient.getAvatar().toString()).transform(new CropCircleTransformation()).into(ivAvatar);
            edPhone.setText(currentPatient.getPhoneNumber());
            edAddress.setText(currentPatient.getAddress());
            edBirthday.setText(currentPatient.getBirthday());
            ed_remainMoney.setText(currentPatient.getRemainMoney() + " đ");
            switch (currentPatient.getGender()) {
                case 1: {
                    rbMale.setChecked(true);
                    rbFmale.setChecked(false);
                    rbOther.setChecked(false);
                    break;
                }
                case 2: {
                    rbMale.setChecked(false);
                    rbFmale.setChecked(true);
                    rbOther.setChecked(false);
                    break;
                }
                case 3: {
                    rbMale.setChecked(false);
                    rbFmale.setChecked(false);
                    rbOther.setChecked(true);
                    break;
                }
            }
            disableAll();
            rlMainButton.setVisibility(View.VISIBLE);
            rlYesNoButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_change_password: {

                break;
            }
            case R.id.iv_upload_avatar: {
                imageUtils.displayAttachImageDialog();
                break;
            }
            case R.id.btn_edit_profile: {
                setScreenFunction(TYPE_EDIT);
                break;
            }
            case R.id.btn_yes: {
                onSubmit();
                break;
            }
            case R.id.btn_no: {
                imageUtils.clearAll();
                setScreenFunction(TYPE_CANCEL);
                break;
            }
        }
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

//    private String uploadImage() throws Exception {
//        Log.d("UPLOAD", "uploadImage");
//        final String imageName = AzureImageManager.randomString(10);
//        try {
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            mImageToBeAttached.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
//            byte[] bitmapdata = bos.toByteArray();
//            final ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
//            final int imageLength = bs.available();
//
//            final Handler handler = new Handler();
//
//            Thread th = new Thread(new Runnable() {
//                public void run() {
//
//                    try {
//                        final String fileName = AzureImageManager.UploadImage(imageName, bs, imageLength);
//                        Log.d("UPLOAD", "Image");
//                    } catch (Exception e) {
//                        Log.d("UPLOAD", e.getMessage());
//                        try {
//                            throw new Exception("error");
//                        } catch (Exception e1) {
//                            e1.printStackTrace();
//                        }
//                    }
//                }
//            });
//            th.start();
//        } catch (Exception ex) {
//            Log.d("UPLOAD", ex.toString());
//            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
//            return null;
//        }
//        return imageName;
//    }

    private void updateBirthDay(Calendar myCalendar) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edBirthday.setText(sdf.format(myCalendar.getTime()));
    }

    private void onSubmit() {
        if (!onValidate()) {
            return;
        }
        String avatar = null;
        onUpdateUser();

    }

    private void onUpdateUser() {
        String fname = edFname.getText().toString();
        String mname = edMname.getText().toString();
        String lname = edLname.getText().toString();
        String birthday = edBirthday.getText().toString();
        String address = edAddress.getText().toString();
        int gender = getGender();
        //Patient patient = new Patient(null, fname, mname, lname, phoneNumber, password, avatar, gender, birthday, address, 1,0, null);
        MultipartBody.Part avatarUpload = null;
        // Map is used to multipart the file using okhttp3.RequestBody
        File file = null;
//        if (mImageToBeAttached != null) {
//            file = Utils.persistImage(mImageToBeAttached, filename, getContext());
//            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
//            avatarUpload = MultipartBody.Part.createFormData("avatar", file.getName(), requestBody);
//        }


        //Log.d("CREATE USER", patient.toString());
//        RegisterPatientService registerPatientService = RetrofitFactory.getInstance().createService(RegisterPatientService.class);
//        final File finalFile = file;
//        registerPatientService.register(avatarUpload, patient)
//                .enqueue(new Callback<AuthResponse>() {
//                    @Override
//                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
//
//                        if (finalFile != null) {
//                            try {
//                                finalFile.delete();
//                            } catch (Exception e) {
//                            }
//                        }
//                        if (response.code() == 200 || response.code() == 201) {
//                            SharedPrefs.getInstance().put(JWT_TOKEN, response.body().getJwtToken());
//                            SharedPrefs.getInstance().put(USER_INFO, response.body().getPatient());
//                            FirebaseMessaging.getInstance().subscribeToTopic(response.body().getPatient().getId());
//                            LoadDefaultModel.getInstance().loadFavoriteDoctor( response.body().getPatient(), getActivity(), btnSignUp);
//
//                        } else {
//                            CommonErrorResponse commonErrorResponse = parseToCommonError(response);
//                            if (commonErrorResponse.getError() != null) {
//                                String error = Utils.getStringResourceByString(getContext(), commonErrorResponse.getError());
//                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
//                                Log.d("RESPONSE", error);
//                            }
//                            btnSignUp.revertAnimation();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<AuthResponse> call, Throwable t) {
//                        btnSignUp.revertAnimation();
//                        if (finalFile != null) {
//                            try {
//                                finalFile.delete();
//                            } catch (Exception e) {
//                            }
//                        }
//                        if (t instanceof SocketTimeoutException) {
//                            Toast.makeText(getActivity(), getResources().getText(R.string.error_timeout), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
    }

    private void enableAll() {
        edFname.setEnabled(true);
        edAddress.setEnabled(true);
        edBirthday.setEnabled(true);
        edLname.setEnabled(true);
        edMname.setEnabled(true);
        ivUploadAvatar.setVisibility(View.VISIBLE);
        rbFmale.setEnabled(true);
        rbMale.setEnabled(true);
        rbOther.setEnabled(true);
    }

    private void disableAll() {
        edFname.setEnabled(false);
        edAddress.setEnabled(false);
        edBirthday.setEnabled(false);
        edLname.setEnabled(false);
        edMname.setEnabled(false);
        ed_remainMoney.setEnabled(false);
        ivUploadAvatar.setVisibility(View.GONE);

        switch (currentPatient.getGender()) {
            case 1: {
                rbFmale.setEnabled(false);
                rbOther.setEnabled(false);
                break;
            }
            case 2: {
                rbMale.setEnabled(false);
                rbOther.setEnabled(false);
                break;
            }
            case 3: {
                rbMale.setEnabled(false);
                rbFmale.setEnabled(false);
                break;
            }
        }

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

//    public CommonErrorResponse parseToCommonError(Response<AuthResponse> response) {
//        CommonErrorResponse commonErrorResponse = new CommonErrorResponse();
//        Gson gson = new GsonBuilder().create();
//        try {
//            commonErrorResponse = gson.fromJson(response.errorBody().string(), CommonErrorResponse.class);
//        } catch (IOException e) {
//            // handle failure to read error
//        }
//        return commonErrorResponse;
//    }

    private boolean onValidate() {
        boolean isValidate = true;
        String fname = edFname.getText().toString();
        String lname = edLname.getText().toString();

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

        return isValidate;

    }

    private boolean onValidatePassword(String oldPassword, String newPassword, String confirmPassword) {
        boolean isValidate = true;
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher;
        if (!oldPassword.equals(currentPatient.getPassword())) {
            //Toast.makeText(getContext(),getResources().getString(R.string.password_rule),Toast.LENGTH_LONG).show();
            return false;
        } else if (!pattern.matcher(newPassword).matches()) {
            Toast.makeText(getContext(), getResources().getString(R.string.password_rule), Toast.LENGTH_LONG).show();
            return false;
        } else if (!newPassword.equals(confirmPassword)) {
            //tilConfirmPassword.setError(getResources().getString(R.string.confirm_password_error));
        }

        return true;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void updateUI() {
        if (imageUtils.getmImageToBeAttached() != null) {
            ivAvatar.setImageBitmap(imageUtils.getmImageToBeAttached());
            isChangeInfo = true;
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
            File file = new File(imageUtils.getmImagePathToBeAttached());
            if (file.exists()) {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(imageUtils.getmImagePathToBeAttached(), options);
                options.inJustDecodeBounds = false;
                imageUtils.setmImageToBeAttached(BitmapFactory.decodeFile(imageUtils.getmImagePathToBeAttached(), options)) ;
                try {
                    ExifInterface exif = new ExifInterface(imageUtils.getmImagePathToBeAttached());
                    String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                    int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
                    int rotationAngle = 0;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
                    Matrix matrix = new Matrix();
                    matrix.setRotate(rotationAngle, (float) imageUtils.getmImageToBeAttached().getWidth() / 2, (float) imageUtils.getmImageToBeAttached().getHeight() / 2);
                    imageUtils.setmImageToBeAttached(Bitmap.createBitmap(imageUtils.getmImageToBeAttached(), 0, 0, options.outWidth, options.outHeight, matrix, true));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                file.delete();
            }
        } else if (requestCode == REQUEST_CHOOSE_PHOTO) {
            try {

                Uri uri = data.getData();
                ContentResolver resolver = getActivity().getContentResolver();
                int rotationAngle = getOrientation(getActivity(), uri);
                imageUtils.setmImageToBeAttached(MediaStore.Images.Media.getBitmap(resolver, uri));
                Matrix matrix = new Matrix();
                matrix.setRotate(rotationAngle, (float) imageUtils.getmImageToBeAttached().getWidth() / 2, (float) imageUtils.getmImageToBeAttached().getHeight() / 2);
                imageUtils.setmImageToBeAttached(Bitmap.createBitmap(imageUtils.getmImageToBeAttached(), 0, 0, imageUtils.getmImageToBeAttached().getWidth(), imageUtils.getmImageToBeAttached().getHeight(), matrix, true));
            } catch (IOException e) {
                Log.e("userProfile", "Cannot get a selected photo from the gallery.", e);
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


//    private void dispatchTakePhotoIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException e) {
//                Log.e(TAG, "Cannot create a temp image file", e);
//            }
//
//            if (photoFile != null) {
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getActivity(),
//                        BuildConfig.APPLICATION_ID + ".provider", photoFile));
//                if (checkPermission()) {
//                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//                } else {
//                    requestPermission();
//                }
//            }
//        }
//    }

//    private File createImageFile() throws IOException {
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String fileName = "TODO_LITE-" + timeStamp + "_";
//        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(fileName, ".jpg", storageDir);
//        mImagePathToBeAttached = image.getAbsolutePath();
//        return image;
//    }

//    private void dispatchChoosePhotoIntent() {
//        Intent intent = new Intent(Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/*");
//        startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_CHOOSE_PHOTO);
//    }

//    private void deleteCurrentPhoto() {
//        if (mImageToBeAttached != null) {
//            mImageToBeAttached.recycle();
//            mImageToBeAttached = null;
//            ivAvatar.setImageResource(R.drawable.patient_avatar);
//        }
//    }

//    private void displayAttachImageDialog() {
//        CharSequence[] items;
//        if (mImageToBeAttached != null)
//            items = new CharSequence[]{"Chụp ảnh", "Chọn ảnh", "Xóa ảnh"};
//        else
//            items = new CharSequence[]{"Chụp ảnh", "Chọn ảnh"};
//
//        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
//        builder.setTitle("Ảnh đại diện");
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if (item == 0) {
//                    dispatchTakePhotoIntent();
//                } else if (item == 1) {
//                    dispatchChoosePhotoIntent();
//                } else {
//                    deleteCurrentPhoto();
//                }
//            }
//        });
//        builder.show();
//    }

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

//    private void requestPermission() {
//        ActivityCompat.requestPermissions(getActivity(), new
//                String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, REQUEST_PERMISSION_CODE);
//    }
//
//    public boolean checkPermission() {
//        int result = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
//                WRITE_EXTERNAL_STORAGE);
//        int result1 = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
//                CAMERA);
//        return result == PackageManager.PERMISSION_GRANTED &&
//                result1 == PackageManager.PERMISSION_GRANTED;
//    }


}
