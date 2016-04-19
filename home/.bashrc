
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

    PS1="$BGRN\u$RST@$BGRN\h$RST:$BBLU\w$GRAY:\n$BRED[\$?]$RST$ "
    PS2="> "
}

# If not running interactively, don't do anything
[ -z "$PS1" ] && return

# add our home bin directory to path if it's not
# already in the path
if [[ ":$PATH:" != *":$HOME/bin:"* ]]; then
	export PATH=$HOME/bin:$PATH
fi

# set default editor to vim
export EDITOR=vim

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

if [ -f ~/.bash_aliases ]; then
    source ~/.bash_aliases
fi
