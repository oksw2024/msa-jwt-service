package osj.javat.Entity;

public enum Role {
	ROLE_USER("USER"),
	ROLE_ADMIN("ADMIN");
	
	// "USER", "ADMIN"
	private String value;
	
	Role(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
