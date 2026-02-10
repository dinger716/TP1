package guiDeleteUser;

import database.Database;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;

/*******
 * <p> Title: ControllerAddRemoveRoles Class. </p>
 * 
 * <p> Description: The Java/FX-based Add Remove Roles Page.  This class provides the controller
 * actions basic on the user's use of the JavaFX GUI widgets defined by the View class.
 * 
 * This page has one of the more complex Controller Classes due to the fact that the changing the
 * values of widgets changes the layout of the page.  It is up to the Controller to determine what
 * to do and it involves the proper elements from View Class for this GUI page.
 * 
 * The class has been written assuming that the View or the Model are the only class methods that
 * can invoke these methods.  This is why each has been declared at "protected".  Do not change any
 * of these methods to public.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-17 Initial version
 * @version 1.01		2025-09-16 Update Javadoc documentation *  
 */

public class ControllerDeleteUser {
	
	/*-********************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	 */

	/**
	 * Default constructor is not used.
	 */
	public ControllerDeleteUser () {
	}
	
	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;		

	/**********
	 * <p> Method: doSelectUser() </p>
	 * 
	 * <p> Description: This method uses the ComboBox widget, fetches which item in the ComboBox
	 * was selected (a user in this case), and establishes that user and the current user, setting
	 * easily accessible values without needing to do a query. </p>
	 * 
	 */
	protected static void doSelectUser() {
		ViewDeleteUser.theSelectedUser = 
				(String) ViewDeleteUser.combobox_SelectUser.getValue();
		if (ViewDeleteUser.theSelectedUser == null || ViewDeleteUser.theSelectedUser.equals("<Select s User>")) {
			return;
		}
	}
	
	
	/**********
	 * <p> Method: repaintTheWindow() </p>
	 * 
	 * <p> Description: This method determines the current state of the window and then establishes
	 * the appropriate list of widgets in the Pane to show the proper set of current values. </p>
	 * 
	 */
	protected static void repaintTheWindow() {
		// Clear what had been displayed
		ViewDeleteUser.theRootPane.getChildren().clear();
		
		// Determine which of the two views to show to the user
		if (ViewDeleteUser.theSelectedUser.compareTo("<Select a User>") == 0) {
			// Only show the request to select a user to be updated and the ComboBox
			ViewDeleteUser.theRootPane.getChildren().addAll(
					ViewDeleteUser.label_PageTitle, ViewDeleteUser.label_UserDetails, 
					ViewDeleteUser.button_UpdateThisUser, ViewDeleteUser.line_Separator1,
					ViewDeleteUser.label_SelectUser, ViewDeleteUser.combobox_SelectUser, 
					ViewDeleteUser.line_Separator4, ViewDeleteUser.button_Return,
					ViewDeleteUser.button_Logout, ViewDeleteUser.button_Quit);
		}
		else {
			// Show all the fields as there is a selected user (as opposed to the prompt)
			ViewDeleteUser.theRootPane.getChildren().addAll(
					ViewDeleteUser.label_PageTitle, ViewDeleteUser.label_UserDetails,
					ViewDeleteUser.button_UpdateThisUser, ViewDeleteUser.line_Separator1,
					ViewDeleteUser.label_SelectUser,
					ViewDeleteUser.combobox_SelectUser, 
					ViewDeleteUser.label_SelectRoleToBeAdded,
					ViewDeleteUser.button_AddRole,
					ViewDeleteUser.line_Separator4, 
					ViewDeleteUser.button_Return,
					ViewDeleteUser.button_Logout,
					ViewDeleteUser.button_Quit);
		}
		
		// Add the list of widgets to the stage and show it
		
		// Set the title for the window
		ViewDeleteUser.theStage.setTitle("CSE 360 Foundation Code: Admin Opertaions Page");
		ViewDeleteUser.theStage.setScene(ViewDeleteUser.theAddRemoveRolesScene);
		ViewDeleteUser.theStage.show();
	}
	
	
	/**********
	 * <p> Method: setupSelectedUser() </p>
	 * 
	 * <p> Description: This method fetches the current values for the widgets whose values change
	 * based on which user has been selected and any actions that the admin takes. </p>
	 * 
	 */

	
	protected static void userDeletion() {
		System.out.println("*** Entering userDeletion");
		
		String user = ViewDeleteUser.theSelectedUser;
		
		if (user == null || user.equals("<Select a User>")) {
			System.out.println("*** not valid choice");
			return;
		}
		
		boolean deleted = theDatabase.deleteUser(user);
		
		if (deleted) {
			System.out.println("*** delete performed");
			ViewDeleteUser.combobox_SelectUser.setItems(FXCollections.observableArrayList(theDatabase.getUserList()));
			ViewDeleteUser.combobox_SelectUser.getSelectionModel().select(0);
			
			ViewDeleteUser.theSelectedUser = "<Select a User>";
			
			repaintTheWindow();
		}
	}
	
	/**********
	 * <p> Method: performAddRole() </p>
	 * 
	 * <p> Description: This method adds a new role to the list of role in the ComboBox select
	 * list. </p>
	 * 
	 */
	/*
	
	protected static void performAddRole() {
		
		// Determine which item in the ComboBox list was selected
		ViewDeleteUser.theAddRole =
				(String) ViewDeleteUser.combobox_SelectRoleToAdd.getValue();
		
		// If the selection is the list header (e.g., "<Select a role>") don't do anything
		if (ViewDeleteUser.theAddRole.compareTo("<Select a role>") != 0) {
			
			// If an actual role was selected, update the database entry for that user for the role
			if (theDatabase.updateUserRole(ViewDeleteUser.theSelectedUser,
					ViewDeleteUser.theAddRole, "true") ) {
				ViewDeleteUser.combobox_SelectRoleToAdd = new ComboBox <String>();
				ViewDeleteUser.combobox_SelectRoleToAdd.setItems(FXCollections.
					observableArrayList(ViewDeleteUser.addList));
				ViewDeleteUser.combobox_SelectRoleToAdd.getSelectionModel().clearAndSelect(0);		
				confirmDeletion();
			}
		}
	}
	*/
	
	/**********
	 * <p> Method: performRemoveRole() </p>
	 * 
	 * <p> Description: This method removes an existing role to the list of role in the ComboBox
	 * select list. </p>
	 * 
	 */
/*
	protected static void performRemoveRole() {
		
		// Determine which item in the ComboBox list was selected
		ViewDeleteUser.theRemoveRole = (String) ViewDeleteUser.
				combobox_SelectRoleToRemove.getValue();
		
		// If the selection is the list header (e.g., "<Select a role>") don't do anything
		if (ViewDeleteUser.theRemoveRole.compareTo("<Select a role>") != 0) {
			
			// If an actual role was selected, update the database entry for that user for the role
			if (theDatabase.updateUserRole(ViewDeleteUser.theSelectedUser, 
					ViewDeleteUser.theRemoveRole, "false") ) {
				ViewDeleteUser.combobox_SelectRoleToRemove = new ComboBox <String>();
				ViewDeleteUser.combobox_SelectRoleToRemove.setItems(FXCollections.
					observableArrayList(ViewDeleteUser.addList));
				ViewDeleteUser.combobox_SelectRoleToRemove.getSelectionModel().
					clearAndSelect(0);		
				confirmDeletion();
			}				
		}
	}
*/	
	
	/**********
	 * <p> Method: performReturn() </p>
	 * 
	 * <p> Description: This method returns the user (who must be an Admin as only admins are the
	 * only users who have access to this page) to the Admin Home page. </p>
	 * 
	 */
	protected static void performReturn() {
		guiAdminHome.ViewAdminHome.displayAdminHome(ViewDeleteUser.theStage,
				ViewDeleteUser.theUser);
	}
	
	
	/**********
	 * <p> Method: performLogout() </p>
	 * 
	 * <p> Description: This method logs out the current user and proceeds to the normal login
	 * page where existing users can log in or potential new users with a invitation code can
	 * start the process of setting up an account. </p>
	 * 
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewDeleteUser.theStage);
	}
	
	
	/**********
	 * <p> Method: performQuit() </p>
	 * 
	 * <p> Description: This method terminates the execution of the program.  It leaves the
	 * database in a state where the normal login page will be displayed when the application is
	 * restarted.</p>
	 * 
	 */
	protected static void performQuit() {
		System.exit(0);
	}
}