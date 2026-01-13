#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <stdbool.h>
#include <inttypes.h>
#include <ctype.h>

typedef uint32_t u32;
typedef uint64_t u64;

typedef struct Line
{
    char *line;
    size_t len;
} Line;

typedef struct List_Line
{
    Line *line;
    size_t len;
} List_Line;

typedef struct NumList
{
    u32 *nums;
    size_t capacity;
    size_t len;
} NumList;

typedef struct List_NumList
{
    NumList *list;
    size_t capacity;
    size_t len;
} List_NumList;

typedef struct Ops
{
    char *ops;
    size_t capacity;
    size_t len;
} Ops;

NumList init_NumList()
{
    NumList list = {0};
    list.capacity = 64;
    list.nums = malloc(list.capacity * sizeof *list.nums);
    return list;
}

List_NumList init_List_NumList()
{
    List_NumList list = {0};
    list.capacity = 64;
    list.list = malloc(list.capacity * sizeof *list.list);
    return list;
}

Ops init_Ops()
{
    Ops ops = {0};
    ops.capacity = 64;
    ops.ops = malloc(ops.capacity * sizeof *ops.ops);
    return ops;
}

void check_List_NumList_realloc(List_NumList *list)
{
    if (list->len == list->capacity)
    {
        size_t new_cap = list->capacity ? list->capacity * 2 : 64;
        NumList *tmp = realloc(list, new_cap * sizeof *list->list);
        if (!tmp)
        {
            perror("realloc list err");
            exit(EXIT_FAILURE);
        }
        list->list = tmp;
        list->capacity = new_cap;
    }
}

void check_NumList_realloc(NumList *nums)
{
    if (nums->len == nums->capacity)
    {
        size_t new_cap = nums->capacity ? nums->capacity * 2 : 64;
        u32 *tmp = realloc(nums->nums, new_cap * sizeof *nums->nums);
        if (!tmp)
        {
            perror("realloc list err");
            exit(EXIT_FAILURE);
        }
        nums->nums = tmp;
        nums->capacity = new_cap;
    }
}

void shrink_List_NumList(List_NumList *list)
{
    NumList *tmp = realloc(list->list, list->len * sizeof *list->list);
    if (!tmp)
    {
        perror("realloc list err");
        exit(EXIT_FAILURE);
    }
    list->list = tmp;
    list->capacity = list->len;
}

void shrink_NumList(NumList *nums)
{
    u32 *tmp = realloc(nums->nums, nums->len * sizeof *nums->nums);
    if (!tmp)
    {
        perror("realloc list err");
        exit(EXIT_FAILURE);
    }
    nums->nums = tmp;
    nums->capacity = nums->len;
}

void shrink_Ops(Ops *ops)
{
    char *tmp = realloc(ops->ops, ops->len * sizeof *ops->ops);
    if (!tmp)
    {
        perror("realloc ops err");
        exit(EXIT_FAILURE);
    }
    ops->ops = tmp;
    ops->capacity = ops->len;
}

void check_Ops_realloc(Ops *ops)
{
    if (ops->len == ops->capacity)
    {
        size_t new_cap = ops->capacity ? ops->capacity * 2 : 64;
        char *tmp = realloc(ops->ops, new_cap * sizeof *ops->ops);
        if (!tmp)
        {
            perror("realloc ops err");
            exit(EXIT_FAILURE);
        }
        ops->ops = tmp;
        ops->capacity = new_cap;
    }
}

void parse_input(FILE *f, List_NumList *list, Ops *ops)
{
    char *line = NULL;
    size_t cap = 0;
    ssize_t nread;

    while ((nread = getline(&line, &cap, f)) != -1)
    {
        check_List_NumList_realloc(list);
        char *token = strtok(line, " ");
        if (isdigit(token[0]))
        {
            NumList nums = init_NumList();
            while (token != NULL && token[0] != '\n')
            {
                check_NumList_realloc(&nums);
                u32 value = (u32)strtol(token, NULL, 10);
                nums.nums[nums.len++] = value;
                token = strtok(NULL, " ");
            }
            shrink_NumList(&nums);
            list->list[list->len++] = nums;
        }
        else
        {
            while (token != NULL && token[0] != '\n')
            {
                check_Ops_realloc(ops);
                ops->ops[ops->len++] = token[0];
                token = strtok(NULL, " ");
            }
        }
    }
    shrink_List_NumList(list);
    shrink_Ops(ops);
    free(line);
}

List_Line parse_input2(FILE *f)
{
    char *line = NULL;
    size_t cap = 0;
    ssize_t nread;

    List_Line lines = {0};
    lines.line = malloc(5 * sizeof *lines.line);

    while ((nread = getline(&line, &cap, f)) != -1)
    {
        Line l = {.line = line, .len = (size_t)nread};
        lines.line[lines.len++] = l;
        line = NULL;
        cap = 0;
    }
    free(line);

    return lines;
}

u64 form_num(List_Line *list, size_t idx_begin, size_t idx_end, char op)
{
    Line *lines = list->line;
    u64 res = op == '*' ? 1 : 0;
    for (int i = idx_begin; i < idx_end; i++)
    {
        size_t m_len = idx_end - idx_begin + 1;
        char *n = malloc(m_len * sizeof(char));
        memset(n, 0, m_len);
        size_t n_len = 0;
        for (int j = 0; j < list->len - 1; j++)
        {
            if (isdigit(lines[j].line[i]))
                n[n_len++] = lines[j].line[i];
        }
        n[n_len] = '\0';
        u64 num = (u64)strtoull(n, NULL, 10);
        res = op == '*' ? res * num : res + num;
        free(n);
    }
    return res;
}

u64 part2(List_Line *list)
{
    size_t sizes[1000] = {0};
    size_t sizes_len = 0;
    char ops[1000] = {'0'};
    size_t ops_len = 0;

    Line l_line = list->line[list->len - 1];
    char *ops_line = l_line.line;
    size_t ops_line_len = l_line.len;

    size_t op_with_padding_len = 0;
    for (int i = 0; i < ops_line_len; i++)
    {
        op_with_padding_len++;
        if (i == 0)
        {
            ops[ops_len] = ops_line[i];
            ops_len++;
        }
        else if (ops_line[i] == '+' || ops_line[i] == '*')
        {
            sizes[sizes_len] = op_with_padding_len - 2;
            sizes_len++;
            ops[ops_len] = ops_line[i];
            ops_len++;
        }
    }
    sizes[sizes_len] = op_with_padding_len - 1;
    sizes_len++;
    size_t start = 0;
    u64 res = 0;
    for (int i = 0; i < sizes_len; i++)
    {
        size_t size = sizes[i];
        char op = ops[i];
        res += form_num(list, start, size, op);
        start = size + 1;
    }

    return res;
}

u64 part1(List_NumList *l, Ops *o)
{
    u64 res = 0;
    for (size_t i = 0; i < l->list->len; i++)
    {
        char op = o->ops[i];
        size_t base = 0;
        if (op == '*')
            base = 1;
        for (size_t j = 0; j < l->len; j++)
        {
            NumList nList = l->list[j];
            if (op == '*')
                base *= nList.nums[i];
            if (op == '+')
                base += nList.nums[i];
        }
        res += base;
    }
    return res;
}

void free_List_NumList(List_NumList *list)
{
    for (int i = 0; i < list->len; i++)
    {
        free(list->list[i].nums);
    }
    free(list->list);
}

int main()
{
    FILE *f = fopen("input.txt", "r");
    if (!f)
    {
        perror("fopen input.txt");
        exit(EXIT_FAILURE);
    }
    List_NumList list = init_List_NumList();
    Ops ops = init_Ops();
    parse_input(f, &list, &ops);

    printf("%" PRIu64 "\n", part1(&list, &ops));
    free_List_NumList(&list);
    free(ops.ops);

    fseek(f, 0, SEEK_SET);
    List_Line lines = parse_input2(f);
    printf("%" PRIu64 "\n", part2(&lines));

    fclose(f);
    return 0;
}