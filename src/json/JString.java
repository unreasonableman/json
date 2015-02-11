package json;

public class JString extends JValue {
	private String string;

	public JString(String string) {
		this.string = string;
	}

	public String toString() {
		return "\"" + string + "\"";
	}

	public String stringValue() {
		return string;
	}

	public int hashCode() {
		return string.hashCode();
	}

	public boolean equals(Object o) {
		if (o instanceof JString) {
			return string.equals(((JString)o).string);
		} else return false;
	}
}