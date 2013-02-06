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

		// requestToken���N���X�ϐ��B
		try {
			requestToken = twitter.getOAuthRequestToken(CALLBACK_URL);
		} catch (TwitterException e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
		}

		// �F�ؗpURL���C���e���g�ɃZ�b�g�B
		// TwitterLogin��Activity�̃N���X���B
		Intent intent = new Intent(this, TwitterLogin.class);
		intent.putExtra("auth_url", requestToken.getAuthorizationURL());

		// �A�N�e�B�r�e�B���N���B
		// REQUEST_CODE�͔C�ӂ�int�^�̒l�B
		this.startActivityForResult(intent, REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			android.content.Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		// �C���e���g����oauth_verifier�����o���āA
		// access_token��access_token_secret���擾����B
		// ���̎擾������Twitter4j������ɂ���Ă���܂��B

		try {
			AccessToken accessToken = twitter.getOAuthAccessToken(requestToken,
					intent.getExtras().getString("oauth_verifier"));

			Configuration config = new ConfigurationBuilder().build();

			// CONSUMER KEY��CONSUMER SECRET�A
			// access_token��access_token_secret���g���āA
			// twitter�C���X�^���X���擾����B
			Twitter twitter = new TwitterFactory(config)
					.getInstance(new OAuthAuthorization(config, CONSUMER_KEY,
							CONSUMER_SECRET, accessToken));

			// �C�ӂ̕�����������ɂ��āA�c�C�[�g�B
			twitter.updateStatus("tweet test from Android");
		} catch (TwitterException e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
		}
		// �A�g��Ԃƃg�[�N���̏�������
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

		// �ݒ肨���܂��B
		finish();
	}
}