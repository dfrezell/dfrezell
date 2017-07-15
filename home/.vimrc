"
" vimrc - main configuration file
" ____________________________________________________________
"
" Settings

" script syntax compatible with early pre-6.0 vim
" exit early if detected
:set secure nocompatible
:if version < 600
:	echo "Don't know anything about vim prior to 6.0"
:	finish
:endif

execute pathogen#infect()

syntax enable
filetype on
filetype plugin on
filetype indent on

if filereadable(expand("~/.vim/local/vimrc"))
	source ~/.vim/local/vimrc
endif

" Settings for C language
let c_gnu=1
let c_comment_strings=1
"let c_space_errors=1

" History and viminfo settings
if has("cmdline_hist") 
	set history=10000
endif
if has("viminfo")
	if filewritable(expand("$HOME/.vim/viminfo")) == 1 || 
				\ filewritable(expand("$HOME/.vim/")) == 2
		set viminfo=!,%,'5000,\"10000,:10000,/10000,n~/.vim/viminfo
	else
		set viminfo=
	endif
endif

" Don't save backups of files.
set nobackup
set backupcopy=yes

" highlight all searches
set hlsearch

" Display a status-bar.
set laststatus=2
if has("statusline")
	set statusline=%5*%{GetID()}%0*#%{winnr()}\ %<%f\ %3*%m%1*%r%0*\ %2*%y%4*%w%0*%=[%b\ 0x%B]\ \ %8l,%10([%c%V/%{strlen(getline(line('.')))}]%)\ %P
endif

" Settings for Explorer script
if !exists("g:explDetailedHelp")
	let g:explDetailedHelp=1
endif
if !exists("g:explDetailedList")
	let g:explDetailedList=1
endif
if !exists("g:explDateFormat")
	let g:explDateFormat="%d %b %Y %H:%M"
endif

" Settings for gcc & make
let g:cflags="-Wall"
let g:c_debug_flags="-ggdb -DDEBUG"
let g:makeflags=""
"
" Settings for AutoLastMod
" Set this to 1 if you will automaticaly change date of modification of file.
let g:autolastmod=1
" Modification is made on line like this variable (regular expression!):
let g:autolastmodtext="^\\([ 	]*Last modified: \\)"


" Priority between files for file name completion (suffixes)
" Do not give .h low priority in command-line filename completion.
set suffixes-=.h
set suffixes+=.aux
set suffixes+=.bbl
set suffixes+=.blg
set suffixes+=.log
set suffixes+=.eps
set suffixes+=.ps
set suffixes+=.pdf

set wildignore+=*.dvi
set wildignore+=*.pdf
set wildignore+=*.o
set wildignore+=*.ko
set wildignore+=*.swp
set wildignore+=*.aux
set wildignore+=*.bbl
set wildignore+=*.blg

" The cursor is kept in the same column (if possible).  This applies to the
" commands: CTRL-D, CTRL-U, CTRL-B, CTRL-F, "G", "H", "M", "L", , and to the
" commands "d", "<<" and ">>" with a linewise operator, with "%" with a count
" and to buffer changing commands (CTRL-^, :bnext, :bNext, etc.).  Also for an
" Ex command that only has a line number, e.g., ":25" or ":+".
set nostartofline

" Automatically setting options in various files
" WARNINNG: disable modeline if you are running vim version < 6.1.265 !!
"           These are security problems. See http://www.guninski.com/vim1.html
"                                                (reported by Georgi Guninski)
" TODO: there is no way how to test vim patchlevel version, therefore we are
"       not able determine vim version < 6.1.265
set modeline

" Available TAGS files
set tags=./TAGS,./tags,tags

" Don't add EOF at end of file
set noendofline

" Do case sensitive matching
set noignorecase

set showfulltag 

set cmdheight=2
set backspace=2
set incsearch
set ignorecase
set smartcase
set report=0
set showcmd showmatch showmode

" Indent of 1 tab with size of 4 spaces
set tabstop=4 
set softtabstop=4
set shiftwidth=4 

" Need more undolevels ??
" (default 100, 1000 for Unix, VMS, Win32 and OS/2)
set undolevels=10000

" Settings for mouse (gvim under Xwindows)
set nomousefocus
set mousehide
set mousemodel=popup

" Cursor always in the middle of the screen if GUI is not running
if ! has("gui_running")
	set scrolloff=999
endif
set sidescroll=5
set sidescrolloff=1


" Make window maximalized
set winheight=100

" The screen will not be redrawn while executing macros, registers and other
" commands that have not been typed. To force an updates use |:redraw|.
set lazyredraw

" time out on mapping after one second, time out on key codes after
" a tenth of a second
set timeout timeoutlen=1000 ttimeoutlen=100

" command completition
set wildchar=<Tab>
set wildmenu
set wildmode=longest:full,full

" Allow specified keys that move the cursor left/right to wrap to the
" previous/next line when the cursor is on the first/last character in the
" line. Allowed keys are 'h' and 'l', arrow keys are not allowed to wrap.
set whichwrap=h,l

" Customize display
" lastline	When included, as much as possible of the last line
"			in a window will be displayed.  When not included, a
"			last line that doesn't fit is replaced with "@" lines.
"uhex		Show unprintable characters hexadecimal as <xx>
"			instead of using ^C and ~C.
set display+=lastline
set display+=uhex

set noerrorbells
set visualbell
set t_vb=

set gfn="Inconsolata 12"

" Set this, if you will open all windows for files specified
" on the commandline at vim startup.
if !exists("g:open_all_win")
	let g:open_all_win=1
endif

" Keybord mappings
"
" start of line
"noremap <C-A>		i<Home>
inoremap <C-A>		<Home>
cnoremap <C-A>		<Home>
" end of line
noremap <C-E>		i<End>
inoremap <C-E>		<End>
" back one word
inoremap <C-B>	<S-Left>
" forward one word
"inoremap <C-F>	<S-Right>

" Switching between windows by pressing one time CTRL-X keys.
noremap <C-X> <C-W><C-W>

" Tip from http://vim.sourceforge.net/tips/tip.php?tip_id=173
map <C-J> <C-W>j<C-W>_
noremap <C-K> <C-W>k<C-W>_

" Make Insert-mode completion more convenient:
imap <C-L> <C-X><C-L>

" Search for the current Visual selection.
vmap S y/<C-R>=escape(@",'/\')<CR>
" replace selected text with text in register
vnoremap p <Esc>:let current_reg = @"<CR>gvdi<C-R>=current_reg<CR><Esc>

" New commands
command! -nargs=0 CallProg				call CallProg()
command! -nargs=0 OpenAllWin			call OpenAllWin()
command! -nargs=* ReadFileAboveCursor	call ReadFileAboveCursor(<f-args>)
command! -nargs=* R						call ReadFileAboveCursor(<f-args>)


" Function AutoLastMod()
" Provides atomatic change of date in files, if it is set via
" modeline variable autolastmod to appropriate value.
"
function! AutoLastMod()
	if exists("g:autolastmod")
		if g:autolastmod < 1
			return 0;
		elseif g:autolastmod == 1
			call LastMod(g:autolastmodtext)
		endif
	endif
endfunction

" Function LastMod()
" Automatic change date in *.html files.
"
function! LastMod(text, ...)
	mark d
	let line = "\\1" . strftime("%Y %b %d %X") " text of changed line
	let find = "g/" . a:text           " regexpr to find line
	let matx = a:text . ".*"            " ...if line was found
	exec find
	let curr_line = getline(".")
	if match(curr_line, matx) == 0
		" call setline(line("."), line)
		call setline(line("."), substitute(getline("."), matx, line, ""))
		exec "'d"
	endif
endfunction

" Function OpenAllWin()
" Opens windows for all files in the command line.
" Variable "opened" is used for testing, if window for file was already opened
" or not. This is prevention for repeat window opening after ViM config file
" reload.
"
function! OpenAllWin()
	" save Vim option to variable
	let s:save_split = &splitbelow
	set splitbelow

	let s:i = 1
	if g:open_all_win == 1
		while s:i < argc()
			if bufwinnr(argv(s:i)) == -1	" buffer doesn't exists or doesn't have window
				exec ":split " . escape(argv(s:i), ' \')
				"echo "Current window is " . bufwinnr(s:i) 
			endif
			let s:i = s:i + 1
		endwhile
	endif

	" force first window to be maximalized. Behaviour of vim has changed after
	" 6.2(?) release, therefore next command is not needed for vim < 6.2(?)
	exec "normal 2\<C-w>\<C-w>1\<C-w>\<C-w>"

	" restore Vim option from variable
	if s:save_split
		set splitbelow
	else
		set nosplitbelow
	endif
endfunction

" Function CallProg()
function! CallProg() abort
	let choice = confirm("Call:", "&make\nm&ake in cwd\n" .
				\ "&compile\nc&ompile in cwd\n" .
				\ "&run\nr&un in cwd")
	if choice == 1
		exec ":wall"
		exec "! cd %:p:h; pwd; make " . g:makeflags
	elseif choice == 2
		exec ":wall"
		exec "! cd " .
					\ getcwd() . "; pwd; make " . g:makeflags
	elseif choice == 3
		:call Compile(1)
	elseif choice == 4
		:call Compile(0)
	elseif choice == 5
		exec "! cd %:p:h; pwd; ./%:t:r"
	elseif choice == 6
		exec "! cd " . getcwd() . "; pwd; ./%<"
	endif
endfunction

" Function Compile()
function! Compile(do_chdir) abort
	let cmd = ""
	let filename = ""
	let filename_ext = ""

	if a:do_chdir == 1
		let cmd = "! cd %:p:h; pwd; "
		let filename = "%:t:r"
		let filename_ext = "%:t"
	else
		let cmd = "! cd " . getcwd() . "; pwd; "
		let filename = "%<"
		let filename_ext = "%"
	endif

	let choice = confirm("Call:", 
				\ "&compile\n" .
				\ "compile and &debug\n" .
				\ "compile and &run\n" .
				\ "compile using first &line")

	if choice != 0
		exec ":wall"
	endif

	if choice == 1
		exec cmd . "gcc " . g:cflags . 
					\ " -o " . filename . " " . filename_ext
	elseif choice == 2
		exec cmd . "gcc " . g:cflags . " " . g:c_debug_flags . 
					\ " -o " . filename . " " . filename_ext " && gdb " . filename
	elseif choice == 3
		exec cmd . "gcc " . g:cflags . 
					\ " -o " . filename . " " . filename_ext " && ./" . filename
	elseif choice == 4
		exec cmd . "gcc " . g:cflags . 
					\ " -o " . filename . " " . filename_ext . 
					\ substitute(substitute(getline(2), "VIMGCC", "", "g"), "GCC", "", "g" )
	endif
endfunction

" Function GetID()
" - used in statusline.
" If you are root, function return "# " string --> it is showed at begin of
"                                                  statusline
" If you aren't root, function return empty string --> nothing is visible
" Check for your name ID
let g:get_id = $USER
" If you are root, set to '#', else set to ''
if g:get_id == "root"
	let g:get_id = "# "
else
	let g:get_id = ""
endif
function! GetID()
	return g:get_id
endfunction

" Function ReadFileAboveCursor()
function! ReadFileAboveCursor(file, ...)
	let str = ":" . (v:lnum - 1) . "read " . a:file
	let idx = 1
	while idx <= a:0
		exec "let str = str . \" \" . a:" . idx
		let idx = idx + 1
	endwhile
	exec str
endfunction

" Function RemoveAutogroup()
silent! function! RemoveAutogroup(group)
silent exec "augroup! ". a:group
endfunction

" Autocomands
if has("autocmd")

" Autocomands for GUIEnter
augroup GUIEnter
	autocmd!
	if has("gui_win32")
		autocmd GUIEnter * simalt ~x
	endif
augroup END

" Autocomands for ~/.vimrc
augroup VimConfig
	autocmd!
	" Reread configuration of ViM if file ~/.vimrc is saved
	autocmd BufWritePost ~/.vimrc	so ~/.vimrc | exec "normal zv"
	autocmd BufWritePost vimrc   	so ~/.vimrc | exec "normal zv"
augroup END

" Autocommands for *.c, *.h, *.cc *.cpp
augroup C
	autocmd!
	autocmd BufEnter     *.c,*.h,*.cc,*.cpp	map  <buffer> <C-F> mfggvG$='f
	autocmd BufEnter     *.c,*.h,*.cc,*.cpp	imap <buffer> <C-F> <Esc>mfggvG$='fi
	autocmd BufEnter     *.c,*.h,*.cc,*.cpp	map <buffer> yii yyp3wdwi
	autocmd BufRead,BufNewFile  *.c,*.h,*.cc,*.cpp	setlocal expandtab 
	autocmd BufRead,BufNewFile  *.c,*.h,*.cc,*.cpp	setlocal cindent
	autocmd BufRead,BufNewFile  *.c,*.h,*.cc,*.cpp	setlocal cinoptions=>4,e0,n0,f0,{0,}0,^0,:4,=4,p4,t4,c3,+4,(2s,u1s,)20,*30,g4,h4
	autocmd BufRead,BufNewFile  *.c,*.h,*.cc,*.cpp	setlocal cinkeys=0{,0},:,0#,!<C-F>,o,O,e
augroup END

" Autocommands for *.py
augroup Python
	autocmd!
	autocmd BufRead,BufNewFile *.py setlocal encoding=utf8
	"autocmd BufRead,BufNewFile *.py setlocal paste
	autocmd BufRead,BufNewFile *.py setlocal expandtab
	autocmd BufRead,BufNewFile *.py setlocal textwidth=0
	autocmd BufRead,BufNewFile *.py setlocal autoindent
	autocmd BufRead,BufNewFile *.py setlocal backspace=indent,eol,start
	autocmd BufRead,BufNewFile *.py setlocal ruler
	autocmd BufRead,BufNewFile *.py setlocal commentstring=\ #\ %s
	autocmd BufRead,BufNewFile *.py setlocal foldlevel=0
	autocmd BufRead,BufNewFile *.py setlocal clipboard+=unnamed
augroup END

" Autocommands for *.html *.cgi
" Automatic updates date of last modification in HTML files. File must
" contain line "^\([<space><Tab>]*\)Last modified: ",
" else will be date writtend on the current " line.
augroup HtmlCgiPHP
	autocmd!
	autocmd BufEnter                 *.html	imap <buffer> QQ </><Esc>2F<lywf>f/pF<i
	autocmd BufWritePre,FileWritePre *.html	call AutoLastMod()
	autocmd BufEnter                 *.cgi	imap <buffer> QQ </><Esc>2F<lywf>f/pF<i
	autocmd BufWritePre,FileWritePre *.cgi	call AutoLastMod()
	autocmd BufEnter                 *.php	imap <buffer> QQ </><Esc>2F<lywf>f/pF<i
	autocmd BufWritePre,FileWritePre *.php	call AutoLastMod()
	autocmd BufEnter                 *.php3	imap <buffer> QQ </><Esc>2F<lywf>f/pF<i
	autocmd BufWritePre,FileWritePre *.php3	call AutoLastMod()
augroup END

" Autocomands for *.txt
augroup Txt
	autocmd BufNewFile,BufRead  *.txt   setf txt
augroup END

augroup Rust
	autocmd BufRead,BufNewFile *.rs set filetype=rust
augroup END

endif " if has("autocmd")

if exists("g:open_all_win")
	if g:open_all_win == 1
		" Open all windows only if we are not running (g)vimdiff
		if match(v:progname, "diff") < 0
			call OpenAllWin()
		endif
	endif
	" turn g:open_all_win off after opening all windows
	" it prevents reopen windows after 2nd sourcing .vimrc
	let g:open_all_win = 0
endif

" i use a black background for my terminal
set background=light
colorscheme solarized

