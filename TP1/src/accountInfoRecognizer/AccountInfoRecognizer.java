  package accountInfoRecognizer;                                                
                                                                                
  public class AccountInfoRecognizer {                                 
                                                                              
      public static String checkName(String input, String fieldName) {
          if (input.length() == 0) {
              return "There was no " + fieldName + " found";
          }
          if (input.length() > 32) {
              return "A " + fieldName + " must have no more than 32 characters";
          }
          if (!input.matches("[a-zA-Z]+")) {
              return "A " + fieldName + " may only contain characters A-Z and a-z";
          }
          return "";
      }
  }
