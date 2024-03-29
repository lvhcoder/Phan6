package com.lvh.phan6;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lvh.phan6.adapter.AdapterStudent;
import com.lvh.phan6.database.StudentDao;
import com.lvh.phan6.model.Student;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteActivity extends AppCompatActivity {
    private EditText edMSHS, edTenHS, edLop;
    private RecyclerView recyclerView;
    private AdapterStudent adapterStudent;
    private StudentDao studentDao;
    private Toolbar toolbar;
    private List<Student> studentList = new ArrayList<>();
    private String strMS, strTen, strLop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SQLiteActivity.this);
                builder.setTitle("Thêm học sinh ");
                View view1 = getLayoutInflater().inflate(R.layout.dialog_them, null);
                builder.setView(view1);
                builder.setCancelable(false);
                edTenHS = view1.findViewById(R.id.edTenHS);
                edMSHS = view1.findViewById(R.id.edMSHS);
                edLop = view1.findViewById(R.id.edLop);
                recyclerView = view.findViewById(R.id.recyclerView);
                builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        studentDao = new StudentDao(SQLiteActivity.this);
                        if (check() > 0) {
                            Toast.makeText(SQLiteActivity.this, "Nhập đầy đủ thoong tin", Toast.LENGTH_SHORT).show();
                        }
                        Student student = new Student(edMSHS.getText().toString(), edTenHS.getText().toString(), edLop.getText().toString());
                        if (studentDao.insert(student) > 0) {
                            try {
                                studentList = studentDao.getAllStudent();
                                Toast.makeText(SQLiteActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                onResume();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setTitle("Thêm HS");
                dialog.show();

            }
        });
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
    }

    public void onResume() {
        super.onResume();
        try {
            studentList = studentDao.getAllStudent();
            adapterStudent.changeDataset(studentDao.getAllStudent());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int check() {
        int check1 = 1;
        strMS = edMSHS.getText().toString();
        strTen = edTenHS.getText().toString();
        strLop = edLop.getText().toString();
        if (strMS.isEmpty() || strLop.isEmpty() || strTen.isEmpty()) {
            Toast.makeText(this, "nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            check1 = -1;
        }
        return check1;
    }
}
