package com.example.coolweather.app.model;

import java.util.ArrayList;
import java.util.List;

import com.example.coolweather.app.db.CoolWeatherOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB {
	
	private static final String DB_NAME = "cool weather";// 数据库名字
	
	public static final int Version = 1; //数据库版本
	
	private static CoolWeatherDB coolWeatherDB;
	
	private SQLiteDatabase db;
	
	private CoolWeatherDB(Context context) {
		CoolWeatherOpenHelper openHelper = new CoolWeatherOpenHelper
				(context, DB_NAME, null, Version);
		db = openHelper.getWritableDatabase();
	}
	
	public synchronized static CoolWeatherDB getInstance (Context context) {
		if (coolWeatherDB == null) {
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	
	/* 将 province实例存储到数据库 */
	public void saveProvince (Province province) {
		if (province != null) {
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}
	
	/*从数据库读取全国各省分天气*/
	public List<Province> loadProvinces () {
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		List<Province> list = new ArrayList<Province>();
		if (cursor.moveToFirst()) {
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
	/* 将 city实例存储到数据库 */
	public void saveCity (City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvincedId());
			db.insert("City", null, values);
		}
	}
	
	/*从数据库读取某省全部城市*/
	public List<City> loadCities (int provincedId) {
		Cursor cursor = db.query("City", null, "province_id = ?", 
				new String[] {String.valueOf(provincedId)}, null, null, null);
		List<City> list = new ArrayList<City>();
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
			  //city.setProvincedId(cursor.getInt(cursor.getColumnIndex("province_id")));
				city.setProvincedId(provincedId);
				list.add(city);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
	/* 将 city实例存储到数据库 */
	public void saveCounty (County county) {
		if (county != null) {
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("county_code", county.getCountyCode());
			values.put("city_id", county.getCityId());
			db.insert("County", null, values);
		}
	}
	
	/*从数据库读取某市全部县*/
	public List<County> loadCounties (int cityId) {
		Cursor cursor = db.query("County", null, "city_id = ?", 
				new String[] {String.valueOf(cityId)}, null, null, null);
		List<County> list = new ArrayList<County>();
		if (cursor.moveToFirst()) {
			do {
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
			  //city.setCountyId(cursor.getInt(cursor.getColumnIndex("county_id")));
				county.setCityId(cityId);
				list.add(county);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
}
