package qr.sample.com.makeitsoon;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TabHost;

public class TaActivity extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
try {
    TabHost tabHost = getTabHost();
    TabHost.TabSpec spec;
    Intent intent;

    intent = new Intent().setClass(this, Main2Activity.class);
    spec = tabHost.newTabSpec("SELF").setIndicator("SELF")
            .setContent(intent);
    tabHost.addTab(spec);

    intent = new Intent().setClass(this,MainActivity .class);
    spec = tabHost.newTabSpec("OTHER").setIndicator("OTHER")
            .setContent(intent);
    tabHost.addTab(spec);

}catch (Exception e){
    e.printStackTrace();
}
    }
}
