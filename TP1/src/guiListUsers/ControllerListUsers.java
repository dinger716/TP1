package guiListUsers;

/*******
 * <p> Title: ControllerListUsers Class. </p>
 * 
 * <p> Description: Controller for the List Users page.</p>
 * 
 */

public class ControllerListUsers {
	
	public ControllerListUsers() {
	}
	
	/**********
	 * <p> Method: performReturn() </p>
	 * 
	 * <p> Description: Method that returns the user to the Admin Home page.</p>
	 */
	protected static void performReturn() {
		guiAdminHome.ViewAdminHome.displayAdminHome(ViewListUsers.theStage, ViewListUsers.theUser);
	}
	
	/**********
	 * <p> Method: performLogout() </p>
	 * 
	 * <p> Description: Method that logs the user out and returns them to the login page.</p>
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewListUsers.theStage);
	}
	
	/**********
	 * <p> Method: performQuit() </p>
	 * 
	 * <p> Description: Method that exits the application.</p>
	 */
	protected static void performQuit() {
		System.exit(0);
	}
}