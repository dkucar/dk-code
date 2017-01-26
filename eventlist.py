import httplib2
import json
import re
import sys 
import datetime

# input is python eventlist.py 'search term'
# search_term could be upper case or lower case; all gets converted in the output
# output is list of events with that search term in name (or nothing)

search_term = sys.argv[1]

base = 'https://api.meetup.com/hvhikers/events?'
timestamp = 1329299201
url = '{}scroll=recent_past&page=1'.format(base)
h = httplib2.Http()
response, content = h.request(url,'GET')
result = json.loads(content)
event_name = result[0][u'name']
event_id = result[0][u'id']
timestamp = int(result[0][u'time'])/1000
link = result[0][u'link']
last = len(result)
tmp = response[u'link'].replace(';','').replace('<','').replace('>','').split()
url = tmp[2]
hits = 0

while (hits < 3):
  
	h = httplib2.Http()
	response, content = h.request(url,'GET')
	result = json.loads(content)
	tmp = response[u'link'].replace(';','').replace('<','').replace('>','').split()
	url = tmp[2]

	event_name = result[0][u'name']
	event_id = result[0][u'id']
	timestamp = int(result[0][u'time'])/1000
	link = result[0][u'link']

    	if re.search(search_term.lower(), event_name.lower()):
		print event_name
    		print link 
        	print datetime.datetime.fromtimestamp(timestamp).strftime('%Y-%m-%d')
    		print "++"
		hits+=1

