import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    public static void oneValute(Document doc) throws Exception {

        String valute;
        try (Scanner inValute = new Scanner(System.in)) {
            System.out.println("What valute data do you need? Type valute short name - 3 letters");
            valute = inValute.next();
        }

        int itemId = 0;
        boolean wasFound = false;
        while (doc.getDocumentElement().getElementsByTagName("CharCode").item(itemId)!=null){
            String charCode = doc.getDocumentElement()
                    .getElementsByTagName("CharCode").item(itemId).getTextContent();
            if(charCode.equals(valute)){

                String name = doc.getDocumentElement()
                        .getElementsByTagName("Name").item(itemId).getTextContent();

                String value = doc.getDocumentElement()
                        .getElementsByTagName("Value").item(itemId).getTextContent();

                System.out.println(charCode + " (" + name + "): "+ value);
                wasFound=true;
            }
            itemId++;
        }
        if(!wasFound){
            throw new Exception("Check your input");
        }
    }

    public static void allValutesPerDate(Document doc) {
        int itemId = 0;
        while (doc.getDocumentElement().getElementsByTagName("CharCode").item(itemId)!=null){
            String charCode = doc.getDocumentElement()
                    .getElementsByTagName("CharCode").item(itemId).getTextContent();

            String name = doc.getDocumentElement()
                    .getElementsByTagName("Name").item(itemId).getTextContent();

            String value = doc.getDocumentElement()
                    .getElementsByTagName("Value").item(itemId).getTextContent();

            System.out.println(charCode + " (" + name + "): "+ value);
            itemId++;
        }
    }

    public static void main(String[] arg) throws Exception {
        StringBuilder response;
        Scanner in = new Scanner(System.in);
        try {
            System.out.println("Type data in specific format like DD/MM/YYYY or DD.MM.YYYY");
            String date = in.next();
            response = new StringBuilder();
            if (Pattern.matches("\\d{2}\\/\\d{2}\\/\\d{4}",date) ||
                    Pattern.matches("\\d{2}\\.\\d{2}\\.\\d{4}",date)){
                String url = "http://www.cbr.ru/scripts/XML_daily.asp?date_req=" + date;
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                try (BufferedReader inReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    while ((inputLine = inReader.readLine()) != null) {
                        response.append(inputLine);
                    }
                }
            } else {
                throw new Exception("Check your input correctness");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new ByteArrayInputStream(response.toString().getBytes()));

        try {
            System.out.println("Type 1, if you want to check info about concrete valute" +
                    " or 2, if you want data about all valutes");
            int i = in.nextInt();
            switch (i){
                case 1:
                    Main.oneValute(doc);
                    break;
                case 2:
                    Main.allValutesPerDate(doc);
                    break;
                default:
                    throw new Exception("Check your input");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        in.close();
    }
}