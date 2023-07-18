package org.example;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.*;

public class XMLParser {
    public static void main(String[] args) throws JRException, FileNotFoundException {
        List<Holiday> holidays = parseXML("SecondTask\\src\\main\\resources\\MyDataBase.xml");
        for (Holiday holiday : holidays) {
            System.out.println(holiday);
        }

        String outputFile = "SecondTask\\src\\main\\resources\\" + "JasperSecondTask.pdf";

        /* Convert List to JRBeanCollectionDataSource */
        JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(holidays, false);

        /* Map to hold Jasper report Parameters */
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("CollectionBeanParam", itemsJRBean);

        //read jrxml file and creating jasperdesign object
        InputStream input = new FileInputStream(new File("SecondTask\\src\\main\\resources\\JasperSecondReport.jrxml"));

        JasperDesign jasperDesign = JRXmlLoader.load(input);

        /*compiling jrxml with help of JasperReport class*/
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        /* Using jasperReport object to generate PDF */
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

        /*call jasper engine to display report in jasperviewer window*/
        JasperViewer.viewReport(jasperPrint);


        /* outputStream to create PDF */
        OutputStream outputStream = new FileOutputStream(new File(outputFile));


        /* Write content to PDF file */
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

        System.out.println("File Generated");
    }

    private static List<Holiday> parseXML(String filePath) {
        List<Holiday> holidays = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(filePath);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("holydays");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String country = getElementValue(element, "COUNTRY");
                String name = getElementValue(element, "NAME");
                String date = getElementValue(element, "DATE");

                Holiday holiday = new Holiday(country, name, date);
                holidays.add(holiday);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return holidays;
    }

    private static String getElementValue(Element element, String tagName) {
        return element.getElementsByTagName(tagName).item(0).getTextContent();
    }

}