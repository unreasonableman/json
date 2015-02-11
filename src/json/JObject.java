package json;

import java.util.*;
//import util.Log;

public class JObject extends JValue {
	private Hashtable<String, JValue> table;

	public JObject() {
		table = new Hashtable<String, JValue>();
	}

	public JValue getValue(String name) {
		return table.get(name);
	}

	public void add(String name, JValue value) {
		//Log.log("JObject.add()");
		//Log.log("- name: " + name);
		//Log.log("- value: " + value);

		table.put(name, value);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("{");

		Enumeration<String> keys = table.keys();

		while (keys.hasMoreElements()) {
			String name = keys.nextElement();

			sb.append('"');
			sb.append(name);
			sb.append("\": ");
			sb.append(table.get(name));

			if (keys.hasMoreElements()) {
				sb.append(", ");
			}
		}

		sb.append("}");

		return sb.toString();
	}
}