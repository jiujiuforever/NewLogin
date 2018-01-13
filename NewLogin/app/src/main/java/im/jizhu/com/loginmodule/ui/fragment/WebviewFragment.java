//package im.jizhu.com.loginmodule.ui.fragment;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.webkit.CookieManager;
//import android.webkit.DownloadListener;
//import android.webkit.JsResult;
//import android.webkit.WebChromeClient;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.ProgressBar;
//
//import im.jizhu.com.loginmodule.R;
//import im.jizhu.com.loginmodule.utils.WebViewDownloader;
//
//
//@SuppressLint("SetJavaScriptEnabled")
//public class WebviewFragment extends MainFragment {
//    private View curView = null;
//    public static String url;
//    private Context context;
//    private static String cookieValue;
//    private ProgressBar mProgressbar;
//    private String scanCode;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        //extractUidFromUri();
//        if (null != curView) {
//            ((ViewGroup) curView.getParent()).removeView(curView);
//            return curView;
//        }
//        curView = inflater.inflate(R.layout.tt_fragment_webview, topContentView);
//        super.init(curView);
//        context = curView.getContext();
//        showProgressBar();
//        initRes();
//
//        LayoutInflater inflater2 = LayoutInflater.from(getActivity());
//        View v = inflater2.inflate(R.layout.tt_update_progress, null);
//        mProgressbar = (ProgressBar) v.findViewById(R.id.progress);
//
//        return curView;
//    }
//
//    private void initRes() {
//        // 设置顶部标题栏
//        setTopTitleBold(getActivity().getString(R.string.main_innernet));
//        topLeftContainerLayout.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                getActivity().finish();
//            }
//        });
//        setTopLeftText(getResources().getString(R.string.top_left_back));
//
//        final WebView webView = (WebView) curView.findViewById(R.id.webView1);
//        webView.getSettings().setJavaScriptEnabled(true);
//
//        // 设置出现缩放工具
//        webView.getSettings().setBuiltInZoomControls(true);
//
//        //扩大比例的缩放
//        webView.getSettings().setUseWideViewPort(true);
//        //自适应屏幕
//        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webView.getSettings().setLoadWithOverviewMode(true);
//
//        webView.setVerticalScrollBarEnabled(false);
//        webView.setHorizontalScrollBarEnabled(false);
//
//        webView.loadUrl(url);
//
//        webView.setWebChromeClient(new WebChromeClient(){
//            @Override
//            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//                return super.onJsAlert(view, url, message, result);
//            }
//        });
//
//        webView.setWebViewClient(new WebViewClient() {
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                setTopTitle(view.getTitle());
//                hideProgressBar();
//            }
//
//            @Override
//            public void onReceivedError(WebView view, int errorCode,
//                                        String description, String failingUrl) {
//                // TODO Auto-generated method stub
//                super.onReceivedError(view, errorCode, description, failingUrl);
//                hideProgressBar();
//            }
//
//            @Override
//            // 在WebView中而不是默认浏览器中显示页面
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                //设置Cookie
//                CookieManager cookieManager = CookieManager.getInstance();
//                cookieValue = cookieManager.getCookie(url);
//                view.loadUrl(url);
//                return true;
//            }
//
//            @Override
//            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
//                return super.shouldOverrideKeyEvent(view, event);
//            }
//
//        });
//
//        //设置点击手机上的后退按钮,让WebView后退一页
//        webView.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
//                        webView.goBack();
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });
//
//        //解决webview邮箱不可下载功能
//        webView.setDownloadListener(new DownloadListener() {
//            @Override
//            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
//                Log.d("webviewCookie", "onDownLoadStart()" + cookieValue);
//                Log.d("WebViewDownload", "url = " + url);
//                Log.d("WebViewDownload", "userAgent = " + userAgent);
//                Log.d("WebViewDownload", "contentDisposition = " + contentDisposition);
//                Log.d("WebViewDownload", "mimetype = " + mimetype);
//                Log.d("WebViewDownload", "contentLength = " + contentLength);
//
//                Intent intent1=getActivity().getIntent();
//                scanCode = intent1.getStringExtra("scanCode");
//
//
//                if (scanCode.equals("httpDown")) {
//
//                    Uri uri = Uri.parse(url);
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    startActivity(intent);
//                }else {
//                    WebViewDownloader webViewDownloader = new WebViewDownloader(getActivity(),
//                            url, userAgent, contentDisposition, mimetype, contentLength);
//                    webViewDownloader.setCookieValue(cookieValue);
//                    webViewDownloader.showNoticeDialog(contentLength);
//                }
//
//            }
//        });
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//
//    @Override
//    protected void initHandler() {
//    }
//
//    /**
//     * @param str
//     */
//    public static void setUrl(String str) {
//        url = str;
//    }
//
//    private static final String SCHEMA = "im.jizhu.com.loginmodule://message_private_url";
//    private static final String PARAM_UID = "uid";
//    private static final Uri PROFILE_URI = Uri.parse(SCHEMA);
//
//    private void extractUidFromUri() {
//        Uri uri = getActivity().getIntent().getData();
//        if (uri != null && PROFILE_URI.getScheme().equals(uri.getScheme())) {
//            url = uri.getQueryParameter(PARAM_UID);
//        }
//        if (url.indexOf("www") == 0) {
//            url = "http://" + url;
//        } else if (url.indexOf("https") == 0) {
//            String bUid = url.substring(5, url.length());
//            url = "http" + bUid;
//        }
//    }
//
//
//
//}
