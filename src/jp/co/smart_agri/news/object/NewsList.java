package jp.co.smart_agri.news.object;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * ニュースのリスト
 */
public class NewsList {

	private ArrayList<News> mList = new ArrayList<News>();

	/**
	 * APIレスポンスから生成する
	 */
	public void convertFromJsonArray(JSONArray jsonArray) {
		mList.clear();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				News news = new News(); 
				news.convartFromJsonObject(jsonObject);
				mList.add(news);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public int getCount() {
		return mList.size();
	}

	public News get(int index) {
		return mList.get(index);
	}
	
	public void add(News news) {
		mList.add(news);
	}
}