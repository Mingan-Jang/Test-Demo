package mapstruct_demo.enumeration;

public enum Gender {
	Male("male"), Female("female");

	private final String gender;

	Gender(String gender) {
		this.gender = gender;
	}

	public String getGender() {
		return gender;
	}
}
