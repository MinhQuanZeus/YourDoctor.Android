package com.yd.yourdoctorandroid;

import android.content.Context;
import android.util.Log;

import com.yd.yourdoctorandroid.utils.SharedPrefs;
import com.yd.yourdoctorandroid.utils.Utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void handleImageFailed(){

        String testString = "<image src='yourdoctor.png'>";

        assertEquals(false, Utils.hanleStringImage(testString) == "yourdoctor.png");
    }

    @Test
    public void handleImageFailed1(){

        assertEquals(true, Utils.hanleStringImage("abchgdj") == "abchgdj");
    }

    @Test
    public void handleImageFailed2(){

        assertEquals(true, Utils.hanleStringImage(null) == "");
    }

    @Test
    public void handleImageSuccess(){

        String testString = "<img src='yourdoctor.png'>";

        assertEquals("yourdoctor.png", Utils.hanleStringImage(testString));
    }

    @Test
    public void handleImageSuccess1(){

        String testString = "<img data-original='yourdoctor.jpg'>";

        assertEquals("yourdoctor.jpg", Utils.hanleStringImage(testString));
    }

    @Test
    public void handleImageSuccess2(){

        String testString = "<img data-original='yourdoctor.jpg'>";

        assertEquals("yourdoctor.jpg", Utils.hanleStringImage(testString));
    }
    @Test
    public void handleImageSuccess4(){

        String testString = "<img src='yourdoctor.jpg'>";

        assertEquals("yourdoctor.jpg", Utils.hanleStringImage(testString));
    }

    @Test
    public void handleStringDescriptionFailed(){

        String testString = "Yourdoctor is application for advisory heath";

        assertEquals(testString, Utils.handleStringDescription(testString));
    }

    @Test
    public void handleStringDescriptionFailed2(){
        assertEquals("", Utils.handleStringDescription(null));
    }

    @Test
    public void handleStringDescriptionFailed3(){

        String testString = "";

        assertEquals("", Utils.handleStringDescription(testString));
    }

    @Test
    public void handleStringDescriptionSuccess(){

        String testString = "Yourdoctor is application for advisory heath</br>YourDoctor";

        assertEquals("YourDoctor", Utils.handleStringDescription(testString));
    }

    @Test
    public void handleStringDescriptionSuccess1(){

        String testString = "Yourdoctor is application for advisory heath</br>YourDoctor2";

        assertEquals("YourDoctor2", Utils.handleStringDescription(testString));
    }

    @Test
    public void handleStringDescriptionSuccess2(){

        String testString = "Yourdoctor is application for advisory heath</br>";

        assertEquals("", Utils.handleStringDescription(testString));
    }


    @Test
    public void convertTimeWrong() {

        assertEquals(false, Utils.convertTime(153529235) == "07:00, 01/01");

    }

    @Test
    public void convertTimeStart() {

        assertEquals("07:00, 01/01", Utils.convertTime(0));
    }

    @Test
    public void convertTimeSuccess() {

        assertEquals("01:38, 03/01", Utils.convertTime(153529235));

    }

    @Test
    public void convertTimeSuccess2() {

        assertEquals("01:38, 03/01", Utils.convertTime(153529978));

    }

    @Test
    public void convertTimeSucess3() {

        assertEquals("06:59, 01/01", Utils.convertTime(-1));
    }

    @Test
    public void getResourseStringFailed() {

        assertEquals("", Utils.getStringResourceByString(null, "logo_text_menu_advisory"));
    }

    @Test
    public void getFormatMoneyWrong() {

        assertEquals(false, Utils.formatStringNumber(12345678) == "123.456.78");

    }

    @Test
    public void getFormatMoneySuccess() {

        assertEquals("-1", Utils.formatStringNumber(-1));

    }

    @Test
    public void getFormatMoneySuccess1() {

        assertEquals("12.345", Utils.formatStringNumber(12345));

    }

    @Test
    public void getFormatMoneySucess2() {

        assertEquals("0", Utils.formatStringNumber(0));

    }

    @Test
    public void getFormatMoneySuccess3() {

        assertEquals("123.456.789", Utils.formatStringNumber(123456789));

    }

    @Test
    public void verifyNameFailed() {

        assertEquals(false, Utils.verifyVietnameesName(""));

    }

    @Test
    public void verifyNameFailed1() {

        assertEquals(false, Utils.verifyVietnameesName("09Anh"));

    }

    @Test
    public void verifyNameFailed2() {

        assertEquals(false, Utils.verifyVietnameesName("09 Anh"));

    }

    @Test
    public void verifyNameSuccess() {

        assertEquals(true, Utils.verifyVietnameesName("Anh"));

    }

    @Test
    public void verifyNameSuccess1() {

        assertEquals(true, Utils.verifyVietnameesName("Lê"));

    }

    @Test
    public void verifyNameSuccess2() {

        assertEquals(true, Utils.verifyVietnameesName("Thế"));

    }

    @Test
    public void handleImageCheckTypeWrong(){

        String testString = "<image src='yourdoctor.png'>";

        assertEquals(false, Utils.hanleStringImage(testString) == "yourdoctor.png");
    }

    @Test
    public void handleImageCheckTypeWrong1(){

        assertEquals(true, Utils.hanleStringImage("abchgdj") == "abchgdj");
    }

    @Test
    public void handleImageCheckTypeWrong2(){

        assertEquals(true, Utils.hanleStringImage(null) == "");
    }

    @Test
    public void handleImageCheckTypeSucess(){

        String testString = "<img src='yourdoctor.png'>";

        assertEquals("yourdoctor.png", Utils.hanleStringImage(testString));
    }

    @Test
    public void handleImageCheckTypeSucess1(){

        String testString = "<img data-original='yourdoctor.jpg'>";

        assertEquals("yourdoctor.jpg", Utils.hanleStringImage(testString));
    }

    @Test
    public void handleImageCheckTypeSucess2(){

        String testString = "<img data-original='yourdoctor.jpg'>";

        assertEquals("yourdoctor.jpg", Utils.hanleStringImage(testString));
    }
    @Test
    public void handleImageCheckTypeSucess3(){

        String testString = "<img src='yourdoctor.jpg'>";

        assertEquals("yourdoctor.jpg", Utils.hanleStringImage(testString));
    }

    @Test
    public void handleStringDescriptionWithFailed(){

        String testString = "Yourdoctor is application for advisory heath";

        assertEquals(testString, Utils.handleStringDescription(testString));
    }

    @Test
    public void handleStringDescriptionWithFailed2(){
        assertEquals("", Utils.handleStringDescription(null));
    }

    @Test
    public void handleStringDescriptionWithFailed3(){

        String testString = "";

        assertEquals("", Utils.handleStringDescription(testString));
    }

    @Test
    public void handleStringDescriptionWithFailed4(){

        String testString = "Yourdoctor is application for advisory heath</br>YourDoctor";

        assertEquals("YourDoctor", Utils.handleStringDescription(testString));
    }

    @Test
    public void handleStringDescriptionWithFailed5(){

        String testString = "Yourdoctor is application for advisory heath</br>YourDoctor2";

        assertEquals("YourDoctor2", Utils.handleStringDescription(testString));
    }

    @Test
    public void handleStringDescriptionWithSuccess(){

        String testString = "Yourdoctor is application for advisory heath</br>";

        assertEquals("", Utils.handleStringDescription(testString));
    }


    @Test
    public void convertTimeWithLongWrong() {

        assertEquals(false, Utils.convertTime(153529235) == "07:00, 01/01");

    }

    @Test
    public void convertTimeWithLongWrong2() {

        assertEquals("07:00, 01/01", Utils.convertTime(0));
    }

    @Test
    public void convertTimeWithLongSuccess() {

        assertEquals("01:38, 03/01", Utils.convertTime(153529235));

    }

    @Test
    public void convertTimeWithLongSuccess2() {

        assertEquals("01:38, 03/01", Utils.convertTime(153529978));

    }

    @Test
    public void convertTimeWithLongSuccess3() {

        assertEquals("06:59, 01/01", Utils.convertTime(-1));
    }

    @Test
    public void getResourseStringContexNull() {

        assertEquals("", Utils.getStringResourceByString(null, "logo_text_menu_advisory"));
    }

    @Test
    public void getFormatMoneyWithGermanTypeWrong() {

        assertEquals(false, Utils.formatStringNumber(12345678) == "123.456.78");

    }

    @Test
    public void getFormatMoneyWithGermanTypeSuccess() {

        assertEquals("-1", Utils.formatStringNumber(-1));

    }

    @Test
    public void getFormatMoneyWithGermanTypeSuccess2() {

        assertEquals("12.345", Utils.formatStringNumber(12345));

    }

    @Test
    public void getFormatMoneyWithGermanTypeSuccess3() {

        assertEquals("0", Utils.formatStringNumber(0));

    }

    @Test
    public void getFormatMoneyWithGermanTypeSuccess4() {

        assertEquals("123.456.789", Utils.formatStringNumber(123456789));

    }

    @Test
    public void verifyNameWithVietNameseFailed() {

        assertEquals(false, Utils.verifyVietnameesName(""));

    }

    @Test
    public void verifyNameWithVietNameseFailed2() {

        assertEquals(false, Utils.verifyVietnameesName("09Anh"));

    }

    @Test
    public void verifyNameWithVietNameseFailed3() {

        assertEquals(false, Utils.verifyVietnameesName("09 Anh"));

    }

    @Test
    public void verifyNameWithVietNameseSuccess() {

        assertEquals(true, Utils.verifyVietnameesName("Anh"));

    }

    @Test
    public void verifyNameWithVietNameseSuccess2() {

        assertEquals(true, Utils.verifyVietnameesName("Lê"));

    }

    @Test
    public void verifyNameWithVietNameseSuccess3() {

        assertEquals(true, Utils.verifyVietnameesName("Thế"));

    }


}