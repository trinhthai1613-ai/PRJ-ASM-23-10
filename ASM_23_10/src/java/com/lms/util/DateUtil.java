package com.lms.util;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Utility class để tính toán số ngày nghỉ
 */
public class DateUtil {
    
    /**
     * Tính số ngày làm việc giữa 2 ngày (không tính thứ 7, chủ nhật)
     */
    public static BigDecimal calculateWorkingDays(LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null || toDate == null || fromDate.isAfter(toDate)) {
            return BigDecimal.ZERO;
        }
        
        long totalDays = ChronoUnit.DAYS.between(fromDate, toDate) + 1; // +1 để bao gồm cả ngày cuối
        long workingDays = 0;
        
        LocalDate current = fromDate;
        while (!current.isAfter(toDate)) {
            DayOfWeek dayOfWeek = current.getDayOfWeek();
            if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
                workingDays++;
            }
            current = current.plusDays(1);
        }
        
        return new BigDecimal(workingDays);
    }
    
    /**
     * Tính tổng số ngày (bao gồm cả cuối tuần)
     */
    public static BigDecimal calculateTotalDays(LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null || toDate == null || fromDate.isAfter(toDate)) {
            return BigDecimal.ZERO;
        }
        
        long days = ChronoUnit.DAYS.between(fromDate, toDate) + 1;
        return new BigDecimal(days);
    }
    
    /**
     * Kiểm tra xem một ngày có phải là ngày làm việc không
     */
    public static boolean isWorkingDay(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }
    
    /**
     * Kiểm tra xem 2 khoảng thời gian có overlap không
     */
    public static boolean isDateRangeOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return !start1.isAfter(end2) && !end1.isBefore(start2);
    }
    
    /**
     * Format date range thành string
     */
    public static String formatDateRange(LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null || toDate == null) {
            return "";
        }
        
        if (fromDate.equals(toDate)) {
            return fromDate.toString();
        }
        
        return fromDate.toString() + " - " + toDate.toString();
    }
}