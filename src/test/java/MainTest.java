import org.xml.sax.SAXException;

import javax.swing.text.Document;
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

import static org.junit.Assert.*;

public class MainTest {

    Main main = new Main();

    public MainTest() throws IOException, ParserConfigurationException, SAXException {
    }

    @org.junit.Test
    public void oneValuteAUD() throws Exception {
//        assertEquals("AUD",main.oneValute(doc));
    }

    @org.junit.Test
    public void allValutesPerDate() {
    }
}