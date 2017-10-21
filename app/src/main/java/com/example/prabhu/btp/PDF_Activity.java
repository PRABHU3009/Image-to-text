package com.example.prabhu.btp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;

import java.io.File;

public class PDF_Activity extends AppCompatActivity {


    Context context;
    PopupDisp disp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context=PDF_Activity.this;
        //PDFVIEW SHALL DISPLAY OUR PDFS
        PDFView pdfView= (PDFView) findViewById(R.id.pdfView);
        //SCROLLBAR TO ENABLE SCROLLING
       // ScrollHandle scrollBar = (ScrollBar) findViewById(R.id.scrollBar);
        //scrollBar=Configurator#scrollHandle()
        //pdfView.scrollHandle(scrollBar);
        //VERTICAL SCROLLING
        //scrollBar.setHorizontal(false);
        //SACRIFICE MEMORY FOR QUALITY
        //pdfView.useBestQuality(true)

        //UNPACK OUR DATA FROM INTENT
        Intent i=this.getIntent();
        String path=i.getExtras().getString("PATH");

        //GET THE PDF FILE
        File file=new File(path);

            if(file.canRead())
            {
                //LOAD IT

                pdfView.fromFile(file).defaultPage(1).onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {
                        Toast.makeText(PDF_Activity.this, String.valueOf(nbPages), Toast.LENGTH_LONG).show();
                    }
                }).load();

            }


    }
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);

        MenuItem searchItem=menu.findItem(R.id.item_search);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                disp=new PopupDisp(query,context);
                disp.show();
                Toast.makeText(getBaseContext(),"Defined as "+query, Toast.LENGTH_LONG).show();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if(id==R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
