package regex.crawler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class PTTCrawler {

	public static String connectUrl(String url) {
		Document document = null;
		try {
			document = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return document.html();
	}

	public static List<String> parseHTML(String html) {
		String linkText = "";
		String author = "";
		String date = "";

		List<String> targetStringList = new ArrayList<>();

		String articlePattern = "<div class=\"r-ent\">(.+?)<div class=\"mark\">";
		Pattern articleRegex = Pattern.compile(articlePattern, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
		Matcher articleMatcher = articleRegex.matcher(html);

		String titlePattern = "<div class=\"title\"><a href=\"[^\"]+?\">(.+?)</a>";
		Pattern titleRegex = Pattern.compile(titlePattern, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

		String titleNotPattern = "<div class=\"title\">\\s*(.+?)\\s*</div>";
		Pattern titleNotRegex = Pattern.compile(titleNotPattern, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

		String authorPattern = "<div class=\"author\">\\s*(\\S+?)\\s*</div>";
		Pattern authorRegex = Pattern.compile(authorPattern, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

		String datePattern = "<div class=\"date\">\\s*([^<]+?)\\s*</div>";
		Pattern dateRegex = Pattern.compile(datePattern, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

		try {
			while (articleMatcher.find()) { // 找到第一筆符合的資料時就會=true，並把位置記錄下來下一次再紀錄的位置開始找，直到找不到時=false
				String articleHtml = articleMatcher.group(1);
				Matcher titleMatcher = titleRegex.matcher(articleHtml);
				Matcher titleNotMatcher = titleNotRegex.matcher(articleHtml);
				Matcher authorMatcher = authorRegex.matcher(articleHtml);
				Matcher dateMatcher = dateRegex.matcher(articleHtml);
				if (titleMatcher.find()) {
					linkText = titleMatcher.group(1);
				} else {
					if (titleNotMatcher.find()) {
						linkText = StringEscapeUtils.unescapeHtml4(titleNotMatcher.group(1)); // html編碼轉換
					} else {
						System.out.println("找不到title");
					}
				}
				if (authorMatcher.find()) {
					author = authorMatcher.group(1);
				} else {
					System.out.println("找不到author");
				}
				if (dateMatcher.find()) {
					date = dateMatcher.group(1);
				} else {
					System.out.println("找不到date");
				}
				String targetString = linkText + "\t" + author + "\t" + date;
				targetStringList.add(targetString);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return targetStringList;
	}

	public static void writeToFile(List<String> targetStringList) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter("./RegexPTTArticle.txt"))) {
			for (String targetString : targetStringList) {
				bw.write(targetString);
				bw.newLine();
//				System.out.println(targetString);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String board = "Stock";
		String url = "https://www.ptt.cc/bbs/" + board + "/index.html";
		String html = connectUrl(url);
		List<String> targetStringList = parseHTML(html);
		writeToFile(targetStringList);
	}

}
