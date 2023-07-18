package org.example;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;

import javax.imageio.ImageIO;

public class Main {

    public Main() {
        build();
    }

    private void build() {
        try {
            BufferedImage logo = ImageIO.read(new File("SecondTask\\src\\main\\resources\\logo-no-bg.png"));
            report()// create new report design
                    .columns(// add columns
                            // title, field name data type
                            col.column("Item", "country", type.stringType()).setWidth(20),
                            col.column("Name", "name", type.stringType()).setWidth(60),
                            col.column("Date", "date", type.stringType()).setWidth(20))
                    .setColumnTitleStyle(stl.style().setBorder(stl.pen1Point()).setBackgroundColor(Color.LIGHT_GRAY))
                    .setColumnStyle(stl.style().setBorder(stl.pen1Point()))
                    .title(cmp.horizontalList().add(cmp.text("Holidays").setStyle(stl.style().setFontSize(20))).add(cmp.image(logo)))// shows report title
                    .pageFooter(cmp.pageXofY())// shows number of page at page footer
                    .setDataSource(createDataSource())// set datasource
                    .show();// create and show report
        } catch (DRException | JRException | ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    private JRDataSource createDataSource() throws JRException, FileNotFoundException, ParseException {

        DRDataSource dataSource = new DRDataSource("country", "name", "date");
        for (Holiday holiday : XMLHolidaysParser.getHolidays()) {
            dataSource.add(holiday.getCountry()
                                  .replace("Italia", "IT")
                                  .replace("Moldavia", "MD"),
                           holiday.getName(),
                    new java.text.SimpleDateFormat("MM/dd/yy hh:mm a")
                                  .format(new java.text.SimpleDateFormat("dd/MM/yyyy")
                                          .parse(holiday.getDate() + " 12:00 AM")));
        }
        return dataSource;
    }

    public static void main(String[] args) {
        new Main();
    }
}
