package nju.com.piece;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.sleepbot.datetimepicker.time.TimePickerDialog.OnTimeSetListener;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import nju.com.piece.activity.MainActivity;
import nju.com.piece.adapter.IconImageAdaptor;
import nju.com.piece.adapter.adapterEntity.IconItem;
import nju.com.piece.database.DBFacade;
import nju.com.piece.database.TagType;
import nju.com.piece.database.pos.AccountPO;
import nju.com.piece.database.pos.TagPO;
import nju.com.piece.database.tools.DateTool;


public class NewTagActivity extends FragmentActivity implements OnDateSetListener,OnTimeSetListener{

    public static final String DATEPICKER_TAG = "选择日期";
    public static final String TIMEPICKER_TAG = "选择时间";

    private TextView date_text;
    private TextView plan_text;
    private EditText tag_name_edit;

    private GridView icon_grid;

    private Button addBtn;

    private static ArrayList<IconItem> icon_array = new ArrayList<IconItem>(){{
        //        add icons
        add(new IconItem(R.drawable.icon1_small));
        add(new IconItem(R.drawable.icon2_small));
        add(new IconItem(R.drawable.icon3_small));
        add(new IconItem(R.drawable.icon4_small));
        add(new IconItem(R.drawable.icon5_small));
        add(new IconItem(R.drawable.icon6_small));
        add(new IconItem(R.drawable.icon7_small));
        add(new IconItem(R.drawable.icon8_small));
        add(new IconItem(R.drawable.icon9_small));
        add(new IconItem(R.drawable.icon10_small));
        add(new IconItem(R.drawable.icon11_small));
        add(new IconItem(R.drawable.icon12_small));
        add(new IconItem(R.drawable.icon13_small));
        add(new IconItem(R.drawable.icon14_small));
        add(new IconItem(R.drawable.icon15_small));
        add(new IconItem(R.drawable.icon16_small));
        add(new IconItem(R.drawable.icon17_small));
        add(new IconItem(R.drawable.icon2_small));
        add(new IconItem(R.drawable.icon3_small));
        add(new IconItem(R.drawable.icon4_small));
        add(new IconItem(R.drawable.icon5_small));
        add(new IconItem(R.drawable.icon6_small));
        add(new IconItem(R.drawable.icon7_small));
        add(new IconItem(R.drawable.icon8_small));
        add(new IconItem(R.drawable.icon9_small));
        add(new IconItem(R.drawable.icon10_small));
        add(new IconItem(R.drawable.icon11_small));
        add(new IconItem(R.drawable.icon12_small));
        add(new IconItem(R.drawable.icon13_small));
        add(new IconItem(R.drawable.icon14_small));
        add(new IconItem(R.drawable.icon15_small));
        add(new IconItem(R.drawable.icon16_small));
        add(new IconItem(R.drawable.icon17_small));
    }};
    private IconImageAdaptor icon_adaptor;


    private boolean ifVibrate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tag);

        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), ifVibrate);
        final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY) ,calendar.get(Calendar.MINUTE), true, ifVibrate);

        date_text = (TextView)findViewById(R.id.date_text);

        plan_text = (TextView)findViewById(R.id.plan_text);

        date_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.setVibrate(ifVibrate);
                datePickerDialog.setYearRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 10);
                datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });
        plan_text.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                timePickerDialog.setVibrate(ifVibrate);
                timePickerDialog.setCloseOnSingleTapMinute(isCloseOnSingleTapMinute());
                timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
            }
        });


        if (savedInstanceState != null) {
            DatePickerDialog dpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag(DATEPICKER_TAG);
            if (dpd != null) {
                dpd.setOnDateSetListener(this);
            }
        }

        addBtn = (Button)findViewById(R.id.add_tag_btn);
        addBtn.setText("添加标签");

        tag_name_edit = (EditText)findViewById(R.id.name_edit);

        final DBFacade facade = new DBFacade(this);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                try {
                    Date end_date = DateTool.increDate(formatter.parse((String) date_text.getText()), 1);
                    int target = (int)(60.0*Double.valueOf((String)plan_text.getText()));
                    String tagName = tag_name_edit.getText().toString();
                    TagType type = TagType.relax;

                    int res = IconImageAdaptor.getSelectedRes();
                    IconImageAdaptor.clearSelecetedRes();

                    facade.addTag(new TagPO(tagName,type,res,target,end_date));
                    TagPO po = facade.getTag("广告广告关于");

                    Log.d("database_test",DateTool.getMonth(po.getStartDate())+"");

                    Intent intent = new Intent(NewTagActivity.this, MainActivity.class);
                    startActivity(intent);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });


        icon_adaptor = new IconImageAdaptor(this,R.layout.icon,icon_array);

        icon_grid = (MyGridView)findViewById(R.id.icon_grid);
        icon_grid.setAdapter(icon_adaptor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_tag, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private boolean isCloseOnSingleTapDay() {
        return true;
    }

    private boolean isCloseOnSingleTapMinute() {
        return true;
    }


    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        date_text.setText(year+"/"+(month+1)+"/"+day);
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int min) {
        double text_hour = hour+min/60.0;
        plan_text.setText(String.format("%.1f", text_hour));
    }
}
