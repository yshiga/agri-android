package jp.co.smart_agri.news.lib;

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
			return "self-sustaining/";
		}
		return null;
	}

	public static int tabPosi2Cid(int position) {
		// バージョンアップして順番を変えたり、ユーザーがタブの順番を設定で変えたりしたときに実装が入る、
		return position;
	}

	public static int getTabCount() {
		return 3;
	}

	private static String getTabNameByCid(int cid) {
		switch (cid) {
		case 0:
			return "トップ";
		case 1:
			return "農業";
		case 2:
			return "家庭菜園";
		}
		return null;
	}

}