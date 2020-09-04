package com.abhiraj.indianbrowsermadeinindia.fragment;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.test.espresso.core.deps.guava.eventbus.Subscribe;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abhiraj.indianbrowsermadeinindia.DownloaderNew;
import com.abhiraj.indianbrowsermadeinindia.FavAndHisActivity;
import com.abhiraj.indianbrowsermadeinindia.Feedback;
import com.abhiraj.indianbrowsermadeinindia.Incognito;
import com.abhiraj.indianbrowsermadeinindia.Incognitooff;
import com.abhiraj.indianbrowsermadeinindia.MainActivity;
import com.abhiraj.indianbrowsermadeinindia.NighhtModeOff;
import com.abhiraj.indianbrowsermadeinindia.NightModeOn;
import com.abhiraj.indianbrowsermadeinindia.PathUtil;
import com.abhiraj.indianbrowsermadeinindia.PopupWindowUrl;
import com.abhiraj.indianbrowsermadeinindia.R;
import com.abhiraj.indianbrowsermadeinindia.Settings;
import com.abhiraj.indianbrowsermadeinindia.Theme;
import com.abhiraj.indianbrowsermadeinindia.constance.fragConst;
import com.abhiraj.indianbrowsermadeinindia.custom.PaintActivity;
import com.abhiraj.indianbrowsermadeinindia.event.baseEvent;
import com.abhiraj.indianbrowsermadeinindia.event.delThisFrag;
import com.abhiraj.indianbrowsermadeinindia.event.deleteFragEvent;
import com.abhiraj.indianbrowsermadeinindia.event.fragEvent;
import com.abhiraj.indianbrowsermadeinindia.event.showDelImg;
import com.abhiraj.indianbrowsermadeinindia.event.windowEvent;
import com.abhiraj.indianbrowsermadeinindia.event.zoomEvent;
import com.abhiraj.indianbrowsermadeinindia.file.FileActivity;
import com.abhiraj.indianbrowsermadeinindia.file.ImageCaptureManager;
import com.abhiraj.indianbrowsermadeinindia.file.ImageDownloadManager;
import com.abhiraj.indianbrowsermadeinindia.file.PageAttributesActivity;
import com.abhiraj.indianbrowsermadeinindia.file.RequestShowImageOnline;
import com.abhiraj.indianbrowsermadeinindia.other.FavAndHisManager;
import com.abhiraj.indianbrowsermadeinindia.other.ItemLongClickedPopWindow;
import com.abhiraj.indianbrowsermadeinindia.other.PopupWindowTools;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.greenrobot.event.EventBus;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2016/11/28.
 */
public class mainFrag extends baseFrag {

    private View view;//缓存Fragment view

    private ImageView gbtn;
    private ImageView fbtn;
    private ImageView instabtn;
    private ImageView twitterbtn;
    private ImageView snapbtn;
    private ImageView crickbtn;
    private ImageView gamesbtn;
    private ImageView youtubebtn;
    private ImageView githubbtn;
    private ImageView coronabtn;

    private LinearLayout mainLayout;
    private FrameLayout rootLayout;
    private DisplayMetrics dm2;
    private String fragTag = "";
    private LinearLayout webToolsLayout;
    private LinearLayout delThisPageLayout;
    private ImageView delThisPage;
    private EditText delTitle;

    private boolean isPrivateBrowsing = false;
    private boolean isNewFragment = false;


    private static Boolean isQuit = false;
    int flag = 0;
    private int pageScreenShotCount = 0;
    private int windowScreenShotCount = 0;
    private String url = "";
    private String title = "";
    private String size = "";
    private String encoding = "";
    private Bitmap icon = null;

    //WebView
//    private SwipeRefreshLayout swipeLayout;
    private WebView webHolder;

    //WebUrlLayout
    private LinearLayout webUrlLayout;
    private FrameLayout frameLayout;
    private EditText webUrlStr;
    private ImageButton webUrlSearch;
    private ImageButton webUrlCancel;
    private ImageButton webUrlFresh;

    //bottom button
    private ImageButton pagePre;
    private ImageButton pageNext;
    private ImageButton pageHome;
    private TextView pageWindow;
    private ImageButton tools;

    //listener
    private ButtonClickedListener buttonClickedListener;
    private WebUrlStrWatcher webUrlStrWatcher;
    private WebViewTouchListener webViewTouchListener;
    private ViewTouchListener viewTouchListner;
    private WebViewLongClickedListener webViewLongClickedListener;
    private ImageClickedListener imageClickedListener;
    private ToolsClickedListener toolsClickedListener;
    private AchorClickedListener achorClickedListener;
    private WebViewClickListener webViewClickListener;

    //progress bar
    private ProgressBar webProgressBar;

    //gesture
    private GestureDetector myGestureDetector;
    private GestureListener gestureListener;

    //tools popup window
    private PopupWindowTools toolsPopWindow;

    //set long clicked popup window
    private ItemLongClickedPopWindow itemLongClickedPopWindow;

    //favorite and history manager
    private FavAndHisManager favAndHisManager;

    //dialog for saving images
    private Dialog saveImageToChoosePath;

    //button to save images
    private TextView choosePath;
    private TextView imgSaveName;

    //剪贴板
    private ClipboardManager clipboardManager;

    LinearLayout layout;
    WebView webView;
    //for back groundd change
    LinearLayout linear;
    LinearLayout editback;
    int themeNumber = 6;
    public static final String SHARED_PREFS = "sharedPrefs";
    //for update
    private int REQUEST_CODE = 11;
    int nightmode = 0;
    int incognito = 0;
    int donotshowagain = 0;
    int addbookmark = 0;
    //for accessing menu from this
    BottomSheetDialog bottomSheetDialogForIconChange;
    CardView cardView;

    CheckBox ClearHistory;
    CheckBox doNotShowAgain;
    Dialog exit;

    public mainFrag() {
        this.fragTag = fragConst.new_mainfrag_count + "";
        fragConst.new_mainfrag_count++;
        isNewFragment = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //////////FOR UPDATING THE APP
        final AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(getActivity());
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {

                if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && result.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(result, AppUpdateType.FLEXIBLE, getActivity(), REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        ////////////////////////

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_main, null);
            webView = (WebView) view.findViewById(R.id.web_holder);

            init(view);
        }
        //The cached rootView needs to determine whether the parent has been added, if there is a parent that needs to be deleted from the parent, otherwise the rootview already has a parent error will occur。
        else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }


    private void init(View view) {

        LinearLayout ly = view.findViewById(R.id.mainbackground);
        editback = view.findViewById(R.id.web_url_layout);

        linear = ly;
/// for changing the background for the first time.

        changebackground();


        Log.d("theme", "init: " + themeNumber);

        mainLayout = (LinearLayout) view.findViewById(R.id.main_lt);
        mainLayout.setOnTouchListener(this);
        rootLayout = (FrameLayout) view.findViewById(R.id.root_lt);
        rootLayout.setOnTouchListener(this);
        webToolsLayout = (LinearLayout) view.findViewById(R.id.web_tool_lt);
        delThisPageLayout = (LinearLayout) view.findViewById(R.id.del_this_page_lt);
        delThisPage = (ImageView) view.findViewById(R.id.del_this_page);
        delTitle = (EditText) view.findViewById(R.id.del_title_lt);

        webHolder = (WebView) view.findViewById(R.id.web_holder);

        //WebUrlLayout
        webUrlLayout = (LinearLayout) view.findViewById(R.id.web_url_layout);
        frameLayout = (FrameLayout) view.findViewById(R.id.Mask);
        webUrlStr = (EditText) view.findViewById(R.id.web_url_str);
        webUrlSearch = (ImageButton) view.findViewById(R.id.web_url_search);
        webUrlCancel = (ImageButton) view.findViewById(R.id.web_url_cancel);
        webUrlFresh = (ImageButton) view.findViewById(R.id.web_url_fresh);
        webUrlStr.setCursorVisible(false);
        webUrlStr.clearFocus();
        webUrlStr.setFocusableInTouchMode(false);

        //listener

        buttonClickedListener = new ButtonClickedListener();
        webUrlStrWatcher = new WebUrlStrWatcher();
        webViewTouchListener = new WebViewTouchListener();
        viewTouchListner = new ViewTouchListener();
        toolsClickedListener = new ToolsClickedListener();
        webViewLongClickedListener = new WebViewLongClickedListener();

        //progress bar
        webProgressBar = (ProgressBar) view.findViewById(R.id.web_process_bar);
        webProgressBar.setVisibility(View.GONE);

        //gesture
        gestureListener = new GestureListener();
        // myGestureDetector = new GestureDetector(this,gestureListener);

        //bottom button
        pagePre = (ImageButton) view.findViewById(R.id.pre_button);
        pageNext = (ImageButton) view.findViewById(R.id.next_button);
        pageHome = (ImageButton) view.findViewById(R.id.home_button);
        pageWindow = (TextView) view.findViewById(R.id.window_button);
        tools = (ImageButton) view.findViewById(R.id.tools_button);

        //tools popup window
        toolsPopWindow = new PopupWindowTools(getActivity());

        //favorite and history manager
        favAndHisManager = new FavAndHisManager(getActivity().getApplicationContext());

        //button setting
        pagePre.setEnabled(true);
        pageNext.setEnabled(true);

        //WebView setting
//        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        webHolder.getSettings().setDefaultTextEncodingName("UTF-8");
        webHolder.getSettings().setJavaScriptEnabled(true);
        webHolder.getSettings().setSupportZoom(false);
        webHolder.getSettings().setUseWideViewPort(false);
        webHolder.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webHolder.getSettings().setLoadWithOverviewMode(false);

        webHolder.setWebViewClient(new MyWebViewClient());
        webHolder.setWebChromeClient(new MyChromeClient()); //set progressbar
//        webHolder.loadUrl("https://www.google.com");
        //webHolder.setOnTouchListener(webViewTouchListener);
        webHolder.setOnLongClickListener(webViewLongClickedListener);

        webUrlSearch.setOnClickListener(buttonClickedListener);
        webUrlCancel.setOnClickListener(buttonClickedListener);
        webUrlFresh.setOnClickListener(buttonClickedListener);
        webUrlStr.addTextChangedListener(webUrlStrWatcher);
        webUrlStr.setOnTouchListener(viewTouchListner);
        frameLayout.setOnTouchListener(viewTouchListner);

        pagePre.setOnClickListener(buttonClickedListener);
        pageNext.setOnClickListener(buttonClickedListener);
        pageHome.setOnClickListener(buttonClickedListener);
        pageWindow.setOnClickListener(buttonClickedListener);
        tools.setOnClickListener(buttonClickedListener);

        EventBus.getDefault().register(this);

        dm2 = getResources().getDisplayMetrics();

        delThisPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delAnime();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        EventBus.getDefault().post(new delThisFrag());
                    }
                }, 300);
                EventBus.getDefault().post(new delThisFrag());
            }
        });

//        swipeLayout.setOnRefreshListener(this);

        if ((fragConst.new_mainfrag_count > 1) && isNewFragment) {
            //Shrink
            PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 0.1f, 1f);
            PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 0.1f, 1f);
            ObjectAnimator scale = ObjectAnimator.ofPropertyValuesHolder(rootLayout, pvhX, pvhY);
            scale.setDuration(500);
            scale.start();
        }

        isNewFragment = false;

        gbtn = view.findViewById(R.id.google_btn);
        gbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("google", "onClick: No");
                webHolder.loadUrl("https://www.google.com");
                ChangeWebViewSize();

            }
        });

        fbtn = view.findViewById(R.id.facebook_btn);
        fbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("google", "onClick: No");
                webHolder.loadUrl("https://www.facebook.com");
                ChangeWebViewSize();

            }
        });

        instabtn = view.findViewById(R.id.insta_btn);
        instabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("google", "onClick: No");
                webHolder.loadUrl("https://www.instagram.com");
                ChangeWebViewSize();

            }
        });

        twitterbtn = view.findViewById(R.id.twitter_btn);
        twitterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("google", "onClick: No");
                webHolder.loadUrl("https://www.twitter.com");
                ChangeWebViewSize();

            }
        });


        snapbtn = view.findViewById(R.id.snap_btn);
        snapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("google", "onClick: No");
                webHolder.loadUrl("https://www.snapchat.com");
                ChangeWebViewSize();

            }
        });

        crickbtn = view.findViewById(R.id.cricket_btn);
        crickbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("google", "onClick: No");
                webHolder.loadUrl("https://www.cricbuzz.com/");
                ChangeWebViewSize();

            }
        });


        gamesbtn = view.findViewById(R.id.games_btn);
        gamesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("google", "onClick: No");
                webHolder.loadUrl("https://135.win.qureka.com");
                ChangeWebViewSize();
            }
        });

        youtubebtn = view.findViewById(R.id.youtube_btn);
        youtubebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("google", "onClick: No");
                webHolder.loadUrl("https://www.youtube.com");
                ChangeWebViewSize();
            }
        });

        githubbtn = view.findViewById(R.id.github_btn);
        githubbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("google", "onClick: No");
                webHolder.loadUrl("https://www.github.com");
                ChangeWebViewSize();

            }
        });

        coronabtn = view.findViewById(R.id.corona_btn);
        coronabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("google", "onClick: No");
                webHolder.loadUrl("https://www.worldometers.info/coronavirus/");
                ChangeWebViewSize();

            }
        });

        webHolder.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(final String url, final String userAgent, String contentDispotion, String mimetype, long contentlength) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        DownloadAlerter(url, userAgent, contentDispotion, mimetype);    // new download dialog  with editText\
                        // Dialog for download DownloadDialog(url, userAgent, contentDispotion, mimetype);
                    }
//                    else {
                    ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                    }

                } else {
                    DownloadAlerter(url, userAgent, contentDispotion, mimetype);       // new download dialog with editText
                    //Dialog for dowwnload DownloadDialog(url, userAgent, contentDispotion, mimetype);
                }
            }
        });


    }


    ////Asking for permission
    public static boolean hasPermission(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void ChangeWebViewSize() {


        webHolder.setVisibility(View.VISIBLE);

    }

    public void DownloadAlerter(final String url, final String UserAgent, String contentDispotion, String mimetype) {
        final String filenamme = URLUtil.guessFileName(url, contentDispotion, mimetype);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());

        View myView = getLayoutInflater().inflate(R.layout.custom_dialog_for_download, null);

        Button buttonYes = myView.findViewById(R.id.Button_Yes);
        Button buttonNo = myView.findViewById(R.id.Button_No);
        EditText EditTextFileName = myView.findViewById(R.id.EditTextFile_Name);
        EditText EditTextFileUrl = myView.findViewById(R.id.EditTextFile_Url);


        builder.setView(myView);
        final android.app.AlertDialog alertDialog = builder.create();

        alertDialog.setCancelable(false);

        EditTextFileName.setText(filenamme);
        EditTextFileUrl.setText(url);
        ////Path for download
        final String ur = EditTextFileUrl.getText().toString();
        final String fi = EditTextFileName.getText().toString();

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentUrls = new Intent(view.getContext(), DownloaderNew.class);  ////change if needed for old download
                intentUrls.putExtra("urlsss", ur);
                intentUrls.putExtra("filenames", fi);
                startActivity(intentUrls);

            /*    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                String cookie = CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("Cookie", cookie);
                request.addRequestHeader("User-Agent", UserAgent);
                request.allowScanningByMediaScanner();
//notification of download
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
////saving file in location
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filenamme);

                manager.enqueue(request);*/

//                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY , hh:mm a", Locale.getDefault());
//
//                File path = new File(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + filenamme);
//                addDownload(filenamme, sdf.format(new Date()), String.valueOf(path));
                alertDialog.dismiss();
            }
        });
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }


    public String getFragTag() { // 被反射的方法
        return fragTag;
    }

    public void setFragTag(String fragTag) {
        this.fragTag = fragTag;
    }

    private float mov_x, mov_y; //相对于手指移动了的位置
    private int left, right, top, bottom;
    private List<int[]> positionlist = new ArrayList<>();

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        super.onTouch(v, event);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            positionlist.clear();
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //     Logger.v("x:  " + event.getX() + "   y:  " + event.getY());
            mov_x = event.getX() - super.point_x;
            mov_y = event.getY() - super.point_y;

            left = rootLayout.getLeft();
            right = rootLayout.getRight();
            top = rootLayout.getTop();
            bottom = rootLayout.getBottom();

            if (Math.abs(dm2.widthPixels - rootLayout.getWidth()) > 5) {
                rootLayout.layout(left, top + (int) mov_y, right, bottom + (int) mov_y);

                int[] position = {left, top + (int) mov_y, right, bottom + (int) mov_y};
                positionlist.add(position);
                // Logger.v("left " + position[0] + " top " + position[1] + " right " + position[2] + "  bottom " + position[3] );

                if (Math.abs(position[1]) > rootLayout.getWidth() / 2) {
                    //  Logger.v("-    显示  删除 按钮     -");
                    EventBus.getDefault().post(new showDelImg(true));   //  发送消息
                } else {
                    EventBus.getDefault().post(new showDelImg(false));   //  发送消息
                }
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {

            if (positionlist.size() >= 2) {
                if (fragConst.fraglist.size() > 1 && Math.abs(positionlist.get(positionlist.size() - 1)[1]) > rootLayout.getWidth() / 2) {
                    //  Logger.v("-      删除  fragment    -");
                    delAnime();
                    EventBus.getDefault().post(new showDelImg(false));   //  发送消息
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            EventBus.getDefault().post(new deleteFragEvent(getFragTag()));   //  发送消息
                        }
                    }, 200);
                    return true;
                }
            } else {

                //放大或者缩小fragment
                EventBus.getDefault().post(new fragEvent(getFragTag()));   //  发送消息
            }

            for (int i = positionlist.size() - 1; i >= 0; i--) {
                rootLayout.layout(positionlist.get(i)[0], positionlist.get(i)[1], positionlist.get(i)[2], positionlist.get(i)[3]);
            }
//            rootLayout.layout(0, 0, rootLayout.getWidth(), rootLayout.getHeight());
        }
        return true;
    }


    //删除动画
    private void delAnime() {
        if (fragConst.fraglist.size() <= 1) {
            return;
        }
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.01f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.01f);
        ObjectAnimator scalexy = ObjectAnimator.ofPropertyValuesHolder(rootLayout, pvhX, pvhY);

        ObjectAnimator scale = ObjectAnimator.ofFloat(rootLayout, "translationY", 0, -2500);

        scale.setDuration(200);
        scalexy.setDuration(200);
        scale.start();
        scalexy.start();

    }

    @Subscribe
    public void onEventMainThread(baseEvent event) {
        // Toast.makeText(getActivity(), " 收到 event 数据  ", 0).show();

        if (event instanceof zoomEvent) {
            if (((zoomEvent) event).isMatchParent()) {
                mainLayout.setVisibility(View.INVISIBLE);
            } else {
                mainLayout.setVisibility(View.VISIBLE);
                delTitle.setText(title);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //EventBus.getDefault().unregister(this);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            webUrlStr.setText(title);
            webUrlStr.setHint(title);
//            changeStatusOfBottomButton();
            //add history
            if (!isPrivateBrowsing) {
                String date = new SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(new Date()).toString();
                favAndHisManager.addHistory(title, url, Long.parseLong(date));
            }
        }
    }

    private class ViewTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (view.getId() == R.id.web_url_str) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP && !webUrlStr.hasFocus()) {
                    webUrlStr.setFocusableInTouchMode(true);
                    webUrlStr.requestFocus();
                    webUrlStr.setText(url);
                    webUrlStr.selectAll();
                }
                webUrlStr.setCursorVisible(true);
                frameLayout.setVisibility(View.VISIBLE);
                setStatusOfSearch(1);
                if (flag == 0) {
                    PopupWindowUrl morePopWindow = new PopupWindowUrl(getActivity(), webHolder.getUrl(), webHolder.getFavicon());
                    morePopWindow.showPopupWindow(webUrlLayout);
                }
                flag = 1;

            } else if (view.getId() == R.id.Mask) {
                url = webHolder.getUrl();
                webUrlStr.setCursorVisible(false);
                webUrlStr.clearFocus();
                webUrlStr.setFocusableInTouchMode(false);
                webUrlStr.setText(title);
                frameLayout.setVisibility(View.GONE);
                setStatusOfSearch(3);
                flag = 0;
            }
            return false;
        }
    }

    //页面点击
    private class WebViewClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

        }
    }

    private class ButtonClickedListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.web_url_search) {
                flag = 0;
                frameLayout.setVisibility(View.GONE);
                webUrlStr.setFocusableInTouchMode(false);
                webUrlStr.clearFocus();
                url = webUrlStr.getText().toString();
                if (!(url.startsWith("http://") || url.startsWith("https://"))) {
                    url = "https://www.google.com/search?q=" + url;
                }
                webHolder.loadUrl(url);
            } else if (view.getId() == R.id.web_url_cancel) {
                url = webHolder.getUrl();
                webUrlStr.setText(url);
            } else if (view.getId() == R.id.web_url_fresh) {
                url = webHolder.getUrl();
                webHolder.loadUrl(url);
            } else if (view.getId() == R.id.pre_button) {

                if (webHolder.canGoBack())
                    webHolder.goBack();
                else {
                    webHolder.setVisibility(View.GONE);
                }

            } else if (view.getId() == R.id.next_button) {


                if (webHolder.canGoForward()) {
                    webHolder.setVisibility(View.VISIBLE);
                    webHolder.goForward();
                }

            } else if (view.getId() == R.id.home_button) {
                webHolder.loadUrl("https://www.google.com");
            } else if (view.getId() == R.id.window_button) {
                EventBus.getDefault().post(new windowEvent());
            } else if (view.getId() == R.id.tools_button) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        getContext(), R.style.BottomSheetDialoTheme);
                //Making a view of Menu to be transparent from the background for Curved corners
                View bottomSheetView = LayoutInflater.from(getContext())
                        .inflate(R.layout.pop_window_tools, (LinearLayout) view.findViewById(R.id.BottomSheetContainer));

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();

                bottomSheetDialogForIconChange = bottomSheetDialog;


                ////******************************* MENU BUTTONS ********************///////
                /////showbookmarks button
                bottomSheetView.findViewById(R.id.showbookmarksbtn).setOnClickListener(toolsClickedListener);
                /////////////////////////////////////////BOOKMARK BUTTON CLICKED HERE
                bottomSheetView.findViewById(R.id.addbookmarkbtn).setOnClickListener(toolsClickedListener);
                //Show History from Menu//
                bottomSheetView.findViewById(R.id.showhistorybtn).setOnClickListener(toolsClickedListener);
                //Show Downloads From Menu//
                bottomSheetView.findViewById(R.id.showdownloadsbtn).setOnClickListener(toolsClickedListener);
                //Incognito From Menu//
                bottomSheetView.findViewById(R.id.incognitobtn).setOnClickListener(toolsClickedListener);
                /////setting the icon for the first time as the menu opens
                if (incognito == 1) {
                    bottomSheetDialogForIconChange.findViewById(R.id.incognitobtn).setBackgroundResource(R.drawable.incognitoon);
                    bottomSheetDialogForIconChange.findViewById(R.id.incognitobtn).setScaleX(.5f);
                    bottomSheetDialogForIconChange.findViewById(R.id.incognitobtn).setScaleY(.6f);
                } else {

                    bottomSheetDialogForIconChange.findViewById(R.id.incognitobtn).setBackgroundResource(R.drawable.incognitooff);
                    bottomSheetDialogForIconChange.findViewById(R.id.incognitobtn).setScaleX(.5f);
                    bottomSheetDialogForIconChange.findViewById(R.id.incognitobtn).setScaleY(.6f);
                }
                //Show Themes From Menu//
                bottomSheetView.findViewById(R.id.showthemesbtn).setOnClickListener(toolsClickedListener);
                //Night Mode From Menu//
                bottomSheetView.findViewById(R.id.nightmodebtnbtn).setOnClickListener(toolsClickedListener);

//////////Assigning night mode buttons
                if (nightmode == 1) {
                    bottomSheetDialogForIconChange.findViewById(R.id.nightmodebtnbtn).setBackgroundResource(R.drawable.sun);
                    bottomSheetDialogForIconChange.findViewById(R.id.nightmodebtnbtn).setScaleX(.65f);
                    bottomSheetDialogForIconChange.findViewById(R.id.nightmodebtnbtn).setScaleY(.75f);

                } else {
                    bottomSheetDialogForIconChange.findViewById(R.id.nightmodebtnbtn).setBackgroundResource(R.drawable.moon);
                    bottomSheetDialogForIconChange.findViewById(R.id.nightmodebtnbtn).setScaleX(.5f);
                    bottomSheetDialogForIconChange.findViewById(R.id.nightmodebtnbtn).setScaleY(.6f);
                }
                //Reload Webpage From Menu//
                bottomSheetView.findViewById(R.id.reloadmenubtn).setOnClickListener(toolsClickedListener);
                //Show Feedback From Menu//
                bottomSheetView.findViewById(R.id.feedbackbtn).setOnClickListener(toolsClickedListener);
                //Show Share From Menu//
                bottomSheetView.findViewById(R.id.sharebtn).setOnClickListener(toolsClickedListener);
                ///Show Settings from Menu//
                bottomSheetView.findViewById(R.id.settingsbtn).setOnClickListener(toolsClickedListener);
                ////Menu down button
                bottomSheetView.findViewById(R.id.menudownbtn).setOnClickListener(toolsClickedListener);
                ///Exit button
                bottomSheetView.findViewById(R.id.exitbtn).setOnClickListener(toolsClickedListener);
//showing the star icon when cliccked on menu
                bottomSheetDialogForIconChange.findViewById(R.id.addbookmarkbtn).setBackgroundResource(R.drawable.star);
                bottomSheetDialogForIconChange.findViewById(R.id.addbookmarkbtn).setScaleX(.5f);
                bottomSheetDialogForIconChange.findViewById(R.id.addbookmarkbtn).setScaleY(.6f);

                cardView = bottomSheetView.findViewById(R.id.menuBar);
                if (nightmode == 1) {
                    bottomSheetDialogForIconChange.findViewById(R.id.addbookmarkbtn).setBackgroundResource(R.drawable.nightstar);
                    bottomSheetDialogForIconChange.findViewById(R.id.addbookmarkbtn).setScaleX(.5f);
                    bottomSheetDialogForIconChange.findViewById(R.id.addbookmarkbtn).setScaleY(.6f);

                    bottomSheetDialogForIconChange.findViewById(R.id.reloadmenubtn).setBackgroundResource(R.drawable.nightrefresh);
                    bottomSheetDialogForIconChange.findViewById(R.id.reloadmenubtn).setScaleX(.5f);
                    bottomSheetDialogForIconChange.findViewById(R.id.reloadmenubtn).setScaleY(.6f);

                    bottomSheetDialogForIconChange.findViewById(R.id.feedbackbtn).setBackgroundResource(R.drawable.nightfeedback);
                    bottomSheetDialogForIconChange.findViewById(R.id.feedbackbtn).setScaleX(.5f);
                    bottomSheetDialogForIconChange.findViewById(R.id.feedbackbtn).setScaleY(.6f);

                    bottomSheetDialogForIconChange.findViewById(R.id.incognitobtn).setBackgroundResource(R.drawable.nightincognitooff);
                    bottomSheetDialogForIconChange.findViewById(R.id.incognitobtn).setScaleX(.5f);
                    bottomSheetDialogForIconChange.findViewById(R.id.incognitobtn).setScaleY(.6f);

                    bottomSheetDialogForIconChange.findViewById(R.id.menudownbtn).setBackgroundResource(R.drawable.nightdownarrow);
                    bottomSheetDialogForIconChange.findViewById(R.id.menudownbtn).setScaleX(.2f);
                    bottomSheetDialogForIconChange.findViewById(R.id.menudownbtn).setScaleY(.4f);

                    bottomSheetDialogForIconChange.findViewById(R.id.settingsbtn).setBackgroundResource(R.drawable.nightsetting);
                    bottomSheetDialogForIconChange.findViewById(R.id.settingsbtn).setScaleX(.25f);
                    bottomSheetDialogForIconChange.findViewById(R.id.settingsbtn).setScaleY(.5f);

                    bottomSheetDialogForIconChange.findViewById(R.id.exitbtn).setBackgroundResource(R.drawable.nightpowera);
                    bottomSheetDialogForIconChange.findViewById(R.id.exitbtn).setScaleX(.23f);
                    bottomSheetDialogForIconChange.findViewById(R.id.exitbtn).setScaleY(.5f);

                    cardView.setCardBackgroundColor(Color.DKGRAY);
                } else {
                    bottomSheetDialogForIconChange.findViewById(R.id.reloadmenubtn).setBackgroundResource(R.drawable.refresh);
                    bottomSheetDialogForIconChange.findViewById(R.id.reloadmenubtn).setScaleX(.5f);
                    bottomSheetDialogForIconChange.findViewById(R.id.reloadmenubtn).setScaleY(.6f);

                    bottomSheetDialogForIconChange.findViewById(R.id.feedbackbtn).setBackgroundResource(R.drawable.feedback);
                    bottomSheetDialogForIconChange.findViewById(R.id.feedbackbtn).setScaleX(.5f);
                    bottomSheetDialogForIconChange.findViewById(R.id.feedbackbtn).setScaleY(.6f);

                    bottomSheetDialogForIconChange.findViewById(R.id.menudownbtn).setBackgroundResource(R.drawable.downarrow);
                    bottomSheetDialogForIconChange.findViewById(R.id.menudownbtn).setScaleX(.2f);
                    bottomSheetDialogForIconChange.findViewById(R.id.menudownbtn).setScaleY(.4f);

                    bottomSheetDialogForIconChange.findViewById(R.id.settingsbtn).setBackgroundResource(R.drawable.setting);
                    bottomSheetDialogForIconChange.findViewById(R.id.settingsbtn).setScaleX(.25f);
                    bottomSheetDialogForIconChange.findViewById(R.id.settingsbtn).setScaleY(.5f);

                    bottomSheetDialogForIconChange.findViewById(R.id.exitbtn).setBackgroundResource(R.drawable.powera);
                    bottomSheetDialogForIconChange.findViewById(R.id.exitbtn).setScaleX(.25f);
                    bottomSheetDialogForIconChange.findViewById(R.id.exitbtn).setScaleY(.5f);
                    cardView.setCardBackgroundColor(Color.WHITE);
                }


            }
        }
    }

    //功能弹出窗口按钮
    private class ToolsClickedListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.incognitobtn) {
                //无痕浏览
                if (isPrivateBrowsing) {


                    bottomSheetDialogForIconChange.dismiss();
                    if (nightmode == 1) {
                        ImageView privateBrowsing = (ImageView) bottomSheetDialogForIconChange.findViewById(R.id.incognitobtn);
                        privateBrowsing.setBackgroundResource(R.drawable.nightincognitooff);
                        privateBrowsing.setScaleX(.5f);
                        privateBrowsing.setScaleY(.6f);
                    } else {
                        ImageView privateBrowsing = (ImageView) bottomSheetDialogForIconChange.findViewById(R.id.incognitobtn);
                        privateBrowsing.setBackgroundResource(R.drawable.incognitooff);
                        privateBrowsing.setScaleX(.5f);
                        privateBrowsing.setScaleY(.6f);
                    }


                    isPrivateBrowsing = false;
                    incognito = 0;
                    changebackground();

                    Intent intent = new Intent();
                    intent.setClass(getActivity(), Incognitooff.class);
                    intent.putExtra("type", "incognitoff");
                    startActivityForResult(intent, MainActivity.REQUEST_OPEN_INCOGNITOOFF);


                } else {
                    bottomSheetDialogForIconChange.dismiss();
                    ////changing the icon of the incognito mode
                    ImageView privateBrowsing = bottomSheetDialogForIconChange.findViewById(R.id.incognitobtn);

                    privateBrowsing.setBackgroundResource(R.drawable.incognitoon);
                    privateBrowsing.setScaleX(.5f);
                    privateBrowsing.setScaleY(.6f);


                    isPrivateBrowsing = true;
                    linear.setBackgroundResource(R.drawable.incognitoback);
                    incognito = 1;

                    Intent intent = new Intent();
                    intent.setClass(getActivity(), Incognito.class);
                    intent.putExtra("type", "incognito ");
                    startActivityForResult(intent, MainActivity.REQUEST_OPEN_INCOGNITOON);


                }

            } else if (view.getId() == R.id.addbookmarkbtn) {

                if (addbookmark == 0) {
                    ImageView add_favorite_button = (ImageView) bottomSheetDialogForIconChange.findViewById(R.id.addbookmarkbtn);
                    add_favorite_button.setBackgroundResource(R.drawable.addbookmark);
                    add_favorite_button.setScaleX(.5f);
                    add_favorite_button.setScaleY(.6f);
                    addbookmark = 1;
                } else {
                    ImageView add_favorite_button = (ImageView) bottomSheetDialogForIconChange.findViewById(R.id.addbookmarkbtn);
                    add_favorite_button.setBackgroundResource(R.drawable.stara);
                    add_favorite_button.setScaleX(.5f);
                    add_favorite_button.setScaleY(.6f);
                    addbookmark = 0;
                }

                //添加书签
                favAndHisManager.addFavorite(title, url);
                favAndHisManager.getAllFavorites();

            } else if (view.getId() == R.id.showbookmarksbtn) {
                //View and edit bookmarks
                bottomSheetDialogForIconChange.dismiss();
                Intent intent = new Intent();
                intent.setClass(getActivity(), FavAndHisActivity.class);
                intent.putExtra("type", "favorite");
                startActivityForResult(intent, MainActivity.REQUEST_OPEN_FAV_OR_HIS);
            } else if (view.getId() == R.id.showhistorybtn) {
                //View edit history
                bottomSheetDialogForIconChange.dismiss();
                Intent intent = new Intent();
                intent.setClass(getActivity(), FavAndHisActivity.class);
                intent.putExtra("type", "history");
                startActivityForResult(intent, MainActivity.REQUEST_OPEN_FAV_OR_HIS);

            } else if (view.getId() == R.id.showthemesbtn) {
                bottomSheetDialogForIconChange.dismiss();
                Intent intent = new Intent();
                intent.setClass(getActivity(), Theme.class);
                intent.putExtra("type", "theme");
                startActivityForResult(intent, MainActivity.REQUEST_OPEN_THEMES);
            } else if (view.getId() == R.id.nightmodebtnbtn) {

                if (nightmode == 0) {

                    bottomSheetDialogForIconChange.dismiss();
                    nightmode = 1;
                    final SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("nightmode", String.valueOf(nightmode));
                    editor.apply();
                    //change background black
                    linear.setBackgroundResource(R.drawable.nightmodeon);

                    editback.setBackgroundColor(Color.DKGRAY);


                    ImageView privateBrowsing = (ImageView) bottomSheetDialogForIconChange.findViewById(R.id.incognitobtn);

                    privateBrowsing.setBackgroundResource(R.drawable.nightincognitooff);
                    privateBrowsing.setScaleX(.5f);
                    privateBrowsing.setScaleY(.6f);

                    ImageView feedbackbtn = (ImageView) bottomSheetDialogForIconChange.findViewById(R.id.feedbackbtn);
                    feedbackbtn.setBackgroundResource(R.drawable.nightfeedback);
                    feedbackbtn.setScaleX(.5f);
                    feedbackbtn.setScaleY(.6f);

                    ImageView reloadmenubtn = (ImageView) bottomSheetDialogForIconChange.findViewById(R.id.reloadmenubtn);
                    reloadmenubtn.setBackgroundResource(R.drawable.nightrefresh);
                    reloadmenubtn.setScaleX(.5f);
                    reloadmenubtn.setScaleY(.6f);

                    ImageView nightmodebtnbtn = (ImageView) bottomSheetDialogForIconChange.findViewById(R.id.nightmodebtnbtn);
                    nightmodebtnbtn.setBackgroundResource(R.drawable.sun);
                    nightmodebtnbtn.setScaleX(.65f);
                    nightmodebtnbtn.setScaleY(.75f);

                    ImageView add_favorite_button = (ImageView) bottomSheetDialogForIconChange.findViewById(R.id.addbookmarkbtn);
                    add_favorite_button.setBackgroundResource(R.drawable.nightstar);
                    add_favorite_button.setScaleX(.5f);
                    add_favorite_button.setScaleY(.6f);

                    Intent intent = new Intent();
                    intent.setClass(getActivity(), NightModeOn.class);
                    intent.putExtra("type", "nightmodeon");
                    startActivityForResult(intent, MainActivity.REQUEST_NIGHT_MODE_ON);


                } else {

                    editback.setBackgroundColor(Color.WHITE);

                    //CHANGE MENU BACKGROUND
                    nightmode = 0;
//change background according to theme

                    bottomSheetDialogForIconChange.dismiss();

                    final SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("nightmode", String.valueOf(nightmode));
                    editor.apply();
                    //change background back
                    changebackground();

                    ImageView feedbackbtn = (ImageView) bottomSheetDialogForIconChange.findViewById(R.id.feedbackbtn);
                    feedbackbtn.setBackgroundResource(R.drawable.feedback);
                    feedbackbtn.setScaleX(.5f);
                    feedbackbtn.setScaleY(.6f);

                    ImageView reloadmenubtn = (ImageView) bottomSheetDialogForIconChange.findViewById(R.id.reloadmenubtn);
                    reloadmenubtn.setBackgroundResource(R.drawable.reload);
                    reloadmenubtn.setScaleX(.5f);
                    reloadmenubtn.setScaleY(.6f);

                    ImageView nightmodebtnbtn = (ImageView) bottomSheetDialogForIconChange.findViewById(R.id.nightmodebtnbtn);
                    nightmodebtnbtn.setBackgroundResource(R.drawable.moon);
                    nightmodebtnbtn.setScaleX(.5f);
                    nightmodebtnbtn.setScaleY(.6f);

                    ImageView add_favorite_button = (ImageView) bottomSheetDialogForIconChange.findViewById(R.id.addbookmarkbtn);
                    add_favorite_button.setBackgroundResource(R.drawable.star);
                    add_favorite_button.setScaleX(.5f);
                    add_favorite_button.setScaleY(.6f);

                    Intent intent = new Intent();
                    intent.setClass(getActivity(), NighhtModeOff.class);
                    intent.putExtra("type", "nightmodeoff");
                    startActivityForResult(intent, MainActivity.REQUEST_NIGHT_MODE_OFF);


                }


            } else if (view.getId() == R.id.showdownloadsbtn) {
                bottomSheetDialogForIconChange.dismiss();
                Intent intent = new Intent();
                intent.setClass(getActivity(), DownloaderNew.class);
                intent.putExtra("type", "download");
                startActivityForResult(intent, MainActivity.REQUEST_OPEN_DOWNLOADS);
            } else if (view.getId() == R.id.menudownbtn) {
                bottomSheetDialogForIconChange.dismiss();

            } else if (view.getId() == R.id.exitbtn) {
                bottomSheetDialogForIconChange.dismiss();
                if (donotshowagain == 0) {
                    Dialog bottomSheetDialog = new Dialog(
                            getContext(), R.style.BottomSheetDialoTheme);
                    //Making a view of Menu to be transparent from the background for Curved corners
                    View bottomSheetView = LayoutInflater.from(getContext())
                            .inflate(R.layout.activity_exit, (LinearLayout) bottomSheetDialog.findViewById(R.id.BottomSheetContainer1));

                    bottomSheetDialog.setContentView(bottomSheetView);
                    bottomSheetDialog.show();

                    bottomSheetDialog.findViewById(R.id.exitconfirmbtn).setOnClickListener(toolsClickedListener);
                    bottomSheetDialog.findViewById(R.id.cancelconfirmbtn).setOnClickListener(toolsClickedListener);

                    bottomSheetDialog.findViewById(R.id.clearHistory).setOnClickListener(toolsClickedListener);
                    ClearHistory = bottomSheetDialog.findViewById(R.id.clearHistory);

                    bottomSheetDialog.findViewById(R.id.doNotShowAgain).setOnClickListener(toolsClickedListener);
                    doNotShowAgain = bottomSheetDialog.findViewById(R.id.doNotShowAgain);
                } else {

                    System.exit(0);
                }

            }

            //To take screen shot of the web page
//            else if (view.getId() == R.id.window_screenshot) {
//                //网页截图或全屏截图
//                bottomSheetDialogForIconChange.dismiss();
//                verifyStoragePermissions(getActivity());
//                View sView;
//                String tempImgName;
//                if (view.getId() == R.id.window_screenshot) {
//                    sView = Objects.requireNonNull(getActivity()).getWindow().getDecorView();
//                    tempImgName = "window_capture" + windowScreenShotCount + ".jpg";
//                    windowScreenShotCount++;
//                }
//                else {
//                    sView = webHolder;
//                    tempImgName = "webview_capture" + pageScreenShotCount + ".jpg";
//                    pageScreenShotCount++;
//                }
//                final Bitmap sBitmap = Bitmap.createBitmap(sView.getWidth(), sView.getHeight(), Bitmap.Config.ARGB_8888);
//                Canvas sCanvas = new Canvas(sBitmap);
//                sView.draw(sCanvas);
//                View dialogSaveImg = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_saveimg, null);
//                choosePath = (TextView) dialogSaveImg.findViewById(R.id.dialog_savePath_enter);
//                imgSaveName = (TextView) dialogSaveImg.findViewById(R.id.dialog_fileName_input);
//                final String imgName = tempImgName;
//                imgSaveName.setText(imgName);
//                choosePath.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (view.getId() == R.id.dialog_savePath_enter) {
//                            Intent imgSavePath = new Intent(getActivity(), FileActivity.class);
//                            imgSavePath.putExtra("savePath", choosePath.getText().toString());
//                            startActivityForResult(imgSavePath, MainActivity.REQUEST_SAVE_IMAGE_PATH);
//                        }
//                    }
//                });
//                saveImageToChoosePath = new AlertDialog.Builder(getActivity())
//                        .setTitle("Choose path")
//                        .setView(dialogSaveImg)
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                new ImageCaptureManager(getActivity(), imgName, choosePath.getText().toString(), sBitmap);
//                                Toast.makeText(getActivity(), "Screenshot saved", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .setNegativeButton("Cancel", null)
//                        .create();
//                saveImageToChoosePath.show();
//
//            }
            else if (view.getId() == R.id.sharebtn) {

                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String body = "INDIAN UC BROWSER : MADE IN INDIA ";
                String sub = "URL";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, sub);
                myIntent.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(myIntent, "Share Using"));

            } else if (view.getId() == R.id.reloadmenubtn) {
                url = webHolder.getUrl();
                webHolder.loadUrl(url);
            } else if (view.getId() == R.id.exitbtn) {

            } else if (view.getId() == R.id.settingsbtn) {
                bottomSheetDialogForIconChange.dismiss();
                Intent intent = new Intent();
                intent.setClass(getActivity(), Settings.class);
                intent.putExtra("type", "settings");
                startActivityForResult(intent, MainActivity.REQUEST_OPEN_SETTINGS);
            } else if (view.getId() == R.id.menudownbtn) {
                bottomSheetDialogForIconChange.dismiss();
            } else if (view.getId() == R.id.feedbackbtn) {
                bottomSheetDialogForIconChange.dismiss();
                Intent intent = new Intent();
                intent.setClass(getActivity(), Feedback.class);
                intent.putExtra("type", "feedback");
                startActivityForResult(intent, MainActivity.REQUEST_OPEN_FEEDBACK);
            }

//            else if (view.getId() == R.id.page_edit) {
//                //编辑网页
//                bottomSheetDialogForIconChange.dismiss();
//                View sView;
//                sView = getActivity().getWindow().getDecorView();
//                final Bitmap sBitmap = Bitmap.createBitmap(sView.getWidth(), sView.getHeight(), Bitmap.Config.ARGB_8888);
//                Canvas sCanvas = new Canvas(sBitmap);
//                sView.draw(sCanvas);
//                Intent intent = new Intent();
//                PaintActivity.originalBitmap = sBitmap;
//                intent.setClass(getActivity(), PaintActivity.class);
//                startActivity(intent);
//            }
////////////////////////////////******************** For Exit Menu DialogBBox
            switch (view.getId()) {
                case R.id.exitconfirmbtn:
                    if (ClearHistory.isChecked()) {
                        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("ClearHistroy", String.valueOf(1));
                        editor.apply();
                    } else {

                        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("ClearHistroy", String.valueOf(0));
                        editor.apply();
                    }
//exit dialog box
                    exit.dismiss();
//exit application
                    System.exit(0);
                    break;
                case R.id.clearHistory:
                    if (ClearHistory.isChecked()) {
                        final SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                        editor1.putString("ClearHistory", String.valueOf(1));
                        editor1.apply();


                    } else if (!ClearHistory.isChecked()) {
                        final SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                        editor1.putString("ClearHistory", String.valueOf(0));
                        editor1.apply();

                    }
                    break;
                case R.id.doNotShowAgain:
                    if (doNotShowAgain.isChecked()) {
                        final SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                        editor1.putString("show", String.valueOf(1));
                        editor1.apply();


                    } else if (!doNotShowAgain.isChecked()) {
                        final SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
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

    private void changenightmodeicon(int nightmode) {

    }

    //图片长按弹出窗口操作
    private class ImageClickedListener implements View.OnClickListener {

        private int type;
        private String value;


        public ImageClickedListener(int type, String value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public void onClick(View v) {
            itemLongClickedPopWindow.dismiss();
            if (v.getId() == R.id.item_longclicked_viewImage) {
                //查看图片
                new RequestShowImageOnline(getActivity()).execute(value);
            } else if (v.getId() == R.id.item_longclicked_saveImage) {
                //保存图片
                verifyStoragePermissions(getActivity());
                View dialogSaveImg = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_saveimg, null);
                choosePath = (TextView) dialogSaveImg.findViewById(R.id.dialog_savePath_enter);
                imgSaveName = (TextView) dialogSaveImg.findViewById(R.id.dialog_fileName_input);
                final String imgName = value.substring(value.lastIndexOf("/") + 1);
                imgSaveName.setText(imgName);
                choosePath.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (view.getId() == R.id.dialog_savePath_enter) {
                            Intent imgSavePath = new Intent(getActivity(), FileActivity.class);
                            imgSavePath.putExtra("savePath", choosePath.getText().toString());
                            startActivityForResult(imgSavePath, MainActivity.REQUEST_SAVE_IMAGE_PATH);
                        }
                    }
                });
                saveImageToChoosePath = new AlertDialog.Builder(getActivity())
                        .setTitle("Choose path")
                        .setView(dialogSaveImg)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new ImageDownloadManager(getActivity()).execute(imgName, value, choosePath.getText().toString());
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                saveImageToChoosePath.show();
            } else if (v.getId() == R.id.item_longclicked_viewImageAttributes) {
                //查看图片属性
                size = String.valueOf(webHolder.getContentHeight()) + "×" + String.valueOf(getActivity().getWindowManager().getDefaultDisplay().getHeight());
                encoding = webHolder.getSettings().getDefaultTextEncodingName();
                Intent intent = new Intent(getActivity(), PageAttributesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("type", type);
                bundle.putString("typeUrl", value);
                bundle.putString("title", title);
                bundle.putString("url", url);
                bundle.putString("size", size);
                bundle.putString("encoding", encoding);
                intent.putExtras(bundle);
                startActivity(intent);
            }

        }
    }


    //超链接长按弹出窗口
    private class AchorClickedListener implements View.OnClickListener {

        private int type;
        private String value;

        public AchorClickedListener(int type, String value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public void onClick(View v) {
            itemLongClickedPopWindow.dismiss();
            if (v.getId() == R.id.item_longclicked_openAchor) {
                webHolder.loadUrl(value);
            } else if (v.getId() == R.id.item_longclicked_copyAchor) {
                clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null, url));

            }
        }
    }

    public class WebUrlStrWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

        }

        public void afterTextChanged(Editable editable) {

        }

        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

        }
    }


    /*
     * WebChromeClient
     * ProgressBar
     * */
    private class MyChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                setStatusOfSearch(3);
                webProgressBar.setVisibility(View.GONE);
//                swipeLayout.setRefreshing(false);
            } else {
                url = webHolder.getUrl();
                webUrlStr.setText(url);
                setStatusOfSearch(2);
                webProgressBar.setVisibility(View.VISIBLE);
                webProgressBar.setProgress(newProgress);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            mainFrag.this.title = title;
        }
    }

    private class WebViewTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //only for WebView
            if (v.getId() == R.id.web_holder) {
                return myGestureDetector.onTouchEvent(event);
            }
            return false;
        }
    }

    /*
     * TODO add gesture
     * distinguish the gesture on WebView
     * hide the webUrlLayout when Fling down
     * appear the webUrlLayout when Fling up
     * */
    private class GestureListener implements GestureDetector.OnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (webHolder.getScrollY() == 0) {
                webUrlLayout.setVisibility(View.VISIBLE);
            }
            if (webHolder.getScrollY() > 0) {
                webUrlLayout.setVisibility(View.GONE);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }
    }

    private static class PointerXY {
        public static int x;
        public static int y;

        public static int getX() {
            return x;
        }

        public static int getY() {
            return y;
        }

    }

    /*
     * WebView Long Clicked Listener
     */
    private class WebViewLongClickedListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            WebView.HitTestResult result = ((WebView) v).getHitTestResult();
            if (null == result)
                return false;
            int type = result.getType();
            if (type == WebView.HitTestResult.UNKNOWN_TYPE)
                return false;
            if (type == WebView.HitTestResult.EDIT_TEXT_TYPE) {
                return true;
            }

            //Setup custon handling depending on the type
            switch (type) {
                case WebView.HitTestResult.SRC_ANCHOR_TYPE:
                    itemLongClickedPopWindow = new ItemLongClickedPopWindow(getActivity(), ItemLongClickedPopWindow.ACHOR_VIEW_POPUPWINDOW);
                    itemLongClickedPopWindow.showAtLocation(v, Gravity.TOP | Gravity.LEFT, PointerXY.getX(), PointerXY.getY() + 10);
                    TextView openAchor = (TextView) itemLongClickedPopWindow.getView(R.id.item_longclicked_openAchor);
                    TextView copyAchor = (TextView) itemLongClickedPopWindow.getView(R.id.item_longclicked_copyAchor);
                    achorClickedListener = new AchorClickedListener(result.getType(), result.getExtra());
                    openAchor.setOnClickListener(achorClickedListener);
                    copyAchor.setOnClickListener(achorClickedListener);
                    break;
                case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE:
                case WebView.HitTestResult.IMAGE_TYPE:
                    itemLongClickedPopWindow = new ItemLongClickedPopWindow(getActivity(), ItemLongClickedPopWindow.IMAGE_VIEW_POPUPWINDOW);
                    itemLongClickedPopWindow.showAtLocation(v, Gravity.TOP | Gravity.LEFT, PointerXY.getX(), PointerXY.getY() + 10);
                    TextView viewImage = (TextView) itemLongClickedPopWindow.getView(R.id.item_longclicked_viewImage);
                    TextView saveImage = (TextView) itemLongClickedPopWindow.getView(R.id.item_longclicked_saveImage);
                    TextView viewImageAttributes = (TextView) itemLongClickedPopWindow.getView(R.id.item_longclicked_viewImageAttributes);
                    imageClickedListener = new ImageClickedListener(result.getType(), result.getExtra());
                    viewImage.setOnClickListener(imageClickedListener);
                    saveImage.setOnClickListener(imageClickedListener);
                    viewImageAttributes.setOnClickListener(imageClickedListener);
                    break;
                default:
                    break;

            }
            return true;
        }
    }
    /*
     * back button(mobile)
     * one: the last page
     * twice: exit program
     * */
//    Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            isQuit = false;
//        }
//    };
//
//    @Override
//    public void getActivity().onBackPressed() {
//        if (webHolder.canGoBack()) {
//            webHolder.goBack();
//        } else {
//            if (!isQuit) {
//                isQuit = true;
//                Toast.makeText(getActivity().getApplicationContext(), "press again exit program",
//                        Toast.LENGTH_SHORT).show();
//                mHandler.sendEmptyMessageDelayed(0,2000);
//            } else {
//                getActivity().finish();
//                System.exit(0);
//            }
//        }
//    }

    /*
     * change the status of search button
     * search
     * cancel
     * fresh
     * three status
     * */


    public void setStatusOfSearch(int status) {
        if (status == 1) {
            //search status
            webUrlSearch.setVisibility(View.VISIBLE);
            webUrlCancel.setVisibility(View.GONE);
            webUrlFresh.setVisibility(View.GONE);
        } else if (status == 2) {
            //cancel status
            webUrlSearch.setVisibility(View.GONE);
            webUrlCancel.setVisibility(View.VISIBLE);
            webUrlFresh.setVisibility(View.GONE);
        } else {
            //fresh status
            webUrlSearch.setVisibility(View.GONE);
            webUrlCancel.setVisibility(View.GONE);
            webUrlFresh.setVisibility(View.VISIBLE);
        }
    }

    /*
     *set the status of bottom buttons
     * GoBack
     * GoForward
     */
    // to change the status of the back and  forward button
//    public void changeStatusOfBottomButton() {
//        if (webHolder.canGoBack()) {
//            pagePre.setEnabled(true);
//        } else {
//            pagePre.setEnabled(false);
//        }
//        if (webHolder.canGoForward()) {
//            pageNext.setEnabled(true);
//        } else {
//            pageNext.setEnabled(false);
//        }
//    }

    //Receive bookmark/historical return processing
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//for changing the background
        changebackground();

        switch (resultCode) {
            case MainActivity.REQUEST_DEFAULT:
                break;


            case FavAndHisActivity.RESULT_FAV_HIS:
                webHolder.loadUrl(data.getStringExtra("url"));
                break;

            case FileActivity.RESULT_FILEMANAGER:
                choosePath.setText(data.getStringExtra("savePath"));
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /////////////// Change Background for the  theme
    private void changebackground() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        themeNumber = Integer.parseInt(sharedPreferences.getString("number", "0"));
        Log.d("theme", "init: " + themeNumber);

        ///for  exit menu
        SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        donotshowagain = Integer.parseInt(sharedPreferences1.getString("show", "0"));

        nightmode = Integer.parseInt(sharedPreferences.getString("nightmode", "0"));

        if (nightmode == 1) {
            linear.setBackgroundResource(R.drawable.nightmodeon);
            editback.setBackgroundColor(Color.DKGRAY);

        } else {

            switch (themeNumber) {
                case 1:
                    linear.setBackgroundResource(R.drawable.back1);
                    break;
                case 2:
                    linear.setBackgroundResource(R.drawable.back2);
                    break;
                case 3:
                    linear.setBackgroundResource(R.drawable.back3);
                    break;
                case 4:
                    linear.setBackgroundResource(R.drawable.back4);
                    break;
                case 5:
                    linear.setBackgroundResource(R.drawable.back5);
                    break;
                case 6:
                    linear.setBackgroundResource(R.drawable.back6);
                    break;
                case 7:
                    linear.setBackgroundResource(R.drawable.back7);
                    break;
                case 8:
                    linear.setBackgroundResource(R.drawable.back8);
                    break;
                case 9:
                    linear.setBackgroundResource(R.drawable.back9);
                    break;
                case 10:
                    linear.setBackgroundResource(R.drawable.back10);
                    break;
                case 11:
                    linear.setBackgroundResource(R.drawable.back11);
                    break;
                case 12:
                    linear.setBackgroundResource(R.drawable.back12);
                    break;
                case 13:
                    linear.setBackgroundResource(R.drawable.back13);
                    break;
                case 14:
                    linear.setBackgroundResource(R.drawable.back14);
                    break;

            }
        }


    }

    //获取存储权限
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    public void onRefresh() {
//        url = webHolder.getUrl();
//        webHolder.loadUrl(url);
    }
}
