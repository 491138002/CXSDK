package com.changxiang.game.sdk.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.changxiang.game.sdk.type.CXSDKStatusCode;
import com.changxiang.game.sdk.vo.CXAlikey;
import com.changxiang.game.sdk.vo.CXGame;
import com.changxiang.game.sdk.vo.CXNews;
import com.changxiang.game.sdk.vo.CXPayResult;
import com.changxiang.game.sdk.vo.CXSmsInfo;
import com.changxiang.game.sdk.vo.CXUser;
import com.changxiang.game.sdk.vo.Transcation;

public class ParseUtil {

	public static HashMap<String, Object> getLoginUserData(String jsonData) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			JSONObject obj = new JSONObject(jsonData);
			if (!obj.isNull("timestamp")) {
				resultMap.put("timestamp", obj.getString("timestamp"));
			}
			if (!obj.isNull("code")) {
				resultMap.put("code", obj.getInt("code"));
			}
			if (!obj.isNull("msg")) {
				resultMap.put("msg", obj.getString("msg"));
			}
			
			if(!obj.isNull("code")){
				int result = obj.getInt("code");
				if(result == CXSDKStatusCode.SUCCESS){
					if(!obj.isNull("data")){
						CXUser user = new CXUser();
						JSONObject userObject = obj.getJSONObject("data");
						if (userObject != null) {
							if (!userObject.isNull("user_id")) {
								user.setUser_id(userObject.getString("user_id"));
							}
							if (!userObject.isNull("nick_name")) {
								user.setNick_name(userObject.getString("nick_name"));
							}
							if (!userObject.isNull("adult")) {
								user.setAdult(userObject.getInt("adult"));
							}
							if (!userObject.isNull("ticket")) {
								user.setTicket(userObject.getString("ticket"));
							}
							if (!userObject.isNull("quick_game")) {
								user.setQuick_game(userObject.getInt("quick_game"));
							}
							if(!userObject.isNull("username")){
								user.setUsername(userObject.getString("username"));
							}
							if(!userObject.isNull("password")){
								user.setPassword(userObject.getString("password"));
							}
							if(!userObject.isNull("origin")){
								user.setOrigin(userObject.getString("origin"));
							}
							
						}
						resultMap.put("data", user);
					}
				}
			}
			return resultMap;
		} catch (JSONException e) {
			e.printStackTrace();
			return resultMap;
		}
	}
	
	
	public static HashMap<String, Object> getConfig(String jsonData) {
		try {
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			JSONObject obj = new JSONObject(jsonData);
			if (!obj.isNull("VERSION")) {
				resultMap.put("VERSION", obj.getInt("VERSION"));
			}
			if (!obj.isNull("SERVER_KEY")) {
				resultMap.put("SERVER_KEY", obj.getString("SERVER_KEY"));
			}
			if (!obj.isNull("PAYMENT_CONFIG")) {
				resultMap.put("PAYMENT_CONFIG", obj.getString("PAYMENT_CONFIG"));
			}
			if (!obj.isNull("IS_UPDATE_PAYMENT_CONFIG")) {
				resultMap.put("IS_UPDATE_PAYMENT_CONFIG", obj.getInt("IS_UPDATE_PAYMENT_CONFIG"));
			}
			return resultMap;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}
	public static HashMap<String, Object> getPayCOnfig(String jsonData) {
		try {
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			JSONObject obj = new JSONObject(jsonData);
			if (!obj.isNull("alipay_CreditCard")) {
				resultMap.put("alipay_CreditCard", obj.getInt("alipay_CreditCard"));
			}
			if (!obj.isNull("alipay_DebitCard")) {
				resultMap.put("alipay_DebitCard", obj.getInt("alipay_DebitCard"));
			}
			if (!obj.isNull("alipay_all")) {
				resultMap.put("alipay_all", obj.getInt("alipay_all"));
			}
			
			if (!obj.isNull("tele_card")) {
				resultMap.put("tele_card", obj.getInt("tele_card"));
			}
			if (!obj.isNull("game_card")) {
				resultMap.put("game_card", obj.getInt("game_card"));
			}
			if (!obj.isNull("Union_pay")) {
				resultMap.put("Union_pay", obj.getInt("Union_pay"));
			}
			if (!obj.isNull("SMS")) {
				resultMap.put("SMS", obj.getInt("SMS"));
			}
			return resultMap;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}
	public static HashMap<String, Object> getGameVersionData(String jsonData) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			JSONObject obj = new JSONObject(jsonData);
			if (!obj.isNull("timestamp")) {
				resultMap.put("timestamp", obj.getString("timestamp"));
			}
			if (!obj.isNull("code")) {
				resultMap.put("code", obj.getInt("code"));
			}
			if (!obj.isNull("msg")) {
				resultMap.put("msg", obj.getString("msg"));
			}
			
			if(!obj.isNull("code")){
				int result = obj.getInt("code");
				if(result == CXSDKStatusCode.SUCCESS){
					if (!obj.isNull("downloadurl")) {
						resultMap.put("downloadurl", obj.getString("downloadurl"));
					}
					
					if (!obj.isNull("version")) {
						resultMap.put("version", obj.getString("version"));
					}
				}
			}
			return resultMap;
		} catch (JSONException e) {
			e.printStackTrace();
			return resultMap;
		}
	}

	public static HashMap<String, Object> getGameListData(String jsonData) {
		try {
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			JSONObject obj = new JSONObject(jsonData);
			if (!obj.isNull("timestamp")) {
				resultMap.put("timestamp", obj.getString("timestamp"));
			}
			if (!obj.isNull("code")) {
				resultMap.put("code", obj.getInt("code"));
			}
			if (!obj.isNull("msg")) {
				resultMap.put("msg", obj.getString("msg"));
			}
			if (!obj.isNull("bbs_url")) {
				resultMap.put("bbs_url", obj.getString("bbs_url"));
			}
			if (!obj.isNull("main_url")) {
				resultMap.put("main_url", obj.getString("main_url"));
			}
			if (!obj.isNull("data")) {
				ArrayList<CXGame> gameList = new ArrayList<CXGame>();
				JSONArray resultObj = obj.getJSONArray("data");
				
				for (int i = 0; i < resultObj.length(); i++) {
					JSONObject gameObj = resultObj.getJSONObject(i);
					CXGame game = getGameByJsonObject(gameObj);
					if (game != null)
						gameList.add(game);
				}
				
				resultMap.put("data", gameList);
			}

			return resultMap;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}

	private static CXGame getGameByJsonObject(JSONObject object) {
		if (object == null)
			return null;
		try {
			CXGame game = new CXGame();
			if (!object.isNull("game_name")) {
				game.setName(new String(object.getString("game_name")
						.getBytes("utf-8")));
			}
			if (!object.isNull("apk_download_url")) {
				game.setApkDownloadUrl(object.getString("apk_download_url"));
			}
			if (!object.isNull("image_download_url")) {
				game.setImageDownloadUrl(object.getString("image_download_url"));
			}
			return game;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static HashMap<String, Object> getNewsListData(String jsonData) {
		try {
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			JSONObject obj = new JSONObject(jsonData);
			if (!obj.isNull("timestamp")) {
				resultMap.put("timestamp", obj.getString("timestamp"));
			}
			if (!obj.isNull("code")) {
				resultMap.put("code", obj.getInt("code"));
			}
			if (!obj.isNull("msg")) {
				resultMap.put("msg", obj.getString("msg"));
			}
			if (!obj.isNull("data")) {
				JSONArray array = obj.getJSONArray("data");
				if (array != null) {
					ArrayList<CXNews> newsList = new ArrayList<CXNews>();
					int lenght = array.length();
					for (int i = 0; i < lenght; i++) {
						JSONObject newsObj = array.getJSONObject(i);
						CXNews news = getNewsByJsonObject(newsObj);
						if (news != null)
							newsList.add(news);
					}
					resultMap.put("data", newsList);
				}

			}

			return resultMap;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}
	
	public static HashMap<String, Object> getNewsData(String jsonData) {
		try {
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			JSONObject obj = new JSONObject(jsonData);
			if (!obj.isNull("timestamp")) {
				resultMap.put("timestamp", obj.getString("timestamp"));
			}
			if (!obj.isNull("code")) {
				resultMap.put("code", obj.getInt("code"));
			}
			if (!obj.isNull("msg")) {
				resultMap.put("msg", obj.getString("msg"));
			}
			
			CXNews news = getNewsByJsonObject(obj);
			resultMap.put("data", news);
			return resultMap;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static CXNews getNewsByJsonObject(JSONObject object) {
		if (object == null)
			return null;
		try {
			CXNews news = new CXNews();
			if (!object.isNull("news_id")) {
				news.setId(object.getInt("news_id"));
			}
			if (!object.isNull("news_title")) {
				news.setTitle(new String(object.getString("news_title")
						.getBytes("utf-8")));
			}
			if (!object.isNull("news_time")) {
				news.setTime(object.getString("news_time"));
			}
			if (!object.isNull("news_content")) {
				news.setContant(new String(object.getString("news_content")
						.getBytes("utf-8")));
			}
			if (!object.isNull("news_version")) {
				news.setNews_version(new String(object.getString("news_version")
						.getBytes("utf-8")));
			}
			if (!object.isNull("is_available")) {
				news.setIs_available(new String(object.getString("is_available")
						.getBytes("utf-8")));
			}
			if (!object.isNull("start_time")) {
				news.setStart_time(new String(object.getString("start_time")
						.getBytes("utf-8")));
			}
			if (!object.isNull("end_time")) {
				news.setEnd_time(new String(object.getString("end_time")
						.getBytes("utf-8")));
			}
			if (!object.isNull("msg")) {
				news.setMsg(new String(object.getString("msg")
						.getBytes("utf-8")));
			}
			
			if (!object.isNull("license_code")) {
				news.setLicense_code(new String(object.getString("license_code")
						.getBytes("utf-8")));
			}
			
			if (!object.isNull("username")) {
				news.setUsername(new String(object.getString("username")
						.getBytes("utf-8")));
			}
			
			if (!object.isNull("batch_id")) {
				news.setBatch_id(new String(object.getString("batch_id")
						.getBytes("utf-8")));
			}
			return news;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 解析支付返回结果
	 * @param object
	 * @return
	 */
	public static CXPayResult getPayResult(String jsonData) {
		try {
			JSONObject object = new JSONObject(jsonData);
			
			CXPayResult result = new CXPayResult();
			if (!object.isNull("timestamp")) {
				result.setTimestamp(object.getLong("timestamp"));
			}
			if (!object.isNull("code")) {
				result.setCode(object.getInt("code"));
			}
			if (!object.isNull("msg")) {
				result.setMsg(object.getString("msg"));
			}
			
			
			try {
				if(!object.isNull("data")){
					JSONObject data = object.getJSONObject("data");
					if (!data.isNull("order_id")) {
						result.setOrderId(data.getString("order_id"));
					}
					if (!data.isNull("tn")) {
						result.setTn(data.getString("tn"));
					}
					if (!data.isNull("notify_url")) {
						result.setNotifyUrl(data.getString("notify_url"));
					}
					if (!data.isNull("param")) {
						result.setParam(data.getString("param"));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * 解析短信支付返回结果
	 * @param object
	 * @return
	 */
	public static CXPayResult getSMSPayResult(String jsonData) {
		try {
			JSONObject object = new JSONObject(jsonData);
			
			CXPayResult result = new CXPayResult();
			if (!object.isNull("timestamp")) {
				result.setTimestamp(object.getLong("timestamp"));
			}
			if (!object.isNull("code")) {
				result.setCode(object.getInt("code"));
			}
			if (!object.isNull("msg")) {
				result.setMsg(object.getString("msg"));
			}
			
			
			try {
				if(!object.isNull("data")){
					JSONObject data = object.getJSONObject("data");
					if (!data.isNull("order_id")) {
						result.setOrderId(data.getString("order_id"));
					}
					if (!data.isNull("param")) {
						result.setParam(data.getString("param"));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 解析支付宝key
	 * @param object
	 * @return
	 */
	public static CXAlikey getAliKey(String jsonData) {
		try {
			JSONObject object = new JSONObject(jsonData);
			
			CXAlikey result = new CXAlikey();
			if (!object.isNull("appid")) {
				result.setAppid(object.getString("appid"));
			}
			if (!object.isNull("pub.cert")) {
				result.setPubcert(object.getString("pub.cert"));
			}
			if (!object.isNull("pri.cert")) {
				result.setPricert(object.getString("pri.cert"));
			}
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * SMS
	 * @param object
	 * @return
	 */
	public static CXSmsInfo getSmsInfo(String jsonData) {
		try {
			JSONObject object = new JSONObject(jsonData);
			
			CXSmsInfo result = new CXSmsInfo();
			if (!object.isNull("cpName")) {
				result.setCpName(object.getString("cpName"));
			}
			if (!object.isNull("gameName")) {
				result.setGameName(object.getString("gameName"));
			}
			if (!object.isNull("goodsname")) {
				result.setProductName(object.getString("goodsname"));
			}
			if (!object.isNull("serviceTel")) {
				result.setServiceTel(object.getString("serviceTel"));
			}
			if (!object.isNull("product_id")) {
				result.setProduct_id(object.getString("product_id"));
			}
			if (!object.isNull("key")) {
				result.setKey(object.getString("key"));
			}
			if (!object.isNull("money")) {
				result.setMoney(object.getString("money"));
			}
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 解析支付查询接口结果
	 * @param object
	 * @return
	 */
	public static CXPayResult getPayResultQuery(String jsonData) {
		try {
			JSONObject object = new JSONObject(jsonData);
			
			CXPayResult result = new CXPayResult();
			if (!object.isNull("timestamp")) {
				result.setTimestamp(object.getLong("timestamp"));
			}
			if (!object.isNull("code")) {
				result.setCode(object.getInt("code"));
			}
			if (!object.isNull("msg")) {
				result.setMsg(object.getString("msg"));
			}
			if (!object.isNull("orderId")) {
				result.setOrderId(object.getString("orderId"));
			}
			
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static HashMap<String, Object> getVersionData(String jsonData) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			JSONObject obj = new JSONObject(jsonData);
			if (!obj.isNull("code")) {
				resultMap.put("status", obj.getString("code"));
			}

			if (!obj.isNull("version")) {
				resultMap.put("version", obj.getInt("version"));
			}
			
			if (!obj.isNull("downloadurl")) {
				resultMap.put("downloadurl", obj.getString("downloadurl"));
			}
			
			if (!obj.isNull("forcedown")) {
				resultMap.put("forcedown", obj.getInt("forcedown"));
			}
			return resultMap;
		} catch (JSONException e) {
			e.printStackTrace();
			return resultMap;
		}
	}
	
	public static HashMap<String, Object> getTranscation(String jsonData) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		List<Transcation> dataList = new ArrayList<Transcation>();
		try {
			JSONObject obj = new JSONObject(jsonData);
			if (!obj.isNull("code")) {
				resultMap.put("code", obj.getString("code"));
			}
			if (!obj.isNull("msg")) {
				resultMap.put("msg", obj.getString("msg"));
			}
			if(!obj.isNull("data")){
				JSONArray data = obj.getJSONArray("data");
				for (int i = 0; i < data.length(); i++) {
					JSONObject jsonBean = data.getJSONObject(i);
					Transcation bean = new Transcation();
					if(!jsonBean.isNull("order_id")){
						bean.setOrder_id(jsonBean.getString("order_id"));
					}
					if(!jsonBean.isNull("order_state")){
						bean.setOrder_state(jsonBean.getString("order_state"));
					}
					if(!jsonBean.isNull("order_state_msg")){
						bean.setOrder_state_msg(jsonBean.getString("order_state_msg"));
					}
					if(!jsonBean.isNull("order_time")){
						bean.setOrder_time(jsonBean.getString("order_time"));
					}
					if(!jsonBean.isNull("order_type")){
						bean.setOrder_type(jsonBean.getString("order_type"));
					}
					if(!jsonBean.isNull("order_goods")){
						bean.setOrder_goods(jsonBean.getString("order_goods"));
					}
					if(!jsonBean.isNull("order_amount")){
						bean.setOrder_amount(jsonBean.getString("order_amount"));
					}
					if(!jsonBean.isNull("order_msg")){
						bean.setOrder_msg(jsonBean.getString("order_msg"));
					}
					dataList.add(bean);
				}
			}
			resultMap.put("dataList", dataList);
			return resultMap;
		} catch (JSONException e) {
			e.printStackTrace();
			return resultMap;
		}
	}
}
