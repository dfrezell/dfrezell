#!/usr/bin/env python

import os
import webapp2
import jinja2

from google.appengine.ext import db

template_dir = os.path.join(os.path.dirname(__file__), 'templates')
jinja_env = jinja2.Environment(loader = jinja2.FileSystemLoader(template_dir),
                               autoescape = True)
                               
class Handler(webapp2.RequestHandler):
    def write(self, *a, **kw):
        self.response.out.write(*a, **kw)
    
    def render_str(self, template, **params):
        t = jinja_env.get_template(template)
        return t.render(params)
    
    def render(self, template, **kw):
        self.write(self.render_str(template, **kw))

class Art(db.Model):
    # types: Integer, Float, String, Text, Date, Time, DateTime
    #        Email, Link, PostalAddress, ...
    #  String vs. Text:
    #    String - less than 500 chars and indexed.
    #    Text - greater than 500 chars but not indexed.
    # look into google appengine datatypes for more info
    title = db.StringProperty(required = True)
    art = db.TextProperty(required = True)
    created = db.DateTimeProperty(auto_now_add = True)

class MainPage(Handler):
    def render_front(self, title="", art="", error=""):
        arts = db.GqlQuery("SELECT * FROM Art ORDER BY created DESC")
        self.render("asciichan.html", title=title, art=art, error=error, arts=arts)        
        
    def get(self):
        self.render_front()
    
    def post(self):
        title = self.request.get("title")
        art = self.request.get("art")
        
        if title and art:
            a = Art(title = title, art = art)
            a.put()
            self.redirect("/asciichan")
        else:
            error = "we need both a title and some artwork!"
            self.render_front(title, art, error)
