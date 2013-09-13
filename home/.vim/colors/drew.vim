" personal vim color scheme
hi clear
if exists("syntax_on")
  syntax reset
endif

let colors_name = "drew"

hi StatusLine   term=bold,reverse cterm=bold,reverse
hi StatusLineNC term=reverse      cterm=reverse
hi User1        term=inverse,bold cterm=inverse,bold ctermfg=Red
hi User2        term=bold         cterm=bold         ctermfg=Yellow
hi User3        term=inverse,bold cterm=inverse,bold ctermfg=Blue
hi User4        term=inverse,bold cterm=inverse,bold ctermfg=LightBlue
hi User5        term=inverse,bold cterm=inverse,bold ctermfg=Red         ctermbg=Green
hi Folded       term=standout     cterm=bold         ctermfg=Blue        ctermbg=Black
hi FoldColumn   term=standout                        ctermfg=DarkBlue    ctermbg=Black
hi Comment      term=bold                            ctermfg=DarkCyan
