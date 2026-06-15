# Fight API

A RESTful API for combat sports data — fighters, bouts, events, and results. Built with Python and Flask.


## Overview

Fight API provides structured JSON endpoints for querying combat sports information. It is designed to be lightweight, easy to extend, and straightforward to integrate into front-end applications or data pipelines.

## Endpoints
```
Method	Endpoint	        Description
GET	    /fighters	        List all fighters
GET	    /fighters/<id>	  Get a single fighter by ID
GET	    /events	List      all events
GET	    /events/<id>	    Get a single event by ID
GET	    /bouts	          List all bouts
GET	    /bouts/<id>	      Get a single bout by ID
POST	  /fighters	        Add a new fighter
POST    /events	          Add a new event
POST	  /bouts	          Add a new bout

```

## Example Response
```
jsonGET /fighters/1

{
  "id": 1,
  "name": "Fighter Name",
  "weight_class": "Welterweight",
  "nationality": "British",
  "record": {
    "wins": 18,
    "losses": 2,
    "draws": 0
  }
}

```
## Tech Stack


Language: Python 3
Framework: Flask
Data Storage: JSON 
Testing: Postman 



## Getting Started
```
bash# Clone the repository
git clone https://github.com/Anoor1174/fight-api.git
cd fight-api
```
# Install dependencies
```
pip install -r requirements.txt

# Run the development server
python app.py

The API will be available at http://localhost:5000.
```

## Project Structure
```
├── app.py               # Flask app and route definitions
├── models.py            # Data models
├── data/                # Seed data (JSON or SQLite DB)
├── requirements.txt     # Python dependencies
└── README.md

# Using Postman
# Import the collection from /postman/FightAPI.postman_collection.json
```

## Dependencies
```
Flask
flask-cors
```
## Install with:
```
bashpip install -r requirements.txt
```
