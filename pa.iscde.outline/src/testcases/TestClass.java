package testcases;

public class TestClass {
	public static int ID = 2213123;
	private int age;
	protected String name;
	
	public TestClass(int givenAge, String givenName) {
		this.age = givenAge;
		this.name = givenName;
	}

	public void setID(int ID){ 
		this.ID=ID;
	}

	protected int getID(){ 
		return this.ID; 
	}

}

	