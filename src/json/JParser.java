package json;

import java.io.*;
//import util.Log;

public class JParser {
	private static final String SEPARATORS = " \t\r\n,:{}[]";

	private static String string;
	private static PushbackInputStream in;
	private static int index;

	private JParser() {
	}

	public static JValue parse(String string) {
		//Log.log("JParser.parse()");
		//Log.log("- string: " + string);

		JParser.string = string;
		index = 0;
		in = new PushbackInputStream(new ByteArrayInputStream(string.getBytes()));
		return parseValue();
	}

	private static boolean isSeparator(char c) {
		//Log.log("JParser.isSeparator()");
		//Log.log("- c: " + c);

		return SEPARATORS.indexOf(c) >= 0;
	}

	private static void fail() {
		fail(0);
	}

	private static void fail(int offset) {
		index += offset;
		throw new RuntimeException("*** parse error at position " + index + ":\n"
			+ string.substring(index) + "\n" + string);
	}

	private static void unread(char c) {
		//Log.log("JParser.unread()");
		//Log.log("- c: " + c);

		try {
			in.unread(c);
		} catch (IOException e) {
			fail();
		}
	}

	private static char read() {
		//Log.log("JParser.read()");

		char c = '\0';

		try {
			c = (char)in.read();
			index++;
		} catch (IOException e) {
			fail();
		}

		return c;
	}

	private static char nextChar() {
		char c;

		//Log.log("JParser.nextChar()");

		for (;;) {
			c = read();

			if (c ==  ' ') continue;
			if (c == '\t') continue;
			if (c == '\r') continue;
			if (c == '\n') continue;

			break;
		}

		return c;
	}

	private static JObject parseObject() {
		JObject object;
		JString name;
		char c;

		//Log.log("JParser.parseObject()");

		object = new JObject();
		c = nextChar();

		for (;;) {
			if (c == '}') return object;
			if (c != '"') fail();

			name = parseString();

			c = nextChar();
			if (c != ':') fail();

			object.add(name.stringValue(), parseValue());

			c = nextChar();
			if (c == ',') c = nextChar();
		}
	}

	private static JArray parseArray() {
		JArray array;
		char c;

		//Log.log("JParser.parseArray()");

		array = new JArray();
		c = nextChar();

		for (;;) {
			if (c == ']') return array;

			unread(c);
			array.add(parseValue());

			c = nextChar();
			if (c == ',') c = nextChar();
		}
	}

	private static JString parseString() {
		StringBuilder sb;
		char c;

		//Log.log("JParser.parseString()");

		sb = new StringBuilder();

		for (;;) {
			c = read();

			if (c == '"') break;

			if (c == '\\') {
				c = read();

				switch (c) {
					case '"':
					case '\\':
						break;

					case 'n':
						c = '\n';
						break;

					case 'r':
						c = '\r';
						break;

					case 't':
						c = '\t';
						break;

					case 'u':
						// implement me!
						fail();

					default: fail();
				}
			}

			sb.append(c);
		}

		//Log.log("- string: " + sb.toString());

		return new JString(sb.toString());
	}

	private static JNumber parseNumber(char c) {
		StringBuilder sb;
		String string;

		//Log.log("JParser.parseNumber()");
		//Log.log("- c: " + c);

		sb = new StringBuilder();
		sb.append(c);

		for (;;) {
			c = read();

			if (isSeparator(c)) {
				unread(c);
				break;
			}

			sb.append(c);
		}

		string = sb.toString();
		//Log.log("- string: " + string);

		try {
			if (string.indexOf('.') >= 0
				|| string.indexOf('e') >= 0
				|| string.indexOf('E') >= 0)
			{
				return new JNumber(Double.parseDouble(string));
			} else {
				return new JNumber(Integer.parseInt(string));
			}
		} catch (NumberFormatException e) {
			fail(-string.length());
		}

		return null;
	}

	private static JValue parseLiteral(char c) {
		StringBuilder sb;
		String string;

		//Log.log("JParser.parseLiteral()");
		//Log.log("- c: " + c);

		sb = new StringBuilder();
		sb.append(c);

		for (;;) {
			c = read();

			if (isSeparator(c)) {
				unread(c);
				break;
			}

			sb.append(c);
		}

		string = sb.toString();
		//Log.log("- string: " + string);

		if (string.equals("true")) return JValue.TRUE;
		else if (string.equals("false")) return JValue.FALSE;
		else if (string.equals("null")) return null;
		else {
			fail(-string.length());
			return null;
		}
	}

	private static JValue parseValue() {
		char c;

		//Log.log("JParser.parseValue()");

		for (;;) {
			c = nextChar();
			//Log.log("- c: " + c);

			switch (c) {
				case '{':
					return parseObject();

				case '[':
					return parseArray();

				case '"':
					return parseString();

				case '-':
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
					return parseNumber(c);

				default:
					return parseLiteral(c);
			}
		}
	}
}