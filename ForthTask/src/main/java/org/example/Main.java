package org.example;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import static net.sf.dynamicreports.report.builder.column.Columns.column;

import java.io.FileNotFoundException;
import java.text.ParseException;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.FieldBuilder;
import net.sf.dynamicreports.report.builder.chart.Bar3DChartBuilder;
import net.sf.dynamicreports.report.builder.chart.BarChartBuilder;
import net.sf.dynamicreports.report.builder.chart.CategoryChartSerieBuilder;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabRowGroupBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.*;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;

public class Main {
    public Main() throws JRException, FileNotFoundException {
        build();
    }

    private void build() throws JRException, FileNotFoundException {
        StyleBuilder leftTextBorder = stl.style().setBorder(stl.pen1Point()).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);

        FieldBuilder<String> countries = field("country", String.class);
        FieldBuilder<String> months = field("month", String.class);

        CrosstabRowGroupBuilder<String> countryGroup = ctab.rowGroup(countries)
                .setTotalHeader("Total for country")
                .setTotalHeaderStyle(stl.style(leftTextBorder));

        CrosstabColumnGroupBuilder<String> monthGroup = ctab.columnGroup(months)
                .setShowTotal(true)
                .setTotalHeader("Total for month")
                .setTotalHeaderStyle(stl.style(leftTextBorder));

        CrosstabBuilder crosstab = ctab.crosstab()
                .headerCell(cmp.text("Month / Country").setStyle(stl.style(leftTextBorder)))
                .rowGroups(countryGroup)
                .columnGroups(monthGroup)
                .setCellStyle(stl.style(leftTextBorder))
                .setStyle(leftTextBorder)
                .measures(
                        ctab.measure("Count", "holidayCount", Integer.class, Calculation.COUNT).setStyle(leftTextBorder))
                .setDataSource(createDataSource());

        VerticalListBuilder verticalListBuilder = cmp.verticalList();
        BarChartBuilder chart = cht.barChart();
        chart.setTitle("Chart")
                .setCategory(months)
                .addSerie(cht.serie("holidayCount", Integer.class).setLabel("No. of Holidays"))
                .setDataSource(createDataSource())
                .setCategoryAxisFormat(cht.axisFormat().setLabel("Month"));
        verticalListBuilder.add(chart);

        JasperReportBuilder report = report()
                .setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
                .summary(crosstab)
                .summary(verticalListBuilder);

        try {
            report.show();
        } catch (DRException e) {
            e.printStackTrace();
        }
    }

    private JRDataSource createDataSource() throws JRException, FileNotFoundException {
        DRDataSource dataSource = new DRDataSource("country", "month", "holidayCount");
        for (Holiday holiday : XMLHolidaysParser.getHolidays()) {
            String country = holiday.getCountry()
                    .replace("Italia", "IT")
                    .replace("Moldavia", "MD");
            String month = getMonthFromDate(holiday.getDate());
            dataSource.add(country, month, 1);
        }
        return dataSource;
    }

    private String getMonthFromDate(String dateString) {
        try {
            java.util.Date date = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(dateString);
            return new java.text.SimpleDateFormat("MM").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) throws JRException, FileNotFoundException {
        try {
            new Main();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}