# ~/.profile: executed by the command interpreter for login shells.
# This file is not read by bash(1), if ~/.bash_profile or ~/.bash_login
# exists.
# see /usr/share/doc/bash/examples/startup-files for examples.
# the files are located in the bash-doc package.

# the default umask is set in /etc/profile
#umask 022

# if running bash
if [ -n "$BASH_VERSION" ]; then
    # include .bashrc if it exists
    if [ -f ~/.bashrc ]; then
	. ~/.bashrc
    fi
fi

# set PATH so it includes user's private bin if it exists
if [ -d $HOME/bin ]; then
	PATH=$HOME/bin:$PATH
fi

PATH=$PATH:/corp/tools/centaur/bin

SHORT_HOSTNAME=`hostname -s`

# disable the annoying message about using +new
export VSLICKXNOPLUSNEWMSG=1

export EDITOR=vim
export MANPATH=~/man:$MANPATH
export PATH=$PATH
export LANG=C

export P4USER=afrezell
export P4CLIENT=$SHORT_HOSTNAME.$USER
export P4DIFF=kdiff3
