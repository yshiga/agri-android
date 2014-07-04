package jp.co.smart_agri.news.lib;

import android.graphics.Color;

public class AppUtils {

	public static String getTabNameByTabPosi(int position) {
		return getTabNameByCid(tabPosi2Cid(position));
	}

	public static String getApiPathByCid(int cid) {
		switch (cid) {
		case 0:
			return "top/";
		case 1:
			return "agriculture/";
		case 2:
			return "kitchengarden/";
		case 3:
			return "event/";
		case 4:
			return "house/";
		}
		return "";
	}

	public static int getColorByTabPosi(int position) {
		return getColorByCid(tabPosi2Cid(position));
	}

	public static int getColorByCid(int cid) {
		switch (cid) {
		case 0:
			return Color.parseColor("#409ECA");
		case 1:
			return Color.parseColor("#F0B02F");
		case 2:
			return Color.parseColor("#7B5A3B");
		case 3:
			return Color.parseColor("#794FAC"); // 紫
		case 4:
			return Color.parseColor("#ff1493"); 
		}
		return Color.parseColor("#ff1493"); 
	}

	public static int tabPosi2Cid(int position) {
		// バージョンアップして順番を変えたり、ユーザーがタブの順番を設定で変えたりしたときに実装が入る、
		return position;
	}

	public static int getTabCount() {
		return 5;
	}

	private static String getTabNameByCid(int cid) {
		switch (cid) {
		case 0:
			return "トップ";
		case 1:
			return "農業";
		case 2:
			return "家庭菜園";
		case 3:
			return "イベント";
		case 4:
			return "物件";

		}
		return "エラー";
	}

}