package com.example.moleurlshortener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.moleurlshortener.DAO.DataBasesAccess;
import com.example.moleurlshortener.DAO.UrlObject;
import com.example.moleurlshortener.adapters.UrlListAdapter;
import com.example.moleurlshortener.connectionServices.HttpManager;
import com.example.moleurlshortener.tools.ConstantKeys;

import android.os.AsyncTask;
import android.os.Bundle;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public int buttonSelected = ConstantKeys.NO_SELEDTED;

	public ListView listView;
	public UrlListAdapter adapter;
	public ArrayList<UrlObject> ul;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final ImageView googleButton = (ImageView) this
				.findViewById(R.id.google_button);
		final ImageView moleButton = (ImageView) this
				.findViewById(R.id.mole_button);
		final ImageButton sendButton = (ImageButton) this
				.findViewById(R.id.send_button);

		googleButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO get resources this way, create a lot of memory leak
				// create a function to get it correctly
				moleButton.setImageResource(R.drawable.ic_mole_grey);
				googleButton.setImageResource(R.drawable.ic_google);
				buttonSelected = ConstantKeys.GOOGLE_URl;
			}
		});

		moleButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO get resources this way, create a lot of memory leak
				// create a function to get it correctly
				moleButton.setImageResource(R.drawable.ic_mole);
				googleButton.setImageResource(R.drawable.ic_google_grey);
				buttonSelected = ConstantKeys.MOLE_URL;
			}
		});

		sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (buttonSelected == ConstantKeys.NO_SELEDTED) {
					Toast.makeText(getApplicationContext(),
							getString(R.string.no_compersor),
							Toast.LENGTH_SHORT).show();
				} else {
					String url = ((EditText) MainActivity.this
							.findViewById(R.id.editText1)).getText().toString();
					sendData(url, buttonSelected);
				}
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		createList();
	}

	private void createList() {
		ul = DataBasesAccess.getInstance(getApplicationContext()).getUrls();
		refreshAdapterData();
	}

	private void refreshAdapterData() {
		// TODO to improve the list, we have to add the new urls not to
		// re-create the adapter, but for this demo it's enought.
		listView = (ListView) findViewById(R.id.list_view);
		adapter = new UrlListAdapter(this, ul);
		listView.setAdapter(adapter);
	}

	private void sendData(final String originalUrl, final int selected) {
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setTitle(R.string.sending_title);
		pd.setMessage(getString(R.string.sending_message));
		pd.setCancelable(false);
		pd.show();
		new AsyncTask<Void, Void, UrlObject>() {

			String url;
			int type;

			@Override
			protected void onPreExecute() {
				url = originalUrl;
				type = selected;
			}

			@Override
			protected UrlObject doInBackground(Void... params) {

				try {
					// In this demo, we need to emulate an access to internet,
					// if we have more time, we will use an httpclient with all
					// the needed data to send the url to one of the services
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// Sending message to the server to create the shorter url
				UrlObject urlObjectReturned = HttpManager.SendUrl(
						MainActivity.this.getApplicationContext(), url, type);

				// check if the url was not created
				if (urlObjectReturned != null) {
					DataBasesAccess.getInstance(
							MainActivity.this.getApplicationContext()).setUrl(
							urlObjectReturned);
					return urlObjectReturned;
				} else {
					return null;
				}
			}

			@Override
			protected void onPostExecute(UrlObject result) {
				if (pd != null) {
					pd.dismiss();
				}

				if (result != null) {
					ul.add(result);
					refreshAdapterData();
				} else {
					Toast.makeText(getApplicationContext(),
							getString(R.string.sending_error),
							Toast.LENGTH_SHORT).show();
				}
			}

		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
