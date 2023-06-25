import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.util.Properties;



public class Main {

    static public ArrayList<String> nameArray = new ArrayList<String>();
    static public ArrayList<String> priceArray = new ArrayList<String>();

    static public List<String> checkArray = new ArrayList<String>();

    static Session session;

    static String from = "radionbes@gmail.com";
    static String to = "radionbeslaneev98@gmail.com";
    static String host = "smtp.gmail.com";
    static String smtpPort = "465";

    static Properties properties;
    //ссылка поиска
    static String s = "https://www.avito.ru/pyatigorsk/noutbuki?cd=1&p=30&q=%D0%B8%D0%B3%D1%80%D0%BE%D0%B2%D0%BE%D0%B9+%D0%BD%D0%BE%D1%83%D1%82%D0%B1%D1%83%D0%BA";

    //переменная ДЛЯ ссылки
    static String url;

    static Document page;

    public static void main(String[] args) throws MessagingException {
        Prop();
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

        checkArray = List.copyOf(nameArray);

        boolean check = false;
        for (int i = 0; i < nameArray.size(); i++) {
            String name1 = nameArray.get(i);
            for (int j = 0; j < checkArray.size(); j++) {
                String name = checkArray.get(i);
                check = name1.equals(name);
                if (!check) {
                    mes("Произошло изменение в поиске!!!");
                    break;
                }
            }
        }
        if (check) mes("Проверка произошла успешно, не выявлено никаких изменений!");

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