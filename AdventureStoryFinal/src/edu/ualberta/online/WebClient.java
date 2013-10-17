package edu.ualberta.online;

import org.apache.http.client.HttpClient;
import edu.ualberta.utils.*;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

//Potential problems: Gson not default lib on my computer, included jar in libs directory, not sure 
//yet how this will pan out. 

public class WebClient {
	private HttpClient httpclient = new DefaultHttpClient();
	private Gson gson = new Gson();
	
	//upload a story object to the webserver
	public void uploadStory(Story story) {
		
	}
	
	//If my interpretation of the requirements is correct, we may want separate download and search
	//download will take the story name or some other identifier, get it from the webserver and save to DB
	
	//TBD: Possible/Optimal Identifier methods
	public void downloadStory(String storyname) {
		
	}
	
	//search the webserver for stories and return a story object, without saving to DB
	public Story storySearch(String storyname) {
	
		return new Story(null, null, null);
	}

	//Will search for story with same name/other identifier (TBD) and replace it with argument
	public void updateStory(Story story) {
		
	}
	
	//Deletes story based on name/identifier
	public void deleteStory(String storyname) {
		
	}
}
