package mgks.os.swv;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.WebView;

import mgks.os.swv.plugins.QRScannerPlugin;

/**
 * QR 전용 테스트 플러그인 플레이그라운드
 */
public class Playground {
    private static final String TAG = "QRPlayground";
    private final Activity activity;
    private final WebView webView;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public Playground(Activity activity, WebView webView, Functions functions) {
        this.activity = activity;
        this.webView = webView;
        // 플러그인 초기화 후 콜백
        SmartWebView.onPluginsInitialized(this::onPluginsReady);
    }

    private void onPluginsReady() {
        // 진단 로그
        if (SmartWebView.getPluginManager().getPluginInstance("QRScannerPlugin") instanceof QRScannerPlugin) {
            Log.d(TAG, "QRScannerPlugin is ready");
        } else {
            Log.w(TAG, "QRScannerPlugin not found");
        }
        // UI 주입 (3초 딜레이)
        mainHandler.postDelayed(this::setupPluginDemoUI, 3000);
    }

    private void setupPluginDemoUI() {
        String demoJs =
                // 버튼 하나만 추가
                "(function(){"
                        +  "var btn=document.getElementById('qr-test-btn');"
                        +  "if(btn)return;"
                        +  "btn=document.createElement('button');"
                        +  "btn.id='qr-test-btn';"
                        +  "btn.innerText='Scan QR Code';"
                        +  "btn.style.position='fixed';"
                        +  "btn.style.bottom='20px';"
                        +  "btn.style.right='20px';"
                        +  "btn.style.padding='10px 15px';"
                        +  "btn.style.backgroundColor='#4285f4';"
                        +  "btn.style.color='white';"
                        +  "btn.style.border='none';"
                        +  "btn.style.borderRadius='4px';"
                        +  "btn.onclick=function(){window.QRScanner && window.QRScanner.scan();};"
                        +  "document.body.appendChild(btn);"
                        +"})();";
        webView.evaluateJavascript(demoJs, null);
    }
}
