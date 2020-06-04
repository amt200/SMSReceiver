package sg.edu.rp.webservices.smsreceiver;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSMSNumber extends Fragment {

    EditText etSMSNumber;
    Button btnRetrieveByNumber;
    TextView tvSMS;

    public FragmentSMSNumber() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_s_m_s_number, container, false);

        etSMSNumber = view.findViewById(R.id.etSMSNumber);
        btnRetrieveByNumber = view.findViewById(R.id.btnRetrieveByNumber);
        tvSMS = view.findViewById(R.id.tvSMSNumber);

        btnRetrieveByNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etSMSNumber.getText().toString().length() > 0 && getActivity() != null){
                    int permissionCheck = PermissionChecker.checkSelfPermission
                            (getActivity(), Manifest.permission.READ_SMS);

                    if (permissionCheck != PermissionChecker.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_SMS}, 0);
                        // stops the action from proceeding further as permission not
                        //  granted yet
                        return;
                    }

                    // Create all messages URI
                    Uri uri = Uri.parse("content://sms/inbox");
                    //  date is when the message took place
                    //  address is the number of the other party
                    //  body is the message content
                    //  type 1 is received, type 2 sent
                    String[] reqCols = new String[]{"date", "address", "body", "type"};

                    // Get Content Resolver object from which to
                    //  query the content provider
                    ContentResolver cr = getActivity().getContentResolver();
                    // Fetch SMS Message from Built-in Content Provider
                    String selection = "address LIKE ?";
                    String[] args = {"%"+etSMSNumber.getText().toString()+"%"};
                    Cursor cursor = cr.query(uri, reqCols, selection, args, null);
                    String smsBody = "";
                    if (cursor.moveToFirst()) {
                        do {
                            long dateInMillis = cursor.getLong(0);
                            String date = (String) DateFormat
                                    .format("dd MMM yyyy h:mm:ss aa", dateInMillis);
                            String address = cursor.getString(1);
                            String body = cursor.getString(2);
                            String type = cursor.getString(3);
                            Log.d("Address", address);
                                type = "Inbox:";

                            smsBody += type + " " + address + "\n at " + date
                                    + "\n\"" + body + "\"\n\n";
                        } while (cursor.moveToNext());
                    }
                    tvSMS.setText(smsBody);
                }
                else{
                    if(getActivity() != null){
                        Toast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the read SMS
                    //  as if the btnRetrieve is clicked
                    btnRetrieveByNumber.performClick();

                } else {
                    // permission denied... notify user
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), "Permission not granted",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
