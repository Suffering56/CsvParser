package common.util;

public class DataEnity {

	public DataEnity(String name, String field, String value) {
		this.name = name;
		this.field = field;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getField() {
		return field;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "SenderEntity [name=" + name + ", field=" + field + ", value=" + value + "]";
	}

	private final String name;
	private final String field;
	private final String value;
}
