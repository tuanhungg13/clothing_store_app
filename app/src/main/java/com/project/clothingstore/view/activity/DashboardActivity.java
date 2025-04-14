package com.project.clothingstore.view.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.project.clothingstore.R;
import com.project.clothingstore.viewmodel.DashboardViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {
    private DashboardViewModel dashboardViewModel;
    private TextView tvOrderCount, tvNewUserCount, tvShippedCount, tvRatedCount;
    private LineChart lineChart;
    private ImageButton btnBack, btnPickDate;
    private TextView tvSelectedDate;

    private final Calendar calendar = Calendar.getInstance(); // Ngày được chọn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initViews();
        observeViewModel();
        setupDatePicker();
        updateDateText(); // hiển thị ngày hôm nay
        loadDashboardData(); // tải dữ liệu của hôm nay
    }

    private void initViews() {
        tvOrderCount = findViewById(R.id.tvOrderCount);
        tvNewUserCount = findViewById(R.id.tvNewUserCount);
        tvShippedCount = findViewById(R.id.tvShippedCount);
        tvRatedCount = findViewById(R.id.tvRatedCount);
        lineChart = findViewById(R.id.lineChart);
        btnBack = findViewById(R.id.btn_back);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        btnPickDate = findViewById(R.id.btnPickDate);

        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        btnBack.setOnClickListener(v -> finish());
    }

    private void observeViewModel() {
        dashboardViewModel.getOrderCount().observe(this, count ->
                tvOrderCount.setText(String.valueOf(count)));

        dashboardViewModel.getNewUserCount().observe(this, count ->
                tvNewUserCount.setText(String.valueOf(count)));

        dashboardViewModel.getShippedCount().observe(this, count ->
                tvShippedCount.setText(String.valueOf(count)));

        dashboardViewModel.getRatedCount().observe(this, count ->
                tvRatedCount.setText(String.valueOf(count)));

        dashboardViewModel.getRevenueData().observe(this, revenueMap -> {
            if (revenueMap != null) {
                showRevenueChart(revenueMap);
            }
        });

        dashboardViewModel.getErrorMessage().observe(this, event -> {
            if (event != null) {
                String errorMessage = event.getContentIfNotHandled();
                if (errorMessage != null) {
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupDatePicker() {
        btnPickDate.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    DashboardActivity.this,
                    (DatePicker view, int y, int m, int d) -> {
                        calendar.set(y, m, d);
                        updateDateText();
                        loadDashboardData();
                    },
                    year, month, day
            );
            dialog.show();
        });
    }

    private void updateDateText() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        tvSelectedDate.setText(sdf.format(calendar.getTime()));
    }
    private void showRevenueChart(Map<String, Double> revenueMap) {
        List<Entry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        int index = 0;
        for (Map.Entry<String, Double> entry : revenueMap.entrySet()) {
            entries.add(new Entry(index, entry.getValue().floatValue()));
            labels.add(entry.getKey());
            index++;
        }

        LineDataSet dataSet = new LineDataSet(entries, "Doanh thu (VNĐ)");
        dataSet.setLineWidth(2f);
        dataSet.setColor(getColor(R.color.blue_primary));
        dataSet.setValueTextSize(10f);
        dataSet.setCircleRadius(4f);
        dataSet.setCircleColor(getColor(R.color.blue_primary));
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int i = (int) value;
                return (i >= 0 && i < labels.size()) ? labels.get(i) : "";
            }
        });

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setGranularity(1f);
        leftAxis.setDrawGridLines(true);

        lineChart.getAxisRight().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(true);
        lineChart.animateY(500);
        lineChart.invalidate();
    }


    private void loadDashboardData() {
        Date selectedDate = calendar.getTime();
        dashboardViewModel.loadDashboardData(selectedDate);
        dashboardViewModel.loadRevenue7Days();
    }
}
