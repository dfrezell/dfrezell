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

class Blog(db.Model):
    # types: Integer, Float, String, Text, Date, Time, DateTime
    #        Email, Link, PostalAddress, ...
    #  String vs. Text:
    #    String - less than 500 chars and indexed.
    #    Text - greater than 500 chars but not indexed.
    # look into google appengine datatypes for more info
    subject = db.StringProperty(required = True)
    content = db.TextProperty(required = True)
    created = db.DateTimeProperty(auto_now_add = True)

class MainPage(Handler):
    def render_front(self, subject="", art="", error=""):
        blogs = db.GqlQuery("SELECT * FROM Blog ORDER BY created DESC LIMIT 10")
        self.render("blog.html", blogs=blogs)        
        
    def get(self, **kw):
        self.render_front()


class NewPost(Handler):
    def render_front(self, subject="", content="", error=""):
        self.render("blog_newpost.html", subject=subject, content=content, error=error)        
        
    def get(self):
        self.render_front()
    
    def post(self):
        subject = self.request.get("subject")
        content = self.request.get("content")
        
        if subject and content:
            a = Blog(subject = subject, content = content)
            a.put()
            self.redirect("/blog/%d" % a.key().id())
        else:
            error = "we need a subject and some more cowbell!"
            self.render_front(subject, content, error)

class SinglePost(Handler):
    def get(self, **kw):
        blog = Blog.get_by_id(int(kw['id']))
        if blog:
            self.render("blog.html", blogs=[blog,])
        else:
            self.error(404)
            self.write("<html> <head> <title>404 Not Found</title> </head> <body> <h1>404 Not Found</h1> The resource could not be found.<br /><br /> </body> </html>")


app = webapp2.WSGIApplication([webapp2.Route('/blog', MainPage),
                               webapp2.Route('/blog/<id:\d+>', SinglePost),
                               webapp2.Route('/blog/newpost', NewPost),],
                              debug=True)
