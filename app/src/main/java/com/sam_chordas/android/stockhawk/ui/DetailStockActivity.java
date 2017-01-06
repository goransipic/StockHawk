package com.sam_chordas.android.stockhawk.ui;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;

import java.util.ArrayList;
import java.util.List;

import static com.sam_chordas.android.stockhawk.data.QuoteProvider.Quotes.CONTENT_URI;

public class DetailStockActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private int ID = 0;
    private String mSymbol;
    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mChart = (LineChart) findViewById(R.id.chart);
        mSymbol = getIntent().getStringExtra(QuoteColumns.SYMBOL);

        getSupportLoaderManager().initLoader(ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
       /* return new CursorLoader(
                DetailStockActivity.this,
                CONTENT_URI,
                new String[]{QuoteColumns.BIDPRICE},
                QuoteColumns.SYMBOL + "=" + mSymbol,
                null,
                null);*/
        return new CursorLoader(
                DetailStockActivity.this,
                CONTENT_URI,
                new String[]{QuoteColumns.BIDPRICE},
                QuoteColumns.SYMBOL + "=?",
                new String[]{mSymbol},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<Entry> entries = new ArrayList<Entry>();
        float x = 0;
        while (data.moveToNext()) {
            String s = data.getString(data.getColumnIndex(QuoteColumns.BIDPRICE));
            entries.add(new Entry(x++,Float.parseFloat(s)));
            //Toast.makeText(this, data.getString(data.getColumnIndex(QuoteColumns.BIDPRICE)), Toast.LENGTH_LONG).show();
        }
        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
        LineData lineData = new LineData(dataSet);
        mChart.setData(lineData);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
