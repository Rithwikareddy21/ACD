#include <stdio.h> #include <ctype.h> #include <string.h> #define MAX 20
char productions[MAX][MAX]; char first[MAX][MAX];
char follow[MAX][MAX]; int prod_count = 0;
int first_computed[26] = {0}; int follow_computed[26] = {0};
int follow_visited[26] = {0}; // Prevent infinite recursion void findFirst(char *result, char c);
void findFollow(char *result, char c); int isNonTerminal(char c);
void addToResult(char *result, char c); int contains(char *str, char c);
int main() { int i;
printf("Enter number of productions: "); scanf("%d", &prod_count);
getchar();
printf("Enter productions (e.g., E->E+T). Use '#' for epsilon:\n"); for (i = 0; i < prod_count; i++) {
fgets(productions[i], MAX, stdin);
productions[i][strcspn(productions[i], "\n")] = '\0'; // Remove newline
}
printf("\nFIRST sets:\n");
for (i = 0; i < prod_count; i++) {
char nonTerminal = productions[i][0];
if (!first_computed[nonTerminal - 'A']) {
findFirst(first[nonTerminal - 'A'], nonTerminal); first_computed[nonTerminal - 'A'] = 1;
}
}
// Print FIRST sets
for (i = 0; i < 26; i++) {
if (first_computed[i]) {
printf("FIRST(%c) = { ", (char)(i + 'A'));
for (int k = 0; first[i][k] != '\0'; k++) { if (first[i][k] == 'i')
printf("id");
else if (first[i][k] == '#')
 
printf("e"); else
printf("%c", first[i][k]);
if (first[i][k + 1] != '\0') printf(", ");
}
printf(" }\n");
}
}
printf("\nFOLLOW sets:\n");
for (i = 0; i < prod_count; i++) {
char nonTerminal = productions[i][0];
if (!follow_computed[nonTerminal - 'A']) { findFollow(follow[nonTerminal - 'A'], nonTerminal); follow_computed[nonTerminal - 'A'] = 1;
}
}
// Print FOLLOW sets for (i = 0; i < 26; i++) {
if (follow_computed[i]) {
printf("FOLLOW(%c) = { ", (char)(i + 'A'));
for (int k = 0; follow[i][k] != '\0'; k++) { if (follow[i][k] == 'i')
printf("id");
else if (follow[i][k] == '#') printf("e");
else
printf("%c", follow[i][k]);
if (follow[i][k + 1] != '\0') printf(", ");
}
printf(" }\n");
}
}
return 0;
}
void findFirst(char *result, char c) { if (!isNonTerminal(c)) {
addToResult(result, c); return;
}
for (int i = 0; i < prod_count; i++) { if (productions[i][0] == c) {
int rhs_index = 3;
while (productions[i][rhs_index] != '\0') { char next = productions[i][rhs_index];
 
if (next == '#') { // epsilon addToResult(result, '#'); break;
} else if (!isNonTerminal(next)) { addToResult(result, next); break;
} else {
char temp[MAX] = ""; findFirst(temp, next); int containsEpsilon = 0;
for (int k = 0; temp[k] != '\0'; k++) {
if (temp[k] != '#') {
addToResult(result, temp[k]);
} else {
containsEpsilon = 1;
}
}
if (!containsEpsilon) { break;
} else {
rhs_index++;
if (productions[i][rhs_index] == '\0') { addToResult(result, '#');
}
}
}
}
}
}
}
void findFollow(char *result, char c) {
if (follow_visited[c - 'A']) return; // Avoid infinite recursion follow_visited[c - 'A'] = 1;
if (productions[0][0] == c) {
addToResult(result, '$'); // Add $ to start symbol
}
for (int i = 0; i < prod_count; i++) { int len = strlen(productions[i]); for (int j = 3; j < len; j++) {
if (productions[i][j] == c) { int nextIndex = j + 1;
int addedFollowFromNext = 0;
 
while (nextIndex < len) {
char next = productions[i][nextIndex]; if (!isNonTerminal(next)) {
addToResult(result, next); addedFollowFromNext = 1; break;
} else {
char temp[MAX] = ""; findFirst(temp, next); int containsEpsilon = 0;
for (int k = 0; temp[k] != '\0'; k++) {
if (temp[k] != '#') {
addToResult(result, temp[k]);
} else {
containsEpsilon = 1;
}
}
if (containsEpsilon) { nextIndex++;
} else {
addedFollowFromNext = 1; break;
}
}
}
if (!addedFollowFromNext && productions[i][0] != c) { findFollow(result, productions[i][0]);
}
}
}
}
follow_visited[c - 'A'] = 0;
}
int isNonTerminal(char c) { return (c >= 'A' && c <= 'Z');
}
void addToResult(char *result, char c) { if (c == '\0') return;
if (!contains(result, c)) { int len = strlen(result); result[len] = c;
result[len + 1] = '\0';
}
}
 
int contains(char *str, char c) { for (int i = 0; str[i] != '\0'; i++) {
if (str[i] == c) return 1;
}
return 0;
}
