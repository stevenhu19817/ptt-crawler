package selector.model;

public class Article {
	private Board board;
	private String url;
	private String title;
	private String body;
	private String author;
	private String date;

	public Article(Board board, String url, String title, String body, String author, String date) {
		this.board = board;
		this.url = url;
		this.title = title;
		this.body = body;
		this.author = author;
		this.date = date;
	}

	public Board getBoard() {
		return board;
	}

	public String getUrl() {
		return url;
	}

	public String getTitle() {
		return title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getAuthor() {
		return author;
	}

	public String getDate() {
		return date;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.format("網址:'%s', 標題:'%s', 內容:'%s', 作者:'%s', 日期:'%s'", url, title, body, author, date);
	}

}