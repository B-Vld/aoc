#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <inttypes.h>

typedef uint64_t u64;

bool has_weird_pattern(char *num, int len, bool part1)
{
    if (part1)
    {
        if (len % 2 != 0)
        {
            return false;
        }
        int half = len / 2;
        for (int i = 0; i < half; i++)
        {
            if (num[i] != num[half + i])
            {
                return false;
            }
        }
        return true;
    }
    char *pattern = (char *)malloc(len / 2 + 1);
    for (int i = 0; i < len / 2; i++)
    {
        pattern[i] = num[i];
        int c_pattern = 0;
        bool has_pattern = true;
        for (int j = 0; j < len; j++)
        {
            if (c_pattern > i)
            {
                c_pattern = 0;
            }
            if (num[j] != pattern[c_pattern])
            {
                has_pattern = false;
                break;
            }
            c_pattern++;
        }
        if (has_pattern && c_pattern == i + 1)
        {
            return true;
        }
    }
    return false;
}

u64 get_weird_patterns(char first[], char second[], size_t f_len, size_t s_len, bool part1)
{
    u64 res = 0;

    char *f_end;
    char *s_end;
    u64 _first = strtoul(first, &f_end, 10);
    if (has_weird_pattern(first, f_len, part1))
    {
        res += _first;
    }
    u64 _second = strtoul(second, &s_end, 10);
    if (has_weird_pattern(second, s_len, part1))
    {
        res += _second;
    }
    for (u64 i = _first + 1; i < _second; i++)
    {
        int len = snprintf("%" PRIu64 "\n", 0, "%" PRIu64, i);
        char *num = (char *)malloc(len + 1);
        snprintf(num, len + 1, "%" PRIu64, i);
        if (has_weird_pattern(num, len, part1))
        {
            res += i;
        }
        free(num);
    }
    return res;
}

u64 solve(FILE *f, bool part1)
{
    u64 res = 0;
    char *line = NULL;
    size_t len = 0;
    int sz_len = getline(&line, &len, f);

    bool toggle = true;

    char first[21] = {0};
    int f_len = 0;

    char second[21] = {0};
    int s_len = 0;

    for (int i = 0; i < sz_len; i++)
    {
        if (toggle)
        {
            if (line[i] == '-')
            {
                toggle = false;
            }
            else
            {
                first[f_len++] = line[i];
            }
        }
        else
        {
            if (line[i] == ',')
            {
                toggle = true;
                res += get_weird_patterns(first, second, strlen(first), strlen(second), part1);

                memset(first, 0, sizeof first);
                f_len = 0;
                memset(second, 0, sizeof second);
                s_len = 0;
            }
            else
            {
                second[s_len++] = line[i];
                if (i == sz_len - 1)
                {
                    res += get_weird_patterns(first, second, strlen(first), strlen(second), part1);
                }
            }
        }
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
    printf("%" PRIu64 "\n", solve(f, true));
    fseek(f, 0, SEEK_SET);
    printf("%" PRIu64 "\n", solve(f, false));

    bool z = 1 % 2 == 0;

    fclose(f);
    return 0;
}