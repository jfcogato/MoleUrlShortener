package com.example.moleurlshortener.DAO;

import java.util.ArrayList;

import android.content.Context;

public class DataBasesAccess {

	private static DataBasesAccess classAccess = null;
	private static UrlDataBaseAdapter dbUrls = null;

	private static String createInstance = "";
	private static String accessUrls = "";

	public static DataBasesAccess getInstance(Context ctx) {

		if (classAccess == null) {
			synchronized (createInstance) {
				if (classAccess == null) {
					classAccess = new DataBasesAccess();
					dbUrls = new UrlDataBaseAdapter(ctx.getApplicationContext());
					dbUrls.open();
				}
			}
		}
		return classAccess;
	}

	public ArrayList<UrlObject> getUrls() {
		synchronized (accessUrls) {
			return dbUrls.getAllEntriesParsed();
		}
	}

	public void deleteUrlDataBase() {
		synchronized (accessUrls) {
			dbUrls.removeAll();
		}
	}

	public void setUrl(UrlObject urlobject) {
		synchronized (accessUrls) {
			dbUrls.insertEntry(urlobject);
		}
	}
}
