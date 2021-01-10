import json
from sseclient import SSEClient as EventSource
import datetime
import sys

workdir = sys.argv[1];
url = 'https://stream.wikimedia.org/v2/stream/recentchange'
fname = workdir+"/input/"+datetime.datetime.now().strftime('%Y-%m-%d %H-%M-%S')+".log"
f = open(fname, "a")

for event in EventSource(url):
    if event.event == 'message':
        try:
            change = json.loads(event.data)
        except ValueError:
            pass
        else:
            #schema meta id type namespace title comment timestamp user bot server_url server_name server_script_path wiki parsedcomment
            #schema meta id type namespace title comment timestamp user bot minor patrolled length revision server_url server_name server_script_path wiki parsedcomment
            #schema meta type namespace title comment timestamp user bot log_id log_type log_action log_params log_action_comment server_url server_name server_script_path wiki parsedcomment
            if change['bot'] == False and change['type'] == 'edit' :
#                print('USER: {user} TITLE:{title} COMMENT:{comment} PARSEDCOMMENT:{parsedcomment}'.format(**change))
                f.write( 'USER:{user} TITLE:{title} COMMENT:{comment} PARSEDCOMMENT:{parsedcomment}'.format(**change) );
                f.write("\n");
            #print( *change);
