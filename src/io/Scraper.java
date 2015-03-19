package io;



import gui.Scatterplot;
import gui.ScrapperPopUp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import records.Logs;
public class Scraper {
	private String webdata = "";
	private String module = "";
	private String user = "";
	private String pass = "";

	private ArrayList<String> fullString = new ArrayList<String>();

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




		HtmlForm form = Loginpage.getFirstByXPath("//form[@id='login']");

		final HtmlTextInput username = form.getInputByName("username");
		final HtmlPasswordInput password = form.getInputByName("password");


		username.setValueAttribute(user);
		password.setValueAttribute(pass);

		// Click "Sign In" button/link
		final HtmlPage Scrappingpage = (HtmlPage) form.getInputByValue("Log in").click();


		/* Gets first page content while Logging in */
		String htmlBody = Scrappingpage.getWebResponse().getContentAsString(); 



		/*   Gets cells containt in plantext */
		final HtmlPage Scrappedpage= (HtmlPage) webClient.getPage(webdata);

		String allScrappedPage = Scrappedpage.asText();

		if(allScrappedPage.contains("Last accessSort by Last access Descending")){
			Logs.loggedin = true;
			Logs.user = this.user;
			Logs.pass = this.pass;
		}



		BufferedReader bufferReader = new BufferedReader(new StringReader(allScrappedPage));
		String currentLine = null;
		int count =0 ;

		while((currentLine=bufferReader.readLine()) != null){

			if((currentLine.contains(Character.toString((char)64)))){


				fullString.add(currentLine);


			}

		}
		for(String current : fullString){

			int whereChar64Is =0;
			int startOfEmail = 0;
			int indexer  = 0;
			int endOfEmail = 0;
			int endOfDate = 0;
			for( indexer = 0 ; indexer<current.length(); indexer++){

				if(current.charAt(indexer) == (char)64){

					endOfEmail = indexer;
					boolean blank = false;
					while(blank == false){
						char ch = current.charAt(endOfEmail);
						if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n' ){


							endOfEmail = endOfEmail -1;
							blank = true;
						}
						endOfEmail++;

					}
					break;
				}

			}


			boolean blank = false;
			startOfEmail=indexer;
			while(blank == false){
				char ch = current.charAt(startOfEmail);
				if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n' ){
					startOfEmail = startOfEmail+2;
					blank = true;
				}
				startOfEmail--;
			}
			int x = current.length()-1;
			while(blank == true){
				if((String.valueOf((current.charAt(x))).matches("[a-zA-Z]"))){
					endOfDate = x+1;
					blank = false;
				}
				x--;
			}

			int startOfDate = endOfEmail;

			while(blank == false){

				if((String.valueOf(current.charAt(startOfDate+1)).matches("^[0-9]*$"))){

					blank = true;
				}
				else if(current.substring(startOfDate,endOfDate).equals("now")){
					startOfDate = startOfDate-1;
					blank = true;
				}
				startOfDate ++;

			}


			TreeMap<String,String> modDate = new TreeMap<String,String>();

			String fullEmail =current.substring(startOfEmail, endOfEmail);
			String fullDate =current.substring(startOfDate,endOfDate);
			modDate.put(mname,fullDate);
			Logs.addToParticipationData(fullEmail, mname, fullDate);
			System.out.println(Logs.getParticipation().get(fullEmail).firstKey());
			System.out.println(Logs.getParticipation().get(fullEmail).get(Logs.getParticipation().get(fullEmail).firstKey()));

		}


		System.out.println(Logs.loggedin);
		/* Gets cells <h1>,<span>,etc.. */
		String scrappString= Scrappedpage.getWebResponse().getContentAsString(); 




		webClient.closeAllWindows();
	}
	public String getuser(){
		return user;
	}
	public String getpass(){
		return pass;
	}


	public static boolean isBlank(String s)
	{
		return (s == null) || (s.trim().length() == 0);
	}

}