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
            System.out.println("Данные о какой валюте вы хотите получить?");
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
            throw new Exception("Проверьте корректность введеного сокращения валюты");
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
            System.out.println("Введите желаемую дату в формате DD/MM/YYYY или в формте DD.MM.YYYY");
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
                throw new Exception("Проверьте корректность введенных данных");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new ByteArrayInputStream(response.toString().getBytes()));

        try {
            System.out.println("Введите 1, если хотите получить данные о конкретной валюте" +
                    " или 2, чтобы получить данные о всех валютах");
            int i = in.nextInt();
            switch (i){
                case 1:
                    Main.oneValute(doc);
                    break;
                case 2:
                    Main.allValutesPerDate(doc);
                    break;
                default:
                    throw new Exception("Проверьте ввод");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        in.close();
    }
}