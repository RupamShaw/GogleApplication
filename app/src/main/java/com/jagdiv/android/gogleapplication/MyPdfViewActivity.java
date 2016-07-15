package com.jagdiv.android.gogleapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.ScrollBar;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import  com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.shockwave.pdfium.PdfDocument;

import java.util.List;

import static java.lang.String.format;


public class MyPdfViewActivity extends BaseActivity implements OnPageChangeListener, OnLoadCompleteListener, OnErrorListener {
   public static String EXTRA_PDFFILENAME="EXTRA_PDFFILENAME";
    private Button mBackButton;
  //  @NonConfigurationInstance
    Integer pageNumber = 1;
    String pdfName = "";
    private static final String TAG = MyPdfViewActivity.class.getSimpleName();

    PDFView pdfView;
    ScrollBar scrollBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pdf_view);
        String toolbarTitle = getResources().getString(R.string.title_activity_news_letter);
        toolBar(savedInstanceState, toolbarTitle);
         pdfView = (PDFView) findViewById(R.id.pdfView);
        ScrollBar scrollBar = (ScrollBar) findViewById(R.id.scrollBar);
        pdfView.setScrollBar(scrollBar);
        mBackButton=(Button)findViewById(R.id.backbtn);
        //pdfName=  "367006CONSOLIDATEDREPORT05072016.pdf";
       Intent intent=getIntent();
        pdfName= intent.getStringExtra(EXTRA_PDFFILENAME);
     //   pdfName=  "Copy of Community_Bus_Timetable_Route_1.pdf";
        java.io.File f = new java.io.File(Environment.getExternalStorageDirectory()
                + java.io.File.separator + pdfName);
                 pdfView.fromFile(f)
              //  .pages(0, 2, 1, 3, 3, 3) //all pages are displayed by default
                .enableSwipe(true)
                .enableDoubletap(true)
                .swipeVertical(false)
                .defaultPage(pageNumber)
                .showMinimap(false)
                .onPageChange(this)
                .onLoad(this)
                .onPageChange(this)
                . onError(this)
                .password("DEE2108")
                .load();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }
  //  @AfterViews
    void afterViews() {
        pdfView.setScrollBar(scrollBar);
    /*    if (uri != null) {
            displayFromUri(uri);
        } else {
            displayFromAsset(SAMPLE_FILE);
        }
      */
        setTitle(pdfName);
    }

    String pdfpath(String filename){
        java.io.File f = new java.io.File(Environment.getExternalStorageDirectory()
                + java.io.File.separator + filename);

        String path = f.getPath();
        return path;
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(format("%s %s / %s", pdfName, page, pageCount));
    }
    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        Log.e(TAG, "title = " + meta.getTitle());
        Log.e(TAG, "author = " + meta.getAuthor());
        Log.e(TAG, "subject = " + meta.getSubject());
        Log.e(TAG, "keywords = " + meta.getKeywords());
        Log.e(TAG, "creator = " + meta.getCreator());
        Log.e(TAG, "producer = " + meta.getProducer());
        Log.e(TAG, "creationDate = " + meta.getCreationDate());
        Log.e(TAG, "modDate = " + meta.getModDate());

        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }
    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    @Override
    public void onError(Throwable t) {
        Log.e(TAG,"error in decrypting");
    }
}
