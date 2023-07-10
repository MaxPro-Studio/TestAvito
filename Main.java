import java.io.IOException;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;

public class Main {
    static List<String> missingElements = new ArrayList<>();

    static public ArrayList<String> nameArray = new ArrayList<String>();
    static public ArrayList<String> priceArray = new ArrayList<String>();
    static public ArrayList<String> checkArray = new ArrayList<String>();



    static Session session;
    static String from = "radionbes@gmail.com";
    static String to = "radionbeslaneev98@gmail.com";
    static String host = "smtp.gmail.com";
    static String smtpPort = "465";
    static Properties properties;


    //ссылка поиска
    static String s = "https://www.avito.ru/pyatigorsk/avtomobili?cd=1&q=%D0%BA%D1%83%D0%BF%D0%B8%D1%82%D1%8C+%D0%B0%D0%B2%D1%82%D0%BE%D0%BC%D0%BE%D0%B1%D0%B8%D0%BB%D0%B8&radius=200&searchRadius=200";
    //переменная ДЛЯ ссылки
    static String url;



    static Document page;

    public static void main(String[] args) throws MessagingException {

        try {
            Prop();
        }
        catch (Exception e){
            mes("Произошла ошибка при запуске!\n" + e.toString());
        }

        try {
            ses();
        }
        catch (Exception e){
            System.out.println(e.toString());
            Poisk();
        }

        try {
            Poisk();
        }
        catch (Exception e){
            mes(e.toString());
            Poisk();
        }
    }

    static public void Poisk() throws MessagingException {
        url = s;

        try {
            page = Jsoup.parse(new URL(url),30000);
        } catch (IOException e) {
            mes(e.toString());
            Poisk();
        }

        try {
            getNameProduct();
            getPriceProduct();
        } catch (Exception e) {
            mes(e.toString());
            Poisk();
        }

        for (int i = 0; i < nameArray.size(); i++) {
            System.out.println(nameArray.get(i) + "  №" + (i+1));
            System.out.println(priceArray.get(i) + "\n");
        }
        System.out.println("\n\n\n\n");

        checkArray = new ArrayList<String>(List.copyOf(nameArray));

        findMissingElements(checkArray,nameArray);
        System.out.println("Элементы, которые есть во втором массиве, но нету в первом:" + missingElements);

        nameArray = new ArrayList<String>();
        priceArray = new ArrayList<String>();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            mes(e.toString());
            Poisk();
        }

        Poisk();
    }


    public static void findMissingElements(ArrayList<String> arr1, ArrayList<String> arr2) {
        for (String num : arr2) {
            if (!arr1.contains(num)) {
                missingElements.add(num);
            }
        }
    }

    private static void getNameProduct() {
        Elements elements = page.select("h3[itemprop=name]");
        for (Element s: elements) {
            String tostring = s.toString();
            String name0 = tostring.substring(302);
            String name = name0.substring(0,name0.length()-5);
            nameArray.add(name);
        }
    }

    private static void getPriceProduct() {
        Elements elements = page.select("meta[itemprop=price]");
        for (Element s: elements) {
            String tostring = s.toString();
            String price0 = tostring.substring(32);
            String price = price0.substring(0,price0.length()-2);
            String fullname = price + " ₽";
            priceArray.add(fullname);
        }
    }

    static void Prop(){
        properties = new Properties();
        properties.put("mail.smtp.host",host);
        properties.put("mail.smtp.port",smtpPort);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
    }

    static void ses(){
        session = Session.getInstance(
                properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, "eugygnopcwbwarsq");
                    }
                }
        );
    }

    static void mes(String info) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress());
        mimeMessage.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(to)));
        mimeMessage.setSubject("Проверка работоспособности!");
        mimeMessage.setText(info);
        Transport.send(mimeMessage);
    }

}