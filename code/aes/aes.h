
/*
 * aes.h
 *
 * Developed by Drew Frezell
 *
 */

#ifndef AES_H
#  define AES_H

#define DATA_BLOCK_LEN 16 // 128 bits 

typedef enum _aes_mode {
    AES_128 = 4,
    AES_192 = 6,
    AES_256 = 8
} aes_mode_e;

typedef struct _aes_word {
    union {
    int w;
    unsigned char b[4];
    };
} aes_word_t;

class aes {
public:
    aes(const char *key = NULL, aes_mode_e mode = AES_128);
    ~aes();
    int encrypt(const char *inp, char *outp, int len);
    int decrypt(const char *inp, char *outp, int len);
    int speed();
private:
    int cipher(unsigned char *src, unsigned char *dst);
    int inv_cipher(unsigned char *src, unsigned char *dst);

    int key_expansion(const char *key, int keylen);
    int sub_word(int w);
    int rot_word(unsigned int w);

    int add_round_key(int r, int c);
    int sub_bytes();
    int inv_sub_bytes();
    int shift_rows();
    int inv_shift_rows();
    int mix_columns();
    int inv_mix_columns();

    unsigned char src_[DATA_BLOCK_LEN];
    unsigned char dst_[DATA_BLOCK_LEN];
    unsigned char sta_[DATA_BLOCK_LEN];
    int rounds_;
    int keylen_;
    aes_word_t *keysched_;
};

#endif /* #ifndef AES_H */


