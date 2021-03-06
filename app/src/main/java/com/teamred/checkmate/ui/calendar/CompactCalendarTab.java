package com.teamred.checkmate.ui.calendar;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
/*import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;*/
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
/*import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;*/

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
//import com.github.sundeepk.compactcalendarview.CompactCalendarController;
import com.github.sundeepk.compactcalendarview.domain.Event;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.teamred.checkmate.R;
import com.teamred.checkmate.data.CheckmateKey;
import com.teamred.checkmate.data.Constant;
import com.teamred.checkmate.data.model.ReviewRecord;
import com.teamred.checkmate.util.DateUtil;


public class CompactCalendarTab extends Fragment {

    private static final String TAG = "CalendarTabActivity";
    private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", Locale.getDefault());
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    private boolean shouldShow = false;
    private CompactCalendarView compactCalendarView;
    private ActionBar toolbar;

    //private GestureDetector GD;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainTabView = inflater.inflate(R.layout.main_calendar_tab,container,false);

        final List<String> mutableBookings = new ArrayList<>();

        final ListView bookingsListView = mainTabView.findViewById(R.id.bookings_listview);
        final Button showPreviousMonthBut = mainTabView.findViewById(R.id.prev_button);
        final Button showNextMonthBut = mainTabView.findViewById(R.id.next_button);
        //final Button slideCalendarBut = mainTabView.findViewById(R.id.slide_calendar);
        final Button showCalendarWithAnimationBut = mainTabView.findViewById(R.id.show_with_animation_calendar);
        /*final Button setLocaleBut = mainTabView.findViewById(R.id.set_locale);
        final Button removeAllEventsBut = mainTabView.findViewById(R.id.remove_all_events);*/

        final ArrayAdapter adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mutableBookings);
        bookingsListView.setAdapter(adapter);
        compactCalendarView = mainTabView.findViewById(R.id.compactcalendar_view);

        //GD = new GestureDetector(this, this);

        // below allows you to configure color for the current day in the month
        // compactCalendarView.setCurrentDayBackgroundColor(getResources().getColor(R.color.black));
        // below allows you to configure colors for the current day the user has selected
        // compactCalendarView.setCurrentSelectedDayBackgroundColor(getResources().getColor(R.color.dark_red));
        compactCalendarView.setUseThreeLetterAbbreviation(false);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        compactCalendarView.setIsRtl(false);
        compactCalendarView.displayOtherMonthDays(false);
        //compactCalendarView.setIsRtl(true);
        loadEvents();
//        loadEventsForYear(2017);
        compactCalendarView.invalidate();

//        logEventsByMonth(compactCalendarView);

        // below line will display Sunday as the first day of the week
        // compactCalendarView.setShouldShowMondayAsFirstDay(false);

        // disable scrolling calendar
        // compactCalendarView.shouldScrollMonth(false);

        // show days from other months as greyed out days
        // compactCalendarView.displayOtherMonthDays(true);

        // show Sunday as first day of month
        // compactCalendarView.setShouldShowMondayAsFirstDay(false);

        //set initial title
        toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        toolbar.setTitle(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));

        //set title on calendar scroll
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                toolbar.setTitle(dateFormatForMonth.format(dateClicked));
                List<Event> bookingsFromMap = compactCalendarView.getEvents(dateClicked);
                Log.d(TAG, "inside onclick " + dateFormatForDisplaying.format(dateClicked));
                if (bookingsFromMap != null) {
                    Log.d(TAG, bookingsFromMap.toString());
                    mutableBookings.clear();
                    for (Event booking : bookingsFromMap) {
                        mutableBookings.add((String) booking.getData());
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                toolbar.setTitle(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });

        showPreviousMonthBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.scrollLeft();
                logEventsByMonth(compactCalendarView);
            }
        });

        showNextMonthBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.scrollRight();
                logEventsByMonth(compactCalendarView);
            }
        });

        /*final View.OnClickListener showCalendarOnClickLis = getCalendarShowLis();
        slideCalendarBut.setOnClickListener(showCalendarOnClickLis);*/

        final View.OnClickListener exposeCalendarListener = getCalendarExposeLis();
        showCalendarWithAnimationBut.setOnClickListener(exposeCalendarListener);

        compactCalendarView.setAnimationListener(new CompactCalendarView.CompactCalendarAnimationListener() {
            @Override
            public void onOpened() {
            }

            @Override
            public void onClosed() {
            }
        });

/*        setLocaleBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale locale = Locale.FRANCE;
                dateFormatForDisplaying = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", locale);
                //TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
                //TimeZone.getDefault()
                TimeZone timeZone = TimeZone.getDefault();
                dateFormatForDisplaying.setTimeZone(timeZone);
                dateFormatForMonth.setTimeZone(timeZone);
                compactCalendarView.setLocale(timeZone, locale);
                compactCalendarView.setUseThreeLetterAbbreviation(false);
                loadEvents();
                loadEventsForYear(2017);
                logEventsByMonth(compactCalendarView);

            }
        });

        removeAllEventsBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.removeAllEvents();
            }
        });*/


        // uncomment below to show indicators above small indicator events
        compactCalendarView.shouldDrawIndicatorsBelowSelectedDays(true);

        // uncomment below to open onCreate
        //openCalendarOnCreate(v);

        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {
                        Log.i(TAG, "onFling has been called");
                        final int SWIPE_MIN_DISTANCE = 100;
                        final int SWIPE_THRESHOLD_VELOCITY = 200;
                        try {
                            if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                                if (!compactCalendarView.isAnimating()) {
                                    compactCalendarView.hideCalendarWithAnimation();
                                    Log.i(TAG, "fling up detected");
                                }
                                return true;
                            }
                            else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                                if (!compactCalendarView.isAnimating()) {
                                    compactCalendarView.showCalendarWithAnimation();
                                    Log.i(TAG, "fling down detected");
                                }
                                return true;
                            }
                            return false;
                        } catch (Exception e) {
                            // nothing
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        mainTabView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });

        return mainTabView;
    }

    /*@NonNull
    private View.OnClickListener getCalendarShowLis() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!compactCalendarView.isAnimating()) {
                    if (shouldShow) {
                        compactCalendarView.showCalendar();
                    } else {
                        compactCalendarView.hideCalendar();
                    }
                    shouldShow = !shouldShow;
                }
            }
        };
    }*/

    @NonNull
    private View.OnClickListener getCalendarExposeLis() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!compactCalendarView.isAnimating()) {
                    if (shouldShow) {
                        compactCalendarView.showCalendarWithAnimation();
                    } else {
                        compactCalendarView.hideCalendarWithAnimation();
                    }
                    shouldShow = !shouldShow;
                }
            }
        };
    }


    private void openCalendarOnCreate(View v) {
        final RelativeLayout layout = v.findViewById(R.id.main_content);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                compactCalendarView.showCalendarWithAnimation();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbar.setTitle(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));
        // Set to current day on resume to set calendar to latest day
        // toolbar.setTitle(dateFormatForMonth.format(new Date()));
    }

    private void loadEvents() {
        addEvents(-1, -1);
//        addEvents(Calendar.DECEMBER, -1);
//        addEvents(Calendar.AUGUST, -1);
    }

//    private void loadEventsForYear(int year) {
//        addEvents(Calendar.DECEMBER, year);
//        addEvents(Calendar.AUGUST, year);
//    }

    private void logEventsByMonth(CompactCalendarView compactCalendarView) {
//        currentCalender.setTime(new Date());
//        currentCalender.set(Calendar.DAY_OF_MONTH, 1);
//        currentCalender.set(Calendar.MONTH, Calendar.AUGUST);
//        List<String> dates = new ArrayList<>();
//        for (Event e : compactCalendarView.getEventsForMonth(new Date())) {
//            dates.add(dateFormatForDisplaying.format(e.getTimeInMillis()));
//        }
        addEvents(compactCalendarView.getFirstDayOfCurrentMonth().getMonth(), -1);
//        Log.d(TAG, "Events for Aug with simple date formatter: " + dates);
//        Log.d(TAG, "Events for Aug month using default local and timezone: " + compactCalendarView.getEventsForMonth(currentCalender.getTime()));
    }

    private void addEvents(int month, int year) {
//        currentCalender.setTime(new Date());
        currentCalender.setTime(new Date());
        currentCalender.set(Calendar.DAY_OF_MONTH, 1);
        if (month > -1){
            currentCalender.set(Calendar.MONTH, month);
        }
        if (year > -1){
            currentCalender.set(Calendar.YEAR, year);
        }

//        Date firstDayOfMonth = currentCalender.getTime();
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        int i = 1;
        while(i< 31 && currentCalender.before(instance) || currentCalender.get(Calendar.DAY_OF_MONTH) == instance.get(Calendar.DAY_OF_MONTH)){

//            currentCalender.set(Calendar.DAY_OF_MONTH, i);
//            i++;

            setToMidnight(currentCalender);
            long timeInMillis = currentCalender.getTimeInMillis();

            String simpleDateString = DateUtil.getSimpleDateString(new Date(timeInMillis));

            FirebaseFirestore.getInstance().collection(CheckmateKey.REVIEW_RECORD)
                    .document(Constant.getInstance().getCurrentUser().getUid())
                    .collection(simpleDateString).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    List<Event> events = new ArrayList<>();
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    int size = documents.size();
                    int argb = Color.argb(255, 169, 68, 65);
                    if (size >= 3){
                        argb = Color.argb(255, 169, 68, 65);
                    }else if (size >=2 ){
                        argb = Color.argb(255/3*2, 169/3*2, 68/3*2, 65/3*2);
                    }else if (size >= 1){
                        argb = Color.argb(255/3, 169/3, 68/3, 65/3);
                    }

                    for (DocumentSnapshot document : documents) {
                        ReviewRecord reviewRecord = document.toObject(ReviewRecord.class);

                        Event event = new Event(argb
                                , reviewRecord.getTime().getTime()
                                , "Review " + reviewRecord.getPostTitle() + " the " + reviewRecord.getTimes() + "th times at " + reviewRecord.getTime());
                        events.add(event);
                    }
                    compactCalendarView.addEvents(events);
                }
            });
            currentCalender.add(Calendar.DATE, 1);
            i++;

        }
    }
//
//    private List<Event> getEvents(long timeInMillis, int day) {
////<<<<<<< HEAD
//        String simpleDateString = DateUtil.getSimpleDateString(new Date(timeInMillis));
//        final List<Event>[] ret = new List[]{null};
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                FirebaseFirestore.getInstance().collection(CheckmateKey.REVIEW_RECORD)
//                        .document(Constant.getInstance().getCurrentUser().getUid())
//                        .collection(simpleDateString).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        List<Event> events = new ArrayList<>();
//                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
//                        for (DocumentSnapshot document : documents) {
//                            ReviewRecord reviewRecord = document.toObject(ReviewRecord.class);
//                            Event event = new Event(Color.argb(255, 169, 68, 65)
//                                    , reviewRecord.getTime().getTime()
//                                    , "Review the " + reviewRecord.getTimes() + " times");
//                            events.add(event);
//                        }
//                        ret[0] = events;
//                    }
//                });
//            }
//        }).start();
//
//        while(true){
//            if (ret[0] != null){
//                return  ret[0];
//            }
//=======
//        if (day < 2) {
//            return Arrays.asList(new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis)));
//        } else if ( day > 2 && day <= 4) {
//            return Arrays.asList(
//                    new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis)),
//                    new Event(Color.argb(255, 100, 68, 65), timeInMillis, "Event 2 at " + new Date(timeInMillis)));
//        } else {
//            return Arrays.asList(
//                    new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis)),
//                    new Event(Color.argb(255, 100, 68, 65), timeInMillis, "Event 2 at " + new Date(timeInMillis)),
//                    new Event(Color.argb(255, 70, 68, 65), timeInMillis, "Event 3 at " + new Date(timeInMillis)));
//>>>>>>> d1c27b97d7c64e27bc6639ef7e2e92f6fc8b8431
//        }



//        if (day < 2) {
//            return Arrays.asList(new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis)));
//        } else if ( day > 2 && day <= 4) {
//            return Arrays.asList(
//                    new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis)),
//                    new Event(Color.argb(255, 100, 68, 65), timeInMillis, "Event 2 at " + new Date(timeInMillis)));
//        } else {
//            return Arrays.asList(
//                    new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis) ),
//                    new Event(Color.argb(255, 100, 68, 65), timeInMillis, "Event 2 at " + new Date(timeInMillis)),
//                    new Event(Color.argb(255, 70, 68, 65), timeInMillis, "Event 3 at " + new Date(timeInMillis)));
//        }
//    }

    private void setToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}