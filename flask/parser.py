from lxml import html
import requests

BASEURL = "http://www.epicurious.com"
URL = "http://www.epicurious.com/search/?page="


PATH = "/html/body[@class='search-page']/span[@class='page-wrap']/div/span[@class='page']/section/div[@class='results-group']/article[@class='recipe-result-item']/div[@class='recipe-panel ']/a[@class='view-complete-item']/@href"
links = []



for i in range(1, 30):
	src = URL + str(i)
	page = requests.get(src)
	tree = html.fromstring(page.content)	
	links.extend(tree.xpath(PATH))

for link in links:
	print BASEURL + link
	