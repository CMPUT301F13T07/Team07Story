/**
 * Allows someone to log in as an author, or register if they aren't already
 * @author: Lyle Rolleman
 */
package edu.ualberta.adventstory;

import edu.ualberta.data.DbManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
	
	private DbManager database;
	private TextView lstat;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		database = ((DataSingleton)this.getApplication()).database;
	}
	protected void onResume() {
		super.onResume();
		login();
	}
	
	/**
	 * Gets user input for login, checks against DB and continues to StartActivity.
	 */
	private void login() {
		setContentView(R.layout.login);
		lstat = (TextView) findViewById(R.id.lstatus);
		final EditText author = (EditText) findViewById(R.id.authorinput);
		
		Button login = (Button) findViewById(R.id.loginb);
		Button register = (Button) findViewById(R.id.registerb);
		Button cont = (Button) findViewById(R.id.nologin);
		
		login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String input = author.getText().toString();
				String user = database.get_user(input);
				if (!user.equals("")) {
					lstat.setText("");
					startAct(user);
				} else {
					lstat.setText("That user is not registered");
				}
					
			}
		});
		register.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				register();
			}
		});
		cont.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startAct(null);
			}
		});
	}
	
	/**
	 * Allows a user to register as author, continues to StartActivity once registered. 
	 */
	private void register() {
		setContentView(R.layout.register);
		
		final EditText reg = (EditText) findViewById(R.id.registerinput);
		Button regb = (Button) findViewById(R.id.compreg);
		
		regb.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String input = reg.getText().toString();
				database.insert_user(input);
				startAct(input);
			}
		});
	}
	
	/**
	 * starts StartActivity after setting author in DataSingleton (can be null if the user
	 * opted to continue with logging in
	 * @param a
	 */
	private void startAct(String a) {
		Intent conti = new Intent(this, StartActivity.class);
		((DataSingleton)this.getApplication()).author = a;
		startActivity(conti);
	}
}
