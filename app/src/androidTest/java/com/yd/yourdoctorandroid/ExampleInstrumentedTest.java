package com.yd.yourdoctorandroid;

import android.content.Context;
import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.utils.SharedPrefs;
import com.yd.yourdoctorandroid.utils.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {


    @Test
    public void useAppContextWrong1() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals(false, appContext.getPackageName() == "com.yd.yourdoctorYD");
    }

    @Test
    public void useAppContextWrong2() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals(false, appContext.getPackageName() == "com.yd.yourdoctorYDDoctor");
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.yd.yourdoctorandroid", appContext.getPackageName());
    }


    @Test
    public void getStringResourceByStringWrong() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals(false, Utils.getStringResourceByString(appContext,"app_name") == "Your Doctor Partner");
    }

    @Test
    public void getStringResourceByStringWrong2() {

        assertEquals("", Utils.getStringResourceByString(null,"app_name"));

    }

    @Test
    public void getStringResourceByStringWrong3() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("", Utils.getStringResourceByString(appContext,"abc"));

    }

    @Test
    public void getStringResourceByStringSuccess() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("Your Doctor", Utils.getStringResourceByString(appContext,"app_name"));
    }

    @Test
    public void getStringResourceByStringVietnameseSuccess2() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("Chuyên môn", Utils.getStringResourceByString(appContext,"specialist"));
    }

    @Test
    public void addIdChatToListTimeOutWrong2() {
        Utils.addIdChatToListTimeOut(null);
        List<String> listChatTimeOut = SharedPrefs.getInstance().get("listChatTimeOutNot", List.class );
        assertEquals(true, listChatTimeOut == null);
        SharedPrefs.getInstance().remove("listChatTimeOutNot");
    }

    @Test
    public void addIdChatToListTimeOutSuccess() {
        String idChat = "56jvsdsbsdvbsjdbs4345";
        Utils.addIdChatToListTimeOut(idChat);
        List<String> listChatTimeOut = SharedPrefs.getInstance().get("listChatTimeOutNot", List.class );
        assertEquals(true, listChatTimeOut.contains(idChat));
        SharedPrefs.getInstance().remove("listChatTimeOutNot");
    }

    @Test
    public void addIdChatToListTimeOutSuccess2() {
        String idChat = "54kdjskvbkdbvdbfvbv";
        Utils.addIdChatToListTimeOut(idChat);
        List<String> listChatTimeOut = SharedPrefs.getInstance().get("listChatTimeOutNot", List.class );
        assertEquals(true, listChatTimeOut.contains(idChat));
        SharedPrefs.getInstance().remove("listChatTimeOutNot");
    }

    @Test
    public void putSharedPreferencesSuccess() {
        SharedPrefs.getInstance().put("Hello", "This is YD App");
        assertEquals("This is YD App", SharedPrefs.getInstance().get("Hello",String.class));

    }

    @Test
    public void putSharedPreferencesSuccess2() {

        SharedPrefs.getInstance().put("Hello", "This is YD App");

        assertEquals("This is YD App", SharedPrefs.getInstance().get("Hello",String.class));

    }

    @Test
    public void putSharedPreferencesObject (){
        Patient patient = new Patient();
        patient.setlName("Anh");
        SharedPrefs.getInstance().put("currentPatient", patient);
        assertEquals(true,
                SharedPrefs.getInstance().get("currentPatient",Patient.class).getlName().equals("Anh"));
        SharedPrefs.getInstance().remove("currentPatient");
    }

    @Test
    public void putSharedPreferencesList(){

        List<String> listID = new ArrayList<>();
        listID.add("a");
        listID.add("b");
        SharedPrefs.getInstance().put("listID", listID);
        assertEquals(true,
                SharedPrefs.getInstance().get("listID",List.class).size() == 2);
        SharedPrefs.getInstance().remove("currentPatient");

    }

    @Test
    public void removeSharedPreferences() {
        SharedPrefs.getInstance().remove("listChatTimeOutNot");

        List<String> listChatTimeOut = SharedPrefs.getInstance().get("listChatTimeOutNot", List.class );

        assertEquals(true, listChatTimeOut == null);
    }

    @Test
    public void useAppContextWithNameWrong() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals(false, appContext.getPackageName() == "com.yd.yourdoctorYD");
    }

    @Test
    public void useAppContextWithNameWrong2() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals(false, appContext.getPackageName() == "com.yd.yourdoctorYDDoctor");
    }

    @Test
    public void useAppContextWithName() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.yd.yourdoctorandroid", appContext.getPackageName());
    }


    @Test
    public void getStringResourceWrong() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals(false, Utils.getStringResourceByString(appContext,"app_name") == "Your Doctor Partner");
    }

    @Test
    public void getStringResourceWrong2() {

        assertEquals("", Utils.getStringResourceByString(null,"app_name"));

    }

    @Test
    public void getStringResourceWrong3() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("", Utils.getStringResourceByString(appContext,"abc"));

    }

    @Test
    public void getStringResourceByStringSuccess1() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("Your Doctor", Utils.getStringResourceByString(appContext,"app_name"));
    }

    @Test
    public void getStringResourceByStringVietnameseSuccess3() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("Chuyên môn", Utils.getStringResourceByString(appContext,"specialist"));
    }

    public static void addIdChatToListTimeOutWithName(String idChat){
        if(idChat == null || idChat.isEmpty()) return;
        List<String> listChatTimeOut = SharedPrefs.getInstance().get("listChatTimeOutNot", List.class );
        if(listChatTimeOut == null){
            listChatTimeOut = new ArrayList<>();
            listChatTimeOut.add(idChat);
            SharedPrefs.getInstance().put("listChatTimeOutNot", listChatTimeOut);
        }else {
            if(!listChatTimeOut.contains(idChat)){
                listChatTimeOut.add(idChat);
                SharedPrefs.getInstance().put("listChatTimeOutNot", listChatTimeOut);
            }
        }
    }

    @Test
    public void addIdChatToListTimeOutWithNameWrong2() {
        Utils.addIdChatToListTimeOut(null);
        List<String> listChatTimeOut = SharedPrefs.getInstance().get("listChatTimeOutNot", List.class );
        assertEquals(true, listChatTimeOut == null);
        SharedPrefs.getInstance().remove("listChatTimeOutNot");
    }

    @Test
    public void addIdChatToListTimeOutWithNameSuccess() {
        String idChat = "56jvsdsbsdvbsjdbs4345";
        Utils.addIdChatToListTimeOut(idChat);
        List<String> listChatTimeOut = SharedPrefs.getInstance().get("listChatTimeOutNot", List.class );
        assertEquals(true, listChatTimeOut.contains(idChat));
        SharedPrefs.getInstance().remove("listChatTimeOutNot");
    }

    @Test
    public void addIdChatToListTimeOutWithNameSuccess2() {
        String idChat = "54kdjskvbkdbvdbfvbv";
        Utils.addIdChatToListTimeOut(idChat);
        List<String> listChatTimeOut = SharedPrefs.getInstance().get("listChatTimeOutNot", List.class );
        assertEquals(true, listChatTimeOut.contains(idChat));
        SharedPrefs.getInstance().remove("listChatTimeOutNot");
    }

    @Test
    public void putSharedPreferencesWithNameSuccess() {
        SharedPrefs.getInstance().put("Hello", "This is YD App");
        assertEquals("This is YD App", SharedPrefs.getInstance().get("Hello",String.class));

    }

    @Test
    public void putSharedPreferencesWithNameSuccess2() {

        SharedPrefs.getInstance().put("Hello", "This is YD App");

        assertEquals("This is YD App", SharedPrefs.getInstance().get("Hello",String.class));

    }

    @Test
    public void putSharedPreferencesWithNameObject (){
        Patient patient = new Patient();
        patient.setlName("Anh");
        SharedPrefs.getInstance().put("currentPatient", patient);
        assertEquals(true,
                SharedPrefs.getInstance().get("currentPatient",Patient.class).getlName().equals("Anh"));
        SharedPrefs.getInstance().remove("currentPatient");
    }

    @Test
    public void putSharedPreferencesListWithName(){

        List<String> listID = new ArrayList<>();
        listID.add("a");
        listID.add("b");
        SharedPrefs.getInstance().put("listID", listID);
        assertEquals(true,
                SharedPrefs.getInstance().get("listID",List.class).size() == 2);
        SharedPrefs.getInstance().remove("currentPatient");

    }

    @Test
    public void removeSharedPreferencesWithName() {
        SharedPrefs.getInstance().remove("listChatTimeOutNot");

        List<String> listChatTimeOut = SharedPrefs.getInstance().get("listChatTimeOutNot", List.class );

        assertEquals(true, listChatTimeOut == null);
    }


}
