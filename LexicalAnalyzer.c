#include <stdio.h>
#include <ctype.h>
#include <string.h>

#define MAX_ID_LEN 31
#define MAX_TOKENS 500

const char *keywords[] = {
    "auto", "break", "case", "char", "const", "continue", "default",
    "do", "double", "else", "enum", "extern", "float", "for",
    "goto", "if", "int", "long", "register", "return", "short",
    "signed", "sizeof", "static", "struct", "switch", "typedef",
    "union", "unsigned", "void", "volatile", "while"
};
int keywordCount = sizeof(keywords) / sizeof(keywords[0]);

const char *operators[] = {
    "+", "-", "*", "/", "%", "=", "==", "!=", "<", ">", "<=", ">=",
    "++", "--", "&&", "||", "!", "+=", "-=", "*=", "/="
};
int operatorCount = sizeof(operators) / sizeof(operators[0]);

const char *delimiters[] = { ";", ",", "(", ")", "{", "}", "[", "]" };
int delimiterCount = sizeof(delimiters) / sizeof(delimiters[0]);

const char *specialSymbols[] = { "#", "\"", "'", "\\", ".", ":", "?" };
int specialCount = sizeof(specialSymbols) / sizeof(specialSymbols[0]);

char keywordsFound[MAX_TOKENS][MAX_ID_LEN];
int kwIndex = 0;

char identifiersFound[MAX_TOKENS][MAX_ID_LEN];
int idIndex = 0;

char numbersFound[MAX_TOKENS][64];
int numIndex = 0;

char operatorsFound[MAX_TOKENS][4];
int opIndex = 0;

char delimitersFound[MAX_TOKENS][4];
int delIndex = 0;

char specialsFound[MAX_TOKENS][4];
int spIndex = 0;

char stringsFound[MAX_TOKENS][256];
int strIndex = 0;

char charsFound[MAX_TOKENS][4];
int chIndex = 0;


// ---------------- Utility functions ----------------

int isKeyword(const char *str) {
    for (int i = 0; i < keywordCount; i++) {
        if (strcmp(str, keywords[i]) == 0)
            return 1;
    }
    return 0;
}

int isOperator(const char *str) {
    for (int i = 0; i < operatorCount; i++) {
        if (strcmp(str, operators[i]) == 0)
            return 1;
    }
    return 0;
}

int isDelimiter(char c) {
    for (int i = 0; i < delimiterCount; i++) {
        if (c == delimiters[i][0])
            return 1;
    }
    return 0;
}

int isSpecial(char c) {
    for (int i = 0; i < specialCount; i++) {
        if (c == specialSymbols[i][0])
            return 1;
    }
    return 0;
}


// ---------------- Skip whitespace & comments ----------------

void skipWhitespaceAndComments(FILE *fp) {
    int c;
    while ((c = fgetc(fp)) != EOF) {
        if (isspace(c))
            continue;

        if (c == '/') {
            int next = fgetc(fp);
            if (next == '/') {
                while ((c = fgetc(fp)) != '\n' && c != EOF);
            } else if (next == '*') {
                while ((c = fgetc(fp)) != EOF) {
                    if (c == '*' && (c = fgetc(fp)) == '/')
                        break;
                }
            } else {
                ungetc(next, fp);
                ungetc(c, fp);
                return;
            }
        } else {
            ungetc(c, fp);
            return;
        }
    }
}


// ---------------- Token extraction ----------------

void getToken(FILE *fp) {
    int c;
    skipWhitespaceAndComments(fp);
    c = fgetc(fp);
    if (c == EOF) return;

    // Identifier or keyword
    if (isalpha(c) || c == '_') {
        char buffer[MAX_ID_LEN + 1];
        int i = 0;
        buffer[i++] = c;
        while ((c = fgetc(fp)) != EOF && (isalnum(c) || c == '_')) {
            if (i < MAX_ID_LEN)
                buffer[i++] = c;
        }
        buffer[i] = '\0';
        ungetc(c, fp);

        if (isKeyword(buffer))
            strcpy(keywordsFound[kwIndex++], buffer);
        else
            strcpy(identifiersFound[idIndex++], buffer);
    }

    // Number
    else if (isdigit(c)) {
        char buffer[64];
        int i = 0;
        buffer[i++] = c;
        while ((c = fgetc(fp)) != EOF && (isdigit(c) || c == '.')) {
            buffer[i++] = c;
        }
        buffer[i] = '\0';
        ungetc(c, fp);
        strcpy(numbersFound[numIndex++], buffer);
    }

    // String literal
    else if (c == '"') {
        char buffer[256];
        int i = 0;
        while ((c = fgetc(fp)) != EOF && c != '"') {
            buffer[i++] = c;
        }
        buffer[i] = '\0';
        strcpy(stringsFound[strIndex++], buffer);
    }

    // Character literal
    else if (c == '\'') {
        char ch = fgetc(fp);
        fgetc(fp); // skip closing quote
        charsFound[chIndex][0] = ch;
        charsFound[chIndex][1] = '\0';
        chIndex++;
    }

    // Delimiters
    else if (isDelimiter(c)) {
        char d[2] = { c, '\0' };
        strcpy(delimitersFound[delIndex++], d);
    }

    // Special symbols
    else if (isSpecial(c)) {
        char s[2] = { c, '\0' };
        strcpy(specialsFound[spIndex++], s);
    }

    // Operators
    else {
        char op[3] = { c, '\0', '\0' };
        int next = fgetc(fp);
        if (next != EOF) {
            op[1] = next;
            if (!isOperator(op)) {
                op[1] = '\0';
                ungetc(next, fp);
            }
        }
        if (isOperator(op))
            strcpy(operatorsFound[opIndex++], op);
    }
}


// ---------------- Main ----------------

int main() {
    FILE *fp = fopen("input.c", "r");
    if (!fp) {
        printf("Could not open file.\n");
        return 1;
    }

    while (!feof(fp)) {
        getToken(fp);
    }

    fclose(fp);

    printf("\nKeywords:\n");
    for (int i = 0; i < kwIndex; i++)
        printf("%s\n", keywordsFound[i]);

    printf("\nIdentifiers:\n");
    for (int i = 0; i < idIndex; i++)
        printf("%s\n", identifiersFound[i]);

    printf("\nNumbers:\n");
    for (int i = 0; i < numIndex; i++)
        printf("%s\n", numbersFound[i]);

    printf("\nStrings:\n");
    for (int i = 0; i < strIndex; i++)
        printf("\"%s\"\n", stringsFound[i]);

    printf("\nChars:\n");
    for (int i = 0; i < chIndex; i++)
        printf("'%s'\n", charsFound[i]);

    printf("\nOperators:\n");
    for (int i = 0; i < opIndex; i++)
        printf("%s\n", operatorsFound[i]);

    printf("\nDelimiters:\n");
    for (int i = 0; i < delIndex; i++)
        printf("%s\n", delimitersFound[i]);

    printf("\nSpecial Symbols:\n");
    for (int i = 0; i < spIndex; i++)
        printf("%s\n", specialsFound[i]);

    return 0;
}
