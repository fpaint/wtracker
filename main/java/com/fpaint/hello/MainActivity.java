package com.fpaint.hello;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    public final static String EXTRA_MESSAGE = "com.fpaint.hello.MESSAGE";
    protected ButtonCounter m_counter;

    class ButtonCounter {

        protected int m_pressed = 0;
        protected MainActivity m_host;

        public ButtonCounter(MainActivity host) {
           m_host = host;
        }

        public void onToggle(int state) {
           m_pressed += state == 1 ? 1 : -1;
           updateCountText();
        }

        public void updateCountText() {
            m_host.updateCountText(m_pressed);
        }
    }

    class ButtonCtx {
        public int state = 0;
        ButtonCounter m_cntr;

        public ButtonCtx(ButtonCounter cntr) {
           m_cntr = cntr;
        }

        void toggleState() {
          state = (state == 0) ? 1 : 0;
          m_cntr.onToggle(state);
        }

        void clear() {
            if(state != 0) {
                state = 0;
                m_cntr.onToggle(state);
            }
        }

        void updateBackground(View v) {
            v.setBackgroundResource(state == 0 ? R.drawable.button_gray : R.drawable.button_blue);
        }
    }

    class ButtonCallback implements Button.OnClickListener {

        public void onClick(View v) {
            ButtonCtx ctx = (ButtonCtx)v.getTag();
            ctx.toggleState();
            ctx.updateBackground(v);
        }
    }

    protected void setOrientation() {
        int current = getRequestedOrientation();
        // only switch the orientation if not in portrait
        if ( current != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE ) {
            setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout ll = (LinearLayout)findViewById(R.id.top_layout);
        // LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams();
        m_counter = new ButtonCounter(this);
        ButtonCallback callback = new ButtonCallback();
        int hour = 8;
        for(int i = 0; i<12; i++) {
            LinearLayout nl = new LinearLayout(this);
            TextView textview = new TextView(this);
            textview.setTextSize(8f);
            textview.setWidth(45);
            textview.setPadding(5,5,5,0);
            textview.setText(hour+":00");
            hour++;
            nl.addView(textview);
            for(int j=0;j<12;j++) {
                ImageButton button = new ImageButton(this);
               // button.setLayoutParams(lp);
                ButtonCtx ctx = new ButtonCtx(m_counter);
                ctx.updateBackground(button);
                button.setTag(ctx);
                button.setOnClickListener(callback);
                nl.addView(button);
            }
            ll.addView(nl);
        }
    }

    public void updateCountText(int count) {
        TextView textview = (TextView)findViewById(R.id.counterText);
        int hours = count / 12;
        int minutes = 5 * (count % 12);
        textview.setText(hours + ":" + String.format("%1$02d", minutes));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void clearField(View sender) {
        LinearLayout ll = (LinearLayout)findViewById(R.id.top_layout);
        for(int i = 0; i < ll.getChildCount(); i++) {
            View v = ll.getChildAt(i);
            if(v.getClass() == LinearLayout.class) {
                LinearLayout l = (LinearLayout)v;
                for(int j = 0; j < l.getChildCount(); j++) {
                    View vv = l.getChildAt(j);
                    if(vv.getClass() == ImageButton.class && vv.getTag().getClass() == ButtonCtx.class) {
                        ImageButton button = (ImageButton)vv;
                        ButtonCtx ctx = (ButtonCtx)vv.getTag();
                        ctx.clear();
                        ctx.updateBackground(button);
                    }
                }
            }
        }
        m_counter.updateCountText();
    }

    public void sendMessage(View view) {
        // Do something in response to button
        /*
        Intent intent = new Intent(this, DisplayMsgActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        */
    }
    
}
