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
import android.graphics.PorterDuff;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
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
import android.widget.TextView;
import android.widget.Toast;

import com.yd.yourdoctorandroid.BuildConfig;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.activities.AuthActivity;
import com.yd.yourdoctorandroid.activities.MainActivity;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.changePassword.ChangePasswordService;
import com.yd.yourdoctorandroid.networks.changePassword.PasswordRequest;
import com.yd.yourdoctorandroid.networks.changePassword.PasswordResponse;
import com.yd.yourdoctorandroid.networks.changeProfile.ChangeProfilePaitentService;
import com.yd.yourdoctorandroid.networks.changeProfile.PatientRequest;
import com.yd.yourdoctorandroid.networks.changeProfile.PatientResponse;
import com.yd.yourdoctorandroid.networks.getLinkImageService.GetLinkeImageService;
import com.yd.yourdoctorandroid.networks.getLinkImageService.MainGetLink;
import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.utils.ImageUtils;
import com.yd.yourdoctorandroid.utils.SharedPrefs;
import com.yd.yourdoctorandroid.utils.Utils;
import com.yd.yourdoctorandroid.utils.ZoomImageViewUtils;

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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
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
public class UserProfileFragment extends Fragment implements View.OnClickListener {

    public static final String JWT_TOKEN = "JWT_TOKEN";
    public static final String USER_INFO = "USER_INFO";

    public static final int TYPE_EDIT = 1;
    public static final int TYPE_CANCEL = 2;

    public static final String TAG = "UserProfileFragment";
    public static final int REQUEST_PERMISSION_CODE = 1;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_CHOOSE_PHOTO = 2;
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
    @BindView(R.id.tv_remainMoney)
    TextView tv_remainMoney;
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

    @BindView(R.id.pbProfilePatient)
    ProgressBar pbProfilePatient;
    @BindView(R.id.til_full_name)
    TextInputLayout tilFullname;

    private Unbinder unbinder;

    private boolean isChangeInfo;

    //handle password
    EditText et_old_password;
    EditText et_new_password;
    EditText et_confirm_new_password;
    TextView tv_message_change_password;
    ProgressBar progress_change_password;
    AlertDialog dialogChangePassword;

    //handle image
    private String mImagePathToBeAttached;
    private Bitmap mImageToBeAttached;
    private String filename;

    //Current Patient
    Patient currentPatient;


    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        setupUI(view);
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
        // can not edit phone, and remain money
        edPhone.setEnabled(false);
        tv_remainMoney.setEnabled(false);

        //set on click
        btnChangePassword.setOnClickListener(this);
        btnEditProfile.setOnClickListener(this);
        ivUploadAvatar.setOnClickListener(this);
        btnNo.setOnClickListener(this);
        btnYes.setOnClickListener(this);

        setUpCalendar();
        setScreenFunction(TYPE_CANCEL);
    }

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
            ZoomImageViewUtils.loadCircleImage(getContext(), currentPatient.getAvatar().toString(), ivAvatar);
            edPhone.setText(currentPatient.getPhoneNumber());
            edAddress.setText(currentPatient.getAddress());
            edBirthday.setText(currentPatient.getBirthday());
            tv_remainMoney.setText(currentPatient.getRemainMoney() + " đ");
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
        mImageToBeAttached = null;
        tilFullname.setError(null);
        pbProfilePatient.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_change_password: {
                showDialogChangePassword();
                break;
            }
            case R.id.iv_upload_avatar: {
                //imageUtils.displayAttachImageDialog();
                displayAttachImageDialog();
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
                setScreenFunction(TYPE_CANCEL);
                break;
            }
        }
    }

    private void showDialogChangePassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.change_password_dialog, null);
        et_old_password = view.findViewById(R.id.et_old_password);
        et_new_password = view.findViewById(R.id.et_new_password);
        et_confirm_new_password = view.findViewById(R.id.et_confirm_new_password);
        et_old_password.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        et_new_password.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        et_confirm_new_password.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        tv_message_change_password = view.findViewById(R.id.tv_message_change_password);
        progress_change_password = view.findViewById(R.id.progress_change_password);
        builder.setView(view);
        builder.setTitle("Thay đổi mật khẩu");

        builder.setPositiveButton("Thay đổi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogChangePassword = builder.create();
        dialogChangePassword.show();
        dialogChangePassword.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onValidatePassword(et_new_password.getText().toString(), et_confirm_new_password.getText().toString(), tv_message_change_password)) {
                    pbProfilePatient.setVisibility(View.VISIBLE);
                    PasswordRequest passwordRequest = new PasswordRequest();
                    passwordRequest.setId(currentPatient.getId());
                    passwordRequest.setOldPassword(et_old_password.getText().toString());
                    passwordRequest.setNewPassword(et_new_password.getText().toString());

                    Log.e(TAG, SharedPrefs.getInstance().get(JWT_TOKEN, String.class));

                    ChangePasswordService changePasswordService = RetrofitFactory.getInstance().createService(ChangePasswordService.class);
                    changePasswordService.changePasswordService(SharedPrefs.getInstance().get(JWT_TOKEN, String.class), passwordRequest).enqueue(new Callback<PasswordResponse>() {
                        @Override
                        public void onResponse(Call<PasswordResponse> call, Response<PasswordResponse> response) {
                            tv_message_change_password.setVisibility(View.VISIBLE);
                            PasswordResponse passwordResponse = response.body();
                            if (response.code() == 200 && passwordResponse.isChangePasswordSuccess()) {
                                tv_message_change_password.setText(passwordResponse.getMessage().toString());
                                tv_message_change_password.setTextColor(getResources().getColor(R.color.colorPrimary));
                                et_old_password.setText("");
                                et_new_password.setText("");
                                et_confirm_new_password.setText("");
                            } else if(response.code() == 401) {
                                Utils.backToLogin(getContext());

                            }else {
                                tv_message_change_password.setTextColor(getResources().getColor(R.color.red));
                                tv_message_change_password.setText("Không thể thay đổi mật khẩu do lỗi máy chủ!");
                            }
                            pbProfilePatient.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<PasswordResponse> call, Throwable t) {
                            tv_message_change_password.setVisibility(View.VISIBLE);
                            tv_message_change_password.setText("Không kết nối được với máy chủ!");
                            pbProfilePatient.setVisibility(View.GONE);

                        }
                    });
                }

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

    private void updateBirthDay(Calendar myCalendar) {
        if (myCalendar.getTimeInMillis() >= (Calendar.getInstance().getTimeInMillis())) {
            Toast.makeText(getContext(), "Bạn không thể chọn ngày sinh của bạn sau thời gian hiện tại", Toast.LENGTH_LONG).show();
        } else {
            String myFormat = "dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            edBirthday.setText(sdf.format(myCalendar.getTime()));
        }
    }

    private void onSubmit() {
        String fname = edFname.getText().toString();
        String mname = edMname.getText().toString();
        String lname = edLname.getText().toString();
        String birthday = edBirthday.getText().toString();
        String address = edAddress.getText().toString();
        int gender = getGender();

        if (!currentPatient.getfName().equals(fname)) isChangeInfo = true;
        if (!currentPatient.getmName().equals(mname)) isChangeInfo = true;
        if (!currentPatient.getlName().equals(lname)) isChangeInfo = true;
        if (currentPatient.getGender() != gender) isChangeInfo = true;
        if (!currentPatient.getBirthday().equals(birthday)) isChangeInfo = true;
        if (!currentPatient.getAddress().equals(address)) isChangeInfo = true;

        if (!isChangeInfo) {
            Toast.makeText(getContext(), "Bạn không thay đổi bất kỳ thông tin nào!", Toast.LENGTH_LONG).show();
            return;
        } else if (onValidate()) {
            Patient newPatient = currentPatient;
            newPatient.setfName(fname);
            newPatient.setmName(mname);
            newPatient.setlName(lname);
            newPatient.setAddress(address);
            newPatient.setBirthday(birthday);
            newPatient.setGender(gender);
            onUpdateUser(newPatient);
        }

    }

    private void onUpdateUser(final Patient newPatient) {
        pbProfilePatient.setVisibility(View.VISIBLE);
        if (mImageToBeAttached != null) {
            GetLinkeImageService getLinkeImageService = RetrofitFactory.getInstance().createService(GetLinkeImageService.class);
            getLinkeImageService.uploadImageToGetLink(SharedPrefs.getInstance().get("JWT_TOKEN", String.class),getImageUpload()).enqueue(new Callback<MainGetLink>() {
                @Override
                public void onResponse(Call<MainGetLink> call, Response<MainGetLink> response) {

                    if (response.code() == 200) {
                        updatePatientNetWork(newPatient, response.body().getFilePath());
                    } else {
                        Toast.makeText(getContext(), "Không thể kết nối máy chủ", Toast.LENGTH_LONG).show();
                        pbProfilePatient.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<MainGetLink> call, Throwable t) {
                    Toast.makeText(getContext(), "Không thể kết nối máy chủ", Toast.LENGTH_LONG).show();
                    pbProfilePatient.setVisibility(View.GONE);
                }
            });
        } else {
            updatePatientNetWork(newPatient, null);
        }

    }

    private void updatePatientNetWork(Patient newPatient, String linkImage) {
        PatientRequest patientRequest;
        if (linkImage == null) {
            patientRequest = new PatientRequest(newPatient.getId(), newPatient.getfName(), newPatient.getmName()
                    , newPatient.getlName(), newPatient.getBirthday(), newPatient.getAddress(),
                    newPatient.getAvatar(), newPatient.getGender());
        } else {
            patientRequest = new PatientRequest(newPatient.getId(), newPatient.getfName(), newPatient.getmName()
                    , newPatient.getlName(), newPatient.getBirthday(), newPatient.getAddress(),
                    linkImage, newPatient.getGender());
        }
        ChangeProfilePaitentService changeProfilePaitentService = RetrofitFactory.getInstance().createService(ChangeProfilePaitentService.class);
        changeProfilePaitentService.changeProfilePaitentService(SharedPrefs.getInstance().get(JWT_TOKEN, String.class), patientRequest).enqueue(new Callback<PatientResponse>() {
            @Override
            public void onResponse(Call<PatientResponse> call, Response<PatientResponse> response) {
                if (response.code() == 200) {
                    PatientResponse patientResponse = response.body();
                    if (patientResponse != null) {
                        currentPatient.setfName(patientResponse.getUpdateSuccess().getFirstName());
                        currentPatient.setmName(patientResponse.getUpdateSuccess().getMiddleName());
                        currentPatient.setlName(patientResponse.getUpdateSuccess().getLastName());
                        currentPatient.setGender(patientResponse.getUpdateSuccess().getGender());
                        currentPatient.setAddress(patientResponse.getUpdateSuccess().getAddress());
                        currentPatient.setAvatar(patientResponse.getUpdateSuccess().getAvatar());
                        currentPatient.setBirthday(patientResponse.getUpdateSuccess().getBirthday());
                        SharedPrefs.getInstance().put(USER_INFO, currentPatient);
                        Toast.makeText(getContext(), "Chỉnh sửa thành công", Toast.LENGTH_LONG).show();
                        setScreenFunction(TYPE_CANCEL);
                    }
                } else {
                    Toast.makeText(getContext(), "Kết nối máy chủ không thành công", Toast.LENGTH_LONG).show();
                }
                pbProfilePatient.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<PatientResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Kết nối máy chủ không thành công", Toast.LENGTH_LONG).show();
                pbProfilePatient.setVisibility(View.GONE);
            }
        });
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

    private boolean onValidate() {

        String fname = edFname.getText().toString();
        String lname = edLname.getText().toString();

        if (fname == null || fname.trim().length() == 0) {
            tilFullname.setError(getResources().getString(R.string.fname_required));
            return false;
        }

        if (lname == null || lname.trim().length() == 0) {
            tilFullname.setError(getResources().getString(R.string.lname_required));
            return false;
        }

        tilFullname.setError(null);

        return true;
    }

    private boolean onValidatePassword(String newPassword, String confirmPassword, TextView tv_message_change_password) {

        boolean isValidate = true;
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        if (!pattern.matcher(newPassword).matches()) {
            tv_message_change_password.setText(getResources().getString(R.string.password_rule));
            tv_message_change_password.setVisibility(View.VISIBLE);
            isValidate = false;
        } else if (!newPassword.equals(confirmPassword)) {
            tv_message_change_password.setText(getResources().getString(R.string.confirm_password_error));
            tv_message_change_password.setVisibility(View.VISIBLE);
            isValidate = false;
        } else {
            tv_message_change_password.setVisibility(View.GONE);
        }
        return isValidate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void displayAttachImageDialog() {
        CharSequence[] items;
        if (mImageToBeAttached != null)
            items = new CharSequence[]{"Chụp ảnh", "Chọn ảnh", "Xóa ảnh"};
        else
            items = new CharSequence[]{"Chụp ảnh", "Chọn ảnh"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
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

    private void deleteCurrentPhoto() {
        if (mImageToBeAttached != null) {
            mImageToBeAttached.recycle();
            mImageToBeAttached = null;
            ZoomImageViewUtils.loadCircleImage(getContext(), currentPatient.getAvatar(), ivAvatar);
        }
    }

    public MultipartBody.Part getImageUpload() {

        filename = UUID.randomUUID().toString();
        if (mImageToBeAttached != null) {
            File file = Utils.persistImage(mImageToBeAttached, filename, getContext());
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            return MultipartBody.Part.createFormData("imageChat", file.getName(), requestBody);
        }
        return null;
    }

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


    public void updateUI() {
        if (mImageToBeAttached != null) {
            Log.e("UserProfileImage", "not null");
            ivAvatar.setImageBitmap(mImageToBeAttached);
            isChangeInfo = true;
        } else {
            Log.e("UserProfileImage", "is null");
            ivAvatar.setImageResource(R.drawable.patient_avatar);
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
                        Toast.makeText(getContext(), "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

}
