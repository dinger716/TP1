package guiListUsers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import database.Database;
import entityClasses.User;

import java.util.List;

/*******
 * <p> Title: ViewListUsers Class. </p>
 * 
 * <p> Description: The Java/FX-based List Users Page. This page displays all user accounts
 * in a table showing username, name, email, and roles.</p>
 *
 */

public class ViewListUsers {
	
	/*-*******************************************************************************************
	Attributes
	*/
	
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	// Page title and user info
	protected static Label label_PageTitle = new Label();
	protected static Label label_UserDetails = new Label();
	protected static Button button_UpdateThisUser = new Button("Account Update");
	
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);
	
	// Table to display users
	protected static TableView<UserDisplay> table_Users = new TableView<>();
	protected static ObservableList<UserDisplay> userData = FXCollections.observableArrayList();
	
	protected static Line line_Separator4 = new Line(20, 525, width-20, 525);
	
	// Navigation buttons
	protected static Button button_Return = new Button("Return");
	protected static Button button_Logout = new Button("Logout");
	protected static Button button_Quit = new Button("Quit");

	private static ViewListUsers theView;
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	protected static Stage theStage;
	private static Pane theRootPane;
	protected static User theUser;

	private static Scene theListUsersScene = null;

	/*-*******************************************************************************************
	Constructors
	*/

	/**********
	 * <p> Method: displayListUsers(Stage ps, User user) </p>
	 * 
	 * <p> Description: Displays all the users in the database.</p>
	 * 
	 */
	public static void displayListUsers(Stage ps, User user) {
	    theStage = ps;
	    theUser = user;
	    
	    if (theView == null) theView = new ViewListUsers();
	    
	    // This ensures the table gets populated every time the page is displayed
	    populateUserTable();
	    
	    label_UserDetails.setText("User: " + theUser.getUserName());
	    
	    theStage.setTitle("CSE 360 Foundation Code: List All Users");
	    theStage.setScene(theListUsersScene);
	    theStage.show();
	}
	
	/**********
	 * <p> Method: ViewListUsers() </p>
	 * 
	 * <p> Description: Constructor that initializes the GUI.</p>
	 */
	private ViewListUsers() {
		theRootPane = new Pane();
		theListUsersScene = new Scene(theRootPane, width, height);
		
		// Page Title
		label_PageTitle.setText("List All Users");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("User: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
		
		setupButtonUI(button_UpdateThisUser, "Dialog", 18, 170, Pos.CENTER, 610, 45);
		button_UpdateThisUser.setOnAction((_) -> 
			{guiUserUpdate.ViewUserUpdate.displayUserUpdate(theStage, theUser); });
		
		// Setup TableView
		setupTableView();
		
		// Navigation Buttons
		setupButtonUI(button_Return, "Dialog", 18, 210, Pos.CENTER, 20, 540);
		button_Return.setOnAction((_) -> {ControllerListUsers.performReturn(); });

		setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 300, 540);
		button_Logout.setOnAction((_) -> {ControllerListUsers.performLogout(); });
    
		setupButtonUI(button_Quit, "Dialog", 18, 210, Pos.CENTER, 570, 540);
		button_Quit.setOnAction((_) -> {ControllerListUsers.performQuit(); });
		
		theRootPane.getChildren().addAll(
			label_PageTitle, label_UserDetails, button_UpdateThisUser, line_Separator1,
			table_Users, line_Separator4, 
			button_Return, button_Logout, button_Quit);
	}

	/**********
	 * <p> Method: setupTableView() </p>
	 * 
	 * <p> Description: Configures the TableView columns and the properties.</p>
	 */

	private void setupTableView() {
		// Creates columns
		TableColumn<UserDisplay, String> usernameCol = new TableColumn<>("Username");
		usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
		usernameCol.setPrefWidth(120);
		
		TableColumn<UserDisplay, String> nameCol = new TableColumn<>("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
		nameCol.setPrefWidth(200);
		
		TableColumn<UserDisplay, String> emailCol = new TableColumn<>("Email");
		emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
		emailCol.setPrefWidth(220);
		
		TableColumn<UserDisplay, String> rolesCol = new TableColumn<>("Roles");
		rolesCol.setCellValueFactory(new PropertyValueFactory<>("roles"));
		rolesCol.setPrefWidth(200);
		
		table_Users.getColumns().addAll(usernameCol, nameCol, emailCol, rolesCol);
		table_Users.setItems(userData);
		
		table_Users.setLayoutX(20);
		table_Users.setLayoutY(110);
		table_Users.setPrefWidth(760);
		table_Users.setPrefHeight(400);
	}
	
	/**********
	 * <p> Method: populateUserTable() </p>
	 * 
	 * <p> Description: Fetchs all the users from database and populates the table.</p>
	 */
	private static void populateUserTable() {
	    userData.clear();
	    List<User> users = theDatabase.getAllUsers();
	    
	    for (User user : users) {
	        System.out.println("*** DEBUG: Processing user: " + user.getUserName());
	        
	        // Builds full name
	        String fullName = buildFullName(user);
	        
	        // Builds roles string
	        String roles = buildRolesString(user);
	        
	        // Gets email
	        String email = user.getEmailAddress();
	        if (email == null || email.isEmpty()) email = "<none>";	        
	        
	        userData.add(new UserDisplay(user.getUserName(), fullName, email, roles));
	    }
	}
	
	/**********
	 * <p> Method: buildFullName(User user) </p>
	 * 
	 * <p> Description: Builds the formatted full name from user's name fields.</p>
	 */
	private static String buildFullName(User user) {
		StringBuilder name = new StringBuilder();
		
		String firstName = user.getFirstName();
		String middleName = user.getMiddleName();
		String lastName = user.getLastName();
		
		if (firstName != null && !firstName.isEmpty()) {
			name.append(firstName);
		}
		
		if (middleName != null && !middleName.isEmpty()) {
			if (name.length() > 0) name.append(" ");
			name.append(middleName);
		}
		
		if (lastName != null && !lastName.isEmpty()) {
			if (name.length() > 0) name.append(" ");
			name.append(lastName);
		}
		
		return name.length() > 0 ? name.toString() : "<none>";
	}
	
	/**********
	 * <p> Method: buildRolesString(User user) </p>
	 * 
	 * <p> Description: Builds a string of all the user's roles.</p>
	 */
	private static String buildRolesString(User user) {
		StringBuilder roles = new StringBuilder();
		
		if (user.getAdminRole()) {
			roles.append("Admin");
		}
		
		if (user.getNewRole1()) {
			if (roles.length() > 0) roles.append(", ");
			roles.append("Student");
		}
		
		if (user.getNewRole2()) {
			if (roles.length() > 0) roles.append(", ");
			roles.append("Staff");
		}
		
		return roles.length() > 0 ? roles.toString() : "<none>";
	}

	/*-*******************************************************************************************
	Helper methods
	*/

	private void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);		
	}
	
	private void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);		
	}
	
	/**********
	 * <p> Inner Class: UserDisplay </p>
	 * 
	 * <p> Description: Simple data class for TableView display.</p>
	 */
	public static class UserDisplay {
	    private final javafx.beans.property.SimpleStringProperty username;
	    private final javafx.beans.property.SimpleStringProperty fullName;
	    private final javafx.beans.property.SimpleStringProperty email;
	    private final javafx.beans.property.SimpleStringProperty roles;
	    
	    public UserDisplay(String username, String fullName, String email, String roles) {
	        this.username = new javafx.beans.property.SimpleStringProperty(username);
	        this.fullName = new javafx.beans.property.SimpleStringProperty(fullName);
	        this.email = new javafx.beans.property.SimpleStringProperty(email);
	        this.roles = new javafx.beans.property.SimpleStringProperty(roles);
	    }
	    
	    public String getUsername() { return username.get(); }
	    public String getFullName() { return fullName.get(); }
	    public String getEmail() { return email.get(); }
	    public String getRoles() { return roles.get(); }
	    
	    public javafx.beans.property.SimpleStringProperty usernameProperty() { return username; }
	    public javafx.beans.property.SimpleStringProperty fullNameProperty() { return fullName; }
	    public javafx.beans.property.SimpleStringProperty emailProperty() { return email; }
	    public javafx.beans.property.SimpleStringProperty rolesProperty() { return roles; }
	}
}