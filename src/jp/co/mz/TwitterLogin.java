package jp.co.mz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TwitterLogin extends Activity {

	private String CALLBACK_URL = "myapp://oauth";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oauth);
		WebView webView = (WebView) findViewById(R.id.webView);

		// 画面遷移時にWebView内で画面遷移するようにする。
		// こうしないと、標準ブラウザが開いてしまう。
		webView.setWebViewClient(new WebViewClient() {
			// ページ描画完了時に呼ばれる。
			public void onPageFinished(WebView view, String url) {
				Log.d("debug:mz:url=", url);
				super.onPageFinished(view, url);

				// CALLBACK_URLは注意点1で書いた適当な文字列。
				// Twitter側でやってくれる処理として、
				// 認証完了したら、指定したCALLBACK URLにリダイレクトするので、
				// 遷移先のURLがCALLBACK URLから始まっていたら、認証成功とみなす。
				if (url != null && url.startsWith(CALLBACK_URL)) {
					// URLパラメータを分解する。
					String[] urlParameters = url.split("\\?")[1].split("&");

					String oauthToken = "";
					String oauthVerifier = "";

					// oauth_tokenをURLパラメータから切り出す。
					if (urlParameters[0].startsWith("oauth_token")) {
						oauthToken = urlParameters[0].split("=")[1];
					} else if (urlParameters[1].startsWith("oauth_token")) {
						oauthToken = urlParameters[1].split("=")[1];
					}

					// oauth_verifierをURLパラメータから切り出す。
					if (urlParameters[0].startsWith("oauth_verifier")) {
						oauthVerifier = urlParameters[0].split("=")[1];
					} else if (urlParameters[1].startsWith("oauth_verifier")) {
						oauthVerifier = urlParameters[1].split("=")[1];
					}

					// oauth_tokenとoauth_verifierをインテントにセット。
					Intent intent = getIntent();
					intent.putExtra("oauth_token", oauthToken);
					intent.putExtra("oauth_verifier", oauthVerifier);

					// 元のActivityに戻す。
					setResult(Activity.RESULT_OK, intent);
					finish();
				}
			}
		});

		// Activity1で設定した認証ページを表示。
		webView.loadUrl(this.getIntent().getExtras().getString("auth_url"));
	}
}
