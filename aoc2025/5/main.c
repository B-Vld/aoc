#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <stdbool.h>
#include <inttypes.h>

typedef uint64_t u64;
typedef struct Range
{
    u64 first, second;
} Range;
typedef struct InputData
{
    Range *ranges;
    size_t ranges_len;
    size_t ranges_capacity;

    u64 *ids;
    size_t ids_len;
    size_t ids_capacity;
} InputData;

static inline u64 max_u64(u64 a, u64 b) { return a > b ? a : b; }
static int cmp_range_first_then_second(const void *a, const void *b)
{
    const Range *ra = (const Range *)a;
    const Range *rb = (const Range *)b;

    if (ra->first < rb->first)
        return -1;
    if (ra->first > rb->first)
        return 1;
    return 0;
}
static int cmp_u64(const void *a, const void *b)
{
    u64 x = *(const u64 *)a;
    u64 y = *(const u64 *)b;
    return (x > y) - (x < y);
}

void print(InputData *input_data)
{
    for (size_t i = 0; i < input_data->ranges_len; i++)
    {
        printf("%" PRIu64 " -- %" PRIu64 "\n", input_data->ranges[i].first, input_data->ranges[i].second);
    }
    for (size_t i = 0; i < input_data->ids_len; i++)
    {
        printf("%" PRIu64 "\n", input_data->ids[i]);
    }
}
void free_input_data(InputData *input)
{
    if (!input)
        return;
    free(input->ranges);
    free(input->ids);
    *input = (InputData){0};
}
void get_ranges(FILE *f, InputData *data)
{
    char *line = NULL;
    size_t cap = 0;
    ssize_t nread;

    bool in_ids = false;

    while ((nread = getline(&line, &cap, f)) != -1)
    {
        while (nread > 0 && (line[nread - 1] == '\n' || line[nread - 1] == '\r'))
            line[--nread] = '\0';

        if (nread == 0)
        {
            in_ids = true;
            continue;
        }

        if (!in_ids)
        {
            u64 first, second;
            if (sscanf(line, "%" SCNu64 "-%" SCNu64, &first, &second) == 2)
            {
                if (data->ranges_len == data->ranges_capacity)
                {
                    size_t new_cap = data->ranges_capacity ? data->ranges_capacity * 2 : 16;
                    Range *tmp = realloc(data->ranges, new_cap * sizeof *data->ranges);
                    if (!tmp)
                    {
                        perror("realloc ranges");
                        exit(EXIT_FAILURE);
                    }
                    data->ranges = tmp;
                    data->ranges_capacity = new_cap;
                }

                data->ranges[data->ranges_len++] = (Range){.first = first, .second = second};
            }
            else
            {
                fprintf(stderr, "Invalid range line: '%s'\n", line);
                exit(EXIT_FAILURE);
            }
        }
        else
        {
            u64 id;
            if (sscanf(line, "%" SCNu64, &id) == 1)
            {
                if (data->ids_len == data->ids_capacity)
                {
                    size_t new_cap = data->ids_capacity ? data->ids_capacity * 2 : 16;
                    u64 *tmp = realloc(data->ids, new_cap * sizeof *data->ids);
                    if (!tmp)
                    {
                        perror("realloc ids");
                        exit(EXIT_FAILURE);
                    }
                    data->ids = tmp;
                    data->ids_capacity = new_cap;
                }
                data->ids[data->ids_len++] = id;
            }
            else
            {
                fprintf(stderr, "Invalid id line: '%s'\n", line);
                exit(EXIT_FAILURE);
            }
        }
    }

    free(line);
}
void merge_ranges(InputData *data)
{
    size_t write = 0;

    for (size_t read = 1; read < data->ranges_len; ++read)
    {
        Range *cur = &data->ranges[write];
        Range nxt = data->ranges[read];

        if (nxt.first <= cur->second)
        {
            cur->second = max_u64(cur->second, nxt.second);
        }
        else
        {
            ++write;
            data->ranges[write] = nxt;
        }
    }

    data->ranges_len = write + 1;

    Range *shrunk = realloc(data->ranges, data->ranges_len * sizeof *data->ranges);
    if (shrunk)
        data->ranges = shrunk;
    data->ranges_capacity = data->ranges_len;
}

InputData init(size_t init_capacity)
{
    InputData d = {0};

    d.ranges_capacity = init_capacity;
    d.ranges = malloc(d.ranges_capacity * sizeof *d.ranges);
    if (!d.ranges)
    {
        perror("malloc ranges");
        exit(EXIT_FAILURE);
    }

    d.ids_capacity = init_capacity;
    d.ids = malloc(d.ids_capacity * sizeof *d.ids);
    if (!d.ids)
    {
        free(d.ranges);
        perror("malloc ids");
        exit(EXIT_FAILURE);
    }

    return d;
}

u64 part1(InputData *data)
{
    u64 res = 0;
    for (size_t i = 0; i < data->ids_len; i++)
    {
        for (size_t j = 0; j < data->ranges_len; j++)
        {
            u64 num = data->ids[i];
            Range range = data->ranges[j];
            if (range.first > num)
                break;
            if (range.first <= num && num <= range.second)
            {
                res++;
                break;
            }
        }
    }
    return res;
}
u64 part2(InputData *data)
{
    u64 res = 0;
    for (size_t i = 0; i < data->ranges_len; i++)
    {
        res += data->ranges[i].second - data->ranges[i].first + 1;
    }
    return res;
}

int main()
{
    FILE *f = fopen("input.txt", "r");
    if (!f)
    {
        perror("fopen input.txt");
        exit(EXIT_FAILURE);
    }
    InputData data = init(16);
    get_ranges(f, &data);
    qsort(data.ranges, data.ranges_len, sizeof *data.ranges, cmp_range_first_then_second);
    qsort(data.ids, data.ids_len, sizeof *data.ids, cmp_u64);
    merge_ranges(&data);

    printf("%" PRIu64 "\n", part1(&data));
    printf("%" PRIu64 "\n", part2(&data));

    free_input_data(&data);
    fclose(f);
    return 0;
}