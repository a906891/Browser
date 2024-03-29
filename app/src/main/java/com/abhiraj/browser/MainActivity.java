package com.abhiraj.browser;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.test.espresso.core.deps.guava.eventbus.Subscribe;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.abhiraj.browser.constance.fragConst;
import com.abhiraj.browser.custom.mainActivitySimpleOnGestureListener;
import com.abhiraj.browser.customview.verticalViewPager;
import com.abhiraj.browser.event.baseEvent;
import com.abhiraj.browser.event.delThisFrag;
import com.abhiraj.browser.event.deleteFragEvent;
import com.abhiraj.browser.event.fragEvent;
import com.abhiraj.browser.event.showDelImg;
import com.abhiraj.browser.event.slideEvent;
import com.abhiraj.browser.event.windowEvent;
import com.abhiraj.browser.event.zoomEvent;
import com.abhiraj.browser.fragment.fragAdapter;
import com.abhiraj.browser.fragment.mainFrag;


import de.greenrobot.event.EventBus;

import static android.widget.RelativeLayout.*;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    int donotshowagain = 0;
    public static final String SHARED_PREFS = "sharedPrefs";


    private verticalViewPager mViewPager;
    private fragAdapter fragPagerAdapter;
    private int thewidth, theheight;

    private LinearLayout llayoutviewpage;
    private LinearLayout pagebarlt;
    private ImageView newPage;
    private ImageView returnMain;

    private DisplayMetrics dm2;
    private GestureDetectorCompat myDetector;
    private mainActivitySimpleOnGestureListener mainSimpleOnGestureListener;

    //set request code
    public static final int REQUEST_DEFAULT = -1;
    public static final int REQUEST_OPEN_FAV_OR_HIS = 0;
    public static final int REQUEST_OPEN_DOWNLOADS = 0; //for openning downloads
    public static final int REQUEST_OPEN_SETTINGS = 0; //for openning settings
    public static final int REQUEST_OPEN_FEEDBACK = 0; //for openning feedback
    public static final int REQUEST_OPEN_THEMES = 0; //for openning themes
    public static final int REQUEST_NIGHT_MODE_ON = 0; //for openning nightmode
    public static final int REQUEST_NIGHT_MODE_OFF = 0; //for openning nightmodeoff
    public static final int REQUEST_OPEN_INCOGNITOON = 0; //for openning incognitoon
    public static final int REQUEST_OPEN_INCOGNITOOFF = 0; //for openning incognitooff


    public static final int REQUEST_SAVE_IMAGE_PATH = 0;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Donot show with exit menu
        SharedPreferences sharedPreferences1 = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        donotshowagain = Integer.parseInt(sharedPreferences1.getString("show", "0"));


        for (int i = 0; i < fragConst.init_page_count; i++) {
            mainFrag tmp = new mainFrag();
            fragConst.fraglist.add(tmp);
            fragConst.fraghashcode.add(String.valueOf(tmp.hashCode()));
        }
        mViewPager = (verticalViewPager) findViewById(R.id.main_view_page);
        fragPagerAdapter = new fragAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(fragPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);//设置预加载网页数目

        EventBus.getDefault().register(this);
        mainSimpleOnGestureListener = new mainActivitySimpleOnGestureListener();
        myDetector = new GestureDetectorCompat(this, mainSimpleOnGestureListener);
        viewInit();

    }

    private void viewInit() {

        llayoutviewpage = (LinearLayout) findViewById(R.id.llayoutviewpage);
        pagebarlt = (LinearLayout) findViewById(R.id.pagebarlt);
        newPage = (ImageView) findViewById(R.id.add_new_page);
        returnMain = (ImageView) findViewById(R.id.return_main);
        newPage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewPage();
            }
        });
        returnMain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomchange();
            }
        });

        // mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        dm2 = getResources().getDisplayMetrics();//获取手机分辨率
    }

    private void addNewPage() {

        mainFrag tmp = new mainFrag();

        fragConst.fraghashcode.add(String.valueOf(tmp.hashCode()));

        fragConst.fraglist.add(mViewPager.getCurrentItem() + 1, tmp);

        fragPagerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dm2.widthPixels - mViewPager.getWidth() < 5) {
                } else {
                    zoomchange();
                }
            }
        }, 400);
    }

    private void removePage(int position) {
        if (position >= 0 && position < fragConst.fraglist.size()) {
            if (fragConst.fraglist.size() <= 1) {
                return;
            }
            fragConst.fraglist.remove(position);
            fragConst.fraghashcode.remove(position);
            fragPagerAdapter.notifyDataSetChanged();
        }
    }


    private boolean currentIsFull = true;//当前是不是全屏

    private void zoomchange() {
        Log.d("before", "enlarge  " + thewidth + " ----  " + dm2.widthPixels);
        Log.d("before", "enlarge  " + theheight + " ----  " + dm2.heightPixels);
        if (dm2.widthPixels - mViewPager.getWidth() < 5) {
            mViewPager.setPageMargin(fragConst.page_interval);
            //   Logger.v("缩小  " + thewidth);
            llayoutviewpage.setGravity(CENTER_IN_PARENT);

            //缩小
            PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.8f);
            PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.8f);
            ObjectAnimator scale = ObjectAnimator.ofPropertyValuesHolder(mViewPager, pvhX, pvhY);
            scale.setDuration(100);
            scale.start();

            RelativeLayout.LayoutParams Rlparam = new RelativeLayout.LayoutParams(dm2.widthPixels * 8 / 10, dm2.heightPixels * 8 / 10);
            Rlparam.addRule(CENTER_IN_PARENT);
            llayoutviewpage.setLayoutParams(Rlparam);
            mainSimpleOnGestureListener.setViewPagePosition(mViewPager.getWidth(), mViewPager.getHeight());
            pagebarlt.setVisibility(VISIBLE);
            EventBus.getDefault().post(new zoomEvent(false));
        } else {
            Log.d("test", "enlarge  " + thewidth + " ----  " + dm2.widthPixels);
            Log.d("test", "enlarge  " + thewidth + " ----  " + dm2.heightPixels);
            mViewPager.setPageMargin(0);
            llayoutviewpage.setGravity(CENTER_IN_PARENT);
            //放大到原来样子
            PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 0.8f, 1f);
            PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 0.8f, 1f);
            ObjectAnimator scale = ObjectAnimator.ofPropertyValuesHolder(mViewPager, pvhX, pvhY);
            scale.setDuration(100);
            scale.start();
            currentIsFull = true;
            RelativeLayout.LayoutParams Rlparam2 = new RelativeLayout.LayoutParams(dm2.widthPixels, dm2.heightPixels);

            Rlparam2.addRule(CENTER_IN_PARENT);
            llayoutviewpage.setLayoutParams(Rlparam2);
            pagebarlt.setVisibility(INVISIBLE);
            EventBus.getDefault().post(new zoomEvent(true));
        }
    }


    @Subscribe
    public void onEventMainThread(baseEvent event) {

        if (event instanceof fragEvent) {
            //  Toast.makeText(this, " 收到 event 数据  " + ((fragEvent) event).getFragTag(), Toast.LENGTH_SHORT).show();
            if (dm2.widthPixels - mViewPager.getWidth() < 5) {
            } else {
                zoomchange();
            }
        }
        if (event instanceof slideEvent) {
            //   Toast.makeText(this, " 收到 event 数据  " + ((slideEvent) event).getType()+"  "+((slideEvent) event).getDirection(), Toast.LENGTH_SHORT).show();
            switch (((slideEvent) event).getType()) {
                case MotionEvent.ACTION_MOVE:
                    switch (((slideEvent) event).getDirection()) {
                        case "left":
                            break;
                        case "right":
                            break;
                    }

                    break;
                case MotionEvent.ACTION_DOWN:
                    switch (((slideEvent) event).getDirection()) {
                        case "left":
                            int cItem = mViewPager.getCurrentItem();
                            mViewPager.setCurrentItem(cItem > 0 ? cItem - 1 : cItem);
                            break;
                        case "right":
                            int rItem = mViewPager.getCurrentItem();
                            mViewPager.setCurrentItem(rItem < fragConst.fraglist.size() - 1 ? rItem + 1 : rItem);
                            break;
                    }
                    break;
            }
        }
        if (event instanceof deleteFragEvent) {
            int i = 0;
            String Tag = ((deleteFragEvent) event).getFragTag();
            for (mainFrag temp : fragConst.fraglist) {
                if (temp.getFragTag().equals(Tag)) {

                    Handler do_handler = new Handler();
                    final int page = i;
                    do_handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            removePage(page);
                        }
                    }, 80);
                }
                i++;
            }
        }
        if (event instanceof showDelImg) {
            if (((showDelImg) event).isShow()) {

            } else {

            }
        }
        if (event instanceof delThisFrag) {
            removePage(mViewPager.getCurrentItem());
        }

        if (event instanceof windowEvent) {
            zoomchange();
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // Necessary
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //get location of TextView
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        fragConst.fraglist.clear();
        fragConst.new_mainfrag_count = 0; //调用次数清0
    }

    CheckBox ClearHistory;
    CheckBox doNotShowAgain;
    Dialog exit;

    @Override
    public void onBackPressed() {

        if (donotshowagain == 0) {
            Dialog bottomSheetDialog = new Dialog(
                    MainActivity.this, R.style.BottomSheetDialoTheme);
            //Making a view of Menu to be transparent from the background for Curved corners
            View bottomSheetView = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.activity_exit, (LinearLayout) findViewById(R.id.BottomSheetContainer1));

            bottomSheetDialog.setContentView(bottomSheetView);
            exit = bottomSheetDialog;
            bottomSheetDialog.show();

            bottomSheetDialog.findViewById(R.id.exitconfirmbtn).setOnClickListener(this);
            bottomSheetDialog.findViewById(R.id.cancelconfirmbtn).setOnClickListener(this);

            bottomSheetDialog.findViewById(R.id.clearHistory).setOnClickListener(this);
            ClearHistory = bottomSheetDialog.findViewById(R.id.clearHistory);

            bottomSheetDialog.findViewById(R.id.doNotShowAgain).setOnClickListener(this);
            doNotShowAgain = bottomSheetDialog.findViewById(R.id.doNotShowAgain);
        } else {
            finishAffinity();
            System.exit(0);
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.exitconfirmbtn:
                if (ClearHistory.isChecked()) {

                    final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("ClearHistroy", String.valueOf(1));
                    editor.apply();
                } else {

                    final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("ClearHistroy", String.valueOf(0));
                    editor.apply();
                }
//exit dialog box
                exit.dismiss();
//exit application
                finishAffinity();
                System.exit(0);
                break;
            case R.id.clearHistory:
                if (ClearHistory.isChecked()) {
                    final SharedPreferences sharedPreferences1 = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                    editor1.putString("ClearHistory", String.valueOf(1));
                    editor1.apply();


                } else if (!ClearHistory.isChecked()) {
                    final SharedPreferences sharedPreferences1 = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                    editor1.putString("ClearHistory", String.valueOf(0));
                    editor1.apply();

                }
                break;
            case R.id.doNotShowAgain:
                if (doNotShowAgain.isChecked()) {
                    final SharedPreferences sharedPreferences1 = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                    editor1.putString("show", String.valueOf(1));
                    editor1.apply();

                } else if (!doNotShowAgain.isChecked()) {
                    final SharedPreferences sharedPreferences1 = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                    editor1.putString("show", String.valueOf(0));
                    editor1.apply();

                }
                break;
            case R.id.cancelconfirmbtn:
                exit.dismiss();
                break;

        }
    }
}
