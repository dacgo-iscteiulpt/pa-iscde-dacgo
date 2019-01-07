package testcases;
class Firetruck {
	private static final String COLOR = "RED";
	private int ladderHeight;
	private String name;

	public Firetruck(int ladderHeight, String name) {
		this.ladderHeight = ladderHeight;
		this.name = name;
	}

	public int getLadderHeight() {
		return ladderHeight;
	}

	public void setLadderHeight(int ladderHeight) {
		this.ladderHeight = ladderHeight;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	static public String getCOLOR() {
		return COLOR;
	}

}
public final class FinalClass {

}
