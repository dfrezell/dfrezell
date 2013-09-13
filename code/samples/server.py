#!/usr/bin/env python

"""
A simple echo server
"""

import socket
import sys
from optparse import OptionParser

parser = OptionParser()
parser.add_option("-p", "--port", dest="port", default=9999,
		help="port to bind the server to.")
parser.add_option("-a", "--address", dest="host", default="localhost",
		help="address to bind the server to.")
parser.add_option("-b", "--backlog", dest="backlog", default=5,
		help="how many clients will queue waiting for a connection.")
parser.add_option("-s", "--size", dest="size", default=1024,
		help="read buffer size.")
(options, args) = parser.parse_args()

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((options.host,options.port))
s.listen(options.backlog)
(client, address) = s.accept()

while 1:
	data = client.recv(1024)
	if not data: break
	client.send(data)

client.close()
