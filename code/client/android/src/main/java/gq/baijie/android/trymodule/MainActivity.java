package gq.baijie.android.trymodule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Toast;

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
      Toast.makeText(MainActivity.this, "Hello World!", Toast.LENGTH_LONG).show();
      return true;
    });

    return true;
  }
}
