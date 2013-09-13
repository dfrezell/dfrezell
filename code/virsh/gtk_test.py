#!/usr/bin/python

import sys
import gtk

menu_items = '''<ui>
        <menubar name="MenuBar">
            <menu action="File">
                <menuitem action="Save"/>
                <menuitem action="Flush"/>
                <separator/>
                <menuitem action="Quit"/>
            </menu>
        </menubar>
    </ui>'''

log_file = "/tmp/test.log"

def quit(*args):
    gtk.main_quit()


def save_cb(w):
    global log_file
    dialog = gtk.FileChooserDialog("Save Log File",
            None, gtk.FILE_CHOOSER_ACTION_SAVE,
            (gtk.STOCK_CANCEL, gtk.RESPONSE_CANCEL,
                gtk.STOCK_SAVE, gtk.RESPONSE_OK))
    dialog.set_default_response(gtk.RESPONSE_OK)
    dialog.set_position(gtk.WIN_POS_CENTER)
    if log_file != None:
        dialog.set_filename(log_file)

    # add *.log file filter
    filter = gtk.FileFilter()
    filter.set_name("Log files")
    filter.add_pattern("*.log")
    dialog.add_filter(filter)

    # add an everything filter
    filter = gtk.FileFilter()
    filter.set_name("All files")
    filter.add_pattern("*")
    dialog.add_filter(filter)

    try:
        response = dialog.run()
        if response == gtk.RESPONSE_OK:
            savefile = dialog.get_filename()
            if savefile == log_file:
                msg = "Overwriting exisiting log file '%s', do you wish to continue?" % log_file
                # warn about overwriting the old log file
                #warn = OkCancelDialog(msg, "Overwrite log file?", dialog)
                warn = gtk.MessageDialog(dialog,
                        gtk.DIALOG_MODAL | gtk.DIALOG_DESTROY_WITH_PARENT,
                        gtk.MESSAGE_WARNING, gtk.BUTTONS_YES_NO, msg)
                cont = warn.run()
                if cont == gtk.RESPONSE_NO:
                    return
            print "saving new file: %s" % savefile
            log_file = savefile
            log_io = open(log_file, "w")
            log_io.write("log file")
            log_io.close()
    finally:
        dialog.destroy()

def flush_cb(w):
    print w
    print type(w)
    print dir(w)

def get_menubar(window):
    uimanager = gtk.UIManager()
    accelgroup = uimanager.get_accel_group()
    window.add_accel_group(accelgroup)

    actiongroup = gtk.ActionGroup("SerialConsole")
    actiongroup.add_actions([
        ("Save",  gtk.STOCK_SAVE, "_Save",  None, "Save", save_cb),
        ("Flush", gtk.STOCK_GOTO_BOTTOM, "_Flush", None, "Flush", flush_cb),
        ("Quit",  gtk.STOCK_QUIT, "_Quit",  None, "Quit", quit),
        ("File",  gtk.STOCK_FILE, "_File"),
        ])
    actiongroup.get_action("Quit").set_property("short-label", "_Quit")

    uimanager.insert_action_group(actiongroup, 0)
    uimanager.add_ui_from_string(menu_items)

    return uimanager.get_widget("/MenuBar")

def main(*args):
    window = gtk.Window()
    window.hide()
    window.set_title("test")
    window.set_border_width(1)
    window.set_position(gtk.WIN_POS_CENTER)

    menubar = get_menubar(window)
    textv = gtk.TextView()
    textv.set_size_request(640, 480)
    textv.show()
    vbox = gtk.VBox()
    vbox.pack_start(menubar)
    vbox.pack_start(textv)
    vbox.show()

    window.add(vbox)
    window.connect("delete-event", quit)
    window.connect("destroy", quit)
    window.show()

    gtk.main()


if __name__ == "__main__":
    sys.exit(main(*sys.argv))

