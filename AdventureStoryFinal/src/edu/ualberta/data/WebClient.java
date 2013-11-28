package edu.ualberta.data;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.ClientConfig;
import io.searchbox.core.Index;
import io.searchbox.core.Search;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import edu.ualberta.multimedia.MultimediaAbstract;
import edu.ualberta.multimedia.Picture;
import edu.ualberta.multimedia.SoundClip;
import edu.ualberta.multimedia.Video;
import edu.ualberta.utils.Page;
import edu.ualberta.utils.Story;

// https://github.com/CMPUT301F13T03/adventure.datetime

public class WebClient implements DataManager{
	private static final String CON_URL = "http://cmput301.softwareprocess.es:8080";
	private static final String _index = "cmput301f13t07";
    private static JestClient jestClient;
    private static String MASTER_QUERY = "{\n" +
            							 "  \"query\" : {\n" +
            							 "    \"match\" : {\n" +
            							 "      \"%s\" : {\n" +
            							 "        \"query\" : \"%s\"\n," +
            							 "        \"type\" : \"phrase_prefix\"\n" +
            							 "      }\n" +
            							 "    }\n" +
            							 "  }\n" +
            							 "}";
    private static String EMPTY_QUERY = "{\"query\":{\"term\":{\"_type\":\"%s\"}}}";
    
    public WebClient() {
		ClientConfig clientConfig = new ClientConfig.Builder(CON_URL).multiThreaded(true).build();
        JestClientFactory factory = new JestClientFactory();
        factory.setClientConfig(clientConfig);
        jestClient = factory.getObject();
    }
	
	/**
	 * Execute update queries such as insert, delete, update
	 * @param obj
	 * @param table
	 */
	public void es_execute(Object obj, String table) {
		Index index = new Index.Builder(obj)
		.index(_index)
		.type(table)
		.build();
		try {
			jestClient.execute(index);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	@Override
	public long insert_story(Story story) {
		ES_Story es_story = new ES_Story(story.getTitle(), story.getAuthor(), 
										 story.getID(), story.getRoot().getID());
		
		es_execute(es_story, Constant.TABLE_STORY);
		return 0;
	}

	@Override
	public long insert_page(Page page) {
		ES_Page es_page = new ES_Page(page.getTitle(), page.getAuthor(), page.getText(), 
									  page.getID());
		
		es_execute(es_page, Constant.TABLE_PAGE);
		return 0;
	}

	@Override
	public long insert_page_option(Page page, Page option) {
		ES_Option es_option = new ES_Option(page.getID(), option.getID());
		
		es_execute(es_option, Constant.TABLE_PAGE_CHILDREN);		
		return 0;
	}

	@Override
	public long insert_multimedia(MultimediaAbstract mult, int page_id) {
		ES_Mult es_mult = new ES_Mult(mult.getID(), mult.getFileDir(),
				  page_id, mult.getIndex(), mult.getClass().getSimpleName());
		es_execute(es_mult, Constant.TABLE_MULT);
		return 0;
	}

	/**
	 * Execute search queries
	 * @param query
	 * @param table
	 * @return
	 */
	private JestResult execute_query(String query, String table) {
		JestResult result = null;
		Search search = new Search.Builder(query)
	                .addIndex(_index)
	                .addType(table)
	                .build();
		try {
			result = jestClient.execute(search);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Converts search results into an array of Story items
	 * @param result
	 * @return
	 */
	private ArrayList<Story> result_to_story(JestResult result) {
		ArrayList<Story> all_stories = new ArrayList<Story>();
		List<ES_Story> articles = result.getSourceAsObjectList(ES_Story.class);
		System.out.println("matching number: " + articles.size());
		for (int i = 0; i < articles.size(); i++) {
			ES_Story es_story = articles.get(i);
			
			Page root = get_page_by_id(es_story.getRoot());
			
			Story story = articles.get(i).toStory(root);
			
			all_stories.add(story);
		}
		return all_stories;
	}
	
	/**
	 * Converts search results to Page items
	 * @param result
	 * @return
	 */
	private ArrayList<Page> result_to_page(JestResult result) {
		ArrayList<Page> all_pages = new ArrayList<Page>();
		
		List<ES_Page> articles = result.getSourceAsObjectList(ES_Page.class);
		for (int i = 0; i < articles.size(); i++) {
			Page page = articles.get(i).toPage();
			
			ArrayList<Page> options = new ArrayList<Page>();
			options = get_page_options(page.getID());
			
			System.out.println("options size: " + options.size());
			
			page.setPages(options);
			
			all_pages.add(page);
		}
		return all_pages;
	}
	
	/**
	 * Converts search results to a list of pages - for page children
	 * @param result
	 * @return
	 */
	private ArrayList<Page> result_to_page_children(JestResult result) {
		ArrayList<Page> all_pages = new ArrayList<Page>();
		
		List<ES_Option> articles = result.getSourceAsObjectList(ES_Option.class);
		
		System.out.println("size of children = " + articles.size());
		
		for (int i = 0; i < articles.size(); i++) {
			Page page = get_page_by_id(articles.get(i).getPage_id());
			
			all_pages.add(page);
		}
		return all_pages;
	}
	
	/**
	 * Converts search results to multimedia items
	 * @param result
	 * @return
	 */
	private ArrayList<MultimediaAbstract> result_to_mult(JestResult result) {
		ArrayList<MultimediaAbstract> multimedia = new ArrayList<MultimediaAbstract>();
		
		List<ES_Mult> articles = result.getSourceAsObjectList(ES_Mult.class);
		for (int i = 0; i < articles.size(); i++) {
			ES_Mult es_mult = articles.get(i);
			
			Integer mult_id = es_mult.getMult_id();
			String file_dir = es_mult.getDirectory();
			String type = es_mult.getMult_type();
			Integer index = es_mult.getIndex();
			
			if( type.compareTo("Picture") == 0 ){
			      multimedia.add( new Picture(mult_id, index, file_dir));
			}else if( type.compareTo("SoundClip") == 0){
			      multimedia.add( new SoundClip(mult_id, index, file_dir));
			}else if( type.compareTo("VideoClip") == 0){
			      multimedia.add( new Video(mult_id, index, file_dir));
			}else{
			      multimedia.add(new MultimediaAbstract(mult_id, index, file_dir){});
			}
		}
		return multimedia;
	}
	
	@Override
	public ArrayList<Story> get_stories_by_title(String search_title) {
		String query;
		System.out.println("search_title = " + search_title);
		if (search_title.isEmpty()){
			query = String.format(EMPTY_QUERY, Constant.TABLE_STORY);
		}
		else {
			query = String.format(MASTER_QUERY, Constant.STORY_TITLE, search_title);
		}
		System.out.println("query: " + query);
		JestResult result = execute_query(query, Constant.TABLE_STORY);
		System.out.println("after execute_query");
		return result_to_story(result);
	}

	@Override
	public ArrayList<Story> get_stories_by_author(String search_author) {
		String query;
		if (search_author.isEmpty()) {
			query = String.format(EMPTY_QUERY, Constant.TABLE_STORY);
		}
		else {
			query = String.format(MASTER_QUERY, Constant.STORY_AUTHOR, search_author);
		}
		JestResult result = execute_query(query, Constant.TABLE_STORY);
		return result_to_story(result);
	}

	@Override
	public Story get_story_by_id(Integer id) {
		String query = String.format(MASTER_QUERY, Constant.STORY_ID, id.toString());
		JestResult result = execute_query(query, Constant.TABLE_STORY);
		return result_to_story(result).get(0);
	}

	@Override
	public ArrayList<Page> get_pages_by_title(String search_title) {
		String query;
		if (search_title.isEmpty()) {
			query = String.format(EMPTY_QUERY, Constant.TABLE_PAGE);
		}
		else {
			query = String.format(MASTER_QUERY, Constant.PAGE_TITLE, search_title);
		}
		JestResult result = execute_query(query, Constant.TABLE_PAGE);
		return result_to_page(result);
	}

	@Override
	public ArrayList<Page> get_pages_by_author(String search_author) {
		String query;
		if (search_author.isEmpty()) {
			query = String.format(EMPTY_QUERY, Constant.TABLE_PAGE);
		}
		else {
			query = String.format(MASTER_QUERY, Constant.PAGE_AUTHOR, search_author);
		}
		JestResult result = execute_query(query, Constant.TABLE_PAGE);
		return result_to_page(result);
	}

	@Override
	public Page get_page_by_id(Integer id) {
		String query = String.format(MASTER_QUERY, Constant.PAGE_ID, id.toString());
		JestResult result = execute_query(query, Constant.TABLE_PAGE);
		return result_to_page(result).get(0);
	}

	@Override
	public ArrayList<Page> get_page_options(Integer id) {
		String query = String.format(MASTER_QUERY, Constant.PAGE_ID, id.toString());
		JestResult result = execute_query(query, Constant.TABLE_PAGE_CHILDREN);
		return result_to_page_children(result);
	}

	@Override
	public ArrayList<MultimediaAbstract> get_multimedia_by_page_id(
			Integer page_id) {
		String query = String.format(MASTER_QUERY, Constant.PAGE_ID, page_id.toString());
		JestResult result = execute_query(query, Constant.TABLE_MULT);
		
		return result_to_mult(result);
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

	@Override
	public long delete_mult(MultimediaAbstract mult, Page page) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int number_stories() {
		// TODO Auto-generated method stub
		return 0;
	}
	/**
	 * Checks whether the user is connected to the Internet or not.
	 * http://stackoverflow.com/questions/18258582/how-to-check-internet-connection-and-spawn-a-dialog-if-not-connected
	 * @param context
	 * @return
	 */
	public boolean isConnected(Context context) {

	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netinfo = cm.getActiveNetworkInfo();

	    if (netinfo != null && netinfo.isConnectedOrConnecting()) {
	        android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	        android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

	        if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) 
	        	return true;
	        else 
	        	return false;
	    } else
	        return false;
	}
	/**
	 * Displays a dialog if there is no Internet connection
	 * @param c
	 * @return
	 */
	public AlertDialog.Builder buildDialog(Context c) {

	    AlertDialog.Builder builder = new AlertDialog.Builder(c);
	    builder.setTitle("No Internet connection.");
	    builder.setMessage("You have no internet connection");

	    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

	        @Override
	        public void onClick(DialogInterface dialog, int which) {

	            dialog.dismiss();
	        }
	    });

	    return builder;
	}
}
