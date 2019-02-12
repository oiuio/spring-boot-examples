package cc.oiuio.validator.constant;


import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum Sex {

	male("1", "男"),
	female("0", "女"),;
	private String value;
	private String label;

	Sex(String value, String label) {
		this.value = value;
		this.label = label;
	}

	public static Sex parse(String s) {
		for (Sex sex : values()) {
			if (StringUtils.equals(s, sex.value)) {
				return sex;
			}
		}
		return null;
	}


}
