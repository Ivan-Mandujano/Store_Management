package application;

public class UserData {
	 private static UserData instance;

	    private String userId;
	    private String first_name;
	    private String last_name;
	    private String access_level;
	    

	    private UserData() {
	        
	    }

	    public static UserData getInstance() {
	        if (instance == null) {
	            instance = new UserData();
	        }
	        return instance;
	    }

	    public String getUserId() {
	        return userId;
	    }
	    public String getAccess__level() {
	        return access_level;
	    }
	    public void setUserId(String userId, String access_level) {
	        this.userId = userId;
	        this.access_level = access_level;
	    }

	    public String getFirstName() {
	        return first_name;
	    }
	    public String getLastName() {
	        return last_name;
	    }

	    public void setName(String firstName, String lastName) {
	        this.first_name = firstName;
	        this.last_name = lastName;
	    }
}
