package mysecond.example.pankajthakr.barcodescanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class shopping_list extends AppCompatActivity {
    Button btnsaveit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        btnsaveit=findViewById(R.id.buttonsaveit);

        btnsaveit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),FetchActivity.class));
            }
        });

    }
}
