package com.changxiang.game.sdk.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.Display;
import android.view.WindowManager;

public class Util {

	/**
	 * 参数编码
	 * 
	 * @param value
	 * @return
	 */
	public static String encode(String s) {
		if (s == null) {
			return "";
		}
		try {
			return URLEncoder.encode(s, "UTF-8").replace("+", "%20")
					.replace("*", "%2A").replace("%7E", "~")
					.replace("#", "%23");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static String aliKeyDecode(String s) {
		String key = "";
		int len = 0;
		StringBuffer sb = new StringBuffer();
		StringBuffer result = new StringBuffer();
		if (StringUtil.isEmpty(key)) {
			key="abcdefghijklmnABCDEFGHIJKLMNOPQRST123456!@#$%^&*()POIKJMNB";
		}
		len = Math.max(s.length(), key.length());
		for (int i = 0; i < 30; i++) {
			sb.append(key);
			sb.append(new StringBuffer(key).reverse());
			sb.append(i);
		}
		key = sb.toString();
		
		for (int i = 0; i < len; i++) {
			int charAtKey, charAtStr;
			if (i >= key.length()) {
				charAtKey = 0;
			} else {
				charAtKey = key.charAt(i);
			}
			if (i >= s.length()) {
				continue;
			} else {
				charAtStr = s.charAt(i);
			}
			char at = (char) (charAtKey ^ charAtStr);
			result.append(at);
		}
		return result.toString();
	}

	/**
	 * 参数反编码
	 * 
	 * @param s
	 * @return
	 */
	public static String decode(String s) {
		if (s == null) {
			return "";
		}
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 获得资源文件名称
	 * 
	 * @param cx
	 * @param defType
	 * @param file_name
	 * @return
	 */
	public static int getResIdFromFileName(Context cx, String defType,
			String file_name) {
		// TODO Auto-generated method stub
		Resources rs = cx.getResources();
		String packageName = getMyPackageName(cx);
		return rs.getIdentifier(file_name, defType, packageName);
	}

	/**
	 * 获得当前程序包名
	 * 
	 * @param cx
	 * @return
	 */
	public static String getMyPackageName(Context cx) {
		try {
			return cx.getPackageManager()
					.getPackageInfo(cx.getPackageName(), 0).packageName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 * 
	 * @param pxValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int px2dip(float pxValue, float scale) {
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 * 
	 * @param dipValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int dip2px(float dipValue, float scale) {
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(float pxValue, float fontScale) {
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(float spValue, float fontScale) {
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 获得设备屏幕矩形区域范围
	 * 
	 * @param context
	 * @return
	 */
	public static Rect getScreenRect(Context context) {
		Display display = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int w = display.getWidth();
		int h = display.getHeight();
		return new Rect(0, 0, w, h);
	}
	
	
	 //从给定位置读取Json文件
    public static String readJson(String path){
        //从给定位置获取文件
        File file = new File(path);
        BufferedReader reader = null;
        //返回值,使用StringBuffer
        StringBuffer data = new StringBuffer();
        //
        try {
            reader = new BufferedReader(new FileReader(file));
            //每次读取文件的缓存
            String temp = null;
            while((temp = reader.readLine()) != null){
                data.append(temp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭文件流
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data.toString();
    }

    //给定路径与Json文件，存储到硬盘
    public static void writeJson(String path,Object json,String fileName){
        BufferedWriter writer = null;
        File file = new File(path + fileName + ".json");
        //如果文件不存在，则新建一个,有就删除再重建
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        //写入
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        System.out.println("文件写入成功！");
    }


}
