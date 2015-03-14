package io;



import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Scanner;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class Scraper {
	private String webdata = "";
	private String module = "";
	private String user = "";
	private String pass = "";
	

    /**  http://keats.kcl.ac.uk/mod/page/view.php?id=886138 */
	public Scraper(String url,String mname,String numb,String pass) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		webdata = url;
		module = mname;
		user =  numb;
		this.pass = pass;
		
 System.setProperty("jsse.enableSNIExtension", "false");
    	WebClient webClient = new WebClient();
            
            webClient.getOptions().setRedirectEnabled(true);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getCookieManager().setCookiesEnabled(true);
            webClient.getOptions().setJavaScriptEnabled(false);
            System.setProperty("jsse.enableSNIExtension", "false");
       
            final HtmlPage Loginpage= (HtmlPage) webClient.getPage("https://login-keats.kcl.ac.uk/");
            
         final  List<HtmlForm> forms = Loginpage.getForms();
         for(HtmlForm form : forms){
        	if( form.getId().equals("login")){
        		
           final HtmlTextInput username = form.getInputByName("username");
         final HtmlPasswordInput password = form.getInputByName("password");
      
        
            username.setValueAttribute(user);
            password.setValueAttribute(pass);
      
            // Click "Sign In" button/link
           final HtmlPage Scrappingpage = (HtmlPage) form.getInputByValue("Log in").click();

           		/** Gets first page content while Logging in */
           String htmlBody = Scrappingpage.getWebResponse().getContentAsString(); 
           		
        	}
         }
        
         /**   Gets cells containt in plantext */
         final HtmlPage Scrappedpage= (HtmlPage) webClient.getPage(webdata);
        System.out.println(Scrappedpage.asText());
         
         /** Gets cells <h1>,<span>,etc.. */
         String scrappString= Scrappedpage.getWebResponse().getContentAsString(); 
       
         
       
      
      
            webClient.closeAllWindows();
            

    }

}