package test;



import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Scanner;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class Scraper {

    public static void main(String[] args)
            throws FailingHttpStatusCodeException, MalformedURLException,
            IOException {
    	WebClient webClient = new WebClient();
            System.setProperty("http.proxyHost","proxy.inf.kcl.ac.uk");
            System.setProperty("http.proxyPort","3128"); 
            
            webClient.getOptions().setRedirectEnabled(true);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getCookieManager().setCookiesEnabled(true);
            webClient.getOptions().setJavaScriptEnabled(false);
            System.setProperty("jsse.enableSNIExtension", "false");
            try{
            	/**
            	Issue running the link due to SSL
            	Probably because  in their certificate the common name 
            	doesn’t match with  url domain. This might be because
            	of the new update where keats.kcl.ac.uk holds the certificate
            	but https://login-keats.kcl.ac.uk/ manages the login and redirects
            	*/
            final HtmlPage Loginpage= (HtmlPage) webClient.getPage("keats.kcl.ac.uk/");

         final  HtmlForm form = Loginpage.getFormByName("loginform");
            System.out.println(""+form);
           final HtmlTextInput username = form.getInputByName("username");
         final HtmlPasswordInput password = form.getInputByName("password");
      
            final HtmlSubmitInput LogButton =  form.getInputByName("BtnLogin");
        
            Scanner in = new  Scanner(System.in);
          
            String user =  in.nextLine();
            String pass =  in.nextLine();
            
            username.setValueAttribute(user);
            password.setValueAttribute(pass);
            System.out.println(Loginpage.asText());

            // Click "Sign In" button/link
           final HtmlPage Scrappingpage = (HtmlPage) form.getInputByValue("Log in").click();

           String htmlBody = Scrappingpage.getWebResponse().getContentAsString(); 
           System.out.println(Scrappingpage.asText());
          System.out.println("Base Uri 1 : "+Loginpage);
         System.out.println("Base Uri 2 : "+Scrappingpage);
            }
            catch(Exception e){
            	
            }
           
           
            // I added the cookie section    
       //     Set<Cookie> cookie = webClient.getCookieManager().getCookies();

//            if(cookie != null){
//
//                Iterator<Cookie> i = cookie.iterator();
//
//                while (i.hasNext()) {
//
//                    webClient.getCookieManager().addCookie(i.next());
//
//                }
//
//            }
//
//
//            //  Get page as Html
//            String htmlBody = Loginpage1.getWebResponse().getContentAsString();
//            //  Save the response in a file
//            String filePath = "test_out.html";
//
//            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filePath)));
//            bw.write(htmlBody);
//            bw.close();

           
            webClient.closeAllWindows();
            

    }

}