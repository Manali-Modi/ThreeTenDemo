package com.example.threetendemo.activity;

import android.os.Bundle;
import android.util.Log;

import com.example.threetendemo.R;
import com.example.threetendemo.databinding.ActivityMainBinding;

import org.threeten.bp.Clock;
import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.Duration;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.Month;
import org.threeten.bp.MonthDay;
import org.threeten.bp.Period;
import org.threeten.bp.Year;
import org.threeten.bp.YearMonth;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.chrono.Chronology;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.TemporalAccessor;

import java.util.Date;
import java.util.GregorianCalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DateTime";
    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        basicClasses();
        convertUTCToZone();
        convertZoneToUTC(); //has internal conversion Zone->Local & Local->Zone
        convertStringToLocalOrZone();
        convertLocalToString();
        getSeparateFields();
        inputDateWithZoneId();
        inputDateWithZoneOffset();
        inputDateTimeWithFractionOrNot();
        inputDateTimeOtherFormats(); //Not Standard
        inputDateInMillis();
    }

    private void inputDateInMillis() {
        String TAG = "InMillis";
        long millis = 1569494827;

        Instant instant = Instant.ofEpochMilli(millis);
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
        Log.d(TAG, "To instant: " + instant);
        Log.d(TAG, "To local: " + ldt);
        Log.d(TAG, "To zone: " + zdt);
    }

    private void inputDateTimeOtherFormats() {
        String TAG = "OtherFormats";
        String date1 = "2020-03-22 05:06:07.000 AM";//yyyy-MM-dd hh:mm:ss.SSS a
        String date2 = "2020-03-22+05:30";//yyyy-MM-ddXXX
        String date3 = "2020-03-22 AD";//yyyy-MM-dd G
        String date4 = "2020-03-22";//yyyy-MM-dd
        String date5 = "22 Mar, 2021";//dd EEE, yyyy
        String date6 = "05:06:07.089";//HH:mm:ss.SSS
        //Or many more possible here

        //take format of either date1, date2, date3, date4, date5 or date6 as per input
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.SSS a");

        //Directly convert to zone not possible if don't have any info of zone. So, first convert it into local and then to zone as per convertZoneToUTC()
        LocalDateTime ldt1 = LocalDateTime.parse(date1, formatter1);

        String output1 = ldt1.format(DateTimeFormatter.ofPattern("EEE dd MMM yyyy"));//Sun 22 Mar 2020
        String output2 = ldt1.format(DateTimeFormatter.ofPattern("D Q YYYY"));//DayOfYear-Quarter-WeekBasedYear -> 82 1 2020
        Log.d(TAG, "\n" + date1 + "-->" + output1);
        Log.d(TAG, "\n" + date1 + "-->" + output2);
    }

    private void inputDateTimeWithFractionOrNot() {
        String TAG = "DateTimeFormats";
        String date1 = "2021-05-28T07:16:12.526";//yyyy-MM-dd'T'HH:mm:ss.SSS
        String date2 = "2021-05-28T07:16:12.526Z";//yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
        String date3 = "2021-05-28T07:16:12";//yyyy-MM-dd'T'HH:mm:ss

        //take format of either date1, date2, date3 or date4 as per input
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        //To convert into zone, For date 1 or date 3 refer convertZoneToUTC()
        //And for date2 refer convertUTCToZone()
        LocalDateTime ldt1 = LocalDateTime.parse(date1, formatter1);

        //take any other output format or as much as want
        String output1 = ldt1.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));//2020-03-22
        Log.d(TAG, "\n" + date1 + "-->" + output1);
    }

    private void inputDateWithZoneOffset() {
        String TAG = "DateWithZoneOffset";
        String date1 = "2020-03-22T05:06:07.000+05:30";//yyyy-MM-dd'T'HH:mm:ss.SSSXXX
        String date2 = "2020-03-22T05:06:07.000+0530";//yyyy-MM-dd'T'HH:mm:ss.SSSXX
        String date3 = "2020-03-22T05:06:07+05:30";//yyyy-MM-dd'T'HH:mm:ssXXX
        String date4 = "2020-03-22T05:06:07+0530";//yyyy-MM-dd'T'HH:mm:ssXX

        //take format of either date1, date2, date3 or date4 as per input
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //take either local or zone as per requirement
        LocalDateTime ldt1 = LocalDateTime.parse(date1, formatter1);
        ZonedDateTime zdt = ZonedDateTime.parse(date1, formatter1);

        //take any other output format or as much as want
        String output1 = ldt1.format(DateTimeFormatter.ofPattern("yyyy-MM-dd G"));//2020-03-22 AD
        String output2 = zdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'['VV']'"));//2020-03-22T05:06:07.000[+05:30]
        String output3 = zdt.format(DateTimeFormatter.ofPattern("dd MMMM yyyy z"));//22 March 2020 +05:30
        Log.d(TAG, "\n" + date1 + "-->" + output1);
        Log.d(TAG, "\n" + date1 + "-->" + output2);
        Log.d(TAG, "\n" + date1 + "-->" + output3);
    }

    private void inputDateWithZoneId() {
        String TAG = "DateWithZoneId";
        String date1 = "2020-03-22T05:06:07.000[Asia/Kolkata]"; //yyyy-MM-dd'T'HH:mm:ss.SSS'['VV']'
        String date2 = "2020-03-22T05:06:07.000+05:30[Asia/Kolkata]"; //yyyy-MM-dd'T'HH:mm:ss.SSSXXX'['VV']'
        String date3 = "2020-03-22T05:06:07+05:30[Asia/Kolkata]"; //yyyy-MM-dd'T'HH:mm:ssXXX'['VV']'

        //take format of either date1, date2 or date3 as per input
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'['VV']'");

        //take either local or zone as per requirement
        LocalDateTime ldt1 = LocalDateTime.parse(date1, formatter1);
        ZonedDateTime zdt = ZonedDateTime.parse(date1, formatter1);

        //take any other output format or as much as want
        String output1 = ldt1.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.SSS a"));//2020-03-22 05:06:07.000 AM
        String output2 = zdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXX"));//2020-03-22T05:06:07+0530
        String output3 = zdt.format(DateTimeFormatter.ofPattern("dd MMMM yyyy z"));//22 March 2020 IST
        Log.d(TAG, "\n" + date1 + "-->" + output1);
        Log.d(TAG, "\n" + date1 + "-->" + output2);
        Log.d(TAG, "\n" + date1 + "-->" + output3);
    }

    private void getSeparateFields() {
        String input = "2021-05-27T16:05:15.598+05:30[Asia/Kolkata]";

        //Also possible using local
        ZonedDateTime zdt = ZonedDateTime.parse(input);

        int year = zdt.getYear();
        Month month = zdt.getMonth();
        int day = zdt.getDayOfMonth();
        int hour = zdt.getHour();
        int min = zdt.getMinute();
        int sec = zdt.getSecond();
        DayOfWeek dow = zdt.getDayOfWeek();
        int doy = zdt.getDayOfYear();
        ZoneId z = zdt.getZone();
        ZoneOffset zo = zdt.getOffset();
        Chronology ch = zdt.getChronology();
        Log.d(TAG, "Year: " + year);
        Log.d(TAG, "Month: " + month.toString());
        Log.d(TAG, "Day: " + day);
        Log.d(TAG, "Hour: " + hour);
        Log.d(TAG, "Minutes: " + min);
        Log.d(TAG, "Seconds: " + sec);
        Log.d(TAG, "DayOfWeek: " + dow);
        Log.d(TAG, "DayOfYear: " + doy);
        Log.d(TAG, "ZoneId: " + z);
        Log.d(TAG, "ZoneOffset: " + zo);
        Log.d(TAG, "Chronology: " + ch);
    }

    private void convertLocalToString() {
        LocalDateTime ldt = LocalDateTime.now();//2021-05-28T12:46:12.533
        String dateInString = ldt.format(DateTimeFormatter.ofPattern("dd MMM, yyyy HH:mm:ss"));//28 May, 2021 12:46:12
        Log.d(TAG, "LocalToString: " + ldt + "-->" + dateInString);
    }

    private void convertStringToLocalOrZone() {
        String strDate = "27/05/2021 03:30 PM";
        LocalDateTime ldt = LocalDateTime.parse(strDate, DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a"));//2021-05-27T15:30
        Log.d(TAG, "StringToLocal: " + strDate + "-->" + ldt);
    }

    private void convertZoneToUTC() {
        ZonedDateTime zdt = ZonedDateTime.now();//2021-05-28T11:44:59.596+05:30[Asia/Kolkata]
        LocalDateTime ldt = zdt.toLocalDateTime();//2021-05-28T07:16:12.527 //this and below both are same
        LocalDateTime ldt1 = LocalDateTime.ofInstant(zdt.toInstant(), ZoneOffset.systemDefault());//2021-05-28T07:16:12.527
        ZonedDateTime zdt1 = ldt.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC);//2021-05-28T07:16:12.527Z
        Log.d(TAG, "ZoneToUTC: " + zdt + "-->" + zdt1);
        Log.d(TAG, "ZoneToLocal: " + zdt + "-->" + ldt);
        Log.d(TAG, "LocalToZone: " + ldt + "-->" + zdt1);
    }

    private void convertUTCToZone() {
        Instant instant = Instant.now(); //2021-05-28T07:16:12.526Z
        ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, ZoneId.of("Asia/Kolkata"));//2021-05-28T12:46:12.526+05:30[Asia/Kolkata]
        Log.d(TAG, "UTCToZone: " + instant + "-->" + zdt);
    }

    private void basicClasses() {
        Instant i1 = Instant.now(); //shows current utc time
        mainBinding.txtDateTime.append("Instant 1 - " + i1);

        Clock c1 = Clock.system(ZoneId.of("Europe/Paris"));
        mainBinding.txtDateTime.append("\nClock 1 - " + c1);

        Clock c2 = Clock.systemUTC(); //gives zone id of utc - represented as Z
        mainBinding.txtDateTime.append("\nClock 2 - " + c2);

        Clock c3 = Clock.systemDefaultZone();
        mainBinding.txtDateTime.append("\nClock 3 - " + c3);

        Clock c4 = Clock.fixed(Instant.now(), ZoneId.of("Europe/Paris"));
        mainBinding.txtDateTime.append("\nClock 4 - " + c4);

        Clock c5 = Clock.offset(Clock.system(ZoneId.of("Europe/Paris")), Duration.ofSeconds(2));
        mainBinding.txtDateTime.append("\nClock 5 - " + c5);

        Instant start = Instant.now();
        Instant end1 = Instant.now().plusSeconds(5);
        Instant end2 = Instant.now().minusSeconds(5);

        Duration d1 = Duration.between(start, end1);
        mainBinding.txtDateTime.append("\nDuration 1 - " + d1);

        Duration d2 = Duration.between(start, end2);
        mainBinding.txtDateTime.append("\nDuration 2 - " + d2);

        ZonedDateTime zdt1 = ZonedDateTime.now();
        mainBinding.txtDateTime.append("\nZonedDateTime 1 - " + zdt1);

        ZonedDateTime zdt2 = ZonedDateTime.now(c3);
        mainBinding.txtDateTime.append("\nZonedDateTime 2 - " + zdt2);

        ZonedDateTime zdt3 = ZonedDateTime.now(ZoneId.of("Europe/Paris"));
        mainBinding.txtDateTime.append("\nZonedDateTime 3 - " + zdt3);

        LocalDate ld1 = LocalDate.now();
        mainBinding.txtDateTime.append("\nLocalDate 1 - " + ld1);

        LocalDate ld2 = LocalDate.now(c3);
        mainBinding.txtDateTime.append("\nLocalDate 2 - " + ld2);

        LocalDate ld3 = LocalDate.now(ZoneId.of("Europe/Paris"));
        mainBinding.txtDateTime.append("\nLocalDate 3 - " + ld3);

        LocalTime lt1 = LocalTime.now();
        mainBinding.txtDateTime.append("\nLocalTime 1 - " + lt1);

        LocalTime lt2 = LocalTime.now(c3);
        mainBinding.txtDateTime.append("\nLocalTime 2 - " + lt2);

        LocalTime lt3 = LocalTime.now(ZoneId.of("Europe/Paris"));
        mainBinding.txtDateTime.append("\nLocalTime 3 - " + lt3);

        LocalDateTime ldt1 = LocalDateTime.now();
        mainBinding.txtDateTime.append("\nLocalDateTime 1 - " + ldt1);

        LocalDateTime ldt2 = LocalDateTime.now(c3);
        mainBinding.txtDateTime.append("\nLocalDateTime 2 - " + ldt2);

        LocalDateTime ldt3 = LocalDateTime.now(ZoneId.of("Europe/Paris"));
        mainBinding.txtDateTime.append("\nLocalDateTime 3 - " + ldt3);

        MonthDay md = MonthDay.now();
        mainBinding.txtDateTime.append("\nMonthDay - " + md);

        YearMonth ym = YearMonth.now();
        mainBinding.txtDateTime.append("\nYearMonth - " + ym);

        Year y = Year.now();
        mainBinding.txtDateTime.append("\nYear - " + y);

        Period p = Period.of(2021, 4, 5);
        mainBinding.txtDateTime.append("\nPeriod - " + p);

        Date date = DateTimeUtils.toDate(Instant.now());
        mainBinding.txtDateTime.append("\nInstant -> Date: " + date);

        Instant i2 = DateTimeUtils.toInstant(date);
        mainBinding.txtDateTime.append("\nDate -> Instant 2: " + i2);

        GregorianCalendar gc = DateTimeUtils.toGregorianCalendar(ZonedDateTime.now());
        mainBinding.txtDateTime.append("\nZonedDateTime -> GregorianCalendar: " + gc);
    }
}