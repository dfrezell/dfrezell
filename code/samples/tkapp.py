#!/usr/bin/python

from Tkinter import *

class App(Frame):
	def say_hi(self):
		print "hi there, everyone!"

	def createWidgets(self):
		self.QUIT = Button(self)
		self.QUIT["text"] = "QUIT"
		self.QUIT["fg"]   = "red"
		self.QUIT["command"] = self.quit
		self.QUIT.pack({"side": "left"})

		self.hi_there = Button(self)
		self.hi_there["text"] = "Hello"
		self.hi_there["command"] = self.say_hi
		self.hi_there.pack({"side": "left"})

	def __init__(self, master=None):
		Frame.__init__(self, master)
		self.pack()
		self.createWidgets()

# create the application
root = Tk()
myapp = App(master=root)

myapp.master.title("my do nothing app")
myapp.mainloop()
root.destroy()
