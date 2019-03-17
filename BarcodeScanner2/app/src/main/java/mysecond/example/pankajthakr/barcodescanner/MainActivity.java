package mysecond.example.pankajthakr.barcodescanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;



public class MainActivity extends AppCompatActivity {

    Button save, scan, logout, groupchat;
    final Activity activity = this;
    EditText et, et1, et2, etbar, etdes, etname, etpr, et11;

    DatabaseReference database;
    FirebaseAuth fauth;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout mDrawerLayout;
    private ImageView iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        save = (Button) findViewById(R.id.buttonsave);
        scan = (Button) findViewById(R.id.buttonscan);
        logout = (Button) findViewById(R.id.buttonlogout);
       // groupchat = (Button) findViewById(R.id.buttongroupChat);

        etbar = (EditText) findViewById(R.id.editText7);
        etdes = (EditText) findViewById(R.id.editText8);
        etname = (EditText) findViewById(R.id.editText9);
        etpr = (EditText) findViewById(R.id.editText10);


        database = FirebaseDatabase.getInstance().getReference("All Barcodes");

        //fauth = FirebaseAuth.getInstance();
        //et = (EditText) findViewById(R.id.editText7);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String B = etbar.getText().toString();
                String PN = etname.getText().toString();
                String PR = etpr.getText().toString();

                String PD = etdes.getText().toString();

                // String G=et4.getText().toString();
               // Product PO = new Product(B, PN, PD, PR);
                Product po = new Product();
                po.setBarcode(B);
                po.setPName(PN);
                po.setPrice(PR);
                po.setDescribe(PD);
                database.child("All Barcodes").child(B).setValue(po);
                //String getPr=po.getPrice();


                Log.d("Product", "Data inserted");
               // Log.i("price recieved",getPr);
                Toast.makeText(getApplicationContext(), "Data Successfully Saved", Toast.LENGTH_LONG).show();
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator in = new IntentIntegrator(activity);
                in.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                in.setPrompt("scan");
                in.setCameraId(0);
                in.setBeepEnabled(false);
                //in.setBarcodeImageEnabled(false);
                in.initiateScan();
            }
        });
        /*groupchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),FetchActivity.class));
            }
        });*/
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();

            } else {
                Log.d("MainActivity", "Scanned");
                etbar.setText(result.getContents());
                Toast.makeText(this, "Scanned", Toast.LENGTH_SHORT).show();

            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        // super.onActivityResult(
     etbar.setText(null);
     etdes.setText(null);
     etname.setText(null);
     etpr.setText(null);
    }


}