package com.pstl.gtfo.tablature.activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.pstl.gtfo.R;
import com.pstl.gtfo.tablature.generation.TablatureGenerator;
import com.pstl.gtfo.tablature.interfaces.ITablatureGenerator;
import com.pstl.gtfo.tablature.views.TabView;
import com.pstl.gtfo.tablature.views.TablatureView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Kevin Lorant on 18/02/2015.
 */
public class TabActivity extends Activity {
    private LinearLayout containerLayout;
    private HorizontalScrollView scrl;
    private Spinner tabSpin;
    ITablatureGenerator tabGen;
    private TabView tabView;
    private String tabName;
    private  String dir = "SoundProject";
    private File dirFile;
    private boolean isInit = false;
    private boolean canGo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        dirFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+dir+"/");
        scrl = (HorizontalScrollView) findViewById(R.id.scrl);
        tabSpin = (Spinner) findViewById(R.id.tabSpinner);
        tabView = (TabView)findViewById(R.id.custom_view);
        tabGen = new TablatureGenerator(dirFile, tabView);
        setTabSpinItem();
        setTabOSL();
    }

    private ArrayList<String> getNotesFilesNames(){
        ArrayList<String> list = new ArrayList<String>();
        list.add("Tablature");
        for(String f: dirFile.list()){
            if(f.endsWith(".txt")){
                list.add(f);
            }
        }
        return list;
    }

    private void setTabSpinItem(){
        ArrayList<String> list = getNotesFilesNames();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tabSpin.setAdapter(dataAdapter);
    }

    private void setTabOSL(){
        tabSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                switch (pos) {
                    case 0:{
                        if(!isInit){
                            ((TabView) tabView).setHeight(scrl.getHeight());
                            isInit = true;
                            canGo = false;
                        }
                        break;
                    }
                    default:{
                        if(!isInit){
                            ((TabView) tabView).setHeight(scrl.getHeight());
                            isInit = true;
                        }

                        if(!canGo) canGo = true;
                        tabName = String.valueOf(parent.getItemAtPosition(pos));
                        ((TablatureGenerator) tabGen).setNotesName(tabName);
                        //new try
                        optNBGen();//hideBElts();
                        //mgSpin.setVisibility(View.VISIBLE);
                        tabView.invalidate();
                        break;
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });
    }

    private void optNBGen(){
        tabGen.optDistConvert();
        tabView.invalidate();
    }


}
