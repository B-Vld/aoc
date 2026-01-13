#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <limits.h>
#include <sys/types.h>

typedef unsigned int uint;
typedef unsigned long long int ull;

ull ipow10(uint exp)
{
    ull result = 1ULL;
    while (exp--)
        result *= 10;
    return result;
}

uint *get_max_and_pos(const char *line, uint line_len, uint start)
{
    uint *res = (uint *)malloc(2 * sizeof(uint));
    res[0] = 0;
    res[1] = 0;
    for (uint i = start; i < line_len; i++)
    {
        if (line[i] == '9')
        {
            res[0] = 9;
            res[1] = i;
            break;
        }
        if (line[i] - '0' > res[0])
        {
            res[0] = line[i] - '0';
            res[1] = i;
        }
    }
    return res;
}

uint part1(FILE *f)
{
    char *line = NULL;
    size_t len = 0;
    ssize_t read;
    uint res = 0;
    while ((read = getline(&line, &len, f)) != -1)
    {
        uint *start = get_max_and_pos(line, read - 2, 0);
        uint *end = get_max_and_pos(line, read - 1, start[1] + 1);

        res += start[0] * 10 + end[0];

        free(start);
        free(end);
    }
    free(line);
    return res;
}

ull part2(FILE *f)
{
    char *line = NULL;
    size_t len = 0;
    ssize_t read;
    ull res = 0;
    while ((read = getline(&line, &len, f)) != -1)
    {
        int i = 12;
        int start = -1;
        ull num = 0ULL;
        uint *m_pos;

        while (i > 0)
        {
            m_pos = get_max_and_pos(line, read - i, start + 1);
            num += m_pos[0] * ipow10(i - 1);
            start = m_pos[1];
            free(m_pos);
            i--;
        }
        res += num;
    }
    free(line);
    return res;
}

int main()
{

    FILE *f = fopen("input.txt", "r");
    if (!f)
    {
        fprintf(stderr, "Unable to open the file \n");
        return 0;
    }
    printf("%d\n", part1(f));
    fseek(f, 0, SEEK_SET);
    printf("%llu\n", part2(f));

    fclose(f);
    return 0;
}