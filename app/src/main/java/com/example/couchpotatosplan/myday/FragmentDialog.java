package com.example.couchpotatosplan.myday;

import static com.example.couchpotatosplan.weekly.CalendarUtils.formattedDate;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.couchpotatosplan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;

public class FragmentDialog extends DialogFragment {

    private EditText eventNameET;
    private LocalTime time;
    private Button save_btn;
    private DatabaseReference mDatabase;
    private ListView eventListView;
    private long postNum;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Write a message to the database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                    postNum = (snapshot.child("event").getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        mDatabase.child("event").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Log.e("firebase", "Error getting data", task.getException());
//                }
//                else {
//                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                }
//            }
//        });

        View view = inflater.inflate(R.layout.test_dialog, container, false);
        save_btn = (Button) view.findViewById(R.id.save_btn);
        time = LocalTime.now();
        eventListView = (ListView) view.findViewById(R.id.eventListView);

        initWidgets(view);
        saveEventAction(view);

        return view;
    }

    private void initWidgets(View view)
    {
        eventNameET = view.findViewById(R.id.eventNameET);
    }

    public void saveEventAction(View view)
    {
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eventName = eventNameET.getText().toString();
                boolean checked = false;

                if(!eventName.equals("")) {
                    writeNewEvent(formattedDate(LocalDate.now()), RandomNum(), eventName, checked);
                } else {
                    Toast.makeText(getContext(), "내용을 입력하세요", Toast.LENGTH_LONG).show();
                }
                dismiss();
            }
        });
    }

    public void writeNewEvent(String date, int time, String content, Boolean check) {
        MyDayEvent event = new MyDayEvent(date, time, content, check);
        mDatabase.child("event").child(String.valueOf(postNum+1)).setValue(event);
    }

    public int RandomNum() {
        Random random = new Random();
        int time = random.nextInt(24) + 1;
        return time;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MyDayFragment()).commit();
    }
}
