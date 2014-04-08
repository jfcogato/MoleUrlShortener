package com.example.moleurlshortener;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends Activity {

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
				moleButton.setImageResource(R.drawable.ic_mole_grey);
				googleButton.setImageResource(R.drawable.ic_google);
			}
		});

		moleButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				moleButton.setImageResource(R.drawable.ic_mole);
				googleButton.setImageResource(R.drawable.ic_google_grey);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
