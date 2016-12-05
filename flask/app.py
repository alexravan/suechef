import requests
from flask import Flask, request 
from flask.json import JSONEncoder, JSONDecoder
from mongokit import Connection, Document, Collection
from pymongo import MongoClient
from collections import defaultdict
# from json import , loads, JSONEncoder, JSONDecoder
import datetime
import pickle
from bson import Binary, Code
from bson.json_util import dumps, loads


# configuration
MONGODB_PORT = 27017

app = Flask(__name__)
PARSER_URL = "http://recipe-api.appspot.com/?url="
app.config.from_object(__name__)
# db = MongoKit(app)
# connect to the database
connection = Connection(app.config['MONGODB_HOST'],
                        app.config['MONGODB_PORT'])
client = MongoClient('localhost', 27017)
db = client.test
f = open('links.txt', 'r')


# connection.register([Recipe])


@connection.register
class Recipe(Document):
	# __collection__ = 'recipes'
	# __database__ = 'test'
    structure = {
        'title': unicode,
        'description': unicode,
        'instructions': list,
        'ingredients': list,
        'date_creation': datetime.datetime,
        'image_url': unicode 
    }
    required_fields = ['title', 'instructions', 'ingredients']
    default_values = {
        'date_creation': datetime.datetime.utcnow
    }


# rp = connection['test'].recipes


rp = db.recipes


@app.route('/load')
def index():
	dood = []

	with open('links.txt') as f:
	    lines = f.readlines()
	
	for i in range(0, 100):
		print PARSER_URL + lines[i]
		blah = requests.get(PARSER_URL + lines[i]).content
		poopjson = loads(blah)

		description = poopjson['recipe']['page_description']
		title = poopjson['recipe']['page_title']
		instructions = poopjson['recipe']['page_recipe_instructions']
		ingredients = poopjson['recipe']['page_recipe_ingredients']
		image = poopjson['recipe']['page_image_url']
		rec = {'title': title, 'description': description, 'ingredients': ingredients, 'instructions': instructions, 'image_url': image }
		collection = connection['test'].recipes
		collection.insert(rec)
	return '1'


@app.route('/hw')
def hw():
	return "Hello world"

@app.route('/recipes')
def getr():
	sort = {'timestamp': -1}
	blah  = rp.find({}, limit=20)
	print blah

	return dumps(blah)

if __name__ == "__main__":
    app.run()
