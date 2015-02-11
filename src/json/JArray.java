package json;

import java.util.ArrayList;
import java.util.List;
//import util.Log;

public class JArray extends JValue {
	private List<JValue> list;

	public JArray() {
		list = new ArrayList<JValue>();
	}

	public void add(JValue value) {
		//Log.log("JArray.add()");
		//Log.log("- value: " + value);

		list.add(value);
	}

	public JValue get(int index) {
		return list.get(index);
	}

	public int length() {
		return list.size();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("[");

		for (int i=0; i<list.size(); i++) {
			if (i != 0) sb.append(", ");
			sb.append(list.get(i));
		}

		sb.append("]");

		return sb.toString();
	}
}