#!/usr/bin/env python
#
# Copyright 2007 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
import webapp2
import asciichan

form = """
<form method="post">
    What is your birthday?
    <br>
    <label>Month <input type="text" value="%(month)s" name="month"></label>
    <label>Day <input type="text" value="%(day)s" name="day"></label>
    <label>Year <input type="text" value="%(year)s" name="year"></label>
    <div style="color: red">%(error)s</div>
    <br><br>
    <input type="submit">
</form>"""

months = {
    "jan": "January",
    "feb": "February",
    "mar": "March",
    "apr": "April",
    "may": "May",
    "jun": "June",
    "jul": "July",
    "aug": "August",
    "sep": "September",
    "oct": "October",
    "nov": "November",
    "dec": "December",
}

month2num = {
    "January" : 1,
    "February" : 2,
    "March" : 3,
    "April" : 4,
    "May" : 5,
    "June" : 6,
    "July" : 7,
    "August" : 8,
    "September" : 9,
    "October" : 10,
    "November" : 11,
    "December" : 12,
}

def valid_month(month):
    month = month[:3].lower()
    if month in months:
        return months[month]

def valid_day(day):
    if day and day.isdigit() and len(day) <= 2:
        day = int(day)
        if day >= 1 and day <= 31:
            return day

def valid_year(year):
    if year and year.isdigit() and len(year) <= 4:
        year = int(year)
        if year > 1900 and year < 2020:
            return year

html_cmap = {
    ">": "&gt;",
    "<": "&lt;",
    "&": "&amp;",
    '"': "&quot;",
}
def sanitize_html(s):
    return "".join([html_cmap.get(c, c) for c in s])

class MainHandler(webapp2.RequestHandler):
    def write_form(self, error="", month="", day="", year=""):
        self.response.out.write(form % {"error": error,
            "month": sanitize_html(month),
            "day": sanitize_html(day),
            "year": sanitize_html(year)})

    def get(self):
        #self.response.headers['Content-Type'] = 'text/html'
        self.write_form()

    def post(self):
        #self.response.headers['Content-Type'] = 'text/plain'
        #self.response.out.write(self.request)
        user_month = self.request.get('month')
        user_day = self.request.get('day')
        user_year = self.request.get('year')

        month = valid_month(user_month)
        day = valid_day(user_day)
        year = valid_year(user_year)

        if not (month and day and year):
            self.write_form("That doesn't look valid to me, friend.",
                    user_month, user_day, user_year)
        else:
            self.redirect("/thanks")

class ThanksHandler(webapp2.RequestHandler):
    def get(self):
        #import datetime
        #d = datetime.date(year, month2num[month], day)
        self.response.out.write("Thanks! that's a totally valid day!")


class Rot13Handler(webapp2.RequestHandler):
    rot13_form = """
            <h3>CS253: Unit 2 - ROT13</h3>
            <form method="post">
                <br>
                <label>Enter plaintext:<br>
                    <textarea style="width: 30em; height: 12em;" type="text" name="text">%(pt)s</textarea>
                </label>
                <br><br>
                <input type="submit">
            </form>"""
    rot13map = {}
    for c in range(26):
        rot13map[chr(c + ord('a'))] = chr(ord('a') + (c + 13) % 26)
        rot13map[chr(c + ord('A'))] = chr(ord('A') + (c + 13) % 26)

    def rot13(self, plaintext):
        return "".join([self.rot13map.get(c, c) for c in plaintext])

    def write_form(self, plaintext=""):
        self.response.out.write(self.rot13_form % {
            "pt": sanitize_html(plaintext)})

    def get(self):
        self.write_form()

    def post(self):
        plaintext = self.request.get('text')
        self.write_form(self.rot13(plaintext))


class AsciichanHandler(webapp2.RequestHandler):
    def get(self):
        self.response.out.write('asciichan!')

app = webapp2.WSGIApplication([('/', MainHandler),
                               ('/thanks', ThanksHandler),
                               ('/rot13', Rot13Handler),
                               ('/asciichan', asciichan.MainPage),],
                              debug=True)
