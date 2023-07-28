package selector.model;

public class Board {
	private String url;
	private String nameCN;
	private String nameEN;
	private boolean over18Check;

	public Board(String url, String nameCN, String nameEN, boolean over18Check) {
		this.url = url;
		this.nameCN = nameCN;
		this.nameEN = nameEN;
		this.over18Check = over18Check;
	}

	public String getUrl() {
		return url;
	}

	public String getNameCN() {
		return nameCN;
	}

	public String getNameEN() {
		return nameEN;
	}

	public boolean isOver18Check() {
		return over18Check;
	}

	@Override
	public String toString() {
		return String.format("網址:'%s', 中文版名:'%s', 英文版名:'%s', 年齡驗證:'%s'", url, nameCN, nameEN, over18Check);
	}

}
