package mysecond.example.pankajthakr.barcodescanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedInputStream;

import static java.lang.Thread.sleep;

public class Main2Activity extends AppCompatActivity {
    LinearLayout l1,l2;
    ProgressBar progressBar;
    RelativeLayout r1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        l1=findViewById(R.id.l1);
        progressBar=findViewById(R.id.progressbar);
        r1=findViewById(R.id.r1);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    startActivity(new Intent(Main2Activity.this,LoginActivity.class));
                }
            }
        });
        t.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.admin:
                Toast.makeText(getApplicationContext(), "You Clicked:" +item.getTitle().toString(), Toast.LENGTH_LONG).show();
                startActivity(new Intent(Main2Activity.this,admin_login.class));
                break;
            case R.id.shopping_list:
                Toast.makeText(getApplicationContext(), "You Clicked:" +item.getTitle().toString(), Toast.LENGTH_LONG).show();
                startActivity(new Intent(Main2Activity.this,shopping_list.class));
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
