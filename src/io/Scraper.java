package io;



import gui.Scatterplot;
import gui.ScraperPopUp;

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


/**
 * Class that Scrapes the data out of keat's and lets the user login to Keats
 * and get the participation data's from a given Web link.
 * 
 * @author Phileas Hocquard
 * 
 */

public class Scraper {
	
	/** The participation data link. */
	private String webdata = "";
	
	/** The module. */
	private String module = "";
	
	/** The user's k-number. */
	private String user = "";
	
	/** The user's password. */
	private String pass = "";

	/** The full string. */
	private ArrayList<String> fullString = new ArrayList<String>();

	/**
	 * 
	 * Sets the webClient, makes the webClient fetch the login page of Keats.
	 * Looking for the needed fields in the login form to put in the username
	 * and password. Once Logged in fetch for the participation data htmlpage.
	 * Add the Users K-number and password in the Logs class if there is a
	 * successful login.
	 * 
	 *Filter the content of the page by line and grab those containing '@'.
	 * Grab the email and date of each student for each line.
	 * Gives the participation data to the Logs Object.
	 *   
	 * @param url the URL
	 * @param mname the module name
	 * @param numb the username's K-number
	 * @param pass the password
	 * @throws FailingHttpStatusCodeException the failing HTTP status code exception
	 * @throws MalformedURLException the malformed URL exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see Logs
	 */
	public Scraper(String url,String mname,String numb,String pass) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
	
		/* Test URL : http://keats.kcl.ac.uk/mod/page/view.php?id=886138 */
		
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
		final HtmlPage Scrapingpage = (HtmlPage) form.getInputByValue("Log in").click();


		/* Gets first page content while Logging in */
		String htmlBody = Scrapingpage.getWebResponse().getContentAsString(); 



		/*   Gets cells containt in plantext */
		final HtmlPage Scrapedpage= (HtmlPage) webClient.getPage(webdata);

		String allScrapedPage = Scrapedpage.asText();

		if(allScrapedPage.contains("Last accessSort by Last access Descending")){
			Logs.loggedin = true;
			Logs.user = this.user;
			Logs.pass = this.pass;
		}



		BufferedReader bufferReader = new BufferedReader(new StringReader(allScrapedPage));
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


			
			String fullEmail =current.substring(startOfEmail, endOfEmail);
			String fullDate =current.substring(startOfDate,endOfDate);
			
			Logs.addToParticipationData(fullEmail, mname, fullDate);

		}


		webClient.closeAllWindows();
	}
	
	
	/**
	 * Gets the user's k-numb.
	 *
	 * @return the user
	 */
	public String getuser(){
		return user;
	}
	
	/**
	 * Gets the password.
	 *
	 * @return the pass
	 */
	public String getpass(){
		return pass;
	}

}