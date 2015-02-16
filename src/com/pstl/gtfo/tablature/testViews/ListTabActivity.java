package com.pstl.gtfo.tablature.testViews;

import java.io.File;
import java.io.FilenameFilter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.pstl.gtfo.R;

public class ListTabActivity extends Activity implements OnItemClickListener
{

	private ListView listView;
	private File path = new File(Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/SoundProject");
    private String[] tabNames;
    ArrayAdapter<String> adapter;
    
    public final static String EXTRA_MESSAGE = "com.pstl.gtfo.MESSAGE";

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_list);
        listView = (ListView)findViewById(R.id.listview);
        
		initList();
        
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, tabNames);
  
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}
	
	private void initList(){
	
		path.mkdirs();
		
		if(path.exists()){
			FilenameFilter fileNameFilter = new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String filename) {
						File sel = new File(dir, filename);
						return filename.contains(".txt") || sel.isDirectory();
				}
			};
			tabNames = path.list(fileNameFilter);
		}
		else{
			System.err.println("The directory is empty");
			tabNames = new String[0];
		}
		
		for (int i = 0; i < tabNames.length; i++) {
			System.out.println(tabNames[i]);
		}
	
	}

	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

        final String item = (String) parent.getItemAtPosition(position);
        Intent intent = new Intent(this, DisplayTabActivity.class);
		intent.putExtra(EXTRA_MESSAGE, item);
		startActivity(intent);
	}

}
