package edu.ualberta.data;

import java.util.ArrayList;

import org.apache.http.client.HttpClient;

import edu.ualberta.multimedia.MultimediaAbstract;
import edu.ualberta.utils.*;

import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

//Potential problems: Gson not default lib on my computer, included jar in libs directory, not sure 
//yet how this will pan out. 

public class WebClient implements DataManager{
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

	@Override
	public long insert_story(Story story) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long insert_page(Page page) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long insert_page_option(Page page, Page option) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long insert_multimedia(MultimediaAbstract mult, int page_id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<Story> get_stories_by_title(String search_title) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Story> get_stories_by_author(String search_author) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Story get_story_by_id(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Page> get_pages_by_title(String search_title) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Page> get_pages_by_author(String search_author) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page get_page_by_id(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Page> get_page_options(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<MultimediaAbstract> get_multimedia_by_page_id(
			Integer page_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long update_story(Story story) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long update_page(Page page) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long delete_story(Story story) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long delete_page(Page page) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long delete_page_option(Page page, Page child) {
		// TODO Auto-generated method stub
		return 0;
	}
}
