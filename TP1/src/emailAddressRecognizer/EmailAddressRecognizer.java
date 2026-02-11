package emailAddressRecognizer;


public class EmailAddressRecognizer {
	/**
	 * <p> Title: FSM-translated EmailAddressRecognizer. </p>
	 * 
	 * <p> Description: A demonstration of the mechanical translation of Finite State Machine 
	 * diagram into an executable Java program using the Email Address Recognizer. The code 
	 * detailed design is based on a while loop with a select list</p>
	 * 
	 * <p> Copyright: Lynn Robert Carter © 2022 </p>
	 * 
	 * @author Lynn Robert Carter
	 * 
	 * @version 0.00		2018-02-04	Initial baseline 
	 * @version 2.00		2022-01-06	Rewritten to recognize email addresses and enhanced
	 * 										to support FSM with up through 999 states for the 
	 * 										trace output to align nicely
	 * @version 3.00		2022-03-22	Adjusted to clean up the code and resolving alignment
	 * 										issues with the design and to correct the issue
	 * 										with an empty email address
	 * 
	 */

	/**********************************************************************************************
	 * 
	 * Result attributes to be used for GUI applications where a detailed error message and a 
	 * pointer to the character of the error will enhance the user experience.
	 * 
	 */

	public static String emailAddressErrorMessage = "";	// The error message text
	public static String emailAddressInput = "";		// The input being processed
	public static int emailAddressIndexofError = -1;	// The index where the error was located
	private static int state = 0;						// The current state value
	private static int nextState = 0;					// The next state value
	private static boolean finalState = false;			// Is this state a final state?
	private static String inputLine = "";				// The input line
	private static char currentChar;					// The current character in the line
	private static int currentCharNdx;					// The index of the current character
	private static boolean running;						// The flag that specifies if the FSM is 
														// running
	private static int domainPartCounter = 0;			// A domain name may not exceed 63 characters

	/**********
	 * This private method display the input line and then on a line under it displays an up arrow
	 * at the point where an error should one be detected.  This method is designed to be used to 
	 * display the error message on the console terminal.
	 * 
	 * @param input				The input string
	 * @param currentCharNdx	The location where an error was found
	 * @return					Two lines, the entire input line followed by a line with an up arrow
	 */
	private static String displayInput(String input, int currentCharNdx) {
		// Display the entire input line
		String result = input.substring(0,currentCharNdx) + "?\n";

		return result;
	}


	private static void displayDebuggingInfo() {
		// Display the current state of the FSM as part of an execution trace
		if (currentCharNdx >= inputLine.length())
			// display the line with the current state numbers aligned
			System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state + 
					((finalState) ? "       F   " : "           ") + "None");
		else
			System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state + 
					((finalState) ? "       F   " : "           ") + "  " + currentChar + " " + 
					((nextState > 99) ? "" : (nextState > 9) || (nextState == -1) ? "   " : "    ") + 
					nextState + "     " + domainPartCounter);
	}

	private static void moveToNextCharacter() {
		currentCharNdx++;
		if (currentCharNdx < inputLine.length())
			currentChar = inputLine.charAt(currentCharNdx);
		else {
			currentChar = ' ';
			running = false;
		}
	}

	/**********
	 * This method is a mechanical transformation of a Finite State Machine diagram into a Java
	 * method.
	 * 
	 * @param input		The input string for the Finite State Machine
	 * @return			An output string that is empty if every things is okay or it will be
	 * 						a string with a help description of the error follow by two lines
	 * 						that shows the input line follow by a line with an up arrow at the
	 *						point where the error was found.
	 */
	public static String checkEmailAddress(String input) {
		// The following are the local variable used to perform the Finite State Machine simulation
		state = 0;							// This is the FSM state number
		inputLine = input;					// Save the reference to the input line as a global
		currentCharNdx = 0;					// The index of the current character

		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state

		emailAddressInput = input;			// Save a copy of the input

		// Let's ensure there is input
		if (input.length() <= 0) {
			emailAddressErrorMessage = "There was no email address found.\n";
			return emailAddressErrorMessage;
		}
		currentChar = input.charAt(0);		// The current character from the above indexed position

		// Let's ensure the address is not too long
		if (input.length() > 255) {
			emailAddressErrorMessage = "A valid email address must be no more than 255 characters.\n";
			return emailAddressErrorMessage;
		}
		running = true;						// Start the loop
		domainPartCounter = 0;              // ***ADDED*** Initialize the domain part counter
		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state
		while (running) {
			// The switch statement takes the execution to the code for the current state, where
			// that code sees whether or not the current character is valid to transition to a
			// next state
			nextState = -1;						// Default to there is no next state		
			
			switch (state) {
			case 0: 
				// State 0 has just 1 valid transition.
				// The current character is must be checked against 62 options. If any are matched
				// the FSM must go to state 1
				// The first and the second check for an alphabet character the third a numeric
				if ((currentChar >= 'A' && currentChar <= 'Z')|| 		// Upper case
						(currentChar >= 'a' && currentChar <= 'z') ||	// Lower case
						(currentChar >= '0' && currentChar <= '9')) {	// Digit
					nextState = 1;
				}
								
				// If it is none of those characters, the FSM halts
				else { 
					running = false;
				}
				
				break;				
				// The execution of this state is finished
			
			case 1: 
				// State 1 has three valid transitions.
				// *** ADDED *** 
				// 1: A-Z, a-z, 0-9 that transitions back to state 1
                // 2: a . that transitions to state 0
                //  3: an @ that transitions to state 2
					
				// A-Z, a-z, 0-9 -> State 1
                if ((currentChar >= 'A' && currentChar <= 'Z') ||        //Upper case
                        (currentChar >= 'a' && currentChar <= 'z') ||    //Lower case
                        (currentChar >= '0' && currentChar <= '9')) {    //Digit
                    nextState = 1;
                }
                // . -> State 0
                else if (currentChar == '.') {
                    nextState = 0;
                }
                // @ -> State 2
                else if (currentChar == '@') {
                    nextState = 2;
                }
                // If it is none of those characters, the FSM halts
                else {
                    running = false;
                }

                break;
                // The execution of this state is finished


				
							
			case 2: 
				// State 2 has one valid transition.
				
				// *** ADDED *** 
                // A-Z, a-z, 0-9 -> State 3

                if ((currentChar >= 'A' && currentChar <= 'Z') ||        //Upper case
                        (currentChar >= 'a' && currentChar <= 'z') ||    //Lower case
                        (currentChar >= '0' && currentChar <= '9')) {    //Digit
                    nextState = 3;

                    // Count the domain part character
                    domainPartCounter++;
                }
                // If it is none of those characters, the FSM halts
                else {
                    running = false;
                }

				

				// The execution of this state is finished
				break;
	
			case 3:
				// State 3 has three valid transition.
				
				// *** ADDED *** 
                // A-Z, a-z, 0-9 -> State 3
                if ((currentChar >= 'A' && currentChar <= 'Z') ||        //Upper case
                        (currentChar >= 'a' && currentChar <= 'z') ||    //Lower case
                        (currentChar >= '0' && currentChar <= '9')) {    //Digit
                    nextState = 3;

                    // Count the domain part character
                    domainPartCounter++;
                }
                // . -> State 2
                else if (currentChar == '.') {
                    nextState = 2;

                    // Reset the domain part counter for the new label
                    domainPartCounter = 0;
                }
                // - -> State 4
                else if (currentChar == '-') {
                    nextState = 4;
                }
                // If it is none of those characters, the FSM halts
                else {
                    running = false;
                }

                // If the domain part counter is larger than 63, the loop must stop
                if (domainPartCounter > 63)
                    running = false;

				

				// The execution of this state is finished
				break;

			case 4: 
				// State 4 has one valid transition.

				// *** ADDED ***
                // A-Z, a-z, 0-9 -> State 3

                if ((currentChar >= 'A' && currentChar <= 'Z') ||        //Upper case
                        (currentChar >= 'a' && currentChar <= 'z') ||    //Lower case
                        (currentChar >= '0' && currentChar <= '9')) {    //Digit
                    nextState = 3;

                    // Count the domain part character
                    domainPartCounter++;
                }
                // If it is none of those characters, the FSM halts
                else {
                    running = false;
                }

                // If the domain part counter is larger than 63, the loop must stop
                if (domainPartCounter > 63)
                    running = false;


				// The execution of this state is finished
				break;

			}

			if (running) {
				// When the processing of a state has finished, the FSM proceeds to the next character
				// in the input and if there is one, it fetches that character and updates the 
				// currentChar.  If there is no next character the currentChar is set to a blank.
				
				moveToNextCharacter();
				
				// Move to the next state
				state = nextState;
				
				// *** ADDED ***
				//Is the new state a final state? If so, signal this fact.
	            if (state == 3) finalState = true; 
	            else finalState = false;

				nextState = -1;
				
				
			}
			// Should the FSM get here, the loop starts again

		}
		emailAddressIndexofError = currentCharNdx;		// Copy the index of the current character;
		
		// When the FSM halts, we must determine if the situation is an error or not.  That depends
		// of the current state of the FSM and whether or not the whole string has been consumed.
		// This switch directs the execution to separate code for each of the FSM states and that
		// makes it possible for this code to display a very specific error message to improve the
		// user experience.
		switch (state) {
		case 0:
			// State 0 is not a final state, so we can return a very specific error message
			emailAddressIndexofError = currentCharNdx;		// Copy the index of the current character;
			// Update error 
			// message emailAddressErrorMessage = "May only be alphanumeric.\n";
			
			//*** ADDED ***
			
			if (currentCharNdx == 0) {
		          emailAddressErrorMessage = "An Email Address must start with A-Z, a-z, 0-9.\n";
		    } else {
		          emailAddressErrorMessage = "An Email Address character after a @, ., - must be A-Z, a-z, 0-9.\n";
		    }
		    return emailAddressErrorMessage;

			

		case 1:
			// State 1 is not a final state, so we can return a very specific error message
			
			// *** ADDED ***
            emailAddressIndexofError = currentCharNdx;        // Copy the index of the current character;
            if (currentCharNdx >= input.length()) {
                  // The input ended without an @ sign
                  emailAddressErrorMessage = "An Email Address must contain an @.\n";
              }
            else {
                  // An invalid character was found
                  emailAddressErrorMessage = "Before the @, an Email Address may only contain A-Z, a-z, 0-9, or .\n";
            }
            return emailAddressErrorMessage;



		case 2:
			// State 2 is not a final state, so we can return a very specific error message
			
			// *** ADDED *** 
			// State 2 is not a final state, so we can return a very specific error message
              emailAddressIndexofError = currentCharNdx;        // Copy the index of the current character;
              emailAddressErrorMessage = "An Email Address character after a @, ., - must be A-Z, a-z, 0-9.\n";
              return emailAddressErrorMessage;


		case 3:
			// State 3 is a Final State, so this is not an error if the input is empty, otherwise
			// we can return a very specific error message.
			
            // *** ADDED *** 
			// Check for domain part counter exceeding 63
            if (domainPartCounter > 63) {
                emailAddressIndexofError = currentCharNdx;
                emailAddressErrorMessage = "Domain name may not exceed 63 characters.\n";
                return emailAddressErrorMessage;
            }


			if (currentCharNdx<input.length()) {
				// If not all of the string has been consumed, we point to the current character
				// in the input line and specify what that character must be in order to move
				// forward.
				emailAddressIndexofError = currentCharNdx;		// Copy the index of the current character;
				// Update error message emailAddressErrorMessage = "This must be the end of the input.\n";
				// *** ADDED ***
				
				emailAddressErrorMessage = "An Email Address character may only contain the characters A-Z, a-z, 0-9, @, ., or -\n";
				return emailAddressErrorMessage;
			}
			else 
			{
				emailAddressIndexofError = -1;
				emailAddressErrorMessage = "";
				return emailAddressErrorMessage;
			}
 

			
        case 4:
        	//State 4 is not a final state, so we can return a very specific error message.\
            // *** ADDED *** 

            emailAddressIndexofError = currentCharNdx;        // Copy the index of the current character;
            emailAddressErrorMessage = "An Email Address character after a @,., - must be A-Z, a-z, 0-9.\n";
            return emailAddressErrorMessage;	



		default:
			return "";
		}
	}
}
