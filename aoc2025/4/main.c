#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <stdbool.h>

typedef unsigned int uint;

static const int pos[][2] = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

bool is_valid_pos(int x, int y, uint l_len, uint i_len)
{
    return x >= 0 &&
           x < i_len &&
           y >= 0 &&
           y < l_len;
}

bool has_fewer_than_4_rolls(char **arr, uint x, uint y, uint l_len, uint i_len)
{
    uint rolls = 0;
    for (int i = 0; i < 8; i++)
    {
        int n_x = x + pos[i][0], n_y = y + pos[i][1];
        if (is_valid_pos(n_x, n_y, l_len, i_len) && arr[n_x][n_y] == '@')
        {
            rolls++;
            if (rolls == 4)
                return false;
        }
    }
    return true;
}

void free_input(char **arr, uint len)
{
    for (int i = 0; i < len; i++)
    {
        free(arr[i]);
    }
    free(arr);
}

char **get_input(FILE *f, uint *count, uint *line_len)
{
    char **res = NULL;
    char *line = NULL;
    uint capacity = 0;
    ssize_t read;
    size_t len = 0;
    while ((read = getline(&line, &len, f)) != -1)
    {
        while (read > 0 && (line[read - 1] == '\n' || line[read - 1] == '\r'))
        {
            line[--read] = '\0';
        }
        if (*count == capacity)
        {
            capacity = capacity > 0 ? (capacity * 2) : 16;
            res = (char **)realloc(res, capacity * (sizeof(char *)));
            if (!res)
            {
                perror("realloc");
                exit(EXIT_FAILURE);
            }
        }
        res[(*count)++] = line;
        *line_len = read;
        line = NULL;
        len = 0;
    }
    free(line);
    return res;
}

uint p1(char **input, uint i_len, uint l_len, bool p2)
{
    uint res = 0;
    for (int i = 0; i < i_len; i++)
    {
        for (int j = 0; j < l_len; j++)
        {
            if (input[i][j] == '@' && has_fewer_than_4_rolls(input, i, j, l_len, i_len))
            {
                res++;
                if (p2)
                {
                    input[i][j] = '.';
                }
            }
        }
    }
    return res;
}

uint p2(char **input, uint i_len, uint l_len)
{
    uint p1_res = 0, start = 0;
    while ((p1_res = p1(input, i_len, l_len, true)) != 0)
    {
        start += p1_res;
    }
    return start;
}

int main()
{
    FILE *f = fopen("input.txt", "r");
    if (!f)
    {
        fprintf(stderr, "Unable to open the file \n");
        exit(0);
    }
    uint input_len = 0, line_len = 0;
    char **input = get_input(f, &input_len, &line_len);

    uint p1_res = p1(input, input_len, line_len, false);
    uint p2_res = p2(input, input_len, line_len);

    printf("%d\n%d\n", p1_res, p2_res);

    free_input(input, input_len);
    fclose(f);
    return 0;
}