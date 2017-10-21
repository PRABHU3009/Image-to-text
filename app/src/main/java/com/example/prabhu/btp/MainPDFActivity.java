package com.example.prabhu.btp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;


public class MainPDFActivity extends AppCompatActivity {
    AsyncTask<Void, Void, Void> search_file = new searchfiles();

    ProgressDialog progressCopy;
    public ArrayList<PDFDoc> PDFlist;
    ArrayList<PDFDoc> pdfDocs;
    ListView gv;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdfactivity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gv= (ListView) findViewById(R.id.gv);
        context=MainPDFActivity.this;
        //PDFlist=getPDFs();
        //gv.setAdapter(new CustomAdapter(MainPDFActivity.this,PDFlist));
        progressCopy = new ProgressDialog(MainPDFActivity.this);
        progressCopy.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressCopy.setIndeterminate(true);
        progressCopy.setCancelable(false);
        progressCopy.setTitle("PDF");
        progressCopy.setMessage("Searching files");
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPDFActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        search_file.execute();
    }

    private ArrayList<PDFDoc> getPDFs()
    {
        pdfDocs=new ArrayList<>();
        walkdir(Environment.getExternalStorageDirectory(),pdfDocs);
        return pdfDocs;
    }

    public void walkdir(File dir, ArrayList<PDFDoc> pdfDocs) {
        String pdfPattern = ".pdf";
        PDFDoc pdfDoc;

        File listFile[] = dir.listFiles();

        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    walkdir(listFile[i], pdfDocs);
                } else {
                    if (listFile[i].getName().endsWith(pdfPattern)){
                        //Do what ever u want
                        pdfDoc = new PDFDoc();
                        pdfDoc.setName(listFile[i].getName());
                        pdfDoc.setPath(listFile[i].getAbsolutePath());
                        pdfDocs.add(pdfDoc);

                    }
                }
            }
        }
        Collections.sort(pdfDocs, PDFDoc.StuNameComparator);

    }

    private class searchfiles extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressCopy.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressCopy.cancel();
            gv.setAdapter(new CustomAdapter(MainPDFActivity.this,PDFlist));

        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i("OCRTask","extracting..");
            PDFlist=getPDFs();
            return null;
        }
    }
   /*public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        MenuItem searchItem=menu.findItem(R.id.item_search);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<PDFDoc> templist=new ArrayList<PDFDoc>();
                for (PDFDoc pdfDoc1:PDFlist){
                    if(newText.toLowerCase().contains(pdfDoc1.toString().toLowerCase()))
                        templist.add(pdfDoc1);
                }
                gv.setAdapter(new CustomAdapter(context,templist));
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }*/


}
