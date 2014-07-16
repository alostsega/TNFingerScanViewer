package th.co.thinknet.atit.tnfingerscanviewer;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.util.Calendar;


public class MainActivity extends Activity {
       PlaceholderFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            fragment = new PlaceholderFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        public WebView webView;
        public boolean isAuth;
        public boolean isSelectTiemAttendant;
        public boolean isSetMonth;
        public String attendantUrl;
        public static ProgressDialog pd;
        private UserData userData;

        public PlaceholderFragment() {
            isAuth = false;
            isSelectTiemAttendant = false;
            isSetMonth = false;
            attendantUrl = "";

            userData = UserData.getInstance();

        }

        @Override
        public void onResume() {
            super.onResume();
            //First Check Login
            resetVariable();
            webView.loadUrl("https://id.thinknet.co.th/user/checkSession?url=http%3A%2F%2Fmy.thinknet.co.th%2Fauth");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            webView = (WebView)rootView.findViewById(R.id.mainWebView);
            webView.getSettings().setJavaScriptEnabled(true);
            //webView.getSettings().setBuiltInZoomControls(true);

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }

                @Override
                public void onPageFinished(final WebView view,final String url) {
                    super.onPageFinished(view, url);

                    if(url.equals("https://id.thinknet.co.th/user/loginForm?url=http://my.thinknet.co.th/auth")){
                        Log.d("Test","Auth");
                        pd = ProgressDialog.show(getActivity(),"Please wait ...","Fetching Data",true,true);

                        webView.post(new Runnable() {
                            @Override
                            public void run() {
                                webView.loadUrl("javascript:" + "(function(){" +
                                        "document.getElementById('user_login').value = '"+userData.getDataModel().getUsername()+"';" +
                                        "document.getElementById('user_pass').value = '"+userData.getDataModel().getPassword()+"';" +
                                        "document.getElementById('loginform').submit();" +
                                        "})()");

                                isAuth = true;
                            }
                        });
                    }
                    else if(url.equals("http://my.thinknet.co.th/index.php")){

                        if(!isSelectTiemAttendant) {
                            Log.d("Test","Click to setting attendant");
                            if(pd == null){
                                pd = ProgressDialog.show(getActivity(),"Please wait ...","Fetching Data",true,true);
                            }

                            webView.post(new Runnable() {
                                @Override
                                public void run() {

                                    webView.loadUrl("javascript:" + "(function(){" +
                                            "selectApplication('Timeattendant');" +
                                            "})()");
                                    delay(3000);

                                    //For Android 4.1.2
                                    if(19 > android.os.Build.VERSION.SDK_INT){
                                        //webView.loadUrl("http://hris.thinknet.co.th/employee/attendant.php");
                                        onPageFinished(view,url);

                                    }
                                }
                            });
                            isSelectTiemAttendant = true;
                        }
                        //For Android 4.4.2
                        else if(isSelectTiemAttendant){
                            if(19 >= android.os.Build.VERSION.SDK_INT) {
                                Log.d("Test", "Go to time attendant");
                                webView.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        webView.loadUrl("http://hris.thinknet.co.th/employee/attendant.php");
                                        delay(2000);
                                        //
                                    }
                                });
                            }
                        }
                    }

                    else if(url.equals("http://hris.thinknet.co.th/employee/attendant.php")){

                        if(!isSetMonth) {
                            Log.d("Test","Prepare Data");


                            webView.post(new Runnable() {
                                @Override
                                public void run() {
                                    Calendar c = Calendar.getInstance();

                                    String year = Integer.toString(c.get(Calendar.YEAR));;
                                    String month = Integer.toString(c.get(Calendar.MONTH)+1);
                                    String amount = "1";

                                    webView.loadUrl("javascript:" + "(function(){" +
                                            "document.getElementById('year').value = '"+year+"';" +
                                            "document.getElementById('month').value = '"+month+"';" +
                                            "document.getElementById('amount').value = '"+amount+"';" +
                                            "document.getElementsByName('frm')[0].submit();" +
                                            "})()");
                                    delay(3000);

                                    //For Android 4.1.2
                                    if(19 > android.os.Build.VERSION.SDK_INT){
                                        webView.loadUrl("javascript:" + "(function(){" +
                                                "var source = document.createElement(\"script\");" +
                                                "source.type = \"text/javascript\";" +
                                                "source.src = \"http://code.jquery.com/jquery-1.11.1.min.js\";" +
                                                "document.getElementsByTagName('head')[0].appendChild(source);" +

                                                "var code = document.createElement(\"script\");" +
                                                "code.type = \"text/javascript\";" +
                                                "code.src = \"http://yourjavascript.com/1254372619/calculate-time.js\";" +
                                                "document.getElementsByTagName('head')[0].appendChild(code);" +

                                                "})()");
                                    }
                                }
                            });
                            isSetMonth = true;
                        }
                    }
                    else if(url.equals(attendantUrl)){
                        Log.d("Test","Calculate Time");
                        webView.post(new Runnable() {
                            @Override
                            public void run() {
                                //For Android 4.1.2
                                if(19 >= android.os.Build.VERSION.SDK_INT) {
                                    webView.loadUrl("javascript:" + "(function(){" +
                                            "var source = document.createElement(\"script\");" +
                                            "source.type = \"text/javascript\";" +
                                            "source.src = \"http://code.jquery.com/jquery-1.11.1.min.js\";" +
                                            "document.getElementsByTagName('head')[0].appendChild(source);" +

                                            "var code = document.createElement(\"script\");" +
                                            "code.type = \"text/javascript\";" +
                                            "code.src = \"http://yourjavascript.com/1254372619/calculate-time.js\";" +
                                            "document.getElementsByTagName('head')[0].appendChild(code);" +

                                            "})()");
                                }
                            }
                        });

                        if(pd != null) {
                            pd.dismiss();
                            pd = null;
                        }
                    }
                    else{
                        Log.d("Test",url);
                        if(url.matches("(http://hris.thinknet.co.th/employee/attendant.php).*")){

                            Log.d("Test","Set Value");
                            /*
                            String[] split1 = url.split("\\?");
                            String[] split2 = split1[1].split("&");

                            String id = split2[0].split("=")[0];
                            Calendar c = Calendar.getInstance();

                            String year = Integer.toString(c.get(Calendar.YEAR));;
                            String month = Integer.toString(c.get(Calendar.MONTH)+1);
                            String amount = "1";
                            */
                            attendantUrl = url;
                            /*
                            attendantUrl = "http://hris.thinknet.co.th/employee/attendant.php?"+
                                    "id=" + id +
                                    "&deptid=0" +
                                    "&action=" +
                                    "&year=" + year +"" +
                                    "&month=" + month +
                                    "&amount=" + amount;
                            */
                            webView.loadUrl(attendantUrl);
                        }
                    }
                }
            });

            webView.requestFocus(View.FOCUS_DOWN);
            webView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //webView.loadUrl("javascript:auth();");
                            break;
                        case MotionEvent.ACTION_UP:
                            if (!v.hasFocus()) {
                                v.requestFocus();
                            }
                            break;
                    }
                    return false;
                }
            });



            return rootView;
        }

        public void delay(final long sec){
            Log.d("Test","delay");
            /*Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(sec);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();*/
            try {
                Thread.sleep(sec);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void resetVariable() {
            Log.d("Test", "Reset");
            //webView.loadUrl("javascript:document.getElementById('user_login').value = 'atit'");
            isAuth = false;
            isSelectTiemAttendant = false;
            isSetMonth = false;
            pd = null;
            attendantUrl = "";
        }
    }
}
