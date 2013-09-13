#include <bfd.h>
#include <bfdlink.h>
#include <stdlib.h>
#include <stdio.h>

int process_symbol(struct bfd_link_info *link_info, asymbol *sym) {
    struct bfd_link_hash_entry *h;

    if (sym->flags & BSF_FUNCTION) {
        symbol_info si;
        bfd_symbol_info(sym, &si);
        if (si.value != 0)
            printf("name : %s %x\n", bfd_asymbol_name(sym), si.value);
    }
    return 0;
}

int main(int argc, char *argv[]) {
    struct bfd *b;
    struct bfd_link_order link_order;
    struct bfd_link_info link_info;
    asymbol **sym_tab;
    asection *sec;

    long size;
    long num_sym;
    long i;

    if (argc != 2) {
        fprintf(stderr, "need elf binary as argument\n");
        return 1;
    }

    bfd_init();
    b = bfd_openr(argv[1], NULL);
    if (!b) {
        bfd_perror("bfd_openr");
    }
    if (!bfd_check_format(b, bfd_object)) {
        bfd_perror("bfd_check_format");
    }

    link_info.hash = bfd_link_hash_table_create(b);
    link_order.type = bfd_indirect_link_order;

    for (sec = b->sections; sec; sec = sec->next) {
        sec->owner = b;
        sec->output_section = (sec->flags & SEC_ALLOC) ? sec : b->sections;
        sec->output_offset = 0;
    }

    if ((size = bfd_get_symtab_upper_bound(b)) < 0) {
        bfd_perror("bfd_get_symtab_upper_bound");
    }

    sym_tab = malloc(size);
    if ((num_sym = bfd_canonicalize_symtab(b, sym_tab)) < 0) {
        bfd_perror("bfd_canonicalize_symtab");
    }

    for (i = 0; i < num_sym; i++)
        process_symbol(&link_info, sym_tab[i]);

    free(sym_tab);
    bfd_close(b);
    return 0;
}
