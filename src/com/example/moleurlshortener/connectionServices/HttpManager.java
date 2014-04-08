package com.example.moleurlshortener.connectionServices;

import com.example.moleurlshortener.DAO.UrlObject;
import com.example.moleurlshortener.tools.ConstantKeys;
import com.example.moleurlshortener.tools.Utils;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import com.example.moleurlshortener.R;

import android.content.Context;

public class HttpManager {

	public static final int NUMBER_OF_CORES = Runtime.getRuntime()
			.availableProcessors();

	public static final int HTTP_CLIENT_CONNECTIONS = 1;
	public static final int HTTP_CLIENT_MULTIPART_CONNECTIONS = NUMBER_OF_CORES;

	private static HttpClient mHttpClient = null;
	private static HttpClient mHttpPostClient = null;
	private static HttpClient[] httpClientsMultipart = null;
	private static int roundRobinIndex = 0;

	public static synchronized void shutdownHttpClients(Context context) {
		if (mHttpClient != null) {
			mHttpClient.getConnectionManager().shutdown();
			mHttpClient = null;
		}
		if (mHttpPostClient != null) {
			mHttpPostClient.getConnectionManager().shutdown();
			mHttpPostClient = null;
		}

		if (httpClientsMultipart != null) {
			for (HttpClient hc : httpClientsMultipart) {
				hc.getConnectionManager().shutdown();
			}
			httpClientsMultipart = null;
		}
	}

	private static HttpClient createHttpClient(Context context, int nConnections) {
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme(context
				.getString(R.string.standard_protocol), PlainSocketFactory
				.getSocketFactory(), context.getResources().getInteger(
				R.integer.standard_port)));

		HttpParams params = new BasicHttpParams();
		/* Android only allows 2 max connections */
		ConnManagerParams.setMaxTotalConnections(params, nConnections);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

		ClientConnectionManager ccm = new ThreadSafeClientConnManager(params,
				registry);

		return new DefaultHttpClient(ccm, params);
	}

	private static synchronized HttpClient getHttpClient(Context context) {
		if (mHttpClient != null) {
			return mHttpClient;
		}
		mHttpClient = createHttpClient(context, 1);

		return mHttpClient;
	}

	private static synchronized HttpClient getHttpPostClient(Context context) {
		if (mHttpPostClient != null) {
			return mHttpPostClient;
		}
		mHttpPostClient = createHttpClient(context, 1);

		return mHttpPostClient;
	}

	private static synchronized HttpClient getHttpClientMultipart(
			Context context) {
		if (httpClientsMultipart == null) {
			httpClientsMultipart = new HttpClient[HTTP_CLIENT_MULTIPART_CONNECTIONS];
		}

		if (httpClientsMultipart[roundRobinIndex] == null) {
			httpClientsMultipart[roundRobinIndex] = createHttpClient(context, 1);
		}

		HttpClient hc = httpClientsMultipart[roundRobinIndex];
		roundRobinIndex = (roundRobinIndex + 1)
				% HTTP_CLIENT_MULTIPART_CONNECTIONS;

		return hc;
	}

	public static UrlObject SendUrl(Context ctx, String originalUrl,
			int typeSelected) {

		// This is a fake, the final feature will be send the original url to
		// the servers, moleserver or goo.gl to create the url. But for now we
		// made it with a random number.

		// This will call httpclient, and after we will get the data for the
		// response, parse it with gson and create the object to return

		String createdUrl = "";
		if (typeSelected == ConstantKeys.GOOGLE_URl) {
			createdUrl = ctx.getString(R.string.google_server);
		} else {
			createdUrl = ctx.getString(R.string.mole_server);
		}

		createdUrl = createdUrl + Utils.generateRandomNumber(5);

		UrlObject urlObject = new UrlObject();
		urlObject.setOriginalUrl(originalUrl);
		urlObject.setCreatedUrl(createdUrl);

		return urlObject;
	}

}
