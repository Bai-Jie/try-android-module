package gq.baijie.android.trymodule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Toast;

import gq.baijie.android.trymodule.business.Greeting;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);

    menu.add("Hello").setOnMenuItemClickListener(menuItem -> {
      final String text = Greeting.getInstance().say("World");
      Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
      return true;
    });

    return true;
  }
}
