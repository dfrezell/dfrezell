"
" vimconfig - project of powerful ViM 6.3 configuration files
" 
" gvimrc - GUI configuration file
" ____________________________________________________________
"

function! Source(File)
	silent! execute "source " . a:File
endfunction

call Source("~/.vim/local/gvimrc")

" When the GUI starts, 't_vb' is reset to its default value.
" We will restore it.
set visualbell
set t_vb=

set guifont=Bitstream\ Vera\ Sans\ Mono\ 10

" Colors
let g:zenburn_high_Contrast=1
:colorscheme zenburn

" Modeline
" vim:set ts=4:
