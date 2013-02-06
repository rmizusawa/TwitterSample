package jp.co.mz;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.http.AccessToken;
import twitter4j.http.OAuthAuthorization;
import twitter4j.http.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Twmain extends Activity {
	/** Called when the activity is first created. */

	private final String CONSUMER_KEY = "aOqSP2Y9jp15KhJUEyeDg";
	private final String CONSUMER_SECRET = "vLUXewTfWAFv1EHdGLyMtXY29LYV2BERAyV12wxs4";
	private String CALLBACK_URL = "myapp://oauth";

	private int REQUEST_CODE;
	private RequestToken requestToken;
	private Twitter twitter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		TwitterFactory factory = new TwitterFactory();
		this.twitter = factory.getOAuthAuthorizedInstance(CONSUMER_KEY,
				CONSUMER_SECRET);

		// requestTokenもクラス変数。
		try {
			requestToken = twitter.getOAuthRequestToken(CALLBACK_URL);
		} catch (TwitterException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		// 認証用URLをインテントにセット。
		// TwitterLoginはActivityのクラス名。
		Intent intent = new Intent(this, TwitterLogin.class);
		intent.putExtra("auth_url", requestToken.getAuthorizationURL());

		// アクティビティを起動。
		// REQUEST_CODEは任意のint型の値。
		this.startActivityForResult(intent, REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			android.content.Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		// インテントからoauth_verifierを取り出して、
		// access_tokenとaccess_token_secretを取得する。
		// この取得処理はTwitter4jが勝手にやってくれます。

		try {
			AccessToken accessToken = twitter.getOAuthAccessToken(requestToken,
					intent.getExtras().getString("oauth_verifier"));

			Configuration config = new ConfigurationBuilder().build();

			// CONSUMER KEYとCONSUMER SECRET、
			// access_tokenとaccess_token_secretを使って、
			// twitterインスタンスを取得する。
			Twitter twitter = new TwitterFactory(config)
					.getInstance(new OAuthAuthorization(config, CONSUMER_KEY,
							CONSUMER_SECRET, accessToken));

			// 任意の文字列を引数にして、ツイート。
			twitter.updateStatus("tweet test from Android");
		} catch (TwitterException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		// 連携状態とトークンの書き込み
		// SharedPreferences pref =
		// getSharedPreferences(
		// PREFERENCE_NAME,
		// MODE_PRIVATE);
		//
		// SharedPreferences.Editor editor=pref.edit();
		// editor.putString("oauth_token",accessToken.getToken());
		// editor.putString("oauth_token_secret",accessToken.getTokenSecret());
		// editor.putString("status","available");
		//
		// editor.commit();

		// 設定おしまい。
		finish();
	}
}