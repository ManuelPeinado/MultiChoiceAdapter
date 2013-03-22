package com.manuelpeinado.multichoicetest;

import java.util.ArrayList;
import java.util.Arrays;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;

public class MainActivity extends SherlockActivity implements OnItemClickListener {
	private SelectionAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		String[] itemArray = getResources().getStringArray(R.array.items);
		ArrayList<String> items = new ArrayList<String>(Arrays.asList(itemArray));
		adapter = new MySelectionAdapter(items);
		adapter.setOnItemClickListener(this);
		adapter.setAdapterView(getListView());
	}

	private ListView getListView() {
		return (ListView) findViewById(android.R.id.list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void onItemClick(android.widget.AdapterView<?> adapterView, View view, int position, long id) {
		Toast.makeText(this, "Item click: " + adapter.getItem(position), Toast.LENGTH_SHORT).show();
	}
}
