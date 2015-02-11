package json;

public class JNumber extends JValue {
	private Number value;

	public JNumber(Number value) {
		this.value = value;
	}

	public String toString() {
		return String.valueOf(value);
	}

	public int hashCode() {
		return value.hashCode();
	}

	public Number numberValue() {
		return value;
	}

	public boolean equals(Object o) {
		if (o instanceof JNumber) {
			return value.equals(((JNumber)o).value);
		} else return false;
	}
}