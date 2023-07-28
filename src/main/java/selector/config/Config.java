package selector.config;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import selector.model.Board;

public final class Config {
	public static Map<String, Board> crawlHotBorad() {
		String nameCN = "";
		Map<String, Board> hotBroadsMap = new HashMap<>();

		Document document = connectUrl();
		Elements elements = document.select("div.b-ent");

		for (Element element : elements) {
			String url = element.select("a").attr("href");
			String nameEN = element.select("div.board-name").text();
			String category = element.select("div.board-class").text();
			String nameCNString = element.select("div.board-title").text();
			String regex = "\\[[\\w\\u4e00-\\u9fa5]+\\]"; // [中文/英文標題]
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(nameCNString);
			boolean contains = matcher.find();
			if (contains == true) {
				int startIndex = nameCNString.indexOf("[");
				int endIndex = nameCNString.lastIndexOf("]") + 1;

				nameCN = nameCNString.substring(startIndex, endIndex);
			} else {
				nameCN = category; // 沒有[]的會取分類作為看板
			}
			hotBroadsMap.put(nameEN, new Board(url, nameCN, nameEN, true));
			writeToFile(hotBroadsMap);
		}
		return hotBroadsMap;
	}

	public static Document connectUrl() {
		Document document = null;

		try {
			document = Jsoup.connect("https://www.ptt.cc/bbs/index.html").get();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return document;
	}

	public static void writeToFile(Map<String, Board> hotBroadsMap) {

		try (BufferedWriter bw = new BufferedWriter(new FileWriter("./PTTHotBoard.txt"))) {
			// iterator查看Map中內容
			Set<String> set = hotBroadsMap.keySet();
			Iterator<String> iterator = set.iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				bw.write("key:" + key + " value:" + hotBroadsMap.get(key));
				bw.newLine();
//				System.out.println("key:" + key + "	value:" + hotBroadsMap.get(key));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
