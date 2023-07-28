package selector.crawler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import selector.config.Config;
import selector.model.Article;
import selector.model.Board;

public class PTTCrawler {

	public static Document connectUrl(String bbsUrl) {
		Document document = null;

		try {
			// 取得年齡驗證cookie
			Connection.Response response = Jsoup.connect("https://www.ptt.cc/ask/over18").data("yes", "yes")
					.method(Method.POST).execute();
			String cookie = response.cookie("over18");

			// 帶上cookie連線
			document = Jsoup.connect(bbsUrl).cookie("over18", cookie).get();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return document;
	}

	public static int getLatestPage(Document document) {
		int latestPage = 0;

		Elements elements = document.select("div.btn-group-paging > a");
		for (Element element : elements) {
			Elements latestElements = element.select("a:contains(‹ 上頁)");
			if (!latestElements.isEmpty()) { // 因contains會有空值
				String href = latestElements.attr("href");
				int startIndex = href.indexOf("index") + 5;
				int endIndex = href.lastIndexOf(".html");
				latestPage = Integer.parseInt(href.substring(startIndex, endIndex)) + 1; // previosPage為最新頁碼的上一頁，+1為最新頁碼
			}
		}
		return latestPage;
	}

	public static Board getTargetBoard(String board) {
		Map<String, Board> hotBoard = Config.crawlHotBorad(); // 取得所有看板資訊
		Board targetBoard = hotBoard.get(board);
		return targetBoard;
	}

	// over18只要驗證一次過後每次點選不同看板都會傳over18=1的cookie
	public static List<Article> crawlPTT(Document document, String boardName, int pages) {
		List<Article> articles = new ArrayList<>();

		try {
			Board board = getTargetBoard(boardName);
//			System.out.println("targetBoard: " + board.toString());

			int latestPage = getLatestPage(document);
			int targetPage = latestPage + 1 - pages; // 假設現在頁碼為39110，取兩頁代表要39109、39110，targetPage = 39110+1-指定爬取頁數

			while (latestPage >= targetPage) {
				String pageUrl = "https://www.ptt.cc/bbs/" + boardName + "/index" + latestPage + ".html";
				Document pageDocument = connectUrl(pageUrl);
				Elements elements = pageDocument.select("div.r-ent");

				for (Element element : elements) {
					String url = element.select("div.title > a").attr("href");
					String title = element.select("div.title > a").text();
					String author = element.select("div.meta > div.author").text();
					String date = element.select("div.meta > div.date").text();

					if (!url.equals("")) { // 沒有網址代表本文已被刪除
						articles.add(new Article(board, url, title, null, author, date));
					}
				}
				latestPage--;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return articles;
	}

	public static void writeToFile(List<Article> articles) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter("./SelectorPTTArticle.txt"))) {
			for (Article article : articles) {
				bw.write(article.toString());
				bw.newLine();
//				System.out.println(article.toString());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String boardName = "Gossiping";
		String url = "https://www.ptt.cc/bbs/" + boardName + "/index.html";
		int pages = 2;
		Document document = connectUrl(url);
		List<Article> articles = crawlPTT(document, boardName, pages);
		writeToFile(articles);
	}
}
