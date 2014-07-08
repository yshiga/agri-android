package jp.co.smart_agri.news.lib;

import android.os.CountDownTimer;

/**
 * 2回連続で戻るボタンを押すと閉じる確認用処理クラス
 */
public class BackBtnFinishConfirmer {
	// BackボタンPress時の有効タイマー
	private CountDownTimer mKeyEventTimer;

	// 一度目のBackボタンが押されたかどうかを判定するフラグ
	private boolean mPressed = false;
	
	// この間隔内で2回戻るを押すと閉じる
	private final static int VALID_INTER_VALMILLIS = 3000;

	public BackBtnFinishConfirmer() {
		init();
	}

	/**
	 * バックボタン押されたときに呼ぶ。falseなら確認処理、trueなら終了処理
	 */
	public boolean canFinishOnBackPressed() {
		if (!mPressed) {
			// Timerを開始
			mKeyEventTimer.cancel(); // いらない？
			mKeyEventTimer.start();

			mPressed = true;
			return false;
		} else {
			return true;
		}
	}

	private void init() {
		mKeyEventTimer = new CountDownTimer(VALID_INTER_VALMILLIS, 100) {

			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {
				mPressed = false;
			}
		};
	}
}