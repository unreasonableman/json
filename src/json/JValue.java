package json;

public class JValue {
	public static final JValue TRUE = new JValue("true");
	public static final JValue FALSE = new JValue("false");

	private String string;

	private JValue(String string) {
		this.string = string;
	}

	JValue() {
	}

	public String toString() {
		return string;
	}
}