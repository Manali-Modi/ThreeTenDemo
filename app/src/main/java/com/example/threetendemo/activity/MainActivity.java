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

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DateTime";
    private static final String TAG_LIST = "localDateList";
    private static final String TAG_DIFF = "Difference";
    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        basicClasses();
        convertUTCToZone(Instant.now(), ZoneId.of("Asia/Kolkata"));
        convertZoneToUTC(ZonedDateTime.now()); //has internal conversion Zone->Local & Local->Zone
        convertStringToLocalOrZone("27/05/2021 03:30 PM");
        convertLocalToString(LocalDateTime.now());
        getSeparateFields("2021-05-27T16:05:15.598+05:30[Asia/Kolkata]");
        inputDateWithZoneId();
        inputDateWithZoneOffset();
        inputDateTimeWithFractionOrNot();
        inputDateTimeOtherFormats(); //Not Standard
        inputDateInMillis(1569494827);

        List<LocalDate> localDateList = getListOfDatesBetweenTwoDate("2021-04-22", "2021-05-05");
        Log.d(TAG_LIST, localDateList.toString());

        List<LocalDate> localDateList1 = getListOfDatesBetweenTwoDateInMillis(1614882600000L, 1615573800000L); //start - 2021-03-05; end - 2021-03-13
        Log.d(TAG_LIST, localDateList1.toString());

        List<String> localDateList2 = getListOfDatesBetweenTwoDateInString("30/12/2020", "09/01/2021");
        Log.d(TAG_LIST, localDateList2.toString());

        long daysBetween = getTotalDaysBetweenTwoDate("2021-01-01", "2021-02-01");
        Log.d(TAG_DIFF, String.valueOf(daysBetween));

        String durationOfTime = getTotalTimeInHourMin("23/03/2021 05:20", "26/03/2021 16:15");
        Log.d(TAG_DIFF, durationOfTime);

        String duration = getTotalTimeInDayHourMin("23/03/2021 05:20", "26/03/2021 16:15");
        Log.d(TAG_DIFF, duration);

        String differenceFromToday = getDifferenceFromToday("23/05/2021 20:20");
        Log.d(TAG_DIFF, differenceFromToday);

        long differenceFromToday1 = getDifferenceFromToday(LocalDate.parse("2021-05-15"));
        Log.d(TAG_DIFF, String.valueOf(differenceFromToday1));

        String differenceFromToday2 = getDifferenceFromToday("01/01/2021 12:00 AM", DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a"), DateTimeFormatter.ofPattern("dd MMM, yy HH:mm"));
        Log.d(TAG_DIFF, differenceFromToday2);

    }

    private String getDifferenceFromToday(String date, DateTimeFormatter inputFormat, DateTimeFormatter outputFormat) {
        LocalDateTime local = LocalDateTime.parse(date, inputFormat);
        LocalDateTime today = LocalDateTime.now();
        long days = Duration.between(local, today).toDays();
        String outputDate = local.format(outputFormat);
        return days + " days from today" + "\nDate: " + outputDate;
    }

    private long getDifferenceFromToday(LocalDate startLocalDate) {
        LocalDate endLocalDate = LocalDate.now();
        return Duration.between(startLocalDate.atStartOfDay(), endLocalDate.atStartOfDay()).toDays();
    }

    private String getDifferenceFromToday(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime start = LocalDateTime.parse(date, formatter);
        LocalDateTime end = LocalDateTime.now();
        long days = Duration.between(start, end).toDays();
        String d1 = start.format(DateTimeFormatter.ofPattern("EEEE dd MMM, yyyy"));
        return "Date: " + d1 + "\nDifference: " + days + " days from today";
    }

    private String getTotalTimeInDayHourMin(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        long days = Duration.between(start, end).toDays();
        long hours = Duration.between(start, end).toHours() - (24 * days);
        long min = Duration.between(start, end).toMinutes() - (24 * 60 * days) - (60 * hours);
        return days + "day " + hours + "hour " + min + "min";
    }

    private String getTotalTimeInHourMin(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        long hours = Duration.between(start, end).toHours();
        long min = Duration.between(start, end).toMinutes() - (60 * hours);
        return hours + "h " + min + "min";
    }

    private long getTotalDaysBetweenTwoDate(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return Duration.between(start.atStartOfDay(), end.atStartOfDay()).toDays();
    }

    private List<String> getListOfDatesBetweenTwoDateInString(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        List<String> dateList = new ArrayList<>();
        while (!start.isAfter(end)) {
            dateList.add(start.format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
            start = start.plusDays(1);
        }
        return dateList;
    }

    private List<LocalDate> getListOfDatesBetweenTwoDateInMillis(long startDate, long endDate) {
        LocalDate start = Instant.ofEpochMilli(startDate).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = Instant.ofEpochMilli(endDate).atZone(ZoneId.systemDefault()).toLocalDate();
        List<LocalDate> dateList = new ArrayList<>();
        while (!start.isAfter(end)) //end.isAfter(start) || end.equals(start)
        {
            dateList.add(start);
            start = start.plusDays(1);
        }
        return dateList;
    }

    private List<LocalDate> getListOfDatesBetweenTwoDate(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<LocalDate> dateList = new ArrayList<>();
        while (!start.isAfter(end)) //end.isAfter(start) || end.equals(start)
        {
            dateList.add(start);
            start = start.plusDays(1);
        }
        return dateList;
    }

    private void inputDateInMillis(long millis) {
        String TAG = "InMillis";

        Instant instant = Instant.ofEpochMilli(millis);
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
        Log.d(TAG, "To instant: " + instant);
        Log.d(TAG, "To local: " + ldt);
        Log.d(TAG, "To zone: " + zdt);
    }

    private void inputDateTimeOtherFormats() {
        String TAG = "OtherFormats";
        String date1 = "2021-03-22 05:06:07.000 AM";//yyyy-MM-dd hh:mm:ss.SSS a
        String date2 = "2021-03-22+05:30";//yyyy-MM-ddXXX
        String date3 = "2021-03-22 AD";//yyyy-MM-dd G
        String date4 = "2021-03-22";//yyyy-MM-dd
        String date5 = "22 Mar, 2021";//dd EEE, yyyy
        String date6 = "05:06:07.089";//HH:mm:ss.SSS
        //Or many more possible here

        //take format of either date1, date2, date3, date4, date5 or date6 as per input
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.SSS a");

        //Directly convert to zone not possible if don't have any info of zone. So, first convert it into local and then to zone as per convertZoneToUTC()
        LocalDateTime ldt1 = LocalDateTime.parse(date1, formatter1);

        String output1 = ldt1.format(DateTimeFormatter.ofPattern("E dd MMM yyyy"));//Sun 22 Mar 2021 //E or EEE both same
        String output2 = ldt1.format(DateTimeFormatter.ofPattern("D Q YYYY"));//DayOfYear-Quarter-WeekBasedYear -> 81 1 2021
        String output3 = ldt1.format(DateTimeFormatter.ofPattern("W w F e"));//Week of month-week of year-day of week-localize day of week -> 4 13 1 2 //Diff of F & e in notes
        String output4 = ldt1.format(DateTimeFormatter.ofPattern("dd/MM/yy ppH"));//22/03/21  5 //p with two elements not allowed eg. dd/pMM/yy ppH not allowed
        String output5 = ldt1.format(DateTimeFormatter.ofPattern("hh 'o''clock' a"));//05 o'clock AM
        String output6 = ldt1.format(DateTimeFormatter.ofPattern("KK:mm a '-' kk:mm"));//
        Log.d(TAG, "\n" + date1 + "-->" + output1);
        Log.d(TAG, "\n" + date1 + "-->" + output2);
        Log.d(TAG, "\n" + date1 + "-->" + output3);
        Log.d(TAG, "\n" + date1 + "-->" + output4);
        Log.d(TAG, "\n" + date1 + "-->" + output5);
        Log.d(TAG, "\n" + date1 + "-->" + output6);
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
        String output2 = zdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssxx"));//2020-03-22T05:06:07+0530
        String output3 = zdt.format(DateTimeFormatter.ofPattern("dd MMMM yyyy z"));//22 March 2020 IST
        Log.d(TAG, "\n" + date1 + "-->" + output1);
        Log.d(TAG, "\n" + date1 + "-->" + output2);
        Log.d(TAG, "\n" + date1 + "-->" + output3);
    }

    private void getSeparateFields(String date) {
        //Also possible using local
        ZonedDateTime zdt = ZonedDateTime.parse(date);

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

    private void convertLocalToString(LocalDateTime ldt) {
        // ldt format: 2021-05-28T12:46:12.533
        String dateInString = ldt.format(DateTimeFormatter.ofPattern("dd MMM, yyyy HH:mm:ss"));//28 May, 2021 12:46:12
        Log.d(TAG, "LocalToString: " + ldt + "-->" + dateInString);
    }

    private void convertStringToLocalOrZone(String strDate) {
        LocalDateTime ldt = LocalDateTime.parse(strDate, DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a"));//2021-05-27T15:30
        Log.d(TAG, "StringToLocal: " + strDate + "-->" + ldt);
    }

    private void convertZoneToUTC(ZonedDateTime zdt) {
        //zdt format: 2021-05-28T11:44:59.596+05:30[Asia/Kolkata]
        LocalDateTime ldt = zdt.toLocalDateTime();//2021-05-28T07:16:12.527 //this and below both are same
        LocalDateTime ldt1 = LocalDateTime.ofInstant(zdt.toInstant(), ZoneOffset.systemDefault());//2021-05-28T07:16:12.527
        ZonedDateTime zdt1 = ldt.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC);//2021-05-28T07:16:12.527Z
        Log.d(TAG, "ZoneToUTC: " + zdt + "-->" + zdt1);
        Log.d(TAG, "ZoneToLocal: " + zdt + "-->" + ldt);
        Log.d(TAG, "LocalToZone: " + ldt + "-->" + zdt1);
    }

    private void convertUTCToZone(Instant instant, ZoneId zoneId) {
        // Instant format: 2021-05-28T07:16:12.526Z
        ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, zoneId);//2021-05-28T12:46:12.526+05:30[Asia/Kolkata]
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