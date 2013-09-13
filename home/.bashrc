# ~/.bashrc: executed by bash(1) for non-login shells.
# see /usr/share/doc/bash/examples/startup-files (in the package bash-doc)
# for examples

# prompt function
function prompt_setup
{
	local WHT="\[\033[0;0m\]"
	local BWHT="\[\033[1;0m\]"
	local BLCK="\[\033[0;30m\]"
	local GRAY="\[\033[1;30m\]"
	local RED="\[\033[0;31m\]"
	local BRED="\[\033[1;31m\]"
	local GRN="\[\033[0;32m\]"
	local BGRN="\[\033[1;32m\]"
	local YEL="\[\033[0;33m\]"
	local BYEL="\[\033[1;33m\]"
	local BLU="\[\033[0;34m\]"
	local BBLU="\[\033[1;34m\]"
	local VIL="\[\033[0;35m\]"
	local BVIL="\[\033[1;35m\]"
	local CYN="\[\033[0;36m\]"
	local BCYN="\[\033[1;36m\]"
	local UNK="\[\033[0;37m\]"
	local BUNK="\[\033[0;37m\]"
	local RST="\[\033[0m\]"

	#let fillsize=${COLUMNS}-${#pprompt}
	#if [ "$fillsize" -lt "0" ]; then
	#	let cut=4-${fillsize}
	#	newPWD="...${PWD:${cut}}"
	#else
	#	newPWD=${PWD}
	#fi

	PS1="$BGRN\u$RST@$BGRN\h$RST:$BBLU\w$GRAY:\n$BRED[\$?]$RST$ "
	PS2="> "

}


# If not running interactively, don't do anything
[ -z "$PS1" ] && return

# don't put duplicate lines in the history. See bash(1) for more options
export HISTCONTROL=ignoredups
# ... and ignore same sucessive entries.
export HISTCONTROL=ignoreboth

# check the window size after each command and, if necessary,
# update the values of LINES and COLUMNS.
shopt -s checkwinsize

# make less more friendly for non-text input files, see lesspipe(1)
[ -x /usr/bin/lesspipe ] && eval "$(lesspipe)"

# set the fancy prompt
prompt_setup

# enable core dumps for crashes
ulimit -c unlimited

# Alias definitions.
# You may want to put all your additions into a separate file like
# ~/.bash_aliases, instead of adding them here directly.
# See /usr/share/doc/bash-doc/examples in the bash-doc package.

if [ -f ~/.bash_aliases ]; then
    . ~/.bash_aliases
fi

# enable programmable completion features (you don't need to enable
# this, if it's already enabled in /etc/bash.bashrc and /etc/profile
# sources /etc/bash.bashrc).
if [ -f /etc/bash_completion ]; then
    . /etc/bash_completion
fi

# create a list of variables for directories to use for 'shopt cdable_vars'
shopt -s cdable_vars
Dp4ws="/var/localdata/afrezell/p4ws"
Dcentmain="$Dp4ws/Centaur/base-branch/main"

