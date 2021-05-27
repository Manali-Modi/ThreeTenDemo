package com.example.threetendemo.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.threetendemo.R;
import com.example.threetendemo.databinding.ActivityMainBinding;

import org.threeten.bp.Clock;
import org.threeten.bp.DateTimeUtils;
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

import java.util.Date;
import java.util.GregorianCalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //showDateTime();
        initButtonClick();
    }

    @SuppressLint("SetTextI18n")
    private void initButtonClick() {
        mainBinding.btnCurrentUTC.setOnClickListener(view -> mainBinding.txtDateTime.setText(Instant.now().toString()));

        mainBinding.btnCurrentZone.setOnClickListener(view -> mainBinding.txtDateTime.setText(ZonedDateTime.now().toString()));

        mainBinding.btnLocal.setOnClickListener(view -> mainBinding.txtDateTime.setText(LocalDateTime.now().toString()));

        mainBinding.btnUTCToZone.setOnClickListener(view -> {
            Instant instant = Instant.now();
            ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, ZoneId.of("Asia/Kolkata"));
            mainBinding.txtDateTime.setText("From UTC:\n" + instant + "\n\nTo Zone:\n" + zdt);
        });

        mainBinding.btnZoneToUTC.setOnClickListener(view -> {
            ZonedDateTime zdt = ZonedDateTime.now();
            LocalDateTime ldt1 = LocalDateTime.ofInstant(zdt.toInstant(), ZoneOffset.systemDefault());
            ZonedDateTime zdt1 = ldt1.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC);
            mainBinding.txtDateTime.setText("From Zone:\n" + zdt + "\n\nTo UTC:\n" + zdt1);
        });

        mainBinding.btnStringToLocal.setOnClickListener(view -> {
            String strDate = "27/05/2021 03:30 PM";
            LocalDateTime ldt = LocalDateTime.parse(strDate, DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a"));
            mainBinding.txtDateTime.setText("From String:\n" + strDate + "\n\nTo Local:\n" + ldt);
        });

        mainBinding.btnLocalToString.setOnClickListener(view -> {
            LocalDateTime ldt = LocalDateTime.now();
            String dateInString = ldt.format(DateTimeFormatter.ofPattern("dd MMM, yyyy HH:mm:ss"));
            mainBinding.txtDateTime.setText("From Local:\n" + ldt + "\n\nTo String:\n" + dateInString);
        });

        mainBinding.btnDiffFormats.setOnClickListener(view -> {
            String input = "2021-05-27T16:05:15.598";
            LocalDateTime ldt = LocalDateTime.parse(input); //No need to use formatter because String input is already in format of LocalDateTime
            String output = ldt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a"));
            mainBinding.txtDateTime.setText("From:\n" + input + "\n\nTo:\n" + output);

            int year = ldt.getYear();
            Month month = ldt.getMonth();
            int day = ldt.getDayOfMonth();
            int hour = ldt.getHour();
            int min = ldt.getMinute();
            int sec = ldt.getSecond();
            int h;
            String ampm;
            if(hour < 12){
                h = hour;
                ampm = "AM";
            }
            else {
                if(hour == 12) h = hour;
                else h = hour - 12;
                ampm = "PM";
            }
            Chronology chronology = ldt.getChronology();
            mainBinding.txtDateTime.append("\n\nYear: " + year + " Month: " + month + " Day: " + day);
            mainBinding.txtDateTime.append("\nHour: " + h + " Minutes: " + min + " Seconds: " + sec + " am/pm: " + ampm);
            mainBinding.txtDateTime.append("\nChronology: " + chronology);
        });
    }

    private void showDateTime() {
        Instant i1 = Instant.now(); //shows current utc time
        mainBinding.txtDateTime.append("Instant 1 - " + i1);

        Clock c1 = Clock.system(ZoneId.of("Europe/Paris"));
        mainBinding.txtDateTime.append("\nClock 1 - " + c1);

        Clock c2 = Clock.systemUTC(); //gives zone id of utc - represented as Z
        mainBinding.txtDateTime.append("\nClock 2 - " + c2);

        Clock c3 = Clock.systemDefaultZone();
        mainBinding.txtDateTime.append("\nClock 3 - " + c3);

        /*Clock c4 = Clock.fixed(Instant.now(), ZoneId.of("Europe/Paris"));
        mainBinding.txtDateTime.append( "\nClock 4 - " + c4);

        Clock c5 = Clock.offset(Clock.system(ZoneId.of("Europe/Paris")), Duration.ofSeconds(2));
        mainBinding.txtDateTime.append( "\nClock 5 - " + c5);*/

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