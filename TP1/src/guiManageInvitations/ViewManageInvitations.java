package guiManageInvitations;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import java.util.Optional;

/*******
 * <p> Title: ViewManageInvitations Class. </p>
 * 
 * <p> Description: The JavaFX-based Manage Invitations Page. This page displays all pending
 * invitation codes in a table.</p>
 *
 */

public class ViewManageInvitations {
	
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	// Page title and user info
	protected static Label label_PageTitle = new Label();
	protected static Label label_UserDetails = new Label();
	protected static Button button_UpdateThisUser = new Button("Account Update");
	
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);
	
	// Table to display invitations
	protected static TableView<InvitationDisplay> table_Invitations = new TableView<>();
	protected static ObservableList<InvitationDisplay> invitationData = FXCollections.observableArrayList();
	
	protected static Line line_Separator4 = new Line(20, 480, width-20, 480);
	
	// Action buttons
	protected static Button button_DeleteInvitation = new Button("Delete Selected Invitation");
	
	protected static Line line_Separator5 = new Line(20, 525, width-20, 525);
	
	// Navigation buttons
	protected static Button button_Return = new Button("Return");
	protected static Button button_Logout = new Button("Logout");
	protected static Button button_Quit = new Button("Quit");

	private static ViewManageInvitations theView;
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	protected static Stage theStage;
	private static Pane theRootPane;
	protected static User theUser;

	private static Scene theManageInvitationsScene = null;

	public static void displayManageInvitations(Stage ps, User user) {
		theStage = ps;
		theUser = user;
		
		if (theView == null) theView = new ViewManageInvitations();
		
		populateInvitationTable();
		
		label_UserDetails.setText("User: " + theUser.getUserName());
		
		theStage.setTitle("CSE 360 Foundation Code: Manage Invitations");
		theStage.setScene(theManageInvitationsScene);
		theStage.show();
	}
	
	private ViewManageInvitations() {
		theRootPane = new Pane();
		theManageInvitationsScene = new Scene(theRootPane, width, height);
		
		// Page Title
		label_PageTitle.setText("Manage Invitations");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("User: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
		
		setupButtonUI(button_UpdateThisUser, "Dialog", 18, 170, Pos.CENTER, 610, 45);
		button_UpdateThisUser.setOnAction((_) -> 
			{guiUserUpdate.ViewUserUpdate.displayUserUpdate(theStage, theUser); });
		
		// Setup TableView
		setupTableView();
		
		// Action buttons
		setupButtonUI(button_DeleteInvitation, "Dialog", 18, 250, Pos.CENTER, 20, 495);
		button_DeleteInvitation.setOnAction((_) -> {ControllerManageInvitations.performDeleteInvitation(); });
		
		// Navigation buttons
		setupButtonUI(button_Return, "Dialog", 18, 210, Pos.CENTER, 20, 540);
		button_Return.setOnAction((_) -> {ControllerManageInvitations.performReturn(); });

		setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 300, 540);
		button_Logout.setOnAction((_) -> {ControllerManageInvitations.performLogout(); });
    
		setupButtonUI(button_Quit, "Dialog", 18, 210, Pos.CENTER, 570, 540);
		button_Quit.setOnAction((_) -> {ControllerManageInvitations.performQuit(); });
		
		theRootPane.getChildren().addAll(
			label_PageTitle, label_UserDetails, button_UpdateThisUser, line_Separator1,
			table_Invitations, line_Separator4,
			button_DeleteInvitation, line_Separator5,
			button_Return, button_Logout, button_Quit);
	}

	private void setupTableView() {
		TableColumn<InvitationDisplay, String> codeCol = new TableColumn<>("Invitation Code");
		codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
		codeCol.setPrefWidth(150);
		
		TableColumn<InvitationDisplay, String> emailCol = new TableColumn<>("Email Address");
		emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
		emailCol.setPrefWidth(220);
		
		TableColumn<InvitationDisplay, String> roleCol = new TableColumn<>("Role");
		roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
		roleCol.setPrefWidth(150);
		
		TableColumn<InvitationDisplay, String> expiresCol = new TableColumn<>("Expires");  // <-- NEW
	    expiresCol.setCellValueFactory(new PropertyValueFactory<>("expiration"));
	    expiresCol.setPrefWidth(240);
		
		table_Invitations.getColumns().addAll(codeCol, emailCol, roleCol, expiresCol);
		table_Invitations.setItems(invitationData);
		
		table_Invitations.setLayoutX(20);
		table_Invitations.setLayoutY(110);
		table_Invitations.setPrefWidth(760);
		table_Invitations.setPrefHeight(355);
	}
	
	private static void populateInvitationTable() {
		invitationData.clear();
		List<String[]> invitations = theDatabase.getAllInvitations();
		
		for (String[] invitation : invitations) {
			String code = invitation[0];
			String email = invitation[1];
			String role = invitation[2];
			String expiration = invitation[3];
			
			// Converts role to readable format
			String roleDisplay;
			if (role.equalsIgnoreCase("admin")) {
				roleDisplay = "Admin";
			} else if (role.equalsIgnoreCase("role1")) {
				roleDisplay = "Student";
			} else if (role.equalsIgnoreCase("role2")) {
				roleDisplay = "Staff";
			} else {
				roleDisplay = role;
			}
			
			invitationData.add(new InvitationDisplay(code, email, roleDisplay, expiration));
		}
	}
	
	protected static void showAlert(String title, String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	protected static boolean showConfirmation(String title, String message) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		
		Optional<ButtonType> result = alert.showAndWait();
		return result.isPresent() && result.get() == ButtonType.OK;
	}

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
	 * <p> Inner Class: InvitationDisplay </p>
	 * 
	 * <p> Description: Data class that displays the table with the invitations.</p>
	 */
	public static class InvitationDisplay {
		private final javafx.beans.property.SimpleStringProperty code;
		private final javafx.beans.property.SimpleStringProperty email;
		private final javafx.beans.property.SimpleStringProperty role;
		private final javafx.beans.property.SimpleStringProperty expiration;
		
		public InvitationDisplay(String code, String email, String role, String expiration) {
			this.code = new javafx.beans.property.SimpleStringProperty(code);
			this.email = new javafx.beans.property.SimpleStringProperty(email);
			this.role = new javafx.beans.property.SimpleStringProperty(role);
			this.expiration = new javafx.beans.property.SimpleStringProperty(expiration);
		}
		
		public String getCode() { return code.get(); }
		public String getEmail() { return email.get(); }
		public String getRole() { return role.get(); }
		public String getExpiration() { return expiration.get(); }
	}
}