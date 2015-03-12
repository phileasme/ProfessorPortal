package io;



import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Scanner;

public class Scraper {
	
	private String user = "";
	private String pass = "";
	private String webdata = "";
	
	
	public Scraper() throws FailingHttpStatusCodeException, MalformedURLException, IOException{
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
            
            	//System.out.println(Loginpage);

         final  List<HtmlForm> forms = Loginpage.getForms();
         for(HtmlForm form : forms){
        	if( form.getId().equals("login")){
        		
        	
            System.out.println(""+form);
           final HtmlTextInput username = form.getInputByName("username");
         final HtmlPasswordInput password = form.getInputByName("password");
      
        	
     
         Scanner in = new  Scanner(System.in);
         System.out.println("Webpage with student data :");
         
         //   http://keats.kcl.ac.uk/mod/page/view.php?id=886138
         webdata =in.nextLine();
         System.out.println("User name:");
        user =  in.nextLine();
      System.out.println(" password :");
          pass =  in.nextLine();

        
            username.setValueAttribute(user);
            password.setValueAttribute(pass);
            //System.out.println(Loginpage.asText());

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