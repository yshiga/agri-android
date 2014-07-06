package jp.co.smart_agri.news.object;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

public class News {

	private String mId;
	private String mTitle;
	private String mUrl;
	private String mImageUrl;
	private String mVia;
	
	public String getId(){
		return mId;
	}
	
	public String getTitle(){
		return mTitle;
	}
	
	public String getUrl(){
		return mUrl;
	}
	
	public String getImageUrl(){
		return mImageUrl;
	}
	
	public String getVia() {
		return mVia;
	}

	public boolean hasVia(){
		if(TextUtils.isEmpty(mVia)){
			return false;
		}
		return true;
	}
	
	
	public boolean hasImage(){
		if(TextUtils.isEmpty(mImageUrl)){
			return false;
		}
		return true;
	}
	

	/**
	 * JSONオブジェクトから変換する
	 */
	public void convartFromJsonObject(JSONObject obj) {
		try {
			mId = obj.getString("uid");
			mUrl= obj.getString("url");
			mTitle = obj.getString("title");
			Log.i("tag", "image class:" + obj.get("image_url").getClass());
			mImageUrl = obj.getString("image_url");
			mVia = obj.getString("via");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("tag", "image url" + mImageUrl);
	}
}