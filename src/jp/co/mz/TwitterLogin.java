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

		// ��ʑJ�ڎ���WebView���ŉ�ʑJ�ڂ���悤�ɂ���B
		// �������Ȃ��ƁA�W���u���E�U���J���Ă��܂��B
		webView.setWebViewClient(new WebViewClient() {
			// �y�[�W�`�抮�����ɌĂ΂��B
			public void onPageFinished(WebView view, String url) {
				Log.d("debug:mz:url=", url);
				super.onPageFinished(view, url);

				// CALLBACK_URL�͒��ӓ_1�ŏ������K���ȕ�����B
				// Twitter���ł���Ă���鏈���Ƃ��āA
				// �F�؊���������A�w�肵��CALLBACK URL�Ƀ��_�C���N�g����̂ŁA
				// �J�ڐ��URL��CALLBACK URL����n�܂��Ă�����A�F�ؐ����Ƃ݂Ȃ��B
				if (url != null && url.startsWith(CALLBACK_URL)) {
					// URL�p�����[�^�𕪉�����B
					String[] urlParameters = url.split("\\?")[1].split("&");

					String oauthToken = "";
					String oauthVerifier = "";

					// oauth_token��URL�p�����[�^����؂�o���B
					if (urlParameters[0].startsWith("oauth_token")) {
						oauthToken = urlParameters[0].split("=")[1];
					} else if (urlParameters[1].startsWith("oauth_token")) {
						oauthToken = urlParameters[1].split("=")[1];
					}

					// oauth_verifier��URL�p�����[�^����؂�o���B
					if (urlParameters[0].startsWith("oauth_verifier")) {
						oauthVerifier = urlParameters[0].split("=")[1];
					} else if (urlParameters[1].startsWith("oauth_verifier")) {
						oauthVerifier = urlParameters[1].split("=")[1];
					}

					// oauth_token��oauth_verifier���C���e���g�ɃZ�b�g�B
					Intent intent = getIntent();
					intent.putExtra("oauth_token", oauthToken);
					intent.putExtra("oauth_verifier", oauthVerifier);

					// ����Activity�ɖ߂��B
					setResult(Activity.RESULT_OK, intent);
					finish();
				}
			}
		});

		// Activity1�Őݒ肵���F�؃y�[�W��\���B
		webView.loadUrl(this.getIntent().getExtras().getString("auth_url"));
	}
}
