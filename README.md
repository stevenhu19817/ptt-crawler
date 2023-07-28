# PTT Crawler
### PTT is the BBS of the Taiwanese.
##### Selector
*	Use jsoup with selectors for web crawling to fetch article titles. You can choose the board and number of pages to fetch.
*	The default value for the property 'body' of the 'Article' model is set to null. There is no code provided here to retrieve the content of each article based on its title. You can implement this functionality yourself.
*	The content written to the file is the result of the model's toString method.
	*	PTTHotBoard.txt
	*	SelectorPTTArticle.txt
##### Regex
*	Use jsoup with regex expression for web crawling to fetch article titles. You can choose the board but limited to non-age-restricted boards without 18+ verification and fetches only the latest page.
*	The content written to the file is a formatted ArrayList.
	*	RegexPTTArticle.txt
