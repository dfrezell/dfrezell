#!/usr/bin/python

import sys
import os
import getopt

_VERSION = "1.0"

def _virusage():
    vsh = virsh()
    print """
virsh.py [options] [commands]

  options:
    -c | --connect <uri>    hypervisor connection URI
    -r | --readonly         connect readonly
    -d | --debug <num>      debug level [0-5]
    -h | --help             this help
    -q | --quiet            quiet mode
    -t | --timing           print timing information
    -l | --log <file>       output logging to file
    -v | --version          program version

  commands (non interactive mode):"""
    vsh.help()
    print "\n  (specify help <command> for details about the command)\n\n"


class virctl(object):
    name = None
    conn = None
    cmd = None
    args = None
    imode = True
    quiet = False
    debug = 0
    timing = False
    readonly = False
    logfile = None

    def __init__(self, session):
        pass
    
    def parse_argv(self, argv):
        sopts = "d:hqtc:vrl:"
        lopts = ["debug=","help","quiet","timing","connect=","version","readonly","log="]

        if len(argv) < 2:
            return True

        try:
            opts, args = getopt.getopt(argv[1:], sopts, lopts)
        except getopt.GetoptError, err:
            print err
            _virusage()
            sys.exit(-1)

        for o, a in opts:
            if o in ("--debug", "-d"):
                self.debug = int(a)
            elif o in ("--help", "-h"):
                _virusage()
                sys.exit(0)
            elif o in ("--quiet", "-q"):
                self.quiet = True
            elif o in ("--timing", "-t"):
                self.timing = True
            elif o in ("--connect", "-c"):
                self.name = a
            elif o in ("--version", "-v"):
                print "%s" % _VERSION
                sys.exit(0)
            elif o in ("--readonly", "-r"):
                self.readonly = True
            elif o in ("--log", "-l"):
                self.log = a
            else:
                print "unsupported option : %s. See --help." % o
                sys.exit(-1)

        if args != None:
            self.imode = False
            self.cmd = args[0]
            self.args = args[1:]

        return True


class virsh(object):
    
    def __init__(self, ctl=None):
        self.ctl = ctl

    def __call__(self, cmd, args):
        self._func_map[cmd](self, *args)

    def attach_device(self, *args):
        """{"help" : "attach device from an XML file",
            "desc" : "Attach device from an XML <file>.",
            "opts" : [("domain", object, 1, "domain name, id or uuid"),
                      ("file", object, 1, "XML file")]}"""
        pass

    def attach_disk(self, *args):
        """{"help" : "attach disk device",
            "desc" : "Attach new disk device.",
            "opts" : [("domain", object, 1, "domain name, id or uuid"),
                      ("source", object, 1, "source of disk device"),
                      ("target", object, 1, "target of disk device"),
                      ("driver", str, 0, "driver of disk device"),
                      ("subdriver", str, 0, "subdriver of disk device"),
                      ("type", str, 0, "target device type"),
                      ("mode", str, 0, "mode of device reading and writing")]}"""
        pass

    def attach_interface(self, *args):
        """{"help" : "attach network interface",
            "desc" : "Attach new network interface.",
            "opts" : [("domain", object, 1, "domain name, id or uuid"),
                      ("type", object, 1, "network interface type"),
                      ("source", object, 1, "source of network interface"),
                      ("target", object, 0, "target network name"),
                      ("mac", object, 0, "MAC address"),
                      ("script", object, 0, "script used to bridge network interface")]}"""
        pass

    def autostart(self, *args):
        """{"help" : "autostart a domain",
            "desc" : "Configure a domain to be automatically started at boot.",
            "opts" : [("domain", object, 1, "domain name, id or uuid"),
                      ("disable", bool, 0, "disable autostarting")]}"""
        pass

    def capabilities(self, *args):
        """{"help" : "capabilities",
            "desc" : "Returns capabilities of hypervisor/driver.",
            "opts" : None}"""
        pass

    def cd(self, *args):
        """{"help" : "change the current directory",
            "desc" : "Change the current directory.",
            "opts" : [("dir", object, 0, "directory to switch to (default: home or else root")]}"""
        pass

    def connect(self, *args):
        """{"help" : "(re)connect to hypervisor",
            "desc" : "Connect to local hypervisor. This is built-in command after shell start up.",
            "opts" : [("name", object, 0, "hypervisor connection URI"),
                      ("readonly", bool, 0, "read-only connection")]}"""
        pass

    def console(self, *args):
        """{"help" : "connect to the guest console",
            "desc" : "Connect the virtual serial console for the guest",
            "opts" : [("domain", object, 1, "domain name, id or uuid")]}"""
        pass

    def create(self, *args):
        """{"help" : "create a domain from an XML file",
            "desc" : "Create a domain.",
            "opts" : [("file", object, 1, "file containing an XML domain description"),
                      ("console", bool, 0, "attach to console after creation")]}"""
        pass

    def start(self, *args):
        """{"help" : "start a (previously defined) inactive domain",
            "desc" : "Start a domain.",
            "opts" : [("domain", object, 1, "name of the inactive domain"),
                      ("console", bool, 0, "attach to console after creation")]}"""
        pass

    def destroy(self, *args):
        """{"help" : "destroy a domain",
            "desc" : "Destroy a given domain.",
            "opts" : [("domain", object, 1, "domain name, id or uuid")]}"""
        pass

    def detach_device(self, *args):
        """{"help" : "detach device from an XML file",
            "desc" : "Detach device from an XML <file>",
            "opts" : [("domain", object, 1, "domain name, id or uuid"),
                      ("file", object, 1, "XML file")]}"""
        pass

    def detach_disk(self, *args):
        """{"help" : "detach disk device",
            "desc" : "Detach disk device.",
            "opts" : [("domain", object, 1, "domain name, id or uuid"),
                      ("target", object, 1, "target of disk device")]}"""
        pass

    def detach_interface(self, *args):
        """{"help" : "detach network interface",
            "desc" : "Detach network interface.",
            "opts" : [("domain", object, 1, "domain name, id or uuid"),
                      ("type", object, 1, "network interface type"),
                      ("mac", str, 0, "MAC address")]}"""
        pass

    def define(self, *args):
        """{"help" : "define (but don't start) a domain from an XML file",
            "desc" : "Define a domain.",
            "opts" : [("file", object, 0, "file containing an XML domain description")]}"""
        pass

    def domid(self, *args):
        """{"help" : "convert a domain name or UUID to domain id",
            "desc" : "",
            "opts" : [("domain", object, 1, "domain name or uuid")]}"""
        pass

    def domuuid(self, *args):
        """{"help" : "convert a domain name or id to domain UUID",
            "desc" : "",
            "opts" : [("domain", object, 1, "domain id or name")]}"""
        pass

    def dominfo(self, *args):
        """{"help" : "domain information",
            "desc" : "Returns basic information about the domain.",
            "opts" : [("domain", object, 1, "domain name, id or uuid")]}"""
        pass

    def domname(self, *args):
        """{"help" : "convert a domain id or UUID to domain name",
            "desc" : "",
            "opts" : [("domain", object, 1, "domain id or uuid")]}"""
        pass

    def domstate(self, *args):
        """{"help" : "domain state",
            "desc" : "Returns state about a domain.",
            "opts" : [("domain", object, 1, "domain name, id or uuid")]}"""
        pass

    def domblkstat(self, *args):
        """{"help" : "get device block stats for a domain",
            "desc" : "Get device block stats for a running domain.",
            "opts" : [("domain", object, 1, "domain name, id or uuid"),
                      ("device", object, 1, "block device")]}"""
        pass

    def domifstat(self, *args):
        """{"help" : "get network interface stats for a domain",
            "desc" : "Get network interface stats for a running domain.",
            "opts" : [("domain", object, 1, "domain name, id or uuid"),
                      ("interface", object, 1, "interface device")]}"""
        pass

    def domxml_from_native(self, *args):
        """{"help" : "Convert native config to domain XML",
            "desc" : "Convert native guest configuration format to domain XML format.",
            "opts" : [("format", object, 1, "source config data format"),
                      ("config", object, 1, "config data file to import from")]}"""
        pass

    def domxml_to_native(self, *args):
        """{"help" : "Convert domain XML to native config",
            "desc" : "Convert domain XML config to a native guest configuration format.",
            "opts" : [("format", object, 0, "target config data type format"),
                      ("xml", object, 0, "xml data file to export from")]}"""
        pass

    def dumpxml(self, *args):
        """{"help" : "domain information in XML",
            "desc" : "Output the domain information as an XML dump to stdout.",
            "opts" : [("domain", object, 1, "domain name, id or uuid"),
                      ("inactive", bool, 0, "show inactive defined XML"),
                      ("security-info", bool, 0, "include security sensitive information in XML dump")]}"""
        pass

    def edit(self, *args):
        """{"help" : "edit XML configuration for a domain",
            "desc" : "Edit the XML configuration for a domain.",
            "opts" : [("domain", object, 0, "domain name, id or uuid")]}"""
        pass

    def help(self, *args):
        """{"help" : "print help",
            "desc" : "Prints global help or command specific help.",
            "opts" : [("command", object, 0, "name of command")]}"""
        if len(args) == 0:
            # print the generic help, by looping through all functions
            # and printing out generic help
            for cmd, func in sorted(self._func_map.iteritems()):
                fdoc = eval(func.__doc__)
                print "    %-20s %s" % (cmd, fdoc["help"])
        else:
            # we need to print out detailed help for a specific command
            func = args[0]
            fdoc = eval(self._func_map[func].__doc__)
            print "  NAME"
            print "    %s - %s\n" % (func, fdoc["help"])
            print "  SYNOPSIS"
            print "    %s" % func,
            # I feel like the following two for loops could be greatly
            # improved in the readability dept
            if fdoc["opts"] != None:
                for c, t, req, d in fdoc["opts"]:
                    if t is int:
                        opt = "[--%s] <number>" % c
                    elif t is str:
                        opt = "[--%s] <string>" % c
                    elif t is object:
                        if req:
                            opt = "<%s>" % c
                        else:
                            opt = "[<%s>]" % c
                    else:
                        opt = "[--%s]" % c
                    print "%s" % opt,
                print ""
            if fdoc["desc"] != None:
                print "\n  DESCRIPTION\n    %s" % fdoc["desc"]
            if fdoc["opts"] != None:
                print "\n  OPTIONS"
                for c, t, f, d in fdoc["opts"]:
                    opt = ""
                    if t is int:
                        opt = "--%s <number>" % c
                    elif t is str:
                        opt = "--%s <string>"
                    elif t is object:
                        opt = "<%s>" % c
                    else:
                        opt = "--%s" % c
                    print "    %-16s  %s" % (opt, d)
            print ""

    def net_list(self, *args):
        """{"help" : "list networks",
            "desc" : "Returns a list of networks.",
            "opts" : [("inactive", bool, 0, "list inactive networks"),
                      ("all", bool, 0, "list inactive & active networks")]}"""
        pass

    _func_map = {
            "help" : help, "attach-device" : attach_device,
            "attach-disk" : attach_disk, "attach-interface" : attach_interface,
            "autostart" : autostart, "capabilities" : capabilities,
            "cd" : cd, "connect" : connect,
            "console" : console, "create" : create,
            "start" : start, "destroy" : destroy,
            "detach-device" : detach_device, "detach-disk" : detach_disk,
            "detach-interface" : detach_interface, "define" : define,
            "domid" : domid, "domuuid" : domuuid,
            "dominfo" : dominfo, "domname" : domname,
            "domstate" : domstate, "domblkstat" : domblkstat,
            "domifstat" : domifstat, "domxml-from-native" : domxml_from_native,
            "domxml-to-native" : domxml_to_native,
            "dumpxml" : dumpxml, "edit" : edit,
            #"find-storage-pool-sources" : find_storage_pool_sources,
            #"find-storage-pool-sources-as" : find_storage_pool_sources_as,
            #"freecell" : freecell, #"hostname" : hostname,
            #"list" : list, #"migrate" : migrate,
            #"net-autostart" : net_autostart, #"net-create" : net_create,
            #"net-define" : net_define, #"net-destroy" : net_destroy,
            #"net-dumpxml" : net_dumpxml, #"net-edit" : net_edit,
            "net-list" : net_list,
            #"net-name" : net_name, #"net-start" : net_start,
            #"net-undefine" : net_undefine, #"net-uuid" : net_uuid,
            #"iface-list" : iface_list, #"iface-name" : iface_name,
            #"iface-mac" : iface_mac, #"iface-dumpxml" : iface_dumpxml,
            #"iface-define" : iface_define, #"iface-undefine" : iface_undefine,
            #"iface-edit" : iface_edit, #"iface-start" : iface_start,
            #"iface-destroy" : iface_destroy, #"nodeinfo" : nodeinfo,
            #"nodedev-list" : nodedev_list,
            #"nodedev-dumpxml" : nodedev_dumpxml,
            #"nodedev-dettach" : nodedev_dettach,
            #"nodedev-reattach" : nodedev_reattach,
            #"nodedev-reset" : nodedev_reset,
            #"nodedev-create" : nodedev_create,
            #"nodedev-destroy" : nodedev_destroy,
            #"pool-autostart" : pool_autostart,
            #"pool-build" : pool_build, #"pool-create" : pool_create,
            #"pool-create-as" : pool_create_as, #"pool-define" : pool_define,
            #"pool-define-as" : pool_define_as, #"pool-destroy" : pool_destroy,
            #"pool-delete" : pool_delete, #"pool-dumpxml" : pool_dumpxml,
            #"pool-edit" : pool_edit, #"pool-info" : pool_info,
            #"pool-list" : pool_list, #"pool-name" : pool_name,
            #"pool-refresh" : pool_refresh, #"pool-start" : pool_start,
            #"pool-undefine" : pool_undefine, #"pool-uuid" : pool_uuid,
            #"secret-define" : secret_define,
            #"secret-dumpxml" : secret_dumpxml,
            #"secret-set-value" : secret_set_value,
            #"secret-get-value" : secret_get_value,
            #"secret-undefine" : secret_undefine,
            #"secret-list" : secret_list,
            #"pwd" : pwd, #"quit" : quit,
            #"reboot" : reboot, #"restore" : restore,
            #"resume" : resume, #"save" : save,
            #"schedinfo" : schedinfo, #"dump" : dump,
            #"shutdown" : shutdown, #"setmem" : setmem,
            #"setmaxmem" : setmaxmem, #"setvcpus" : setvcpus,
            #"suspend" : suspend, #"ttyconsole" : ttyconsole,
            #"undefine" : undefine, #"uri" : uri,
            #"vol-create" : vol_create, #"vol-create-from" : vol_create_from,
            #"vol-create-as" : vol_create_as, #"vol-clone" : vol_clone,
            #"vol-delete" : vol_delete, #"vol-dumpxml" : vol_dumpxml,
            #"vol-info" : vol_info, #"vol-list" : vol_list,
            #"vol-path" : vol_path, #"vol-name" : vol_name,
            #"vol-key" : vol_key, #"vcpuinfo" : vcpuinfo,
            #"vcpupin" : vcpupin, #"version" : version,
            #"vncdisplay" : vncdisplay
            }



def main(*argv):
    ctl = virctl(os.getenv("VIRSH_DEFAULT_CONNECT_URI"))
    ctl.parse_argv(argv)

    vsh = virsh(ctl)

    if (not ctl.imode):
        vsh(ctl.cmd, ctl.args)
    else:
        # do the fancy interactive mode stuff here
        pass


if __name__ == "__main__":
    sys.exit(main(*sys.argv))
