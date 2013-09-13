#!/usr/bin/env python

import os
import webapp2
import jinja2
import re
import hashlib

from google.appengine.ext import db

template_dir = os.path.join(os.path.dirname(__file__), 'templates')
jinja_env = jinja2.Environment(loader = jinja2.FileSystemLoader(template_dir),
                               autoescape = True)

class Users(db.Model):
    user = db.StringProperty(required = True)
    pw   = db.StringProperty(required = True)
    salt = db.StringProperty(required = True)
    email = db.EmailProperty(required = False)
    created = db.DateTimeProperty(auto_now_add = True)


class Handler(webapp2.RequestHandler):
    def write(self, *a, **kw):
        self.response.out.write(*a, **kw)
    
    def render_str(self, template, **params):
        t = jinja_env.get_template(template)
        return t.render(params)
    
    def render(self, template, **kw):
        self.write(self.render_str(template, **kw))

class LogoutHandler(Handler):
    def get(self):
        self.response.headers.add_header('Set-Cookie', 'user=; Path=/')
        self.redirect("/blog/signup")

class MainPage(Handler):
    USERNAME_RE = re.compile(r"^[a-zA-Z0-9_-]{3,20}$")
    PASSWORD_RE = re.compile(r"^.{3,20}$")
    
    def valid_login(self, username, password):
        valid = True
        hash_pw = ""
        error = ""

        # check username
        if not re.match(self.USERNAME_RE, username) or \
                not re.match(self.PASSWORD_RE, password):
            valid = False

        # check password with hash in database
        if valid:
            users = db.GqlQuery("SELECT * FROM Users WHERE user = :1", username)
            valid = False
            for user in users:
                hash_pw = self.hash_password(username, password, user.salt)
                valid = hash_pw == user.pw

        if not valid:
            error = 'Invalid login'

        return (valid, hash_pw, error)

    def hash_password(self, name, pw, salt):
        return hashlib.sha256(name + pw + salt).hexdigest()

    def render_front(self, user="", error=""):
        self.render("login.html", user=user, error=error)

    def get(self):
        self.render_front()

    def post(self):
        username = self.request.get('username')
        password = self.request.get('password')

        valid, hash_pw, error = self.valid_login(username, password)

        if valid:
            self.response.headers.add_header('Set-Cookie',
                    'user=%s|%s; Path=/' % (str(username), str(hash_pw)))
            self.redirect("/blog/welcome")
        else:
            self.render_front(username, error)

app = webapp2.WSGIApplication([webapp2.Route('/blog/login', MainPage),
                               webapp2.Route('/blog/logout', LogoutHandler),],
                              debug=True)
