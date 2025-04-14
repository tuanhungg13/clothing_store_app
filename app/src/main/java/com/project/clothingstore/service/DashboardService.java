package com.project.clothingstore.service;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.project.clothingstore.utils.helper.FirebaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DashboardService {

    private static final CollectionReference userRef = FirebaseHelper.getUserCollection();
    private static final CollectionReference orderRef = FirebaseHelper.getOrderCollection();
    private static final CollectionReference ratingRef = FirebaseHelper.getRatingCollection();

    public void fetchDashboardData(Date selectedDate, ValueEventListener listener) {
        Timestamp startOfDay = getStartOfDay(selectedDate);
        Timestamp endOfDay = getEndOfDay(selectedDate);

        AtomicInteger orderCount = new AtomicInteger(0);
        AtomicInteger successCount = new AtomicInteger(0);

        orderRef
                .whereGreaterThanOrEqualTo("orderDate", startOfDay)
                .whereLessThanOrEqualTo("orderDate", endOfDay)
                .get()
                .addOnSuccessListener(orderQuery -> {
                    for (QueryDocumentSnapshot doc : orderQuery) {
                        orderCount.getAndIncrement();

                        String status = doc.getString("status");
                        if ("SUCCESS".equalsIgnoreCase(status)) {
                            successCount.getAndIncrement();
                        }
                    }

                    userRef
                            .whereGreaterThanOrEqualTo("createdAt", startOfDay)
                            .whereLessThanOrEqualTo("createdAt", endOfDay)
                            .get()
                            .addOnSuccessListener(userQuery -> {
                                int newUserCount = userQuery.size();

                                ratingRef
                                        .whereGreaterThanOrEqualTo("createdAt", startOfDay)
                                        .whereLessThanOrEqualTo("createdAt", endOfDay)
                                        .get()
                                        .addOnSuccessListener(ratingQuery -> {
                                            int ratingCount = ratingQuery.size();

                                            listener.onDataLoaded(
                                                    orderCount.get(),
                                                    newUserCount,
                                                    successCount.get(),
                                                    ratingCount
                                            );
                                        });
                            });
                });
    }

    private Timestamp getStartOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Timestamp(cal.getTime());
    }

    private Timestamp getEndOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return new Timestamp(cal.getTime());
    }
    public interface ValueEventListener {
        void onDataLoaded(int order, int newUser, int shipped, int rating);

        void onError(String error);
    }
    //Lấy dữ liệu thống kê 7 ngày gần nhất
    public void fetchRevenueLast7Days(OnRevenueDataListener listener) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        List<Timestamp> days = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            Calendar cal = (Calendar) calendar.clone();
            cal.add(Calendar.DAY_OF_YEAR, -i);
            days.add(new Timestamp(cal.getTime()));
        }

        orderRef
                .whereGreaterThanOrEqualTo("orderDate", days.get(0))
                .get()
                .addOnSuccessListener(query -> {
                    Map<String, Double> revenueMap = new LinkedHashMap<>();

                    for (int i = 0; i < 7; i++) {
                        String key = formatDate(days.get(i).toDate());
                        revenueMap.put(key, 0.0);
                    }

                    for (QueryDocumentSnapshot doc : query) {
                        Timestamp orderDate = doc.getTimestamp("orderDate");
                        String status = doc.getString("status");
                        Double total = doc.getDouble("totalPrice");

                        if (orderDate != null && "SUCCESS".equalsIgnoreCase(status) && total != null) {
                            String key = formatDate(orderDate.toDate());
                            if (revenueMap.containsKey(key)) {
                                revenueMap.put(key, revenueMap.get(key) + total);
                            }
                        }
                    }

                    listener.onRevenueLoaded(revenueMap);
                });
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM", Locale.getDefault());
        return sdf.format(date);
    }

    public interface OnRevenueDataListener {
        void onRevenueLoaded(Map<String, Double> dailyRevenue);

        void onError(String error);
    }

}
