#!/usr/bin/env python

import os
import webapp2
import jinja2
import re
import random
import string
import hashlib
import logging
from collections import namedtuple

from google.appengine.ext import db

template_dir = os.path.join(os.path.dirname(__file__), 'templates')
jinja_env = jinja2.Environment(loader = jinja2.FileSystemLoader(template_dir),
                               autoescape = True)

SECRET="imsosecret"
def hash_str(s):
    return hmac.new(SECRET, s).hexdigest()

def make_secure_val(s):
    return "%s|%s" % (s, hash_str(s))

def check_secure_val(h):
    val = h.split('|')[0]
    if h == make_secure_val(val):
        return val

UserStr = namedtuple("UserStr", "name email")
ErrStr = namedtuple("ErrStr", "name pw verify email")

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

class WelcomeHandler(Handler):
    page = """<html><h3>Welcome, %s</h3></html>"""

    def valid_user(self, user):
        username, hash_pw = user.split('|')
        users = db.GqlQuery("SELECT * FROM Users WHERE user = :1", username)
        for u in users:
            if u.pw == hash_pw:
                return True
        return False

    def get(self):
        user = self.request.cookies.get('user')
        if user and self.valid_user(user):
            self.write(self.page % user.split('|')[0])
        else:
            self.redirect("/blog/signup")


class MainPage(Handler):
    USERNAME_RE = re.compile(r"^[a-zA-Z0-9_-]{3,20}$")
    PASSWORD_RE = re.compile(r"^.{3,20}$")
    EMAIL_RE = re.compile(r"^[\S]+@[\S]+\.[\S]+$")
    
    def is_valid(self, username, password, verify, email):
        valid = True
        user = UserStr(username, email)
        error = ErrStr("", "", "", "")

        # check username
        if not re.match(self.USERNAME_RE, username):
            valid = False
            error = error._replace(name = 'Invalid username')

        # check if user already in database
        if valid:
            u = db.GqlQuery("SELECT * FROM Users WHERE user = :1", username)
            if u.count():
                valid = False
                error = error._replace(name = 'User already exists')

        # check password
        if not re.match(self.PASSWORD_RE, password):
            valid = False
            error = error._replace(pw = 'Invalid password')

        # verify password
        if (password != verify):
            valid = False
            error = error._replace(verify = 'Password mismatch')

        # check email
        if email != "" and not re.match(self.EMAIL_RE, email):
            error = error._replace(email = 'Invalid email')

        return (valid, user, error)

    def new_salt(self):
        return ''.join([random.choice(string.ascii_letters) for x in range(5)])

    def hash_password(self, name, pw, salt):
        return hashlib.sha256(name + pw + salt).hexdigest()

    def store_user(self, username, password, email):
        salt = self.new_salt()
        hash_pw = self.hash_password(username, password, salt)
        if email == "":
            email = None
        u = Users(user=username, pw=hash_pw, salt=salt, email=email)
        u.put()
        self.response.headers.add_header('Set-Cookie',
                'user=%s|%s; Path=/blog' % (str(username), str(hash_pw)))

    def render_front(self, user=UserStr("", ""), error=ErrStr("", "", "", "")):
        self.render("signup.html", user=user, error=error)

    def get(self):
        self.render_front()

    def post(self):
        username = self.request.get('username')
        password = self.request.get('password')
        verify = self.request.get('verify')
        email = self.request.get('email')

        valid, user, error = self.is_valid(username, password, verify, email)

        if valid:
            self.store_user(username, password, email)
            self.redirect("/blog/welcome")
        else:
            self.render_front(user, error)

app = webapp2.WSGIApplication([webapp2.Route('/blog/signup', MainPage),
                               webapp2.Route('/blog/welcome', WelcomeHandler),],
                              debug=True)
