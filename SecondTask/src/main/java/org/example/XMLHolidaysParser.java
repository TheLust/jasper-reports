package org.example;

import net.sf.jasperreports.engine.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.*;

public class XMLHolidaysParser {
    public static List<Holiday> getHolidays() throws JRException, FileNotFoundException {
        return parseXML("SecondTask\\src\\main\\resources\\MyDataBase.xml");
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