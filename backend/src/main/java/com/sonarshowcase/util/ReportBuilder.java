package com.sonarshowcase.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Report builder with extremely long methods
 * 
 * MNT-12: Long methods (200+ lines)
 */
public class ReportBuilder {

    private StringBuilder report;
    private SimpleDateFormat dateFormat;
    
    /**
     * Default constructor for ReportBuilder.
     */
    public ReportBuilder() {
        this.report = new StringBuilder();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    
    /**
     * MNT-12: Extremely long method - should be broken into smaller methods
     *
     * @param data Report data as a list of maps
     * @param reportType Type of report to generate
     * @param title Report title
     * @param author Report author
     * @param includeHeader Whether to include header section
     * @param includeFooter Whether to include footer section
     * @param includeSummary Whether to include summary section
     * @return Generated report as a string
     */
    public String buildFullReport(List<Map<String, Object>> data, String reportType, 
                                  String title, String author, boolean includeHeader,
                                  boolean includeFooter, boolean includeSummary) {
        
        report = new StringBuilder();
        
        // Header section - should be separate method
        if (includeHeader) {
            appendHeader(reportType, title, author);
        }
        
        // Data section - should be separate method
        appendDataSection(data);
        
        // Statistics section - should be separate method
        int totalRows = appendStatistics(data);
        
        // Summary section - should be separate method
        if (includeSummary) {
            appendSummary(reportType, author, totalRows);
        }
        
        // Footer section - should be separate method
        if (includeFooter) {
            appendFooter();
        }
        
        return report.toString();
    }
    
    private void appendHeader(String reportType, String title, String author) {
        report.append("╔══════════════════════════════════════════════════════════════════╗\n");
        report.append("║                                                                  ║\n");
        report.append("║                    SONARSHOWCASE REPORT                          ║\n");
        report.append("║                                                                  ║\n");
        report.append("╚══════════════════════════════════════════════════════════════════╝\n");
        report.append("\n");
        report.append("Report Type: ").append(reportType).append("\n");
        report.append("Title: ").append(title).append("\n");
        report.append("Author: ").append(author).append("\n");
        report.append("Generated: ").append(dateFormat.format(new Date())).append("\n");
        report.append("\n");
        report.append("────────────────────────────────────────────────────────────────────\n");
        report.append("\n");
    }
    
    private void appendDataSection(List<Map<String, Object>> data) {
        report.append("DATA SECTION\n");
        report.append("============\n\n");
        
        if (data != null && !data.isEmpty()) {
            int rowNumber = 1;
            for (Map<String, Object> row : data) {
                report.append("Row #").append(rowNumber).append(":\n");
                report.append("--------\n");
                
                if (row != null) {
                    for (Map.Entry<String, Object> entry : row.entrySet()) {
                        report.append("  ").append(entry.getKey()).append(": ");
                        appendFormattedValue(entry.getValue());
                        report.append("\n");
                    }
                }
                
                report.append("\n");
                rowNumber++;
            }
        } else {
            report.append("No data available.\n\n");
        }
    }
    
    private void appendFormattedValue(Object value) {
        if (value == null) {
            report.append("NULL");
        } else if (value instanceof String) {
            report.append("\"").append(value).append("\"");
        } else if (value instanceof Number) {
            report.append(value);
        } else if (value instanceof Date) {
            report.append(dateFormat.format((Date) value));
        } else if (value instanceof Boolean) {
            report.append(value);
        } else if (value instanceof List) {
            appendListValue((List<?>) value);
        } else if (value instanceof Map) {
            appendMapValue((Map<?, ?>) value);
        } else {
            report.append(value.toString());
        }
    }
    
    private void appendListValue(List<?> list) {
        report.append("[");
        for (int i = 0; i < list.size(); i++) {
            report.append(list.get(i));
            if (i < list.size() - 1) {
                report.append(", ");
            }
        }
        report.append("]");
    }
    
    private void appendMapValue(Map<?, ?> map) {
        report.append("{");
        int count = 0;
        for (Map.Entry<?, ?> e : map.entrySet()) {
            report.append(e.getKey()).append("=").append(e.getValue());
            if (count < map.size() - 1) {
                report.append(", ");
            }
            count++;
        }
        report.append("}");
    }
    
    private int appendStatistics(List<Map<String, Object>> data) {
        report.append("────────────────────────────────────────────────────────────────────\n");
        report.append("\n");
        report.append("STATISTICS\n");
        report.append("==========\n\n");
        
        int totalRows = data != null ? data.size() : 0;
        report.append("Total Rows: ").append(totalRows).append("\n");
        
        if (data != null && !data.isEmpty()) {
            int numberFields = appendFieldStatistics(data);
            
            // Calculate averages if number fields exist
            if (numberFields > 0) {
                appendNumericAnalysis(data);
            }
        }
        
        return totalRows;
    }
    
    private int appendFieldStatistics(List<Map<String, Object>> data) {
        int totalFields = 0;
        int nullFields = 0;
        int stringFields = 0;
        int numberFields = 0;
        int dateFields = 0;
        int otherFields = 0;
        
        for (Map<String, Object> row : data) {
            if (row != null) {
                for (Object value : row.values()) {
                    totalFields++;
                    if (value == null) {
                        nullFields++;
                    } else if (value instanceof String) {
                        stringFields++;
                    } else if (value instanceof Number) {
                        numberFields++;
                    } else if (value instanceof Date) {
                        dateFields++;
                    } else {
                        otherFields++;
                    }
                }
            }
        }
        
        report.append("Total Fields: ").append(totalFields).append("\n");
        report.append("Null Fields: ").append(nullFields).append("\n");
        report.append("String Fields: ").append(stringFields).append("\n");
        report.append("Number Fields: ").append(numberFields).append("\n");
        report.append("Date Fields: ").append(dateFields).append("\n");
        report.append("Other Fields: ").append(otherFields).append("\n");
        report.append("\n");
        
        return numberFields;
    }
    
    private void appendNumericAnalysis(List<Map<String, Object>> data) {
        report.append("Numeric Analysis:\n");
        report.append("-----------------\n");
        
        for (String key : data.get(0).keySet()) {
            appendNumericColumnAnalysis(data, key);
        }
    }
    
    private void appendNumericColumnAnalysis(List<Map<String, Object>> data, String key) {
        boolean isNumeric = true;
        double sum = 0;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        int count = 0;
        
        for (Map<String, Object> row : data) {
            Object value = row.get(key);
            if (value instanceof Number) {
                double num = ((Number) value).doubleValue();
                sum += num;
                min = Math.min(min, num);
                max = Math.max(max, num);
                count++;
            } else if (value != null) {
                isNumeric = false;
                break;
            }
        }
        
        if (isNumeric && count > 0) {
            double avg = sum / count;
            report.append("  ").append(key).append(":\n");
            report.append("    Min: ").append(String.format("%.2f", min)).append("\n");
            report.append("    Max: ").append(String.format("%.2f", max)).append("\n");
            report.append("    Avg: ").append(String.format("%.2f", avg)).append("\n");
            report.append("    Sum: ").append(String.format("%.2f", sum)).append("\n");
        }
    }
    
    private void appendSummary(String reportType, String author, int totalRows) {
        report.append("\n");
        report.append("────────────────────────────────────────────────────────────────────\n");
        report.append("\n");
        report.append("SUMMARY\n");
        report.append("=======\n\n");
        report.append("This report was generated automatically by the SonarShowcase system.\n");
        report.append("Report type: ").append(reportType).append("\n");
        report.append("Contains: ").append(totalRows).append(" records\n");
        report.append("Generated by: ").append(author).append("\n");
        report.append("Timestamp: ").append(dateFormat.format(new Date())).append("\n");
        report.append("\n");
    }
    
    private void appendFooter() {
        report.append("────────────────────────────────────────────────────────────────────\n");
        report.append("\n");
        report.append("╔══════════════════════════════════════════════════════════════════╗\n");
        report.append("║                       END OF REPORT                              ║\n");
        report.append("║                                                                  ║\n");
        report.append("║  This report is confidential and intended for internal use only  ║\n");
        report.append("║  Unauthorized distribution is prohibited                         ║\n");
        report.append("║                                                                  ║\n");
        report.append("║  © 2023 SonarShowcase Inc. All rights reserved.                  ║\n");
        report.append("╚══════════════════════════════════════════════════════════════════╝\n");
    }
    
    /**
     * Builds a simple report from data.
     * MNT: Duplicate of logic above
     *
     * @param data Report data as a list of maps
     * @return Generated simple report as a string
     */
    public String buildSimpleReport(List<Map<String, Object>> data) {
        StringBuilder sb = new StringBuilder();
        sb.append("Simple Report\n");
        sb.append("=============\n\n");
        
        if (data != null) {
            for (Map<String, Object> row : data) {
                if (row != null) {
                    for (Map.Entry<String, Object> entry : row.entrySet()) {
                        sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                    }
                    sb.append("\n");
                }
            }
        }
        
        return sb.toString();
    }
}

